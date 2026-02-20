package com.idgovern.security;

import com.idgovern.common.RequestHolder;
import com.idgovern.model.SysUser;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


/**
 * Login Authentication Filter
 *
 * <p>
 * This security is responsible for verifying whether a user has
 * successfully logged into the system before accessing protected resources.
 * </p>
 *
 * <p>
 * Core Responsibilities:
 * <ul>
 *     <li>Checks the HTTP session for a valid SysUser object.</li>
 *     <li>Redirects unauthenticated users to the login page.</li>
 *     <li>Stores authenticated user and request information in RequestHolder.</li>
 *     <li>Allows the request to proceed if authentication succeeds.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Design Notes:
 * <ul>
 *     <li>Relies on session-based authentication.</li>
 *     <li>Uses ThreadLocal (RequestHolder) to maintain per-request user context.</li>
 *     <li>Should be configured before authorization filters in security chain order.</li>
 * </ul>
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author     | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-03-13 | Lilian S.  | Initial creation                |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */
@Slf4j
public class LoginFilter implements Filter {

    /**
     * Filter initialization method.
     * Called once when the security is created.
     */
    public void init(FilterConfig filterConfig) throws ServletException {
        // No initialization logic required
    }


    /**
     * Core filtering logic.
     *
     * Steps:
     * 1. Retrieve current user from HTTP session.
     * 2. If user is not logged in, redirect to login page.
     * 3. If authenticated, store user and request in RequestHolder.
     * 4. Continue security chain execution.
     */
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        // Retrieve user from session
        SysUser sysUser = (SysUser)req.getSession().getAttribute("user");

        // If not logged in, redirect to login page
        if (sysUser == null) {
            String path = "/signin.jsp";
            resp.sendRedirect(path);
            return;
        }

        try {
            // Store user and request in thread-local
            RequestHolder.setCurrentUser(sysUser);
            RequestHolder.setCurrentRequest(req);

            // Continue the filter chain
            filterChain.doFilter(servletRequest, servletResponse);

        } finally {
            // Always remove thread-local references to prevent memory leaks
            RequestHolder.remove();
        }
    }

    /**
     * Cleanup logic when security is destroyed.
     */
    public void destroy() {
        // No resources to release
    }
}
