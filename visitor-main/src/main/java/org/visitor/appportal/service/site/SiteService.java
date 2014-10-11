package org.visitor.appportal.service.site;

import org.springframework.beans.factory.annotation.Autowired;

import org.visitor.appportal.domain.Site;
import org.visitor.appportal.repository.SiteRepository;

public abstract class SiteService {

	@Autowired
    private SiteRepository siteRepository;
	
	private Site site;

	/**
	 * 选择站点，create方法使用
	 * @return
	 */
	
	public Site getSite(){
		return this.siteRepository.findOne(getSiteId());
	}
	
	/**
	 * 选择站点，其它方法使用
	 * @return
	 */	
	public Site getDefaultSite(){
		
		if (site == null) {
			site = this.siteRepository.findOne(getSiteId());
		}
		
		return site;
	}	
	
	/**
	 * 由子类决定站点是什么
	 * @return
	 */
	public abstract Integer getSiteId();
	
	public enum ResultEnum {
		OK,ERROR,EXISTED
	}	
	
}
