package org.visitor.appportal.domain;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.EAGER;
import static org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "account")
@NamedQuery(name = "Account.selectAll", query = "from org.visitor.appportal.domain.Account as account where 1 = 1", hints = {
        @QueryHint(name = "org.hibernate.comment", value = "enableDynamicOrderBySupport"),
        @QueryHint(name = "org.hibernate.cacheable", value = "true") })
@Cache(usage = NONSTRICT_READ_WRITE)
@FilterDef(name = "myAccountFilter", defaultCondition = "account_id = :currentAccountId ", parameters = @ParamDef(name = "currentAccountId", type = "org.hibernate.type.LongType"))
@Filter(name = "myAccountFilter")
public class Account implements Identifiable<Long>, Serializable, Copyable<Account> {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(Account.class);

    // Raw attributes
    private Long accountId; // pk
    private String username; // not null
    private String nickname; // not null
    private String password; // not null
    private String email;
    private Integer type; // not null
    private Integer status; // not null
    
    private List<Role> roles;

    // ---------------------------
    // Constructors
    // ---------------------------

    public Account() {
    }

    public Account(Long primaryKey) {
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
    // Role names support
    // -------------------------------

    /**
     * Default implementation returns hard coded granted authorities for this account (i.e. "ROLE_USER" and "ROLE_ADMIN").
     * TODO: You should override this method to provide your own custom authorities using your own logic.
     * Or you can follow Celerio Account Table convention. Please refer to Celerio Documentation.
     */
    @Transient
    @XmlTransient
    public List<String> getRoleNames() {
        List<String> roleNames = new ArrayList<String>();
        if (roles != null && roles.size() > 0) {
        	for (Role r : roles) {
        		roleNames.add(r.getRoleName());
        	}
        }
//        if ("user".equalsIgnoreCase(getUsername())) {
//            roleNames.add("ROLE_USER");
//        } else if ("admin".equalsIgnoreCase(getUsername())) {
//            roleNames.add("ROLE_USER");
//            roleNames.add("ROLE_ADMIN");
//        }

        logger.warn("Returning hard coded role names. TODO: get the real role names");
        return roleNames;
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

    // -- [username] ------------------------

    @NotEmpty
    @Length(max = 200)
    @Column(nullable = false, length = 200)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // -- [nickname] ------------------------

    @NotEmpty
    @Length(max = 200)
    @Column(nullable = false, length = 200)
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    // -- [password] ------------------------

    @NotEmpty
    @Length(max = 200)
    @Column(nullable = false, length = 200)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // -- [email] ------------------------

    @Length(max = 200)
    @Email
    @Column(length = 200)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // -- [type] ------------------------

    @NotNull
    @Column(nullable = false, precision = 10)
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    // -- [status] ------------------------
    @Column(nullable = false, precision = 10)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	// many-to-many: account ==> roles
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	/**
	 * Returns the roles List.
	 */
	@JoinTable(name = "account_role", joinColumns = @JoinColumn(name = "account_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	@ManyToMany(cascade = PERSIST, fetch = EAGER)
	@JsonIgnore
	public List<Role> getRoles() {
		return roles;
	}

	/**
	 * Set the roles List. It is recommended to use the helper method addRole
	 * / removeRole if you want to preserve referential integrity at the object
	 * level.
	 * 
	 * @param models
	 *            the List of ModelList
	 */
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	/**
	 * Helper method to add the passed role to the roles List.
	 */
	public boolean addRole(Role role) {
		return getRoles().add(role);
	}

	/**
	 * Helper method to remove the passed role from the roles List.
	 */
	public boolean removeRole(Role role) {
		return getRoles().remove(role);
	}

	/**
	 * Helper method to determine if the passed role is present in the roles
	 * List.
	 */
	public boolean containsRole(Role role) {
		return getRoles() != null && getRoles().contains(role);
	}

    // -----------------------------------------
    // Set defaults values
    // -----------------------------------------

    /**
     * Set the default values.
     */
    public void initDefaultValues() {
        setType(0);
        setStatus(0);
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
    public boolean equals(Object account) {
        if (this == account) {
            return true;
        }

        if (!(account instanceof Account)) {
            return false;
        }

        Account other = (Account) account;
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
     * Construct a readable string representation for this Account instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("account.accountId=[").append(getAccountId()).append("]\n");
        result.append("account.username=[").append(getUsername()).append("]\n");
        result.append("account.nickname=[").append(getNickname()).append("]\n");
        result.append("account.password=[").append(getPassword()).append("]\n");
        result.append("account.email=[").append(getEmail()).append("]\n");
        result.append("account.type=[").append(getType()).append("]\n");
        result.append("account.status=[").append(getStatus()).append("]\n");
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
    public Account copy() {
        Account account = new Account();
        copyTo(account);
        return account;
    }

    /**
     * Copy the current properties to the given object
     */
    @Override
    public void copyTo(Account account) {
        account.setAccountId(getAccountId());
        account.setUsername(getUsername());
        account.setNickname(getNickname());
        account.setPassword(getPassword());
        account.setEmail(getEmail());
        account.setType(getType());
        account.setStatus(getStatus());
        account.setRoles(account.getRoles());
    }
}