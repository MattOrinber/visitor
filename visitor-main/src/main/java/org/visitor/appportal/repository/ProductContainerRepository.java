/**
 * 
 */
package org.visitor.appportal.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.visitor.appportal.domain.ProductContainer;
import org.visitor.appportal.repository.base.BaseRepository;

/**
 * @author mengw
 *
 */
public interface ProductContainerRepository extends BaseRepository<ProductContainer, Long> {

	List<ProductContainer> findByProductId(Long productId);
	
	List<ProductContainer> findByFolderId(Long folderId);

	//@Query("select p from ProductContainer p join fetch p.product where p.pageId = :pageId and p.containerId = :containerId")
	List<ProductContainer> findByPageIdAndContainerId(Long pageId, Long containerId, Sort sort);

	List<ProductContainer> findByPageIdAndContainerIdAndProductId(Long pageId, Long containerId, Long productId);

	List<ProductContainer> findByContainerId(Long recommandContainerId);
	
	/**
	 * 查找产品库中是否存在，后续两个方法类似
	 * @param pageId
	 * @param containerId
	 * @param productId
	 * @param type
	 * @return
	 */
	List<ProductContainer> findByPageIdAndContainerIdAndProductIdAndType(Long pageId, Long containerId, Long productId,Integer type);
	List<ProductContainer> findByPageIdAndContainerIdAndAdvertiseIdAndType(Long pageId, Long containerId, Long productId,Integer type);
	List<ProductContainer> findByPageIdAndContainerIdAndTfolderIdAndType(Long pageId, Long containerId, Long productId,Integer type);

	/**
	 * 根据广告ID查询
	 * @param advertiseId
	 * @return
	 */
	List<ProductContainer> findByAdvertiseId(Long advertiseId);	
	/**
	 * 根据频道ID查询
	 * @param tFolderId
	 * @return
	 */
	List<ProductContainer> findByTfolderId(Long tFolderId);
	
	@Query("select max(p.sortOrder) from ProductContainer p where p.siteId = :siteId and p.folderId = :folderId and " +
			"p.pageId = :pageId and p.containerId = :containerId ")
	Long findMaxSortOrder(@Param("siteId")Integer siteId, @Param("folderId")Long folderId,
			@Param("pageId")Long pageId, @Param("containerId")Long containerId);
}
