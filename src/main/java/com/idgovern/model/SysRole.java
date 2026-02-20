package com.idgovern.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import java.util.Date;


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
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */
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
    private Date operateTime;

    /**
     * IP address from which the operation was performed.
     */
    private String operateIp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator == null ? null : operator.trim();
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getOperateIp() {
        return operateIp;
    }

    public void setOperateIp(String operateIp) {
        this.operateIp = operateIp == null ? null : operateIp.trim();
    }
}