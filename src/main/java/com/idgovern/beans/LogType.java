package com.idgovern.beans;

/**
 * Defines constant types for logging different system operations.
 *
 * <p>
 * These constants are used in the system's logging and audit mechanism
 * to distinguish the type of entity affected by an operation.
 * </p>
 *
 * <p>
 * Business Rules:
 * <ul>
 *     <li>TYPE_DEPT: Operations related to departments.</li>
 *     <li>TYPE_USER: Operations related to users.</li>
 *     <li>TYPE_ACL_MODULE: Operations on permission modules.</li>
 *     <li>TYPE_ACL: Operations on individual permission points.</li>
 *     <li>TYPE_ROLE: Operations on roles.</li>
 *     <li>TYPE_ROLE_ACL: Role-to-permission assignments.</li>
 *     <li>TYPE_ROLE_USER: Role-to-user assignments.</li>
 * </ul>
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author     | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-02-17 | Lilian S.  | Initial creation                |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */
public interface LogType {

    /** Log type for department operations */
    int TYPE_DEPT = 1;

    /** Log type for user operations */
    int TYPE_USER = 2;

    /** Log type for permission module operations */
    int TYPE_ACL_MODULE = 3;

    /** Log type for permission point (ACL) operations */
    int TYPE_ACL = 4;

    /** Log type for role operations */
    int TYPE_ROLE = 5;

    /** Log type for role-permission assignments */
    int TYPE_ROLE_ACL = 6;

    /** Log type for role-user assignments */
    int TYPE_ROLE_USER = 7;
}
