package com.idgovern.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Department Request Parameter Object.
 *
 * <p>
 * This class encapsulates input data for department creation
 *  and update operations within the RBAC service.
 * </p>
 *
 * <p>
 * Ensures that department name, hierarchy structure, and display
 * sequence meet business constraints before processing.
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author   | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-02-16 | Lilian S.| Initial creation                |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */


@Getter
@Setter
@ToString
@Schema(description = "Request payload for creating or updating organizational departments")
public class DeptParam {

    /**
     * Unique identifier of the department.
     * Null when creating a new department.
     */
    @Schema(description = "Unique department ID (Required for updates, omit for creation)", example = "1")
    private Integer id;

    /**
     * Name of the department.
     * Must be between 2 and 15 characters.
     */
    @NotBlank(message = "Department name must not be empty")
    @Length(max = 15, min = 2, message = "Department name length must be between 2 and 15 characters")
    @Schema(description = "Display name of the department", example = "Engineering", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    /**
     * Parent department ID.
     * Default value 0 indicates a root-level department.
     */
    @Schema(description = "Parent department ID. Use 0 for root-level departments.", example = "0", defaultValue = "0")
    private Integer parentId = 0;

    /**
     * Display order used for sorting departments at the same hierarchy level.
     */
    @NotNull(message = "Display order must not be null")
    @Schema(description = "Priority for sorting (lower numbers appear first)", example = "1")
    private Integer seq;

    /**
     * Optional description or remark for the department.
     * Maximum length: 150 characters.
     */
    @Length(max = 150, message = "Remark must not exceed 150 characters")
    @Schema(description = "Brief description of the department's function", example = "Core software development team")
    private String remark;
}
