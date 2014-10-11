package org.visitor.appportal.web.controller.common;

import java.util.Date;

import org.visitor.app.portal.model.FolderRank.RankTypeEnum;
import org.visitor.appportal.web.utils.SearchParameters;

public class RankSearchForm {
	
	public Integer siteId;
	public Long folderId;
	public Long productId;
	public Integer type = RankTypeEnum.DailyDownload.ordinal();
	public Long version;
	public Integer level = 1;
		
	public Date publishDate;
	
    private SearchParameters searchParameters = new SearchParameters();

    /**
     * @return the {@link SearchParameters} controlling search meta attributes (order, pagination, etc.)
     */
    public SearchParameters getSp() {
        return searchParameters;
    }
	
	public Integer getSiteId() {
		return siteId;
	}
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	public Long getFolderId() {
		return folderId;
	}
	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Long getVersion() {
		return version;
	}
	public void setVersion(Long version) {
		this.version = version;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}

	public Date getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	
	public Integer getFirstResults(){
		return (getSp().getPageNumber() - 1) * getSp().getPageSize();
	}
	
	public Integer getMaxResults(){
		return getSp().getPageNumber() * getSp().getPageSize() - 1;
	}
}
