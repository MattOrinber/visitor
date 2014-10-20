/**
 * 
 */
package org.visitor.appportal.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.visitor.appportal.domain.Category;
import org.visitor.appportal.repository.base.BaseRepository;

/**
 * @author mengw
 *
 */
public interface CategoryRepository extends BaseRepository<Category, Long> {
	
	List<Category> findByEnNameAndStatus(String enName, Integer status);
	
	List<Category> findByParentCategoryIdAndName(Long parentCategoryId, String name);
	
	List<Category> findByParentCategoryIdAndStatus(Long parentCategoryId, Integer status, Sort sort);

	@Query("select n from Category as n, Category as p where p.nsLeft " +
			"between n.nsLeft and n.nsRight " +
			"and p.categoryId = :id and p.nsThread = n.nsThread " +
			"order by n.categoryId asc")
	List<Category> findAncestors(@Param("id")Long categoryId);

	@Query("select max(c.sortOrder) from Category c where c.parentCategoryId = :pid")
	Integer findMaxSortOrderByParentId(@Param("pid")Long parentCategoryId);

	List<Category> findByParentCategoryId(Long categoryId);

	@Query("select c from Category c, Category p where c.status = :status and c.nsLeft between p.nsLeft " +
			"and p.nsRight and p.categoryId = :cid and c.nsThread = p.nsThread")
	List<Category> findByParentCategoryIdAndStatusWithChildren(@Param("cid")Long categoryId, @Param("status")Integer status);

	@Query("select c from Category c,Category c2 where c.parentCategoryId = c2.categoryId and c2.parentCategoryId = :pid" +
			" and c.status = :status")
	List<Category> findPlatformByParentCategoryId(@Param("pid")Long parentCategoryId, @Param("status")Integer status);

}
