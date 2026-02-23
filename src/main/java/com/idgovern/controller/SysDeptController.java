package com.idgovern.controller;

import com.idgovern.common.JsonData;
import com.idgovern.dto.DeptLevelDto;
import com.idgovern.param.DeptParam;
import com.idgovern.service.SysDeptService;
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
import java.util.List;


/**
 * Department Management Controller.
 *
 * <p>
 * This controller handles department-related operations including
 * creation, update, deletion, and hierarchical tree retrieval
 * within the RBAC management system.
 * </p>
 *
 * <p>
 * Provides web endpoints for managing department structures
 * and maintaining organizational hierarchy data.
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author    | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-02-16| Lilian S. | Initial creation                |
 * | 1.1     | 2026-01-26| Lilian S. | Professional Logging Strategy added|
 * | 1.2     | 2026-02-22| Lilian S.| Refactored for AOP & Clean Architecture |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.2
 * @since 1.0
 */
@RestController
@RequestMapping("/sys/dept")
@RequiredArgsConstructor
@Tag(name = "Department Management", description = "Operations for managing organizational hierarchy and departments")
@SecurityRequirement(name = "bearerAuth")
public class SysDeptController {

    @Resource
    private SysDeptService sysDeptService;

    @Resource
    private SysTreeService sysTreeService;

    /**
     * Loads the department management page.
     *
     * @return ModelAndView pointing to the department page
     */
    @Operation(summary = "Render Dept Page", description = "Returns the JSP view for managing organizational departments.")
    @GetMapping("/dept.page")
    public ModelAndView page() {

        return new ModelAndView("dept");
    }

    /**
     * Handles department creation request.
     *
     * @param param department request parameters
     * @return success response in JSON format
     * 2016-02-16
     */
    @Operation(summary = "Create Department", description = "Adds a new department node to the organizational tree.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Department saved successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed or circular parent reference")
    })
    @PostMapping("/save.json")
    public JsonData saveDept(@Parameter(description = "Department creation parameters") @Valid @RequestBody DeptParam param) {

        sysDeptService.save(param);
        return JsonData.success();
    }

    /**
     * Retrieves the department hierarchical tree structure.
     *
     * @return JSON response containing department tree data
     * 2016-02-16
     */
    @Operation(summary = "Get Department Tree", description = "Retrieves the full hierarchical tree structure of all departments.")
    @ApiResponse(responseCode = "200", description = "Tree structure loaded successfully")
    @GetMapping("/tree.json")
    public JsonData tree() {

        List<DeptLevelDto> dtoList = sysTreeService.deptTree();
        return JsonData.success(dtoList);
    }


    /**
     * Handles department update request.
     *
     * @param param department update parameters
     * @return success response in JSON format
     * 2016-02-16
     */
    @Operation(summary = "Update Department", description = "Updates an existing department's details. Hierarchical shifts are validated.")
    @PutMapping("/update.json")
    public JsonData updateDept(@Parameter(description = "Updated department details") @Valid @RequestBody DeptParam param) {

        sysDeptService.update(param);
        return JsonData.success();
    }


    /**
     * Handles department deletion request.
     *
     * @param id department identifier
     * @return success response in JSON format
     */
    @Operation(summary = "Delete Department", description = "Removes a department. Action is blocked if sub-departments or users exist.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Department deleted"),
            @ApiResponse(responseCode = "409", description = "Conflict: Department is not empty")
    })
    @DeleteMapping("/delete.json")
    public JsonData delete(@Parameter(description = "ID of department to remove", required = true) @RequestParam("id") int id) {

        sysDeptService.delete(id);
        return JsonData.success();
    }

}
