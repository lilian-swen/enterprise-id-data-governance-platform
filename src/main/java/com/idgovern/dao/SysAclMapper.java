package com.idgovern.dao;

import com.idgovern.dto.PageQuery;
import com.idgovern.model.SysAcl;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * Data Access Object (DAO) for SysAcl entity.
 *
 * <p>
 * Provides CRUD operations and query methods for Access Control List (ACL)
 * entries in the RBAC system. Supports pagination, filtering by ACL module,
 * name, and URL.
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author   | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-03-04 | Lilian S.| Initial creation of SysAclMapper |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */
public interface SysAclMapper {

    /**
     * Deletes an ACL record by primary key.
     *
     * @param id ACL ID
     * @return number of rows affected
     */
    int deleteByPrimaryKey(Integer id);


    /**
     * Inserts a new ACL record.
     *
     * @param record SysAcl object
     * @return number of rows affected
     */
    int insert(SysAcl record);


    /**
     * Inserts a new ACL record selectively (only non-null fields).
     *
     * @param record SysAcl object
     * @return number of rows affected
     */
    int insertSelective(SysAcl record);


    /**
     * Selects an ACL record by primary key.
     *
     * @param id ACL ID
     * @return SysAcl object
     */
    SysAcl selectByPrimaryKey(Integer id);


    /**
     * Updates an ACL record selectively by primary key (only non-null fields).
     *
     * @param record SysAcl object
     * @return number of rows affected
     */
    int updateByPrimaryKeySelective(SysAcl record);


    /**
     * Updates an ACL record by primary key.
     *
     * @param record SysAcl object
     * @return number of rows affected
     */
    int updateByPrimaryKey(SysAcl record);


    /**
     * Counts the number of ACL records under a specific ACL module.
     *
     * @param aclModuleId ACL module ID
     * @return number of ACLs
     */
    int countByAclModuleId(@Param("aclModuleId") int aclModuleId);


    /**
     * Retrieves a paginated list of ACLs under a specific ACL module.
     *
     * @param aclModuleId ACL module ID
     * @param page        Pagination information
     * @return list of SysAcl objects
     */
    List<SysAcl> getPageByAclModuleId(@Param("aclModuleId") int aclModuleId, @Param("page") PageQuery page);


    /**
     * Counts ACLs by name within a specific ACL module, excluding a specific ID.
     * Useful for checking uniqueness during updates.
     *
     * @param aclModuleId ACL module ID
     * @param name        ACL name
     * @param id          ACL ID to exclude (can be null)
     * @return number of ACLs with the same name in the module
     */
    int countByNameAndAclModuleId(@Param("aclModuleId") int aclModuleId, @Param("name") String name, @Param("id") Integer id);


    /**
     * Retrieves all ACL records.
     *
     * @return list of SysAcl objects
     */
    List<SysAcl> getAll();


    /**
     * Retrieves ACL records by a list of IDs.
     *
     * @param idList list of ACL IDs
     * @return list of SysAcl objects
     */
    List<SysAcl> getByIdList(@Param("idList") List<Integer> idList);

    /**
     * Retrieves ACL records by their URL.
     *
     * @param url ACL URL
     * @return list of SysAcl objects
     */
    List<SysAcl> getByUrl(@Param("url") String url);
}