package org.visitor.appportal.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "site_value")
public class SiteValue implements Serializable, Copyable<SiteValue> {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(SiteValue.class);

    // Raw attributes
    private Long siteValueId; // pk
    private Integer siteId; // not null
    private Long value; // not null
    private Integer type; // not null

    // ---------------------------
    // Constructors
    // ---------------------------

    public SiteValue() {
    }

    public SiteValue(Long primaryKey) {
        this();
        setPrimaryKey(primaryKey);
    }

    // ---------------------------
    // Identifiable implementation
    // ---------------------------

    @Transient
    @XmlTransient
    public Long getPrimaryKey() {
        return getSiteValueId();
    }

    public void setPrimaryKey(Long siteValueId) {
        setSiteValueId(siteValueId);
    }

    // -------------------------------
    // Getter & Setter
    // -------------------------------

    // -- [siteValueId] ------------------------

    @Column(name = "site_value_id", nullable = false, unique = true, precision = 20)
    @GeneratedValue
    @Id
    public Long getSiteValueId() {
        return siteValueId;
    }

    public void setSiteValueId(Long siteValueId) {
        this.siteValueId = siteValueId;
    }
    
    // -- [siteId] ------------------------

    @NotNull
    @Column(name = "site_id", nullable = false, precision = 10)
    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    // -- [value] ------------------------

    @NotNull
    @Column(nullable = false, precision = 20)
    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
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
    public boolean equals(Object siteValue) {
        if (this == siteValue) {
            return true;
        }

        if (!(siteValue instanceof SiteValue)) {
            return false;
        }

        SiteValue other = (SiteValue) siteValue;
        return _getUid().equals(other._getUid());
    }

    @Override
    public int hashCode() {
        return _getUid().hashCode();
    }

    private Object _uid;

    private Object _getUid() {
        if (_uid == null) {
            if (null != this.getSiteValueId()) {
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
     * Construct a readable string representation for this SiteValue instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("siteValue.siteValueId=[").append(getSiteValueId()).append("]\n");
        result.append("siteValue.siteId=[").append(getSiteId()).append("]\n");
        result.append("siteValue.value=[").append(getValue()).append("]\n");
        result.append("siteValue.type=[").append(getType()).append("]\n");
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
    public SiteValue copy() {
        SiteValue siteValue = new SiteValue();
        copyTo(siteValue);
        return siteValue;
    }

    /**
     * Copy the current properties to the given object
     */
    @Override
    public void copyTo(SiteValue siteValue) {
        siteValue.setSiteValueId(getSiteValueId());
        siteValue.setSiteId(getSiteId());
        siteValue.setValue(getValue());
        siteValue.setType(getType());
    }
    
    public enum TypeEnum {
    	Theme(0), OperaVersion(1), ModelId(2), Platform(3), PlatformVerion(4), Brand(5), Resolution(6);
    	Integer value;
    	String displayName;
    	private TypeEnum(Integer value) {
    		this.value = value;
    		switch(this.value) {
    		case 1:
    			this.displayName = "opera版本";
    			break;
    		case 2:
    			this.displayName = "机型ID";
    			break;
    		case 3:
    			this.displayName = "手机平台";
    			break;
    		case 4:
    			this.displayName = "手机平台版本";
    			break;
    		case 5:
    			this.displayName = "手机品牌";
    			break;
    		case 6:
    			this.displayName = "分辨率";
    			break;
    		case 0:
    			this.displayName = "主题UI";
    			break;    			
    		}
    	}
    	public static TypeEnum getInstance(Integer value) {
    		if(value == null) {
    			return null;
    		}
    		TypeEnum[] types = TypeEnum.values();
    		for(TypeEnum type : types) {
    			if(type.value.intValue() == value.intValue()) {
    				return type;
    			}
    		}
    		return null;
    	}
		/**
		 * @return the value
		 */
		public Integer getValue() {
			return value;
		}
		/**
		 * @return the displayName
		 */
		public String getDisplayName() {
			return displayName;
		}
    } 
}