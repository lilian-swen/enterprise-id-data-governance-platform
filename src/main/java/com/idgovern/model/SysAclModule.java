package com.idgovern.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import java.util.Date;

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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysAclModule {

    /** Unique identifier for the ACL module */
    private Integer id;

    /** Name of the ACL module */
    private String name;

    /** Parent module ID; 0 indicates top-level module */
    private Integer parentId;

    /** Level of the module in the hierarchy (e.g., "0", "0.1", "0.1.2") */
    private String level;

    /** Sequence number for ordering within the same level */
    private Integer seq;

    /** Status of the module (0: inactive, 1: active) */
    private Integer status;

    /** Optional remark or description for the module */
    private String remark;

    /** Operator who last modified the module */
    private String operator;

    /** Timestamp of last modification */
    private Date operateTime;

    /** IP address of the operator at last modification */
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level == null ? null : level.trim();
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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