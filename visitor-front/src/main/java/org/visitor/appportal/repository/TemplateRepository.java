/**
 * 
 */
package org.visitor.appportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import org.visitor.appportal.domain.Template;
import org.visitor.appportal.repository.base.BaseRepository;

/**
 * @author mengw
 *
 */
public interface TemplateRepository extends BaseRepository<Template, Long> {

	List<Template> findByName(String name);

	/**
	 * 根据名称和所属站点查找相应站点下的头模板(type＝0)
	 * @param name
	 * @param siteId
	 * @return
	 */
	@Query("from Template where name like ?1 and publishStatus = 0 and type = 0 and siteId = ?2")
	List<Template> findByNameLike(String name,Integer siteId);

	Template findByNameAndSiteId(String name, Integer siteId);
}
