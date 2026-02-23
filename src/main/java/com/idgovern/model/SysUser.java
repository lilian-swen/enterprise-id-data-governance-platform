package com.idgovern.model;

import lombok.*;

import java.util.Date;

/**
 * System User Entity Object.
 *
 * <p>
 * This class represents a system user within the RBAC (Role-Based Access Control)
 * framework. It encapsulates user identity information, department association,
 * account status, and audit metadata.
 * </p>
 *
 * <p>
 * The entity is typically mapped to the database table <code>sys_user</code>
 * and is used in persistence, service, and controller layers.
 * </p>
 *
 * <p>
 * It supports Lombok annotations for builder pattern construction and
 * convenient object creation.
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author   | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-02-24 | Lilian S.| Initial creation                |
 * | 1.1     | 2016-02-26 | Updated  | documentation added             |
 * | 1.2     | 2026-02-21 | Lilian S.| Use @Getter and @Setter at the class level and refactor the model accordingly. |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.1
 * @since 1.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SysUser {
    public static final String SESSION_USER_KEY = "user";

    private Integer id;

    private String username;

    private String telephone;

    private String mail;

    private String password;

    private Integer deptId;

    private Integer status;

    private String remark;

    private String operator;

    private Date operateTime;

    private String operateIp;

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone == null ? null : telephone.trim();
    }

    public void setMail(String mail) {
        this.mail = mail == null ? null : mail.trim();
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
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