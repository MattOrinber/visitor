package org.visitor.appportal.domain;

import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Table(name = "recommend_rule_across")
public class RecommendRuleAcross implements Serializable, Copyable<RecommendRuleAcross> {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(RecommendRuleAcross.class);
    
    public final static Integer TYPE_SYSTEM = 0;
    public final static Integer TYPE_FOLDER = 1;
    public final static Integer TYPE_PRODUCT = 2;

    // Raw attributes
    private Long ruleId; // pk
    private Integer serviceId;
    private String serviceName;
    private Integer serviceSiteId;
    private String serviceSiteName;
    private Long serviceFolderId;
    private Long serviceProductId;
    private String name;
    private Integer type;
    private Integer behaviorSeq;
    private Integer behaviorNum;
    private Integer storageSeq;
    private Integer storageNum;
    private Integer manualSeq;
    private Integer manualNum;
    private String manualIds;

    private Long folderId;
    private Integer folderNum;
    private Integer folderSeq;
    private Long containerId;
    private Integer containerNum;
    private Integer containerSeq;
    private String disSort;

	private transient String modBy; // not null
	private Date modDate; // not null
	private transient String createBy; // not null
	private Date createDate; // not null

    // ---------------------------
    // Constructors
    // ---------------------------

    public RecommendRuleAcross() {
    }

    public RecommendRuleAcross(Long primaryKey) {
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
    
    // -- [serviceId] ------------------------

    @Column(name = "service_id", precision = 20)
    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }
    
    // -- [serviceName] ------------------------
    
    @Length(max = 200)
    @Column(name = "service_name", length = 200)
    @JsonIgnore
    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    
    // -- [serviceSiteId] ------------------------

    @Column(name = "service_site_id", precision = 20)
    public Integer getServiceSiteId() {
        return serviceSiteId;
    }

    public void setServiceSiteId(Integer serviceSiteId) {
        this.serviceSiteId = serviceSiteId;
    }
    
    // -- [serviceSiteName] ------------------------
    
    @Length(max = 200)
    @Column(name = "service_site_name", length = 200)
    @JsonIgnore
    public String getServiceSiteName() {
        return serviceSiteName;
    }

    public void setServiceSiteName(String serviceSiteName) {
        this.serviceSiteName = serviceSiteName;
    }
    
    // -- [serviceFolderId] ------------------------

    @Column(name = "service_folder_id", precision = 20)
    public Long getServiceFolderId() {
        return serviceFolderId;
    }

    public void setServiceFolderId(Long serviceFolderId) {
        this.serviceFolderId = serviceFolderId;
    }
    
    // -- [serviceProductId] ------------------------

    @Column(name = "service_product_id", precision = 20)
    public Long getServiceProductId() {
        return serviceProductId;
    }

    public void setServiceProductId(Long serviceProductId) {
        this.serviceProductId = serviceProductId;
    }
    
    // -- [name] ------------------------
    
    @Length(max = 200)
    @Column(name = "name", length = 200)
    @JsonIgnore
    public String getName() {
        return name;
    }

    public void setName(String Name) {
        this.name = Name;
    }

    // -- [typeId] ------------------------
    @NotNull
    @Column(name = "type", precision = 10)
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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
    

    // -- [folderId] ------------------------

    @Column(name = "folder_id", precision = 20)
    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    // -- [folderNum] ------------------------
    @Column(name = "folder_num", precision = 10)
    public Integer getFolderNum() {
        return folderNum;
    }

    public void setFolderNum(Integer folderNum) {
        this.folderNum = folderNum;
    }
    
    // -- [folderSeq] ------------------------
    @Column(name = "folder_seq", precision = 10)
    public Integer getFolderSeq() {
        return folderSeq;
    }

    public void setFolderSeq(Integer folderSeq) {
        this.folderSeq = folderSeq;
    }
    
    // -- [containerId] ------------------------

    @Column(name = "container_id", precision = 10)
    public Long getContainerId() {
        return containerId;
    }

    public void setContainerId(Long containerId) {
        this.containerId = containerId;
    }

    // -- [containerNum] ------------------------
    @Column(name = "container_num", precision = 10)
    public Integer getContainerNum() {
        return containerNum;
    }

    public void setContainerNum(Integer containerNum) {
        this.containerNum = containerNum;
    }
    
    // -- [containerSeq] ------------------------
    @Column(name = "container_seq", precision = 10)
    public Integer getContainerSeq() {
        return containerSeq;
    }

    public void setContainerSeq(Integer containerSeq) {
        this.containerSeq = containerSeq;
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

	// -- [modBy] ------------------------
	@Length(max = 60)
	@Column(name = "mod_by", nullable = false, length = 60)
	@JsonIgnore
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
	@JsonIgnore
	public Date getModDate() {
		return modDate;
	}

	public void setModDate(Date modDate) {
		this.modDate = modDate;
	}

	// -- [createBy] ------------------------
	@Length(max = 60)
	@Column(name = "create_by", nullable = false, length = 60)
	@JsonIgnore
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

        if (!(recommendRule instanceof RecommendRuleAcross)) {
            return false;
        }

        RecommendRuleAcross other = (RecommendRuleAcross) recommendRule;
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
        result.append("recommendRule.serviceId=[").append(getServiceId()).append("]\n");
        result.append("recommendRule.serviceSiteId=[").append(getServiceSiteId()).append("]\n");
        result.append("recommendRule.serviceFolderId=[").append(getServiceFolderId()).append("]\n");
        result.append("recommendRule.serviceProductId=[").append(getServiceProductId()).append("]\n");
        result.append("recommendRule.typeId=[").append(getType()).append("]\n");
        result.append("recommendRule.behaviorSeq=[").append(getBehaviorSeq()).append("]\n");
        result.append("recommendRule.behaviorNum=[").append(getBehaviorNum()).append("]\n");
        result.append("recommendRule.storageSeq=[").append(getStorageSeq()).append("]\n");
        result.append("recommendRule.storageNum=[").append(getStorageNum()).append("]\n");
        result.append("recommendRule.manualSeq=[").append(getManualSeq()).append("]\n");
        result.append("recommendRule.manualNum=[").append(getManualNum()).append("]\n");
        result.append("recommendRule.manualIds=[").append(getManualIds()).append("]\n");
        result.append("recommendRule.folderId=[").append(getFolderId()).append("]\n");
        result.append("recommendRule.folderSeq=[").append(getFolderSeq()).append("]\n");
        result.append("recommendRule.folderNum=[").append(getFolderNum()).append("]\n");
        result.append("recommendRule.containerId=[").append(getContainerId()).append("]\n");
        result.append("recommendRule.containerSeq=[").append(getContainerSeq()).append("]\n");
        result.append("recommendRule.containerNum=[").append(getContainerNum()).append("]\n");
        result.append("recommendRule.disSort=[").append(getDisSort()).append("]\n");
        result.append("recommendRule.createBy=[").append(getCreateBy()).append("]\n");
        result.append("recommendRule.createDate=[").append(getCreateDate()).append("]\n");
        result.append("recommendRule.modBy=[").append(getModBy()).append("]\n");
        result.append("recommendRule.modDate=[").append(getModDate()).append("]\n");
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
    public RecommendRuleAcross copy() {
        RecommendRuleAcross recommendRule = new RecommendRuleAcross();
        copyTo(recommendRule);
        return recommendRule;
    }

    /**
     * Copy the current properties to the given object
     */
    @Override
    public void copyTo(RecommendRuleAcross recommendRule) {
    	recommendRule.setRuleId(getRuleId());
    	recommendRule.setServiceId(getServiceId());
    	recommendRule.setServiceName(getServiceName());
    	recommendRule.setServiceSiteId(getServiceSiteId());
    	recommendRule.setServiceSiteName(getServiceSiteName());
    	recommendRule.setServiceFolderId(getServiceFolderId());
    	recommendRule.setServiceProductId(getServiceProductId());
    	recommendRule.setName(getName());
    	recommendRule.setType(getType());
    	recommendRule.setBehaviorSeq(getBehaviorSeq());
    	recommendRule.setBehaviorNum(getBehaviorNum());
    	recommendRule.setStorageSeq(getStorageSeq());
    	recommendRule.setStorageNum(getStorageNum());
    	recommendRule.setManualSeq(getManualSeq());
    	recommendRule.setManualNum(getManualNum());
    	recommendRule.setManualIds(getManualIds());
    	recommendRule.setFolderId(getFolderId());
    	recommendRule.setFolderNum(getFolderNum());
    	recommendRule.setFolderSeq(getFolderSeq());
    	recommendRule.setContainerId(getContainerId());
    	recommendRule.setContainerNum(getContainerNum());
    	recommendRule.setContainerSeq(getContainerSeq());
    	recommendRule.setDisSort(getDisSort());
    }

    public enum RecommendRuleAcrossTypeEnum {
    	Behavior(0), Storage(1), Manual(2), Folder(3), Container(4);
    	private Integer value;
    	private String displayName;
    	private String suffix;
    	private RecommendRuleAcrossTypeEnum(Integer value) {
    		this.value = value;
    		switch(value) {
    		case 0:
    			this.displayName = "用户行为";
    			this.suffix = "A";
    			break;
    		case 1:
    			this.displayName = "推荐库";
    			this.suffix = "B";
    			break;
    		case 2:
    			this.displayName = "固定产品";
    			this.suffix = "C";
    			break;
    		case 3:
    			this.displayName = "频道产品";
    			this.suffix = "D";
    			break;
    		case 4:
    			this.displayName = "容器产品";
    			this.suffix = "E";
    			break;
    		}
    	}
    	public static RecommendRuleAcrossTypeEnum getInstance(Integer value) {
    		if(null != value) {
    			RecommendRuleAcrossTypeEnum[] types = RecommendRuleAcrossTypeEnum.values();
	    		for(RecommendRuleAcrossTypeEnum type : types) {
	    			if(type.getValue().intValue() == value.intValue()) {
	    				return type;
	    			}
	    		}
    		}
    		return null;
    	}
    	
    	public static RecommendRuleAcrossTypeEnum getInstance(String suffix) {
    		if(null != suffix) {
    			RecommendRuleAcrossTypeEnum[] types = RecommendRuleAcrossTypeEnum.values();
	    		for(RecommendRuleAcrossTypeEnum type : types) {
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
    	if (this.getBehaviorNum().intValue() > 0) {
    		m.put(this.getBehaviorSeq(), RecommendRuleAcrossTypeEnum.Behavior.getSuffix());
    		ls.add(this.getBehaviorSeq());
    	}
    	if (this.getStorageNum().intValue() > 0) {
    		m.put(this.getStorageSeq(), RecommendRuleAcrossTypeEnum.Storage.getSuffix());
    		ls.add(this.getStorageSeq());
    	}
    	if (this.getManualNum().intValue() > 0) {
    		m.put(this.getManualSeq(), RecommendRuleAcrossTypeEnum.Manual.getSuffix());
    		ls.add(this.getManualSeq());
    	}
    	if (this.getFolderNum().intValue() > 0) {
    		m.put(this.getFolderSeq(), RecommendRuleAcrossTypeEnum.Folder.getSuffix());
    		ls.add(this.getFolderSeq());
    	}
    	if (this.getContainerNum().intValue() > 0) {
    		m.put(this.getContainerSeq(), RecommendRuleAcrossTypeEnum.Container.getSuffix());
    		ls.add(this.getContainerSeq());
    	}
    	
    	Collections.sort(ls);
    	
    	for (Integer seq : ls) {
    		sort.append(m.get(seq).toString());
    	}
		return sort.toString();
    }
}