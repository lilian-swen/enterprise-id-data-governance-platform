package com.idgovern.controller;

import com.idgovern.common.JsonData;
import com.idgovern.param.AclModuleParam;
import com.idgovern.service.SysAclModuleService;
import com.idgovern.service.SysTreeService;
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


/**
 * Controller for managing Access Control List (ACL) Modules.
 * This implementation adheres to RESTful standards and utilizes AOP for
 * automated logging and global exception handling.
 *
 * <p>
 * Provides endpoints for creating, updating, deleting, and retrieving
 * ACL modules in a hierarchical tree structure as part of the
 * Role-Based Access Control (RBAC) system.
 * </p>
 *
 * <p>
 * An ACL module represents a group or category of permissions, which
 * helps organize ACLs for different functional areas of the system.
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author   | Description                        |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-02-28 | Lilian S.| Initial creation of SysAclModuleController|
 * | 1.1     | 2026-01-26 | Lilian S.| Professional Logging Strategy added |
 * | 1.2     | 2026-02-22 | Lilian S.| Enhanced Swagger/OpenAPI metadata. Refactored for Spring Boot 3 & AOP |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.2
 * @since 1.0
 */
@RestController
@RequestMapping("/sys/aclModule")
@RequiredArgsConstructor // Generates constructor for final fields (Constructor Injection)
@Tag(name = "ACL Module Management", description = "Operations for hierarchical grouping of permissions for RBAC")
@SecurityRequirement(name = "bearerAuth")
public class SysAclModuleController {

    @Resource
    private SysAclModuleService sysAclModuleService;

    @Resource
    private SysTreeService sysTreeService;


    /**
     * Render the ACL module management page.
     *
     * @return ModelAndView for the ACL management page
     */
    @Operation(summary = "Render ACL Module Page", description = "Returns the JSP view for managing ACL modules management UI.")
    @GetMapping("/acl.page")
    public ModelAndView page() {

        return new ModelAndView("acl");
    }


    /**
     * Create a new ACL module.
     *
     * @param param the ACL module parameters
     * @return success response
     */
    @Operation(summary = "Create ACL Module", description = "Adds a new category to the permission hierarchy.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Module created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid module hierarchy or data")
    })
    @PostMapping("/save.json")
    public JsonData saveAclModule(@Parameter(description = "Module creation parameters") @Valid @RequestBody AclModuleParam param) {

        sysAclModuleService.save(param);
        return JsonData.success();
    }


    /**
     * Update an existing ACL module.
     *
     * @param param the ACL module parameters
     * @return success response
     */
    @Operation(
            summary = "Update ACL Module",
            description = "Updates the name, parent ID, or display order of an existing ACL module. All changes are logged for auditing."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Module updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters (e.g., circular hierarchy)"),
            @ApiResponse(responseCode = "404", description = "Target ACL module not found"),
            @ApiResponse(responseCode = "403", description = "Insufficient permissions to perform update")
    })
    @PutMapping("/update.json")
    public JsonData updateAclModule(@RequestBody @Valid AclModuleParam param) {

        sysAclModuleService.update(param);
        return JsonData.success();

    }


    /**
     * Retrieve all ACL modules in a hierarchical tree structure.
     *
     * @return a tree structure of ACL modules
     */
    @Operation(summary = "Get Module Tree", description = "Retrieves the full hierarchical structure of ACL modules.")
    @ApiResponse(responseCode = "200", description = "Tree structure loaded successfully")
    @GetMapping("/tree.json")
    public JsonData tree() {

        Object tree = sysTreeService.aclModuleTree();
        return JsonData.success(tree);
    }


    /**
     * Delete an ACL module by its ID.
     *
     * @param id the ID of the ACL module to delete
     * @return success response
     */
    @Operation(summary = "Delete ACL Module", description = "Permanently removes a module. Fails if the module contains sub-modules or ACLs.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Module deleted successfully"),
            @ApiResponse(responseCode = "409", description = "Conflict: Module is not empty")
    })
    @DeleteMapping("/delete.json")
    public JsonData delete(@Parameter(description = "ID of the module to delete", required = true) @RequestParam("id") int id) {

        sysAclModuleService.delete(id);
        return JsonData.success();
    }
}
