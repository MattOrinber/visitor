package org.visitor.appportal.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "product_state")
public class ProductState implements Serializable, Copyable<ProductState> {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(ProductState.class);

    // Raw attributes
    private Long totalPv;
    private Long totalUv;
    private Long totalDl;
    private Long weekPv;
    private Long weekUv;
    private Long weekDl;
    private Long monthPv;
    private Long monthUv;
    private Long monthDl;
    private Long dailyPv;
    private Long dailyUv;
    private Long dailyDl;

    // Technical attributes for query by example
    private Long productId; // pk

    // ---------------------------
    // Constructors
    // ---------------------------

    public ProductState() {
    }

    public ProductState(Long primaryKey) {
        this();
        setPrimaryKey(primaryKey);
    }

    // ---------------------------
    // Identifiable implementation
    // ---------------------------

    @Transient
    @XmlTransient
    @JsonIgnore
    public Long getPrimaryKey() {
        return getProductId();
    }

    public void setPrimaryKey(Long productId) {
        setProductId(productId);
    }

    // -------------------------------
    // Getter & Setter
    // -------------------------------

    // -- [productId] ------------------------

    @Column(name = "product_id", nullable = false, unique = true, precision = 20)
    @Id
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
    // -- [totalPv] ------------------------

    @Column(name = "total_pv", precision = 20)
    public Long getTotalPv() {
        return totalPv;
    }

    public void setTotalPv(Long totalPv) {
        this.totalPv = totalPv;
    }

    // -- [totalUv] ------------------------

    @Column(name = "total_uv", precision = 20)
    public Long getTotalUv() {
        return totalUv;
    }

    public void setTotalUv(Long totalUv) {
        this.totalUv = totalUv;
    }


    // -- [totalDl] ------------------------

    @Column(name = "total_dl", precision = 20)
    public Long getTotalDl() {
        return totalDl;
    }

    public void setTotalDl(Long totalDl) {
        this.totalDl = totalDl;
    }

    // -- [weekPv] ------------------------

    @Column(name = "week_pv", precision = 20)
    public Long getWeekPv() {
        return weekPv;
    }

    public void setWeekPv(Long weekPv) {
        this.weekPv = weekPv;
    }


    // -- [weekUv] ------------------------
    @Column(name = "week_uv", precision = 20)
    public Long getWeekUv() {
        return weekUv;
    }

    public void setWeekUv(Long weekUv) {
        this.weekUv = weekUv;
    }

    // -- [weekDl] ------------------------
    @Column(name = "week_dl", precision = 20)
    public Long getWeekDl() {
        return weekDl;
    }

    public void setWeekDl(Long weekDl) {
        this.weekDl = weekDl;
    }

    // -- [monthPv] ------------------------
    @Column(name = "month_pv", precision = 20)
    public Long getMonthPv() {
        return monthPv;
    }

    public void setMonthPv(Long monthPv) {
        this.monthPv = monthPv;
    }

    // -- [monthUv] ------------------------
    @Column(name = "month_uv", precision = 20)
    public Long getMonthUv() {
        return monthUv;
    }

    public void setMonthUv(Long monthUv) {
        this.monthUv = monthUv;
    }

    // -- [monthDl] ------------------------
    @Column(name = "month_dl", precision = 20)
    public Long getMonthDl() {
        return monthDl;
    }

    public void setMonthDl(Long monthDl) {
        this.monthDl = monthDl;
    }
    
    // -- [dailyPv] ------------------------
    @Column(name = "daily_pv", precision = 20)
    public Long getDailyPv() {
        return dailyPv;
    }

    public void setDailyPv(Long dailyPv) {
        this.dailyPv = dailyPv;
    }

    // -- [dailyUv] ------------------------
    @Column(name = "daily_uv", precision = 20)
    public Long getDailyUv() {
        return dailyUv;
    }

    public void setDailyUv(Long dailyUv) {
        this.dailyUv = dailyUv;
    }

    // -- [dailyDl] ------------------------
    @Column(name = "daily_dl", precision = 20)
    public Long getDailyDl() {
        return dailyDl;
    }

    public void setDailyDl(Long dailyDl) {
        this.dailyDl = dailyDl;
    }

    // -----------------------------------------
    // Set defaults values
    // -----------------------------------------

    /**
     * Set the default values.
     */
    public void initDefaultValues() {
        setTotalPv(0l);
        setTotalUv(0l);
        setTotalDl(0l);
        setWeekPv(0l);
        setWeekUv(0l);
        setWeekDl(0l);
        setMonthPv(0l);
        setMonthUv(0l);
        setMonthDl(0l);
        setDailyPv(0l);
        setDailyUv(0l);
        setDailyDl(0l);
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
    public boolean equals(Object productState) {
        if (this == productState) {
            return true;
        }

        if (!(productState instanceof ProductState)) {
            return false;
        }

        ProductState other = (ProductState) productState;
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
     * Construct a readable string representation for this ProductState instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("productState.productId=[").append(getProductId()).append("]\n");
        result.append("productState.totalPv=[").append(getTotalPv()).append("]\n");
        result.append("productState.totalUv=[").append(getTotalUv()).append("]\n");
        result.append("productState.totalDl=[").append(getTotalDl()).append("]\n");
        result.append("productState.weekPv=[").append(getWeekPv()).append("]\n");
        result.append("productState.weekUv=[").append(getWeekUv()).append("]\n");
        result.append("productState.weekDl=[").append(getWeekDl()).append("]\n");
        result.append("productState.monthPv=[").append(getMonthPv()).append("]\n");
        result.append("productState.monthUv=[").append(getMonthUv()).append("]\n");
        result.append("productState.monthDl=[").append(getMonthDl()).append("]\n");
        result.append("productState.dailyPv=[").append(getDailyPv()).append("]\n");
        result.append("productState.dailyUv=[").append(getDailyUv()).append("]\n");
        result.append("productState.dailyDl=[").append(getDailyDl()).append("]\n");
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
    public ProductState copy() {
        ProductState productState = new ProductState();
        copyTo(productState);
        return productState;
    }

    /**
     * Copy the current properties to the given object
     */
    @Override
    public void copyTo(ProductState productState) {
        productState.setProductId(getProductId());
        productState.setTotalPv(getTotalPv());
        productState.setTotalUv(getTotalUv());
        productState.setTotalDl(getTotalDl());
        productState.setWeekPv(getWeekPv());
        productState.setWeekUv(getWeekUv());
        productState.setWeekDl(getWeekDl());
        productState.setMonthPv(getMonthPv());
        productState.setMonthUv(getMonthUv());
        productState.setMonthDl(getMonthDl());
        productState.setDailyPv(getDailyPv());
        productState.setDailyUv(getDailyUv());
        productState.setDailyDl(getDailyDl());
    }

}