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
import static net.logstash.logback.argument.StructuredArguments.kv;

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
 * | 1.1     | 2016-02-23 | Lilian S.| ACL Module Service with structured professional logging. |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.1
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

        log.info("Request to create ACL Module",
                kv("event", "ACL_MODULE_CREATE"),
                kv("name", param.getName()),
                kv("parentId", param.getParentId()));

        BeanValidator.check(param);

        if(checkExist(param.getParentId(), param.getName(), param.getId())) {

            log.warn("ACL Module creation failed: duplicate name",
                    kv("event", "ACL_MODULE_CREATE_FAIL"),
                    kv("name", param.getName()),
                    kv("parentId", param.getParentId()));

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

        log.debug("Persisting new ACL Module", kv("aclModule", aclModule));
        sysAclModuleMapper.insertSelective(aclModule);

        // Save operation log
        sysLogService.saveAclModuleLog(null, aclModule);

        log.info("Successfully created ACL Module",
                kv("event", "ACL_MODULE_CREATE_SUCCESS"),
                kv("aclModuleId", aclModule.getId()));
    }


    /**
     * Update an existing ACL Module.
     *
     * @param param input parameters for updating an ACL module
     * @throws ParamException if a module with the same name exists in the same hierarchy
     */
    @Transactional // Fixes the internal call proxy issue
    public void update(AclModuleParam param) {

        log.info("Request to update ACL Module",
                kv("event", "ACL_MODULE_UPDATE"),
                kv("aclModuleId", param.getId()),
                kv("name", param.getName()));

        BeanValidator.check(param);

        if(checkExist(param.getParentId(), param.getName(), param.getId())) {

            log.warn("ACL Module update failed: duplicate name",
                    kv("event", "ACL_MODULE_UPDATE_FAIL"),
                    kv("aclModuleId", param.getId()),
                    kv("name", param.getName()));

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

        log.debug("Updating ACL Module with children if level changes",
                kv("before", before),
                kv("after", after));

        // Update module along with any child modules if level changes
        updateWithChild(before, after);

        sysLogService.saveAclModuleLog(before, after);

        log.info("Successfully updated ACL Module",
                kv("event", "ACL_MODULE_UPDATE_SUCCESS"),
                kv("aclModuleId", after.getId()));
    }

    /**
     * Update an ACL module and recursively update child modules' levels if the parent level changes.
     * TODO:
     * To guarantee that the transactional behavior is applied correctly, the method should be refactored.
     * A practical approach is to extract it into a new service class so that it can be invoked through the Spring proxy.
     *
     * @param before the original ACL module
     * @param after  the updated ACL module
     */
    @Transactional
    public void updateWithChild(SysAclModule before, SysAclModule after) {

        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();

        if (!after.getLevel().equals(before.getLevel())) {
            String curLevel = before.getLevel() + "." + before.getId();
            List<SysAclModule> aclModuleList = sysAclModuleMapper.getChildAclModuleListByLevel(curLevel + "%");
            // getChildAclModuleListByLevel may return extra records, so an additional check is required.
            // For example, querying with "0.1*" might return:
            // 0.1, 0.1.3, 0.11, 0.11.3
            // However, the expected results are only: 0.1 and 0.1.3
            // Therefore, we need to ensure that the value is either exactly "0.1"
            // or starts with the prefix "0.1." to meet the condition.

            if (!CollectionUtils.isEmpty(aclModuleList)) {
                for (SysAclModule aclModule : aclModuleList) {
                    String level = aclModule.getLevel();
                    // an additional check
                    if (level.equals(curLevel) || level.indexOf(curLevel + ".") == 0) {
                        level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                        aclModule.setLevel(level);
                    }
                }
                sysAclModuleMapper.batchUpdateLevel(aclModuleList);
                log.debug("Updated child ACL Module levels", kv("childModules", aclModuleList.size()));
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

        boolean exists = sysAclModuleMapper.countByNameAndParentId(parentId, aclModuleName, deptId) > 0;
        log.debug("Checking ACL Module existence",
                kv("name", aclModuleName),
                kv("parentId", parentId),
                kv("exists", exists));

        return exists;
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

            log.warn("Cannot delete ACL Module: has child modules", kv("aclModuleId", aclModuleId));
            throw new ParamException("Cannot delete module with child modules");
        }

        if (sysAclMapper.countByAclModuleId(aclModule.getId()) > 0) {

            log.warn("Cannot delete ACL Module: has associated ACL points", kv("aclModuleId", aclModuleId));
            throw new ParamException("Cannot delete module with associated ACL points");
        }

        sysAclModuleMapper.deleteByPrimaryKey(aclModuleId);
        log.info("Successfully deleted ACL Module", kv("event", "ACL_MODULE_DELETE_SUCCESS"), kv("aclModuleId", aclModuleId));
    }

}
