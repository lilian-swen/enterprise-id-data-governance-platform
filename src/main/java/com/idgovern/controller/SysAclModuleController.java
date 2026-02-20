package com.idgovern.controller;

import com.idgovern.common.JsonData;
import com.idgovern.common.RequestHolder;
import com.idgovern.model.SysUser;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


/**
 * Controller for managing Access Control List (ACL) Modules.
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
 * | 1.0     | 2016-02-28 | Lilian S.| Initial creation of SysAclModuleController |
 * | 1.1     | 2026-01-26 | Updated  | Professional Logging Strategy added|
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */
@Controller
@RequestMapping("/sys/aclModule")
@Slf4j
@Tag(name = "ACL Module Management", description = "Operations for hierarchical grouping of permissions")
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
    @Operation(summary = "Render Module Page", description = "Returns the JSP view for managing ACL modules.")
    @RequestMapping("/acl.page")
    public ModelAndView page() {

        SysUser operator = RequestHolder.getCurrentUser();

        log.debug("ACL module page accessed by operator={}",
                operator != null ? operator.getUsername() : "ANONYMOUS");

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
            @ApiResponse(responseCode = "200", description = "Module created"),
            @ApiResponse(responseCode = "400", description = "Invalid module hierarchy or data")
    })
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveAclModule(@Parameter(description = "Module creation parameters") AclModuleParam param) {

        SysUser operator = RequestHolder.getCurrentUser();

        log.info("ACL module create request: operator={}, moduleName={}, parentId={}",
                operator != null ? operator.getUsername() : "SYSTEM",
                param.getName(),
                param.getParentId());


        try {

            sysAclModuleService.save(param);

            log.info("ACL module created successfully: operator={}, moduleName={}, parentId={}",
                    operator != null ? operator.getUsername() : "SYSTEM",
                    param.getName(),
                    param.getParentId());

            return JsonData.success();

        } catch (Exception e) {

            log.error("ACL module creation failed: operator={}, param={}",
                    operator != null ? operator.getUsername() : "SYSTEM",
                    param,
                    e);

            return JsonData.fail("ACL module creation failed");
        }
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
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateAclModule(AclModuleParam param) {

        SysUser operator = RequestHolder.getCurrentUser();

        log.info("ACL module update request: operator={}, moduleId={}, moduleName={}",
                operator != null ? operator.getUsername() : "SYSTEM",
                param.getId(),
                param.getName());

        try {

            sysAclModuleService.update(param);

            log.info("ACL module updated successfully: operator={}, moduleId={}, moduleName={}",
                    operator != null ? operator.getUsername() : "SYSTEM",
                    param.getId(),
                    param.getName());

            return JsonData.success();

        } catch (Exception e) {

            log.error("ACL module update failed: operator={}, param={}",
                    operator != null ? operator.getUsername() : "SYSTEM",
                    param,
                    e);

            return JsonData.fail("ACL module update failed");
        }
    }


    /**
     * Retrieve all ACL modules in a hierarchical tree structure.
     *
     * @return a tree structure of ACL modules
     */
    @Operation(summary = "Get Module Tree", description = "Retrieves the full hierarchical structure of ACL modules.")
    @ApiResponse(responseCode = "200", description = "Tree structure loaded successfully")
    @RequestMapping("/tree.json")
    @ResponseBody
    public JsonData tree() {

        SysUser operator = RequestHolder.getCurrentUser();

        log.debug("ACL module tree requested by operator={}",
                operator != null ? operator.getUsername() : "SYSTEM");

        try {

            Object tree = sysTreeService.aclModuleTree();

            log.debug("ACL module tree loaded successfully");

            return JsonData.success(tree);

        } catch (Exception e) {

            log.error("ACL module tree load failed: operator={}",
                    operator != null ? operator.getUsername() : "SYSTEM",
                    e);

            return JsonData.fail("Tree load failed");
        }
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
    @RequestMapping("/delete.json")
    @ResponseBody
    public JsonData delete(@Parameter(description = "ID of the module to delete", required = true) @RequestParam("id") int id) {

        SysUser operator = RequestHolder.getCurrentUser();

        log.warn("ACL module delete request: operator={}, moduleId={}",
                operator != null ? operator.getUsername() : "SYSTEM",
                id);

        try {

            sysAclModuleService.delete(id);

            log.warn("ACL module deleted successfully: operator={}, moduleId={}",
                    operator != null ? operator.getUsername() : "SYSTEM",
                    id);

            return JsonData.success();

        } catch (Exception e) {

            log.error("ACL module deletion failed: operator={}, moduleId={}",
                    operator != null ? operator.getUsername() : "SYSTEM",
                    id,
                    e);

            return JsonData.fail("ACL module deletion failed");
        }
    }
}
