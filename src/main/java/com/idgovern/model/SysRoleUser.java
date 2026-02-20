package com.idgovern.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * System Role-User Association Entity.
 *
 * <p>
 * Represents the mapping between a Role and a User.
 * This entity is fundamental in Role-Based Access Control (RBAC) systems,
 * linking users to the roles they belong to. It also tracks auditing
 * information such as who performed the assignment, when, and from which IP.
 * </p>
 *
 * <p>
 * Typical usage:
 * <ul>
 *     <li>Determine which roles a user belongs to</li>
 *     <li>Assign or revoke roles from a user</li>
 *     <li>Audit changes for compliance purposes</li>
 * </ul>
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author   | Description                       |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-02-22 | Lilian S.| Initial creation of SysRoleUser   |
 * ------------------------------------------------------------------------
 *
 * @author Lilian
 * @version 1.0
 * @since 1.0
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysRoleUser {

    /** Primary key ID */
    private Integer id;

    /** Role ID assigned to the user */
    private Integer roleId;

    /** User ID associated with the role */
    private Integer userId;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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