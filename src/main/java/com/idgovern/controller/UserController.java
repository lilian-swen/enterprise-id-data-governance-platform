package com.idgovern.controller;

import com.idgovern.model.SysUser;
import com.idgovern.service.SysUserService;
import com.idgovern.util.MD5Util;
import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

import static com.alibaba.druid.support.jakarta.ResourceServlet.SESSION_USER_KEY;

/**
 * Controller for user authentication and session management.
 *
 * <p>
 * Provides login and logout endpoints for users, handling session
 * creation, invalidation, password verification, and redirection
 * based on authentication status.
 * </p>
 *
 * <p>
 * Implements basic validation and error handling for login attempts,
 * including empty input checks, user existence, password correctness,
 * and account status checks.
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author   | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-02-17 | Lilian S.| Initial creation of UserController |
 * | 1.1     | 2026-01-26 | Updated  | Professional Logging Strategy added|
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Controller
@Tag(name = "Authentication Management", description = "Endpoints for user session lifecycle, including login and logout operations")
public class UserController {

    @Resource
    private SysUserService sysUserService;

    /**
     * Logs out the current user by invalidating the session
     * and redirects to the login page.
     *
     * @param request  HTTP request
     * @param response HTTP response
     * @throws IOException      if an input/output error occurs
     * @throws ServletException if a servlet error occurs
     */
    @Operation(
            summary = "User Logout",
            description = "Invalidates the current HTTP session and clears the security context. Redirects to the login page upon completion."
    )
    @RequestMapping("/logout.page")
    public void logout(HttpServletRequest request,
                       HttpServletResponse response) throws IOException {

        String username = null;

        try {
            SysUser user = (SysUser) request.getSession().getAttribute(SysUser.SESSION_USER_KEY);

            if (user != null) {
                username = user.getUsername();
            }

            request.getSession().invalidate();;

            log.info("User logout successful: username={}, ip={}",
                    username,
                    request.getRemoteAddr());

            response.sendRedirect("signin.jsp");

        } catch (Exception e) {

            log.error("Logout failed: username={}, ip={}",
                    username,
                    request.getRemoteAddr(),
                    e);

            throw e;
        }
    }


    /**
     * Handles user login requests.
     *
     * <p>
     * Validates input parameters (username and password), checks
     * user existence, verifies password, checks account status,
     * and sets user session if login is successful. Supports
     * optional redirection via 'ret' parameter.
     * </p>
     *
     * @param request  HTTP request
     * @param response HTTP response
     * @throws IOException      if an input/output error occurs
     * @throws ServletException if a servlet error occurs
     */
    @Operation(
            summary = "User Login",
            description = "Authenticates a user via username and MD5-encrypted password. Establishes a server-side session upon success."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Redirect to dashboard or requested 'ret' URL on success"),
            @ApiResponse(responseCode = "200", description = "Returns to login page with error message if authentication fails"),
            @ApiResponse(responseCode = "500", description = "Internal system or database error")
    })
    @RequestMapping("/login.page")
    public void login(HttpServletRequest request,
                      HttpServletResponse response) throws IOException, ServletException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String ret = request.getParameter("ret");

        String ip = request.getRemoteAddr();

        log.info("Login attempt: username={}, ip={}", username, ip);

        String errorMsg = "";

        try {
            // Validation
            if (StringUtils.isBlank(username)) {

                log.warn("Login failed: empty username, ip={}", ip);
                errorMsg = "Username cannot be empty";

            } else if (StringUtils.isBlank(password)) {

                log.warn("Login failed: empty password, username={}, ip={}", username, ip);

                errorMsg = "Password cannot be empty";
                
            } else {

                SysUser sysUser = sysUserService.findByKeyword(username);

                if (sysUser == null) {
                    
                    log.warn("Login failed: user not found, username={}, ip={}", username, ip);

                    errorMsg = "User not found";
                    
                } else if (!sysUser.getPassword().equals(MD5Util.encrypt(password))) {

                    log.warn("Login failed: incorrect password, username={}, ip={}", username, ip);
                    
                    errorMsg = "Incorrect username or password";
                    
                } else if (sysUser.getStatus() != 1) {

                    log.warn("Login blocked: disabled user, username={}, ip={}", username, ip);

                    errorMsg = "Account disabled";

                } else {
                    // SUCCESS LOGIN
                    request.getSession().setAttribute(SysUser.SESSION_USER_KEY, sysUser);

                    log.info("Login successful: username={}, userId={}, ip={}",
                            sysUser.getUsername(),
                            sysUser.getId(),
                            ip);

                    if (StringUtils.isNotBlank(ret)) {

                        log.debug("Redirecting user={} to ret={}", username, ret);

                        response.sendRedirect(ret);

                    } else {

                        log.debug("Redirecting user={} to admin dashboard", username);

                        response.sendRedirect("/admin/index.page");
                    }

                    return;

                }
            }

        } catch (Exception e) {

            log.error("Login system error: username={}, ip={}", username, ip, e);

            errorMsg = "System error, please contact administrator";

        }

        // If login fails, forward back to login page with error message
        request.setAttribute("error", errorMsg);
        request.setAttribute("username", username);

        if (StringUtils.isNotBlank(ret)) {
            request.setAttribute("ret", ret);
        }

        log.debug("Forwarding login failure: username={}, ip={}", username, ip);

        String path = "signin.jsp";

        request.getRequestDispatcher(path).forward(request, response);
    }
}
