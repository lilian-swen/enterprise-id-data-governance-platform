package com.idgovern.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.Date;

/**
 * Data Transfer Object (DTO) for searching system logs.
 *
 * <p>
 * Encapsulates search criteria for querying logs in the system, including:
 * <ul>
 *     <li>type: Log type or category</li>
 *     <li>beforeSeg: Content before the change</li>
 *     <li>afterSeg: Content after the change</li>
 *     <li>operator: Username who performed the operation</li>
 *     <li>fromTime: Start time of the log search (format: yyyy-MM-dd HH:mm:ss)</li>
 *     <li>toTime: End time of the log search (format: yyyy-MM-dd HH:mm:ss)</li>
 * </ul>
 * </p>
 *
 * <p>
 * Typically used to security logs for auditing, debugging, or operational monitoring.
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author   | Description                        |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-03-02 | Lilian S.| Initial creation of SearchLogDto   |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class SearchLogDto {

    /**
     * Log type (e.g., CREATE, UPDATE, DELETE).
     */
    private Integer type; // LogType

    /**
     * Content before the operation.
     */
    private String beforeSeg;

    /**
     * Content after the operation.
     */
    private String afterSeg;

    /**
     * Operator (username) who performed the action.
     */
    private String operator;

    /**
     * Start time for log search (format: yyyy-MM-dd HH:mm:ss).
     */
    private Date fromTime;//yyyy-MM-dd HH:mm:ss

    /**
     * End time for log search (format: yyyy-MM-dd HH:mm:ss).
     */
    private Date toTime; //yyyy-MM-dd HH:mm:ss
}
