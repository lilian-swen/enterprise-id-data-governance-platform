package com.idgovern.dao;

import com.idgovern.model.SysAclModule;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * Data Access Object (DAO) for SysAclModule entity.
 *
 * <p>
 * Provides CRUD operations and query methods for ACL Modules in the RBAC system.
 * Supports hierarchical structure queries, batch updates of levels, and
 * uniqueness checks.
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author   | Description                       |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-03-04 | Lilian S.| Initial creation of SysAclModuleMapper |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */
public interface SysAclModuleMapper {

    /**
     * Deletes an ACL module by primary key.
     *
     * @param id ACL module ID
     * @return number of rows affected
     */
    int deleteByPrimaryKey(Integer id);


    /**
     * Inserts a new ACL module record.
     *
     * @param record SysAclModule object
     * @return number of rows affected
     */
    int insert(SysAclModule record);


    /**
     * Inserts a new ACL module record selectively (only non-null fields).
     *
     * @param record SysAclModule object
     * @return number of rows affected
     */
    int insertSelective(SysAclModule record);


    /**
     * Selects an ACL module by primary key.
     *
     * @param id ACL module ID
     * @return SysAclModule object
     */
    SysAclModule selectByPrimaryKey(Integer id);


    /**
     * Updates an ACL module record selectively by primary key (only non-null fields).
     *
     * @param record SysAclModule object
     * @return number of rows affected
     */
    int updateByPrimaryKeySelective(SysAclModule record);


    /**
     * Updates an ACL module record by primary key.
     *
     * @param record SysAclModule object
     * @return number of rows affected
     */
    int updateByPrimaryKey(SysAclModule record);


    /**
     * Counts ACL modules with the same name under a given parent ID, excluding a specific ID.
     * Useful for checking uniqueness during updates.
     *
     * @param parentId Parent ACL module ID
     * @param name     ACL module name
     * @param id       ACL module ID to exclude (can be null)
     * @return number of matching ACL modules
     */
    int countByNameAndParentId(@Param("parentId") Integer parentId, @Param("name") String name, @Param("id") Integer id);


    /**
     * Retrieves child ACL modules by hierarchical level.
     *
     * @param level hierarchical level string
     * @return list of child SysAclModule objects
     */
    List<SysAclModule> getChildAclModuleListByLevel(@Param("level") String level);


    /**
     * Batch updates the level field for a list of ACL modules.
     *
     * @param sysAclModuleList list of SysAclModule objects
     */
    void batchUpdateLevel(@Param("sysAclModuleList") List<SysAclModule> sysAclModuleList);


    /**
     * Retrieves all ACL modules.
     *
     * @return list of SysAclModule objects
     */
    List<SysAclModule> getAllAclModule();


    /**
     * Counts the number of child ACL modules under a specific parent.
     *
     * @param aclModuleId parent ACL module ID
     * @return number of child ACL modules
     */
    int countByParentId(@Param("aclModuleId") int aclModuleId);

}