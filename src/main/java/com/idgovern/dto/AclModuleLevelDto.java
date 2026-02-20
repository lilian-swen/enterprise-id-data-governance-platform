package com.idgovern.dto;

import com.google.common.collect.Lists;
import com.idgovern.model.SysAclModule;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;
import java.util.List;

/**
 * Data Transfer Object (DTO) for ACL Module with hierarchical structure.
 *
 * <p>
 * Extends the SysAclModule entity and adds:
 * <ul>
 *     <li>Nested child ACL modules (aclModuleList) for hierarchical representation.</li>
 *     <li>List of ACLs (aclList) under this module.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Typically used to build frontend tree structures representing ACL modules
 * and their associated access control entries in a Role-Based Access Control (RBAC) system.
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author   | Description                          |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-03-02 | Lilian S.| Initial creation of AclModuleLevelDto |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class AclModuleLevelDto extends SysAclModule {

    /**
     * List of child ACL modules for building hierarchical module trees.
     */
    private List<AclModuleLevelDto> aclModuleList = Lists.newArrayList();

    /**
     * List of ACLs (permissions) under this module.
     */
    private List<AclDto> aclList = Lists.newArrayList();


    /**
     * Converts a SysAclModule entity into an AclModuleLevelDto.
     *
     * @param aclModule SysAclModule entity
     * @return corresponding AclModuleLevelDto
     */
    public static AclModuleLevelDto adapt(SysAclModule aclModule) {
        AclModuleLevelDto dto = new AclModuleLevelDto();
        BeanUtils.copyProperties(aclModule, dto);
        return dto;
    }
}
