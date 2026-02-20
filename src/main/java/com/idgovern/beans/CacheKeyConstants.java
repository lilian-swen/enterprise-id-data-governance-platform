package com.idgovern.beans;

import lombok.Getter;
/**
 * Enumeration of cache key prefixes used in the RBAC system.
 *
 * <p>
 * Defines standardized cache key namespaces to ensure
 * consistency and avoid key collisions in Redis.
 * Each enum value represents a logical cache domain.
 * </p>
 *
 * <p>
 * Business Rules:
 * <ul>
 *     <li>All Redis cache keys must use a defined prefix.</li>
 *     <li>Prefixes represent logical cache boundaries.</li>
 *     <li>New cache domains must be added here to maintain consistency.</li>
 * </ul>
 * </p>
 *
 * Cache Key Pattern:
 * <pre>
 *     PREFIX_optionalKey1_optionalKey2
 * </pre>
 *
 * Example:
 * <pre>
 *     USER_ACLS_1001
 *     SYSTEM_ACLS
 * </pre>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author     | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-03-2 | Lilian S.  | Initial creation                |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */
@Getter
public enum CacheKeyConstants {

    /**
     * Cache key prefix for system-level ACL (Access Control List) data.
     * Typically used for caching all permission configurations.
     */
    SYSTEM_ACLS,

    /**
     * Cache key prefix for user-level ACL (Access Control List) data.
     * Typically used for caching permissions assigned to a specific user.
     */
    USER_ACLS;

}
