/**
 * 
 */
package org.visitor.appportal.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.visitor.appportal.domain.ProductList;
import org.visitor.appportal.repository.base.BaseRepository;

/**
 * @author mengw
 *
 */
public interface ProductListRepository extends BaseRepository<ProductList, Long> {

	@Query("select createBy from ProductList group by createBy")
	List<String> getProductCreateBy();

	/**
	 * 
	 * @param productSource
	 * @param productSourceId
	 * @return
	 */
	ProductList findByOperatorIdAndProductSourceId(Long operatorId, String productSourceId);
	
	ProductList findByProductId(Long productId);
	
	@Query("from ProductList where productId in (:productIds)")
	List<ProductList> findByProductIds(@Param("productIds") String productIds);

	/**
	 * 频道下所有已经绑定的产品列表。
	 * @param folderId
	 * @return
	 */
	@Query("select p from ProductSiteFolder psf, ProductList p where psf.productSiteFolderPk.productId = p.productId and psf.folderId = :folderId")
	List<ProductList> findByFolderId(@Param("folderId")Long folderId);

	@Query("select distinct p from ProductOperation o join o.product p left join fetch p.folders" +
			" where o.createDate >= :date and o.type = :type")
	List<ProductList> findByDailyCreate(@Param("date")Date date, @Param("type")int type);

	@Query("select distinct p from ProductOperation o join o.product p left join fetch p.folders" +
			" where o.createDate >= :date and o.type != :type")
	List<ProductList> findByDailyUpdate(@Param("date")Date date, @Param("type")int type);

	@Query("from ProductList where productId in (:productIds)")
	List<ProductList> findByProductIds(@Param("productIds") List<Long> productIds);
	
	@Query("select p from ProductDetail pd, ProductList p where pd.productId = p.productId and pd.siteId = :siteId and pd.recommendStorage = 1")
	List<ProductList> findStorageProductBySiteId(@Param("siteId") Integer siteId);
	
	List<ProductList> findBySafeType(@Param("safeType") Integer safeType);
	
	List<ProductList> findByCreateDateGreaterThan(Date date);
	
	
	/**
	 * 根据站点绑定信息来查找某个产品是不是当前站点下的产品
	 * @param siteId 站点ID
	 * @param productId 产品ID
	 * @return
	 */
	@Query("select pl from ProductSiteFolder p,ProductList pl where p.productSiteFolderPk.siteId = :siteId and p.productSiteFolderPk.productId = :productId and pl.productId = p.productSiteFolderPk.productId")
	ProductList findByPidWithSiteId(@Param("siteId")Integer siteId, @Param("productId")Long productId);
	
}
