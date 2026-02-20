package com.idgovern.beans;

import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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
public class PageResult<T> {

    /** The list of data objects for the current page. Defaults to an empty list. */
    private List<T> data = Lists.newArrayList();

    /** The total number of records matching the query. Defaults to 0. */
    private int total = 0;
}
