package com.idgovern.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.idgovern.beans.CacheKeyConstants;
import com.idgovern.common.RequestHolder;
import com.idgovern.dao.SysAclMapper;
import com.idgovern.dao.SysRoleAclMapper;
import com.idgovern.dao.SysRoleUserMapper;
import com.idgovern.model.SysAcl;
import com.idgovern.model.SysUser;
import com.idgovern.util.JsonMapper;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Core System Service.
 *
 * <p>
 * Responsible for central permission and role management logic:
 * retrieving user ACLs, role ACLs, determining access rights,
 * caching ACLs for performance optimization, and checking
 * super administrator privileges.
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
public class SysCoreService {

    @Resource
    private SysAclMapper sysAclMapper;

    @Resource
    private SysRoleUserMapper sysRoleUserMapper;

    @Resource
    private SysRoleAclMapper sysRoleAclMapper;

    @Resource
    private SysCacheService sysCacheService;


    /**
     * Retrieve ACL list for the currently logged-in user.
     *
     * @return list of permissions assigned to the current user
     */
    public List<SysAcl> getCurrentUserAclList() {
        int userId = RequestHolder.getCurrentUser().getId();
        return getUserAclList(userId);
    }


    /**
     * Retrieve ACL list associated with a specific role.
     *
     * @param roleId role identifier
     * @return list of ACLs assigned to the role
     */
    public List<SysAcl> getRoleAclList(int roleId) {
        List<Integer> aclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(Lists.<Integer>newArrayList(roleId));

        if (CollectionUtils.isEmpty(aclIdList)) {
            return Lists.newArrayList();
        }

        return sysAclMapper.getByIdList(aclIdList);
    }


    /**
     * Retrieve ACL list for a specified user.
     *
     * <p>
     * Permission resolution flow:
     * <ol>
     *     <li>If user is super admin → return all ACLs.</li>
     *     <li>Fetch user's role IDs.</li>
     *     <li>Fetch ACL IDs from roles.</li>
     *     <li>Return corresponding ACL entities.</li>
     * </ol>
     * </p>
     *
     * @param userId user identifier
     * @return list of ACLs assigned to the user
     */
    public List<SysAcl> getUserAclList(int userId) {

        if (isSuperAdmin()) {
            return sysAclMapper.getAll();
        }

        List<Integer> userRoleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);

        if (CollectionUtils.isEmpty(userRoleIdList)) {
            return Lists.newArrayList();
        }

        List<Integer> userAclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(userRoleIdList);

        if (CollectionUtils.isEmpty(userAclIdList)) {
            return Lists.newArrayList();
        }

        return sysAclMapper.getByIdList(userAclIdList);
    }


    /**
     * Determine whether the current user is a super administrator.
     *
     * <p>
     * NOTE:
     * This is a simplified rule for demonstration purposes.
     * In production systems, super admin determination should
     * be based on configuration, role assignment, or database flags.
     * </p>
     *
     * @return true if current user is super admin
     */
    public boolean isSuperAdmin() {

        // Here I defined a mock super administrator rule myself.
        // In practice, this should be modified according to the specific project requirements.
        // It can be loaded from a configuration file, or you can designate a specific user or a specific role.
        SysUser sysUser = RequestHolder.getCurrentUser();
        if (sysUser.getMail().contains("admin")) {
            return true;
        }
        return false;
    }


    /**
     * Check whether the current user has permission to access a given URL.
     *
     * <p>
     * Authorization logic:
     * <ul>
     *     <li>Super admin → always allowed.</li>
     *     <li>If no ACL is configured for the URL → allowed.</li>
     *     <li>If at least one valid ACL matches user's permissions → allowed.</li>
     *     <li>If all ACLs are invalid → allowed.</li>
     *     <li>Otherwise → denied.</li>
     * </ul>
     * </p>
     *
     * @param url requested URL
     * @return true if access is permitted
     */
    public boolean hasUrlAcl(String url) {

        if (isSuperAdmin()) {
            return true;
        }

        List<SysAcl> aclList = sysAclMapper.getByUrl(url);
        if (CollectionUtils.isEmpty(aclList)) {
            return true;
        }

        List<SysAcl> userAclList = getCurrentUserAclListFromCache();
        Set<Integer> userAclIdSet = userAclList.stream()
                .map(acl -> acl.getId()).
                collect(Collectors.toSet());

        boolean hasValidAcl = false;

        // 规则：只要有一个权限点有权限，那么我们就认为有访问权限
        for (SysAcl acl : aclList) {
            // 判断一个用户是否具有某个权限点的访问权限
            if (acl == null || acl.getStatus() != 1) { // 权限点无效
                continue;
            }

            hasValidAcl = true;

            if (userAclIdSet.contains(acl.getId())) {
                return true;
            }

        }

        if (!hasValidAcl) {
            return true;
        }

        return false;
    }


    /**
     * Retrieve current user's ACL list using cache optimization.
     *
     * <p>
     * Cache strategy:
     * <ul>
     *     <li>Cache key: USER_ACLS_userId</li>
     *     <li>TTL: 600 seconds</li>
     *     <li>Cache miss → load from DB and cache result</li>
     * </ul>
     * </p>
     *
     * @return list of ACLs assigned to current user
     */
    public List<SysAcl> getCurrentUserAclListFromCache() {
        int userId = RequestHolder.getCurrentUser().getId();
        String cacheValue = sysCacheService.getFromCache(CacheKeyConstants.USER_ACLS, String.valueOf(userId));
        if (StringUtils.isBlank(cacheValue)) {
            List<SysAcl> aclList = getCurrentUserAclList();
            if (!CollectionUtils.isEmpty(aclList)) {
                sysCacheService.saveCache(JsonMapper.obj2String(aclList), 600, CacheKeyConstants.USER_ACLS, String.valueOf(userId));
            }
            return aclList;
        }
        return JsonMapper.string2Obj(cacheValue, new TypeReference<List<SysAcl>>() {
        });
    }
}
