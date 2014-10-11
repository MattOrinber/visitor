/**
 * 
 */
package org.visitor.app.portal.model;

import org.msgpack.annotation.Ignore;
import org.msgpack.annotation.Message;

import org.visitor.appportal.domain.Site;

/**
 * 用户的一些固定参数。默认为Android/Symbian站点的数据。即apps.oupeng.com域名下的信息
 * 对于games.oupeng.com域名下的信息需要通过另外的方式获取。
 * @author mengw
 *
 */
@Message
public class UserPreference implements java.io.Serializable {
	
	private static final long serialVersionUID = -1873689522146016213L;
	private String uid;
	
	private Long platformId;
	private Long platformVersionId;
	private Long resolutionId;
	private Long screenSize;//屏幕尺寸widthXheight
	private String previousSessionId;//上次访问时的sessionId
	
	private float latitude;//经度，平台版本转换值
	private float longitude;//纬度，分辨率转换值
	private float minPlatformVersionResolution;//用户当前平台版本的最小分辨率
	private float minPlatformVersionNumber;//用户当前平台的最小版本
	
	//之所以改为String，是因为在反序列化的时候，这个会自动反序列化为new Date（）
	//这样的话，每次这个时间都会变化。
	private String firstLoginDate;//第一次登录的时间，用来确定是否新用户
	private String branding;//用户branding，统计用
	private String channelId; //所在渠道
	
	@Ignore
	private transient boolean showPageGame = false;//是否显示页游，不需要持久化，每次请求均会计算
	@Ignore
	private transient Site site;//本次访问的站点，不需要存储了，每次都重新解析吧。无法通用
	@Ignore
	private transient String domain;//当前访问的域名

	/**
	 * 
	 */
	public UserPreference() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the screenSize
	 */
	public Long getScreenSize() {
		return screenSize;
	}

	/**
	 * @param screenSize the screenSize to set
	 */
	public void setScreenSize(Long screenSize) {
		this.screenSize = screenSize;
	}

	/**
	 * @return the previousSessionId
	 */
	public String getPreviousSessionId() {
		return previousSessionId;
	}

	/**
	 * @param previousSessionId the previousSessionId to set
	 */
	public void setPreviousSessionId(String previousSessionId) {
		this.previousSessionId = previousSessionId;
	}

	/**
	 * @return the site
	 */
	public Site getSite() {
		return site;
	}

	/**
	 * @param site the site to set
	 */
	public void setSite(Site site) {
		this.site = site;
	}

	/**
	 * @return the uid
	 */
	public String getUid() {
		return uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(String uid) {
		this.uid = uid;
	}

	/**
	 * @return the latitude
	 */
	public float getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public float getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the minPlatformVersionResolution
	 */
	public float getMinPlatformVersionResolution() {
		return minPlatformVersionResolution;
	}

	/**
	 * @param minPlatformVersionResolution the minPlatformVersionResolution to set
	 */
	public void setMinPlatformVersionResolution(float minPlatformVersionResolution) {
		this.minPlatformVersionResolution = minPlatformVersionResolution;
	}

	/**
	 * @return the minPlatformVersionNumber
	 */
	public float getMinPlatformVersionNumber() {
		return minPlatformVersionNumber;
	}

	/**
	 * @param minPlatformVersionNumber the minPlatformVersionNumber to set
	 */
	public void setMinPlatformVersionNumber(float minPlatformVersionNumber) {
		this.minPlatformVersionNumber = minPlatformVersionNumber;
	}


	/**
	 * @return the platformId
	 */
	public Long getPlatformId() {
		return platformId;
	}

	/**
	 * @param platformId the platformId to set
	 */
	public void setPlatformId(Long platformId) {
		this.platformId = platformId;
	}

	/**
	 * @return the platformVersionId
	 */
	public Long getPlatformVersionId() {
		return platformVersionId;
	}

	/**
	 * @param platformVersionId the platformVersionId to set
	 */
	public void setPlatformVersionId(Long platformVersionId) {
		this.platformVersionId = platformVersionId;
	}

	public Long getResolutionId() {
		return resolutionId;
	}

	/**
	 * @param resolutionId the resolutionId to set
	 */
	public void setResolutionId(Long resolutionId) {
		this.resolutionId = resolutionId;
	}

	/**
	 * @return the firstLoginDate
	 */
	public String getFirstLoginDate() {
		return firstLoginDate;
	}

	/**
	 * @param firstLoginDate the firstLoginDate to set
	 */
	public void setFirstLoginDate(String firstLoginDate) {
		this.firstLoginDate = firstLoginDate;
	}

	/**
	 * @return the branding
	 */
	public String getBranding() {
		return branding;
	}

	/**
	 * @param branding the branding to set
	 */
	public void setBranding(String branding) {
		this.branding = branding;
	}

	/**
	 * @return the channelId
	 */
	public String getChannelId() {
		return channelId;
	}

	/**
	 * @param channelId the channelId to set
	 */
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public boolean isShowPageGame() {
		return showPageGame;
	}

	public void setShowPageGame(boolean showPageGame) {
		this.showPageGame = showPageGame;
	}

	@Override
	public String toString() {
		return "UserPreference [uid=" + uid + ", platformId=" + platformId
				+ ", platformVersionId=" + platformVersionId
				+ ", resolutionId=" + resolutionId + ", screenSize="
				+ screenSize + ", previousSessionId=" + previousSessionId
				+ ", latitude=" + latitude + ", longitude=" + longitude
				+ ", minPlatformVersionResolution="
				+ minPlatformVersionResolution + ", minPlatformVersionNumber="
				+ minPlatformVersionNumber + ", firstLoginDate="
				+ firstLoginDate + ", branding=" + branding + ", channelId="
				+ channelId + ", site=" + site + ", domain=" + domain + "]";
	}
	
}
