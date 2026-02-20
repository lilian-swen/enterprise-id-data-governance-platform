package com.idgovern.controller;

import com.google.common.collect.Lists;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;



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
 * | 1.1     | 2026-01-26 | Updated  | Professional Logging Strategy added|
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Controller
@RequestMapping("/sys/role")
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
    @RequestMapping("role.page")
    public ModelAndView page() {

        log.info("Accessing role management page");
        return new ModelAndView("role");
    }


    /**
     * Creates a new role.
     *
     * @param param role request parameters
     * @return success response in JSON format
     */
    @Operation(summary = "Create Role", description = "Adds a new role to the system.")
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveRole(@Parameter(description = "Role creation details") RoleParam param) {

        log.info("Creating new role: {}", param);
        sysRoleService.save(param);
        log.info("Role created successfully: {}", param.getName());
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
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateRole(@Parameter(description = "Updated role object including ID", required = true) RoleParam param) {

        log.info("Updating role: {}", param);
        sysRoleService.update(param);
        log.info("Role updated successfully: id={}", param.getId());
        return JsonData.success();
    }


    /**
     * Retrieves all roles in the system.
     *
     * @return JSON response containing role list
     */
    @Operation(summary = "Get All Roles", description = "Retrieves a list of all defined roles in the system.")
    @RequestMapping("/list.json")
    @ResponseBody
    public JsonData list() {

        log.debug("Fetching all roles");
        List<?> roles = sysRoleService.getAll();
        log.debug("Total roles fetched: {}", roles.size());

        return JsonData.success(roles);
    }


    /**
     * Retrieves the permission tree for a specific role.
     *
     * @param roleId role identifier
     * @return JSON response containing permission tree structure
     */
    @Operation(summary = "Get Role Permission Tree", description = "Retrieves the full ACL tree highlighting permissions currently assigned to the specified role.")
    @RequestMapping("/roleTree.json")
    @ResponseBody
    public JsonData roleTree(@Parameter(description = "Target Role ID", required = true) @RequestParam("roleId") int roleId) {

        log.debug("Fetching permission tree for roleId={}", roleId);
        return JsonData.success(sysTreeService.roleTree(roleId));
    }


    /**
     * Updates the permission (ACL) assignments of a role.
     *
     * @param roleId role identifier
     * @param aclIds comma-separated ACL ID list
     * @return success response in JSON format
     */
    @Operation(summary = "Update Role Permissions", description = "Replaces the current ACL assignments for a role with a new set of permissions.")
    @RequestMapping("/changeAcls.json")
    @ResponseBody
    public JsonData changeAcls(@Parameter(description = "Target Role ID", required = true) @RequestParam("roleId") int roleId,
                               @Parameter(description = "Comma-separated list of ACL IDs") @RequestParam(value = "aclIds", required = false, defaultValue = "") String aclIds) {

        log.info("Updating ACLs for roleId={} with aclIds={}", roleId, aclIds);
        List<Integer> aclIdList = StringUtil.splitToListInt(aclIds);
        sysRoleAclService.changeRoleAcls(roleId, aclIdList);
        log.info("ACLs updated successfully for roleId={}", roleId);
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
    @RequestMapping("/changeUsers.json")
    @ResponseBody
    public JsonData changeUsers(@Parameter(description = "Target Role ID", required = true) @RequestParam("roleId") int roleId,
                                @Parameter(description = "Comma-separated list of User IDs") @RequestParam(value = "userIds", required = false, defaultValue = "") String userIds) {

        log.info("Updating users for roleId={} with userIds={}", roleId, userIds);
        List<Integer> userIdList = StringUtil.splitToListInt(userIds);
        sysRoleUserService.changeRoleUsers(roleId, userIdList);
        log.info("Users updated successfully for roleId={} ({} users assigned)", roleId, userIdList.size());

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
    @RequestMapping("/users.json")
    @ResponseBody
    public JsonData users(@Parameter(description = "Target Role ID", required = true) @RequestParam("roleId") int roleId) {

        log.debug("Fetching selected and unselected users for roleId={}", roleId);

        // Users currently assigned to the role
        List<SysUser> selectedUserList = sysRoleUserService.getListByRoleId(roleId);

        // All users in the system
        List<SysUser> allUserList = sysUserService.getAll();

        List<SysUser> unselectedUserList = Lists.newArrayList();

        Set<Integer> selectedUserIdSet = selectedUserList.stream()
                .map(sysUser -> sysUser.getId())
                .collect(Collectors.toSet());

        /*Set<Integer> selectedUserIdSet = selectedUserList.stream().
                map(sysUser::sysUser.getId()).
                collect(Collectors.toSet());*/

        // Identify active users not assigned to the role
        for(SysUser sysUser : allUserList) {
            if (sysUser.getStatus() == 1 && !selectedUserIdSet.contains(sysUser.getId())) {
                unselectedUserList.add(sysUser);
            }
        }

        // selectedUserList = selectedUserList.stream().security(sysUser -> sysUser.getStatus() != 1).collect(Collectors.toList());

        // Build response structure
        Map<String, List<SysUser>> map = Maps.newHashMap();
        map.put("selected", selectedUserList);
        map.put("unselected", unselectedUserList);

        log.debug("Selected users: {}, Unselected users: {}", selectedUserList.size(), unselectedUserList.size());

        return JsonData.success(map);
    }
}
