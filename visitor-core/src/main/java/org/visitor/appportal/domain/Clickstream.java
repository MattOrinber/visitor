package org.visitor.appportal.domain;

import static javax.persistence.TemporalType.TIMESTAMP;
import static org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "clickstream")
@Cache(usage = NONSTRICT_READ_WRITE)
public class Clickstream implements Serializable, Copyable<Clickstream> {
    static final private long serialVersionUID = 1L;
    static final private Logger logger = LoggerFactory.getLogger(Clickstream.class);

    // Raw attributes
    private Long clickstreamId; // pk
    private String cookieId;
    private Long userId;
    private Date startDate; // not null
    private Date endDate; // not null
    private String referer;
    private String remoteAddress; // not null
    private String remoteHostName; // not null
    private String userAgent; // not null
    private Integer siteId; // not null
    private Long lastPageId; // not null
    private Long firstPageId; // not null
    private String operateSystem; // not null
    private String browserName; // not null
    private String browserVersion; // not null
    private String browserFullVersion; // not null
    private Boolean mobileDevice; // not null
	private Date lastRequested;//not null
    private Date lastSaved; // not null
    private String phoneBrand;
    private String phoneSeries;
    private String uid;
    private String imei;
    private String msisdn;
    private String imsi;
    private String channelId;
    private String geoData;
    private Boolean ifNewUser; // not null
    private Boolean ifKeepUser; // not null
    private Integer numberOfRequests;
	private String hostName;
	private Boolean bot;
	
    private List<ClickstreamRequest> clickstreamRequests;
	
    // ---------------------------
    // Identifiable implementation
    // ---------------------------

    @Transient
    @JsonIgnore
    public Long getPrimaryKey() {
        return getClickstreamId();
    }

    public void setPrimaryKey(Long clickstreamId) {
        setClickstreamId(clickstreamId);
    }
    // -------------------------------
    // Getter & Setter
    // -------------------------------

    // -- [clickstreamId] ------------------------

    @Column(name = "clickstream_id", nullable = false, unique = true, precision = 20)
    @GeneratedValue
    @Id
    public Long getClickstreamId() {
        return clickstreamId;
    }

    public void setClickstreamId(Long clickstreamId) {
        this.clickstreamId = clickstreamId;
    }

    // -- [cookieId] ------------------------

    @Length(max = 120)
    @Column(name = "cookie_id", length = 120)
    public String getCookieId() {
        return cookieId;
    }

    public void setCookieId(String cookieId) {
        this.cookieId = cookieId;
    }

    // -- [userId] ------------------------

    @Column(name = "user_id", precision = 20)
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    // -- [startDate] ------------------------

    @NotNull
    @Column(name = "start_date", nullable = false, length = 19)
    @Temporal(TIMESTAMP)
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    // -- [endDate] ------------------------

    @NotNull
    @Column(name = "end_date", nullable = false, length = 19)
    @Temporal(TIMESTAMP)
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    // -- [referer] ------------------------

    @Length(max = 255)
    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    // -- [remoteAddress] ------------------------

    @NotEmpty
    @Length(max = 60)
    @Column(name = "remote_address", nullable = false, length = 60)
    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    // -- [remoteHostName] ------------------------

    @NotEmpty
    @Length(max = 45)
    @Column(name = "remote_host_name", nullable = false, length = 45)
    public String getRemoteHostName() {
        return remoteHostName;
    }

    public void setRemoteHostName(String remoteHostName) {
        this.remoteHostName = remoteHostName;
    }

    // -- [userAgent] ------------------------

    @NotEmpty
    @Length(max = 255)
    @Column(name = "user_agent", nullable = false)
    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    // -- [siteId] ------------------------

    @NotNull
    @Column(name = "site_id", nullable = false, precision = 10)
    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    // -- [lastPageId] ------------------------

    @NotNull
    @Column(name = "last_page_id", nullable = false, precision = 20)
    public Long getLastPageId() {
        return lastPageId;
    }

    public void setLastPageId(Long lastPageId) {
        this.lastPageId = lastPageId;
    }

    // -- [firstPageId] ------------------------

    @NotNull
    @Column(name = "first_page_id", nullable = false, precision = 20)
    public Long getFirstPageId() {
        return firstPageId;
    }

    public void setFirstPageId(Long firstPageId) {
        this.firstPageId = firstPageId;
    }

    // -- [operateSystem] ------------------------

    @NotEmpty
    @Length(max = 45)
    @Column(name = "operate_system", nullable = false, length = 45)
    public String getOperateSystem() {
        return operateSystem;
    }

    public void setOperateSystem(String operateSystem) {
        this.operateSystem = operateSystem;
    }

    // -- [browserName] ------------------------

    @NotEmpty
    @Length(max = 60)
    @Column(name = "browser_name", nullable = false, length = 60)
    public String getBrowserName() {
        return browserName;
    }

    public void setBrowserName(String browserName) {
        this.browserName = browserName;
    }

    // -- [browserVersion] ------------------------

    @NotEmpty
    @Length(max = 10)
    @Column(name = "browser_version", nullable = false, length = 10)
    public String getBrowserVersion() {
        return browserVersion;
    }

    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
    }

    // -- [mobileDevice] ------------------------

    /**
	 * @return the browserFullVersion
	 */
    @Length(max = 20)
    @Column(name = "browser_full_version", length = 20)
	public String getBrowserFullVersion() {
		return browserFullVersion;
	}

	/**
	 * @param browserFullVersion the browserFullVersion to set
	 */
	public void setBrowserFullVersion(String browserFullVersion) {
		this.browserFullVersion = browserFullVersion;
	}

	@NotNull
    @Column(name = "mobile_device", nullable = false, length = 0)
    public Boolean getMobileDevice() {
        return mobileDevice;
    }

    public void setMobileDevice(Boolean mobileDevice) {
        this.mobileDevice = mobileDevice;
    }

    // -- [lastSaved] ------------------------

    @NotNull
    @Column(name = "last_saved", nullable = false, length = 19)
    @Temporal(TIMESTAMP)
    public Date getLastSaved() {
        return lastSaved;
    }

    public void setLastSaved(Date lastSaved) {
        this.lastSaved = lastSaved;
    }

    // -- [phoneBrand] ------------------------

    @Length(max = 45)
    @Column(name = "phone_brand", length = 45)
    public String getPhoneBrand() {
        return phoneBrand;
    }

    public void setPhoneBrand(String phoneBrand) {
        this.phoneBrand = phoneBrand;
    }

    // -- [phoneSeries] ------------------------

    @Length(max = 45)
    @Column(name = "phone_series", length = 45)
    public String getPhoneSeries() {
        return phoneSeries;
    }

    public void setPhoneSeries(String phoneSeries) {
        this.phoneSeries = phoneSeries;
    }

    // -- [uid] ------------------------

    @Length(max = 40)
    @Column(length = 40)
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    // -- [imei] ------------------------

    @Length(max = 45)
    @Column(length = 45)
    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    // -- [msisdn] ------------------------

    @Length(max = 45)
    @Column(length = 45)
    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    // -- [imsi] ------------------------

    @Length(max = 45)
    @Column(length = 45)
    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    // -- [channelId] ------------------------

    @Length(max = 12)
    @Column(name = "channel_id", length = 12)
    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    // -- [geoData] ------------------------

    @Length(max = 255)
    @Column(name = "geo_data")
    public String getGeoData() {
        return geoData;
    }

    public void setGeoData(String geoData) {
        this.geoData = geoData;
    }

    // -- [ifNewUser] ------------------------

    @NotNull
    @Column(name = "if_new_user", nullable = false, length = 0)
    public Boolean getIfNewUser() {
        return ifNewUser;
    }

    public void setIfNewUser(Boolean ifNewUser) {
        this.ifNewUser = ifNewUser;
    }

    // -- [ifKeepUser] ------------------------

    @NotNull
    @Column(name = "if_keep_user", nullable = false, length = 0)
    public Boolean getIfKeepUser() {
        return ifKeepUser;
    }

    public void setIfKeepUser(Boolean ifKeepUser) {
        this.ifKeepUser = ifKeepUser;
    }

    //-- [numberOfRequests] ------------------------

    @Column(name = "number_of_requests", precision = 10)
    public Integer getNumberOfRequests() {
        return numberOfRequests;
    }

    public void setNumberOfRequests(Integer numberOfRequests) {
        this.numberOfRequests = numberOfRequests;
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
    public boolean equals(Object clickstream) {
        if (this == clickstream) {
            return true;
        }

        if (!(clickstream instanceof Clickstream)) {
            return false;
        }

        Clickstream other = (Clickstream) clickstream;
        return _getUid().equals(other._getUid());
    }

    @Override
    public int hashCode() {
        return _getUid().hashCode();
    }

    private Object _uid;
	private ClickstreamRequest lastClickstreamRequest;

    private Object _getUid() {
        if (_uid == null) {
            if (null != this.getClickstreamId()) {
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
     * Construct a readable string representation for this Clickstream instance.
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("clickstream.clickstreamId=[").append(getClickstreamId()).append("]\n");
        result.append("clickstream.cookieId=[").append(getCookieId()).append("]\n");
        result.append("clickstream.userId=[").append(getUserId()).append("]\n");
        result.append("clickstream.startDate=[").append(getStartDate()).append("]\n");
        result.append("clickstream.endDate=[").append(getEndDate()).append("]\n");
        result.append("clickstream.referer=[").append(getReferer()).append("]\n");
        result.append("clickstream.remoteAddress=[").append(getRemoteAddress()).append("]\n");
        result.append("clickstream.remoteHostName=[").append(getRemoteHostName()).append("]\n");
        result.append("clickstream.userAgent=[").append(getUserAgent()).append("]\n");
        result.append("clickstream.siteId=[").append(getSiteId()).append("]\n");
        result.append("clickstream.lastPageId=[").append(getLastPageId()).append("]\n");
        result.append("clickstream.firstPageId=[").append(getFirstPageId()).append("]\n");
        result.append("clickstream.operateSystem=[").append(getOperateSystem()).append("]\n");
        result.append("clickstream.browserName=[").append(getBrowserName()).append("]\n");
        result.append("clickstream.browserVersion=[").append(getBrowserVersion()).append("]\n");
        result.append("clickstream.mobileDevice=[").append(getMobileDevice()).append("]\n");
        result.append("clickstream.lastSaved=[").append(getLastSaved()).append("]\n");
        result.append("clickstream.phoneBrand=[").append(getPhoneBrand()).append("]\n");
        result.append("clickstream.phoneSeries=[").append(getPhoneSeries()).append("]\n");
        result.append("clickstream.uid=[").append(getUid()).append("]\n");
        result.append("clickstream.imei=[").append(getImei()).append("]\n");
        result.append("clickstream.msisdn=[").append(getMsisdn()).append("]\n");
        result.append("clickstream.imsi=[").append(getImsi()).append("]\n");
        result.append("clickstream.channelId=[").append(getChannelId()).append("]\n");
        result.append("clickstream.geoData=[").append(getGeoData()).append("]\n");
        result.append("clickstream.ifNewUser=[").append(getIfNewUser()).append("]\n");
        result.append("clickstream.ifKeepUser=[").append(getIfKeepUser()).append("]\n");
        return result.toString();
    }

	@Override
	public Clickstream copy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void copyTo(Clickstream t) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return the lastRequestDate
	 */
    @NotNull
    @Column(name = "last_requested", nullable = false, length = 19)
    @Temporal(TIMESTAMP)
	public Date getLastRequested() {
		return lastRequested;
	}

	/**
	 * @param lastRequested the lastRequested to set
	 */
	public void setLastRequested(Date lastRequested) {
		this.lastRequested = lastRequested;
	}

	@NotEmpty
    @Length(max = 45)
    @Column(name = "host_name", nullable = false, length = 45)
	public String getHostName() {
		return hostName;
	}

	/**
	 * @param hostName the hostName to set
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

    //-- [bot] ------------------------

    @Column(length = 1)
    public Boolean getBot() {
        return bot;
    }

    public void setBot(Boolean bot) {
        this.bot = bot;
    }
    
	@Transient
	public List<ClickstreamRequest> getClickstreamRequests() {
		return this.clickstreamRequests;
	}

	public void addClickstreamRequest(ClickstreamRequest clickstreamRequest) {
		if(null == this.clickstreamRequests) {
			this.clickstreamRequests = new ArrayList<ClickstreamRequest>();
		}
		clickstreamRequests.add(clickstreamRequest);
		
		//add relationship between clickstream and clickstream request
		if(null == clickstreamRequest.getClickstream()) {
			clickstreamRequest.setClickstream(this);
		}
		this.lastClickstreamRequest = clickstreamRequest;
	}

	@Transient
	public ClickstreamRequest getLastClickstreamRequest() {
		return this.lastClickstreamRequest;
	}
}