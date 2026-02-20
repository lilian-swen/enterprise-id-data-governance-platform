package com.idgovern.dao;

import com.idgovern.model.SysRoleAcl;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * Data Access Object (DAO) for SysRoleAcl entity.
 *
 * <p>
 * Provides CRUD operations and relationship management between roles and ACLs.
 * Supports batch inserts, deletions by role, and retrieving ACL IDs or role IDs.
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author   | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-02-29 | Lilian S.| Initial creation of SysRoleAclMapper |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */
public interface SysRoleAclMapper {

    /**
     * Deletes a role-ACL record by primary key.
     *
     * @param id record ID
     * @return number of rows affected
     */
    int deleteByPrimaryKey(Integer id);


    /**
     * Inserts a new role-ACL record.
     *
     * @param record SysRoleAcl object
     * @return number of rows affected
     */
    int insert(SysRoleAcl record);


    /**
     * Inserts a new role-ACL record selectively (only non-null fields).
     *
     * @param record SysRoleAcl object
     * @return number of rows affected
     */
    int insertSelective(SysRoleAcl record);


    /**
     * Selects a role-ACL record by primary key.
     *
     * @param id record ID
     * @return SysRoleAcl object
     */
    SysRoleAcl selectByPrimaryKey(Integer id);


    /**
     * Updates a role-ACL record selectively (only non-null fields).
     *
     * @param record SysRoleAcl object
     * @return number of rows affected
     */
    int updateByPrimaryKeySelective(SysRoleAcl record);


    /**
     * Updates a role-ACL record.
     *
     * @param record SysRoleAcl object
     * @return number of rows affected
     */
    int updateByPrimaryKey(SysRoleAcl record);


    /**
     * Retrieves ACL ID list by a list of role IDs.
     *
     * @param roleIdList list of role IDs
     * @return list of ACL IDs
     */
    List<Integer> getAclIdListByRoleIdList(@Param("roleIdList") List<Integer> roleIdList);


    /**
     * Deletes all role-ACL mappings for a given role ID.
     *
     * @param roleId role ID
     */
    void deleteByRoleId(@Param("roleId") int roleId);


    /**
     * Batch inserts role-ACL mappings.
     *
     * @param roleAclList list of SysRoleAcl objects
     */
    void batchInsert(@Param("roleAclList") List<SysRoleAcl> roleAclList);


    /**
     * Retrieves role ID list by a given ACL ID.
     *
     * @param aclId ACL ID
     * @return list of role IDs
     */
    List<Integer> getRoleIdListByAclId(@Param("aclId") int aclId);
}