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
 * Role Request Parameter Object.
 *
 * <p>
 * Encapsulates input parameters for role creation and update
 * operations within the RBAC (Role-Based Access Control) system.
 * </p>
 *
 * <p>
 * Ensures that role name, type, status, and remarks comply
 * with defined business validation rules before processing.
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author    | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-02-22 | Lilian S. | Initial creation                |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@ToString
@Schema(description = "Request payload for creating or updating system roles")
public class RoleParam {

    /**
     * Unique identifier of the role.
     * Used during update operations.
     */
    @Schema(description = "Unique role identifier (Required for updates, omit for creation)", example = "1")
    private Integer id;

    /**
     * Role name.
     * Must be between 2 and 20 characters.
     */
    @NotBlank(message = "Role name must not be blank")
    @Length(min = 2, max = 20, message = "Role name length must be between 2 and 20 characters")
    @Schema(description = "Unique display name for the role", example = "System Admin", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    /**
     * Role type.
     * 1 - Administrative Role
     * 2 - Business Role
     */
    @Min(value = 1, message = "Invalid role type")
    @Max(value = 2, message = "Invalid role type")
    @Schema(description = "Classification: 1=Administrative, 2=Business", allowableValues = {"1", "2"}, defaultValue = "1")
    private Integer type = 1;

    /**
     * Role status.
     * 0 - Disabled
     * 1 - Enabled
     */
    @NotNull(message = "Role status must not be null")
    @Min(value = 0, message = "Invalid role status")
    @Max(value = 1, message = "Invalid role status")
    @Schema(description = "Availability status: 0=Disabled, 1=Enabled", allowableValues = {"0", "1"}, example = "1")
    private Integer status;

    /**
     * Additional description or remarks for the role.
     * Maximum length: 200 characters.
     */
    @Length(min = 0, max = 200, message = "Role remark must not exceed 200 characters")
    @Schema(description = "Additional context or scope for the role", example = "Full access to user and department management")
    private String remark;
}
