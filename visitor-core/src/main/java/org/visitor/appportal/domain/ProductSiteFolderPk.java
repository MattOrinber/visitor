package org.visitor.appportal.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;

@Embeddable
public class ProductSiteFolderPk implements Comparable<ProductSiteFolderPk>, Serializable {
    static final private long serialVersionUID = 1L;

    //--------------------------------------------
    // Primary key columns
    //--------------------------------------------
    private Long productId;
    private Integer siteId;

    public ProductSiteFolderPk() {
    }

    public ProductSiteFolderPk(Long productId, Integer siteId) {
        this.productId = productId;
        this.siteId = siteId;
    }

    /**
     * Helper to determine if this composite primary key can be considered as set or not.
     */
    @Transient
    @JsonIgnore
    public boolean isProductSiteFolderPkSet() {
        return isProductIdSet() && isSiteIdSet();
    }

    /**
     * Helper method to determine if this instance is considered empty, that is,
     * if all the non primary key columns are null.
     */
    @Transient
    @JsonIgnore
    public boolean isEmpty() {
        return !isProductIdSet() && !isSiteIdSet();
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
     * Helper that determines if this attribute is set or not.
     */
    @Transient
    @JsonIgnore
    public boolean isProductIdSet() {
        return getProductId() != null;
    }

    //-- [siteId] ------------------------------

    @Column(name = "site_id", nullable = false, precision = 10, insertable = false, updatable = false)
    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    /**
     * Helper that determines if this attribute is set or not.
     */
    @Transient
    @JsonIgnore
    public boolean isSiteIdSet() {
        return getSiteId() != null;
    }

    //-----------------------------------------------
    // equals & hashCode
    //-----------------------------------------------

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof ProductSiteFolderPk)) {
            return false;
        }

        ProductSiteFolderPk other = (ProductSiteFolderPk) object;
        return new EqualsBuilder().append(getProductId(), other.getProductId()).append(getSiteId(), other.getSiteId())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getProductId()).append(getSiteId()).toHashCode();
    }

    //-----------------------------------------------
    // Comparable
    //-----------------------------------------------

    public int compareTo(ProductSiteFolderPk other) {
        return new CompareToBuilder().append(getProductId(), other.getProductId()).append(getSiteId(),
                other.getSiteId()).toComparison();
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

        if (isSiteIdSet()) {
            result.append(getSiteId());
        }

        return result.toString();
    }

    /**
     * Build an instance from a string version.
     */
    public static ProductSiteFolderPk fromString(String string) {
        ProductSiteFolderPk result = new ProductSiteFolderPk();
        String[] values = string.split(":");
        if (values[0] != null && values[0].length() > 0) {
            result.setProductId(new Long(values[0]));
        }
        if (values[1] != null && values[1].length() > 0) {
            result.setSiteId(new Integer(values[1]));
        }

        return result;
    }
}
