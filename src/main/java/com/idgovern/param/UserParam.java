package com.idgovern.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;



/**
 * User Request Parameter Object.
 *
 * <p>
 * Encapsulates input parameters for user creation and update
 * operations within the RBAC (Role-Based Access Control) system.
 * </p>
 *
 * <p>
 * Ensures that username, contact information, department assignment,
 * and status comply with defined business validation rules
 * before processing.
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author    | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-02-24 | Lilian S. | Initial creation                |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User account request payload for creation and updates")
public class UserParam {

    /**
     * Unique identifier of the user.
     * Used during update operations.
     */
    @Schema(description = "Unique identifier (Required for updates, omit for creation)", example = "1")
    private Integer id;

    /**
     * Username.
     * Must not be blank and must not exceed 20 characters.
     */
    @NotBlank(message = "Username must not be blank")
    @Length(min = 1, max = 20, message = "Username length must not exceed 20 characters")
    @Schema(description = "Unique login name", example = "lilian.s", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    /**
     * Telephone number.
     * Must not be blank and must not exceed 13 characters.
     */
    @NotBlank(message = "Telephone number must not be blank")
    @Length(min = 1, max = 13, message = "Telephone number length must not exceed 13 characters")
    @Schema(description = "Primary contact number", example = "13800138000")
    private String telephone;


    /**
     * Email address.
     * Must not be blank and must not exceed 50 characters.
     */
    @NotBlank(message = "Email must not be blank")
    @Length(min = 5, max = 50, message = "Email length must not exceed 50 characters")
    @Schema(description = "Work email address", example = "lilian.swen@outlook.com")
    private String mail;

    /**
     * Department ID to which the user belongs.
     * Must be provided.
     */
    @NotNull(message = "Department ID must be provided")
    @Schema(description = "Parent Department ID", example = "10")
    private Integer deptId;

    /**
     * User status.
     * 0 - Frozen
     * 1 - Active
     * 2 - Deleted
     */
    @NotNull(message = "User status must be specified")
    @Min(value = 0, message = "Invalid user status")
    @Max(value = 2, message = "Invalid user status")
    @Schema(description = "Account Status: 0=Frozen, 1=Active, 2=Deleted", allowableValues = {"0", "1", "2"}, example = "1")
    private Integer status;

    /**
     * Additional remarks or description for the user.
     * Maximum length: 200 characters.
     */
    @Length(min = 0, max = 200, message = "Remark must not exceed 200 characters")
    @Schema(description = "Additional administrative remarks", example = "Senior Software Engineer")
    private String remark = "";
}
