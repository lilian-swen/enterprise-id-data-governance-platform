package com.idgovern.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;


/**
 * ACL Module Request Parameter Object.
 *
 * <p>
 * This class encapsulates input data for ACL module creation
 * and update operations within the RBAC (Role-Based Access Control) system.
 * </p>
 *
 * <p>
 * Ensures that module name, parent-child hierarchy, display order, and status
 * meet business constraints before processing.
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author    | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-02-17 | Lilian S.| Initial creation                |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@ToString
@Schema(description = "Request payload for creating or updating ACL modules (permission categories)")
public class AclModuleParam {

    /** ID of the ACL module. Optional for create, required for update */
    @Schema(description = "Unique module ID (Required for updates, omit for creation)", example = "1")
    private Integer id;

    /** Name of the ACL module. Required, 2-20 characters */
    @NotBlank(message = "ACL module name cannot be empty")
    @Length(min = 2, max = 20, message = "ACL module name length must be between 2 and 20 characters")
    @Schema(description = "Name of the permission category", example = "User Management", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    /** Parent ACL module ID. Defaults to 0 (top-level module) */
    @Schema(description = "Parent module ID. Use 0 for top-level categories.", example = "0", defaultValue = "0")
    private Integer parentId = 0;

    /** Display order of the module. Required */
    @NotNull(message = "ACL module display order cannot be null")
    @Schema(description = "Priority for sorting within the same level (lower numbers appear first)", example = "1")
    private Integer seq;

    /** Status of the module. 0 = inactive, 1 = active */
    @NotNull(message = "ACL module status cannot be null")
    @Min(value = 0, message = "ACL module status is invalid")
    @Max(value = 1, message = "ACL module status is invalid")
    @Schema(description = "Availability status: 0=Inactive, 1=Active", allowableValues = {"0", "1"}, example = "1")
    private Integer status;

    /** Additional remarks or description. Maximum 200 characters */
    @Length(max = 200, message = "ACL module remark must be within 200 characters")
    @Schema(description = "Additional description of the module's scope", example = "Contains all user-related permission points")
    private String remark;
}
