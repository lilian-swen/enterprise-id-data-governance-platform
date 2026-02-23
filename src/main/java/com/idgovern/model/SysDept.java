package com.idgovern.model;

import lombok.*;
import java.time.LocalDateTime;



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
 * | 1.1     | 2026-02-21 | Lilian S.| Use @Getter and @Setter at the class level and refactor the model accordingly. |
 * ------------------------------------------------------------------------
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
    private LocalDateTime operateTime;

    /**
     * IP address of the operator who last modified this department.
     */
    private String operateIp;

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public void setLevel(String level) {
        this.level = level == null ? null : level.trim();
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