package com.idgovern.model;


/**
 * System Log Entity with BLOBs (Before/After Data Snapshots).
 *
 * <p>
 * Extends {@link SysLog} to include detailed change information for auditing purposes.
 * Contains old and new values of the entity being modified to track data changes.
 * This is especially useful for rollback, auditing, and compliance.
 * </p>
 *
 * ------------------------------------------------------------------------
 * | Version | Date       | Author   | Description                      |
 * ------------------------------------------------------------------------
 * | 1.0     | 2016-02-17 | Lilian S.| Initial creation of SysLogWithBLOBs |
 * ------------------------------------------------------------------------
 *
 * Usage:
 * <ul>
 *     <li>Stores before and after states of an entity for auditing</li>
 *     <li>Can be used in conjunction with SysLog for detailed operation logs</li>
 * </ul>
 *
 * @author Lilian
 * @version 1.0
 * @since 1.0
 */
public class SysLogWithBLOBs extends SysLog {

    /** The state of the entity before the operation */
    private String oldValue;

    /** The state of the entity after the operation */
    private String newValue;

    // =======================
    // Getters and Setters
    // =======================
    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue == null ? null : oldValue.trim();
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue == null ? null : newValue.trim();
    }
}