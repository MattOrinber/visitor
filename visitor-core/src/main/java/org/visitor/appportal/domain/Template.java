package org.visitor.appportal.domain;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
@Table(name = "template")
public class Template implements Serializable, Copyable<Template> {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(Template.class);

    // Raw attributes
    private Long templateId; // pk
    private Integer type; // not null
    private String name; // not null
    private String meta;
    private String tempMeta;
    private transient String createBy; // not null
    private Date createDate; // not null
    private Date modDate; // not null
    private transient String modBy; // not null
    private Date publishDate;
    private transient String publishBy;
    private String description;
    private Integer publishStatus; // not null

    private Integer siteId;
    
	// Many to many
	private transient List<HtmlPage> htmlPages;

    private transient Site site;

    // ---------------------------
    // Constructors
    // ---------------------------

    public Template() {
    }

    public Template(Long primaryKey) {
        this();
        setPrimaryKey(primaryKey);
    }

    // ---------------------------
    // Identifiable implementation
    // ---------------------------

    @Transient
    @XmlTransient
    public Long getPrimaryKey() {
        return getTemplateId();
    }

    public void setPrimaryKey(Long templateId) {
        setTemplateId(templateId);
    }

    // -------------------------------
    // Getter & Setter
    // -------------------------------

    // -- [templateId] ------------------------

    @Column(name = "template_id", nullable = false, unique = true, precision = 20)
    @GeneratedValue
    @Id
    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
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
    
    @Transient
    @JsonIgnore
    public String getTypeName(){
    	if(null != this.getType()) {
    		TypeEnum typeEnum = TypeEnum.getInstance(this.getType());
    		if(null != typeEnum) {
    			return typeEnum.getDisplayName();
    		}
    	}
    	return null;
    }

    // -- [name] ------------------------

    @NotEmpty
    @Length(max = 200)
    @Column(nullable = false, unique = true, length = 200)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // -- [meta] ------------------------

    @Length(max = 65535)
    @Column(length = 65535)
    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    // -- [tempMeta] ------------------------

    @Length(max = 65535)
    @Column(name = "temp_meta", length = 65535)
    public String getTempMeta() {
        return tempMeta;
    }

    public void setTempMeta(String tempMeta) {
        this.tempMeta = tempMeta;
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

    // -- [publishDate] ------------------------

    @Column(name = "publish_date", length = 19)
    @Temporal(TIMESTAMP)
    @DateTimeFormat(iso = ISO.DATE)
    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    // -- [publishBy] ------------------------

    @Length(max = 60)
    @Column(name = "publish_by", length = 60)
    public String getPublishBy() {
        return publishBy;
    }

    public void setPublishBy(String publishBy) {
        this.publishBy = publishBy;
    }

    // -- [description] ------------------------

    @Length(max = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    // -- [publishStatus] ------------------------

    @Column(name = "publish_status", nullable = false, precision = 10)
    public Integer getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(Integer publishStatus) {
        this.publishStatus = publishStatus;
    }
    
    @ManyToMany(mappedBy="fragements", fetch=FetchType.LAZY)
    @JsonIgnore
	public List<HtmlPage> getHtmlPages() {
		return htmlPages;
	}

	/**
	 * @param htmlPages the htmlPages to set
	 */
	public void setHtmlPages(List<HtmlPage> htmlPages) {
		this.htmlPages = htmlPages;
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
    public boolean equals(Object template) {
        if (this == template) {
            return true;
        }

        if (!(template instanceof Template)) {
            return false;
        }

        Template other = (Template) template;
        return _getUid().equals(other._getUid());
    }

    @Override
    public int hashCode() {
        return _getUid().hashCode();
    }

    private Object _uid;

    private Object _getUid() {
        if (_uid == null) {
            if (this.getTemplateId() != null) {
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
     * Construct a readable string representation for this Template instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("template.templateId=[").append(getTemplateId()).append("]\n");
        result.append("template.type=[").append(getType()).append("]\n");
        result.append("template.name=[").append(getName()).append("]\n");
        result.append("template.meta=[").append(getMeta()).append("]\n");
        result.append("template.tempMeta=[").append(getTempMeta()).append("]\n");
        result.append("template.createBy=[").append(getCreateBy()).append("]\n");
        result.append("template.createDate=[").append(getCreateDate()).append("]\n");
        result.append("template.modDate=[").append(getModDate()).append("]\n");
        result.append("template.modBy=[").append(getModBy()).append("]\n");
        result.append("template.publishDate=[").append(getPublishDate()).append("]\n");
        result.append("template.publishBy=[").append(getPublishBy()).append("]\n");
        result.append("template.description=[").append(getDescription()).append("]\n");
        result.append("template.publishStatus=[").append(getPublishStatus()).append("]\n");
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
    public Template copy() {
        Template template = new Template();
        copyTo(template);
        return template;
    }

    /**
     * Copy the current properties to the given object
     */
    @Override
    public void copyTo(Template template) {
        template.setTemplateId(getTemplateId());
        template.setType(getType());
        template.setName(getName());
        template.setCreateBy(getCreateBy());
        template.setCreateDate(getCreateDate());
        template.setModDate(getModDate());
        template.setModBy(getModBy());
        template.setPublishDate(getPublishDate());
        template.setPublishBy(getPublishBy());
        template.setDescription(getDescription());
        template.setPublishStatus(getPublishStatus());
        template.setHtmlPages(getHtmlPages());
    }
    
    public enum PublishStatusEnum {
    	Published, Unpublished
    }
    public enum TypeEnum {
    	/**
    	 * 头模板。include近来的共用模板，比如header/footer等
    	 */
    	Template(0), 
    	/**
    	 * 系统模板所使用的可以通用的模板
    	 */
    	Fragement(1), 
    	/**
    	 * 各个组件所使用的模板
    	 */
    	System(2), 
    	/**
    	 * 宏模板
    	 */
    	Tags(3);
    	private Integer value;
    	private String displayName;
    	private TypeEnum(Integer value) {
    		this.value = value;
    		switch(value) {
    		case 0:
    			this.displayName = "头模板";
    			break;
    		case 1:
    			this.displayName = "单独页";
    			break;
    		case 2:
    			this.displayName = "系统模板";
    			break;
    		case 3:
    			this.displayName = "宏模板";
    			break;    			
    		}
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
    	
		public static TypeEnum getInstance(Integer value) {
			TypeEnum[] values = TypeEnum.values();
			for(TypeEnum typeValue : values) {
				if(typeValue.getValue().intValue() == value.intValue()) {
					return typeValue;
				}
			}
			return null;
		}
    }
}