package com.idgovern.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;



/**
 * Controller for handling admin dashboard requests.
 *
 * <p>
 * Provides endpoints for the main administration page.
 * Typically used as the landing page after successful admin login.
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author   | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-02-17 | Lilian S.| Initial creation of AdminController |
 * | 1.1     | 2026-01-28 | Updated  | Controllers annotated with Swagger for better API documentation|
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Controller
@RequestMapping("/admin")
@Tag(name = "Admin UI Management", description = "Endpoints for managing and rendering the Administrative Dashboard")
public class AdminController {

    /**
     * Handles the request for the main admin dashboard page.
     *
     * @return ModelAndView object pointing to "admin" view
     */
    @Operation(
            summary = "Render Admin Dashboard",
            description = "Returns the administrative landing page view. Requires active admin session."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully rendered admin dashboard"),
            @ApiResponse(responseCode = "302", description = "Redirect to login if session is invalid"),
            @ApiResponse(responseCode = "403", description = "Access denied for non-admin users")
    })
    @RequestMapping("index.page")
    public ModelAndView index() {

        // INFO: business-level event
        log.info("Admin dashboard access request received");

        // DEBUG: developer diagnostic info
        if (log.isDebugEnabled()) {
            log.debug("Rendering admin dashboard view: admin.jsp");
        }

        ModelAndView mav = new ModelAndView("admin");

        // INFO: successful processing
        log.info("Admin dashboard rendered successfully");

        return mav;
    }
}
