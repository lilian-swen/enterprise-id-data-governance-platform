package com.idgovern.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.idgovern.common.RequestHolder;
import com.idgovern.dao.SysRoleAclMapper;
import com.idgovern.dao.SysRoleMapper;
import com.idgovern.dao.SysRoleUserMapper;
import com.idgovern.dao.SysUserMapper;
import com.idgovern.exception.ParamException;
import com.idgovern.model.SysRole;
import com.idgovern.model.SysUser;
import com.idgovern.param.RoleParam;
import com.idgovern.util.BeanValidator;
import com.idgovern.util.IpUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Role Service.
 *
 * <p>
 * Provides business logic for role management within the
 * RBAC (Role-Based Access Control) system.
 * </p>
 *
 * <p>
 * Responsibilities include role creation, update,
 * validation, relationship retrieval (role-user / role-acl),
 * and audit logging.
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author    | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-02-23 | Lilian S. | Initial creation                |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */
@Service
public class SysRoleService {

    /**
     * Role data access layer.
     */
    @Resource
    private SysRoleMapper sysRoleMapper;

    /**
     * Role-User relationship mapper.
     */
    @Resource
    private SysRoleUserMapper sysRoleUserMapper;

    /**
     * Role-Permission (ACL) relationship mapper.
     */
    @Resource
    private SysRoleAclMapper sysRoleAclMapper;

    /**
     * User data access layer.
     */
    @Resource
    private SysUserMapper sysUserMapper;

    /**
     * System audit logging service.
     */
    @Resource
    private SysLogService sysLogService;

    /**
     * Creates a new role.
     *
     * @param param role request parameters
     */
    public void save(RoleParam param) {

        // Validate request parameters
        BeanValidator.check(param);

        // Check role name uniqueness
        if (checkExist(param.getName(), param.getId())) {
            throw new ParamException("Role name already exists");
        }

        // Build role entity
        SysRole role = SysRole.builder()
                .name(param.getName())
                .status(param.getStatus())
                .type(param.getType())
                .remark(param.getRemark())
                .build();

        // Set audit information
        role.setOperator(RequestHolder.getCurrentUser().getUsername());
        role.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        role.setOperateTime(new Date());

        // Persist role
        sysRoleMapper.insertSelective(role);

        // Record audit log
        sysLogService.saveRoleLog(null, role);
    }

    /**
     * Updates an existing role.
     *
     * @param param role update parameters
     */
    public void update(RoleParam param) {

        // Validate request parameters
        BeanValidator.check(param);

        // Check role name uniqueness
        if (checkExist(param.getName(), param.getId())) {
            throw new ParamException("Role name already exists");
        }

        // Retrieve existing role
        SysRole before = sysRoleMapper.selectByPrimaryKey(param.getId());
        Preconditions.checkNotNull(before, "The role to be updated does not exist");

        SysRole after = SysRole.builder()
                .id(param.getId())
                .name(param.getName())
                .status(param.getStatus())
                .type(param.getType())
                .remark(param.getRemark())
                .build();

        // Set audit information
        after.setOperator(RequestHolder.getCurrentUser().getUsername());
        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());

        // Update role
        sysRoleMapper.updateByPrimaryKeySelective(after);

        // Record audit log (before & after comparison)
        sysLogService.saveRoleLog(before, after);
    }

    /**
     * Retrieves all roles.
     *
     * @return list of roles
     */
    public List<SysRole> getAll() {
        return sysRoleMapper.getAll();
    }

    /**
     * Checks whether a role name already exists.
     *
     * @param name role name
     * @param id   role ID (used to exclude current record during update)
     * @return true if exists, false otherwise
     */
    private boolean checkExist(String name, Integer id) {
        return sysRoleMapper.countByName(name, id) > 0;
    }

    /**
     * Retrieves roles assigned to a specific user.
     *
     * @param userId user identifier
     * @return list of roles
     */
    public List<SysRole> getRoleListByUserId(int userId) {

        List<Integer> roleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);

        if (CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }

        return sysRoleMapper.getByIdList(roleIdList);

    }

    /**
     * Retrieves roles associated with a specific permission (ACL).
     *
     * @param aclId permission identifier
     * @return list of roles
     */
    public List<SysRole> getRoleListByAclId(int aclId) {

        List<Integer> roleIdList = sysRoleAclMapper.getRoleIdListByAclId(aclId);

        if (CollectionUtils.isEmpty(roleIdList)) {
            return Lists.newArrayList();
        }

        return sysRoleMapper.getByIdList(roleIdList);

    }


    /**
     * Retrieves users assigned to a list of roles.
     *
     * @param roleList list of roles
     * @return list of users
     */
    public List<SysUser> getUserListByRoleList(List<SysRole> roleList) {

        if (CollectionUtils.isEmpty(roleList)) {
            return Lists.newArrayList();
        }

        // Extract role IDs
        List<Integer> roleIdList = roleList.stream().map(role -> role.getId()).collect(Collectors.toList());

        // Retrieve user IDs by role IDs
        List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleIdList(roleIdList);

        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }

        // Retrieve user entities
        return sysUserMapper.getByIdList(userIdList);
    }
}
