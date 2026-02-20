package com.idgovern.controller;

import com.idgovern.beans.PageQuery;
import com.idgovern.common.JsonData;
import com.idgovern.param.SearchLogParam;
import com.idgovern.service.SysLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


/**
 * Controller for system operation logs management.
 *
 * <p>
 * Provides endpoints to view logs, search logs with filters, and
 * recover actions or entities from logs when necessary. This
 * supports auditing and monitoring activities within the RBAC system.
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author   | Description                           |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-02-28| Lilian S.| Initial creation of SysLogController  |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */
@Controller
@RequestMapping("/sys/log")
@Tag(name = "Audit Log Management", description = "Operations for system auditing, activity tracking, and record recovery")
@SecurityRequirement(name = "bearerAuth")
public class SysLogController {

    @Resource
    private SysLogService sysLogService;


    /**
     * Render the log management page.
     *
     * @return ModelAndView for the log page
     */
    @Operation(summary = "Render Log Page", description = "Returns the JSP view for the system audit log dashboard.")
    @RequestMapping("/log.page")
    public ModelAndView page() {
        return new ModelAndView("log");
    }


    /**
     * Recover a record or action from the logs by its ID.
     *
     * @param id the ID of the log entry to recover
     * @return success response
     */
    @Operation(
            summary = "Recover From Log",
            description = "Reverts a system entity (User, Dept, Role, etc.) to the state captured in the specified log entry."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Action recovered successfully"),
            @ApiResponse(responseCode = "400", description = "Recovery failed due to invalid log ID or data inconsistency"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions to perform recovery")
    })
    @RequestMapping("/recover.json")
    @ResponseBody
    public JsonData recover(@Parameter(description = "ID of the log entry to revert to", required = true, example = "500") @RequestParam("id") int id) {
        sysLogService.recover(id);
        return JsonData.success();
    }


    /**
     * Search logs with optional filters and pagination.
     *
     * @param param search criteria encapsulated in SearchLogParam
     * @param page pagination parameters
     * @return paginated list of logs matching the search criteria
     */
    @Operation(
            summary = "Search Audit Logs",
            description = "Retrieves a paginated list of system operation logs based on filters like operator, target type, or time range."
    )
    @RequestMapping("/page.json")
    @ResponseBody
    public JsonData searchPage(SearchLogParam param, PageQuery page) {
        return JsonData.success(sysLogService.searchPageList(param, page));
    }
}
