package com.idgovern.dao;

import com.idgovern.beans.PageQuery;
import com.idgovern.model.SysUser;
import org.apache.ibatis.annotations.Param;
import java.util.List;


/**
 * System User Data Access Object (DAO).
 *
 * <p>
 * This interface defines database operations for the {@link SysUser} entity.
 * It is used by MyBatis for ORM (Object-Relational Mapping) between
 * the application and the underlying database.
 * </p>
 *
 * <p>
 * The mapper provides:
 * <ul>
 *     <li>Basic CRUD operations</li>
 *     <li>Department-based pagination queries</li>
 *     <li>Uniqueness validation (email, telephone)</li>
 *     <li>Batch user retrieval</li>
 * </ul>
 * </p>
 *
 * <p>
 * SQL statements corresponding to these methods are typically defined
 * in the MyBatis XML mapper configuration file.
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author   | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-02-18 | Lilian S.| Initial creation                |
 * | 1.1     | 2016-02-20 | Updated  | Professional documentation added|
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.1
 * @since 1.0
 */
public interface SysUserMapper {

    /**
     * Deletes a user by primary key.
     *
     * @param id user ID
     * @return number of rows affected
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * Inserts a new user record.
     *
     * @param record complete user entity
     * @return number of rows affected
     */
    int insert(SysUser record);

    /**
     * Inserts a new user record selectively.
     *
     * <p>
     * Only non-null fields will be included in the SQL statement.
     * </p>
     *
     * @param record user entity
     * @return number of rows affected
     */
    int insertSelective(SysUser record);

    /**
     * Retrieves a user by primary key.
     *
     * @param id user ID
     * @return SysUser entity
     */
    SysUser selectByPrimaryKey(Integer id);


    /**
     * Updates user record selectively by primary key.
     *
     * <p>
     * Only non-null fields will be updated.
     * </p>
     *
     * @param record user entity
     * @return number of rows affected
     */
    int updateByPrimaryKeySelective(SysUser record);

    /**
     * Updates the entire user record by primary key.
     *
     * @param record complete user entity
     * @return number of rows affected
     */
    int updateByPrimaryKey(SysUser record);

    /**
     * Finds a user by keyword.
     *
     * <p>
     * Keyword may represent username, email, or telephone.
     * Used primarily for login or quick search functionality.
     * </p>
     *
     * @param keyword search keyword
     * @return SysUser entity if found
     */
    SysUser findByKeyword(@Param("keyword") String keyword);


    /**
     * Counts users by email (for uniqueness validation).
     *
     * @param mail email address
     * @param id   user ID (excluded when updating)
     * @return number of users with the same email
     */
    int countByMail(@Param("mail") String mail, @Param("id") Integer id);

    /**
     * Counts users by telephone (for uniqueness validation).
     *
     * @param telephone telephone number
     * @param id        user ID (excluded when updating)
     * @return number of users with the same telephone
     */
    int countByTelephone(@Param("telephone") String telephone, @Param("id") Integer id);


    /**
     * Counts the number of users under a specific department.
     *
     * @param deptId department ID
     * @return user count
     */
    int countByDeptId(@Param("deptId") int deptId);


    /**
     * Retrieves paginated users by department ID.
     *
     * @param deptId department ID
     * @param page   pagination parameters
     * @return list of users within the specified page
     */
    List<SysUser> getPageByDeptId(@Param("deptId") int deptId, @Param("page") PageQuery page);

    /**
     * Retrieves users by a list of IDs.
     *
     * <p>
     * Typically used for batch queries or permission association.
     * </p>
     *
     * @param idList list of user IDs
     * @return list of SysUser entities
     */
    List<SysUser> getByIdList(@Param("idList") List<Integer> idList);

    /**
     * Retrieves all users in the system.
     *
     * @return list of all users
     */
    List<SysUser> getAll();
}