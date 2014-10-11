/**
 * 
 */
package org.visitor.app.portal.model;

/**
 * @author mengw
 *
 */
public class SitePublishMessage {
	private Integer siteId;
	/**
	 * 
	 */
	public SitePublishMessage() {
		// TODO Auto-generated constructor stub
	}
	public SitePublishMessage(Integer siteId) {
		this.siteId = siteId;
	}
	/**
	 * @return the siteId
	 */
	public Integer getSiteId() {
		return siteId;
	}
	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

}
