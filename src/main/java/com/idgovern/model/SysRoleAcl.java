package com.idgovern.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import java.util.Date;


/**
 * System Role-ACL Association Entity.
 *
 * <p>
 * Represents the mapping between a Role and an Access Control List (ACL) entry.
 * This entity tracks which roles have permissions to which ACLs and records
 * auditing information including operator, operation time, and IP address.
 * </p>
 *
 * <p>
 * Typical usage:
 * <ul>
 *     <li>Used in role-based access control (RBAC) systems</li>
 *     <li>Supports auditing of permission changes</li>
 *     <li>Facilitates batch operations for assigning or revoking ACLs from roles</li>
 * </ul>
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author   | Description                      |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-02-22 | Lilian S.| Initial creation of SysRoleAcl   |
 * ------------------------------------------------------------------------
 *
 * @author Lilian
 * @version 1.0
 * @since 1.0
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysRoleAcl {

    /** Primary key ID */
    private Integer id;

    /** Role ID associated with the ACL */
    private Integer roleId;

    /** ACL (permission) ID associated with the role */
    private Integer aclId;

    /** Operator who performed the change */
    private String operator;

    /** Timestamp when the operation was performed */
    private Date operateTime;

    /** IP address from which the operation was performed */
    private String operateIp;

    // =======================
    // Getters and Setters
    // =======================
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getAclId() {
        return aclId;
    }

    public void setAclId(Integer aclId) {
        this.aclId = aclId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getOperateIp() {
        return operateIp;
    }

    public void setOperateIp(String operateIp) {
        this.operateIp = operateIp == null ? null : operateIp.trim();
    }
}