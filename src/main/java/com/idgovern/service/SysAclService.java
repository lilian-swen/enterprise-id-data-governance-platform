package com.idgovern.service;

import com.google.common.base.Preconditions;
import com.idgovern.dto.PageQuery;
import com.idgovern.dto.PageResult;
import com.idgovern.common.RequestHolder;
import com.idgovern.dao.SysAclMapper;
import com.idgovern.exception.ParamException;
import com.idgovern.model.SysAcl;
import com.idgovern.param.AclParam;
import com.idgovern.util.BeanValidator;
import com.idgovern.util.IpUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * ACL Service.
 *
 * <p>
 * Handles business operations related to Access Control List (ACL) points,
 * including creation, update, validation, pagination, and code generation.
 * </p>
 *
 * <p>
 * Ensures that ACL names are unique within a module, validates input parameters,
 * calculates unique codes, and records operation logs for audit purposes.
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author    | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-03-01 | Lilian S.| Initial creation                |
 * | 1.1     | 2016-03-01 | Lilian S.| Code review and improving       |
 * | 1). Enhanced logging and observability by integrating Lombok's @Slf4j. |
 * | 2). Strengthened data consistency through Spring's @Transactional to ensure atomic operations.|
 * | 3). Improved concurrency safety by removing non-thread-safe SimpleDateFormat and implementing UUID-based identifier generation to avoid collision under high concurrency.|
 * | 4). Integrated AOP with Java 17 and Spring Boot 3. TODO: Clean up manual logging in SysAclService|
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */
@Service
@Slf4j
public class SysAclService {

    @Resource
    private SysAclMapper sysAclMapper;

    @Resource
    private SysLogService sysLogService;


    /**
     * Create a new ACL point.
     * Update:
     * @param param input parameters for creating ACL
     * @throws ParamException if a point with the same name exists under the same module
     */
    @Transactional
    public void save(AclParam param) {

        log.info("Receive request to create ACL. Name: [{}], ModuleId: [{}]", param.getName(), param.getAclModuleId());

        BeanValidator.check(param);

        if (checkExist(param.getAclModuleId(), param.getName(), param.getId())) {

            log.warn("ACL creation failed: Name already exists. Name: [{}], ModuleId: [{}]",
                    param.getName(), param.getAclModuleId());

            throw new ParamException("An ACL with the same name already exists in this module");
        }

        SysAcl acl = SysAcl.builder()
                .name(param.getName())
                .aclModuleId(param.getAclModuleId())
                .url(param.getUrl())
                .type(param.getType())
                .status(param.getStatus())
                .seq(param.getSeq())
                .remark(param.getRemark())
                .build();

        // Generate unique code for the ACL point
        acl.setCode(generateCode());
        acl.setOperator(RequestHolder.getCurrentUser().getUsername());
        acl.setOperateTime(new Date());
        acl.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));

        log.debug("Persisting new SysAcl entity: {}", acl);
        sysAclMapper.insertSelective(acl);

        // Save operation log
        sysLogService.saveAclLog(null, acl);
        log.info("Successfully created ACL. ID: [{}], Code: [{}]", acl.getId(), acl.getCode());

    }


    /**
     * Update an existing ACL point.
     *
     * @param param input parameters for updating ACL
     * @throws ParamException if a point with the same name exists under the same module
     */
    @Transactional
    public void update(AclParam param) {

        BeanValidator.check(param);

        if (checkExist(param.getAclModuleId(), param.getName(), param.getId())) {
            throw new ParamException("An ACL with the same name already exists in this module");
        }

        SysAcl before = sysAclMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "The ACL to update does not exist");

        SysAcl after = SysAcl.builder()
                .id(param.getId())
                .name(param.getName())
                .aclModuleId(param.getAclModuleId())
                .url(param.getUrl())
                .type(param.getType())
                .status(param.getStatus())
                .seq(param.getSeq())
                .remark(param.getRemark())
                .build();

        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateTime(new Date());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));

        sysAclMapper.updateByPrimaryKeySelective(after);
        sysLogService.saveAclLog(before, after);
    }


    /**
     * Check if an ACL point with the same name exists under a specific module.
     *
     * @param aclModuleId module ID
     * @param name        ACL point name
     * @param id          current ACL ID (optional, for updates)
     * @return true if an ACL exists with the same name, false otherwise
     */
    public boolean checkExist(int aclModuleId, String name, Integer id) {
        return sysAclMapper.countByNameAndAclModuleId(aclModuleId, name, id) > 0;
    }


    /**
     * Generate a unique ACL code.
     *
     * <p>
     * The code format is yyyyMMddHHmmss_randomNumber (0-99) to ensure uniqueness.
     * 2026-02-21, Lilian S.: Improve generateCode() logic for Integration with Distributed Systems
     * </p>
     *
     * @return generated ACL code
     */
    public String generateCode() {

        // Generates a 36-character unique string like: 550e8400-e29b-41d4-a716-446655440000
        return UUID.randomUUID().toString().replace("-", "");
    }


    /**
     * Get a paginated list of ACL points under a specific module.
     *
     * @param aclModuleId module ID
     * @param page        pagination query
     * @return PageResult containing ACL points and total count
     */
    public PageResult<SysAcl> getPageByAclModuleId(int aclModuleId, PageQuery page) {

        BeanValidator.check(page);

        // 1. Get the total count
        int count = sysAclMapper.countByAclModuleId(aclModuleId);

        if (count > 0) {

            // 2. Get the list of data
            List<SysAcl> aclList = sysAclMapper.getPageByAclModuleId(aclModuleId, page);

            // 3. Wrap it in our standard envelope
            return PageResult.<SysAcl>builder()
                    .data(aclList)
                    .total(count)
                    .pageNo(page.getPageNo())   // Echo back the page number
                    .pageSize(page.getPageSize()) // Echo back the page size
                    .build();
        }

        return PageResult.<SysAcl>builder().build(); // Empty result
    }
}
