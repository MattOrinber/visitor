package org.visitor.appportal.repository.newsite;

import org.springframework.data.jpa.repository.Query;
import org.visitor.appportal.repository.base.BaseRepository;
import org.visitor.appportal.visitor.domain.ProductDetailInfo;

public interface VisitorProductDetailInfoRepository extends
		BaseRepository<ProductDetailInfo, Long> {
	@Query("select p from ProductDetailInfo p where pdiProductId = ?1")
	ProductDetailInfo findProductDetailInfoByProductId(Long pid);
}
