/*******************************************************************************
 * Migration:      V1__init_data.sql
 * Author:         Lilian S.
 * Last Updated:   2016-01-19
 *
 * Description:
 *   Initial seed data for the Enterprise Identity & Data Governance Platform.
 *   Populates foundational records required for system bootstrap, including:
 *     - Root departments
 *     - Default administrative users
 *     - Core RBAC (Role-Based Access Control) mappings
 *
 *   Originally developed as a practice project in 2016 and refined with
 *   improved documentation and structural alignment to reflect
 *   enterprise-grade implementation standards.
 *
 * Jira:           DG-101 (Core Identity Setup)
 *******************************************************************************/


USE `data_governance_db`;

-- Ensure atomicity: All seed data must succeed or none at all
START TRANSACTION;

-- -----------------------------------------------------------------------------
-- 1. ACL MODULES (Hierarchy)
-- Defines the navigation tree for permissions.
-- Root modules have parent_id = 0.
-- -----------------------------------------------------------------------------
INSERT INTO `sys_acl_module` (`id`, `name`, `parent_id`, `level`, `seq`, `status`, `remark`, `operator`, `operate_time`, `operate_ip`) VALUES
                                                                                                                                           (1,  'Product Management',      0, '0',   1, 1, 'Root for product ops', 'Admin', '2017-10-14 21:13:15', '::1'),
                                                                                                                                           (2,  'Order Management',        0, '0',   2, 1, NULL,                   'Admin', '2017-10-14 20:17:11', '::1'),
                                                                                                                                           (3,  'Announcement Management', 0, '0',   3, 1, NULL,                   'Admin', '2017-10-14 20:17:21', '::1'),
                                                                                                                                           (6,  'Access Control Mgmt',     0, '0',   4, 1, 'RBAC Root',            'Admin', '2017-10-15 21:27:37', '::1'),
                                                                                                                                           (10, 'Operations Management',   0, '0',   6, 1, NULL,                   'Admin', '2017-10-16 23:03:37', '::1'),
-- Sub-modules for Product (Parent ID: 1)
                                                                                                                                           (4,  'Active Products',         1, '0.1', 1, 1, NULL,                   'Admin', '2017-10-14 21:13:39', '::1'),
                                                                                                                                           (5,  'Inactive Products',       1, '0.1', 2, 1, NULL,                   'Admin', '2017-10-14 20:18:02', '::1'),
-- Sub-modules for Access Control (Parent ID: 6)
                                                                                                                                           (7,  'Permission Mgmt',         6, '0.6', 1, 1, NULL,                   'Admin', '2017-10-15 21:27:57', '::1'),
                                                                                                                                           (8,  'Role Mgmt',               6, '0.6', 2, 1, NULL,                   'Admin', '2017-10-15 21:28:22', '::1'),
                                                                                                                                           (9,  'User Mgmt',               6, '0.6', 3, 1, NULL,                   'Admin', '2017-10-15 21:28:36', '::1'),
                                                                                                                                           (11, 'Audit Logs',              6, '0.6', 4, 1, NULL,                   'Admin', '2017-10-16 23:04:07', '::1');


-- -----------------------------------------------------------------------------
-- 3. DEPARTMENTS
-- Initial organizational structure.
-- -----------------------------------------------------------------------------
INSERT INTO `sys_dept` (`id`, `name`, `parent_id`, `level`, `seq`, `remark`, `operator`, `operate_ip`) VALUES
                                                                                                           ('1',  'Technical Dept',  '0', '0',   '1', 'Core Tech', 'system', '127.0.0.1'),
                                                                                                           ('2',  'Backend Dev',     '1', '0.1', '1', 'Java/Go',   'system', '127.0.0.1'),
                                                                                                           ('3',  'Frontend Dev',    '1', '0.1', '2', 'React/Vue', 'system', '127.0.0.1'),
                                                                                                           ('11', 'Product Dept',    '0', '0',   '2', NULL,       'Admin',  '::1');

-- -----------------------------------------------------------------------------
-- 4. ROLES
-- Type 1 = Admin, Type 2 = Regular
-- -----------------------------------------------------------------------------
INSERT INTO `sys_role` (`id`, `name`, `type`, `status`, `operator`, `operate_ip`) VALUES
                                                                                      ('1', 'Super Admin',      1, 1, 'Admin', '::1'),
                                                                                      ('4', 'Permission Admin', 1, 1, 'Admin', '::1'),
                                                                                      ('5', 'Ops Manager',      2, 1, 'Admin', '::1');

-- -----------------------------------------------------------------------------
-- 5. USERS
-- Default credentials (password: helloworld)
-- -----------------------------------------------------------------------------
INSERT INTO `sys_user` (`id`, `username`, `password`, `dept_id`, `status`, `operator`) VALUES
                                                                                           ('1', 'Admin', '25D55AD283AA400AF464C76D713C07AD', 1, 1, 'system'),
                                                                                           ('4', 'Kate',  '25D55AD283AA400AF464C76D713C07AD', 1, 1, 'Admin');

-- -----------------------------------------------------------------------------
-- 6. RELATIONSHIPS (Junction Tables)
-- Assigning Users to Roles and Roles to Permissions.
-- -----------------------------------------------------------------------------

-- Assign Role 4 (Permission Admin) to permissions 7, 8, 9
INSERT INTO `sys_role_acl` (`role_id`, `acl_id`, `operator`, `operate_ip`) VALUES
                                                                               (4, 7, 'Admin', '::1'),
                                                                               (4, 8, 'Admin', '::1'),
                                                                               (4, 9, 'Admin', '::1');

-- Assign User 1 (Admin) and User 4 (Kate) to Role 4 (Permission Admin)
INSERT INTO `sys_role_user` (`role_id`, `user_id`, `operator`, `operate_ip`) VALUES
                                                                                 (4, 1, 'Admin', '::1'),
                                                                                 (4, 4, 'Admin', '::1');

COMMIT;