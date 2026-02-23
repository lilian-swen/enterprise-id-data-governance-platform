package com.idgovern.model;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * System ACL Module Entity.
 *
 * <p>
 * Represents an Access Control Module in the system, which can group multiple
 * ACLs under a hierarchical structure. Modules are used to organize permissions
 * logically and build the permission tree in RBAC (Role-Based Access Control).
 * </p>
 *
 * <p>
 * Fields include identification, hierarchy (parentId and level), sequence, status,
 * and auditing information such as operator and operation timestamp.
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author   | Description                      |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-02-26 | Lilian S.| Initial creation of SysAclModule |
 * | 1.1     | 2026-02-21 | Lilian S.| Use @Getter and @Setter at the class level and refactor the model accordingly. |
 * ------------------------------------------------------------------------
 *
 * Usage:
 * <ul>
 *     <li>Used to group ACL entries into logical modules</li>
 *     <li>Supports hierarchical levels for building tree structures</li>
 *     <li>Provides ordering and status for modules in the system</li>
 * </ul>
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SysAclModule implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Unique identifier for the ACL module */
    private Integer id;

    /** Name of the ACL module */
    private String name;

    /** Parent module ID; 0 indicates top-level module */
    @Builder.Default
    private Integer parentId = 0;

    /** Level of the module in the hierarchy (e.g., "0", "0.1", "0.1.2") */
    private String level;

    /** Sequence number for ordering within the same level */
    @Builder.Default
    private Integer seq = 0;

    /** Status of the module (0: inactive, 1: active) */
    @Builder.Default
    private Integer status = 1;

    /** Optional remark or description for the module */
    private String remark;

    // Audit fields
    /** Operator who last modified the module */
    private String operator;

    /** Timestamp of last modification */
    private LocalDateTime operateTime;

    /** IP address of the operator at last modification */
    private String operateIp;

    // --- Custom Logic Setters ---
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public void setOperateIp(String operateIp) {
        this.operateIp = operateIp == null ? null : operateIp.trim();
    }
}