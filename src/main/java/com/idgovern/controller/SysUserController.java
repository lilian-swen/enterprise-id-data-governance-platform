package com.idgovern.controller;

import com.google.common.collect.Maps;
import com.idgovern.dto.PageQuery;
import com.idgovern.dto.PageResult;
import com.idgovern.common.JsonData;
import com.idgovern.model.SysUser;
import com.idgovern.param.UserParam;
import com.idgovern.service.SysRoleService;
import com.idgovern.service.SysTreeService;
import com.idgovern.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import java.util.Map;


/**
 * System User Controller.
 *
 * <p>
 * This controller handles HTTP requests related to system user management
 * within the RBAC (Role-Based Access Control) module.
 * </p>
 *
 * <p>
 * It provides endpoints for:
 * <ul>
 *     <li>User creation</li>
 *     <li>User update</li>
 *     <li>Department-based user pagination</li>
 *     <li>User role and permission (ACL) retrieval</li>
 * </ul>
 * </p>
 *
 * <p>
 * All JSON-based responses are wrapped using {@link JsonData}
 * to ensure consistent API response formatting.
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author   | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-02-18 | Lilian S.| Initial creation                |
 * | 1.1     | 2016-02-26 | Updated  | Professional documentation added|
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.1
 * @since 1.0
 */
@Slf4j
@Controller
@RequestMapping("/sys/user")
@Tag(name = "User Management", description = "Operations for managing system users, including account lifecycle and authorization tracking")
@SecurityRequirement(name = "bearerAuth")
public class SysUserController {

    /**
     * User service for handling user-related business logic.
     */
    @Resource
    private SysUserService sysUserService;

    /**
     * Tree service for building user permission (ACL) trees.
     */
    @Resource
    private SysTreeService sysTreeService;

    /**
     * Role service for retrieving user role information.
     */
    @Resource
    private SysRoleService sysRoleService;


    /**
     * Redirects to the "no authorization" page.
     *
     * @return ModelAndView for noAuth page
     */
    @Operation(summary = "Render Access Denied Page", description = "Redirects users to the unauthorized access landing page.")
    @RequestMapping("/noAuth.page")
    public ModelAndView noAuth() {

        log.warn("Unauthorized access attempt");
        return new ModelAndView("noAuth");
    }


    /**
     * Creates a new system user.
     *
     * <p>
     * Accepts user input parameters, validates them (via annotations in
     * {@link UserParam}), and delegates persistence logic to the service layer.
     * </p>
     *
     * @param param user creation parameters
     * @return success response
     */
    @Operation(summary = "Create System User", description = "Onboards a new user to the platform. Validates unique constraints for username and email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error (e.g., email already exists)")
    })
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveUser(@Parameter(description = "New user account details") UserParam param) {

        log.info("Creating new user: {}", param.getUsername());
        sysUserService.save(param);
        log.info("User '{}' created successfully", param.getUsername());
        return JsonData.success();
    }


    /**
     * Updates an existing system user.
     *
     * <p>
     * Updates user information based on the provided parameters.
     * Validation is handled at the parameter level.
     * </p>
     *
     * @param param user update parameters
     * @return success response
     */
    @Operation(summary = "Update User Details", description = "Modifies existing user profile information, status, or department assignment.")
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateUser(@Parameter(description = "Updated user profile object") UserParam param) {

        log.info("Updating user: id={}, username={}", param.getId(), param.getUsername());
        sysUserService.update(param);
        log.info("User '{}' updated successfully", param.getUsername());
        return JsonData.success();
    }


    /**
     * Retrieves paginated users by department ID.
     *
     * <p>
     * This endpoint supports pagination and filtering by department.
     * It returns a {@link PageResult} containing user records.
     * </p>
     *
     * @param deptId   department ID
     * @param pageQuery pagination parameters (page number, page size)
     * @return paginated user result
     */
    @Operation(summary = "Get Paginated Users", description = "Retrieves a filtered list of users belonging to a specific department.")
    @RequestMapping("/page.json")
    @ResponseBody
    public JsonData page(@Parameter(description = "ID of the target department", required = true, example = "1") @RequestParam("deptId") int deptId,
                         @Parameter(description = "Pagination configuration (pageNo, pageSize)") PageQuery pageQuery) {

        log.debug("Fetching users for deptId={} with pageQuery={}", deptId, pageQuery);
        PageResult<SysUser> result = sysUserService.getPageByDeptId(deptId, pageQuery);
        log.debug("Fetched {} users for deptId={}", result.getData().size(), deptId);
        return JsonData.success(result);

    }


    /**
     * Retrieves user permissions (ACLs) and assigned roles.
     *
     * <p>
     * This endpoint returns:
     * <ul>
     *     <li>Permission tree (ACL structure)</li>
     *     <li>List of roles assigned to the user</li>
     * </ul>
     * </p>
     *
     * @param userId user ID
     * @return map containing ACL tree and role list
     */
    @Operation(
            summary = "Query User Permissions & Roles",
            description = "Retrieves the full ACL permission tree for a user and a list of their assigned roles for security auditing."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authorization data retrieved"),
            @ApiResponse(responseCode = "404", description = "User ID not found")
    })
    @RequestMapping("/acls.json")
    @ResponseBody
    public JsonData acls(@Parameter(description = "The unique user ID to query", required = true) @RequestParam("userId") int userId) {

        log.debug("Fetching ACLs and roles for userId={}", userId);
        Map<String, Object> map = Maps.newHashMap();
        map.put("acls", sysTreeService.userAclTree(userId));
        map.put("roles", sysRoleService.getRoleListByUserId(userId));
        log.debug("Fetched ACLs and roles for userId={}", userId);
        return JsonData.success(map);

    }
}
