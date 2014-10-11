/*
 * (c) Copyright 2005-2011 JAXIO, www.jaxio.com
 * Source code generated by Celerio, a Jaxio product
 * Want to use Celerio within your company? email us at info@jaxio.com
 * Follow us on twitter: @springfuse
 * Template pack-backend:src/main/java/project/domain/Entity.e.vm.java
 */
package org.visitor.appportal.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "product_site_recommend", uniqueConstraints = { @UniqueConstraint(columnNames = { "product_id", "site_id" }) })
public class ProductSiteRecommend implements Serializable, Copyable<ProductSiteRecommend> {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(ProductSiteRecommend.class);
    
    // Composite primary key
    private ProductSiteRecommendPk productSiteRecommendPk = new ProductSiteRecommendPk();

    // Raw attributes
    private String behaviors1;
    private String behaviors2;
    private String behaviors3;
    private String behaviors4;
    private String behaviors5;
    private String similars;

    // Technical attributes for query by example

    // ---------------------------
    // Constructors
    // ---------------------------

    public ProductSiteRecommend() {
    }

    public ProductSiteRecommend(ProductSiteRecommendPk primaryKey) {
        this();
        setProductSiteRecommendPk(primaryKey);
    }
    
    // ---------------------------
    // Identifiable implementation
    // ---------------------------
    
    @Transient
    @XmlTransient
    @JsonIgnore
    public ProductSiteRecommendPk getPrimaryKey() {
        return getProductSiteRecommendPk();
    }

    public void setPrimaryKey(ProductSiteRecommendPk productSiteRecommendPk) {
        setProductSiteRecommendPk(productSiteRecommendPk);
    }
    
    // -----------------------
    // Composite Primary Key
    // -----------------------

    /**
     * Returns the composite primary key.
     */
    @EmbeddedId
    public ProductSiteRecommendPk getProductSiteRecommendPk() {
        return productSiteRecommendPk;
    }

    /**
     * Set the composite primary key.
     * @param pageContainerPk the composite primary key.
     */
    public void setProductSiteRecommendPk(ProductSiteRecommendPk productSiteRecommendPk) {
        this.productSiteRecommendPk = productSiteRecommendPk;
    }
    
    /**
     * Helper method to set directly the productId into the ProductSiteRecommendPk corresponding field.
     * todo document $pkAttribute.comment
     * @param productId the productId
     */
    public void setProductId(Long productId) {
        if (getProductSiteRecommendPk() == null) {
            setProductSiteRecommendPk(new ProductSiteRecommendPk());
        }

        getProductSiteRecommendPk().setProductId(productId);
    }

    /**
     * Helper method to get directly the productId from the entity.root.primaryKey.type corresponding field.
     * @return the productId
     */
    @Transient
    @XmlTransient
    public Long getProductId() {
        return getProductSiteRecommendPk() != null ? getProductSiteRecommendPk().getProductId() : null;
    }
    
    /**
     * Helper method to set directly the serviceId into the ProductSiteRecommendPk corresponding field.
     * todo document $pkAttribute.comment
     * @param serviceId the serviceId
     */
    public void setSiteId(Integer siteId) {
        if (getProductSiteRecommendPk() == null) {
            setProductSiteRecommendPk(new ProductSiteRecommendPk());
        }

        getProductSiteRecommendPk().setSiteId(siteId);
    }

    /**
     * Helper method to get directly the pageId from the entity.root.primaryKey.type corresponding field.
     * @return the serviceId
     */
    @Transient
    @XmlTransient
    public Integer getSiteId() {
        return getProductSiteRecommendPk() != null ? getProductSiteRecommendPk().getSiteId() : null;
    }

    // -------------------------------
    // Getter & Setter
    // -------------------------------
    
    // -- [behaviors1] ------------------------
    @Length(max = 200)
    @Column(name = "behaviors_1", length = 200)
    public String getBehaviors1() {
        return behaviors1;
    }

    public void setBehaviors1(String behaviors1) {
        this.behaviors1 = behaviors1;
    }

    // -- [behaviors2] ------------------------
    @Length(max = 200)
    @Column(name = "behaviors_2", length = 200)
    public String getBehaviors2() {
        return behaviors2;
    }

    public void setBehaviors2(String behaviors2) {
        this.behaviors2 = behaviors2;
    }


    // -- [behaviors3] ------------------------
    @Length(max = 200)
    @Column(name = "behaviors_3", length = 200)
    public String getBehaviors3() {
        return behaviors3;
    }

    public void setBehaviors3(String behaviors3) {
        this.behaviors3 = behaviors3;
    }

    // -- [behaviors4] ------------------------
    @Length(max = 200)
    @Column(name = "behaviors_4", length = 200)
    public String getBehaviors4() {
        return behaviors4;
    }

    public void setBehaviors4(String behaviors4) {
        this.behaviors4 = behaviors4;
    }

    // -- [behaviors5] ------------------------
    @Length(max = 200)
    @Column(name = "behaviors_5", length = 200)
    public String getBehaviors5() {
        return behaviors5;
    }

    public void setBehaviors5(String behaviors5) {
        this.behaviors5 = behaviors5;
    }
    
    // -- [similars] ------------------------
    @Length(max = 200)
    @Column(name = "similars", length = 200)
    public String getSimilars() {
        return similars;
    }

    public void setSimilars(String similars) {
        this.similars = similars;
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
    public boolean equals(Object productSiteRecommand) {
        if (this == productSiteRecommand) {
            return true;
        }

        if (!(productSiteRecommand instanceof ProductSiteRecommend)) {
            return false;
        }

        ProductSiteRecommend other = (ProductSiteRecommend) productSiteRecommand;
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
     * Construct a readable string representation for this ProductSiteRecommand instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("productSiteRecommand.productId=[").append(getProductId()).append("]\n");
        result.append("productSiteRecommand.siteId=[").append(getSiteId()).append("]\n");
        result.append("productSiteRecommand.behaviors1=[").append(getBehaviors1()).append("]\n");
        result.append("productSiteRecommand.behaviors2=[").append(getBehaviors2()).append("]\n");
        result.append("productSiteRecommand.behaviors3=[").append(getBehaviors3()).append("]\n");
        result.append("productSiteRecommand.behaviors4=[").append(getBehaviors4()).append("]\n");
        result.append("productSiteRecommand.behaviors5=[").append(getBehaviors5()).append("]\n");
        result.append("productSiteRecommand.similars=[").append(getSimilars()).append("]\n");
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
    public ProductSiteRecommend copy() {
        ProductSiteRecommend productSiteRecommand = new ProductSiteRecommend();
        copyTo(productSiteRecommand);
        return productSiteRecommand;
    }

    /**
     * Copy the current properties to the given object
     */
    @Override
    public void copyTo(ProductSiteRecommend productSiteRecommand) {
    	productSiteRecommand.setProductId(getProductId());
    	productSiteRecommand.setSiteId(getSiteId());
    	productSiteRecommand.setBehaviors1(getBehaviors1());
    	productSiteRecommand.setBehaviors2(getBehaviors2());
    	productSiteRecommand.setBehaviors3(getBehaviors3());
    	productSiteRecommand.setBehaviors4(getBehaviors4());
    	productSiteRecommand.setBehaviors5(getBehaviors5());
    	productSiteRecommand.setSimilars(getSimilars());
    }

}