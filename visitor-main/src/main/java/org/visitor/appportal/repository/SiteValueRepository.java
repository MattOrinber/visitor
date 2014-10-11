package org.visitor.appportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import org.visitor.appportal.domain.Category;
import org.visitor.appportal.domain.SiteValue;
import org.visitor.appportal.repository.base.BaseRepository;

public interface SiteValueRepository extends BaseRepository<SiteValue, Long> {

	List<SiteValue> findBySiteIdAndType(Integer siteId, Integer type);
	
	@Query("select b from SiteValue as a, Category as b where a.value = b.categoryId and a.siteId = ?1 and a.type = ?2")
	List<Category> getSiteValueCategory(Integer siteId, Integer type);
}
