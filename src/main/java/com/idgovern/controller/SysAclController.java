package com.idgovern.controller;

import com.google.common.collect.Maps;
import com.idgovern.beans.PageQuery;
import com.idgovern.common.JsonData;
import com.idgovern.common.RequestHolder;
import com.idgovern.model.SysRole;
import com.idgovern.model.SysUser;
import com.idgovern.param.AclParam;
import com.idgovern.service.SysAclService;
import com.idgovern.service.SysRoleService;
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
import java.util.List;
import java.util.Map;

/**
 * Controller for managing Access Control List (ACL) entries.
 *
 * <p>
 * Provides endpoints for creating, updating, querying, and listing ACLs
 * as part of the Role-Based Access Control (RBAC) system.
 * </p>
 *
 * <p>
 * Each ACL entry represents a specific permission that can be assigned
 * to roles and users.
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author   | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-02-28| Lilian S.| Initial creation of SysAclController |
 * | 1.1     | 2026-01-26 | Updated  | Professional Logging Strategy added|
 * | 1.2     | 2026-01-28 | Updated  | Controllers annotated with Swagger for better API documentation|
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */
@Controller
@RequestMapping("/sys/acl")
@Slf4j
@Tag(name = "ACL Management", description = "Operations related to Access Control List (ACL) entries")
@SecurityRequirement(name = "bearerAuth") // Indicates these APIs require authentication
public class SysAclController {

    @Resource
    private SysAclService sysAclService;

    @Resource
    private SysRoleService sysRoleService;


    /**
     * Create a new ACL entry.
     *
     * @param param the ACL parameters to save
     * @return success response
     */
    @Operation(summary = "Create ACL", description = "Creates a new permission entry within a specific module.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ACL created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions")
    })
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveAclModule(@Parameter(description = "ACL details") AclParam param) {
        SysUser operator = RequestHolder.getCurrentUser();

        log.info("ACL create request: operator={}, aclName={}, aclModuleId={}",
                operator != null ? operator.getUsername() : "SYSTEM",
                param.getName(),
                param.getAclModuleId());

        try {

            sysAclService.save(param);

            log.info("ACL created successfully: operator={}, aclName={}, aclModuleId={}",
                    operator != null ? operator.getUsername() : "SYSTEM",
                    param.getName(),
                    param.getAclModuleId());

            return JsonData.success();

        } catch (Exception e) {

            log.error("ACL creation failed: operator={}, param={}",
                    operator != null ? operator.getUsername() : "SYSTEM",
                    param,
                    e);

            return JsonData.fail("ACL creation failed");
        }
    }


    /**
     * Update an existing ACL entry.
     *
     * @param param the ACL parameters to update
     * @return success response
     */
    @Operation(summary = "Update ACL", description = "Updates an existing ACL entry. Changes are tracked for audit purposes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ACL updated successfully"),
            @ApiResponse(responseCode = "404", description = "ACL entry not found"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions to update")
    })
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateAclModule(@Parameter(description = "Updated ACL details") AclParam param) {

        SysUser operator = RequestHolder.getCurrentUser();

        log.info("ACL update request: operator={}, aclId={}, aclName={}",
                operator != null ? operator.getUsername() : "SYSTEM",
                param.getId(),
                param.getName());

        try {

            sysAclService.update(param);

            log.info("ACL updated successfully: operator={}, aclId={}, aclName={}",
                    operator != null ? operator.getUsername() : "SYSTEM",
                    param.getId(),
                    param.getName());

            return JsonData.success();

        } catch (Exception e) {

            log.error("ACL update failed: operator={}, param={}",
                    operator != null ? operator.getUsername() : "SYSTEM",
                    param,
                    e);

            return JsonData.fail("ACL update failed");
        }

    }


    /**
     * Get paginated ACLs under a specific ACL module.
     *
     * @param aclModuleId the ID of the ACL module
     * @param pageQuery   pagination information
     * @return paginated ACL list
     */
    @Operation(summary = "Get Paginated ACLs", description = "Retrieves a paginated list of permissions belonging to a specific ACL module.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Page retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid module ID or pagination parameters")
    })
    @RequestMapping("/page.json")
    @ResponseBody
    public JsonData list(@Parameter(description = "ID of the parent ACL module", required = true, example = "1") @RequestParam("aclModuleId") Integer aclModuleId,
                         @Parameter(description = "Pagination and sorting criteria") PageQuery pageQuery) {

        SysUser operator = RequestHolder.getCurrentUser();

        log.debug("ACL page query: operator={}, aclModuleId={}, page={}, size={}",
                operator != null ? operator.getUsername() : "SYSTEM",
                aclModuleId,
                pageQuery.getPageNo(),
                pageQuery.getPageSize());

        try {

            Object result = sysAclService.getPageByAclModuleId(aclModuleId, pageQuery);

            log.debug("ACL page query success: aclModuleId={}, resultSize={}",
                    aclModuleId,
                    result != null);

            return JsonData.success(result);

        } catch (Exception e) {

            log.error("ACL page query failed: operator={}, aclModuleId={}",
                    operator != null ? operator.getUsername() : "SYSTEM",
                    aclModuleId,
                    e);

            return JsonData.fail("Query failed");
        }
    }


    /**
     * Retrieve roles and users associated with a specific ACL.
     *
     * @param aclId the ID of the ACL
     * @return a map containing roles and their corresponding users
     */
    @Operation(summary = "Retrieve ACL Associations", description = "Fetches the roles and users currently associated with a specific permission ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Associations retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "ACL ID does not exist")
    })
    @RequestMapping("acls.json")
    @ResponseBody
    public JsonData acls(@Parameter(description = "The unique ID of the ACL entry", required = true, example = "10")
                             @RequestParam("aclId") int aclId) {

        SysUser operator = RequestHolder.getCurrentUser();

        log.debug("ACL detail query: operator={}, aclId={}",
                operator != null ? operator.getUsername() : "SYSTEM",
                aclId);

        try {

            Map<String, Object> map = Maps.newHashMap();

            List<SysRole> roleList = sysRoleService.getRoleListByAclId(aclId);
            map.put("roles", roleList);
            map.put("users", sysRoleService.getUserListByRoleList(roleList));

            log.debug("ACL detail query success: aclId={}, roleCount={}",
                    aclId,
                    roleList != null ? roleList.size() : 0);

            return JsonData.success(map);

        } catch (Exception e) {

            log.error("ACL detail query failed: operator={}, aclId={}",
                    operator != null ? operator.getUsername() : "SYSTEM",
                    aclId,
                    e);

            return JsonData.fail("Query failed");
        }
    }
}
