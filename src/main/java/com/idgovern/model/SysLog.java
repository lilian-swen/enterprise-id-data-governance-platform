package com.idgovern.model;

import lombok.*;
import java.time.LocalDateTime;


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
 * | 1.1     | 2026-02-21 | Lilian S.| Use @Getter and @Setter at the class level and refactor the model accordingly. |
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
@Getter
@Setter
@ToString
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
    private LocalDateTime operateTime;

    /** IP address of the operator */
    private String operateIp;

    /** Status of the log entry (e.g., 0: invalid/deleted, 1: valid) */
    private Integer status;

    // =======================
    // Getters and Setters
    // =======================

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public void setOperateIp(String operateIp) {
        this.operateIp = operateIp == null ? null : operateIp.trim();
    }
}