package com.idgovern.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.Date;


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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
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
    private Date operateTime;

    /** IP address of the operator during the last modification */
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getAclModuleId() {
        return aclModuleId;
    }

    public void setAclModuleId(Integer aclModuleId) {
        this.aclModuleId = aclModuleId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
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