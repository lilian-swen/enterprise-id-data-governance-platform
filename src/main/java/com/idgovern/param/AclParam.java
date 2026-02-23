package com.idgovern.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * ACL Request Parameter Object.
 *
 * <p>
 * This class encapsulates input data for ACL (Access Control List) creation
 * and update operations within the RBAC (Role-Based Access Control) system.
 * </p>
 *
 * <p>
 * Ensures that ACL point name, associated module, URL, type, status,
 * display order, and remarks meet business constraints before processing.
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author    | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-02-17 | Lilian S.| Initial creation                |
 * | 1.1     | 2026-01-26 | Lilian S.| Upgraded Swagger to the latest stable version |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.1
 * @since 1.0
 */
@Getter
@Setter
@ToString
@Schema(description = "Request payload for creating or updating Access Control List (ACL) entries")
public class AclParam {

    @Schema(description = "Unique ACL ID (Required for updates, omit for creation)", example = "1")
    private Integer id;

    @NotBlank(message = "Permission name cannot be empty")
    @Size(min = 2, max = 30, message = "Permission name length must be between 2 and 30 characters")
    @Schema(description = "Human-readable name of the permission", example = "User Edit", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotNull(message = "A permission module must be selected")
    @Schema(description = "ID of the parent ACL module grouping this permission", example = "5")
    private Integer aclModuleId;

    @Size(min = 6, max = 100, message = "Permission URL length must be between 6 and 100 characters")
    @Schema(description = "System resource URL or API path protected by this ACL", example = "/sys/user/update.json")
    private String url;

    @NotNull(message = "Permission type must be selected")
    @Min(value = 1, message = "Invalid permission type")
    @Max(value = 3, message = "Invalid permission type")
    @Schema(description = "Type of permission: 1=Menu, 2=Button, 3=Other", allowableValues = {"1", "2", "3"}, example = "2")
    private Integer type;

    @NotNull(message = "Permission status must be selected")
    @Min(value = 0, message = "Invalid permission status")
    @Max(value = 1, message = "Invalid permission status")
    @Schema(description = "Access status: 0=Disabled, 1=Enabled", allowableValues = {"0", "1"}, example = "1")
    private Integer status;

    @NotNull(message = "Permission display order must be specified")
    @Schema(description = "Sorting order within the ACL module", example = "1")
    private Integer seq;

    @Size(min = 0, max = 200, message = "Permission remark must not exceed 200 characters")
    @Schema(description = "Additional notes regarding the permission's scope", example = "Allows modifying existing user profiles")
    private String remark;
}
