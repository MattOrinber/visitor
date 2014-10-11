package org.visitor.appportal.domain;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Table(name = "html_page")
public class HtmlPage implements Serializable, Copyable<HtmlPage> {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(HtmlPage.class);
    
	/** default page */
	public final static boolean DEFAULT_PAGE = true;
	/** edit page */
	public final static boolean EDIT_PAGE = false;
	
	/** no yet publish */
	public final static Integer PUBLISH_NO = 0;
	/** published */
	public final static Integer PUBLISH_YES = 1;
	
	/** index */
	public final static Integer PAGE_TEMPLATE_INDEX = 0;
	/** list */
	public final static Integer PAGE_TEMPLATE_LIST = 1;
	/** detail */
	public final static Integer PAGE_TEMPLATE_DETAIL = 2;
	/** navi */
	public final static Integer PAGE_TEMPLATE_NAVI = 3;

    // Raw attributes
    private Long pageId; // pk
    private String name; // not null
    private String path; // not null
    private String meta;
    private String tempMeta;
    private Date publishDate; // not null
    private transient String publishBy; // not null
    private Boolean ifDefaultPage; // not null
    private Integer siteId;
    private Integer pageType; // not null
    private transient String createBy; // not null
    private Date createDate; // not null
    private Date modDate; // not null
    private transient String modBy; // not null
    private Boolean ifQueryElement;//not null
    private String description;
    private Integer publishStatus; // not null

    // Technical attributes for query by example
    private Long templateId; // not null
    private Long folderId; // not null

    // Many to one
    private transient Folder folder; // not null (folderId)
    private transient Template template; // not null (templateId)
    private transient Site site;
    
    //Many to Many
    private List<Template> fragements; //
    // ---------------------------
    // Constructors
    // ---------------------------

    public HtmlPage() {
    }

    public HtmlPage(Long primaryKey) {
        this();
        setPrimaryKey(primaryKey);
    }

    // ---------------------------
    // Identifiable implementation
    // ---------------------------

    @Transient
    @XmlTransient
    public Long getPrimaryKey() {
        return getPageId();
    }

    public void setPrimaryKey(Long pageId) {
        setPageId(pageId);
    }
    // -------------------------------
    // Getter & Setter
    // -------------------------------

    // -- [pageId] ------------------------

    @Column(name = "page_id", nullable = false, unique = true, precision = 20)
    @GeneratedValue
    @Id
    public Long getPageId() {
        return pageId;
    }

    public void setPageId(Long pageId) {
        this.pageId = pageId;
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

    @NotEmpty
    @Length(max = 255)
    @Column(nullable = false)
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    // -- [templateId] ------------------------

    @Column(name = "template_id", nullable = false, precision = 20, insertable = false, updatable = false)
    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    // -- [folderId] ------------------------

    @Column(name = "folder_id", nullable = false, precision = 20, insertable = false, updatable = false)
    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    // -- [meta] ------------------------

    @Length(max = 65535)
    @Column(length = 65535)
    @JsonIgnore
    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    // -- [tempMeta] ------------------------

    @Length(max = 65535)
    @Column(name = "temp_meta", length = 65535)
    @JsonIgnore
    public String getTempMeta() {
        return tempMeta;
    }

    public void setTempMeta(String tempMeta) {
        this.tempMeta = tempMeta;
    }

    // -- [publishDate] ------------------------

    @Column(name = "publish_date", nullable = false, length = 19)
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
    @Column(name = "publish_by", nullable = false, length = 60)
    public String getPublishBy() {
        return publishBy;
    }

    public void setPublishBy(String publishBy) {
        this.publishBy = publishBy;
    }

    // -- [ifDefaultPage] ------------------------

    @NotNull
    @Column(name = "if_default_page", nullable = false, length = 0)
    public Boolean getIfDefaultPage() {
        return ifDefaultPage;
    }

    public void setIfDefaultPage(Boolean ifDefaultPage) {
        this.ifDefaultPage = ifDefaultPage;
    }

    // -- [ifDefaultPage] ------------------------

    @NotNull
    @Column(name = "if_query_element", nullable = false, length = 0)
    public Boolean getIfQueryElement() {
        return ifQueryElement;
    }
    /**
	 * @param ifQueryElement the ifQueryElement to set
	 */
	public void setIfQueryElement(Boolean ifQueryElement) {
		this.ifQueryElement = ifQueryElement;
	}

    // -- [siteId] ------------------------

	@Column(name = "site_id", precision = 10)
    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    // -- [pageType] ------------------------

    @NotNull
    @Column(name = "page_type", nullable = false, precision = 10)
    public Integer getPageType() {
        return pageType;
    }

    public void setPageType(Integer pageType) {
        this.pageType = pageType;
    }

    // -- [createBy] ------------------------

    @NotEmpty
    @Length(max = 60)
    @Column(name = "create_by", nullable = false, length = 60)
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    // -- [createDate] ------------------------

    @NotNull
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

    @NotNull
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

    @NotEmpty
    @Length(max = 60)
    @Column(name = "mod_by", nullable = false, length = 60)
    public String getModBy() {
        return modBy;
    }

    public void setModBy(String modBy) {
        this.modBy = modBy;
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

    @NotNull
    @Column(name = "publish_status", nullable = false, precision = 10)
    public Integer getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(Integer publishStatus) {
        this.publishStatus = publishStatus;
    }

    // -- [publishStatus] ------------------------


    // --------------------------------------------------------------------
    // Many to One support
    // --------------------------------------------------------------------

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // many-to-one: HtmlPage.folderId ==> Folder.folderId
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @JoinColumn(name = "folder_id", nullable = false)
    @ManyToOne(cascade = PERSIST, fetch = LAZY)
    @JsonIgnore
    public Folder getFolder() {
        return folder;
    }

    /**
     * Set the folder without adding this HtmlPage instance on the passed folder
     * If you want to preserve referential integrity we recommend to use
     * instead the corresponding adder method provided by Folder
     */
    public void setFolder(Folder folder) {
        this.folder = folder;

        // We set the foreign key property so it can be used by Hibernate search by Example facility.
        if (folder != null) {
            setFolderId(folder.getFolderId());
        } else {
            setFolderId(null);
        }
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // many-to-one: HtmlPage.siteId ==> Site.siteId
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
	 * @return the site
	 */
    @JoinColumn(name = "site_id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(fetch = LAZY)
    @JsonIgnore
	public Site getSite() {
		return site;
	}

	/**
	 * @param site the site to set
	 */
	public void setSite(Site site) {
		this.site = site;
	}
	
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // many-to-one: HtmlPage.templateId ==> Template.templateId
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	@JoinColumn(name = "template_id", nullable = false)
    @ManyToOne(cascade = PERSIST, fetch = LAZY)
    @JsonIgnore
    public Template getTemplate() {
        return template;
    }
	
    /**
     * Set the template without adding this HtmlPage instance on the passed template
     * If you want to preserve referential integrity we recommend to use
     * instead the corresponding adder method provided by Template
     */
    public void setTemplate(Template template) {
        this.template = template;

        // We set the foreign key property so it can be used by Hibernate search by Example facility.
        if (template != null) {
            setTemplateId(template.getTemplateId());
        } else {
            setTemplateId(null);
        }
    }
	
    // --------------------------------------------------------------------
    // Many to Many
    // --------------------------------------------------------------------

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // many-to-many: htmlpages ==> templates
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Returns the templates List.
     */
    @JoinTable(name = "fragement_page", joinColumns = @JoinColumn(name = "page_id"), inverseJoinColumns = @JoinColumn(name = "fragement_id"))
    @ManyToMany(cascade = ALL)
    @JsonIgnore
    public List<Template> getFragements() {
        return fragements;
    }
    
    /**
     * Set the fragements List.
     * It is recommended to use the helper method addSinglePage /  removeSinglePage
     * if you want to preserve referential integrity at the object level.
     *
     * @param fragements the List of Template
     */
    public void setFragements(List<Template> fragements) {
        this.fragements = fragements;
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
    public boolean equals(Object htmlPage) {
        if (this == htmlPage) {
            return true;
        }

        if (!(htmlPage instanceof HtmlPage)) {
            return false;
        }

        HtmlPage other = (HtmlPage) htmlPage;
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
     * Construct a readable string representation for this HtmlPage instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("htmlPage.pageId=[").append(getPageId()).append("]\n");
        result.append("htmlPage.name=[").append(getName()).append("]\n");
        result.append("htmlPage.path=[").append(getPath()).append("]\n");
        result.append("htmlPage.templateId=[").append(getTemplateId()).append("]\n");
        result.append("htmlPage.folderId=[").append(getFolderId()).append("]\n");
        result.append("htmlPage.meta=[").append(getMeta()).append("]\n");
        result.append("htmlPage.tempMeta=[").append(getTempMeta()).append("]\n");
        result.append("htmlPage.publishDate=[").append(getPublishDate()).append("]\n");
        result.append("htmlPage.publishBy=[").append(getPublishBy()).append("]\n");
        result.append("htmlPage.ifDefaultPage=[").append(getIfDefaultPage()).append("]\n");
        result.append("htmlPage.siteId=[").append(getSiteId()).append("]\n");
        result.append("htmlPage.pageType=[").append(getPageType()).append("]\n");
        result.append("htmlPage.createBy=[").append(getCreateBy()).append("]\n");
        result.append("htmlPage.createDate=[").append(getCreateDate()).append("]\n");
        result.append("htmlPage.modDate=[").append(getModDate()).append("]\n");
        result.append("htmlPage.modBy=[").append(getModBy()).append("]\n");
        result.append("htmlPage.description=[").append(getDescription()).append("]\n");
        result.append("htmlPage.publishStatus=[").append(getPublishStatus()).append("]\n");
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
    public HtmlPage copy() {
        HtmlPage htmlPage = new HtmlPage();
        copyTo(htmlPage);
        return htmlPage;
    }

    /**
     * Copy the current properties to the given object
     */
    @Override
    public void copyTo(HtmlPage htmlPage) {
        htmlPage.setPageId(getPageId());
        htmlPage.setName(getName());
        htmlPage.setPath(getPath());
        //htmlPage.setTemplateId(getTemplateId());
        //htmlPage.setFolderId(getFolderId());
        htmlPage.setMeta(getMeta());
        htmlPage.setTempMeta(getTempMeta());
        htmlPage.setPublishDate(getPublishDate());
        htmlPage.setPublishBy(getPublishBy());
        htmlPage.setIfDefaultPage(getIfDefaultPage());
        htmlPage.setSiteId(getSiteId());
        htmlPage.setPageType(getPageType());
        htmlPage.setCreateBy(getCreateBy());
        htmlPage.setCreateDate(getCreateDate());
        htmlPage.setModDate(getModDate());
        htmlPage.setModBy(getModBy());
        htmlPage.setDescription(getDescription());
        htmlPage.setPublishStatus(getPublishStatus());
        
        
        if (getFolder() != null) {
            htmlPage.setFolder(new Folder(getFolder().getPrimaryKey()));
        }
        if (getTemplate() != null) {
            htmlPage.setTemplate(new Template(getTemplate().getPrimaryKey()));
        }
    }
    
    public enum PageTypeEnum {
    	Index(0), List(1), Detail(2), Navi(3);
    	private Integer value;
    	private String displayName;
    	private String suffix;
    	private PageTypeEnum(Integer value) {
    		this.value = value;
    		switch(value) {
    		case 0:
    			this.displayName = "首页";
    			this.suffix = "index";
    			break;
    		case 1:
    			this.displayName = "列表页";
    			this.suffix = "list";
    			break;
    		case 2:
    			this.displayName = "详情页";
    			this.suffix = "detail";
    			break;
    		case 3:
    			this.displayName = "频道导航页";
    			this.suffix = "navi";
    			break;    			
    		}
    	}
    	public static PageTypeEnum getInstance(Integer value) {
    		if(null != value) {
	    		PageTypeEnum[] types = PageTypeEnum.values();
	    		for(PageTypeEnum type : types) {
	    			if(type.getValue().intValue() == value.intValue()) {
	    				return type;
	    			}
	    		}
    		}
    		return null;
    	}
    	
    	public static PageTypeEnum getInstance(String suffix) {
    		if(null != suffix) {
	    		PageTypeEnum[] types = PageTypeEnum.values();
	    		for(PageTypeEnum type : types) {
	    			if(StringUtils.equalsIgnoreCase(suffix, type.getSuffix())) {
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
		/**
		 * @return the suffix
		 */
		public String getSuffix() {
			return suffix;
		}
    }
    
    public enum PublishStatusEnum {
    	Unpublished, Published
    }
}