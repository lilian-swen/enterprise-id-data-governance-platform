package com.idgovern.controller;

import com.google.common.collect.Maps;
import com.idgovern.dto.PageQuery;
import com.idgovern.common.JsonData;
import com.idgovern.model.SysRole;
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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import static net.logstash.logback.argument.StructuredArguments.kv;



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
 * | 1.1     | 2026-01-26| Lilian S.  | Professional Logging Strategy added|
 * | 1.2     | 2026-01-28| Lilian S. | Controllers annotated with Swagger for better API documentation|
 * | 1.3     | 2026-02-22| Lilian S.| Refactored for AOP & Clean Architecture |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.3
 * @since 1.0
 */
@Slf4j
@RestController
@RequestMapping("/sys/acl")
@RequiredArgsConstructor
@Tag(name = "ACL Management", description = "Operations for permission-level access control")
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
    @PostMapping("/save.json")
    public JsonData saveAclModule(@Parameter(description = "ACL details") @Valid AclParam param) {

        log.info("Creating ACL",
                kv("event", "ACL_CREATE"),
                kv("aclName", param.getName()),
                kv("aclModuleId", param.getAclModuleId()),
                kv("type", param.getType()),
                kv("status", param.getStatus())
        );

        sysAclService.save(param);

        log.info("ACL created successfully",
                kv("event", "ACL_CREATE_SUCCESS"),
                kv("aclName", param.getName()),
                kv("aclModuleId", param.getAclModuleId())
        );

        return JsonData.success();
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
    @PostMapping("/update.json")
    public JsonData updateAclModule(@Parameter(description = "Updated ACL details") @Valid @RequestBody AclParam param) {

        log.info("Updating ACL",
                kv("event", "ACL_UPDATE"),
                kv("aclId", param.getId()),
                kv("aclName", param.getName()),
                kv("aclModuleId", param.getAclModuleId())
        );

        sysAclService.update(param);

        log.info("ACL updated successfully",
                kv("event", "ACL_UPDATE_SUCCESS"),
                kv("aclId", param.getId())
        );

        return JsonData.success();
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
    @GetMapping("/page.json")
    public JsonData list(@Parameter(description = "ID of the parent ACL module", required = true, example = "1") @RequestParam("aclModuleId") Integer aclModuleId,
                         @Parameter(description = "Pagination and sorting criteria") @Valid PageQuery pageQuery) {

        log.debug("Fetching ACL page",
                kv("event", "ACL_PAGE_QUERY"),
                kv("aclModuleId", aclModuleId),
                kv("pageNo", pageQuery.getPageNo()),
                kv("pageSize", pageQuery.getPageSize())
        );

        return JsonData.success(sysAclService.getPageByAclModuleId(aclModuleId, pageQuery));
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
    @GetMapping("/acls.json")
    public JsonData acls(@Parameter(description = "The unique ID of the ACL entry", required = true, example = "10")
                             @RequestParam("aclId") int aclId) {

        log.debug("Fetching ACL associations",
                kv("event", "ACL_ASSOC_QUERY"),
                kv("aclId", aclId)
        );

        Map<String, Object> map = Maps.newHashMap();
        List<SysRole> roleList = sysRoleService.getRoleListByAclId(aclId);
        map.put("roles", roleList);
        map.put("users", sysRoleService.getUserListByRoleList(roleList));

        log.debug("ACL associations retrieved",
                kv("event", "ACL_ASSOC_QUERY_SUCCESS"),
                kv("aclId", aclId),
                kv("roleCount", roleList.size())
        );

        return JsonData.success(map);
    }
}
