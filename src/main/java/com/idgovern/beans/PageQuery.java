package com.idgovern.beans;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;


/**
 * Encapsulates pagination parameters for queries.
 *
 * <p>
 * This class provides a standardized way to pass pagination information, including
 * page number, page size, and offset for database queries.
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
 * ------------------------------------------------------------------------
 *
 * @author Lilian
 * @version 1.0
 * @since 1.0
 */
public class PageQuery {

    /** Current page number (1-based). Defaults to 1. */
    @Getter
    @Setter
    @Min(value = 1, message = "The current page number is invalid.")
    private int pageNo = 1;

    /** Number of items per page. Defaults to 10. */
    @Getter
    @Setter
    @Min(value = 1, message = "The number of items per page is invalid.")
    private int pageSize = 10;

    /** Database offset for the query. Automatically calculated. */
    @Setter
    private int offset;

    /**
     * Returns the offset for database queries, calculated as (pageNo - 1) * pageSize.
     *
     * @return the offset
     */
    public int getOffset() {
        return (pageNo - 1) * pageSize;
    }
}
