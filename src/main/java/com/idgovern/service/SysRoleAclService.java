package com.idgovern.service;

import com.idgovern.beans.LogType;
import com.idgovern.common.RequestHolder;
import com.idgovern.dao.SysLogMapper;
import com.idgovern.dao.SysRoleAclMapper;
import com.idgovern.model.SysLogWithBLOBs;
import com.idgovern.model.SysRoleAcl;
import com.idgovern.util.IpUtil;
import com.idgovern.util.JsonMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Role-ACL Service.
 *
 * <p>
 * Handles the assignment of ACL (Access Control List) permissions to roles.
 * Supports role-permission update operations and logs changes for auditing purposes.
 * </p>
 *
 * <p>
 * Key responsibilities:
 * <ul>
 *     <li>Change ACL assignments for a role</li>
 *     <li>Update role-ACL relationships in the database</li>
 *     <li>Record logs for role-ACL changes</li>
 * </ul>
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author    | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-03-02 | Lilian S.| Initial creation                |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */
@Service
public class SysRoleAclService {

    @Resource
    private SysRoleAclMapper sysRoleAclMapper;

    @Resource
    private SysLogMapper sysLogMapper;

    /**
     * Change the ACL assignments for a specific role.
     *
     * <p>
     * Compares the existing ACL list with the new one. If there is a difference,
     * updates the role-ACL relationships and logs the changes.
     * </p>
     *
     * @param roleId   the role identifier
     * @param aclIdList list of ACL IDs to assign to the role
     */
    @Transactional
    public void changeRoleAcls(Integer roleId, List<Integer> aclIdList) {

        // 1. Fetch current IDs
        List<Integer> originAclIdList = sysRoleAclMapper.getAclIdListByRoleIdList(List.of(roleId));

        // If the existing ACL list and the new list have the same size, check for differences
        if (Set.copyOf(originAclIdList).equals(Set.copyOf(aclIdList))) {

            // 2. Performance Check: Use Sets for O(1) comparison
            Set<Integer> originAclIdSet = Set.copyOf(originAclIdList);
            Set<Integer> aclIdSet = Set.copyOf(aclIdList);
            originAclIdSet.removeAll(aclIdSet);

            if (CollectionUtils.isEmpty(originAclIdSet)) {
                // No changes detected
                return;
            }
        }

        // 3. Execute Update & Log
        updateRoleAcls(roleId, aclIdList);
        saveRoleAclLog(roleId, originAclIdList, aclIdList);
    }


    /**
     * Update the role-ACL relationships in the database.
     *
     * <p>
     * This method deletes existing ACL assignments for the role and inserts
     * the new assignments. Annotated with {@link Transactional} to ensure
     * atomicity of the operation.
     * </p>
     *
     * @param roleId   role identifier
     * @param aclIdList list of ACL IDs to assign
     */
    @Transactional
    public void updateRoleAcls(int roleId, List<Integer> aclIdList) {

        // Delete existing ACLs
        sysRoleAclMapper.deleteByRoleId(roleId);

        if (CollectionUtils.isEmpty(aclIdList)) {
            return;
        }

        String operator = RequestHolder.getCurrentUser().getUsername();
        String operateIp = IpUtil.getRemoteIp(RequestHolder.getCurrentRequest());
        LocalDateTime operateTime = LocalDateTime.now();

        // Modern Stream mapping
        List<SysRoleAcl> roleAclList = aclIdList.stream()
                .map(aclId -> SysRoleAcl.builder()
                        .roleId(roleId)
                        .aclId(aclId)
                        .operator(operator)
                        .operateIp(operateIp)
                        .operateTime(operateTime)
                        .build())
                .toList(); // JDK 16+ collector

        sysRoleAclMapper.batchInsert(roleAclList);
    }


    /**
     * Save a log entry recording the change of ACL assignments for a role.
     *
     * <p>
     * Captures the role ID, old ACL list, new ACL list, operator info, IP, and timestamp.
     * </p>
     *
     * @param roleId role identifier
     * @param before ACL list before the change
     * @param after  ACL list after the change
     */
    private void saveRoleAclLog(int roleId, List<Integer> before, List<Integer> after) {

        SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
        sysLog.setType(LogType.TYPE_ROLE_ACL);
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
