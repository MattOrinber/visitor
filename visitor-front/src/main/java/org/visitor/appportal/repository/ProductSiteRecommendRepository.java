package org.visitor.appportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.visitor.appportal.domain.ProductSiteRecommend;
import org.visitor.appportal.repository.base.BaseRepository;

public interface ProductSiteRecommendRepository extends BaseRepository<ProductSiteRecommend, Long> {
	
	@Query("select p from ProductSiteRecommend p where p.productSiteRecommendPk.productId = :productId")
	List<ProductSiteRecommend> findByProductId(@Param("productId")Long productId);
	
	@Query("select p from ProductSiteRecommend p where p.productSiteRecommendPk.siteId = :siteId")
	List<ProductSiteRecommend> findBySiteId(@Param("siteId")Integer siteId);
	
	@Query("select p from ProductSiteRecommend p where p.productSiteRecommendPk.siteId = :siteId and p.productSiteRecommendPk.productId = :productId")
	ProductSiteRecommend findByProductIdAndSiteId(@Param("productId")Long productId, @Param("siteId")Integer siteId);
}
