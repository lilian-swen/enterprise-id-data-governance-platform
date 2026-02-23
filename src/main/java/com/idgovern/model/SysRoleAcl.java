package com.idgovern.model;

import lombok.*;
import java.time.LocalDateTime;


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
 * | 1.1     | 2026-02-21 | Lilian S.| Use @Getter and @Setter at the class level and refactor the model accordingly. |
 * ------------------------------------------------------------------------
 *
 * @author Lilian
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@ToString
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
    private LocalDateTime operateTime;

    /** IP address from which the operation was performed */
    private String operateIp;

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public void setOperateIp(String operateIp) {
        this.operateIp = operateIp == null ? null : operateIp.trim();
    }
}