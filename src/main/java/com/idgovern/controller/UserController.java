package com.idgovern.controller;

import com.idgovern.model.SysUser;
import com.idgovern.service.SysUserService;
import com.idgovern.util.MD5Util;
import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.io.IOException;


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
 * | 1.1     | 2026-01-26 | Lilian S.  | Professional Logging Strategy added|
 * | 1.2     | 2026-02-22 | Lilian S.| Refactored for AOP & Clean Architecture |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.2
 * @since 1.0
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "Authentication Management", description = "Endpoints for user session lifecycle, including login and logout operations")
public class UserController {

    @Resource
    private SysUserService sysUserService;

    /**
     * Logs out the current user by invalidating the session
     * and redirects to the login page.
     *
     * @param session HTTP session
     * @throws IOException      if an input/output error occurs
     * @throws ServletException if a servlet error occurs
     */
    @Operation(
            summary = "User Logout",
            description = "Invalidates the current HTTP session and clears the security context. Redirects to the login page upon completion."
    )
    @GetMapping("/logout.page")
    public String logout(HttpSession session) {

        // Log handled by HttpAspect; logic focuses on invalidation
        session.invalidate();
        return "redirect:signin.jsp";

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
     * @param request  the current HTTP request used for session management
     * @return {@link ModelAndView} directing to the dashboard on success,
     * or returning to the sign-in page with error metadata on failure
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
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        SysUser sysUser = sysUserService.findByKeyword(username);
        String errorMsg = "";
        String ret = request.getParameter("ret");

        if (StringUtils.isBlank(username)) {
            errorMsg = "User name cannot be null";
        } else if (StringUtils.isBlank(password)) {
            errorMsg = "Password cannot be null";
        } else if (sysUser == null) {
            errorMsg = "Cannot find the user in the system";
        } else if (!sysUser.getPassword().equals(MD5Util.encrypt(password))) {
            errorMsg = "Incorrect username or password";
        } else if (sysUser.getStatus() != 1) {
            errorMsg = "The user account has been suspended. Please contact the administrator.";
        } else {
            // login success
            request.getSession().setAttribute("user", sysUser);
            if (StringUtils.isNotBlank(ret)) {
                response.sendRedirect(ret);
            } else {
                response.sendRedirect("/admin/index.page");
            }
            return;
        }

        request.setAttribute("error", errorMsg);
        request.setAttribute("username", username);
        if (StringUtils.isNotBlank(ret)) {
            request.setAttribute("ret", ret);
        }
        String path = "signin.jsp";
        request.getRequestDispatcher(path).forward(request, response);
    }
}
