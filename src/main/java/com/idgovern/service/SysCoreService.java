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
import java.util.Random;
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
 * | 1.1     | 2026-02-28 | Lilian S.| Enhanced high-concurrency resilience
 * by applying a Double-Checked Locking (DCL) strategy to user-level cache keys,
 * mitigating cache breakdown (hot key storm) caused by synchronized cache expiration under heavy traffic. |
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

    @Resource
    private RedisService redisService;


    /**
     * Fetch the Access Control List (ACL) associated with the currently authenticated user session.
     *
     * @return list of permissions assigned to the current user
     */
    public List<SysAcl> getCurrentUserAclList() {

        int userId = RequestHolder.getCurrentUser().getId();
        return getUserAclList(userId);
    }


    /**
     * Fetch ACL permissions assigned to a specific role in batch.
     *
     * @param roleId role identifier
     * @return list of ACLs assigned to the role
     */
    public List<SysAcl> getRoleAclList(int roleId) {

        List<Integer> aclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(Lists.<Integer>newArrayList(roleId));

        if (CollectionUtils.isEmpty(aclIdList)) {
            // Returning an empty list instead of null to prevent NullPointerException
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
     *
     * !!! Note: In a high-concurrency environment like Real-Time Fraud Detection,
     * running these 3–4 database queries for every single user request is expensive.
     *
     * Common Optimization:
     * Once this list is generated, it is often cached in Redis using a key like user_acl_{userId}.
     * The cache would be cleared (evicted) only when the user's roles are updated or a permission is changed in the admin panel.
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


//    -------------- 2/28/2026 concurrency solution for getUserAclList Start ---------------
    /**
     * Retrieves the complete list of Access Control entries for a specific user,
     * leveraging a multi-level resolution strategy (Super Admin check -> Redis Cache -> Database).
     *
     * <p>Optimized for high-concurrency environments using a Double-Checked Locking pattern
     * on the user-specific cache key to prevent "Cache Breakdown" (Hotkey storm) when
     * a cache entry expires under heavy load.</p>
     *
     * @param userId The unique identifier of the user.
     * @return A {@link List} of {@link SysAcl} objects assigned to the user; returns an empty list if no permissions are found.
     */
    public List<SysAcl> getUserAclListWithCache(int userId) {
        String cacheKey = "user_acl_" + userId;

        // 1. Attempt initial cache retrieval
        List<SysAcl> cachedAcls = redisService.getList(cacheKey, SysAcl.class);
        if (!CollectionUtils.isEmpty(cachedAcls)) {
            return cachedAcls;
        }

        // 2. Synchronize on the interned string to ensure thread-safety per user
        synchronized (cacheKey.intern()) {

            // Double-check cache to see if another thread populated it while this thread was waiting
            cachedAcls = redisService.getList(cacheKey, SysAcl.class);
            if (!CollectionUtils.isEmpty(cachedAcls)) {
                return cachedAcls;
            }

            // 3. Handle Super Admin privilege (Short-circuit DB lookups)
            if (isSuperAdmin()) {
                return sysAclMapper.getAll();
            }

            // 4. Resolve permissions through RBAC hierarchy
            List<Integer> userRoleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
            if (CollectionUtils.isEmpty(userRoleIdList)) {
                return handleEmptyAclCache(cacheKey);
            }

            List<Integer> userAclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(userRoleIdList);
            if (CollectionUtils.isEmpty(userAclIdList)) {
                return handleEmptyAclCache(cacheKey);
            }

            List<SysAcl> aclList = sysAclMapper.getByIdList(userAclIdList);

            // 5. Populate Redis Cache with a TTL (1 hour + random jitter to prevent Cache Avalanche)
            if (!CollectionUtils.isEmpty(aclList)) {
                long ttl = 3600 + new Random().nextInt(300);
                redisService.setEx(cacheKey, JsonMapper.obj2String(aclList), ttl);
            } else {
                handleEmptyAclCache(cacheKey);
            }

            return aclList;
        }
    }

    /**
     * Prevents "Cache Penetration" by storing an empty representation in Redis
     * for users with no assigned permissions.
     */
    private List<SysAcl> handleEmptyAclCache(String key) {
        List<SysAcl> emptyList = Lists.newArrayList();
        // Cache empty result for 5 minutes to protect the database
        redisService.setEx(key, JsonMapper.obj2String(emptyList), 300);
        return emptyList;
    }

//    -------------- 2/28/2026 concurrency solution for getUserAclList end ---------------


    /**
     * Determine whether the current user is a super administrator.
     *
     * <p>
     * NOTE:
     * This rule has been simplified for demonstration purposes within this IAM project portfolio.
     * In production systems, super admin determination should
     * be based on configuration, role assignment, or database flags.
     * </p>
     *
     * @return true if current user is super admin
     */
    public boolean isSuperAdmin() {

        // Here I defined a mock super administrator rule myself.
        // In practice, this should be modified according to the specific project requirements.
        // It can be loaded from a configuration file, or we can designate a specific user or a specific role.
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
        //  每次从系统里取出同样url对应的权限 ， 这个值很少会变，对系统请求的压力不大
        List<SysAcl> aclList = sysAclMapper.getByUrl(url);
        if (CollectionUtils.isEmpty(aclList)) {
            return true;
        }

        // 为啥在这里加缓存
        List<SysAcl> userAclList = getCurrentUserAclListFromCache();
        Set<Integer> userAclIdSet = userAclList.stream()
                .map(acl -> acl.getId()).
                collect(Collectors.toSet());

        boolean hasValidAcl = false;

        // Access Control Rule: Access is granted as long as the user possesses authorization for at least one permission item.
        for (SysAcl acl : aclList) {
            // Determine whether a user has a permission.
            if (acl == null || acl.getStatus() != 1) { // Permission not found or Invalid permission
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
