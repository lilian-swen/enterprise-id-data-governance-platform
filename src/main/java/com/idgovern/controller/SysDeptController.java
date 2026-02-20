package com.idgovern.controller;

import com.idgovern.common.JsonData;
import com.idgovern.common.RequestHolder;
import com.idgovern.dto.DeptLevelDto;
import com.idgovern.model.SysUser;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;


// http://localhost:8082/signin.jsp
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
 * | 1.1     | 2026-01-26 | Updated  | Professional Logging Strategy added|
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */
@Controller
@RequestMapping("/sys/dept")
@Slf4j
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
    @RequestMapping("/dept.page")
    public ModelAndView page() {

        SysUser operator = RequestHolder.getCurrentUser();

        log.debug("Department page accessed by operator={}",
                operator != null ? operator.getUsername() : "ANONYMOUS");

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
    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData saveDept(@Parameter(description = "Department creation parameters") DeptParam param) {

        SysUser operator = RequestHolder.getCurrentUser();

        log.info("Department create request: operator={}, deptName={}, parentId={}",
                operator != null ? operator.getUsername() : "SYSTEM",
                param.getName(),
                param.getParentId());

        try {

            sysDeptService.save(param);

            log.info("Department created successfully: operator={}, deptName={}, parentId={}",
                    operator != null ? operator.getUsername() : "SYSTEM",
                    param.getName(),
                    param.getParentId());

            return JsonData.success();

        } catch (Exception e) {

            log.error("Department creation failed: operator={}, param={}",
                    operator != null ? operator.getUsername() : "SYSTEM",
                    param,
                    e);

            return JsonData.fail("Department creation failed");
        }
    }

    /**
     * Retrieves the department hierarchical tree structure.
     *
     * @return JSON response containing department tree data
     * 2016-02-16
     */
    @Operation(summary = "Get Department Tree", description = "Retrieves the full hierarchical tree structure of all departments.")
    @ApiResponse(responseCode = "200", description = "Tree structure loaded successfully")
    @RequestMapping("/tree.json")
    @ResponseBody
    public JsonData tree() {

        SysUser operator = RequestHolder.getCurrentUser();

        log.debug("Department tree requested by operator={}",
                operator != null ? operator.getUsername() : "SYSTEM");

        try {

            List<DeptLevelDto> dtoList = sysTreeService.deptTree();

            log.debug("Department tree loaded successfully, nodeCount={}", dtoList.size());

            return JsonData.success(dtoList);

        } catch (Exception e) {

            log.error("Department tree load failed: operator={}",
                    operator != null ? operator.getUsername() : "SYSTEM",
                    e);

            return JsonData.fail("Department tree load failed");
        }
    }


    /**
     * Handles department update request.
     *
     * @param param department update parameters
     * @return success response in JSON format
     * 2016-02-16
     */
    @Operation(summary = "Update Department", description = "Updates an existing department's details. Hierarchical shifts are validated.")
    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData updateDept(@Parameter(description = "Updated department details") DeptParam param) {

        SysUser operator = RequestHolder.getCurrentUser();

        log.info("Department update request: operator={}, deptId={}, deptName={}",
                operator != null ? operator.getUsername() : "SYSTEM",
                param.getId(),
                param.getName());

        try {

            sysDeptService.update(param);

            log.info("Department updated successfully: operator={}, deptId={}, deptName={}",
                    operator != null ? operator.getUsername() : "SYSTEM",
                    param.getId(),
                    param.getName());

            return JsonData.success();

        } catch (Exception e) {

            log.error("Department update failed: operator={}, param={}",
                    operator != null ? operator.getUsername() : "SYSTEM",
                    param,
                    e);

            return JsonData.fail("Department update failed");
        }
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
    @RequestMapping("/delete.json")
    @ResponseBody
    public JsonData delete(@Parameter(description = "ID of department to remove", required = true) @RequestParam("id") int id) {

        SysUser operator = RequestHolder.getCurrentUser();

        log.warn("Department delete request: operator={}, deptId={}",
                operator != null ? operator.getUsername() : "SYSTEM",
                id);

        try {

            sysDeptService.delete(id);

            log.warn("Department deleted successfully: operator={}, deptId={}",
                    operator != null ? operator.getUsername() : "SYSTEM",
                    id);

            return JsonData.success();

        } catch (Exception e) {

            log.error("Department deletion failed: operator={}, deptId={}",
                    operator != null ? operator.getUsername() : "SYSTEM",
                    id,
                    e);

            return JsonData.fail("Department deletion failed");
        }
    }

}
