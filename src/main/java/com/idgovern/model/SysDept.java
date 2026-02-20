package com.idgovern.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.Date;


/**
 * Entity class representing a Department within the RBAC system.
 *
 * <p>
 * Departments can form hierarchical structures through parent-child relationships.
 * This class stores department metadata, hierarchy level, ordering, and audit information.
 * </p>
 *
 * <p>
 * Fields include:
 * <ul>
 *     <li>id: Unique identifier</li>
 *     <li>name: Department name</li>
 *     <li>parentId: Parent department ID (null or 0 indicates root)</li>
 *     <li>level: Hierarchical level string for the department</li>
 *     <li>seq: Display order within the same hierarchy level</li>
 *     <li>remark: Optional description</li>
 *     <li>operator: User who last modified the department</li>
 *     <li>operateTime: Timestamp of last modification</li>
 *     <li>operateIp: IP address of the operator</li>
 * </ul>
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author   | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-02-16 | Lilian S.| Initial creation                |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SysDept {
    /**
     * Unique identifier of the department.
     */
    private Integer id;

    /**
     * Name of the department.
     */
    private String name;

    /**
     * Parent department ID.
     * Null or 0 indicates a root-level department.
     */
    private Integer parentId;

    /**
     * Hierarchical level string.
     * Constructed using parent levels and IDs (e.g., "0.1.2").
     */
    private String level;

    /**
     * Display order among sibling departments.
     */
    private Integer seq;

    /**
     * Optional description or remark about the department.
     */
    private String remark;

    /**
     * Username of the operator who last modified this department.
     */
    private String operator;

    /**
     * Timestamp of the last modification.
     */
    private Date operateTime;

    /**
     * IP address of the operator who last modified this department.
     */
    private String operateIp;

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