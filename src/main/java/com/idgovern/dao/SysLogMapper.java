package com.idgovern.dao;

import com.idgovern.dto.PageQuery;
import com.idgovern.dto.SearchLogDto;
import com.idgovern.model.SysLog;
import com.idgovern.model.SysLogWithBLOBs;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * Data Access Object (DAO) for SysLog and SysLogWithBLOBs entities.
 *
 * <p>
 * Provides CRUD operations and search/query methods for system logs.
 * Supports paginated retrieval and filtering using SearchLogDto.
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author   | Description                       |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-02-29 | Lilian S.| Initial creation of SysLogMapper  |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */
public interface SysLogMapper {

    /**
     * Deletes a log record by primary key.
     *
     * @param id log ID
     * @return number of rows affected
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * Inserts a new log record including BLOB fields.
     *
     * @param record SysLogWithBLOBs object
     * @return number of rows affected
     */
    int insert(SysLogWithBLOBs record);

    /**
     * Inserts a new log record selectively (only non-null fields) including BLOB fields.
     *
     * @param record SysLogWithBLOBs object
     * @return number of rows affected
     */
    int insertSelective(SysLogWithBLOBs record);

    /**
     * Selects a log record by primary key including BLOB fields.
     *
     * @param id log ID
     * @return SysLogWithBLOBs object
     */
    SysLogWithBLOBs selectByPrimaryKey(Integer id);

    /**
     * Updates a log record selectively (only non-null fields) including BLOB fields.
     *
     * @param record SysLogWithBLOBs object
     * @return number of rows affected
     */
    int updateByPrimaryKeySelective(SysLogWithBLOBs record);

    /**
     * Updates a log record including BLOB fields.
     *
     * @param record SysLogWithBLOBs object
     * @return number of rows affected
     */
    int updateByPrimaryKeyWithBLOBs(SysLogWithBLOBs record);

    /**
     * Updates a log record excluding BLOB fields.
     *
     * @param record SysLog object
     * @return number of rows affected
     */
    int updateByPrimaryKey(SysLog record);

    /**
     * Counts the total number of logs matching the search criteria.
     *
     * @param dto SearchLogDto object containing security criteria
     * @return total count of matching logs
     */
    int countBySearchDto(@Param("dto") SearchLogDto dto);

    /**
     * Retrieves a paginated list of logs based on search criteria.
     *
     * @param dto  SearchLogDto object containing security criteria
     * @param page PageQuery object for pagination
     * @return list of SysLogWithBLOBs objects
     */
    List<SysLogWithBLOBs> getPageListBySearchDto(@Param("dto") SearchLogDto dto, @Param("page") PageQuery page);
}