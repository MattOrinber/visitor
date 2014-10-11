package org.visitor.appportal.domain;

import static javax.persistence.CascadeType.PERSIST;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "picture")
public class Picture implements Serializable, Copyable<Picture> {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(Picture.class);

	// Raw attributes
	private Long pictureId; // pk
	private String name; // not null
	private String path; // not null
	private Integer type; // not null
	private Integer status; // not null
	private String description;

    private Integer siteId;
    private transient Site site;

	// ---------------------------
	// Constructors
	// ---------------------------

	public Picture() {
	}

	public Picture(Long primaryKey) {
		this();
		setPrimaryKey(primaryKey);
	}

	// ---------------------------
	// Identifiable implementation
	// ---------------------------

	@Transient
	@XmlTransient
	@JsonIgnore
	public Long getPrimaryKey() {
		return getPictureId();
	}

	public void setPrimaryKey(Long pictureId) {
		setPictureId(pictureId);
	}

	// -------------------------------
	// Getter & Setter
	// -------------------------------

	// -- [pictureId] ------------------------

	@Column(name = "picture_id", nullable = false, unique = true, precision = 20)
	@GeneratedValue
	@Id
	public Long getPictureId() {
		return pictureId;
	}

	public void setPictureId(Long pictureId) {
		this.pictureId = pictureId;

	}

	// -- [name] ------------------------

	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false, length = 200)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// -- [path] ------------------------

	@Length(max = 200)
	@Column(nullable = false, length = 200)
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
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

	// -- [description] ------------------------

	@Length(max = 255)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    // -- [siteId] ------------------------
	@Column(name = "site_id", precision = 10, insertable = false, updatable = false)
    public Integer getSiteId() {
        return siteId;
    }
    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }
	

    @JoinColumn(name = "site_id")
    @ManyToOne(cascade = PERSIST)
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;

        // We set the foreign key property so it can be used by Hibernate search by Example facility.
        if (site != null) {
            setSiteId(site.getSiteId());
        } else {
            setSiteId(null);
        }
    }
    
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
	public boolean equals(Object picture) {
		if (this == picture) {
			return true;
		}

		if (!(picture instanceof Picture)) {
			return false;
		}

		Picture other = (Picture) picture;
		return _getUid().equals(other._getUid());
	}

	@Override
	public int hashCode() {
		return _getUid().hashCode();
	}

	private Object _uid;

	private Object _getUid() {
		if (_uid == null) {
			if (null != this.getPrimaryKey()) {
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
	 * Construct a readable string representation for this PicList instance.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("picture.pictureId=[").append(getPictureId()).append("]\n");
		result.append("picture.name=[").append(getName()).append("]\n");
		result.append("picture.path=[").append(getPath()).append("]\n");
		result.append("picture.type=[").append(getType()).append("]\n");
		result.append("picture.status=[").append(getStatus()).append("]\n");
		result.append("picture.description=[").append(getDescription()).append("]\n");
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
	public Picture copy() {
		Picture picList = new Picture();
		copyTo(picList);
		return picList;
	}

	/**
	 * Copy the current properties to the given object
	 */
	@Override
	public void copyTo(Picture picture) {
		picture.setPictureId(getPictureId());
		picture.setName(getName());
		picture.setPath(getPath());
		picture.setType(getType());
		picture.setStatus(getStatus());
		picture.setDescription(getDescription());
	}
	public enum StatusEnum {
		Enable, Disable;
	}
    public enum PictureType {
    	folder(1), cms(2), watermark(3), other(9),special(10);
    	private Integer value;
    	private String displayName;
    	private PictureType(Integer value) {
    		this.value = value;
    		switch(value) {
    		case 1:
    			this.displayName = "folder";
    			break;
    		case 2:
    			this.displayName = "cms";
    			break;
    		case 3:
    			this.displayName = "watermark";
    			break;
    		case 9:
    			this.displayName = "other";
    		case 10:
    			this.displayName = "special";
    		}
    	}
    	public static PictureType getInstance(Integer value) {
    		if(null != value) {
    			PictureType[] types = PictureType.values();
	    		for(PictureType type : types) {
	    			if(type.getValue().intValue() == value.intValue()) {
	    				return type;
	    			}
	    		}
    		}
    		return null;
    	}
    	
    	public static PictureType getInstance(String displayName) {
    		if(null != displayName) {
    			PictureType[] types = PictureType.values();
	    		for(PictureType type : types) {
	    			if(StringUtils.equalsIgnoreCase(displayName, type.getDisplayName())) {
	    				return type;
	    			}
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