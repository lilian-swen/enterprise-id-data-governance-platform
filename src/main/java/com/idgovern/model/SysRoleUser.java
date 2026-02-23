package com.idgovern.model;

import lombok.*;
import java.time.LocalDateTime;


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