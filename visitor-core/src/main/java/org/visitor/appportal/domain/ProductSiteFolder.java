package org.visitor.appportal.domain;

import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
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
@Table(name = "product_site_folder", uniqueConstraints = { @UniqueConstraint(columnNames = { "product_id", "site_id" }) })
public class ProductSiteFolder implements Serializable, Copyable<ProductSiteFolder> {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(ProductSiteFolder.class);

    // Composite primary key
    private ProductSiteFolderPk productSiteFolderPk = new ProductSiteFolderPk();

    // Raw attributes
    private transient String createBy; // not null
    private Date createDate; // not null
    private Integer sortOrder;
    // Technical attributes for query by example
    private Long folderId; // not null

    // Many to one
    private transient ProductList product; // not null (productId)
    private transient Site site; // not null (siteId)
    private transient Folder folder; // not null (folderId)

    // ---------------------------
    // Constructors
    // ---------------------------

    public ProductSiteFolder() {
    }

    public ProductSiteFolder(ProductSiteFolderPk primaryKey) {
        this();
        setPrimaryKey(primaryKey);
    }

    // ---------------------------
    // Identifiable implementation
    // ---------------------------

    @Transient
    @XmlTransient
    public ProductSiteFolderPk getPrimaryKey() {
        return getProductSiteFolderPk();
    }

    public void setPrimaryKey(ProductSiteFolderPk productSiteFolderPk) {
        setProductSiteFolderPk(productSiteFolderPk);
    }

    // -----------------------
    // Composite Primary Key
    // -----------------------

    /**
     * Returns the composite primary key.
     */
    @EmbeddedId
    public ProductSiteFolderPk getProductSiteFolderPk() {
        return productSiteFolderPk;
    }

    /**
     * Set the composite primary key.
     * @param productSiteFolderPk the composite primary key.
     */
    public void setProductSiteFolderPk(ProductSiteFolderPk productSiteFolderPk) {
        this.productSiteFolderPk = productSiteFolderPk;
    }

    /**
     * Helper method to set directly the productId into the ProductSiteFolderPk corresponding field.
     * todo document $pkAttribute.comment
     * @param productId the productId
     */
    public void setProductId(Long productId) {
        if (getProductSiteFolderPk() == null) {
            setProductSiteFolderPk(new ProductSiteFolderPk());
        }

        getProductSiteFolderPk().setProductId(productId);
    }

    /**
     * Helper method to get directly the productId from the entity.root.primaryKey.type corresponding field.
     * @return the productId
     */
    @Transient
    @XmlTransient
    public Long getProductId() {
        return getProductSiteFolderPk() != null ? getProductSiteFolderPk().getProductId() : null;
    }

    /**
     * Helper method to set directly the siteId into the ProductSiteFolderPk corresponding field.
     * todo document $pkAttribute.comment
     * @param siteId the siteId
     */
    public void setSiteId(Integer siteId) {
        if (getProductSiteFolderPk() == null) {
            setProductSiteFolderPk(new ProductSiteFolderPk());
        }

        getProductSiteFolderPk().setSiteId(siteId);
    }

    /**
     * Helper method to get directly the siteId from the entity.root.primaryKey.type corresponding field.
     * @return the siteId
     */
    @Transient
    @XmlTransient
    public Integer getSiteId() {
        return getProductSiteFolderPk() != null ? getProductSiteFolderPk().getSiteId() : null;
    }

    // -------------------------------
    // Getter & Setter
    // -------------------------------

    // -- [folderId] ------------------------

    @Column(name = "folder_id", nullable = false, precision = 20, insertable = false, updatable = false)
    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
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


    /**
	 * @return the sortOrder
	 */
    @Column(name = "sort_order")
	public Integer getSortOrder() {
		return sortOrder;
	}

	/**
	 * @param sortOrder the sortOrder to set
	 */
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}
    // --------------------------------------------------------------------
    // Many to One support
    // --------------------------------------------------------------------

	// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // many-to-one: ProductSiteFolder.productId ==> ProductList.productId
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @JoinColumn(name = "product_id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @OrderBy("modDate  DESC")
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
    // many-to-one: ProductSiteFolder.siteId ==> Site.siteId
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @JoinColumn(name = "site_id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    public Site getSite() {
        return site;
    }

    /**
     * Set the site without adding this ProductSiteFolder instance on the passed site
     * If you want to preserve referential integrity we recommend to use
     * instead the corresponding adder method provided by Site
     */
    public void setSite(Site site) {
        this.site = site;

        // We set the foreign key property so it can be used by Hibernate search by Example facility.
        if (site != null) {
            setSiteId(site.getSiteId());
        } // when null, we do not propagate it to the pk.
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // many-to-one: ProductSiteFolder.folderId ==> Folder.folderId
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @NotNull
    @JoinColumn(name = "folder_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
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
    	this.setCreateDate(new Date());
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
    public boolean equals(Object productSiteFolder) {
        if (this == productSiteFolder) {
            return true;
        }

        if (!(productSiteFolder instanceof ProductSiteFolder)) {
            return false;
        }

        ProductSiteFolder other = (ProductSiteFolder) productSiteFolder;
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
     * Construct a readable string representation for this ProductSiteFolder instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("productSiteFolder.folderId=[").append(getFolderId()).append("]\n");
        result.append("productSiteFolder.createBy=[").append(getCreateBy()).append("]\n");
        result.append("productSiteFolder.createDate=[").append(getCreateDate()).append("]\n");
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
    public ProductSiteFolder copy() {
        ProductSiteFolder productSiteFolder = new ProductSiteFolder();
        copyTo(productSiteFolder);
        return productSiteFolder;
    }

    /**
     * Copy the current properties to the given object
     */
    @Override
    public void copyTo(ProductSiteFolder productSiteFolder) {
        productSiteFolder.setProductId(getProductId());
        productSiteFolder.setSiteId(getSiteId());
        //productSiteFolder.setFolderId(getFolderId());
        productSiteFolder.setCreateBy(getCreateBy());
        productSiteFolder.setCreateDate(getCreateDate());
        if (getProduct() != null) {
            productSiteFolder.setProduct(new ProductList(getProduct().getPrimaryKey()));
        }
        if (getSite() != null) {
            productSiteFolder.setSite(new Site(getSite().getPrimaryKey()));
        }
        if (getFolder() != null) {
            productSiteFolder.setFolder(new Folder(getFolder().getPrimaryKey()));
        }
    }
}