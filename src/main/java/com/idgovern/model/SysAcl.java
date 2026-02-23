package com.idgovern.model;

import lombok.*;
import java.time.LocalDateTime;


/**
 * System Access Control (ACL) Entity.
 *
 * <p>
 * Represents an Access Control resource in the system. Each ACL defines
 * permissions for a specific module, URL, or operation within the RBAC framework.
 * </p>
 *
 * <p>
 * Fields include identification, hierarchical module mapping, type, status,
 * operation sequence, and auditing information such as operator and timestamp.
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author   | Description                        |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-02-26 | Lilian S.| Initial creation of SysAcl model   |
 * | 1.1     | 2026-02-21 | Lilian S.| Use @Getter and @Setter at the class level and refactor the model accordingly. |
 * ------------------------------------------------------------------------
 *
 * Usage:
 * <ul>
 *     <li>Stored in the database to manage access control</li>
 *     <li>Mapped to roles via SysRoleAcl for permission assignment</li>
 *     <li>Used in constructing permission trees for UI and API checks</li>
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
@EqualsAndHashCode(of = {"id"})
@ToString
public class SysAcl {

    /** Unique identifier for the ACL entry */
    private Integer id;

    /** Unique code identifying the ACL (used for permission checks) */
    private String code;

    /** Human-readable name of the ACL */
    private String name;

    /** ID of the ACL module this ACL belongs to */
    private Integer aclModuleId;

    /** URL or resource path this ACL controls */
    private String url;

    /** Type of ACL (e.g., 1: menu, 2: button, etc.) */
    private Integer type;

    /** Status of ACL (0: inactive, 1: active) */
    private Integer status;

    /** Sequence number for ordering within the module */
    private Integer seq;

    /** Optional remark or description */
    private String remark;

    /** Username of the operator who last modified this ACL */
    private String operator;

    /** Timestamp of the last modification */
    private LocalDateTime operateTime;

    /** IP address of the operator during the last modification */
    private String operateIp;

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

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