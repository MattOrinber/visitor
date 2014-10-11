package org.visitor.appportal.domain;

import static org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "account_role")
@Cache(usage = NONSTRICT_READ_WRITE)
public class AccountRole implements Identifiable<Long>, Serializable, Copyable<AccountRole> {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(AccountRole.class);

    // Raw attributes
    private Long accountId; // pk
    private Long roleId; // not null

    // ---------------------------
    // Constructors
    // ---------------------------

    public AccountRole() {
    }

    public AccountRole(Long primaryKey) {
        this();
        setPrimaryKey(primaryKey);
    }

    // ---------------------------
    // Identifiable implementation
    // ---------------------------

    @Transient
    @XmlTransient
    public Long getPrimaryKey() {
        return getAccountId();
    }

    public void setPrimaryKey(Long accountId) {
        setAccountId(accountId);
    }

    @Transient
    @XmlTransient
    public boolean isPrimaryKeySet() {
        return isAccountIdSet();
    }

    // -------------------------------
    // Getter & Setter
    // -------------------------------

    // -- [accountId] ------------------------

    @Column(name = "account_id", nullable = false, unique = true, precision = 20)
    @GeneratedValue
    @Id
    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    @Transient
    public boolean isAccountIdSet() {
        return accountId != null;
    }

    /**
     * Helper method to set the accountId attribute via an int.
     * @see #setAccountId(Long)
     */
    public void setAccountId(int accountId) {
        this.accountId = Long.valueOf(accountId);
    }

    // -- [roleId] ------------------------

    @NotNull
    @Column(name = "role_id", nullable = false, precision = 20)
    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    /**
     * Helper method to set the roleId attribute via an int.
     * @see #setRoleId(Long)
     */
    public void setRoleId(int roleId) {
        this.roleId = Long.valueOf(roleId);
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
    public boolean equals(Object accountRole) {
        if (this == accountRole) {
            return true;
        }

        if (!(accountRole instanceof AccountRole)) {
            return false;
        }

        AccountRole other = (AccountRole) accountRole;
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
     * Construct a readable string representation for this AccountRole instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("accountRole.accountId=[").append(getAccountId()).append("]\n");
        result.append("accountRole.roleId=[").append(getRoleId()).append("]\n");
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
    public AccountRole copy() {
        AccountRole accountRole = new AccountRole();
        copyTo(accountRole);
        return accountRole;
    }

    /**
     * Copy the current properties to the given object
     */
    @Override
    public void copyTo(AccountRole accountRole) {
        accountRole.setAccountId(getAccountId());
        accountRole.setRoleId(getRoleId());
    }
}