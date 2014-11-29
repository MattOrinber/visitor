package org.visitor.appportal.repository.newsite;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.visitor.appportal.repository.base.BaseRepository;
import org.visitor.appportal.visitor.domain.ProductMultiPrice;

public interface VisitorProductMultiPriceRepository extends
		BaseRepository<ProductMultiPrice, Long> {
	@Query("from ProductMultiPrice where pmpProductId = ?1")
	List<ProductMultiPrice> findProductMultiPriceByProductId(Long pid);
}
