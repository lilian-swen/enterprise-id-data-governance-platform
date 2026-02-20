/*******************************************************************************
 * Migration:      V1__init_schema.sql
 * Author:         Lilian S.
 * Last Updated:   2016-01-19
 *
 * Description:
 *   Initial DDL implementation for the Enterprise Identity & Data Governance
 *   Platform. Originally created in 2016 as a practice project and refined
 *   on 2016-01-19 with enhanced documentation, structural improvements,
 *   and schema optimizations to better align with enterprise-grade standards.
 *
 * Scope:
 *   - Defines RBAC (Role-Based Access Control) model
 *   - Defines Department organizational structure
 *
 * Jira:           DG-101 (Core Identity Setup)
 *******************************************************************************/


-- 1. Database and Environment Setup
CREATE DATABASE IF NOT EXISTS `data_governance_db`
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

GRANT ALL PRIVILEGES ON data_governance_db.* TO 'Lilian'@'localhost';

USE `data_governance_db`;

-- 2. Table Structures

-- -----------------------------------------------------
-- Table: sys_acl
-- Purpose: Individual permission items (Access Control List)
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sys_acl`;
CREATE TABLE `sys_acl` (
                           `id`            INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Primary key ID',
                           `code`          VARCHAR(50) NOT NULL  COMMENT 'System-generated unique permission code',
                           `name`          VARCHAR(50) NOT NULL  COMMENT 'Human-readable permission name',
                           `acl_module_id` INT UNSIGNED NOT NULL COMMENT 'FK: References sys_acl_module.id',
                           `url`           VARCHAR(255) NOT NULL COMMENT 'The secured resource URL/Endpoint (Regex supported)',
                           `type`          TINYINT NOT NULL DEFAULT 3 COMMENT '1:Menu, 2:Button, 3:API/Other',
                           `status`        TINYINT NOT NULL DEFAULT 1 COMMENT '1:Available, 0:Frozen',
                           `seq`           INT NOT NULL DEFAULT 0     COMMENT 'Sort order within the same module',
                           `remark`        VARCHAR(255) DEFAULT NULL  COMMENT 'Business justification for permission',
                           `operator`      VARCHAR(50) NOT NULL       COMMENT 'Last user to modify this record',
                           `operate_time`  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Timestamp of last update',
                           `operate_ip`    VARCHAR(45) NOT NULL       COMMENT 'IP address of the last operator',
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `uk_sys_acl_code` (`code`),
                           KEY `idx_sys_acl_module_id` (`acl_module_id`),
                           KEY `idx_sys_acl_url` (`url`),
                           KEY `idx_sys_acl_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Permission (ACL) Details';

-- -----------------------------------------------------
-- Table: sys_acl_module
-- Purpose: Hierarchical grouping for permissions
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sys_acl_module`;
CREATE TABLE `sys_acl_module` (
                                  `id`            INT UNSIGNED NOT NULL AUTO_INCREMENT,
                                  `name`          VARCHAR(50) NOT NULL  COMMENT 'Module name (e.g., User Management)',
                                  `parent_id`     INT UNSIGNED NOT NULL DEFAULT 0 COMMENT 'FK: References sys_acl_module.id (0 if root)',
                                  `level`         VARCHAR(255) NOT NULL COMMENT 'Materialized path (e.g., 0.1.2) for tree traversal',
                                  `seq`           INT NOT NULL DEFAULT 0     COMMENT 'Sort order within hierarchy level',
                                  `status`        TINYINT NOT NULL DEFAULT 1 COMMENT '1:Available, 0:Frozen',
                                  `remark`        VARCHAR(255) DEFAULT NULL,
                                  `operator`      VARCHAR(50) NOT NULL,
                                  `operate_time`  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                  `operate_ip`    VARCHAR(45) NOT NULL,
                                  PRIMARY KEY (`id`),
                                  KEY `idx_sys_acl_module_parent_id` (`parent_id`),
                                  KEY `idx_sys_acl_module_level` (`level`),
                                  KEY `idx_sys_acl_module_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC COMMENT='Permission Module Folders';

-- -----------------------------------------------------
-- Table: sys_dept
-- Purpose: Organizational hierarchy (Departments)
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
                            `id`            INT UNSIGNED NOT NULL AUTO_INCREMENT,
                            `name`          VARCHAR(50) NOT NULL  COMMENT 'Department name',
                            `parent_id`     INT UNSIGNED NOT NULL DEFAULT 0 COMMENT 'FK: sys_dept.id (0 for root)',
                            `level`         VARCHAR(255) NOT NULL COMMENT 'Hierarchy path (e.g., 0.1)',
                            `seq`           INT NOT NULL DEFAULT 0,
                            `remark`        VARCHAR(255) DEFAULT NULL,
                            `operator`      VARCHAR(50) NOT NULL,
                            `operate_time`  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                            `operate_ip`    VARCHAR(45) NOT NULL,
                            PRIMARY KEY (`id`),
                            KEY `idx_sys_dept_parent_id` (`parent_id`),
                            KEY `idx_sys_dept_level` (`level`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Department Hierarchy';

-- -----------------------------------------------------
-- Table: sys_log
-- Purpose: Audit trail for all system entity changes
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
                           `id`           INT(11) NOT NULL AUTO_INCREMENT,
                           `type`         INT(11) NOT NULL DEFAULT '0' COMMENT 'Entity Type: 1:Dept, 2:User, 3:Module, 4:ACL, 5:Role, 6:RoleUser, 7:RoleACL',
                           `target_id`    INT(11) NOT NULL             COMMENT 'Primary key of the modified target entity',
                           `old_value`    TEXT                         COMMENT 'JSON representation of data before change',
                           `new_value`    TEXT                         COMMENT 'JSON representation of data after change',
                           `operator`     VARCHAR(20) NOT NULL DEFAULT '',
                           `operate_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           `operate_ip`   VARCHAR(20) NOT NULL DEFAULT '',
                           `status`       INT(11) NOT NULL DEFAULT '0' COMMENT 'Restoration Status: 0:Original, 1:Restored',
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COMMENT='System Audit Logs';

-- -----------------------------------------------------
-- Table: sys_role
-- Purpose: RBAC Groupings
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
                            `id`           INT(11) NOT NULL AUTO_INCREMENT,
                            `name`         VARCHAR(20) NOT NULL,
                            `type`         INT(11) NOT NULL DEFAULT '1' COMMENT '1:Admin Role, 2:Standard Role',
                            `status`       INT(11) NOT NULL DEFAULT '1' COMMENT '1:Available, 0:Frozen',
                            `remark`       VARCHAR(200) DEFAULT '',
                            `operator`     VARCHAR(20) NOT NULL DEFAULT '',
                            `operate_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `operate_ip`   VARCHAR(20) NOT NULL DEFAULT '',
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COMMENT='User Roles';

-- -----------------------------------------------------
-- Table: sys_role_acl
-- Purpose: Junction table for Many-to-Many: Role <-> Permission
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sys_role_acl`;
CREATE TABLE `sys_role_acl` (
                                `id`           INT(11) NOT NULL AUTO_INCREMENT,
                                `role_id`      INT(11) NOT NULL COMMENT 'FK: sys_role.id',
                                `acl_id`       INT(11) NOT NULL COMMENT 'FK: sys_acl.id',
                                `operator`     VARCHAR(20) NOT NULL DEFAULT '',
                                `operate_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                `operate_ip`   VARCHAR(200) NOT NULL DEFAULT '',
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COMMENT='Role-Permission Mapping';

-- -----------------------------------------------------
-- Table: sys_role_user
-- Purpose: Junction table for Many-to-Many: Role <-> User
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sys_role_user`;
CREATE TABLE `sys_role_user` (
                                 `id`           INT(11) NOT NULL AUTO_INCREMENT,
                                 `role_id`      INT(11) NOT NULL COMMENT 'FK: sys_role.id',
                                 `user_id`      INT(11) NOT NULL COMMENT 'FK: sys_user.id',
                                 `operator`     VARCHAR(20) NOT NULL DEFAULT '',
                                 `operate_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 `operate_ip`   VARCHAR(20) NOT NULL DEFAULT '',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COMMENT='Role-User Assignment';

-- -----------------------------------------------------
-- Table: sys_user
-- Purpose: Core platform identity
-- -----------------------------------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
                            `id`           INT(11) NOT NULL AUTO_INCREMENT,
                            `username`     VARCHAR(20) NOT NULL DEFAULT '' COMMENT 'Unique Login Name',
                            `telephone`    VARCHAR(13) NOT NULL DEFAULT '' COMMENT 'Primary Contact Mobile',
                            `mail`         VARCHAR(20) NOT NULL DEFAULT '' COMMENT 'Corporate Email Address',
                            `password`     VARCHAR(40) NOT NULL DEFAULT '' COMMENT 'Encrypted Credential',
                            `dept_id`      INT(11) NOT NULL DEFAULT '0'    COMMENT 'FK: sys_dept.id',
                            `status`       INT(11) NOT NULL DEFAULT '1'    COMMENT '1:Active, 0:Frozen, 2:Deleted',
                            `remark`       VARCHAR(200) DEFAULT '',
                            `operator`     VARCHAR(20) NOT NULL DEFAULT '',
                            `operate_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `operate_ip`   VARCHAR(20) NOT NULL DEFAULT '',
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COMMENT='User Accounts';