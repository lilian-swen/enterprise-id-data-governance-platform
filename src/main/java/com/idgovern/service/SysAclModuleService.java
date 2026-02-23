package com.idgovern.service;

import com.google.common.base.Preconditions;
import com.idgovern.common.RequestHolder;
import com.idgovern.dao.SysAclMapper;
import com.idgovern.dao.SysAclModuleMapper;
import com.idgovern.exception.ParamException;
import com.idgovern.model.SysAclModule;
import com.idgovern.param.AclModuleParam;
import com.idgovern.util.BeanValidator;
import com.idgovern.util.IpUtil;
import com.idgovern.util.LevelUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.time.LocalDateTime;
import java.util.List;


/**
 * ACL Module Service.
 *
 * <p>
 * Handles business operations related to ACL Modules, including
 * creation, update, hierarchical updates, and deletion of modules.
 * </p>
 *
 * <p>
 * This service ensures proper validation, hierarchical level calculation,
 * and logging of ACL module changes for audit purposes.
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author    | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-03-01 | Lilian S.| Initial creation                |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */
@Service
@Slf4j
public class SysAclModuleService {

    @Resource
    private SysAclModuleMapper sysAclModuleMapper;

    @Resource
    private SysAclMapper sysAclMapper;

    @Resource
    private SysLogService sysLogService;

    /**
     * Create a new ACL Module.
     *
     * @param param input parameters for creating an ACL module
     * @throws ParamException if a module with the same name exists in the same hierarchy
     */
    @Transactional // Ensure the whole save operation is atomic
    public void save(AclModuleParam param) {

        BeanValidator.check(param);

        if(checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("A module with the same name already exists in this level");
        }

        SysAclModule aclModule = SysAclModule.builder()
                .name(param.getName())
                .parentId(param.getParentId())
                .seq(param.getSeq())
                .status(param.getStatus())
                .remark(param.getRemark())
                .build();

        // Compute hierarchical level
        aclModule.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        aclModule.setOperator(RequestHolder.getCurrentUser().getUsername());
        aclModule.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        aclModule.setOperateTime(LocalDateTime.now());

        sysAclModuleMapper.insertSelective(aclModule);

        // Save operation log
        sysLogService.saveAclModuleLog(null, aclModule);
    }


    /**
     * Update an existing ACL Module.
     *
     * @param param input parameters for updating an ACL module
     * @throws ParamException if a module with the same name exists in the same hierarchy
     */
    @Transactional // Fixes the internal call proxy issue
    public void update(AclModuleParam param) {

        BeanValidator.check(param);

        if(checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("A module with the same name already exists in this level");
        }

        SysAclModule before = sysAclModuleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "The ACL module to be updated does not exist");

        SysAclModule after = SysAclModule.builder()
                .id(param.getId())
                .name(param.getName())
                .parentId(param.getParentId())
                .seq(param.getSeq())
                .status(param.getStatus())
                .remark(param.getRemark())
                .build();

        // Compute hierarchical level
        after.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(LocalDateTime.now());

        // Update module along with any child modules if level changes
        updateWithChild(before, after);

        sysLogService.saveAclModuleLog(before, after);
    }

    /**
     * Update an ACL module and recursively update child modules' levels if the parent level changes.
     *
     * @param before the original ACL module
     * @param after  the updated ACL module
     */
    @Transactional // 如果要保证事务生效，需要调整这个方法，一个可行的方法是重新创建一个service类，然后把这个方法转移过去
    public void updateWithChild(SysAclModule before, SysAclModule after) {

        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();

        if (!after.getLevel().equals(before.getLevel())) {
            String curLevel = before.getLevel() + "." + before.getId();
            List<SysAclModule> aclModuleList = sysAclModuleMapper.getChildAclModuleListByLevel(curLevel + "%");

            if (!CollectionUtils.isEmpty(aclModuleList)) {
                for (SysAclModule aclModule : aclModuleList) {
                    String level = aclModule.getLevel();
                    if (level.equals(curLevel) || level.indexOf(curLevel + ".") == 0) {
                        // getChildAclModuleListByLevel可能会取出多余的内容，因此需要加个判断
                        // 比如0.1* 可能取出0.1、0.1.3、0.11、0.11.3，而期望取出  0.1、0.1.3， 因此呢需要判断等于0.1或者以0.1.为前缀才满足条件
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        aclModule.setLevel(level);
                    }
                }
                sysAclModuleMapper.batchUpdateLevel(aclModuleList);
            }
        }
        sysAclModuleMapper.updateByPrimaryKeySelective(after);
    }


    /**
     * Check if an ACL module with the same name exists at the same parent level.
     *
     * @param parentId     parent module ID
     * @param aclModuleName module name to check
     * @param deptId     department ID
     * @return true if exists, false otherwise
     */
    private boolean checkExist(Integer parentId, String aclModuleName, Integer deptId) {
        return sysAclModuleMapper.countByNameAndParentId(parentId, aclModuleName, deptId) > 0;
    }


    /**
     * Get the hierarchical level of a given ACL module ID.
     *
     * @param aclModuleId ACL module ID
     * @return level string, e.g., "0.1.3"
     */
    private String getLevel(Integer aclModuleId) {
        SysAclModule aclModule = sysAclModuleMapper.selectByPrimaryKey(aclModuleId);
        if (aclModule == null) {
            return null;
        }
        return aclModule.getLevel();
    }


    /**
     * Delete an ACL module by ID.
     *
     * @param aclModuleId ID of the module to delete
     * @throws ParamException if the module has child modules or associated ACLs
     */
    public void delete(int aclModuleId) {

        SysAclModule aclModule = sysAclModuleMapper.selectByPrimaryKey(aclModuleId);
        Preconditions.checkNotNull(aclModule, "The ACL module to delete does not exist");

        if(sysAclModuleMapper.countByParentId(aclModule.getId()) > 0) {
            throw new ParamException("Cannot delete module with child modules");
        }

        if (sysAclMapper.countByAclModuleId(aclModule.getId()) > 0) {
            throw new ParamException("Cannot delete module with associated ACL points");
        }
        sysAclModuleMapper.deleteByPrimaryKey(aclModuleId);
    }

}
