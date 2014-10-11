package org.visitor.appportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import org.visitor.appportal.domain.ProductDetail;
import org.visitor.appportal.repository.base.BaseRepository;

public interface ProductDetailRepository extends BaseRepository<ProductDetail, Long>{

	/**
	 * 查找指定产品在指定站点下的产品细节
	 * @param productId
	 * @param siteId
	 * @return
	 */
	public ProductDetail findByProductIdAndSiteId(Long productId,Integer siteId);

	/**
	 * 查找指定产品的产品详情
	 * @param productId
	 * @return
	 */
	public List<ProductDetail> findByProductId(Long productId);

	/**
	 * 根据站点ID和产品ID来删除对应的产品详情
	 * @param siteId
	 * @param productId
	 */
	@Transactional
	@Modifying
	@Query("delete from ProductDetail pd where pd.siteId = :siteId and pd.productId = :productId")
	public void deleteBySiteIdAndProductId(@Param("siteId")Integer siteId, @Param("productId")Long productId);
	
}
