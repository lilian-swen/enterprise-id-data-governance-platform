package com.idgovern.dto;

import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;


/**
 * A generic container class for paginated query results.
 *
 * <p>
 * This class is used to wrap the results of a paginated query in a standardized way.
 * It contains the actual data list and the total number of records matching the query.
 * </p>
 *
 * <p>
 * Business Rules:
 * <ul>
 *     <li>If no data is found, {@link #data} will be an empty list.</li>
 *     <li>{@link #total} represents the total number of matching records, regardless of page size.</li>
 *     <li>Designed to work with paginated queries for APIs, front-end tables, or other UI components.</li>
 * </ul>
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author     | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-03-19 | Lilian S.  | Initial creation                |
 * | 1.1     | 2026-02-20 | Lilian S.  | Enhanced with navigation metadata |
 * ------------------------------------------------------------------------
 *
 * @param <T> The type of the objects contained in the page data list
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
@Schema(description = "Standardized paginated response wrapper")
public class PageResult<T> {

    /** The list of data objects for the current page. Defaults to an empty list. */
    @Schema(description = "The list of data objects for the current page")
    @Builder.Default
    private List<T> data = Lists.newArrayList();

    /** Total count of records across all pages. The total number of records matching the query. Defaults to 0. */
    @Schema(description = "Total number of records matching the query criteria", example = "105")
    private int total = 0;

    /** Current page number (starting from 1) */
    @Schema(description = "Current page number (1-based)", example = "1")
    private int pageNo;

    /** Number of records per page */
    @Schema(description = "Number of records per page", example = "10")
    private int pageSize;

    /**
     * Convenience method for the frontend to calculate total pages.
     * * @return total pages based on total count and page size
     */
    @Schema(description = "Total number of pages based on total count and page size", example = "11")
    public long getTotalPage() {
        if (pageSize == 0) return 0;
        return (total + pageSize - 1) / pageSize;
    }

}
