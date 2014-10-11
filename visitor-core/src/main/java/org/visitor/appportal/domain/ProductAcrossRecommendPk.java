package org.visitor.appportal.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Embeddable
public class ProductAcrossRecommendPk implements Comparable<ProductAcrossRecommendPk>, Serializable {
    static final private long serialVersionUID = 1L;

    //--------------------------------------------
    // Primary key columns
    //--------------------------------------------
    private Long productId;
    private Integer serviceId;
    private Integer serviceSiteId;

    public ProductAcrossRecommendPk() {
    }

    public ProductAcrossRecommendPk(Long productId, Integer serviceId, Integer serviceSiteId) {
        this.productId = productId;
        this.serviceId = serviceId;
        this.serviceSiteId = serviceSiteId;
    }

    /**
     * Helper to determine if this composite primary key can be considered as set or not.
     */
    @Transient
    public boolean isProductAcrossRecommendPkSet() {
        return isProductIdSet() && isServiceIdSet();
    }

    /**
     * Helper method to determine if this instance is considered empty, that is,
     * if all the non primary key columns are null.
     */
    @Transient
    public boolean isEmpty() {
        return !isProductIdSet() && !isServiceIdSet();
    }

    //--------------------------------------------
    // Getters & Setters
    //--------------------------------------------

    //-- [productId] ------------------------------

    @Column(name = "product_id", nullable = false, precision = 20, insertable = false, updatable = false)
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /**
     * Helper method to set the productId attribute via an int.
     * @see #setProductId(Long)
     */
    public void setProductId(int productId) {
        this.productId = Long.valueOf(productId);
    }

    /**
     * Helper that determines if this attribute is set or not.
     */
    @Transient
    public boolean isProductIdSet() {
        return getProductId() != null;
    }

    //-- [serviceId] ------------------------------

    @Column(name = "service_id", nullable = false, precision = 20, insertable = false, updatable = false)
    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * Helper that determines if this attribute is set or not.
     */
    @Transient
    public boolean isServiceIdSet() {
        return getServiceId() != null;
    }
    
    //-- [serviceSiteId] ------------------------------

    @Column(name = "service_site_id", nullable = false, precision = 10, insertable = false, updatable = false)
    public Integer getServiceSiteId() {
        return serviceSiteId;
    }

    public void setServiceSiteId(Integer serviceSiteId) {
        this.serviceSiteId = serviceSiteId;
    }

    /**
     * Helper that determines if this attribute is set or not.
     */
    @Transient
    public boolean isServiceSiteIdSet() {
        return getServiceSiteId() != null;
    }

    //-----------------------------------------------
    // equals & hashCode
    //-----------------------------------------------

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof ProductAcrossRecommendPk)) {
            return false;
        }

        ProductAcrossRecommendPk other = (ProductAcrossRecommendPk) object;
        return new EqualsBuilder().append(getProductId(), other.getProductId()).append(getServiceId(),
                other.getServiceId()).append(getServiceSiteId(), other.getServiceSiteId()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getProductId()).append(getServiceId()).append(getServiceSiteId()).toHashCode();
    }

    //-----------------------------------------------
    // Comparable
    //-----------------------------------------------

    public int compareTo(ProductAcrossRecommendPk other) {
        return new CompareToBuilder().append(getProductId(), other.getProductId()).append(getServiceId(),
                other.getServiceId()).append(getServiceSiteId(), other.getServiceSiteId()).toComparison();
    }

    //-----------------------------------------------
    // toString / from String
    //-----------------------------------------------

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        if (isProductIdSet()) {
            result.append(getProductId());
        }

        result.append(":");

        if (isServiceIdSet()) {
            result.append(getServiceId());
        }
        
        result.append(":");

        if (isServiceSiteIdSet()) {
            result.append(getServiceSiteId());
        }

        return result.toString();
    }

    /**
     * Build an instance from a string version.
     */
    public static ProductAcrossRecommendPk fromString(String string) {
        ProductAcrossRecommendPk result = new ProductAcrossRecommendPk();
        String[] values = string.split(":");
        if (values[0] != null && values[0].length() > 0) {
            result.setProductId(new Long(values[0]));
        }
        if (values[1] != null && values[1].length() > 0) {
            result.setServiceId(new Integer(values[1]));
        }
        if (values[2] != null && values[2].length() > 0) {
            result.setServiceSiteId(new Integer(values[2]));
        }
        return result;
    }
}
