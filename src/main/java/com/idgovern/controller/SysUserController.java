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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
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
 * | 1.1     | 2016-02-26 | Lilian S.| Professional documentation added|
 * | 1.2     | 2026-02-22 | Lilian S.| Refactored for AOP & Clean Architecture |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.2
 * @since 1.0
 */
@RestController
@RequestMapping("/sys/user")
@RequiredArgsConstructor
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
    @GetMapping("/noAuth.page")
    public ModelAndView noAuth() {

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
    @PostMapping("/save.json")
    public JsonData saveUser(@Parameter(description = "New user account details") @Valid @RequestBody UserParam param) {

        sysUserService.save(param);
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
    @PutMapping("/update.json")
    public JsonData updateUser(@Parameter(description = "Updated user profile object") @Valid @RequestBody UserParam param) {

        sysUserService.update(param);
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
    @GetMapping("/page.json")
    public JsonData page(@Parameter(description = "ID of the target department", required = true, example = "1") @RequestParam("deptId") int deptId,
                         @Parameter(description = "Pagination configuration (pageNo, pageSize)") @Valid PageQuery pageQuery) {

        PageResult<SysUser> result = sysUserService.getPageByDeptId(deptId, pageQuery);
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
    @GetMapping("/acls.json")
    public JsonData acls(@Parameter(description = "The unique user ID to query", required = true) @RequestParam("userId") int userId) {

        Map<String, Object> map = Maps.newHashMap();
        map.put("acls", sysTreeService.userAclTree(userId));
        map.put("roles", sysRoleService.getRoleListByUserId(userId));
        return JsonData.success(map);

    }
}
