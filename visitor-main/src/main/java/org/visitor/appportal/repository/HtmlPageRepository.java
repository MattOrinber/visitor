/**
 * 
 */
package org.visitor.appportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.visitor.appportal.domain.HtmlPage;
import org.visitor.appportal.repository.base.BaseRepository;

/**
 * @author mengw
 *
 */
public interface HtmlPageRepository extends BaseRepository<HtmlPage, Long> {

	List<HtmlPage> findByTemplateId(Long templateId);
	
	List<HtmlPage> findByFolderId(Long folderId);

	HtmlPage findByFolderIdAndPageType(Long folderId, Integer pageType);
	
	HtmlPage findBySiteIdAndIfDefaultPageAndPageType(Integer siteId, boolean ifDefaultPage, Integer pageType);

	@Query("select distinct p.page from ProductContainer p where p.containerId = :containerId order by p.createDate")
	List<HtmlPage> findPagesByContainerId(@Param("containerId")Long containerId);

	/**
	 * 所有用户自定义编辑的页面
	 * @param siteId
	 * @param b
	 * @return
	 */
	List<HtmlPage> findBySiteIdAndIfDefaultPage(Integer siteId, boolean b);

}
