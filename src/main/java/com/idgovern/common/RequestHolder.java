package com.idgovern.common;

import com.idgovern.model.SysUser;
import jakarta.servlet.http.HttpServletRequest;


/**
 * Thread-local holder for storing {@link SysUser} and {@link HttpServletRequest} objects
 * for the current request thread.
 *
 * <p>
 * This class provides a convenient way to access the currently logged-in user and the
 * current HTTP request without explicitly passing them through method parameters
 * across service layers.
 * </p>
 *
 * <p>
 * Business Rules:
 * <ul>
 *     <li>Each thread has its own copy of {@code SysUser} and {@code HttpServletRequest}.</li>
 *     <li>Values must be explicitly added at the beginning of the request (typically via a security/interceptor).</li>
 *     <li>Values must be removed after the request completes to prevent memory leaks.</li>
 * </ul>
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author     | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-03-06 | Lilian S.  | Initial creation                |
 * ------------------------------------------------------------------------
 *
 * @author Lilian
 * @version 1.0
 * @since 1.0
 */
public class RequestHolder {

    /**
     * Thread-local variable storing the current {@link SysUser}.
     */
    private static final ThreadLocal<SysUser> userHolder = new ThreadLocal<SysUser>();

    /**
     * Thread-local variable storing the current {@link HttpServletRequest}.
     */
    private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<HttpServletRequest>();

    /**
     * Stores the current {@link SysUser} in the thread-local.
     *
     * @param sysUser the currently logged-in user
     */
    public static void setCurrentUser(SysUser sysUser) {
        userHolder.set(sysUser);
    }


    /** Stores the current {@link HttpServletRequest} in the thread-local. */
    public static void setCurrentRequest(HttpServletRequest request) {
        requestHolder.set(request);
    }


    /**
     * Retrieves the {@link SysUser} associated with the current thread.
     *
     * @return the current user, or {@code null} if none is set
     */
    public static SysUser getCurrentUser() {
        return userHolder.get();
    }


    /**
     * Retrieves the {@link HttpServletRequest} associated with the current thread.
     *
     * @return the current HTTP request, or {@code null} if none is set
     */
    public static HttpServletRequest getCurrentRequest() {
        return requestHolder.get();
    }


    /**
     * Removes both {@link SysUser} and {@link HttpServletRequest} from the thread-local storage.
     *
     * <p>
     * Must be called at the end of each request to prevent memory leaks in long-running threads
     * (e.g., from servlet containers with thread pooling).
     * </p>
     */
    public static void remove() {
        userHolder.remove();
        requestHolder.remove();
    }
}
