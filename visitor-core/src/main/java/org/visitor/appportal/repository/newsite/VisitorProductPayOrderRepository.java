package org.visitor.appportal.repository.newsite;

import org.springframework.data.jpa.repository.Query;
import org.visitor.appportal.repository.base.BaseRepository;
import org.visitor.appportal.visitor.domain.ProductPayOrder;

public interface VisitorProductPayOrderRepository extends BaseRepository<ProductPayOrder, Long> {
	@Query("select p from ProductPayOrder p where payOrderId = ?1")
	ProductPayOrder getProductPayOrderByPayOrderId(Long payOrderId);
}
