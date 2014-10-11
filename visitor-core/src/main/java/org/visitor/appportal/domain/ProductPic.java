package org.visitor.appportal.domain;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.Date;
import java.util.regex.Pattern;

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

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Table(name = "product_pic")
public class ProductPic implements Serializable, Copyable<ProductPic> {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(ProductPic.class);
    public static final Pattern SUFFIX_REG = Pattern.compile("^.*?\\.(jpg|png|gif|JPG|PNG|GIF)$")	;
    /**
     * 0-封面图；1-图标；2-截图
     */
    public static final int COVER=0;
    public static final int ICON=1;
    public static final int PRINT_SCREEN=2;
    
	public static final int ENABLE=0;
	public static final int DISNABLE=1;
    
    // Raw attributes
    private Long productPicId; // pk
    private Integer picType; // not null

    // Technical attributes for query by example
    private Long productId; // not null
//    private Long picId; // not null

    // Many to one
//    private PicList pic; // not null (picId)
	private String picPath; // not null
	private String picSize; // not null
	private String picStyle; // not null
	private String picPixels; // not null
	private Integer status; // not null
	private Date createDate; // not null
	private transient String createBy; // not null
	private String description;
	private transient String jarMd5;

    private transient ProductList product; // not null (productId)

    // ---------------------------
    // Constructors
    // ---------------------------

    public ProductPic() {
    }

    public ProductPic(Integer picType) {
		super();
		this.picType = picType;
	}

	public ProductPic(Long primaryKey) {
        this();
        setPrimaryKey(primaryKey);
    }

    // ---------------------------
    // Identifiable implementation
    // ---------------------------

    @Transient
    @XmlTransient
    public Long getPrimaryKey() {
        return getProductPicId();
    }

    public void setPrimaryKey(Long productPicId) {
        setProductPicId(productPicId);
    }

    // -------------------------------
    // Getter & Setter
    // -------------------------------

    // -- [productPicId] ------------------------

    @Column(name = "product_pic_id", nullable = false, unique = true, precision = 20)
    @GeneratedValue
    @Id
    public Long getProductPicId() {
        return productPicId;
    }

    public void setProductPicId(Long productPicId) {
        this.productPicId = productPicId;
    }

    // -- [productId] ------------------------

    @Column(name = "product_id", nullable = false, precision = 20, insertable = false, updatable = false)
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    // -- [picType] ------------------------

    @NotNull
    @Column(name = "pic_type", nullable = false, precision = 10)
    public Integer getPicType() {
        return picType;
    }

    public void setPicType(Integer picType) {
        this.picType = picType;
    }

    // --------------------------------------------------------------------
    // Many to One support
    // --------------------------------------------------------------------

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // many-to-one: ProductPic.picId ==> PicList.picId
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

//    @NotNull
//    @JoinColumn(name = "pic_id", nullable = false)
//    @ManyToOne(fetch = LAZY)
//    public PicList getPic() {
//        return pic;
//    }
//
//    /**
//     * Set the pic without adding this ProductPic instance on the passed pic
//     * If you want to preserve referential integrity we recommend to use
//     * instead the corresponding adder method provided by PicList
//     */
//    public void setPic(PicList pic) {
//        this.pic = pic;
//
//        // We set the foreign key property so it can be used by Hibernate search by Example facility.
//        if (pic != null) {
//            setPicId(pic.getPicId());
//        } else {
//            setPicId(null);
//        }
//    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // many-to-one: ProductPic.productId ==> ProductList.productId
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @NotNull
    @JoinColumn(name = "product_id", nullable = false)
    @ManyToOne(fetch = LAZY)
    @JsonIgnore
    public ProductList getProduct() {
        return product;
    }

    /**
     * Set the product without adding this ProductPic instance on the passed product
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

	// -- [picPath] ------------------------

	@NotEmpty
	@Length(max = 200)
	@Column(name = "pic_path", nullable = false, length = 200)
	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	// -- [picSize] ------------------------

	@NotEmpty
	@Length(max = 60)
	@Column(name = "pic_size", nullable = false, length = 60)
	public String getPicSize() {
		return picSize;
	}

	public void setPicSize(String picSize) {
		this.picSize = picSize;
	}

	// -- [picStyle] ------------------------

	@NotEmpty
	@Length(max = 20)
	@Column(name = "pic_style", nullable = false, length = 20)
	public String getPicStyle() {
		return picStyle;
	}

	public void setPicStyle(String picStyle) {
		this.picStyle = picStyle;
	}

	// -- [picPixels] ------------------------

	@NotEmpty
	@Length(max = 60)
	@Column(name = "pic_pixels", nullable = false, length = 60)
	public String getPicPixels() {
		return picPixels;
	}

	public void setPicPixels(String picPixels) {
		this.picPixels = picPixels;
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

	// -- [createDate] ------------------------

	@NotNull
	@Column(name = "create_date", nullable = false, length = 19)
	@Temporal(TIMESTAMP)
	@DateTimeFormat(iso = ISO.DATE)
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	// -- [createBy] ------------------------

	@NotEmpty
	@Length(max = 60)
	@Column(name = "create_by", nullable = false, length = 60)
	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	// -- [description] ------------------------

	@Length(max = 255)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    // -- [jarMd5] ------------------------

    @Length(max = 60)
    @Column(name = "md5", length = 60)
    public String getJarMd5() {
        return jarMd5;
    }

    public void setJarMd5(String jarMd5) {
        this.jarMd5 = jarMd5;
    }
    
    // -----------------------------------------
    // Set defaults values
    // -----------------------------------------

    /**
     * Set the default values.
     */
    public void initDefaultValues() {
    	this.setCreateDate(new Date());
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
    public boolean equals(Object productPic) {
        if (this == productPic) {
            return true;
        }

        if (!(productPic instanceof ProductPic)) {
            return false;
        }

        ProductPic other = (ProductPic) productPic;
        return _getUid().equals(other._getUid());
    }

    @Override
    public int hashCode() {
        return _getUid().hashCode();
    }

    private Object _uid;

    private Object _getUid() {
        if (_uid == null) {
            if (null != this.getProductPicId()) {
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
     * Construct a readable string representation for this ProductPic instance.
     * @see java.lang.Object#toString()
     */
	@Override
	public String toString() {
		return "ProductPic [productPicId=" + productPicId + ", picType="
				+ picType + ", productId=" + productId + ", picPath=" + picPath
				+ ", picSize=" + picSize + ", picStyle=" + picStyle
				+ ", picPixels=" + picPixels + ", status=" + status
				+ ", createDate=" + createDate + ", createBy=" + createBy
				+ ", description=" + description + ", jarMd5=" + jarMd5
				+ ", product=" + product + ", _uid=" + _uid + "]";
	}

    // -----------------------------------------
    // Copyable Implementation
    // (Support for REST web layer)
    // -----------------------------------------

    /**
     * Return a copy of the current object
     */
    @Override
    public ProductPic copy() {
        ProductPic productPic = new ProductPic();
        copyTo(productPic);
        return productPic;
    }

	/**
     * Copy the current properties to the given object
     */
    @Override
    public void copyTo(ProductPic productPic) {
        productPic.setProductPicId(getProductPicId());
        //productPic.setProductId(getProductId());
        //productPic.setPicId(getPicId());
        productPic.setPicType(getPicType());
        productPic.setPicPath(getPicPath());
//        if (getPic() != null) {
//            productPic.setPic(new PicList(getPic().getPrimaryKey()));
//        }
        if (getProduct() != null) {
            productPic.setProduct(new ProductList(getProduct().getPrimaryKey()));
        }
    }
    
	/**
	 * 根据id生成相对路径
	 * 
	 * @author mengw
	 * 
	 * @param picId2
	 */
	public static String getPath(Long picId, String suffix) {
		String fileName = StringUtils.leftPad(String.valueOf(picId), 8, "0");
		StringBuffer path = new StringBuffer("/" + fileName);
		for (int i = path.indexOf("/"); i < path.length() - 2; i = path.lastIndexOf("/")) {
			path.insert(i + 3, "/");
		}
		return path + String.valueOf(picId) + "." + suffix;
	}
}