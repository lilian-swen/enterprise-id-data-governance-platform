package com.idgovern.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Utility class for managing hierarchical levels in the RBAC system.
 *
 * <p>
 * Levels are represented as strings with IDs separated by a dot ('.') to indicate
 * hierarchy. For example:
 * </p>
 * <pre>
 * Root department: "0"
 * First child:    "0.1"
 * Second child:   "0.1.2"
 * Another child:  "0.1.3"
 * Another root:   "0.4"
 * </pre>
 *
 * <p>
 * Provides helper methods to calculate the level string for a department
 * based on its parent department.
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
public class LevelUtil {
    /**
     * Separator used in level strings to denote hierarchy.
     * Example: "0.1.2"
     */
    public final static String SEPARATOR = ".";

    /**
     * Root level identifier.
     */
    public final static String ROOT = "0";

    /**
     * Calculates the hierarchical level string for a department.
     *
     * <p>
     * If the parent level is blank (null or empty), this method returns the root level "0".
     * Otherwise, it appends the parent ID to the parent level using the separator.
     * </p>
     *
     * @param parentLevel the hierarchical level of the parent department, e.g., "0.1"
     * @param parentId the ID of the parent department
     * @return the calculated level string for the current department, e.g., "0.1.3"
     */
    public static String calculateLevel(String parentLevel, int parentId) {
        if (StringUtils.isBlank(parentLevel)) {
            // Root-level department
            return ROOT;
        } else {
            // Child department: append parentId to parent's level
            return StringUtils.join(parentLevel, SEPARATOR, parentId);
        }
    }
}
