package com.idgovern.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * Encapsulates pagination parameters for queries.
 *
 * <p>
 * This class provides a standardized way to pass pagination information, including
 * page number, page size, and offset for database queries.
 * It acts as the Request DTO that captures user input from the frontend and prepares it for the database.
 * </p>
 *
 * <p>
 * Business Rules:
 * <ul>
 *     <li>Page numbers are 1-based; the first page is pageNo = 1.</li>
 *     <li>pageSize must be greater than 0 and determines the number of items per page.</li>
 *     <li>Offset is calculated automatically as (pageNo - 1) * pageSize.</li>
 * </ul>
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author     | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-03-19 | Lilian S.  | Initial creation                |
 * | 1.1     | 2026-02-20 | Lilian S.  | Added Max guard and Swagger    |
 * ------------------------------------------------------------------------
 *
 * @author Lilian
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@ToString
@Schema(description = "Pagination query parameters")
public class PageQuery {

    /** Current page number (1-based). Defaults to 1. */
    @Min(value = 1, message = "Page number must be at least 1")
    @Schema(description = "Current page number (1-based)", example = "1", defaultValue = "1")
    private int pageNo = 1;

    /** Number of items per page. Defaults to 10. */
    @Min(value = 1, message = "Page size must be at least 1")
    @Max(value = 200, message = "Page size cannot exceed 200 to protect system performance")
    @Schema(description = "Number of records per page", example = "10", defaultValue = "10")
    private int pageSize = 10;

    /** Database offset for the query. Automatically calculated.
     * Offset is used by MyBatis/SQL but should not be exposed in API documentation
     */
    @Schema(hidden = true)
    private int offset;

    /**
     * Returns the offset for database queries, calculated as (pageNo - 1) * pageSize.
     * Standard MyBatis/SQL offset calculation.
     * @return the offset
     */
    public int getOffset() {
        return (pageNo - 1) * pageSize;
    }
}
