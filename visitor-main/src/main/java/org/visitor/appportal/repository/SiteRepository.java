package org.visitor.appportal.repository;

import java.util.List;

import org.visitor.appportal.domain.Site;
import org.visitor.appportal.repository.base.BaseRepository;

public interface SiteRepository extends BaseRepository<Site, Integer> {
	
	List<Site> findByStatus(Integer status);
	
	List<Site> findByNameLike(String name);
	List<Site> findAll();
	
	/**查找指定站点*/
	Site findBySiteId(Integer siteId);
}
