package org.visitor.appportal.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Embeddable
public class PageContainerPk implements Comparable<PageContainerPk>, Serializable {
    static final private long serialVersionUID = 1L;

    //--------------------------------------------
    // Primary key columns
    //--------------------------------------------
    private Long containerId;
    private Long pageId;

    public PageContainerPk() {
    }

    public PageContainerPk(Long containerId, Long pageId) {
        this.containerId = containerId;
        this.pageId = pageId;
    }

    /**
     * Helper to determine if this composite primary key can be considered as set or not.
     */
    @Transient
    public boolean isPageContainerPkSet() {
        return isContainerIdSet() && isPageIdSet();
    }

    /**
     * Helper method to determine if this instance is considered empty, that is,
     * if all the non primary key columns are null.
     */
    @Transient
    public boolean isEmpty() {
        return !isContainerIdSet() && !isPageIdSet();
    }

    //--------------------------------------------
    // Getters & Setters
    //--------------------------------------------

    //-- [containerId] ------------------------------

    @Column(name = "container_id", nullable = false, precision = 20, insertable = false, updatable = false)
    public Long getContainerId() {
        return containerId;
    }

    public void setContainerId(Long containerId) {
        this.containerId = containerId;
    }

    /**
     * Helper method to set the containerId attribute via an int.
     * @see #setContainerId(Long)
     */
    public void setContainerId(int containerId) {
        this.containerId = Long.valueOf(containerId);
    }

    /**
     * Helper that determines if this attribute is set or not.
     */
    @Transient
    public boolean isContainerIdSet() {
        return getContainerId() != null;
    }

    //-- [pageId] ------------------------------

    @Column(name = "page_id", nullable = false, precision = 20, insertable = false, updatable = false)
    public Long getPageId() {
        return pageId;
    }

    public void setPageId(Long pageId) {
        this.pageId = pageId;
    }

    /**
     * Helper method to set the pageId attribute via an int.
     * @see #setPageId(Long)
     */
    public void setPageId(int pageId) {
        this.pageId = Long.valueOf(pageId);
    }

    /**
     * Helper that determines if this attribute is set or not.
     */
    @Transient
    public boolean isPageIdSet() {
        return getPageId() != null;
    }

    //-----------------------------------------------
    // equals & hashCode
    //-----------------------------------------------

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof PageContainerPk)) {
            return false;
        }

        PageContainerPk other = (PageContainerPk) object;
        return new EqualsBuilder().append(getContainerId(), other.getContainerId()).append(getPageId(),
                other.getPageId()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getContainerId()).append(getPageId()).toHashCode();
    }

    //-----------------------------------------------
    // Comparable
    //-----------------------------------------------

    public int compareTo(PageContainerPk other) {
        return new CompareToBuilder().append(getContainerId(), other.getContainerId()).append(getPageId(),
                other.getPageId()).toComparison();
    }

    //-----------------------------------------------
    // toString / from String
    //-----------------------------------------------

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        if (isContainerIdSet()) {
            result.append(getContainerId());
        }

        result.append(":");

        if (isPageIdSet()) {
            result.append(getPageId());
        }

        return result.toString();
    }

    /**
     * Build an instance from a string version.
     */
    public static PageContainerPk fromString(String string) {
        PageContainerPk result = new PageContainerPk();
        String[] values = string.split(":");
        if (values[0] != null && values[0].length() > 0) {
            result.setContainerId(new Long(values[0]));
        }
        if (values[1] != null && values[1].length() > 0) {
            result.setPageId(new Long(values[1]));
        }

        return result;
    }
}
