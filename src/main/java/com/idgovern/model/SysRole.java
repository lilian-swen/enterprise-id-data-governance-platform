package com.idgovern.model;

import lombok.*;
import java.time.LocalDateTime;


/**
 * System Role Entity.
 *
 * <p>
 * Represents a role within the RBAC (Role-Based Access Control) system.
 * A role defines a collection of permissions that can be assigned
 * to users for access control and authorization management.
 * </p>
 *
 * <p>
 * This entity maps to the system role table in the database and
 * contains audit information for traceability and governance.
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author    | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-02-22 | Lilian S. | Initial creation                |
 * | 1.1     | 2026-02-21 | Lilian S.| Use @Getter and @Setter at the class level and refactor the model accordingly. |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysRole {

    /**
     * Unique identifier of the role.
     */
    private Integer id;

    /**
     * Role name.
     * Should be unique within the system.
     */
    private String name;

    /**
     * Role type.
     * 1 - Administrative Role
     * 2 - Business Role
     */
    private Integer type;

    /**
     * Role status.
     * 0 - Disabled
     * 1 - Enabled
     */
    private Integer status;

    /**
     * Additional description or remarks about the role.
     */
    private String remark;

    /**
     * Operator who performed the last modification.
     */
    private String operator;

    /**
     * Timestamp of the last modification.
     */
    private LocalDateTime operateTime;
    /**
     * IP address from which the operation was performed.
     */
    private String operateIp;

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public void setOperateIp(String operateIp) {
        this.operateIp = operateIp == null ? null : operateIp.trim();
    }
}