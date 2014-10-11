package org.visitor.appportal.domain;

import static javax.persistence.CascadeType.ALL;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Table(name = "product_list")
public class ProductList implements Serializable, Copyable<ProductList> {
	/**
	 * 产品状态:上架
	 */
	public static final int ENABLE=0;
	/**
	 * 产品状态:下架
	 */
	public static final int DISNABLE=1;
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(ProductList.class);

	// Raw attributes
	private Long productId; // pk
	private String name; // not null
	private Long billingTypeId; // not null
	private Double price; // not null
	private String description;
	private String tagLine;//推广语
	private Date publishDate;
	private transient String publishBy;
	private Long categoryId; // not null
	private String label;
	private String productVersion;
	private Date modDate; // not null
	private transient String modBy; // not null
	private transient String createBy; // not null
	private Date createDate; // not null
	private Integer downStatus=ENABLE; // not null
	private String downReason;
	private String importBy; // not null
	private Long operationModelId; // not null
	private Long cooperationModelId; // not null
	private String developer;
	private Long operatorId;
	private Long sourceId; // not null
	private String productSourceId; //
	private String languageType;
	private String starLevel;
	private Integer miniLab;
    private Long merchantId; // not null

	private Integer recommendStorage;
	private Integer safeType; //下载文件安全类型
	private Integer productType;//产品类型
	
	private transient Category category; // not null
	private transient Category billingType; // not null
	private transient Category operationModel; // not null
	private transient Category cooperationModel; // not null
	private transient Category operator; // not null
	private transient Category productSource; // not null
    private transient Category merchant; // not null
    //many to many
    private transient List<Label> labels; //产片所属标签列表
	private transient ProductState productState;
	
    // Many to many
    private transient List<Folder> folders = new ArrayList<Folder>();
    
    
    private String manualDescription;//手动维护的描述
    private String moreinfo;//更多信息，预留
    
	@JoinColumn(name = "category_id", nullable = false)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonIgnore
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		if(null != category) {
			this.category = category.copy();
		} else {
			this.category = null;
		}
	}

	@JoinColumn(name = "billing_type_id", nullable = false)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonIgnore
	public Category getBillingType() {
		return billingType;
	}

	public void setBillingType(Category billingType) {
		if(null != billingType) {
			this.billingType = billingType.copy();
		} else {
			this.billingType = null;
		}
	}

	@JoinColumn(name = "operation_model_id", nullable = false)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonIgnore
	public Category getOperationModel() {
		return operationModel;
	}

	public void setOperationModel(Category operationModel) {
		if(null != operationModel) {
			this.operationModel = operationModel.copy();
		} else {
			this.operationModel = null;
		}
	}

	@JoinColumn(name = "cooperation_model_id", nullable = false)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonIgnore
	public Category getCooperationModel() {
		return cooperationModel;
	}

	public void setCooperationModel(Category cooperationModel) {
		if(null != cooperationModel) {
			this.cooperationModel = cooperationModel.copy();
		} else {
			this.cooperationModel = null;
		}
	}
	
	@JoinColumn(name = "merchant_id", nullable = true)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonIgnore
	public Category getMerchant() {
		return merchant;
	}

	public void setMerchant(Category merchant) {
		if(null != merchant) {
			this.merchant = merchant.copy();
		} else {
			this.merchant = null;
		}
	}
	
	@JoinColumn(name = "operator_id", nullable = true)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonIgnore
	public Category getOperator() {
		return operator;
	}

	public void setOperator(Category operator) {
		if(null != operator) {
			this.operator = operator.copy();
			this.setOperatorId(operator.getCategoryId());
		} else {
			this.operator = null;
		}
	}
	
	@JoinColumn(name = "product_source", nullable = false)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonIgnore
	public Category getProductSource() {
		return productSource;
	}

	public void setProductSource(Category productSource) {
		if(null != productSource) {
			this.productSource = productSource.copy();
			this.setSourceId(productSource.getCategoryId());
		} else {
			this.productSource = null;
		}
	}

	// ---------------------------
	// Constructors
	// ---------------------------

	public ProductList() {
	}

	public ProductList(Long primaryKey) {
		this();
		setPrimaryKey(primaryKey);
	}

	// ---------------------------
	// Identifiable implementation
	// ---------------------------

	@Transient
	@XmlTransient
	public Long getPrimaryKey() {
		return getProductId();
	}

	public void setPrimaryKey(Long productId) {
		setProductId(productId);
	}

	// -------------------------------
	// Getter & Setter
	// -------------------------------

	// -- [productId] ------------------------

	@Column(name = "product_id", nullable = false, unique = true, precision = 20)
	@GeneratedValue
	@Id
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}
	
	// -- [name] ------------------------

	@NotBlank
	@Length(max = 200)
	@Column(nullable = false, length = 200)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// -- [billingType] ------------------------

	@NotNull
	@Column(name = "billing_type_id", nullable = false, precision = 10, insertable = false, updatable = false)
	public Long getBillingTypeId() {
		return billingTypeId;
	}

	public void setBillingTypeId(Long billingTypeId) {
		this.billingTypeId = billingTypeId;
		if (this.billingType == null) {
			this.billingType = new Category();
		}
		billingType.setCategoryId(billingTypeId);
	}

	// -- [price] ------------------------

	@NotNull
	@Column(nullable = false, precision = 22)
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}

	// -- [description] ------------------------

	@Length(max = 2048)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	// -- [tagLine] ------------------------

	@Length(max = 255)
	@Column(name = "tag_line")
	public String getTagLine() {
		return tagLine;
	}

	public void setTagLine(String tagLine) {
		this.tagLine = tagLine;
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

	// -- [categoryId] ------------------------

	@NotNull
	@Column(name = "category_id", nullable = false, precision = 20, insertable = false, updatable = false)
	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
		if (this.category == null) {
			this.category = new Category();
		}
		category.setCategoryId(categoryId);
	}

	// -- [label] ------------------------

	@Length(max = 200)
	@Column(length = 200)
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	// -- [productVersion] ------------------------

	@Length(max = 100)
	@Column(name = "product_version", length = 100)
	public String getProductVersion() {
		return productVersion;
	}

	public void setProductVersion(String productVersion) {
		this.productVersion = productVersion;
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

	// -- [downStatus] ------------------------

	@NotNull
	@Column(name = "down_status", nullable = false, precision = 10)
	public Integer getDownStatus() {
		return downStatus;
	}

	public void setDownStatus(Integer downStatus) {
		this.downStatus = downStatus;
	}

	// -- [downReason] ------------------------

	@Length(max = 255)
	@Column(name = "down_reason")
	public String getDownReason() {
		return downReason;
	}

	public void setDownReason(String downReason) {
		this.downReason = downReason;
	}

	// -- [importBy] ------------------------

	@NotEmpty
	@Length(max = 60)
	@Column(name = "import_by", nullable = false, length = 60)
	public String getImportBy() {
		return importBy;
	}

	public void setImportBy(String importBy) {
		this.importBy = importBy;
	}

	// -- [operationModel] ------------------------

	@NotNull
	@Column(name = "operation_model_id", nullable = false, precision = 20, insertable = false, updatable = false)
	public Long getOperationModelId() {
		return operationModelId;
	}

	public void setOperationModelId(Long operationModelId) {
		this.operationModelId = operationModelId;
		if (this.operationModel == null) {
			this.operationModel = new Category();
		}
		operationModel.setCategoryId(operationModelId);
	}

	// -- [cooperationModel] ------------------------

	@NotNull
	@Column(name = "cooperation_model_id", nullable = false, precision = 20, insertable = false, updatable = false)
	public Long getCooperationModelId() {
		return cooperationModelId;
	}

	public void setCooperationModelId(Long cooperationModelId) {
		this.cooperationModelId = cooperationModelId;
		if (this.cooperationModel == null) {
			this.cooperationModel = new Category();
		}
		cooperationModel.setCategoryId(cooperationModelId);
	}

	// -- [developer] ------------------------

	@Length(max = 200)
	@Column(length = 200)
	public String getDeveloper() {
		return developer;
	}

	public void setDeveloper(String developer) {
		this.developer = developer;
	}

	// -- [merchant] ------------------------
	@Column(name = "merchant_id", precision = 20, insertable = false, updatable = false)
	public Long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Long merchantId) {
		this.merchantId = merchantId;
		if (this.merchant == null) {
			this.merchant = new Category();
		}
		merchant.setCategoryId(merchantId);
	}
	
	// -- [recommendStorage] ------------------------

	@Column(name = "recommend_storage", nullable = false, precision = 10)
	public Integer getRecommendStorage() {
		return recommendStorage;
	}

	public void setRecommendStorage(Integer recommendStorage) {
		this.recommendStorage = recommendStorage;
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

	// -- [operator] ------------------------
	@Column(name = "operator_id", precision = 20, insertable = false, updatable = false)
	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
		if (this.operator == null) {
			this.operator = new Category();
		}
		operator.setCategoryId(operatorId);
	}

	// -- [productSource] ------------------------

	@NotNull
//	@Pattern(regexp = "^\\d+$", message = "来源产品ID必须为整数")
	@Column(name = "product_source", nullable = false, precision = 20, insertable = false, updatable = false)
	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
		if (this.productSource == null) {
			this.productSource = new Category();
		}
		productSource.setCategoryId(sourceId);
	}


    @JoinTable(name = "product_labels", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "label_id"))
    @ManyToMany(cascade = ALL, targetEntity = Label.class)
    @JsonIgnore
    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }


    /**
	 * @return the productSourceId
	 */
	@Length(max = 200)
	@Column(name = "product_source_id", length = 200)
	public String getProductSourceId() {
		return productSourceId;
	}

	/**
	 * @param productSourceId the productSourceId to set
	 */
	public void setProductSourceId(String productSourceId) {
		this.productSourceId = productSourceId;
	}

	@Length(max = 64)
	@Column(name = "language_type", nullable = true, length = 64)
	public String getLanguageType() {
		return languageType;
	}

	public void setLanguageType(String languageType) {
		this.languageType = languageType;
	}

	@NotEmpty
	@Length(max = 8)
	@Column(name = "star_level", nullable = false, length = 8)
	public String getStarLevel() {
		return starLevel;
	}

	public void setStarLevel(String starLevel) {
		this.starLevel = starLevel;
	}
	
	@Column(name = "mini_lab", nullable = true, length = 10)
	public Integer getMiniLab() {
		return miniLab;
	}

	public void setMiniLab(Integer miniLab) {
		this.miniLab = miniLab;
	}

	// -- [manualDescription] ------------------------

	@Length(max = 1000)
	@Column(name = "manual_description")
	public String getManualDescription() {
		return this.manualDescription;
	}

	public void setManualDescription(String manualDescription) {
		this.manualDescription = manualDescription;
	}

	// -- [moreinfo] ------------------------

	@Length(max = 1000)
	@Column(name = "moreinfo")
	public String getMoreinfo() {
		return this.moreinfo;
	}

	public void setMoreinfo(String moreinfo) {
		this.moreinfo = moreinfo;
	}
	
    // --------------------------------------------------------------------
    // One to one
    // --------------------------------------------------------------------
	@Column(name = "product_type", nullable = true, precision = 2)
    public Integer getProductType() {
		return productType;
	}

	public void setProductType(Integer productType) {
		this.productType = productType;
	}

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // Owner side of one-to-one relation: ProductList.productId ==> ProductState.productId
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	/**
	 * Returns the models List.
	 */
    @JoinColumn(name = "product_id", nullable = false, unique = true, insertable = false, updatable = false)
    @OneToOne(fetch = LAZY)
	public ProductState getProductState() {
		return productState;
	}


	public void setProductState(ProductState productState) {
		this.productState = productState;
	}
		
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // many-to-many: productLists ==> folders
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	/**
     * Returns the folders List.
     */
    @JoinTable(name = "product_site_folder", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "folder_id"))
    @ManyToMany(cascade = ALL)
    @JsonIgnore
    public List<Folder> getFolders() {
        return folders;
    }

    /**
     * Set the folders List.
     * It is recommended to use the helper method addFolder /  removeFolder
     * if you want to preserve referential integrity at the object level.
     *
     * @param folders the List of Category
     */
    public void setFolders(List<Folder> folders) {
        this.folders = folders;
    }

    /**
     * Helper method to add the passed folder to the folders List.
     */
    public boolean addFolders(Folder folder) {
        return getFolders().add(folder);
    }

    /**
     * Helper method to remove the passed folder from the folders List.
     */
    public boolean removeFolder(Folder folder) {
        return getFolders().remove(folder);
    }

    /**
     * Helper method to determine if the passed folder is present in the folders List.
     */
    public boolean containsFolder(Folder folder) {
        return getFolders() != null && getFolders().contains(folder);
    }
	
	// -----------------------------------------
	// Set defaults values
	// -----------------------------------------

	/**
	 * Set the default values.
	 */
	public void initDefaultValues() {
		this.setSafeType(SafeTypeEnum.Unknown.ordinal());
		this.setCreateDate(new Date());
		this.setModDate(this.getCreateDate());
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
	public boolean equals(Object productList) {
		if (this == productList) {
			return true;
		}

		if (!(productList instanceof ProductList)) {
			return false;
		}

		ProductList other = (ProductList) productList;
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
	 * Construct a readable string representation for this ProductList instance.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("productList.productId=[").append(getProductId()).append("]\n");
		result.append("productList.name=[").append(getName()).append("]\n");
		result.append("productList.billingType=[").append(getBillingTypeId()).append("]\n");
		result.append("productList.price=[").append(getPrice()).append("]\n");
		result.append("productList.description=[").append(getDescription()).append("]\n");
		result.append("productList.tagLine=[").append(getTagLine()).append("]\n");
		result.append("productList.publishDate=[").append(getPublishDate()).append("]\n");
		result.append("productList.publishBy=[").append(getPublishBy()).append("]\n");
		result.append("productList.categoryId=[").append(getCategoryId()).append("]\n");
		result.append("productList.label=[").append(getLabel()).append("]\n");
		result.append("productList.productVersion=[").append(getProductVersion()).append("]\n");
		result.append("productList.modDate=[").append(getModDate()).append("]\n");
		result.append("productList.modBy=[").append(getModBy()).append("]\n");
		result.append("productList.createBy=[").append(getCreateBy()).append("]\n");
		result.append("productList.createDate=[").append(getCreateDate()).append("]\n");
		result.append("productList.downStatus=[").append(getDownStatus()).append("]\n");
		result.append("productList.downReason=[").append(getDownReason()).append("]\n");
		result.append("productList.importBy=[").append(getImportBy()).append("]\n");
		result.append("productList.operationModel=[").append(getOperationModelId()).append("]\n");
		result.append("productList.cooperationModel=[").append(getCooperationModelId()).append("]\n");
		result.append("productList.developer=[").append(getDeveloper()).append("]\n");
		result.append("productList.operator=[").append(getOperator()).append("]\n");
		result.append("productList.merchant=[").append(getMerchantId()).append("]\n");
		result.append("productList.starLevel=[").append(this.getStarLevel()).append("]\n");
		result.append("productList.productSource=[").append(getProductSource()).append("]\n");
		result.append("productList.sourceId=[").append(getSourceId()).append("]\n");
		result.append("productList.manual_description=[").append(getManualDescription()).append("]\n");
		result.append("productList.moreinfo=[").append(getMoreinfo()).append("]\n");
		result.append("productList.producttype=[").append(getProductType()).append("]\n");
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
	public ProductList copy() {
		ProductList productList = new ProductList();
		copyTo(productList);
		return productList;
	}

	/**
	 * Copy the current properties to the given object
	 */
	@Override
	public void copyTo(ProductList productList) {
		productList.setProductId(getProductId());
		productList.setName(getName());
		productList.setBillingTypeId(getBillingTypeId());
		productList.setPrice(getPrice());
		productList.setDescription(getDescription());
		productList.setTagLine(getTagLine());
		productList.setPublishDate(getPublishDate());
		productList.setPublishBy(getPublishBy());
		productList.setCategoryId(getCategoryId());
		productList.setLabel(getLabel());
		productList.setProductVersion(getProductVersion());
		productList.setModDate(getModDate());
		productList.setModBy(getModBy());
		productList.setCreateBy(getCreateBy());
		productList.setCreateDate(getCreateDate());
		productList.setDownStatus(getDownStatus());
		productList.setDownReason(getDownReason());
		productList.setImportBy(getImportBy());
		productList.setOperationModelId(getOperationModelId());
		productList.setCooperationModelId(getCooperationModelId());
		productList.setDeveloper(getDeveloper());
		productList.setOperator(getOperator());
		productList.setStarLevel(getStarLevel());
		productList.setSourceId(this.getSourceId());
		productList.setProductSourceId(this.getProductSourceId());
		productList.setOperatorId(this.getOperatorId());
		productList.setMiniLab(this.getMiniLab());
		productList.setFolders(this.getFolders());
		productList.setMerchantId(this.getMerchantId());
		productList.setRecommendStorage(this.getRecommendStorage());
		productList.setSafeType(this.getSafeType());
		
		productList.setManualDescription(this.getManualDescription());
		productList.setMoreinfo(this.getMoreinfo());
		productList.setProductType(this.getProductType());
	}

	@Transient
	@JsonIgnore
	public boolean isEnable() {
		if(null != this.getDownStatus()) {
			ProductListStatusEnum sts = ProductListStatusEnum.getInstance(this.getDownStatus());
			if(null != sts && sts == ProductListStatusEnum.Enable) {
				return true;
			}
		}
		return false;
	}
	
	public enum ProductListStatusEnum {
    	Enable, Disable;
    	public static ProductListStatusEnum getInstance(Integer value) {
    		if(null != value) {
    			ProductListStatusEnum[] types = ProductListStatusEnum.values();
	    		for(ProductListStatusEnum type : types) {
	    			if(type.ordinal() == value.intValue()) {
	    				return type;
	    			}
	    		}
    		}
    		return null;
    	}
	}
	
	public enum SafeTypeEnum {
		Unknown, Safe, Unsafe;
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
			case Unsafe:
				return "风险";
			}
			return this.name().toLowerCase();
		}
	}
	
	public enum ProductTypeEnum {
		Downloadable, PageGame;
		public static ProductTypeEnum getInstance(Integer value) {
    		if(null != value) {
    			ProductTypeEnum[] types = ProductTypeEnum.values();
	    		for(ProductTypeEnum type : types) {
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
			case Downloadable:
				return "包体";
			case PageGame:
				return "页游";
			}
			return this.name().toLowerCase();
		}
	}
}