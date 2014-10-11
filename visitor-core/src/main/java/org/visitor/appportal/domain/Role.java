package org.visitor.appportal.domain;

import static org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Cache;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "role")
@Cache(usage = NONSTRICT_READ_WRITE)
public class Role implements Identifiable<Long>, Serializable, Copyable<Role> {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(Role.class);

    // Raw attributes
    private Long roleId; // pk
    private String roleName; // not null
    private String roleDes; // not null

    // ---------------------------
    // Constructors
    // ---------------------------

    public Role() {
    }

    public Role(Long primaryKey) {
        this();
        setPrimaryKey(primaryKey);
    }

    // ---------------------------
    // Identifiable implementation
    // ---------------------------

    @Transient
    @XmlTransient
    public Long getPrimaryKey() {
        return getRoleId();
    }

    public void setPrimaryKey(Long roleId) {
        setRoleId(roleId);
    }

    @Transient
    @XmlTransient
    public boolean isPrimaryKeySet() {
        return isRoleIdSet();
    }

    // -------------------------------
    // Getter & Setter
    // -------------------------------

    // -- [roleId] ------------------------

    @Column(name = "role_id", nullable = false, unique = true, precision = 20)
    @GeneratedValue
    @Id
    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Transient
    public boolean isRoleIdSet() {
        return roleId != null;
    }

    /**
     * Helper method to set the roleId attribute via an int.
     * @see #setRoleId(Long)
     */
    public void setRoleId(int roleId) {
        this.roleId = Long.valueOf(roleId);
    }

    // -- [roleName] ------------------------

    @NotEmpty
    @Length(max = 200)
    @Column(name = "role_name", nullable = false, length = 200)
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    // -- [roleDes] ------------------------

    @NotEmpty
    @Length(max = 200)
    @Column(name = "role_des", nullable = false, length = 200)
    public String getRoleDes() {
        return roleDes;
    }

    public void setRoleDes(String roleDes) {
        this.roleDes = roleDes;
    }

    // -----------------------------------------
    // Set defaults values
    // -----------------------------------------

    /**
     * Set the default values.
     */
    public void initDefaultValues() {
    }

    // -----------------------------------------
    // equals and hashCode
    // -----------------------------------------

    // The first time equals or hashCode is called,
    // we check if the primary key is present or not.
    // If yes: we use it in equals/hashCode
    // If no: we use a VMID during the entire life of this
    // instance even if later on this instance is assigned
    // a primary key.

    @Override
    public boolean equals(Object role) {
        if (this == role) {
            return true;
        }

        if (!(role instanceof Role)) {
            return false;
        }

        Role other = (Role) role;
        return _getUid().equals(other._getUid());
    }

    @Override
    public int hashCode() {
        return _getUid().hashCode();
    }

    private Object _uid;

    private Object _getUid() {
        if (_uid == null) {
            if (isPrimaryKeySet()) {
                _uid = getPrimaryKey();
            } else {
                _uid = new java.rmi.dgc.VMID();
                logger
                        .warn("DEVELOPER: hashCode has changed!."
                                + "If you encounter this message you should take the time to carefuly review equals/hashCode for: "
                                + getClass().getCanonicalName());
            }
        }
        return _uid;
    }

    // -----------------------------------------
    // toString
    // -----------------------------------------

    /**
     * Construct a readable string representation for this Role instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("role.roleId=[").append(getRoleId()).append("]\n");
        result.append("role.roleName=[").append(getRoleName()).append("]\n");
        result.append("role.roleDes=[").append(getRoleDes()).append("]\n");
        return result.toString();
    }

    // -----------------------------------------
    // Copyable Implementation
    // (Support for REST web layer)
    // -----------------------------------------

    /**
     * Return a copy of the current object
     */
    @Override
    public Role copy() {
        Role role = new Role();
        copyTo(role);
        return role;
    }

    /**
     * Copy the current properties to the given object
     */
    @Override
    public void copyTo(Role role) {
        role.setRoleId(getRoleId());
        role.setRoleName(getRoleName());
        role.setRoleDes(getRoleDes());
    }
}