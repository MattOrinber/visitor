package org.visitor.appportal.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "product_across_recommend", uniqueConstraints = { @UniqueConstraint(columnNames = { "product_id", "service_id", "service_site_id" }) })
public class ProductAcrossRecommend implements Serializable, Copyable<ProductAcrossRecommend> {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(ProductAcrossRecommend.class);
    
    // Composite primary key
    private ProductAcrossRecommendPk productAcrossRecommendPk = new ProductAcrossRecommendPk();

    // Raw attributes
    private String behaviors;

    // Technical attributes for query by example

    // ---------------------------
    // Constructors
    // ---------------------------

    public ProductAcrossRecommend() {
    }

    public ProductAcrossRecommend(ProductAcrossRecommendPk primaryKey) {
        this();
        setPrimaryKey(primaryKey);
    }

    // ---------------------------
    // Identifiable implementation
    // ---------------------------

    @Transient
    @XmlTransient
    public ProductAcrossRecommendPk getPrimaryKey() {
        return getProductAcrossRecommendPk();
    }

    public void setPrimaryKey(ProductAcrossRecommendPk productAcrossRecommendPk) {
        setProductAcrossRecommendPk(productAcrossRecommendPk);
    }

    // -----------------------
    // Composite Primary Key
    // -----------------------

    /**
     * Returns the composite primary key.
     */
    @EmbeddedId
    public ProductAcrossRecommendPk getProductAcrossRecommendPk() {
        return productAcrossRecommendPk;
    }

    /**
     * Set the composite primary key.
     * @param pageContainerPk the composite primary key.
     */
    public void setProductAcrossRecommendPk(ProductAcrossRecommendPk productAcrossRecommendPk) {
        this.productAcrossRecommendPk = productAcrossRecommendPk;
    }

    /**
     * Helper method to set directly the productId into the ProductAcrossRecommendPk corresponding field.
     * todo document $pkAttribute.comment
     * @param productId the productId
     */
    public void setProductId(Long productId) {
        if (getProductAcrossRecommendPk() == null) {
            setProductAcrossRecommendPk(new ProductAcrossRecommendPk());
        }

        getProductAcrossRecommendPk().setProductId(productId);
    }

    /**
     * Helper method to get directly the productId from the entity.root.primaryKey.type corresponding field.
     * @return the productId
     */
    @Transient
    @XmlTransient
    public Long getProductId() {
        return getProductAcrossRecommendPk() != null ? getProductAcrossRecommendPk().getProductId() : null;
    }

    /**
     * Helper method to set directly the serviceId into the ProductAcrossRecommendPk corresponding field.
     * todo document $pkAttribute.comment
     * @param serviceId the serviceId
     */
    public void setServiceId(Integer serviceId) {
        if (getProductAcrossRecommendPk() == null) {
            setProductAcrossRecommendPk(new ProductAcrossRecommendPk());
        }

        getProductAcrossRecommendPk().setServiceId(serviceId);
    }

    /**
     * Helper method to get directly the pageId from the entity.root.primaryKey.type corresponding field.
     * @return the serviceId
     */
    @Transient
    @XmlTransient
    public Integer getServiceId() {
        return getProductAcrossRecommendPk() != null ? getProductAcrossRecommendPk().getServiceId() : null;
    }
    
    /**
     * Helper method to set directly the serviceId into the ProductAcrossRecommendPk corresponding field.
     * todo document $pkAttribute.comment
     * @param serviceId the serviceId
     */
    public void setServiceSiteId(Integer serviceSiteId) {
        if (getProductAcrossRecommendPk() == null) {
            setProductAcrossRecommendPk(new ProductAcrossRecommendPk());
        }

        getProductAcrossRecommendPk().setServiceSiteId(serviceSiteId);
    }

    /**
     * Helper method to get directly the pageId from the entity.root.primaryKey.type corresponding field.
     * @return the serviceId
     */
    @Transient
    @XmlTransient
    public Integer getServiceSiteId() {
        return getProductAcrossRecommendPk() != null ? getProductAcrossRecommendPk().getServiceSiteId() : null;
    }

    // -------------------------------
    // Getter & Setter
    // -------------------------------
    
    // -- [behaviors] ------------------------
    @Length(max = 200)
    @Column(name = "behaviors", length = 200)
    public String getBehaviors() {
        return behaviors;
    }

    public void setBehaviors(String behaviors) {
        this.behaviors = behaviors;
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
    public boolean equals(Object productAutoRecommand) {
        if (this == productAutoRecommand) {
            return true;
        }

        if (!(productAutoRecommand instanceof ProductAcrossRecommend)) {
            return false;
        }

        ProductAcrossRecommend other = (ProductAcrossRecommend) productAutoRecommand;
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
     * Construct a readable string representation for this ProductAutoRecommand instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("productAutoRecommand.productId=[").append(getProductId()).append("]\n");
        result.append("productAutoRecommand.serviceId=[").append(getServiceId()).append("]\n");
        result.append("productAutoRecommand.behaviors=[").append(getBehaviors()).append("]\n");
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
    public ProductAcrossRecommend copy() {
        ProductAcrossRecommend productAutoRecommand = new ProductAcrossRecommend();
        copyTo(productAutoRecommand);
        return productAutoRecommand;
    }

    /**
     * Copy the current properties to the given object
     */
    @Override
    public void copyTo(ProductAcrossRecommend productAutoRecommand) {
    	productAutoRecommand.setProductId(getProductId());
    	productAutoRecommand.setServiceId(getServiceId());
    	productAutoRecommand.setBehaviors(getBehaviors());
    }

}