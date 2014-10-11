package org.visitor.appportal.domain;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Table(name = "model_list")
public class ModelList implements Serializable, Copyable<ModelList> {
	/**
	 * 状态:有效
	 */
	public static final int ENABLE = 0;
	/**
	 * 状态:无效
	 */
	public static final int DISABLE = 1;
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(ModelList.class);

	// Raw attributes
	private Long modelId; // pk
	private String name; // not null
	private Long platformId; // not null
	private Long brandId; // not null
	private Long platformVersionId; // not null
	private Long resolutionId; // not null
	private Long cpuId;
	private Long memoryId;
	private String description;
	private Boolean ifTouch; // not null
	private String userAgent;
	private String userAgent1;
	private String createBy; // not null
	private Date createDate; // not null
	private String modBy; // not null
	private Date modDate; // not null
	private Integer status = ENABLE; // not null
	private String modelSeries;
	private Long productCount;// not null
	private Long picId;

	private Category platform; // not null
	private Category platformVersion; // not null
	private Category brand; // not null
	private Category resolution; // not null
	
	@NotNull
	@JoinColumn(name = "platform_id", nullable = false)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = LAZY)
	public Category getPlatform() {
		return platform;
	}

	public void setPlatform(Category platform) {
		this.platform = platform;
		if (platform != null && platform.getCategoryId() != null) {
			setPlatformId(platform.getCategoryId());
		} else {
			setPlatformId(null);
		}
	}
	
	@JoinColumn(name = "platform_version_id", nullable = false)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	public Category getPlatformVersion() {
		return platformVersion;
	}

	public void setPlatformVersion(Category platformVersion) {
		this.platformVersion = platformVersion;
		if(null != platformVersion) {
			this.setPlatformVersionId(platformVersion.getCategoryId());
		} else {
			this.setPlatformVersionId(null);
		}
	}
	@JoinColumn(name = "brand_id", nullable = false)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch =FetchType.EAGER)
	@JsonIgnore
	public Category getBrand() {
		return brand;
	}

	public void setBrand(Category brand) {
		this.brand = brand;
		if(null != brand) {
			this.setBrandId(brand.getCategoryId());
		} else {
			this.setBrandId(null);
		}
	}
	@JoinColumn(name = "resolution_id", nullable = false)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	public Category getResolution() {
		return resolution;
	}

	public void setResolution(Category resolution) {
		this.resolution = resolution;
		if(null != resolution) {
			setResolutionId(resolution.getCategoryId());
		} else {
			setResolutionId(null);
		}
	}

	

	// ---------------------------
	// Constructors
	// ---------------------------

	public ModelList() {
	}

	public ModelList(Long primaryKey) {
		this();
		setPrimaryKey(primaryKey);
	}

	// ---------------------------
	// Identifiable implementation
	// ---------------------------

	@Transient
	@XmlTransient
	public Long getPrimaryKey() {
		return getModelId();
	}

	public void setPrimaryKey(Long modelId) {
		setModelId(modelId);
	}


	// -------------------------------
	// Getter & Setter
	// -------------------------------

	// -- [modelId] ------------------------

	@Column(name = "model_id", nullable = false, unique = true, precision = 20)
	@GeneratedValue
	@Id
	public Long getModelId() {
		return modelId;
	}

	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}

	// -- [name] ------------------------

	@NotBlank
	@Length(max = 120)
	@Column(nullable = false, length = 120)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// -- [platformId] ------------------------
	@NotNull
	@Column(name = "platform_id", nullable = false, precision = 20, insertable = false, updatable = false)
	public Long getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Long platformId) {
		this.platformId = platformId;
		if (this.platform==null) {
			this.platform = new Category();
		}
		platform.setCategoryId(platformId);
	}

	// -- [brandId] ------------------------

	@NotNull
	@Column(name = "brand_id", nullable = false, precision = 20, insertable = false, updatable = false)
	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
		if (this.brand==null) {
			this.brand = new Category();
		}
		brand.setCategoryId(brandId);
	}

	// -- [platformVersionId] ------------------------

	@NotNull
	@Column(name = "platform_version_id", nullable = false, precision = 20, insertable = false, updatable = false)
	public Long getPlatformVersionId() {
		return platformVersionId;
	}

	public void setPlatformVersionId(Long platformVersionId) {
		this.platformVersionId = platformVersionId;
		if (this.platformVersion==null) {
			this.platformVersion = new Category();
		}
		platformVersion.setCategoryId(platformVersionId);
	}

	// -- [resolutionId] ------------------------

	@NotNull
	@Column(name = "resolution_id", nullable = false, precision = 20, insertable = false, updatable = false)
	public Long getResolutionId() {
		return resolutionId;
	}

	public void setResolutionId(Long resolutionId) {
		this.resolutionId = resolutionId;
		if (this.resolution==null) {
			this.resolution = new Category();
		}
		resolution.setCategoryId(resolutionId);
	}

	// -- [cpuId] ------------------------

	@Column(name = "cpu_id", precision = 20)
	public Long getCpuId() {
		return cpuId;
	}

	public void setCpuId(Long cpuId) {
		this.cpuId = cpuId;
	}

	// -- [memoryId] ------------------------

	@Column(name = "memory_id", precision = 20)
	public Long getMemoryId() {
		return memoryId;
	}

	public void setMemoryId(Long memoryId) {
		this.memoryId = memoryId;
	}

	// -- [description] ------------------------

	@Length(max = 255)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	// -- [ifTouch] ------------------------

	@NotNull
	@Column(name = "if_touch", nullable = false, length = 0)
	public Boolean getIfTouch() {
		return ifTouch;
	}

	public void setIfTouch(Boolean ifTouch) {
		this.ifTouch = ifTouch;
	}

	// -- [userAgent] ------------------------

	@Length(max = 200)
	@Column(name = "user_agent", length = 200)
	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	// -- [userAgent1] ------------------------

	@Length(max = 200)
	@Column(name = "user_agent1", length = 200)
	public String getUserAgent1() {
		return userAgent1;
	}

	public void setUserAgent1(String userAgent1) {
		this.userAgent1 = userAgent1;
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

	// -- [status] ------------------------

	@NotNull
	@Column(nullable = false, precision = 10)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	// -- [modelSeries] ------------------------

	@Length(max = 60)
	@Column(name = "model_series", length = 60)
	public String getModelSeries() {
		return modelSeries;
	}

	public void setModelSeries(String modelSeries) {
		this.modelSeries = modelSeries;
	}

	// -- [productCount] ------------------------

	@NotNull
	@Column(name = "product_count", nullable = false, precision = 20)
	public Long getProductCount() {
		return productCount;
	}

	public void setProductCount(Long productCount) {
		this.productCount = productCount;
	}

	// -- [picId] ------------------------

	@Column(name = "pic_id", precision = 20)
	public Long getPicId() {
		return picId;
	}
	
	public void setPicId(Long picId) {
		this.picId = picId;
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
	public boolean equals(Object modelList) {
		if (this == modelList) {
			return true;
		}

		if (!(modelList instanceof ModelList)) {
			return false;
		}

		ModelList other = (ModelList) modelList;
		return _getUid().equals(other._getUid());
	}

	@Override
	public int hashCode() {
		return _getUid().hashCode();
	}

	private Object _uid;

	private Object _getUid() {
		if (_uid == null) {
			if (null == this.getModelId()) {
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
	 * Construct a readable string representation for this ModelList instance.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("modelList.modelId=[").append(getModelId()).append("]\n");
		result.append("modelList.name=[").append(getName()).append("]\n");
		result.append("modelList.platformId=[").append(getPlatformId()).append("]\n");
		result.append("modelList.brandId=[").append(getBrandId()).append("]\n");
		result.append("modelList.platformVersionId=[").append(getPlatformVersionId()).append("]\n");
		result.append("modelList.resolutionId=[").append(getResolutionId()).append("]\n");
		result.append("modelList.cpuId=[").append(getCpuId()).append("]\n");
		result.append("modelList.memoryId=[").append(getMemoryId()).append("]\n");
		result.append("modelList.description=[").append(getDescription()).append("]\n");
		result.append("modelList.ifTouch=[").append(getIfTouch()).append("]\n");
		result.append("modelList.userAgent=[").append(getUserAgent()).append("]\n");
		result.append("modelList.userAgent1=[").append(getUserAgent1()).append("]\n");
		result.append("modelList.createBy=[").append(getCreateBy()).append("]\n");
		result.append("modelList.createDate=[").append(getCreateDate()).append("]\n");
		result.append("modelList.modBy=[").append(getModBy()).append("]\n");
		result.append("modelList.modDate=[").append(getModDate()).append("]\n");
		result.append("modelList.status=[").append(getStatus()).append("]\n");
		result.append("modelList.modelSeries=[").append(getModelSeries()).append("]\n");
		result.append("modelList.productCount=[").append(getProductCount()).append("]\n");
		result.append("modelList.picId=[").append(getPicId()).append("]\n");
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
	public ModelList copy() {
		ModelList modelList = new ModelList();
		copyTo(modelList);
		return modelList;
	}

	/**
	 * Copy the current properties to the given object
	 */
	@Override
	public void copyTo(ModelList modelList) {
		modelList.setModelId(getModelId());
		modelList.setName(getName());
		modelList.setPlatformId(getPlatformId());
		modelList.setBrandId(getBrandId());
		modelList.setPlatformVersionId(getPlatformVersionId());
		modelList.setResolutionId(getResolutionId());
		modelList.setCpuId(getCpuId());
		modelList.setMemoryId(getMemoryId());
		modelList.setDescription(getDescription());
		modelList.setIfTouch(getIfTouch());
		modelList.setUserAgent(getUserAgent());
		modelList.setUserAgent1(getUserAgent1());
		modelList.setCreateBy(getCreateBy());
		modelList.setCreateDate(getCreateDate());
		modelList.setModBy(getModBy());
		modelList.setModDate(getModDate());
		modelList.setStatus(getStatus());
		modelList.setModelSeries(getModelSeries());
		modelList.setProductCount(getProductCount());
		modelList.setPicId(getPicId());
	}
}