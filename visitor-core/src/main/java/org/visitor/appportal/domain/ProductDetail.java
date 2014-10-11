package org.visitor.appportal.domain;

import static javax.persistence.CascadeType.PERSIST;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "product_detail")
public class ProductDetail implements Serializable, Copyable<ProductDetail> {
	/**
	 * 有效
	 */
	public static final int ENABLE = 0;
	/**
	 * 无效
	 */
	public static final int DISABLE = 1;


	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(ProductDetail.class);

	// Raw attributes
	private Long productDetailId; // pk
	private String manualDescription;
	
	private Integer recommendStorage;

	private Long productId;
	private Integer siteId;
	private Integer sortId;
	
	private Integer miniLab;
	private Integer status = ENABLE; // not null	
	private String picPath;
	
	// Many to one
	private transient ProductList productList; 
	private transient Site site;

	// ---------------------------
	// Constructors
	// ---------------------------

	public ProductDetail() {
	}

	public ProductDetail(Long primaryKey) {
		this();
		setPrimaryKey(primaryKey);
	}

	// ---------------------------
	// Identifiable implementation
	// ---------------------------

	@Transient
	@XmlTransient
	public Long getPrimaryKey() {
		return getProductDetailId();
	}

	public void setPrimaryKey(Long productDetailId) {
		setProductDetailId(productDetailId);
	}

	// -------------------------------
	// Getter & Setter
	// -------------------------------

	// -- [productDetailId] ------------------------

	@Column(name = "product_detail_id", nullable = false, unique = true, precision = 20)
	@GeneratedValue
	@Id
	public Long getProductDetailId() {
		return productDetailId;
	}

	public void setProductDetailId(Long productDetailId) {
		this.productDetailId = productDetailId;
	}
	// -- [manualDescription] ------------------------

	@Length(max = 1024)
	@Column(name="manual_description")
	public String getManualDescription() {
		return manualDescription;
	}

	public void setManualDescription(String manualDescription) {
		this.manualDescription = manualDescription;
	}

	@Length(max = 200)
	@Column(name="pic_path")
	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
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

	@NotNull
	@Column(name = "recommend_storage", nullable = false, precision = 10)
	public Integer getRecommendStorage() {
		return recommendStorage;
	}

	public void setRecommendStorage(Integer recommendStorage) {
		this.recommendStorage = recommendStorage;
	}
	
	@Column(name = "sort_id", precision = 10)
	public Integer getSortId() {
		return sortId;
	}

	public void setSortId(Integer sortId) {
		this.sortId = sortId;
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
    @JsonIgnore
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;

        if (site != null) {
            setSiteId(site.getSiteId());
        } else {
            setSiteId(null);
        }
    }

	// -- [siteId] ------------------------

	@Column(name = "product_id", precision = 20, insertable = false, updatable = false)
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

    @JoinColumn(name = "product_id")
    @ManyToOne(cascade = PERSIST)
    @JsonIgnore
    public ProductList getProduct() {
        return productList;
    }

    public void setProduct(ProductList productList) {
        this.productList = productList;

        if (productList != null) {
            setProductId(productList.getProductId());
        } else {
            setProductId(null);
        }
    }
    
	
	@Column(name = "mini_lab", nullable = true, length = 10)
	public Integer getMiniLab() {
		return miniLab;
	}

	public void setMiniLab(Integer miniLab) {
		this.miniLab = miniLab;
	}
	
	public enum RecommendStorageEnum {
		NO,YES
	}

	// -----------------------------------------
	// Set defaults values
	// -----------------------------------------

	/**
	 * Set the default values.
	 */
	public void initDefaultValues() {
		this.setStatus(0);
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
	public boolean equals(Object productDetail) {
		if (this == productDetail) {
			return true;
		}

		if (!(productDetail instanceof ProductDetail)) {
			return false;
		}

		ProductDetail other = (ProductDetail) productDetail;
		return _getUid().equals(other._getUid());
	}

	@Override
	public int hashCode() {
		return _getUid().hashCode();
	}

	private Object _uid;

	private Object _getUid() {
		if (_uid == null) {
			if (null != this.getProductDetailId()) {
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

	@Override
	public String toString() {
		return "ProductDetail [productDetailId=" + productDetailId
				+ ", manualDescription=" + manualDescription
				+ ", recommendStorage=" + recommendStorage + ", productId="
				+ productId + ", siteId=" + siteId + ", miniLab=" + miniLab
				+ ", status=" + status + "]";
	}

	// -----------------------------------------
	// Copyable Implementation
	// (Support for REST web layer)
	// -----------------------------------------

	/**
	 * Return a copy of the current object
	 */
	@Override
	public ProductDetail copy() {
		ProductDetail productDetail = new ProductDetail();
		copyTo(productDetail);
		return productDetail;
	}

	/**
	 * Copy the current properties to the given object
	 */
	@Override
	public void copyTo(ProductDetail productDetail) {
		productDetail.setProductDetailId(getProductDetailId());
		
		productDetail.setManualDescription(getManualDescription());
		productDetail.setMiniLab(getMiniLab());
		productDetail.setStatus(getStatus());
		productDetail.setRecommendStorage(getRecommendStorage());
		productDetail.setSortId(getSortId());
		productDetail.setSite(getSite());
		productDetail.setProduct(getProduct());
		
	}
}
