package org.visitor.appportal.domain;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.TemporalType.TIMESTAMP;
import static org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

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
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Table(name = "product_file")
public class ProductFile implements Serializable, Copyable<ProductFile> {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(ProductFile.class);
	public static final Integer DISABLE = 1;
	public static final Integer ENABLE = 0;
	public static final Pattern INSTALL_SUFFIX_REG = Pattern.compile("^.*?\\.(jar|sis|sisx|JAR|SIS|SISX)$");
	public static final Pattern JAD_SUFFIX_REG = Pattern.compile("^.*?\\.(jad|JAD)$");
	public static final Integer DEFAULT_TRUE = 0; //是否默认，是
	public static final Integer DEFAULT_FALSE = 1;//是否默认，否

	public static final Integer AUTOSCAN_OPEN = 0; //是否默认，是
	public static final Integer AUTOSCAN_CLOSE = 1;//是否默认，否
	
    // Raw attributes
    private Long fileId; // pk
    private String filePath;
    private String fileUrl;
    private String fileName; // not null
    private String fileDisName;
    private String fileSize;
    private String fileSuffix;
    private Integer status;
    private transient String createBy; // not null
    private Date createDate; // not null
    private String modBy; // not null
    private Date modDate; // not null
    private String jarMd5;
    private String jadMd5;
    private Long dlCount; // not null
    private Long screenSize;
    private String versionOperator;
    private String versionValue = "0";//平台版本的值，不需要持久化，但需要写入redis
    private Integer ifDefaultFile;

    // Technical attributes for query by example
    private Long productId; // not null
    private Long osId;
    private Long platformId;
    private Long platformVersionId;
    private Long resolutionId;
	private Integer safeType; //下载文件安全类型
	private Integer autoScan; //安全监测开关
	private String packageName;
	private Integer versionCode;
	private String versionName;
	private Integer sdkVersion;
	private Integer minSdkVersion;
	private Integer targetSdkVersion;
	private String identity;

    // Many to one
    private Category platform; // (platformId)
    private Category os; // (osId)
    private Category platformVersion; // (platformVersionId)
    private Category resolution; // (resolutionId)
    private ProductList product; // not null (productId)
    
	// Many to many
	private List<ModelList> models = new ArrayList<ModelList>();

    // ---------------------------
    // Constructors
    // ---------------------------

    public ProductFile() {
    }

    public ProductFile(Long primaryKey) {
        this();
        setPrimaryKey(primaryKey);
    }

    // ---------------------------
    // Identifiable implementation
    // ---------------------------

    @Transient
    @XmlTransient
    public Long getPrimaryKey() {
        return getFileId();
    }

    public void setPrimaryKey(Long fileId) {
        setFileId(fileId);
    }

    // -------------------------------
    // Getter & Setter
    // -------------------------------

    // -- [fileId] ------------------------

    @Column(name = "file_id", nullable = false, unique = true, precision = 20)
    @GeneratedValue
    @Id
    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }
    
    // -- [filePath] ------------------------

    @Length(max = 255)
    @Column(name = "file_path")
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    // -- [fileUrl] ------------------------

    @Length(max = 255)
    @Column(name = "file_url")
    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    // -- [productId] ------------------------

    @Column(name = "product_id", nullable = false, precision = 20, insertable = false, updatable = false)
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    // -- [fileName] ------------------------

    @Length(max = 200)
    @Column(name = "file_name", nullable = false, length = 200)
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    // -- [fileDisName] ------------------------

    @Length(max = 200)
    @Column(name = "file_dis_name", nullable = false, length = 200)
    public String getFileDisName() {
        return fileDisName;
    }

    public void setFileDisName(String fileDisName) {
        this.fileDisName = fileDisName;
    }

    // -- [fileSize] ------------------------

    @Length(max = 60)
    @Column(name = "file_size", length = 60)
    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    // -- [fileSuffix] ------------------------

    @Length(max = 10)
    @Column(name = "file_suffix", length = 10)
    public String getFileSuffix() {
        return fileSuffix;
    }

    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }

    // -- [status] ------------------------

    @Column(nullable = false, precision = 10)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    // -- [jarMd5] ------------------------

    @Length(max = 60)
    @Column(name = "jar_md5", length = 60)
    public String getJarMd5() {
        return jarMd5;
    }

    public void setJarMd5(String jarMd5) {
        this.jarMd5 = jarMd5;
    }

    // -- [jadMd5] ------------------------

    @Length(max = 60)
    @Column(name = "jad_md5", length = 60)
    public String getJadMd5() {
        return jadMd5;
    }

    public void setJadMd5(String jadMd5) {
        this.jadMd5 = jadMd5;
    }

    // -- [dlCount] ------------------------

    @Column(name = "dl_count", nullable = false, precision = 20)
    public Long getDlCount() {
        return dlCount;
    }

    public void setDlCount(Long dlCount) {
        this.dlCount = dlCount;
    }

    /**
	 * @return the screenSize
	 */
    @Column(name = "screen_size", precision = 20)
	public Long getScreenSize() {
		return screenSize;
	}

	/**
	 * @param screenSize the screenSize to set
	 */
	public void setScreenSize(Long screenSize) {
		this.screenSize = screenSize;
	}

	/**
	 * @return the versionOperator
	 */
    @Length(max = 2)
    @Column(name = "version_operator", length = 2)
	public String getVersionOperator() {
		return versionOperator;
	}

	/**
	 * @param versionOperator the versionOperator to set
	 */
	public void setVersionOperator(String versionOperator) {
		this.versionOperator = versionOperator;
	}
    
    // -- [osId] ------------------------


	/**
	 * @return the versionValue
	 */
	@Transient
	public String getVersionValue() {
		return versionValue;
	}

	/**
	 * @param versionValue the versionValue to set
	 */
	public void setVersionValue(String versionValue) {
		this.versionValue = versionValue;
	}

	@Column(name = "os_id", precision = 20, insertable = false, updatable = false)
    public Long getOsId() {
        return osId;
    }

    public void setOsId(Long osId) {
        this.osId = osId;
    }

    // -- [platformId] ------------------------

    @Column(name = "platform_id", precision = 20, insertable = false, updatable = false)
    public Long getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Long platformId) {
        this.platformId = platformId;
    }

    // -- [platformVersionId] ------------------------

    @Column(name = "platform_version_id", precision = 20, insertable = false, updatable = false)
    public Long getPlatformVersionId() {
        return platformVersionId;
    }

    public void setPlatformVersionId(Long platformVersionId) {
        this.platformVersionId = platformVersionId;
        if (this.getPlatformVersion() != null) {
            this.setVersionValue(getPlatformVersion().getName());
        }
    }

    // -- [resolutionId] ------------------------

    @Column(name = "resolution_id", precision = 20, insertable = false, updatable = false)
    public Long getResolutionId() {
        return resolutionId;
    }

    public void setResolutionId(Long resolutionId) {
        this.resolutionId = resolutionId;
    }

    // -- [ifDefaultFile] ------------------------

    @Column(name = "if_default_file", precision = 10)
    public Integer getIfDefaultFile() {
        return ifDefaultFile;
    }

    public void setIfDefaultFile(Integer ifDefaultFile) {
        this.ifDefaultFile = ifDefaultFile;
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
	/**
	 * @return the autoScan
	 */
	@Column(name = "auto_scan", nullable = true, precision = 2)
	public Integer getAutoScan() {
		return autoScan;
	}

	/**
	 * @param autoScan the autoScan to set
	 */
	public void setAutoScan(Integer autoScan) {
		this.autoScan = autoScan;
	}
	
	/**
	 * @return the packageName
	 */
    @Length(max = 200)
    @Column(name = "package_name", nullable = true, length = 200)
	public String getPackageName() {
		return packageName;
	}

	/**
	 * @param packageName the packageName to set
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
	/**
	 * @return the versionCode
	 */
	@Column(name = "version_code", nullable = true, precision = 2)
	public Integer getVersionCode() {
		return versionCode;
	}

	/**
	 * @param versionCode the versionCode to set
	 */
	public void setVersionCode(Integer versionCode) {
		this.versionCode = versionCode;
	}
	
	/**
	 * @return the versionName
	 */
    @Length(max = 200)
    @Column(name = "version_name", nullable = true, length = 200)
	public String getVersionName() {
		return versionName;
	}

	/**
	 * @param versionName the versionName to set
	 */
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	
	/**
	 * @return the sdkVersion
	 */
	@Column(name = "sdk_version", nullable = true, precision = 2)
	public Integer getSdkVersion() {
		return sdkVersion;
	}

	/**
	 * @param sdkVersion the sdkVersion to set
	 */
	public void setSdkVersion(Integer sdkVersion) {
		this.sdkVersion = sdkVersion;
	}
	
	/**
	 * @return the minSdkVersion
	 */
	@Column(name = "min_sdk_version", nullable = true, precision = 2)
	public Integer getMinSdkVersion() {
		return minSdkVersion;
	}

	/**
	 * @param minSdkVersion the minSdkVersion to set
	 */
	public void setMinSdkVersion(Integer minSdkVersion) {
		this.minSdkVersion = minSdkVersion;
	}
	
	/**
	 * @return the targetSdkVersion
	 */
	@Column(name = "target_sdk_version", nullable = true, precision = 2)
	public Integer getTargetSdkVersion() {
		return targetSdkVersion;
	}

	/**
	 * @param targetSdkVersion the targetSdkVersion to set
	 */
	public void setTargetSdkVersion(Integer targetSdkVersion) {
		this.targetSdkVersion = targetSdkVersion;
	}
	
	/**
	 * @return the identity
	 */
    @Length(max = 200)
    @Column(name = "identity", nullable = true, length = 200)
	public String getIdentity() {
		return identity;
	}

	/**
	 * @param identity the identity to set
	 */
	public void setIdentity(String identity) {
		this.identity = identity;
	}
    // --------------------------------------------------------------------
    // Many to One support
    // --------------------------------------------------------------------

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // many-to-one: ProductFile.platformId ==> Category.categoryId
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Cache(usage = NONSTRICT_READ_WRITE)
    @JoinColumn(name = "platform_id")
    @ManyToOne(fetch = EAGER)
    @Fetch(FetchMode.JOIN)
    @JsonIgnore
    public Category getPlatform() {
        return platform;
    }

    /**
     * Set the platform without adding this ProductFile instance on the passed platform
     * If you want to preserve referential integrity we recommend to use
     * instead the corresponding adder method provided by Category
     */
    public void setPlatform(Category platform) {
        this.platform = platform;

        // We set the foreign key property so it can be used by Hibernate search by Example facility.
        if (platform != null) {
            setPlatformId(platform.getCategoryId());
        } else {
            setPlatformId(null);
        }
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // many-to-one: ProductFile.osId ==> Category.categoryId
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Cache(usage = NONSTRICT_READ_WRITE)
    @JoinColumn(name = "os_id")
    @ManyToOne(fetch = EAGER)
    @Fetch(FetchMode.JOIN)
    @JsonIgnore
    public Category getOs() {
        return os;
    }

    /**
     * Set the os without adding this ProductFile instance on the passed os
     * If you want to preserve referential integrity we recommend to use
     * instead the corresponding adder method provided by Category
     */
    public void setOs(Category os) {
        this.os = os;

        // We set the foreign key property so it can be used by Hibernate search by Example facility.
        if (os != null) {
            setOsId(os.getCategoryId());
        } else {
            setOsId(null);
        }
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // many-to-one: ProductFile.platformVersionId ==> Category.categoryId
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Cache(usage = NONSTRICT_READ_WRITE)
    @JoinColumn(name = "platform_version_id")
    @ManyToOne(fetch = EAGER)
    @Fetch(FetchMode.JOIN)
    @JsonIgnore
    public Category getPlatformVersion() {
        return platformVersion;
    }

    /**
     * Set the platformVersion without adding this ProductFile instance on the passed platformVersion
     * If you want to preserve referential integrity we recommend to use
     * instead the corresponding adder method provided by Category
     */
    public void setPlatformVersion(Category platformVersion) {
        this.platformVersion = platformVersion;

        // We set the foreign key property so it can be used by Hibernate search by Example facility.
        if (platformVersion != null) {
            setPlatformVersionId(platformVersion.getCategoryId());
            this.setVersionValue(platformVersion.getName());
        } else {
        	this.setVersionValue(null);
            setPlatformVersionId(null);
        }
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // many-to-one: ProductFile.resolutionId ==> Category.categoryId
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @Cache(usage = NONSTRICT_READ_WRITE)
    @JoinColumn(name = "resolution_id")
    @ManyToOne(fetch = EAGER)
    @Fetch(FetchMode.JOIN)    
    @JsonIgnore
    public Category getResolution() {
        return resolution;
    }

    /**
     * Set the resolution without adding this ProductFile instance on the passed resolution
     * If you want to preserve referential integrity we recommend to use
     * instead the corresponding adder method provided by Category
     */
    public void setResolution(Category resolution) {
        this.resolution = resolution;

        // We set the foreign key property so it can be used by Hibernate search by Example facility.
        if (resolution != null) {
            setResolutionId(resolution.getCategoryId());
            String[] array = StringUtils.split(StringUtils.upperCase(resolution.getName()), "X");
            if(null != array && array.length == 2) {
            	if(StringUtils.isNumeric(array[0]) && StringUtils.isNumeric(array[1])) {
            		this.setScreenSize(Long.valueOf(Integer.valueOf(array[0]) * Integer.valueOf(array[1])));
            	}
            }
        } else {
            setResolutionId(null);
            setScreenSize(0l);
        }
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // many-to-one: ProductFile.productId ==> ProductList.productId
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @NotNull
    @Cache(usage = NONSTRICT_READ_WRITE)
    @JoinColumn(name = "product_id", nullable = false)
    @ManyToOne(fetch = LAZY)
    @JsonIgnore
    public ProductList getProduct() {
        return product;
    }

    /**
     * Set the product without adding this ProductFile instance on the passed product
     * If you want to preserve referential integrity we recommend to use
     * instead the corresponding adder method provided by ProductList
     */
    public void setProduct(ProductList product) {
        this.product = product;

        // We set the foreign key property so it can be used by Hibernate search by Example facility.
        if (product != null) {
            setProductId(product.getProductId());
        } else {
            setProductId(null);
        }
    }
    
	// --------------------------------------------------------------------
	// Many to Many
	// --------------------------------------------------------------------

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	// many-to-many: files ==> models
	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

	/**
	 * Returns the models List.
	 */
	@JoinTable(name = "file_suite_model", joinColumns = @JoinColumn(name = "file_id"), inverseJoinColumns = @JoinColumn(name = "model_id"))
	@ManyToMany(cascade = PERSIST)
	@JsonIgnore
	public List<ModelList> getModels() {
		return models;
	}

	/**
	 * Set the models List. It is recommended to use the helper method addModel
	 * / removeModel if you want to preserve referential integrity at the object
	 * level.
	 * 
	 * @param models
	 *            the List of ModelList
	 */
	public void setModels(List<ModelList> models) {
		this.models = models;
	}

	/**
	 * Helper method to add the passed model to the models List.
	 */
	public boolean addModel(ModelList model) {
		return getModels().add(model);
	}

	/**
	 * Helper method to remove the passed model from the models List.
	 */
	public boolean removeModel(ModelList model) {
		return getModels().remove(model);
	}

	/**
	 * Helper method to determine if the passed model is present in the models
	 * List.
	 */
	public boolean containsModel(ModelList model) {
		return getModels() != null && getModels().contains(model);
	}

    // -----------------------------------------
    // Set defaults values
    // -----------------------------------------

    /**
     * Set the default values.
     */
    public void initDefaultValues() {
    	this.setAutoScan(AUTOSCAN_OPEN);
    	this.setSafeType(SafeTypeEnum.Unknown.ordinal());
    	this.setCreateDate(new Date());
    	this.setModDate(this.getCreateDate());
    	this.setStatus(ENABLE);
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
    public boolean equals(Object productFile) {
        if (this == productFile) {
            return true;
        }

        if (!(productFile instanceof ProductFile)) {
            return false;
        }

        ProductFile other = (ProductFile) productFile;
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
     * Construct a readable string representation for this ProductFile instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("productFile.fileId=[").append(getFileId()).append("]\n");
        result.append("productFile.filePath=[").append(getFilePath()).append("]\n");
        result.append("productFile.fileUrl=[").append(getFileUrl()).append("]\n");
        result.append("productFile.productId=[").append(getProductId()).append("]\n");
        result.append("productFile.fileName=[").append(getFileName()).append("]\n");
        result.append("productFile.fileDisName=[").append(getFileDisName()).append("]\n");
        result.append("productFile.fileSize=[").append(getFileSize()).append("]\n");
        result.append("productFile.fileSuffix=[").append(getFileSuffix()).append("]\n");
        result.append("productFile.status=[").append(getStatus()).append("]\n");
        result.append("productFile.createBy=[").append(getCreateBy()).append("]\n");
        result.append("productFile.createDate=[").append(getCreateDate()).append("]\n");
        result.append("productFile.modBy=[").append(getModBy()).append("]\n");
        result.append("productFile.modDate=[").append(getModDate()).append("]\n");
        result.append("productFile.jarMd5=[").append(getJarMd5()).append("]\n");
        result.append("productFile.jadMd5=[").append(getJadMd5()).append("]\n");
        result.append("productFile.dlCount=[").append(getDlCount()).append("]\n");
        result.append("productFile.osId=[").append(getOsId()).append("]\n");
        result.append("productFile.platformId=[").append(getPlatformId()).append("]\n");
        result.append("productFile.platformVersionId=[").append(getPlatformVersionId()).append("]\n");
        result.append("productFile.resolutionId=[").append(getResolutionId()).append("]\n");
        result.append("productFile.ifDefaultFile=[").append(getIfDefaultFile()).append("]\n");
        result.append("productFile.screenSize=[").append(getScreenSize()).append("]\n");
        result.append("productFile.versionOperator=[").append(getVersionOperator()).append("]\n");
        result.append("productFile.packageName=[").append(getPackageName()).append("]\n");
        result.append("productFile.versionCode=[").append(getVersionCode()).append("]\n");
        result.append("productFile.versionName=[").append(getVersionName()).append("]\n");
        result.append("productFile.sdkVersion=[").append(getSdkVersion()).append("]\n");
        result.append("productFile.minSdkVersion=[").append(getMinSdkVersion()).append("]\n");
        result.append("productFile.targetSdkVersion=[").append(getTargetSdkVersion()).append("]\n");
        result.append("productFile.identity=[").append(getIdentity()).append("]\n");
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
    public ProductFile copy() {
        ProductFile productFile = new ProductFile();
        copyTo(productFile);
        return productFile;
    }

    /**
     * Copy the current properties to the given object
     */
    @Override
    public void copyTo(ProductFile productFile) {
        productFile.setFileId(getFileId());
        productFile.setFilePath(getFilePath());
        productFile.setFileUrl(getFileUrl());
        //productFile.setProductId(getProductId());
        productFile.setFileName(getFileName());
        productFile.setFileDisName(getFileDisName());
        productFile.setFileSize(getFileSize());
        productFile.setFileSuffix(getFileSuffix());
        productFile.setStatus(getStatus());
        productFile.setCreateBy(getCreateBy());
        productFile.setCreateDate(getCreateDate());
        productFile.setModBy(getModBy());
        productFile.setModDate(getModDate());
        productFile.setJarMd5(getJarMd5());
        productFile.setJadMd5(getJadMd5());
        productFile.setDlCount(getDlCount());
        productFile.setScreenSize(getScreenSize());
        productFile.setVersionOperator(getVersionOperator());
        productFile.setSafeType(getSafeType());//文件级别的安全类型
        productFile.setAutoScan(getAutoScan());
        
        productFile.setPackageName(getPackageName());
        productFile.setVersionCode(getVersionCode());
        productFile.setVersionName(getVersionName());
        productFile.setSdkVersion(getSdkVersion());
        productFile.setMinSdkVersion(getMinSdkVersion());
        productFile.setTargetSdkVersion(getTargetSdkVersion());
        productFile.setIdentity(getIdentity());
        //productFile.setOsId(getOsId());
        //productFile.setPlatformId(getPlatformId());
        //productFile.setPlatformVersionId(getPlatformVersionId());
        //productFile.setResolutionId(getResolutionId());
        productFile.setIfDefaultFile(getIfDefaultFile());
        if (getPlatform() != null) {
            productFile.setPlatform(getPlatform());
        }
        if (getOs() != null) {
            productFile.setOs(getOs());
        }
        if (getPlatformVersion() != null) {
            productFile.setPlatformVersion(getPlatformVersion());
        }
        if (getResolution() != null) {
            productFile.setResolution(getResolution());
        }
        if (getProduct() != null) {
            productFile.setProduct(getProduct());
        }
    }
    
    public static String getFoldPath(Long productId) {
		String fileName = StringUtils.leftPad(String.valueOf(productId), 8, "0");
		StringBuffer path = new StringBuffer("/" + fileName);
		for (int i = path.indexOf("/"); i < path.length() - 2; i = path.lastIndexOf("/")) {
			path.insert(i + 3, "/");
		}
		path.append(productId).append("/");
		return path.toString();
    }

	public static String getPath(Long productId, Long fileId, String fileName) {
		return getFoldPath(productId) + fileId + "/" + fileName;
	}
	
	@Transient
	@JsonIgnore
	public List<Long> getModelIdList() {
		List<Long> list = new ArrayList<Long>();
		for (ModelList model : models) {
			list.add(model.getModelId());
		}
		return list;
	}
	
	@Transient
	@JsonIgnore
	public boolean isValid() {
		return null != this.getStatus() && ProductFile.ENABLE.intValue() == this.getStatus().intValue();
	}
	/**
	 * 文件版本操作类型
	 */
    public enum VersionOperatorEnum {
    	GE(0), EQ(1), LE(2);
    	private Integer value;
    	private String displayName;
    	private VersionOperatorEnum(Integer value) {
    		this.value = value;
    		switch(value) {
    		case 0:
    			this.displayName = ">=";
    			break;
    		case 1:
    			this.displayName = "=";
    			break;
    		case 2:
    			this.displayName = "<=";
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
    	
		public static VersionOperatorEnum getInstance(Integer value) {
			VersionOperatorEnum[] values = VersionOperatorEnum.values();
			for(VersionOperatorEnum typeValue : values) {
				if(typeValue.getValue().intValue() == value.intValue()) {
					return typeValue;
				}
			}
			return null;
		}
		
		public static VersionOperatorEnum getInstance(String displayName) {
			
			if(displayName.equals("==")){
				displayName = "=";
			}
			
			VersionOperatorEnum[] values = VersionOperatorEnum.values();
			for(VersionOperatorEnum typeValue : values) {
				if(typeValue.getDisplayName().equals(displayName)) {
					return typeValue;
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