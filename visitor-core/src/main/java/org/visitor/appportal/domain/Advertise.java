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
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Table(name = "advertise")
public class Advertise  implements Serializable, Copyable<Advertise>{
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(Advertise.class);
    
    /**
     * 采用枚举类型而不是静态常量
     */
    /*public static final int TYPE_URL=0;/*广告类型为URL*/
    //public static final int TYPE_FOLDER=1;//频道
    //public static final int TYPE_PRODUCT=2;//产品
    
    
    /*字段域*/
    private Long advertiseId;//主键',

    private String name;//VARCHAR(200) NOT NULL COMMENT '名称',

    private Long pictureId;// BIGINT(20) UNSIGNED NOT NULL COMMENT'广告图',
    private Integer type;//INT(10) UNSIGNED NOT NULL COMMENT '类型 0：合作url 1：频道 2：产品',
    private String url;//VARCHAR(200) COMMENT '合作url',
    private Long folderId;//BIGINT(20) UNSIGNED  COMMENT '频道',
    private Long productId;// BIGINT(20) UNSIGNED  COMMENT '产品',
    private Integer status;// INT(10) UNSIGNED NOT NULL COMMENT '状态',

    private String description;// VARCHAR(255) DEFAULT NULL COMMENT '描述',
    private String createBy;//VARCHAR(60) NOT NULL COMMENT '创建人',
    private Date createDate;// DATETIME NOT NULL COMMENT '创建日期',
    private String modBy;// VARCHAR(60) NOT NULL COMMENT '更新人',
    private Date modDate;// DATETIME NOT NULL COMMENT '更新日期',
    private Long iconId;//广告ICON
    
    private Integer siteId;
    
    /*关联关系，均为多对一*/
    private transient Picture picture;//not null
    private transient Picture icon;//not null
    private transient Folder folder;
    private transient ProductList product;
    
    private Site site;
    
    public Advertise() {
    }

    public Advertise(Long primaryKey) {
        this();
        setPrimaryKey(primaryKey);
    }

    /*主键*/
	@Transient
	@XmlTransient
	public Long getPrimaryKey() {
		return getAdvertiseId();
	}

	public void setPrimaryKey(Long advertiseId) {
		setAdvertiseId(advertiseId);
	}
   
    
	@Column(name = "advertise_id", nullable = false, unique = true, precision = 20)
	@GeneratedValue
	@Id
	public Long getAdvertiseId() {
		return advertiseId;
	}

	public void setAdvertiseId(Long advertiseId) {
		this.advertiseId = advertiseId;
	}

	@NotBlank
	@Length(max = 200)
	@Column(nullable = false, length = 200)
	public String getName() {
		return name;//不允许为空
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "picture_id", nullable = false, precision = 20,insertable = false, updatable = false)
	public Long getPictureId() {
		return pictureId;
	}

	public void setPictureId(Long pictureId) {
		this.pictureId = pictureId;
	}

	@Column(name = "icon_id", nullable = false, precision = 20,insertable = false, updatable = false)
	public Long getIconId() {
		return iconId;
	}

	public void setIconId(Long iconId) {
		this.iconId = iconId;
	}

	@Column(name = "type", nullable = false, precision = 10)
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Length(max=200)
	@Column(name = "url", length=200)
	public String getUrl() {
		return url;//可空
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "folder_id", precision = 20,insertable = false, updatable = false)
	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}

	@Column(name = "product_id", precision = 20,insertable = false, updatable = false)
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	@Column(name = "status", nullable = false, precision = 10)
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Length(max=255)
	@Column(name = "description", nullable = false, length=255)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    @Length(max = 60)
    @Column(name = "create_by", nullable = false, length = 60)
	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

    @Column(name = "create_date", nullable = false, length = 19)
    @Temporal(TIMESTAMP)
    @DateTimeFormat(iso = ISO.DATE)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

    @Length(max = 60)
    @Column(name = "mod_by", nullable = false, length = 60)
	public String getModBy() {
		return modBy;
	}

	public void setModBy(String modBy) {
		this.modBy = modBy;
	}

    @Column(name = "mod_date", nullable = false, length = 19)
    @Temporal(TIMESTAMP)
    @DateTimeFormat(iso = ISO.DATE)
	public Date getModDate() {
		return modDate;
	}

	public void setModDate(Date modDate) {
		this.modDate = modDate;
	}

	@JoinColumn(name = "picture_id", nullable = false)
	@ManyToOne(cascade = PERSIST, fetch = LAZY)
	@JsonIgnore
	public Picture getPicture() {
		return picture;
	}

	public void setPicture(Picture picture) {
		this.picture = picture;
		
		if(picture!=null){
			this.setPictureId(picture.getPictureId());
		}else {
			setPictureId(null);
		}
	}
	
	@JoinColumn(name = "icon_id", nullable = true)
	@ManyToOne(cascade = PERSIST, fetch = LAZY)
	@JsonIgnore
	public Picture getIcon() {
		return icon;
	}

	public void setIcon(Picture icon) {
		this.icon = icon;
		
		if(icon!=null){
			this.setIconId(icon.getPictureId());
		}else {
			setIconId(null);
		}
	}
	
    @JoinColumn(name = "folder_id")//可空
    @ManyToOne(cascade = PERSIST, fetch = LAZY)
    @JsonIgnore
	public Folder getFolder() {
		return folder;
	}

	public void setFolder(Folder folder) {
		this.folder = folder;
		
		if(folder!=null){
			this.setFolderId(folder.getFolderId());
		}else {
			setFolderId(null);
		}
	}

    @JoinColumn(name = "product_id")
    @ManyToOne(cascade = PERSIST, fetch = LAZY)
    @JsonIgnore
	public ProductList getProduct() {
		return product;
	}

	public void setProduct(ProductList product) {
		this.product = product;
		
		if(product!=null){
			this.setProductId(product.getProductId());
		}else{
			setProductId(null);
		}
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
	
	

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static Logger getLogger() {
		return logger;
	}

	@Override
	public Advertise copy() {
		// TODO Auto-generated method stub
		Advertise advertise = new Advertise();
        copyTo(advertise);
        return advertise;
	}

	@Override
	public void copyTo(Advertise advertise) {
		// TODO Auto-generated method stub
		advertise.setAdvertiseId(getAdvertiseId());
		advertise.setName(getName());
		advertise.setPictureId(getPictureId());
		advertise.setType(getType());
		advertise.setUrl(getUrl());
		advertise.setFolderId(getFolderId());
		advertise.setProductId(getProductId());
		advertise.setStatus(getStatus());
		
		advertise.setDescription(getDescription());
		advertise.setCreateBy(getCreateBy());
		advertise.setCreateDate(getCreateDate());
		advertise.setModBy(getModBy());
		advertise.setModDate(getModDate());
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
	public String toString() {
		// TODO Auto-generated method stub
        StringBuilder result = new StringBuilder();
        result.append("advertise.advertiseId=[").append(this.getAdvertiseId()).append("]\n");
        result.append("advertise.name=[").append(getName()).append("]\n");
        result.append("advertise.pictureId=[").append(getPictureId()).append("]\n");
        result.append("advertise.type=[").append(getType()).append("]\n");
        result.append("advertise.url=[").append(getUrl()).append("]\n");
        result.append("advertise.folderId=[").append(getFolderId()).append("]\n");
        result.append("advertise.productId=[").append(getProductId()).append("]\n");
        result.append("advertise.status=[").append(getStatus()).append("]\n");
        result.append("advertise.description=[").append(getDescription()).append("]\n");
        result.append("advertise.createBy=[").append(getCreateBy()).append("]\n");
        result.append("advertise.createDate=[").append(getCreateDate()).append("]\n");
        result.append("advertise.modBy=[").append(getModBy()).append("]\n");
        result.append("advertise.modDate=[").append(getModDate()).append("]\n");

        return result.toString();
	}

	@Override
	public boolean equals(Object advertise) {
		if (this == advertise) {
			return true;
		}

		if (!(advertise instanceof Advertise)) {
			return false;
		}

		Advertise other = (Advertise) advertise;
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

	public enum AdvertiseTypeEnum {
    	ExtUrl, Folder, Product;
    	public static AdvertiseTypeEnum getInstance(Integer value) {
    		if(null != value) {
    			AdvertiseTypeEnum[] types = AdvertiseTypeEnum.values();
	    		for(AdvertiseTypeEnum type : types) {
	    			if(type.ordinal() == value.intValue()) {
	    				return type;
	    			}
	    		}
    		}
    		return null;
    	}
	}

	@Transient
	@JsonIgnore
	public boolean isEnable() {
		if(null != this.getStatus()) {
			AdvertiseStatusEnum sts = AdvertiseStatusEnum.getInstance(this.getStatus());
			if(null != sts && sts == AdvertiseStatusEnum.Enable) {
				return true;
			}
		}
		return false;
	}
	
	public enum AdvertiseStatusEnum {
    	Enable, Disable;
    	public static AdvertiseStatusEnum getInstance(Integer value) {
    		if(null != value) {
    			AdvertiseStatusEnum[] types = AdvertiseStatusEnum.values();
	    		for(AdvertiseStatusEnum type : types) {
	    			if(type.ordinal() == value.intValue()) {
	    				return type;
	    			}
	    		}
    		}
    		return null;
    	}
	}
}
