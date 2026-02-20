package com.idgovern.exception;


/**
 * Custom exception class for permission or access control errors.
 *
 * <p>
 * This exception is thrown when a user or system component attempts
 * to perform an operation without the required permissions.
 * </p>
 *
 * <p>
 * Usage Guidelines:
 * <ul>
 *     <li>Use in service and controller layers to indicate unauthorized access.</li>
 *     <li>Encapsulates an error message and optional cause for debugging.</li>
 *     <li>Supports full customization of stack trace and suppression behavior.</li>
 * </ul>
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author     | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-02-19 | Lilian S.  | Initial creation                |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */
public class PermissionException extends RuntimeException {

    /**
     * Constructs a new PermissionException with {@code null} as its detail message.
     */
    public PermissionException() {
        super();
    }


    /**
     * Constructs a new PermissionException with the specified detail message.
     *
     * @param message the detail message
     */
    public PermissionException(String message) {
        super(message);
    }


    /**
     * Constructs a new PermissionException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public PermissionException(String message, Throwable cause) {
        super(message, cause);
    }


    /**
     * Constructs a new PermissionException with the specified cause.
     *
     * @param cause the cause of the exception
     */
    public PermissionException(Throwable cause) {
        super(cause);
    }


    /**
     * Constructs a new PermissionException with the specified detail message, cause,
     * suppression enabled or disabled, and writable stack trace enabled or disabled.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     * @param enableSuppression whether suppression is enabled or disabled
     * @param writableStackTrace whether the stack trace should be writable
     */
    protected PermissionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
