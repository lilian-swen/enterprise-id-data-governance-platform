package com.idgovern.service;

import com.google.common.base.Preconditions;
import com.idgovern.common.RequestHolder;
import com.idgovern.dao.SysDeptMapper;
import com.idgovern.dao.SysUserMapper;
import com.idgovern.exception.ParamException;
import com.idgovern.model.SysDept;
import com.idgovern.param.DeptParam;
import com.idgovern.util.BeanValidator;
import com.idgovern.util.IpUtil;
import com.idgovern.util.LevelUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Service class for managing departments within the RBAC system.
 *
 * <p>
 * Provides operations for creating, updating, deleting, and managing
 * hierarchical departments. Includes validation, level calculation,
 * and logging for audit purposes.
 * </p>
 *
 * <p>
 * Business Rules:
 * <ul>
 *     <li>Department names must be unique within the same hierarchy level.</li>
 *     <li>Departments may have child departments, forming a hierarchy.</li>
 *     <li>Root-level departments are identified by a null or 0 parentId.</li>
 * </ul>
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author   | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-02-16 | Lilian S.| Initial creation                |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */

@Service
public class SysDeptService {

    @Resource
    private SysDeptMapper sysDeptMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysLogService sysLogService;

    /**
     * Saves a new department.
     *
     * @param param validated department request object
     * @throws ParamException if a department with the same name exists at the same level
     */
    public void save(DeptParam param) {

        // Validate input parameters
        BeanValidator.check(param);

        // Check for duplicate department name under the same parent
        if(checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("A department with the same name already exists at this level.");
        }

        // Build the SysDept entity
        SysDept dept = SysDept.builder()
                .name(param.getName())
                .parentId(param.getParentId())
                .seq(param.getSeq())
                .remark(param.getRemark())
                .build();

        // Calculate hierarchical level
        dept.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        dept.setOperator(RequestHolder.getCurrentUser().getUsername());
        dept.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        dept.setOperateTime(LocalDateTime.now());

        // Persist department
        sysDeptMapper.insertSelective(dept);

        // Log creation for auditing
        sysLogService.saveDeptLog(null, dept);
    }

    /**
     * Updates an existing department and its child departments if the hierarchy changes.
     *
     * @param param validated department request object
     * @throws ParamException if a department with the same name exists at the same level
     * @throws IllegalArgumentException if the department does not exist
     * 2016-02-16
     */
    public void update(DeptParam param) {

        BeanValidator.check(param);

        if(checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("A department with the same name already exists at this level.");
        }

        // Build the updated department entity
        SysDept before = sysDeptMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "The department you’re trying to update does not exist.");

        SysDept after = SysDept.builder()
                .id(param.getId())
                .name(param.getName())
                .parentId(param.getParentId())
                .seq(param.getSeq())
                .remark(param.getRemark())
                .build();

        after.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(LocalDateTime.now());

        // Update the department and child levels if hierarchy changed
        updateWithChild(before, after);

        // Log the update for auditing
        sysLogService.saveDeptLog(before, after);
    }


    /**
     * Updates a department and all its child departments if the hierarchical level changes.
     *
     * <p>
     * This method is transactional to ensure consistency of the department hierarchy.
     * </p>
     *
     * @param before the existing department before update
     * @param after the updated department after changes
     *
     *              2016-02-16
     */
    @Transactional
    public void updateWithChild(SysDept before, SysDept after) {
        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();

        // Only update child departments if the level has changed
        if (!after.getLevel().equals(before.getLevel())) {
            String curLevel = before.getLevel() + "." + before.getId();
            List<SysDept> deptList = sysDeptMapper.getChildDeptListByLevel(curLevel + "%");

            if (!CollectionUtils.isEmpty(deptList)) {
                for (SysDept dept : deptList) {
                    String level = dept.getLevel();

                    // Only update levels that match exactly or are direct children
                    if (level.equals(curLevel) || level.indexOf(curLevel + ".") == 0) {
                        // getChildAclModuleListByLevel可能会取出多余的内容，因此需要加个判断
                        // 比如0.1* 可能取出0.1、0.1.3、0.11、0.11.3，而期望取出  0.1、0.1.3， 因此呢需要判断等于0.1或者以0.1.为前缀才满足条件
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        dept.setLevel(level);
                    }
                }
                // Batch update child department levels
                sysDeptMapper.batchUpdateLevel(deptList);
            }
        }
        // Update the current department
        sysDeptMapper.updateByPrimaryKey(after);
    }


    /**
     * Checks if a department with the same name exists under the same parent.
     *
     * @param parentId parent department ID
     * @param deptName name of the department
     * @param deptId ID of the department to exclude (useful for updates)
     * @return true if a duplicate exists, false otherwise
     * 2016-02-16
     */
    private boolean checkExist(Integer parentId, String deptName, Integer deptId) {
        return sysDeptMapper.countByNameAndParentId(parentId, deptName, deptId) > 0;
    }


    /**
     * Retrieves the hierarchical level of a department by its ID.
     *
     * @param deptId department ID
     * @return hierarchical level string, or null if department does not exist
     */
    private String getLevel(Integer deptId) {

        SysDept dept = sysDeptMapper.selectByPrimaryKey(deptId);

        if (dept == null) {
            return null;
        }

        return dept.getLevel();
    }


    /**
     * Deletes a department.
     *
     * <p>
     * A department cannot be deleted if it has child departments or assigned users.
     * </p>
     *
     * @param deptId ID of the department to delete
     * @throws ParamException if department has children or users
     * @throws IllegalArgumentException if department does not exist
     */
    public void delete(int deptId) {
        SysDept dept = sysDeptMapper.selectByPrimaryKey(deptId);
        Preconditions.checkNotNull(dept, "The department to delete does not exist.");
        if (sysDeptMapper.countByParentId(dept.getId()) > 0) {
            throw new ParamException("Cannot delete department because it has child departments.");
        }
        if(sysUserMapper.countByDeptId(dept.getId()) > 0) {
            throw new ParamException("Cannot delete department because it has assigned users.");
        }
        sysDeptMapper.deleteByPrimaryKey(deptId);
    }
}
