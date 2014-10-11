/**
 * 
 */
package org.visitor.app.portal.model;

/**
 * @author mengw
 *
 */
public class HtmlPagePublishMessage {
	private Long pageId;
	private Integer siteId;
	private String metaKey;
	private boolean preview;
	
	/**
	 * 
	 */
	public HtmlPagePublishMessage() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the pageId
	 */
	public Long getPageId() {
		return pageId;
	}

	/**
	 * @param pageId the pageId to set
	 */
	public void setPageId(Long pageId) {
		this.pageId = pageId;
	}

	/**
	 * @return the metaKey
	 */
	public String getMetaKey() {
		return metaKey;
	}

	/**
	 * @param metaKey the metaKey to set
	 */
	public void setMetaKey(String metaKey) {
		this.metaKey = metaKey;
	}

	/**
	 * @return the preview
	 */
	public boolean isPreview() {
		return preview;
	}

	/**
	 * @param preview the preview to set
	 */
	public void setPreview(boolean preview) {
		this.preview = preview;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

}
