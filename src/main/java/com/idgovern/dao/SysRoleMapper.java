package com.idgovern.dao;

import com.idgovern.model.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Role Data Access Object (DAO).
 *
 * <p>
 * Provides database interaction methods for the {@link SysRole} entity.
 * This interface is used by MyBatis to map SQL operations to Java methods.
 * </p>
 *
 * <p>
 * Supports CRUD operations, role uniqueness validation,
 * and batch retrieval for RBAC authorization management.
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
public interface SysRoleMapper {

    /**
     * Deletes a role by its primary key.
     *
     * @param id role identifier
     * @return number of rows affected
     */
    int deleteByPrimaryKey(Integer id);


    /**
     * Inserts a new role record into the database.
     *
     * @param record role entity
     * @return number of rows affected
     */
    int insert(SysRole record);


    /**
     * Inserts a new role record selectively.
     * Only non-null fields will be included in the SQL statement.
     *
     * @param record role entity
     * @return number of rows affected
     */
    int insertSelective(SysRole record);


    /**
     * Retrieves a role by its primary key.
     *
     * @param id role identifier
     * @return role entity
     */
    SysRole selectByPrimaryKey(Integer id);


    /**
     * Updates a role selectively.
     * Only non-null fields will be updated.
     *
     * @param record role entity
     * @return number of rows affected
     */
    int updateByPrimaryKeySelective(SysRole record);


    /**
     * Updates a role by its primary key.
     *
     * @param record role entity
     * @return number of rows affected
     */
    int updateByPrimaryKey(SysRole record);


    /**
     * Retrieves all roles from the system.
     *
     * @return list of all roles
     */
    List<SysRole> getAll();


    /**
     * Counts roles by name.
     * Used to validate role name uniqueness.
     *
     * @param name role name
     * @param id   role identifier (used to exclude current record during update)
     * @return number of roles matching the given name
     */
    int countByName(@Param("name") String name, @Param("id") Integer id);


    /**
     * Retrieves roles by a list of role IDs.
     * Commonly used for batch authorization queries.
     *
     * @param idList list of role identifiers
     * @return list of roles
     */
    List<SysRole> getByIdList(@Param("idList") List<Integer> idList);
}