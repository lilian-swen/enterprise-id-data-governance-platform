package com.idgovern.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import java.util.Date;


/**
 * System Log Entity.
 *
 * <p>
 * Represents a log entry in the system for auditing and tracking operations.
 * Each log entry contains information about the type of operation, target entity,
 * operator, time of operation, IP address, and status.
 * </p>
 *
 * <p>
 * This entity can be extended to SysLogWithBLOBs if detailed before/after snapshots
 * of changes are required for auditing.
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author   | Description                    |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-02-17 | Lilian S.| Initial creation of SysLog      |
 * ------------------------------------------------------------------------
 *
 * Usage:
 * <ul>
 *     <li>Used to store operation logs for auditing purposes</li>
 *     <li>Tracks operator, operation time, target entity, and IP</li>
 *     <li>Status can be used to indicate active or rolled-back logs</li>
 * </ul>
 *
 * @author Lilian
 * @version 1.0
 * @since 1.0
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysLog {

    /** Unique identifier for the log entry */
    private Integer id;

    /** Type of operation (e.g., 1: CREATE, 2: UPDATE, 3: DELETE) */
    private Integer type;

    /** Target entity ID that this log is related to */
    private Integer targetId;

    /** Username of the operator performing the action */
    private String operator;

    /** Timestamp of the operation */
    private Date operateTime;

    /** IP address of the operator */
    private String operateIp;

    /** Status of the log entry (e.g., 0: invalid/deleted, 1: valid) */
    private Integer status;

    // =======================
    // Getters and Setters
    // =======================
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}