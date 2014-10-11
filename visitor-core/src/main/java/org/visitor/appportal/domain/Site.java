package org.visitor.appportal.domain;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Table(name = "site")
public class Site implements Serializable, Copyable<Site> {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(Site.class);

    // Raw attributes
    private Integer siteId; // pk
    private String name; // not null
    private String schemaName;
    private Integer status; // not null
    private Integer companyId;
    private Long resolutionId;
    private String defaultPath;
    private String createBy; // not null
    private Date createDate; // not null
    private Date modDate; // not null
    private String modBy; // not null
    
    private Long indexTemplateId;
    private Long listTemplateId;
    private Long detailTemplateId;
    private Long naviTemplateId;
    
	// Many to one
	private transient Template indexTemplate; // (indexTemplateId)
	private transient Template listTemplate; // (listTemplateId)
	private transient Template detailTemplate; // (detailTemplateId)
	private transient Template naviTemplate; // (naviTemplateId)

	private List<SiteValue> siteValues = new ArrayList<SiteValue>();
	
	private String channels;
    // ---------------------------
    // Constructors
    // ---------------------------

    public Site() {
    }

    public Site(Integer primaryKey) {
        this();
        setPrimaryKey(primaryKey);
    }

    // ---------------------------
    // Identifiable implementation
    // ---------------------------

    @Transient
    @XmlTransient
    public Integer getPrimaryKey() {
        return getSiteId();
    }

    public void setPrimaryKey(Integer siteId) {
        setSiteId(siteId);
    }

    // -------------------------------
    // Getter & Setter
    // -------------------------------

    // -- [siteId] ------------------------

    @Column(name = "site_id", nullable = false, unique = true, precision = 10)
    @GeneratedValue
    @Id
    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    // -- [name] ------------------------

    @NotEmpty
    @Length(max = 120)
    @Column(nullable = false, length = 120)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // -- [schemaName] ------------------------

    @Length(max = 240)
    @Column(name = "schema_name", length = 240)
    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    // -- [status] ------------------------

    @Column(nullable = false, precision = 10)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    // -- [companyId] ------------------------

    @Column(name = "company_id", precision = 10)
    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    // -- [resolutionId] ------------------------

    @Column(name = "resolution_id", precision = 20)
    public Long getResolutionId() {
        return resolutionId;
    }

    public void setResolutionId(Long resolutionId) {
        this.resolutionId = resolutionId;
    }

    // -- [defaultPath] ------------------------

    @Length(max = 200)
    @Column(name = "default_path", length = 200)
    public String getDefaultPath() {
        return defaultPath;
    }

    public void setDefaultPath(String defaultPath) {
        this.defaultPath = defaultPath;
    }

    // -- [createBy] ------------------------

    @Length(max = 60)
    @Column(name = "create_by", nullable = false, length = 60)
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    // -- [createDate] ------------------------

    @Column(name = "create_date", nullable = false, length = 19)
    @Temporal(TIMESTAMP)
    @DateTimeFormat(iso = ISO.DATE)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    // -- [modDate] ------------------------

    @Column(name = "mod_date", nullable = false, length = 19)
    @Temporal(TIMESTAMP)
    @DateTimeFormat(iso = ISO.DATE)
    public Date getModDate() {
        return modDate;
    }

    public void setModDate(Date modDate) {
        this.modDate = modDate;
    }

    // -- [modBy] ------------------------
    @Length(max = 60)
    @Column(name = "mod_by", nullable = false, length = 60)
    public String getModBy() {
        return modBy;
    }

    public void setModBy(String modBy) {
        this.modBy = modBy;
    }
    
	// -- [indexTemplateId] ------------------------
    @NotNull
    @Column(name = "index_template_id", precision = 20, insertable = false, updatable = false)
    public Long getIndexTemplateId() {
		return indexTemplateId;
	}

	public void setIndexTemplateId(Long indexTemplateId) {
		this.indexTemplateId = indexTemplateId;
	}

	// -- [listTemplateId] ------------------------
	@NotNull
    @Column(name = "list_template_id", precision = 20, insertable = false, updatable = false)
	public Long getListTemplateId() {
		return listTemplateId;
	}

	public void setListTemplateId(Long listTemplateId) {
		this.listTemplateId = listTemplateId;
	}

	// -- [detailTemplateId] ------------------------
	@NotNull
    @Column(name = "detail_template_id", precision = 20, insertable = false, updatable = false)
	public Long getDetailTemplateId() {
		return detailTemplateId;
	}

	public void setDetailTemplateId(Long detailTemplateId) {
		this.detailTemplateId = detailTemplateId;
	}

	// -- [naviTemplateId] ------------------------
	@NotNull
    @Column(name = "navi_template_id", precision = 20, insertable = false, updatable = false)
	public Long getNaviTemplateId() {
		return naviTemplateId;
	}

	public void setNaviTemplateId(Long naviTemplateId) {
		this.naviTemplateId = naviTemplateId;
	}
	
	// --------------------------------------------------------------------
	// Many to One support
	// --------------------------------------------------------------------

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	// many-to-one: Site.indexTemplateId ==> Template.templateId
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	@JoinColumn(name = "index_template_id")
	@ManyToOne(cascade = PERSIST, fetch = LAZY)
	@JsonIgnore
	public Template getIndexTemplate() {
		return indexTemplate;
	}

	/**
	 * Set the indexTemplate without adding this Site instance on the
	 * passed indexTemplate If you want to preserve referential integrity we
	 * recommend to use instead the corresponding adder method provided by
	 * Category
	 */
	public void setIndexTemplate(Template indexTemplate) {
		this.indexTemplate = indexTemplate;

		// We set the foreign key property so it can be used by Hibernate search
		// by Example facility.
		if (indexTemplate != null) {
			setIndexTemplateId(indexTemplate.getTemplateId());
		} else {
			setIndexTemplateId(null);
		}
	}
	
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	// many-to-one: Site.listTemplateId ==> Template.templateId
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	@JoinColumn(name = "list_template_id")
	@ManyToOne(cascade = PERSIST, fetch = LAZY)
	@JsonIgnore
	public Template getListTemplate() {
		return listTemplate;
	}

	/**
	 * Set the listTemplate without adding this Site instance on the
	 * passed listTemplate If you want to preserve referential integrity we
	 * recommend to use instead the corresponding adder method provided by
	 * Category
	 */
	public void setListTemplate(Template listTemplate) {
		this.listTemplate = listTemplate;

		// We set the foreign key property so it can be used by Hibernate search
		// by Example facility.
		if (listTemplate != null) {
			setListTemplateId(listTemplate.getTemplateId());
		} else {
			setListTemplateId(null);
		}
	}
	
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	// many-to-one: Site.detailTemplateId ==> Template.templateId
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	@JoinColumn(name = "detail_template_id")
	@ManyToOne(cascade = PERSIST, fetch = LAZY)
	@JsonIgnore
	public Template getDetailTemplate() {
		return detailTemplate;
	}

	/**
	 * Set the detailTemplate without adding this Site instance on the
	 * passed detailTemplate If you want to preserve referential integrity we
	 * recommend to use instead the corresponding adder method provided by
	 * Category
	 */
	public void setDetailTemplate(Template detailTemplate) {
		this.detailTemplate = detailTemplate;

		// We set the foreign key property so it can be used by Hibernate search
		// by Example facility.
		if (detailTemplate != null) {
			setDetailTemplateId(detailTemplate.getTemplateId());
		} else {
			setDetailTemplateId(null);
		}
	}
	
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	// many-to-one: Site.naviTemplateId ==> Template.templateId
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	@JoinColumn(name = "navi_template_id")
	@ManyToOne(cascade = PERSIST, fetch = LAZY)
	@JsonIgnore
	public Template getNaviTemplate() {
		return naviTemplate;
	}

	/**
	 * Set the naviTemplate without adding this Site instance on the
	 * passed naviTemplate If you want to preserve referential integrity we
	 * recommend to use instead the corresponding adder method provided by
	 * Category
	 */
	public void setNaviTemplate(Template naviTemplate) {
		this.naviTemplate = naviTemplate;

		// We set the foreign key property so it can be used by Hibernate search
		// by Example facility.
		if (naviTemplate != null) {
			setNaviTemplateId(naviTemplate.getTemplateId());
		} else {
			setNaviTemplateId(null);
		}
	}

	/**
	 * @return the siteValues
	 */
	@OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
	@JoinColumn (name = "site_id")
	public List<SiteValue> getSiteValues() {
		return siteValues;
	}

	/**
	 * @param siteValues the siteValues to set
	 */
	public void setSiteValues(List<SiteValue> siteValues) {
		this.siteValues = siteValues;
	}
	
    // -- [channels] ------------------------
    @Length(max = 1024)
    @Column(name = "channels", nullable = true, length = 1024)
    public String getChannels() {
        return channels;
    }

    public void setChannels(String channels) {
        this.channels = channels;
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
    public boolean equals(Object site) {
        if (this == site) {
            return true;
        }

        if (!(site instanceof Site)) {
            return false;
        }

        Site other = (Site) site;
        return _getUid().equals(other._getUid());
    }

    @Override
    public int hashCode() {
        return _getUid().hashCode();
    }

    private Object _uid;

    private Object _getUid() {
        if (_uid == null) {
            if (null != this.getSiteId()) {
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
     * Construct a readable string representation for this Site instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("site.siteId=[").append(getSiteId()).append("]\n");
        result.append("site.name=[").append(getName()).append("]\n");
        result.append("site.schemaName=[").append(getSchemaName()).append("]\n");
        result.append("site.status=[").append(getStatus()).append("]\n");
        result.append("site.companyId=[").append(getCompanyId()).append("]\n");
        result.append("site.resolutionId=[").append(getResolutionId()).append("]\n");
        result.append("site.defaultPath=[").append(getDefaultPath()).append("]\n");
        result.append("site.createBy=[").append(getCreateBy()).append("]\n");
        result.append("site.createDate=[").append(getCreateDate()).append("]\n");
        result.append("site.modDate=[").append(getModDate()).append("]\n");
        result.append("site.modBy=[").append(getModBy()).append("]\n");
        result.append("site.indexTemplateId=[").append(getIndexTemplateId()).append("]\n");
        result.append("site.listTemplateId=[").append(getListTemplateId()).append("]\n");
        result.append("site.detailTemplateId=[").append(getDetailTemplateId()).append("]\n");
        result.append("site.naviTemplateId=[").append(getNaviTemplateId()).append("]\n");
        result.append("site.siteValues=[").append(getSiteValues()).append("]\n");
        
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
    public Site copy() {
        Site site = new Site();
        copyTo(site);
        return site;
    }

    /**
     * Copy the current properties to the given object
     */
    @Override
    public void copyTo(Site site) {
        site.setSiteId(getSiteId());
        site.setName(getName());
        site.setSchemaName(getSchemaName());
        site.setStatus(getStatus());
        site.setCompanyId(getCompanyId());
        site.setResolutionId(getResolutionId());
        site.setDefaultPath(getDefaultPath());
        site.setCreateBy(getCreateBy());
        site.setCreateDate(getCreateDate());
        site.setModDate(getModDate());
        site.setModBy(getModBy());
        site.setIndexTemplateId(getIndexTemplateId());
        site.setListTemplateId(getListTemplateId());
        site.setDetailTemplateId(getDetailTemplateId());
        site.setNaviTemplateId(getNaviTemplateId());

		if (getSiteValues() != null) {
			site.setSiteValues(getSiteValues());
		}
    }
    
    public enum StatusEnum {
    	Enable, Disable
    }
}