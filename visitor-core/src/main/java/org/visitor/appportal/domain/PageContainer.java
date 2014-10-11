package org.visitor.appportal.domain;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Table(name = "page_site_container", uniqueConstraints = { @UniqueConstraint(columnNames = { "page_id", "container_id" }) })
public class PageContainer implements Serializable, Copyable<PageContainer> {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(PageContainer.class);

    // Composite primary key
    private PageContainerPk pageContainerPk = new PageContainerPk();

    // Raw attributes
    private Long folderId; // not null
    private Integer siteId; // not null
    private String createBy; // not null
    private Date createDate; // not null
    private Date modDate; // not null
    private String modBy; // not null

    // Many to one
    private RecommandContainer container; // not null (containerId)
    private HtmlPage page; // not null (pageId)
    
    private Site site; // not null (siteId)
    private Folder folder; // not null (folderId)

    //显示类型
    private Integer showType; // not null
    
    // ---------------------------
    // Constructors
    // ---------------------------

    public PageContainer() {
    }

    public PageContainer(PageContainerPk primaryKey) {
        this();
        setPrimaryKey(primaryKey);
    }

    // ---------------------------
    // Identifiable implementation
    // ---------------------------

    @Transient
    @XmlTransient
    public PageContainerPk getPrimaryKey() {
        return getPageContainerPk();
    }

    public void setPrimaryKey(PageContainerPk pageContainerPk) {
        setPageContainerPk(pageContainerPk);
    }

    // -----------------------
    // Composite Primary Key
    // -----------------------

    /**
     * Returns the composite primary key.
     */
    @EmbeddedId
    public PageContainerPk getPageContainerPk() {
        return pageContainerPk;
    }

    /**
     * Set the composite primary key.
     * @param pageContainerPk the composite primary key.
     */
    public void setPageContainerPk(PageContainerPk pageContainerPk) {
        this.pageContainerPk = pageContainerPk;
    }

    /**
     * Helper method to set directly the containerId into the PageContainerPk corresponding field.
     * todo document $pkAttribute.comment
     * @param containerId the containerId
     */
    public void setContainerId(Long containerId) {
        if (getPageContainerPk() == null) {
            setPageContainerPk(new PageContainerPk());
        }

        getPageContainerPk().setContainerId(containerId);
    }

    /**
     * Helper method to get directly the containerId from the entity.root.primaryKey.type corresponding field.
     * @return the containerId
     */
    @Transient
    @XmlTransient
    public Long getContainerId() {
        return getPageContainerPk() != null ? getPageContainerPk().getContainerId() : null;
    }

    /**
     * Helper method to set directly the pageId into the PageContainerPk corresponding field.
     * todo document $pkAttribute.comment
     * @param pageId the pageId
     */
    public void setPageId(Long pageId) {
        if (getPageContainerPk() == null) {
            setPageContainerPk(new PageContainerPk());
        }

        getPageContainerPk().setPageId(pageId);
    }

    /**
     * Helper method to get directly the pageId from the entity.root.primaryKey.type corresponding field.
     * @return the pageId
     */
    @Transient
    @XmlTransient
    public Long getPageId() {
        return getPageContainerPk() != null ? getPageContainerPk().getPageId() : null;
    }

    // -------------------------------
    // Getter & Setter
    // -------------------------------

    // -- [folderId] ------------------------

    @NotNull
    @Column(name = "folder_id", nullable = false, precision = 20)
    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    /**
     * Helper method to set the folderId attribute via an int.
     * @see #setFolderId(Long)
     */
    public void setFolderId(int folderId) {
        this.folderId = Long.valueOf(folderId);
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

    // --------------------------------------------------------------------
    // Many to One support
    // --------------------------------------------------------------------

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // many-to-one: PageContainer.containerId ==> RecommandContainer.containerId
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @JoinColumn(name = "container_id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(cascade = PERSIST, fetch = LAZY)
    public RecommandContainer getContainer() {
        return container;
    }

    /**
     * Set the container without adding this PageContainer instance on the passed container
     * If you want to preserve referential integrity we recommend to use
     * instead the corresponding adder method provided by RecommandContainer
     */
    public void setContainer(RecommandContainer container) {
        this.container = container;

        // We set the foreign key property so it can be used by Hibernate search by Example facility.
        if (container != null) {
            setContainerId(container.getContainerId());
        } // when null, we do not propagate it to the pk.
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // many-to-one: PageContainer.pageId ==> HtmlPage.pageId
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @JoinColumn(name = "page_id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(cascade = PERSIST, fetch = LAZY)
    public HtmlPage getPage() {
        return page;
    }

    /**
     * Set the page without adding this PageContainer instance on the passed page
     * If you want to preserve referential integrity we recommend to use
     * instead the corresponding adder method provided by HtmlPage
     */
    public void setPage(HtmlPage page) {
        this.page = page;

        // We set the foreign key property so it can be used by Hibernate search by Example facility.
        if (page != null) {
            setPageId(page.getPageId());
        } // when null, we do not propagate it to the pk.
    }
    
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // many-to-one: PageContainer.siteId ==> Site.siteId
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @NotNull
    @JoinColumn(name = "site_id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(cascade = PERSIST, fetch = LAZY)
    @JsonIgnore
    public Site getSite() {
        return site;
    }

    /**
     * Set the site without adding this PageContainer instance on the passed site
     * If you want to preserve referential integrity we recommend to use
     * instead the corresponding adder method provided by Site
     */
    public void setSite(Site site) {
        this.site = site;

        // We set the foreign key property so it can be used by Hibernate search by Example facility.
        if (site != null) {
            setSiteId(site.getSiteId());
        } else {
            setSiteId(null);
        }
    }
    
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // many-to-one: PageContainer.folderId ==> Folder.folderId
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @NotNull
    @JoinColumn(name = "folder_id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(cascade = PERSIST, fetch = LAZY)
    @JsonIgnore
    public Folder getFolder() {
        return folder;
    }

    /**
     * Set the folder without adding this PageContainer instance on the passed folder
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

    @NotNull
    @Column(name = "showtype", nullable = false)
    public Integer getShowType() {
        return showType;
    }

    public void setShowType(Integer showType) {
        this.showType = showType;
    }
    
    // -----------------------------------------
    // Set defaults values
    // -----------------------------------------

    /**
     * Set the default values.
     */
    public void initDefaultValues() {
    	this.setShowType(ShowTypeEnum.Manual.getValue());
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
    public boolean equals(Object pageContainer) {
        if (this == pageContainer) {
            return true;
        }

        if (!(pageContainer instanceof PageContainer)) {
            return false;
        }

        PageContainer other = (PageContainer) pageContainer;
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
     * Construct a readable string representation for this PageContainer instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("pageContainer.folderId=[").append(getFolderId()).append("]\n");
        result.append("pageContainer.siteId=[").append(getSiteId()).append("]\n");
        result.append("pageContainer.createBy=[").append(getCreateBy()).append("]\n");
        result.append("pageContainer.createDate=[").append(getCreateDate()).append("]\n");
        result.append("pageContainer.modDate=[").append(getModDate()).append("]\n");
        result.append("pageContainer.modBy=[").append(getModBy()).append("]\n");
        result.append("pageContainer.showType=[").append(getShowType()).append("]\n");
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
    public PageContainer copy() {
        PageContainer pageContainer = new PageContainer();
        copyTo(pageContainer);
        return pageContainer;
    }

    /**
     * Copy the current properties to the given object
     */
    @Override
    public void copyTo(PageContainer pageContainer) {
        pageContainer.setPageId(getPageId());
        pageContainer.setContainerId(getContainerId());
        pageContainer.setFolderId(getFolderId());
        pageContainer.setSiteId(getSiteId());
        pageContainer.setCreateBy(getCreateBy());
        pageContainer.setCreateDate(getCreateDate());
        pageContainer.setModDate(getModDate());
        pageContainer.setModBy(getModBy());
        if (getContainer() != null) {
            pageContainer.setContainer(new RecommandContainer(getContainer().getPrimaryKey()));
        }
        if (getPage() != null) {
            pageContainer.setPage(new HtmlPage(getPage().getPrimaryKey()));
        }
        if (getSite() != null) {
        	pageContainer.setSite(new Site(getSite().getPrimaryKey()));
        }
        if (getFolder() != null) {
        	pageContainer.setFolder(new Folder(getFolder().getPrimaryKey()));
        }
        
        pageContainer.setShowType(getShowType());

    }
    
	public enum ShowTypeEnum {
		Manual, Auto;
		public static ShowTypeEnum getInstance(Integer value) {
    		if(null != value) {
    			ShowTypeEnum[] types = ShowTypeEnum.values();
	    		for(ShowTypeEnum type : types) {
	    			if(type.ordinal() == value.intValue()) {
	    				return type;
	    			}
	    		}
    		}
    		return null;
    	}
		public Integer getValue() {
			return this.ordinal();
		}
		public String getName() {
			switch(this) {
				case Manual:
					return "运营产品";
				case Auto:
					return "定时产品";
			}
			return this.name().toLowerCase();
		}
	}
    
}