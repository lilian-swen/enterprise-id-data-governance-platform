package com.idgovern.dto;

import com.idgovern.model.SysAcl;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

/**
 * Data Transfer Object (DTO) for ACL (Access Control List) information.
 *
 * <p>
 * Extends the SysAcl entity and adds additional fields used for UI and permission
 * management, such as whether the ACL should be selected by default or whether
 * the current user has permission to access it.
 * </p>
 *
 * <p>
 * This class is typically used in tree structures for role-based access control
 * (RBAC) in the frontend.
 *
 * Explanation of "hasAcl": During permission assignment operations, a user must not grant permissions
 * beyond the scope of their own authorized privileges (i.e., no privilege escalation).
 *
 * For example, although the current page may display the complete set of system permissions,
 * the user can only assign permissions that they themselves possess when granting privileges to a role.
 * They are not allowed to assign all permissions listed on the page unless those permissions are within
 * their own authorization scope.
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author   | Description                     |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-03-02 | Lilian S.| Initial creation of AclDto      |
 * | 1.1     | 2026-02-26 | Lilian S.| Enhanced code comments and documentation clarity |
 * ------------------------------------------------------------------------
 *
 * @author Lilian S.
 * @version 1.1
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class AclDto extends SysAcl {

    /**
     * Indicates whether this ACL should be selected by default in UI trees.
     */
    private boolean checked = false;

    /**
     * Indicates whether the current user has permission to perform actions
     * associated with this ACL.
     */
    private boolean hasAcl = false;

    /**
     * Adapts a SysAcl entity into an AclDto.
     *
     * @param acl SysAcl entity
     * @return corresponding AclDto object
     */
    public static AclDto adapt(SysAcl acl) {
        AclDto dto = new AclDto();
        BeanUtils.copyProperties(acl, dto);
        return dto;
    }
}
