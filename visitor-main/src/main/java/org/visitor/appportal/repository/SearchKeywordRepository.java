/**
 * 
 */
package org.visitor.appportal.repository;

import java.util.List;

import org.springframework.data.domain.Sort;

import org.visitor.appportal.domain.SearchKeyword;
import org.visitor.appportal.repository.base.BaseRepository;

/**
 * @author mengw
 *
 */
public interface SearchKeywordRepository extends BaseRepository<SearchKeyword, Long> {
	
	List<SearchKeyword> findBySiteIdAndName(Integer siteId, String name, Sort sort);

	List<SearchKeyword> findBySiteIdAndStatus(Integer siteId, Integer status, Sort sort);
	List<SearchKeyword> findBySiteIdAndStatusAndIsHot(Integer siteId, Integer status, Integer isHot,Sort sort);

	List<SearchKeyword> findBySiteId(Integer siteId, Sort sort);
	
	SearchKeyword findByKeywordId(Long keywordId);
	/**
	 * 以是否为热词和当前状态来作参数，查找相应的结果
	 * @param isHot
	 * @param status
	 * @return
	 */
	List<SearchKeyword> findByIsHotAndStatus(Integer isHot,Integer status);

}
