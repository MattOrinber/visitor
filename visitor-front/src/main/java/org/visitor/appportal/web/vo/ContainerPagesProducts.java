package org.visitor.appportal.web.vo;

import java.util.List;

import org.visitor.appportal.domain.Folder;
import org.visitor.appportal.domain.PageContainer;
import org.visitor.appportal.domain.ProductContainer;
import org.visitor.appportal.domain.Site;

public class ContainerPagesProducts {
	
	private Site site;
	private List<Folder> folders;
	private PageContainer pageContainer;
	private List<ProductContainer> productContainers;
	
	public Site getSite() {
		return site;
	}
	public void setSite(Site site) {
		this.site = site;
	}
	public List<Folder> getFolders() {
		return folders;
	}
	public void setFolders(List<Folder> folders) {
		this.folders = folders;
	}
	public List<ProductContainer> getProductContainers() {
		return productContainers;
	}
	public void setProductContainers(List<ProductContainer> productContainers) {
		this.productContainers = productContainers;
	}
	/**
	 * @return the pageContainer
	 */
	public PageContainer getPageContainer() {
		return pageContainer;
	}
	/**
	 * @param pageContainer the pageContainer to set
	 */
	public void setPageContainer(PageContainer pageContainer) {
		this.pageContainer = pageContainer;
	}
	
}
