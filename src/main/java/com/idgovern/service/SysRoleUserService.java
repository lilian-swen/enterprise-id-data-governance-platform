package com.idgovern.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.idgovern.beans.LogType;
import com.idgovern.common.RequestHolder;
import com.idgovern.dao.SysLogMapper;
import com.idgovern.dao.SysRoleUserMapper;
import com.idgovern.dao.SysUserMapper;
import com.idgovern.model.SysLogWithBLOBs;
import com.idgovern.model.SysRoleUser;
import com.idgovern.model.SysUser;
import com.idgovern.util.IpUtil;
import com.idgovern.util.JsonMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;



/**
 * Service class for managing role-user assignments.
 *
 * <p>
 * Provides methods to retrieve, update, and manage the association
 * between roles and users in the RBAC system. Ensures that changes
 * are logged for audit purposes using {@link com.idgovern.model.SysLogWithBLOBs}.
 * </p>
 *
 * <p>
 * Business Rules:
 * <ul>
 *     <li>Role-user mappings should only be updated if there is a real change.</li>
 *     <li>All updates are performed in a transactional manner to ensure consistency.</li>
 *     <li>Operations automatically log the previous and new state for traceability.</li>
 * </ul>
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author     | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-03-01 | Lilian S.  | Initial creation                |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */
@Service
public class SysRoleUserService {

    @Resource
    private SysRoleUserMapper sysRoleUserMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysLogMapper sysLogMapper;


    /**
     * Retrieve all users associated with a specific role.
     *
     * @param roleId role identifier
     * @return list of users belonging to the role
     */
    public List<SysUser> getListByRoleId(int roleId) {

        List<Integer> userIdList = sysRoleUserMapper.getUserIdListByRoleId(roleId);

        if (CollectionUtils.isEmpty(userIdList)) {
            return Lists.newArrayList();
        }
        return sysUserMapper.getByIdList(userIdList);
    }


    /**
     * Update the user list for a role.
     *
     * <p>
     * Compares the current user-role assignments with the new list and
     * only performs updates if there are changes. Also logs the changes
     * for auditing purposes.
     * </p>
     *
     * @param roleId role identifier
     * @param userIdList new list of user IDs for the role
     */
    public void changeRoleUsers(int roleId, List<Integer> userIdList) {
        List<Integer> originUserIdList = sysRoleUserMapper.getUserIdListByRoleId(roleId);

        // Check if there are any actual changes
        if (originUserIdList.size() == userIdList.size()) {

            Set<Integer> originUserIdSet = Sets.newHashSet(originUserIdList);
            Set<Integer> userIdSet = Sets.newHashSet(userIdList);
            originUserIdSet.removeAll(userIdSet);

            if (CollectionUtils.isEmpty(originUserIdSet)) {
                // No changes detected, exit early
                return;
            }

        }

        // Apply updates and log changes
        updateRoleUsers(roleId, userIdList);
        saveRoleUserLog(roleId, originUserIdList, userIdList);
    }


    /**
     * Transactionally update role-user assignments.
     *
     * @param roleId role identifier
     * @param userIdList list of user IDs to assign to the role
     */
    @Transactional
    private void updateRoleUsers(int roleId, List<Integer> userIdList) {

        sysRoleUserMapper.deleteByRoleId(roleId);

        if (CollectionUtils.isEmpty(userIdList)) {
            return;
        }

        List<SysRoleUser> roleUserList = Lists.newArrayList();

        for (Integer userId : userIdList) {
            SysRoleUser roleUser = SysRoleUser.builder()
                    .roleId(roleId)
                    .userId(userId)
                    .operator(RequestHolder.getCurrentUser().getUsername())
                    .operateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()))
                    .operateTime(LocalDateTime.now())
                    .build();
            roleUserList.add(roleUser);
        }
        sysRoleUserMapper.batchInsert(roleUserList);
    }


    /**
     * Log role-user assignment changes for auditing.
     *
     * @param roleId role identifier
     * @param before list of user IDs before change
     * @param after list of user IDs after change
     */
    private void saveRoleUserLog(int roleId, List<Integer> before, List<Integer> after) {

        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ROLE_USER);
        sysLog.setTargetId(roleId);
        sysLog.setOldValue(before == null ? "" : JsonMapper.obj2String(before));
        sysLog.setNewValue(after == null ? "" : JsonMapper.obj2String(after));
        sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
        sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        sysLog.setOperateTime(LocalDateTime.now());
        sysLog.setStatus(1);
        sysLogMapper.insertSelective(sysLog);
    }
}
