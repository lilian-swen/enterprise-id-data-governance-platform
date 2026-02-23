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
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
@Controller
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
     * @param username the user-provided identifier (email or username)
     * @param password the plain-text password to be encrypted and verified
     * @param ret      optional redirect URL for post-authentication navigation
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
    @PostMapping("/login.page")
    public ModelAndView login(@RequestParam("username") String username,
                              @RequestParam("password") String password,
                              @RequestParam(value = "ret", required = false) String ret,
                              HttpServletRequest request) {

        ModelAndView mav = new ModelAndView("signin.jsp");
        mav.addObject("username", username);
        mav.addObject("ret", ret);

        // Validation - Business logic delegates to service or keeps concise here
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            mav.addObject("error", "Username and password cannot be empty");
            return mav;
        }

        SysUser sysUser = sysUserService.findByKeyword(username);

        // Security check logic
        if (sysUser == null || !sysUser.getPassword().equals(MD5Util.encrypt(password))) {
            mav.addObject("error", "Incorrect username or password");
            return mav;
        }

        if (sysUser.getStatus() != 1) {
            mav.addObject("error", "Account is disabled");
            return mav;
        }

        // Establish Session
        request.getSession().setAttribute(SysUser.SESSION_USER_KEY, sysUser);

        // Redirect logic
        String redirectUrl = StringUtils.isNotBlank(ret) ? "redirect:" + ret : "redirect:/admin/index.page";
        return new ModelAndView(redirectUrl);
    }
}
