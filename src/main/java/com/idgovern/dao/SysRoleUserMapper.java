package com.idgovern.dao;

import com.idgovern.model.SysRoleUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * Data Access Object (DAO) interface for managing role-user relationships.
 *
 * <p>
 * Provides CRUD operations and batch queries for mapping users to roles
 * within the RBAC (Role-Based Access Control) system.
 * </p>
 *
 * <p>
 * Responsibilities:
 * <ul>
 *     <li>Insert, update, delete, and select role-user mappings</li>
 *     <li>Retrieve role IDs associated with a user</li>
 *     <li>Retrieve user IDs associated with a role or multiple roles</li>
 *     <li>Batch insert role-user relationships</li>
 * </ul>
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author   | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-02-26 | Lilian S.| Initial creation                |
 * | 1.1     | 2016-02-27 | Lilian S.| Added professional comments     |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.1
 * @since 1.0
 */
public interface SysRoleUserMapper {

    /**
     * Delete a role-user mapping by its primary key ID.
     *
     * @param id primary key ID
     * @return number of rows affected
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * Insert a new role-user mapping.
     *
     * @param record role-user entity
     * @return number of rows affected
     */
    int insert(SysRoleUser record);

    /**
     * Insert a new role-user mapping selectively (non-null fields only).
     *
     * @param record role-user entity
     * @return number of rows affected
     */
    int insertSelective(SysRoleUser record);

    /**
     * Select a role-user mapping by its primary key ID.
     *
     * @param id primary key ID
     * @return SysRoleUser entity
     */
    SysRoleUser selectByPrimaryKey(Integer id);

    /**
     * Update a role-user mapping selectively by primary key (non-null fields only).
     *
     * @param record role-user entity
     * @return number of rows affected
     */
    int updateByPrimaryKeySelective(SysRoleUser record);

    /**
     * Update a role-user mapping by primary key.
     *
     * @param record role-user entity
     * @return number of rows affected
     */
    int updateByPrimaryKey(SysRoleUser record);

    /**
     * Retrieve all role IDs associated with a given user ID.
     *
     * @param userId user ID
     * @return list of role IDs
     */
    List<Integer> getRoleIdListByUserId(@Param("userId") int userId);

    /**
     * Retrieve all user IDs associated with a given role ID.
     *
     * @param roleId role ID
     * @return list of user IDs
     */
    List<Integer> getUserIdListByRoleId(@Param("roleId") int roleId);

    /**
     * Delete all role-user mappings for a given role ID.
     *
     * @param roleId role ID
     */
    void deleteByRoleId(@Param("roleId") int roleId);

    /**
     * Batch insert multiple role-user mappings.
     *
     * @param roleUserList list of role-user entities
     */
    void batchInsert(@Param("roleUserList") List<SysRoleUser> roleUserList);

    /**
     * Retrieve all user IDs associated with a list of role IDs.
     *
     * @param roleIdList list of role IDs
     * @return list of user IDs
     */
    List<Integer> getUserIdListByRoleIdList(@Param("roleIdList") List<Integer> roleIdList);
}