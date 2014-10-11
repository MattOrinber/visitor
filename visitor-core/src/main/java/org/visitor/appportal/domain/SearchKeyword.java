package org.visitor.appportal.domain;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "search_keyword")
public class SearchKeyword implements Serializable, Copyable<SearchKeyword> {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(SearchKeyword.class);

    // Raw attributes
    private Long keywordId; // pk
    private String name; // not null
    private String color;
    private String size;
    private Integer status; // not null
    private Long sortOrder;
    private Integer siteId;
    private String createBy; // not null
    private Date createDate; // not null
    private String modBy; // not null
    private Date modDate; // not null
    
    //fields added newly
    private Integer wordKind;
    private Integer isHot;
    private String idList;
    private Long platformId;
    
    private Site site; // not null (siteId)
    
    private Integer type;
    private String specUrl;
    //private Category plat; // not null (platId)

    // ---------------------------
    // Constructors
    // ---------------------------

    public SearchKeyword() {
    }

    public SearchKeyword(Long primaryKey) {
        setPrimaryKey(primaryKey);
    }

    // ---------------------------
    // Identifiable implementation
    // ---------------------------

    @Transient
    @XmlTransient
    public Long getPrimaryKey() {
        return getKeywordId();
    }

    public void setPrimaryKey(Long keywordId) {
        setKeywordId(keywordId);
    }

    // -------------------------------
    // Getter & Setter
    // -------------------------------

    // -- [keywordId] ------------------------

    @Column(name = "keyword_id", nullable = false, unique = true, precision = 20)
    @GeneratedValue
    @Id
    public Long getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(Long keywordId) {
        this.keywordId = keywordId;
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

    // -- [color] ------------------------
    @Length(max = 10)
    @Column(length = 10)
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    // -- [size] ------------------------
    @Length(max = 10)
    @Column(length = 10)
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    // -- [status] ------------------------

    @NotNull
    @Column(nullable = false, precision = 10)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    
	// -- [sortOrder] ------------------------
	@Column(name = "sort_order")
	public Long getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Long sortOrder) {
		this.sortOrder = sortOrder;
	}
	
    // -- [siteId] ------------------------
    @Column(name = "site_id", nullable = false, precision = 10, insertable = false, updatable = false)
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

    // -- [modBy] ------------------------

    @Length(max = 60)
    @Column(name = "mod_by", nullable = false, length = 60)
    public String getModBy() {
        return modBy;
    }

    public void setModBy(String modBy) {
        this.modBy = modBy;
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
        
    /**
	 * @return the wordKind
	 */
    @Column(name = "word_kind", nullable = false, length = 1)
	public Integer getWordKind() {
		return wordKind;
	}

	/**
	 * @param wordKind the wordKind to set
	 */
	public void setWordKind(Integer wordKind) {
		this.wordKind = wordKind;
	}

	/**
	 * @return the isHot
	 */
    @Column(name = "is_hot", nullable = false, length = 1)
	public Integer getIsHot() {
		return isHot;
	}

	/**
	 * @param isHot the isHot to set
	 */
	public void setIsHot(Integer isHot) {
		this.isHot = isHot;
	}

	/**
	 * @return the idList
	 */
    @Column(name = "id_list", nullable = true, length = 1000)
	public String getIdList() {
		return idList;
	}

	/**
	 * @param idList the idList to set
	 */
	public void setIdList(String idList) {
		this.idList = idList;
	}

	
	/**
	 * @return the platformId
	 */
    @Column(name = "platform_id", nullable = true, precision = 20)
	public Long getPlatformId() {
		return platformId;
	}

	/**
	 * @param platformId the platformId to set
	 */
	public void setPlatformId(Long platformId) {
		this.platformId = platformId;
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
    
/*    public void setPlat(Category plat) {
        this.plat = plat;

        // We set the foreign key property so it can be used by Hibernate search by Example facility.
        if (plat != null) {
        	this.setPlatformId(plat.getCategoryId());
        } else {
            this.setPlatformId(null);
        }
    }*/

/*	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // many-to-one: Folder.siteId ==> Site.siteId
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @JoinColumn(name = "platform_id", referencedColumnName="category_id",nullable = true)
    @ManyToOne(cascade = PERSIST, fetch = LAZY)
    @JsonIgnore
    public Category getPlat() {
        return plat;
    }*/
    
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
    
	@Column(name = "type", nullable = true, precision = 2)
    public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
    /**
	 * @return the specUrl
	 */
    @Column(name = "spec_url", length = 512)
	public String getSpecUrl() {
		return specUrl;
	}

	/**
	 * @param specUrl the specUrl to set
	 */
	public void setSpecUrl(String specUrl) {
		this.specUrl = specUrl;
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
    public boolean equals(Object searchKeyword) {
        if (this == searchKeyword) {
            return true;
        }

        if (!(searchKeyword instanceof SearchKeyword)) {
            return false;
        }

        SearchKeyword other = (SearchKeyword) searchKeyword;
        return _getUid().equals(other._getUid());
    }

    @Override
    public int hashCode() {
        return _getUid().hashCode();
    }

    private Object _uid;

    private Object _getUid() {
        if (_uid == null) {
            if (null != this.getKeywordId()) {
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
    /* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SearchKeyword [keywordId=" + keywordId + ", name=" + name
				+ ", color=" + color + ", size=" + size + ", status=" + status
				+ ", sortOrder=" + sortOrder + ", siteId=" + siteId
				+ ", createBy=" + createBy + ", createDate=" + createDate
				+ ", modBy=" + modBy + ", modDate=" + modDate + ", wordKind="
				+ wordKind + ", isHot=" + isHot + ", idList=" + idList
				+ ", site=" + site + ", type=" + type + "]";
	}

    // -----------------------------------------
    // Copyable Implementation
    // (Support for REST web layer)
    // -----------------------------------------

    /**
     * Return a copy of the current object
     */
    @Override
    public SearchKeyword copy() {
        SearchKeyword searchKeyword = new SearchKeyword();
        copyTo(searchKeyword);
        return searchKeyword;
    }

    /**
     * Copy the current properties to the given object
     */
    @Override
    public void copyTo(SearchKeyword searchKeyword) {
        searchKeyword.setKeywordId(getKeywordId());
        searchKeyword.setName(getName());
        searchKeyword.setColor(getColor());
        searchKeyword.setSize(getSize());
        searchKeyword.setStatus(getStatus());
        searchKeyword.setSortOrder(getSortOrder());
        searchKeyword.setCreateBy(getCreateBy());
        searchKeyword.setCreateDate(getCreateDate());
        searchKeyword.setModBy(getModBy());
        searchKeyword.setModDate(getModDate());
        searchKeyword.setType(getType());
    }
    
    public enum StatusEnum {
    	Enable, Disable
    }
    
    /**
     * 搜索词类型，值1，2，3分别代表产品，频道和广告
     * @author mengw
     *
     */
    public enum WordKindEnum {
    	PRD(1),FLD(2),ADV(3);
    	private Integer id;
    	
    	WordKindEnum(int id){
    		this.id = id;
    	}
    	
    	public Integer getValue(){
    		return this.id.intValue();
    	}

		public static WordKindEnum getInstance(Integer value) {
    		if(null != value) {
    			WordKindEnum[] types = WordKindEnum.values();
	    		for(WordKindEnum type : types) {
	    			if(type.id == value.intValue()) {
	    				return type;
	    			}
	    		}
    		}
    		return null;
    	}
    }
   
    /**
     * 热词类型 NORMAL->普通  	HOT->热词
     * @author mengw
     *
     */
    public enum IsHotEnum {
    	NORMAL, HOT
    }
    
    public enum TypeEnum {
		Downloadable, PageGame;
	}
}