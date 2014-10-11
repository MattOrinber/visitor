/**
 * 
 */
package org.visitor.app.portal.model;

import java.util.Date;

import org.visitor.appportal.domain.Advertise;
import org.visitor.appportal.domain.Folder;
import org.visitor.appportal.domain.ProductContainer;

/**
 * @author mengw
 * TODO Rename to ProductContainerRecommend
 */
public class ProductContainerRecommand {
	// Raw attributes
	private Long productContainerId; // pk
	private Long sortOrder; // not null
	
	private Date startDate;
	private Date endDate;
	private Integer status; // not null
	private Long click; // not null
	private String createBy; // not null
	private Date createDate; // not null
	private String modBy; // not null
	private Date modDate; // not null
	private Long picId;

	// Technical attributes for query by example
	private Long containerId; // not null
	private Long folderId; // not null
	private Long pageId; // not null
	private Long productId; // not null
	private Integer siteId; // not null
	
	private Integer type;/*容器类型*/
	
	private Long advertiseId;/*广告ID*/
	private Long tfolderId;/**/

	// Many to one
	private transient Product product; // not null (productId)
	private transient Advertise advertise;
	private transient Folder tfolder;

	/**
	 * @param c 
	 * 
	 */
	public ProductContainerRecommand(ProductContainer c) {
		setProductContainerId(c.getProductContainerId());
		setContainerId(c.getContainerId());
		setSiteId(c.getSiteId());
		setFolderId(c.getFolderId());
		setPageId(c.getPageId());
		setProductId(c.getProductId());
		setSortOrder(c.getSortOrder());
		setStartDate(c.getStartDate());
		setEndDate(c.getEndDate());
		setStatus(c.getStatus());
		setClick(c.getClick());
		setCreateBy(c.getCreateBy());
		setCreateDate(c.getCreateDate());
		setModBy(c.getModBy());
		setModDate(c.getModDate());
		setPicId(c.getPicId());
		
		setType(c.getType());
		this.setAdvertiseId(c.getAdvertiseId());
		this.setTfolderId(c.getTfolderId());
		this.setProductId(c.getProductId());
	}

	/**
	 * @return the productContainerId
	 */
	public Long getProductContainerId() {
		return productContainerId;
	}

	/**
	 * @param productContainerId the productContainerId to set
	 */
	public void setProductContainerId(Long productContainerId) {
		this.productContainerId = productContainerId;
	}

	/**
	 * @return the sortOrder
	 */
	public Long getSortOrder() {
		return sortOrder;
	}

	/**
	 * @param sortOrder the sortOrder to set
	 */
	public void setSortOrder(Long sortOrder) {
		this.sortOrder = sortOrder;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the click
	 */
	public Long getClick() {
		return click;
	}

	/**
	 * @param click the click to set
	 */
	public void setClick(Long click) {
		this.click = click;
	}

	/**
	 * @return the createBy
	 */
	public String getCreateBy() {
		return createBy;
	}

	/**
	 * @param createBy the createBy to set
	 */
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the modBy
	 */
	public String getModBy() {
		return modBy;
	}

	/**
	 * @param modBy the modBy to set
	 */
	public void setModBy(String modBy) {
		this.modBy = modBy;
	}

	/**
	 * @return the modDate
	 */
	public Date getModDate() {
		return modDate;
	}

	/**
	 * @param modDate the modDate to set
	 */
	public void setModDate(Date modDate) {
		this.modDate = modDate;
	}

	/**
	 * @return the picId
	 */
	public Long getPicId() {
		return picId;
	}

	/**
	 * @param picId the picId to set
	 */
	public void setPicId(Long picId) {
		this.picId = picId;
	}

	/**
	 * @return the containerId
	 */
	public Long getContainerId() {
		return containerId;
	}

	/**
	 * @param containerId the containerId to set
	 */
	public void setContainerId(Long containerId) {
		this.containerId = containerId;
	}

	/**
	 * @return the folderId
	 */
	public Long getFolderId() {
		return folderId;
	}

	/**
	 * @param folderId the folderId to set
	 */
	public void setFolderId(Long folderId) {
		this.folderId = folderId;
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
	 * @return the productId
	 */
	public Long getProductId() {
		return productId;
	}

	/**
	 * @param productId the productId to set
	 */
	public void setProductId(Long productId) {
		this.productId = productId;
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

	/**
	 * @return the type
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * @return the advertiseId
	 */
	public Long getAdvertiseId() {
		return advertiseId;
	}

	/**
	 * @param advertiseId the advertiseId to set
	 */
	public void setAdvertiseId(Long advertiseId) {
		this.advertiseId = advertiseId;
	}

	/**
	 * @return the tfolderId
	 */
	public Long getTfolderId() {
		return tfolderId;
	}

	/**
	 * @param tfolderId the tfolderId to set
	 */
	public void setTfolderId(Long tfolderId) {
		this.tfolderId = tfolderId;
	}

	/**
	 * @return the product
	 */
	public Product getProduct() {
		return product;
	}

	/**
	 * @param product the product to set
	 */
	public void setProduct(Product product) {
		this.product = product;
	}

	/**
	 * @return the advertise
	 */
	public Advertise getAdvertise() {
		return advertise;
	}

	/**
	 * @param advertise the advertise to set
	 */
	public void setAdvertise(Advertise advertise) {
		this.advertise = advertise;
	}

	/**
	 * @return the tfolder
	 */
	public Folder getTfolder() {
		return tfolder;
	}

	/**
	 * @param tfolder the tfolder to set
	 */
	public void setTfolder(Folder tfolder) {
		this.tfolder = tfolder;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ProductContainerRecommand [productContainerId="
				+ productContainerId + ", sortOrder=" + sortOrder
				+ ", startDate=" + startDate + ", endDate=" + endDate
				+ ", status=" + status + ", click=" + click + ", createBy="
				+ createBy + ", createDate=" + createDate + ", modBy=" + modBy
				+ ", modDate=" + modDate + ", picId=" + picId
				+ ", containerId=" + containerId + ", folderId=" + folderId
				+ ", pageId=" + pageId + ", productId=" + productId
				+ ", siteId=" + siteId + ", type=" + type + ", advertiseId="
				+ advertiseId + ", tfolderId=" + tfolderId + ", product="
				+ product + ", advertise=" + advertise + ", tfolder=" + tfolder
				+ "]";
	}
}
