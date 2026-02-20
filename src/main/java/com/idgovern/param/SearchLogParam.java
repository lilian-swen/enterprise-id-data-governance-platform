package com.idgovern.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * Search Log Query Parameter Object
 *
 * <p>
 * This parameter object encapsulates search criteria used for querying
 * system operation logs. It is typically used in controller and service
 * layers to receive and transfer log filtering conditions.
 * </p>
 *
 * <p>
 * Core Responsibilities:
 * <ul>
 *     <li>Defines filtering conditions for log type.</li>
 *     <li>Supports searching by operator (user who performed the action).</li>
 *     <li>Allows filtering based on before/after change content segments.</li>
 *     <li>Supports time-range based querying.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Design Notes:
 * <ul>
 *     <li>Used as a DTO (Data Transfer Object) for log search requests.</li>
 *     <li>Time fields follow the format: yyyy-MM-dd HH:mm:ss.</li>
 *     <li>Intended for use with pagination and dynamic query construction.</li>
 * </ul>
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author     | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-03-19 | Lilian S.  | Initial creation                |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@ToString
@Schema(description = "Query parameters for filtering system audit logs")
public class SearchLogParam {

    /**
     * Log type identifier.
     * Corresponds to system-defined LogType enumeration.
     */
    @Schema(description = "Target entity type (e.g., 1=Dept, 2=User, 3=Role, 4=ACL, 5=ACLModule)",
            example = "2", allowableValues = {"1", "2", "3", "4", "5", "6"})
    private Integer type; // LogType

    /**
     * Content segment before modification (for fuzzy matching).
     */
    @Schema(description = "Fuzzy search string for content BEFORE the change", example = "Senior Developer")
    private String beforeSeg;

    /**
     * Content segment after modification (for fuzzy matching).
     */
    @Schema(description = "Fuzzy search string for content AFTER the change", example = "Lead Engineer")
    private String afterSeg;

    /**
     * Operator username (the user who performed the action).
     */
    @Schema(description = "Username of the person who performed the action", example = "lilian.s")
    private String operator;

    /**
     * Start time for log search.
     * Format: yyyy-MM-dd HH:mm:ss
     */
    @Schema(description = "Search start time", example = "2026-01-01 00:00:00", pattern = "yyyy-MM-dd HH:mm:ss")
    private String fromTime;//yyyy-MM-dd HH:mm:ss

    /**
     * End time for log search.
     * Format: yyyy-MM-dd HH:mm:ss
     */
    @Schema(description = "Search end time", example = "2025-12-31 23:59:59", pattern = "yyyy-MM-dd HH:mm:ss")
    private String toTime;
}
