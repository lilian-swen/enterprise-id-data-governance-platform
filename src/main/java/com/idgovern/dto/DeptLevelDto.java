package com.idgovern.dto;

import com.google.common.collect.Lists;
import com.idgovern.model.SysDept;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;
import java.util.List;

/**
 * Data Transfer Object (DTO) representing a Department with hierarchical children.
 *
 * <p>
 * Extends {@link SysDept} and adds a list of child departments,
 * enabling tree-structured department representations for frontend display
 * or hierarchical processing.
 * </p>
 *
 * <p>
 * This DTO is commonly used when building department trees in the RBAC system,
 * such as:
 * <ul>
 *     <li>Displaying department hierarchy in UI</li>
 *     <li>Generating nested organizational structures</li>
 *     <li>Constructing tree-based permission views</li>
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

@Getter
@Setter
@ToString
public class DeptLevelDto extends SysDept {

    /**
     * List of child departments under the current department.
     *
     * <p>
     * Used to construct a hierarchical tree structure.
     * Each child is also a {@link DeptLevelDto}, allowing recursive nesting.
     * </p>
     */
    private List<DeptLevelDto> deptList = Lists.newArrayList();

    /**
     * Converts a {@link SysDept} entity into a {@link DeptLevelDto}.
     *
     * <p>
     * Copies all properties from the entity object and prepares
     * an empty child department list for tree construction.
     * </p>
     *
     * @param dept the source department entity
     * @return a corresponding DeptLevelDto object
     */
    public static DeptLevelDto adapt(SysDept dept) {
        DeptLevelDto dto = new DeptLevelDto();
        BeanUtils.copyProperties(dept, dto);
        return dto;
    }
}
