package org.visitor.appportal.domain;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Table(name = "recommend_rule")
public class RecommendRule implements Serializable, Copyable<RecommendRule> {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(RecommendRule.class);
    
    public final static Integer TYPE_SYSTEM = 0;
    public final static Integer TYPE_FOLDER = 1;
    public final static Integer TYPE_PRODUCT = 2;

    // Raw attributes
    private Long ruleId; // pk
    private Long folderId;
    private Long productId;
    private Integer type;
    private Integer behaviorId;
    private Integer behaviorSeq;
    private Integer behaviorNum;
    private Integer similarSeq;
    private Integer similarNum;
    private Integer storageSeq;
    private Integer storageNum;
    private Integer manualSeq;
    private Integer manualNum;
    private String manualIds;
    private String disSort;
    private Integer siteId;

	private transient String modBy; // not null
	private Date modDate; // not null
	private transient String createBy; // not null
	private Date createDate; // not null
	
    private transient ProductList product; // not null (productId)
    private transient Folder folder; // not null (folderId)
    private transient Site site;

    // ---------------------------
    // Constructors
    // ---------------------------

    public RecommendRule() {
    }

    public RecommendRule(Long primaryKey) {
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
        return getRuleId();
    }

    public void setPrimaryKey(Long ruleId) {
    	setRuleId(ruleId);
    }

    // -------------------------------
    // Getter & Setter
    // -------------------------------

    // -- [ruleId] ------------------------

    @Column(name = "rule_id", nullable = false, unique = true, precision = 20)
    @GeneratedValue
    @Id
    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }
    // -- [folderId] ------------------------

    @Column(name = "folder_id", precision = 20)
    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }
    
    // -- [productId] ------------------------

    @Column(name = "product_id", precision = 20)
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    // -- [typeId] ------------------------
    @Column(name = "type", precision = 10)
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }


    // -- [behaviorId] ------------------------

    @Column(name = "behavior_id", precision = 10)
    public Integer getBehaviorId() {
        return behaviorId;
    }

    public void setBehaviorId(Integer behaviorId) {
        this.behaviorId = behaviorId;
    }

    // -- [behaviorSeq] ------------------------

    @Column(name = "behavior_seq", precision = 10)
    public Integer getBehaviorSeq() {
        return behaviorSeq;
    }

    public void setBehaviorSeq(Integer behaviorSeq) {
        this.behaviorSeq = behaviorSeq;
    }


    // -- [behaviorNum] ------------------------
    @Column(name = "behavior_num", precision = 10)
    public Integer getBehaviorNum() {
        return behaviorNum;
    }

    public void setBehaviorNum(Integer behaviorNum) {
        this.behaviorNum = behaviorNum;
    }

    // -- [similarSeq] ------------------------
    @Column(name = "similar_seq", precision = 10)
    public Integer getSimilarSeq() {
        return similarSeq;
    }

    public void setSimilarSeq(Integer similarSeq) {
        this.similarSeq = similarSeq;
    }

    // -- [similarNum] ------------------------
    @Column(name = "similar_num", precision = 10)
    public Integer getSimilarNum() {
        return similarNum;
    }

    public void setSimilarNum(Integer similarNum) {
        this.similarNum = similarNum;
    }

    // -- [storageSeq] ------------------------
    @Column(name = "storage_seq", precision = 10)
    public Integer getStorageSeq() {
        return storageSeq;
    }

    public void setStorageSeq(Integer storageSeq) {
        this.storageSeq = storageSeq;
    }

    // -- [storageNum] ------------------------
    @Column(name = "storage_num", precision = 10)
    public Integer getStorageNum() {
        return storageNum;
    }

    public void setStorageNum(Integer storageNum) {
        this.storageNum = storageNum;
    }
    
    // -- [manualSeq] ------------------------
    @Column(name = "manual_seq", precision = 10)
    public Integer getManualSeq() {
        return manualSeq;
    }

    public void setManualSeq(Integer manualSeq) {
        this.manualSeq = manualSeq;
    }
    
    // -- [manualNum] ------------------------
    @Column(name = "manual_num", precision = 10)
    public Integer getManualNum() {
        return manualNum;
    }

    public void setManualNum(Integer manualNum) {
        this.manualNum = manualNum;
    }

    // -- [manualIds] ------------------------
    @Length(max = 200)
    @Column(name = "manual_ids", length = 200)
    public String getManualIds() {
        return manualIds;
    }

    public void setManualIds(String manualIds) {
        this.manualIds = manualIds;
    }
    
    // -- [disSort] ------------------------
    @Length(max = 20)
    @Column(name = "dis_sort", length = 20)
    public String getDisSort() {
        return disSort;
    }

    public void setDisSort(String disSort) {
        this.disSort = disSort;
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
	
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // one-to-one: RecommendRule.productId ==> ProductList.productId
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @JoinColumn(name = "product_id",unique = true, nullable = true, insertable = false, updatable = false)
    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JsonIgnore
    public ProductList getProduct() {
        return product;
    }

    /**
     * Set the product without adding this ProductSiteFolder instance on the passed product
     * If you want to preserve referential integrity we recommend to use
     * instead the corresponding adder method provided by ProductList
     */
    public void setProduct(ProductList product) {
        this.product = product;

        // We set the foreign key property so it can be used by Hibernate search by Example facility.
        if (product != null) {
            setProductId(product.getProductId());
        } // when null, we do not propagate it to the pk.
    }
    
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // one-to-one: RecommendRule.folderId ==> Folder.folderId
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @JoinColumn(name = "folder_id",unique = true, nullable = true, insertable = false, updatable = false)
    @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JsonIgnore
    public Folder getFolder() {
        return folder;
    }

    /**
     * Set the folder without adding this ProductSiteFolder instance on the passed folder
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
    public boolean equals(Object recommendRule) {
        if (this == recommendRule) {
            return true;
        }

        if (!(recommendRule instanceof RecommendRule)) {
            return false;
        }

        RecommendRule other = (RecommendRule) recommendRule;
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
                logger.warn("DEVELOPER: hashCode has changed!."
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
     * Construct a readable string representation for this RecommendRule instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("recommendRule.ruleId=[").append(getRuleId()).append("]\n");
        result.append("recommendRule.folderId=[").append(getFolderId()).append("]\n");
        result.append("recommendRule.productId=[").append(getProductId()).append("]\n");
        result.append("recommendRule.typeId=[").append(getType()).append("]\n");
        result.append("recommendRule.behaviorId=[").append(getBehaviorId()).append("]\n");
        result.append("recommendRule.behaviorSeq=[").append(getBehaviorSeq()).append("]\n");
        result.append("recommendRule.behaviorNum=[").append(getBehaviorNum()).append("]\n");
        result.append("recommendRule.similarSeq=[").append(getSimilarSeq()).append("]\n");
        result.append("recommendRule.similarNum=[").append(getSimilarNum()).append("]\n");
        result.append("recommendRule.storageSeq=[").append(getStorageSeq()).append("]\n");
        result.append("recommendRule.storageNum=[").append(getStorageNum()).append("]\n");
        result.append("recommendRule.manualSeq=[").append(getManualSeq()).append("]\n");
        result.append("recommendRule.manualIds=[").append(getManualIds()).append("]\n");
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
    public RecommendRule copy() {
        RecommendRule recommendRule = new RecommendRule();
        copyTo(recommendRule);
        return recommendRule;
    }

    /**
     * Copy the current properties to the given object
     */
    @Override
    public void copyTo(RecommendRule recommendRule) {
    	recommendRule.setRuleId(getRuleId());
    	recommendRule.setFolderId(getFolderId());
    	recommendRule.setProductId(getProductId());
    	recommendRule.setType(getType());
    	recommendRule.setBehaviorId(getBehaviorId());
    	recommendRule.setBehaviorSeq(getBehaviorSeq());
    	recommendRule.setBehaviorNum(getBehaviorNum());
    	recommendRule.setSimilarSeq(getSimilarSeq());
    	recommendRule.setSimilarNum(getSimilarNum());
    	recommendRule.setStorageSeq(getStorageSeq());
    	recommendRule.setStorageNum(getStorageNum());
    	recommendRule.setManualSeq(getManualSeq());
    	recommendRule.setManualNum(getManualNum());
    	recommendRule.setManualIds(getManualIds());
    }

    public enum RecommendRuleTypeEnum {
    	Behavior(0), Similar(1), Storage(2), Manual(3);
    	private Integer value;
    	private String displayName;
    	private String suffix;
    	private RecommendRuleTypeEnum(Integer value) {
    		this.value = value;
    		switch(value) {
    		case 0:
    			this.displayName = "下载推荐";
    			this.suffix = "A";
    			break;
    		case 1:
    			this.displayName = "相似产品";
    			this.suffix = "B";
    			break;
    		case 2:
    			this.displayName = "推荐库";
    			this.suffix = "C";
    			break;
    		case 3:
    			this.displayName = "固定产品";
    			this.suffix = "D";
    			break;    			
    		}
    	}
    	public static RecommendRuleTypeEnum getInstance(Integer value) {
    		if(null != value) {
    			RecommendRuleTypeEnum[] types = RecommendRuleTypeEnum.values();
	    		for(RecommendRuleTypeEnum type : types) {
	    			if(type.getValue().intValue() == value.intValue()) {
	    				return type;
	    			}
	    		}
    		}
    		return null;
    	}
    	
    	public static RecommendRuleTypeEnum getInstance(String suffix) {
    		if(null != suffix) {
    			RecommendRuleTypeEnum[] types = RecommendRuleTypeEnum.values();
	    		for(RecommendRuleTypeEnum type : types) {
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
    
    @JsonIgnore
    public String formatDisSort(){
    	StringBuffer sort = new StringBuffer();
    	Map<Integer, String> m = new HashMap<Integer, String>();
    	List<Integer> ls = new ArrayList<Integer>();
    	if (this.getBehaviorNum().intValue() >= 0) {
    		m.put(this.getBehaviorSeq(), RecommendRuleTypeEnum.Behavior.getSuffix());
    		ls.add(this.getBehaviorSeq());
    	}
    	if (this.getSimilarNum().intValue() >= 0) {
    		m.put(this.getSimilarSeq(), RecommendRuleTypeEnum.Similar.getSuffix());
    		ls.add(this.getSimilarSeq());
    	}
    	if (this.getStorageNum().intValue() >= 0) {
    		m.put(this.getStorageSeq(), RecommendRuleTypeEnum.Storage.getSuffix());
    		ls.add(this.getStorageSeq());
    	}
    	if (this.getManualNum().intValue() >= 0) {
    		m.put(this.getManualSeq(), RecommendRuleTypeEnum.Manual.getSuffix());
    		ls.add(this.getManualSeq());
    	}
    	
    	Collections.sort(ls);
    	
    	for (Integer seq : ls) {
    		sort.append(m.get(seq).toString());
    	}
		return sort.toString();
    }
    
}