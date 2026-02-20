package com.idgovern.service;

import com.google.common.base.Preconditions;
import com.idgovern.beans.PageQuery;
import com.idgovern.beans.PageResult;
import com.idgovern.common.RequestHolder;
import com.idgovern.dao.SysAclMapper;
import com.idgovern.exception.ParamException;
import com.idgovern.model.SysAcl;
import com.idgovern.param.AclParam;
import com.idgovern.util.BeanValidator;
import com.idgovern.util.IpUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */
@Service
public class SysAclService {

    @Resource
    private SysAclMapper sysAclMapper;

    @Resource
    private SysLogService sysLogService;


    /**
     * Create a new ACL point.
     *
     * @param param input parameters for creating ACL
     * @throws ParamException if a point with the same name exists under the same module
     */
    public void save(AclParam param) {

        BeanValidator.check(param);
        if (checkExist(param.getAclModuleId(), param.getName(), param.getId())) {
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

        sysAclMapper.insertSelective(acl);

        // Save operation log
        sysLogService.saveAclLog(null, acl);

    }


    /**
     * Update an existing ACL point.
     *
     * @param param input parameters for updating ACL
     * @throws ParamException if a point with the same name exists under the same module
     */
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
     * </p>
     *
     * @return generated ACL code
     */
    public String generateCode() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(new Date()) + "_" + (int)(Math.random() * 100);
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
        int count = sysAclMapper.countByAclModuleId(aclModuleId);

        if (count > 0) {
            List<SysAcl> aclList = sysAclMapper.getPageByAclModuleId(aclModuleId, page);
            return PageResult.<SysAcl>builder().data(aclList).total(count).build();
        }
        return PageResult.<SysAcl>builder().build();
    }
}
