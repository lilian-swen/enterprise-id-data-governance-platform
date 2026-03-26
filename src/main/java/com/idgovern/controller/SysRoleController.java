package com.idgovern.controller;

import com.google.common.collect.Maps;
import com.idgovern.common.JsonData;
import com.idgovern.model.SysUser;
import com.idgovern.param.RoleParam;
import com.idgovern.service.SysRoleAclService;
import com.idgovern.service.SysRoleService;
import com.idgovern.service.SysRoleUserService;
import com.idgovern.service.SysTreeService;
import com.idgovern.service.SysUserService;
import com.idgovern.util.StringUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import static net.logstash.logback.argument.StructuredArguments.kv;


/**
 * Role Management Controller.
 *
 * <p>
 * Handles role-related operations within the RBAC (Role-Based Access Control)
 * system, including role creation, update, permission assignment,
 * user-role association, and role tree retrieval.
 * </p>
 *
 * <p>
 * Provides web endpoints for managing role authorization
 * and maintaining access control relationships.
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author    | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-02-28 | Lilian S. | Initial creation                |
 * | 1.1     | 2026-01-26 | Lilian S. | Professional Logging Strategy added|
 * | 1.2     | 2026-02-22 | Lilian S. | Refactored for AOP & Clean Architecture |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.2
 * @since 1.0
 */
@Slf4j
@RestController
@RequestMapping("/sys/role")
@RequiredArgsConstructor
@Tag(name = "Role Management", description = "Operations for managing roles and their associations with users and permissions")
@SecurityRequirement(name = "bearerAuth")
public class SysRoleController {

    /**
     * Service for core role CRUD operations.
     */
    @Resource
    private SysRoleService sysRoleService;

    /**
     * Service for building permission tree structures.
     */
    @Resource
    private SysTreeService sysTreeService;

    /**
     * Service for managing role-permission (ACL) relationships.
     */
    @Resource
    private SysRoleAclService sysRoleAclService;

    /**
     * Service for managing role-user relationships.
     */
    @Resource
    private SysRoleUserService sysRoleUserService;

    /**
     * Service for user-related operations.
     */
    @Resource
    private SysUserService sysUserService;


    /**
     * Loads the role management page.
     *
     * @return ModelAndView pointing to the role page
     */
    @Operation(summary = "Render Role Page", description = "Returns the JSP view for the role management dashboard.")
    @GetMapping("/role.page")
    public ModelAndView page() {

        log.info("Loading role management page", kv("event", "ROLE_PAGE_LOAD"));

        return new ModelAndView("role");
    }


    /**
     * Creates a new role.
     *
     * @param param role request parameters
     * @return success response in JSON format
     */
    @Operation(summary = "Create Role", description = "Adds a new role to the system.")
    @PostMapping("/save.json")
    public JsonData saveRole(@Parameter(description = "Role creation details") @Valid RoleParam param) {

        sysRoleService.save(param);
        log.info("Created new role", kv("event", "ROLE_CREATE"), kv("roleName", param.getName()));

        return JsonData.success();
    }


    /**
     * Updates an existing role.
     *
     * @param param role update parameters
     * @return success response in JSON format
     */
    @Operation(
            summary = "Update Role Metadata",
            description = "Updates the basic information of a role, such as name, status, and remarks. Does not affect ACL or User mappings."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid role parameters (e.g., duplicate role name)"),
            @ApiResponse(responseCode = "404", description = "Target role ID not found"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions to modify roles")
    })
    @PostMapping("/update.json")
    public JsonData updateRole(@Parameter(description = "Updated role object including ID", required = true) @Valid RoleParam param) {

        sysRoleService.update(param);
        log.info("Updated role metadata",
                kv("event", "ROLE_UPDATE"),
                kv("roleId", param.getId()),
                kv("roleName", param.getName()));

        return JsonData.success();
    }


    /**
     * Retrieves all roles in the system.
     *
     * @return JSON response containing role list
     */
    @Operation(summary = "Get All Roles", description = "Retrieves a list of all defined roles in the system.")
    @GetMapping("/list.json")
    public JsonData list() {

        List<?> roles = sysRoleService.getAll();
        log.info("Retrieved all roles", kv("event", "ROLE_LIST"), kv("count", roles.size()));

        return JsonData.success(roles);
    }


    /**
     * Retrieves the permission tree for a specific role.
     *
     * @param roleId role identifier
     * @return JSON response containing permission tree structure
     */
    @Operation(summary = "Get Role Permission Tree", description = "Retrieves the full ACL tree highlighting permissions currently assigned to the specified role.")
    @GetMapping("/roleTree.json")
    public JsonData roleTree(@Parameter(description = "Target Role ID", required = true) @RequestParam("roleId") int roleId) {

        Object tree = sysTreeService.roleTree(roleId);
        log.info("Fetched role permission tree", kv("event", "ROLE_TREE_FETCH"), kv("roleId", roleId));
        return JsonData.success(tree);
    }


    /**
     * Updates the permission (ACL) assignments of a role.
     *
     * @param roleId role identifier
     * @param aclIds comma-separated ACL ID list
     * @return success response in JSON format
     */
    @Operation(summary = "Update Role Permissions", description = "Replaces the current ACL assignments for a role with a new set of permissions.")
    @PostMapping("/changeAcls.json")
    public JsonData changeAcls(@Parameter(description = "Target Role ID", required = true) @RequestParam("roleId") int roleId,
                               @Parameter(description = "Comma-separated list of ACL IDs") @RequestParam(value = "aclIds", required = false, defaultValue = "") String aclIds) {

        List<Integer> aclIdList = StringUtil.splitToListInt(aclIds);
        sysRoleAclService.changeRoleAcls(roleId, aclIdList);

        log.info("Updated role ACL assignments", kv("event", "ROLE_ACL_UPDATE"), kv("roleId", roleId), kv("aclIds", aclIdList));
        return JsonData.success();
    }


    /**
     * Updates the user assignments of a role.
     *
     * @param roleId role identifier
     * @param userIds comma-separated user ID list
     * @return success response in JSON format
     */
    @Operation(summary = "Update Role Users", description = "Updates the set of users assigned to a specific role.")
    @PostMapping("/changeUsers.json")
    public JsonData changeUsers(@Parameter(description = "Target Role ID", required = true) @RequestParam("roleId") int roleId,
                                @Parameter(description = "Comma-separated list of User IDs") @RequestParam(value = "userIds", required = false, defaultValue = "") String userIds) {

        List<Integer> userIdList = StringUtil.splitToListInt(userIds);
        sysRoleUserService.changeRoleUsers(roleId, userIdList);

        log.info("Updated role-user assignments", kv("event", "ROLE_USER_UPDATE"), kv("roleId", roleId), kv("userIds", userIdList));
        return JsonData.success();

    }


    /**
     * Retrieves selected and unselected users for a given role.
     *
     * <p>
     * Selected users: users already assigned to the role.
     * Unselected users: active users not assigned to the role.
     * </p>
     *
     * @param roleId role identifier
     * @return JSON response containing selected and unselected user lists
     */
    @Operation(summary = "Get Role User Assignments", description = "Retrieves two lists: users already assigned to the role and active users available for assignment.")
    @GetMapping("/users.json")
    public JsonData users(@Parameter(description = "Target Role ID", required = true) @RequestParam("roleId") int roleId) {

        // Users currently assigned to the role
        List<SysUser> selectedUserList = sysRoleUserService.getListByRoleId(roleId);
        // All users in the system
        List<SysUser> allUserList = sysUserService.getAll();

        Set<Integer> selectedUserIdSet = selectedUserList.stream()
                .map(SysUser::getId)
                .collect(Collectors.toSet());

        // Identify active users not assigned to the role
        List<SysUser> unselectedUserList = allUserList.stream()
                .filter(user -> user.getStatus() == 1 && !selectedUserIdSet.contains(user.getId()))
                .collect(Collectors.toList());

        // Build response structure
        Map<String, List<SysUser>> map = Maps.newHashMap();
        map.put("selected", selectedUserList);
        map.put("unselected", unselectedUserList);

        log.info("Fetched role user assignments", kv("event", "ROLE_USER_FETCH"), kv("roleId", roleId),
                kv("selectedCount", selectedUserList.size()), kv("unselectedCount", unselectedUserList.size()));

        return JsonData.success(map);
    }
}
