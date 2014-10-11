package org.visitor.appportal.domain;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import org.visitor.util.AppStringUtils;

@Entity
@Table(name = "folder")
public class Folder implements Serializable, Copyable<Folder> {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(Folder.class);
    
	/** enable */
	public static final int ENABLE = 0;
	/** disable */
	public static final int DISABLE = 1;
    
	/** site system folder */
	public final static Integer FOLDER_TYPE_SYSTEM = 0;
	/** site bussiness folder */
	public final static Integer FOLDER_TYPE_BUSSINESS = 1;

    // Raw attributes
    private Long folderId;               // pk
    private String name;                 // not null
    private String aliasName;            // not null
    private String path;                 // not null
    private Integer status;              // not null
    private Date sortOrder;              // not null
    private transient String createBy;             // not null
    private Date createDate;             // not null
    private Date modDate;                // not null
    private transient String modBy;                // not null
    private String description;
	private Long nsThread; // not null
	private Long nsLeft; // not null
	private Long nsRight; // not null
    private Integer folderType;          // not null
	private Integer miniLab;

    // Technical attributes for query by example
    private Integer siteId;              // not null
    private Long parentFolderId;
    private Long indexPageId;
    private Long listPageId;
    private Long naviPageId;
    private Long detailPageId;
    private Long picId;
    private Long tagId;
	private Integer safeType; //下载文件安全类型

    private transient Long productCount;
    // Many to one
    private transient Category tag;               // (tagId)
    private transient HtmlPage detailPage;        // (detailPageId)
    private transient HtmlPage listPage;          // (listPageId)
    private transient HtmlPage indexPage;         // (indexPageId)
    private transient HtmlPage naviPage;          // (naviPageId)
    private transient Site site;                  // not null (siteId)
    private transient Folder parentFolder;        // (parentFolderId)
    private Picture pic;                // (picId)
    
    // Many to many
    private transient List<Category> tags = new ArrayList<Category>();
    
    private transient List<Folder> children;
    
    // ---------------------------
    // Constructors
    // ---------------------------
    
    public Folder() {
    }

    public Folder(Long primaryKey) {
        this();
        setPrimaryKey(primaryKey);
    }

    // ---------------------------
    // Identifiable implementation
    // ---------------------------
    @JsonIgnore
    @Transient
    @XmlTransient
    public Long getPrimaryKey() {
        return getFolderId();
    }

    public void setPrimaryKey(Long folderId) {
        setFolderId(folderId);
    }

    // -------------------------------
    // Getter & Setter
    // -------------------------------

    // -- [folderId] ------------------------

    @Column(name = "folder_id", nullable = false, unique = true, precision = 20)
    @GeneratedValue
    @Id
    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    // -- [siteId] ------------------------
    @NotNull
    @Column(name = "site_id", nullable = false, precision = 10, insertable = false, updatable = false)
    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    // -- [parentFolderId] ------------------------

    @Column(name = "parent_folder_id", precision = 20, insertable = false, updatable = false)
    public Long getParentFolderId() {
        return parentFolderId;
    }

    public void setParentFolderId(Long parentFolderId) {
        this.parentFolderId = parentFolderId;
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
    
    // -- [aliasName] ------------------------

    @Length(max = 200)
    @Column(name = "alias_name", nullable = true, length = 200)
    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    // -- [path] ------------------------

    @Length(max = 240)
    @Column(nullable = false, length = 240)
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    // -- [indexPageId] ------------------------

    @Column(name = "index_page_id", precision = 20, insertable = false, updatable = false)
    public Long getIndexPageId() {
        return indexPageId;
    }

    public void setIndexPageId(Long indexPageId) {
        this.indexPageId = indexPageId;
    }

    // -- [listPageId] ------------------------

    @Column(name = "list_page_id", precision = 20, insertable = false, updatable = false)
    public Long getListPageId() {
        return listPageId;
    }

    public void setListPageId(Long listPageId) {
        this.listPageId = listPageId;
    }

    // -- [detailPageId] ------------------------

    @Column(name = "detail_page_id", precision = 20, insertable = false, updatable = false)
    public Long getDetailPageId() {
        return detailPageId;
    }

    public void setDetailPageId(Long detailPageId) {
        this.detailPageId = detailPageId;
    }

    // -- [naviPageId] ------------------------
    @Column(name = "navi_page_id", precision = 20, insertable = false, updatable = false)
	public Long getNaviPageId() {
		return naviPageId;
	}
    /**
	 * @param naviPageId the naviPageId to set
	 */
	public void setNaviPageId(Long naviPageId) {
		this.naviPageId = naviPageId;
	}
    // -- [nsThread] ------------------------
    @Column(name = "ns_thread", nullable = false, precision = 20)
    public Long getNsThread() {
        return nsThread;
    }

    public void setNsThread(Long nsThread) {
        this.nsThread = nsThread;
    }
    // -- [nsLeft] ------------------------
    @Column(name = "ns_left", nullable = false, precision = 20)
    public Long getNsLeft() {
        return nsLeft;
    }

    public void setNsLeft(Long nsLeft) {
        this.nsLeft = nsLeft;
    }

    // -- [nsRight] ------------------------
    @Column(name = "ns_right", nullable = false, precision = 20)
    public Long getNsRight() {
        return nsRight;
    }

    public void setNsRight(Long nsRight) {
        this.nsRight = nsRight;
    }

    // -- [status] ------------------------
	@Column(nullable = false, precision = 10)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    // -- [sortOrder] ------------------------

    @Column(name = "sort_order", nullable = false, length = 19)
    @Temporal(TIMESTAMP)
    @DateTimeFormat(iso = ISO.DATE)
    public Date getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Date sortOrder) {
        this.sortOrder = sortOrder;
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

    // -- [description] ------------------------

    @Length(max = 254)
    @Column(length = 254)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    // -- [folderType] ------------------------
    
    @Column(name = "folder_type", nullable = false, precision = 10)
    public Integer getFolderType() {
		return folderType;
	}

	public void setFolderType(Integer folderType) {
		this.folderType = folderType;
	}
	
    // -- [miniLab] ------------------------
	
	@Column(name = "mini_lab", nullable = true, length = 10)
	public Integer getMiniLab() {
		return miniLab;
	}

	public void setMiniLab(Integer miniLab) {
		this.miniLab = miniLab;
	}

	/**
	 * @return the safeType
	 */
	@Column(name = "safe_type", nullable = true, precision = 2)
	public Integer getSafeType() {
		return safeType;
	}

	/**
	 * @param safeType the safeType to set
	 */
	public void setSafeType(Integer safeType) {
		this.safeType = safeType;
	}
	
    // -- [picId] ------------------------

	@Column(name = "pic_id", precision = 20, insertable = false, updatable = false)
    public Long getPicId() {
        return picId;
    }

    public void setPicId(Long picId) {
        this.picId = picId;
    }
    
    // -- [tagId] ------------------------

    @Column(name = "tag_id", precision = 20, insertable = false, updatable = false)
    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    // --------------------------------------------------------------------
    // Many to One support
    // --------------------------------------------------------------------
    
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // many-to-one: Folder.tagId ==> Category.categoryId
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @JoinColumn(name = "tag_id")
    @ManyToOne(cascade = PERSIST, fetch = LAZY)
    @JsonIgnore
    public Category getTag() {
        return tag;
    }
    
    /**
     * Set the tag without adding this Folder instance on the passed tag
     * If you want to preserve referential integrity we recommend to use
     * instead the corresponding adder method provided by Category
     */
    public void setTag(Category tag) {
        this.tag = tag;

        // We set the foreign key property so it can be used by Hibernate search by Example facility.
        if (tag != null) {
            setTagId(tag.getCategoryId());
        } else {
            setTagId(null);
        }
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // many-to-one: Folder.detailPageId ==> HtmlPage.pageId
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @JoinColumn(name = "detail_page_id")
    @ManyToOne(cascade = PERSIST, fetch = LAZY)
    @JsonIgnore
    public HtmlPage getDetailPage() {
        return detailPage;
    }

    /**
     * Set the detailPage without adding this Folder instance on the passed detailPage
     * If you want to preserve referential integrity we recommend to use
     * instead the corresponding adder method provided by HtmlPage
     */
    public void setDetailPage(HtmlPage detailPage) {
        this.detailPage = detailPage;

        // We set the foreign key property so it can be used by Hibernate search by Example facility.
        if (detailPage != null) {
            setDetailPageId(detailPage.getPageId());
        } else {
            setDetailPageId(null);
        }
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // many-to-one: Folder.listPageId ==> HtmlPage.pageId
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @JoinColumn(name = "list_page_id")
    @ManyToOne(cascade = PERSIST, fetch = LAZY)
    @JsonIgnore
    public HtmlPage getListPage() {
        return listPage;
    }

    /**
     * Set the listPage without adding this Folder instance on the passed listPage
     * If you want to preserve referential integrity we recommend to use
     * instead the corresponding adder method provided by HtmlPage
     */
    public void setListPage(HtmlPage listPage) {
        this.listPage = listPage;

        // We set the foreign key property so it can be used by Hibernate search by Example facility.
        if (listPage != null) {
            setListPageId(listPage.getPageId());
        } else {
            setListPageId(null);
        }
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // many-to-one: Folder.indexPageId ==> HtmlPage.pageId
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @JoinColumn(name = "index_page_id")
    @ManyToOne(cascade = PERSIST, fetch = LAZY)
    @JsonIgnore
    public HtmlPage getIndexPage() {
        return indexPage;
    }

    /**
     * Set the indexPage without adding this Folder instance on the passed indexPage
     * If you want to preserve referential integrity we recommend to use
     * instead the corresponding adder method provided by HtmlPage
     */
    public void setIndexPage(HtmlPage indexPage) {
        this.indexPage = indexPage;

        // We set the foreign key property so it can be used by Hibernate search by Example facility.
        if (indexPage != null) {
            setIndexPageId(indexPage.getPageId());
        } else {
            setIndexPageId(null);
        }
    }

    @JoinColumn(name = "navi_page_id")
    @ManyToOne(cascade = PERSIST, fetch = LAZY)
    @JsonIgnore
    public HtmlPage getNaviPage() {
        return naviPage;
    }

    /**
     * Set the naviPage without adding this Folder instance on the passed detailPage
     * If you want to preserve referential integrity we recommend to use
     * instead the corresponding adder method provided by HtmlPage
     */
    public void setNaviPage(HtmlPage naviPage) {
        this.naviPage = naviPage;

        // We set the foreign key property so it can be used by Hibernate search by Example facility.
        if (naviPage != null) {
            setNaviPageId(naviPage.getPageId());
        } else {
            setNaviPageId(null);
        }
    }
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // many-to-one: Folder.siteId ==> Site.siteId
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @JoinColumn(name = "site_id", nullable = false)
    @ManyToOne(cascade = PERSIST, fetch = LAZY)
    @JsonIgnore
    public Site getSite() {
        return site;
    }

    /**
     * Set the site without adding this Folder instance on the passed site
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
    // many-to-one: Folder.parentFolderId ==> Folder.folderId
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @JoinColumn(name = "parent_folder_id")
    @ManyToOne(cascade = PERSIST, fetch = LAZY)
    @JsonIgnore
    public Folder getParentFolder() {
        return parentFolder;
    }

    /**
     * Set the parentFolder without adding this Folder instance on the passed parentFolder
     * If you want to preserve referential integrity we recommend to use
     * instead the corresponding adder method provided by Folder
     */
    public void setParentFolder(Folder parentFolder) {
        this.parentFolder = parentFolder;

        // We set the foreign key property so it can be used by Hibernate search by Example facility.
        if (parentFolder != null) {
            setParentFolderId(parentFolder.getFolderId());
        } else {
            setParentFolderId(null);
        }
        if(null != this.parentFolder) {
        	this.parentFolder.addChild(this);
        }
    }

    public void addChild(Folder folder) {
    	if(null == this.children) {
    		this.children = new ArrayList<Folder>();
    	}
    	this.children.add(folder);
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // many-to-one: Folder.picId ==> Picture.picId
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @JoinColumn(name = "pic_id")
    @ManyToOne(cascade = PERSIST, fetch = FetchType.EAGER)
    public Picture getPic() {
        return pic;
    }

    /**
     * Set the pic without adding this Folder instance on the passed pic
     * If you want to preserve referential integrity we recommend to use
     * instead the corresponding adder method provided by Picture
     */
    public void setPic(Picture pic) {
        this.pic = pic;

        // We set the foreign key property so it can be used by Hibernate search by Example facility.
        if (pic != null) {
            setPicId(pic.getPictureId());
        } else {
            setPicId(null);
        }
    }
    
    // --------------------------------------------------------------------
    // Many to Many
    // --------------------------------------------------------------------

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // many-to-many: folders ==> tags
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Returns the tags List.
     */
    @JoinTable(name = "folder_tag", joinColumns = @JoinColumn(name = "folder_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @ManyToMany(cascade = ALL)
    @JsonIgnore
    public List<Category> getTags() {
        return tags;
    }

    /**
     * Set the tags List.
     * It is recommended to use the helper method addTag /  removeTag
     * if you want to preserve referential integrity at the object level.
     *
     * @param tags the List of Category
     */
    public void setTags(List<Category> tags) {
        this.tags = tags;
    }

    /**
	 * @return the children
	 */
    @JsonIgnore
    @Transient
	public List<Folder> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<Folder> children) {
		this.children = children;
	}

	/**
     * Helper method to add the passed tag to the tags List.
     */
    public boolean addTag(Category tag) {
        return getTags().add(tag);
    }

    /**
     * Helper method to remove the passed tag from the tags List.
     */
    public boolean removeTag(Category tag) {
        return getTags().remove(tag);
    }

    /**
     * Helper method to determine if the passed tag is present in the tags List.
     */
    public boolean containsTag(Category tag) {
        return getTags() != null && getTags().contains(tag);
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
    public boolean equals(Object folder) {
        if (this == folder) {
            return true;
        }

        if (!(folder instanceof Folder)) {
            return false;
        }

        Folder other = (Folder) folder;
        return _getUid().equals(other._getUid());
    }

    @Override
    public int hashCode() {
        return _getUid().hashCode();
    }

    private transient Object _uid;

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
     * Construct a readable string representation for this Folder instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("folder.folderId=[").append(getFolderId()).append("]\n");
        result.append("folder.siteId=[").append(getSiteId()).append("]\n");
        result.append("folder.parentFolderId=[").append(getParentFolderId()).append("]\n");
        result.append("folder.name=[").append(getName()).append("]\n");
        result.append("folder.path=[").append(getPath()).append("]\n");
        result.append("folder.indexPageId=[").append(getIndexPageId()).append("]\n");
        result.append("folder.listPageId=[").append(getListPageId()).append("]\n");
        result.append("folder.detailPageId=[").append(getDetailPageId()).append("]\n");
        result.append("folder.naviPageId=[").append(getNaviPageId()).append("]\n");
        result.append("folder.status=[").append(getStatus()).append("]\n");
        result.append("folder.sortOrder=[").append(getSortOrder()).append("]\n");
        result.append("folder.createBy=[").append(getCreateBy()).append("]\n");
        result.append("folder.createDate=[").append(getCreateDate()).append("]\n");
        result.append("folder.modDate=[").append(getModDate()).append("]\n");
        result.append("folder.modBy=[").append(getModBy()).append("]\n");
        result.append("folder.description=[").append(getDescription()).append("]\n");
        result.append("folder.picId=[").append(getPicId()).append("]\n");
        result.append("folder.tagId=[").append(getTagId()).append("]\n");
        result.append("filder.aliasName=[").append(getAliasName()).append("]\n");
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
    public Folder copy() {
        Folder folder = new Folder();
        copyTo(folder);
        return folder;
    }

    /**
     * Copy the current properties to the given object
     */
    @Override
    public void copyTo(Folder folder) {
        folder.setFolderId(getFolderId());
        folder.setSiteId(getSiteId());
        folder.setParentFolderId(getParentFolderId());
        folder.setName(getName());
        folder.setAliasName(getAliasName());
        folder.setPath(getPath());
        folder.setIndexPageId(getIndexPageId());
        folder.setListPageId(getListPageId());
        folder.setDetailPageId(getDetailPageId());
        folder.setStatus(getStatus());
        folder.setSortOrder(getSortOrder());
        folder.setCreateBy(getCreateBy());
        folder.setCreateDate(getCreateDate());
        folder.setModDate(getModDate());
        folder.setModBy(getModBy());
        folder.setDescription(getDescription());
        folder.setFolderType(getFolderType());
        folder.setPicId(getPicId());
        folder.setNsThread(this.getNsThread());
        folder.setNsLeft(this.getNsLeft());
        folder.setNsRight(this.getNsRight());
        folder.setSafeType(this.getSafeType());
        folder.setMiniLab(this.getMiniLab());
        if (getTag() != null) {
            folder.setTag(new Category(getTag().getPrimaryKey()));
        }
        if (getDetailPage() != null) {
            folder.setDetailPage(new HtmlPage(getDetailPageId()));
        }
        if (getListPage() != null) {
            folder.setListPage(new HtmlPage(getListPageId()));
        }
        if (getIndexPage() != null) {
            folder.setIndexPage(new HtmlPage(getIndexPageId()));
        }
        if (getNaviPage() != null) {
            folder.setNaviPage(new HtmlPage(getNaviPageId()));
        }
        if (getSite() != null) {
            folder.setSite(new Site(getSiteId()));
        }
        if (getParentFolder() != null) {
            folder.setParentFolder(new Folder(getParentFolderId()));
        }
        if (getPic() != null) {
            folder.setPic(getPic().copy());
        }
    }
    

	/**
	 * @return the productCount
	 */
    @Transient
    @JsonIgnore
	public String getProductCount() {
    	if(null != this.productCount) {
    		return AppStringUtils.numberToDisplaySize(this.productCount);
    	}
		return "";
	}

	/**
	 * @param productCount the productCount to set
	 */
	public void setProductCount(Long productCount) {
		this.productCount = productCount;
	}

	@Transient
	@JsonIgnore
	public Long getId() {
		return this.getPrimaryKey();
	}
	
	public HtmlPage getHtmlPageByType(Integer type) {
		switch (type) {
			case 0:
				return this.indexPage;
			case 1:
				return this.listPage;
			case 2:
				return this.detailPage;
			case 3:
				return this.naviPage;
		}
		return null;
	}

	/**
	 * 系统频道名称。使用时将其转换为小写即可。
	 * Home：首页
	 * Tag：tag频道，显示频道关联的tag；
	 * Search：搜索频道；
	 * More：更多，用来显示更多评论；更多章节；阅读历史记录
	 * Advertise：显示广告。More的子频道
	 * Chapter：更多章节。More的子频道
	 * History：阅读历史。More的子频道
	 * @author mengw
	 *
	 */
	public enum SystemFolderEnum {
		Home, Tag, Search, More, Advertise, Chapter, History
	}

	@Transient
	@JsonIgnore
	public boolean isEnable() {
		if(null != this.getStatus()) {
			FolderStatusEnum sts = FolderStatusEnum.getInstance(this.getStatus());
			if(null != sts && sts == FolderStatusEnum.Enable) {
				return true;
			}
		}
		return false;
	}
	
	public enum FolderStatusEnum {
    	Enable, Disable;
    	public static FolderStatusEnum getInstance(Integer value) {
    		if(null != value) {
    			FolderStatusEnum[] types = FolderStatusEnum.values();
	    		for(FolderStatusEnum type : types) {
	    			if(type.ordinal() == value.intValue()) {
	    				return type;
	    			}
	    		}
    		}
    		return null;
    	}
	}
	public enum SafeTypeEnum {
		Unknown, Safe, Lowrisk, Midrisk, Virus;
		public static SafeTypeEnum getInstance(Integer value) {
    		if(null != value) {
    			SafeTypeEnum[] types = SafeTypeEnum.values();
	    		for(SafeTypeEnum type : types) {
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
			case Unknown:
				return "未知";
			case Safe:
				return "安全";
			case Lowrisk:
				return "低风险";
			case Midrisk:
				return "中风险";
			case Virus:
				return "病毒";
			}
			return this.name().toLowerCase();
		}
	}
}