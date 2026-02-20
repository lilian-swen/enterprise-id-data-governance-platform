package com.idgovern.dao;

import com.idgovern.model.SysDept;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Data Access Object (DAO) for Department management.
 *
 * <p>
 * This interface defines CRUD operations and hierarchical queries
 * for the department module in the RBAC system.
 * </p>
 *
 * <p>
 * Responsibilities:
 * <ul>
 *     <li>Basic CRUD operations</li>
 *     <li>Department hierarchy queries</li>
 *     <li>Level-based tree structure support</li>
 *     <li>Uniqueness validation</li>
 *     <li>Batch level updates (used during tree restructuring)</li>
 * </ul>
 * </p>
 *
 * <p>
 * Integrated with MyBatis for ORM mapping.
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author   | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-02-16 | Lilian S.| Initial creation                |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */
public interface SysDeptMapper {

    /**
     * Deletes a department by its primary key.
     *
     * @param id department ID
     * @return number of affected rows
     */
    int deleteByPrimaryKey(@Param("id") Integer id);


    /**
     * Inserts a new department record (all fields).
     *
     * @param record department entity
     * @return number of affected rows
     */
    int insert(SysDept record);


    /**
     * Inserts a department record selectively.
     * Only non-null fields will be inserted.
     *
     * @param record department entity
     * @return number of affected rows
     */
    int insertSelective(SysDept record);


    /**
     * Retrieves a department by its primary key.
     *
     * @param id department ID
     * @return department entity
     */
    SysDept selectByPrimaryKey(@Param("id") Integer id);


    /**
     * Updates department record selectively.
     * Only non-null fields will be updated.
     *
     * @param record department entity
     * @return number of affected rows
     */
    int updateByPrimaryKeySelective(SysDept record);


    /**
     * Updates department record completely.
     *
     * @param record department entity
     * @return number of affected rows
     */
    int updateByPrimaryKey(SysDept record);


    /**
     * Retrieves all departments.
     *
     * @return list of all departments
     */
    List<SysDept> getAllDept();


    /**
     * Retrieves child departments by hierarchical level.
     *
     * <p>
     * Used in tree construction. The "level" field
     * represents the path-based hierarchical encoding.
     * </p>
     *
     * @param level department level string
     * @return list of child departments
     * 2016-02-16
     */
    List<SysDept> getChildDeptListByLevel(@Param("level") String level);


    /**
     * Batch updates department levels.
     *
     * <p>
     * Used when a department is moved within the tree,
     * requiring its descendants' levels to be recalculated.
     * </p>
     *
     * @param sysDeptList list of departments to update
     */
    void batchUpdateLevel(@Param("sysDeptList") List<SysDept> sysDeptList);


    /**
     * Counts departments with the same name under the same parent.
     *
     * <p>
     * Used to enforce uniqueness constraint within the same parent node.
     * </p>
     *
     * @param parentId parent department ID. This is an Object type, it’s better to initialize it with a default value to avoid a NullPointerException.
     * @param name department name
     * @param id department ID (exclude self when updating)
     * @return count of matching records
     * 2016-02-16
     */
    int countByNameAndParentId(@Param("parentId") Integer parentId, @Param("name") String name, @Param("id") Integer id);


    /**
     * Counts number of child departments under a given parent.
     *
     * <p>
     * Used to prevent deletion of departments
     * that still contain sub-departments.
     * </p>
     *
     * @param deptId parent department ID
     * @return number of child departments
     */
    int countByParentId(@Param("deptId") int deptId);
}