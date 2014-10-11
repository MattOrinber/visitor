package org.visitor.appportal.domain;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;
import static org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClickstreamRequest implements Serializable, Copyable<ClickstreamRequest> {
    static final private long serialVersionUID = 1L;
    static final private Logger logger = LoggerFactory.getLogger(ClickstreamRequest.class);

    // Raw attributes
    private Long requestId; // pk
    private Integer requestOrder; // not null
    private Long previousFolderId;
    private Long previousPageId;
    private Long previousContainerId;
    private Long previousFragmentId;
    private Long currentFolderId;
    private Long currentPageId;
    private String productId;
    private String externalUrl;
	private Integer pageType;
    // Technical attributes for query by example
    private Long clickstreamId; // not null

    // Many to one
    private Clickstream clickstream; // not null (clickstreamId)
	private Integer siteId;
	
	private Integer responseCode;
	private Long searchResultCount;
	private Long responseTime;
	private String themeType;
	
	private Integer pageIndex;//列表页的第几页
	private Integer sequenceNumber;//列表的第几个
	
	public String comeFrom;//来源

	private String width;
	private String height;
	private String platform;
    // ---------------------------
    // Identifiable implementation
    // ---------------------------

    @Transient
    @JsonIgnore
    public Long getPrimaryKey() {
        return getRequestId();
    }

    public void setPrimaryKey(Long requestId) {
        setRequestId(requestId);
    }

    // -------------------------------
    // Getter & Setter
    // -------------------------------

    // -- [requestId] ------------------------

    @Column(name = "request_id", nullable = false, unique = true, precision = 20)
    @GeneratedValue
    @Id
    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    // -- [requestOrder] ------------------------

    @NotNull
    @Column(name = "request_order", nullable = false, precision = 10)
    public Integer getRequestOrder() {
        return requestOrder;
    }

    public void setRequestOrder(Integer requestOrder) {
        this.requestOrder = requestOrder;
    }


    // -- [siteId] ------------------------

    @NotNull
    @Column(name = "site_id", nullable = false, precision = 6)
    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    // -- [previousFolderId] ------------------------

    @Column(name = "previous_folder_id", precision = 20)
    public Long getPreviousFolderId() {
        return previousFolderId;
    }

    public void setPreviousFolderId(Long previousFolderId) {
        this.previousFolderId = previousFolderId;
    }

    // -- [previousPageId] ------------------------

    @Column(name = "previous_page_id", precision = 20)
    public Long getPreviousPageId() {
        return previousPageId;
    }

    public void setPreviousPageId(Long previousPageId) {
        this.previousPageId = previousPageId;
    }

    // -- [previousContainerId] ------------------------

    @Column(name = "previous_container_id", precision = 20)
    public Long getPreviousContainerId() {
        return previousContainerId;
    }

    public void setPreviousContainerId(Long previousContainerId) {
        this.previousContainerId = previousContainerId;
    }


    // -- [previousFragmentId] ------------------------

    @Column(name = "previous_fragment_id", precision = 20)
    public Long getPreviousFragmentId() {
        return previousFragmentId;
    }

    public void setPreviousFragmentId(Long previousFragmentId) {
        this.previousFragmentId = previousFragmentId;
    }

    // -- [currentFolderId] ------------------------

    @Column(name = "current_folder_id", precision = 20)
    public Long getCurrentFolderId() {
        return currentFolderId;
    }

    public void setCurrentFolderId(Long currentFolderId) {
        this.currentFolderId = currentFolderId;
    }

    // -- [currentPageId] ------------------------

    @Column(name = "current_page_id", precision = 20)
    public Long getCurrentPageId() {
        return currentPageId;
    }

    public void setCurrentPageId(Long currentPageId) {
        this.currentPageId = currentPageId;
    }
    
    // -- [productId] ------------------------

    @Column(name = "product_id", precision = 20)
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    // -- [externalUrl] ------------------------

    @Length(max = 500)
    @Column(name = "external_url", length = 500)
    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    // -- [clickstreamId] ------------------------

    @Column(name = "clickstream_id", nullable = false, precision = 20, insertable = false, updatable = false)
    public Long getClickstreamId() {
        return clickstreamId;
    }

    public void setClickstreamId(Long clickstreamId) {
        this.clickstreamId = clickstreamId;
    }

    // --------------------------------------------------------------------
    // Many to One support
    // --------------------------------------------------------------------

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // many-to-one: ClickstreamRequest.clickstreamId ==> Clickstream.clickstreamId
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    @NotNull
    @Cache(usage = NONSTRICT_READ_WRITE)
    @JoinColumn(name = "clickstream_id", nullable = false)
    @ManyToOne(cascade = PERSIST, fetch = LAZY)
    @JsonIgnore
    public Clickstream getClickstream() {
        return clickstream;
    }

    /**
     * Set the clickstream without adding this ClickstreamRequest instance on the passed clickstream
     * If you want to preserve referential integrity we recommend to use
     * instead the corresponding adder method provided by Clickstream
     */
    public void setClickstream(Clickstream clickstream) {
        this.clickstream = clickstream;

        // We set the foreign key property so it can be used by Hibernate search by Example facility.
        if (clickstream != null) {
            setClickstreamId(clickstream.getClickstreamId());
        } else {
            setClickstreamId(null);
        }
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
    public boolean equals(Object clickstreamRequest) {
        if (this == clickstreamRequest) {
            return true;
        }

        if (!(clickstreamRequest instanceof ClickstreamRequest)) {
            return false;
        }

        ClickstreamRequest other = (ClickstreamRequest) clickstreamRequest;
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

    // -----------------------------------------
    // toString
    // -----------------------------------------

    /**
     * Construct a readable string representation for this ClickstreamRequest instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("clickstreamRequest.requestId=[").append(getRequestId()).append("]\n");
        result.append("clickstreamRequest.requestOrder=[").append(getRequestOrder()).append("]\n");
        result.append("clickstreamRequest.siteId=[").append(getSiteId()).append("]\n");
        result.append("clickstreamRequest.previousFolderId=[").append(getPreviousFolderId()).append("]\n");
        result.append("clickstreamRequest.previousPageId=[").append(getPreviousPageId()).append("]\n");
        result.append("clickstreamRequest.previousContainerId=[").append(getPreviousContainerId()).append("]\n");
        result.append("clickstreamRequest.previousFragmentId=[").append(getPreviousFragmentId()).append("]\n");
        result.append("clickstreamRequest.currentFolderId=[").append(getCurrentFolderId()).append("]\n");
        result.append("clickstreamRequest.currentPageId=[").append(getCurrentPageId()).append("]\n");
        result.append("clickstreamRequest.productId=[").append(getProductId()).append("]\n");
        result.append("clickstreamRequest.externalUrl=[").append(getExternalUrl()).append("]\n");
        result.append("clickstreamRequest.clickstreamId=[").append(getClickstreamId()).append("]\n");
        return result.toString();
    }

	@Override
	public ClickstreamRequest copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void copyTo(ClickstreamRequest t) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return the pageType
	 */
    @Column(name = "page_type")
	public Integer getPageType() {
		return pageType;
	}

	/**
	 * @param pageType the pageType to set
	 */
	public void setPageType(Integer pageType) {
		this.pageType = pageType;
	}

	public Integer getResponseCode() {
		return responseCode;
	}

	/**
	 * @param responseCode the responseCode to set
	 */
	public void setResponseCode(Integer responseCode) {
		this.responseCode = responseCode;
	}

	public Long getSearchResultCount() {
		return searchResultCount;
	}

	/**
	 * @param searchResultCount the searchResultCount to set
	 */
	public void setSearchResultCount(Long searchResultCount) {
		this.searchResultCount = searchResultCount;
	}

	public Long getResponseTime() {
		return responseTime;
	}

	/**
	 * @param responseTime the responseTime to set
	 */
	public void setResponseTime(Long responseTime) {
		this.responseTime = responseTime;
	}

	public String getThemeType() {
		return themeType;
	}

	/**
	 * @param themeType the themeType to set
	 */
	public void setThemeType(String themeType) {
		this.themeType = themeType;
	}

	/**
	 * @return the pageIndex
	 */
	public Integer getPageIndex() {
		return pageIndex;
	}

	/**
	 * @param pageIndex the pageIndex to set
	 */
	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	/**
	 * @return the sequenceNumber
	 */
	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * @param sequenceNumber the sequenceNumber to set
	 */
	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * @return the comeFrom
	 */
	public String getComeFrom() {
		return comeFrom;
	}

	/**
	 * @param comeFrom the comeFrom to set
	 */
	public void setComeFrom(String comeFrom) {
		this.comeFrom = comeFrom;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}
}