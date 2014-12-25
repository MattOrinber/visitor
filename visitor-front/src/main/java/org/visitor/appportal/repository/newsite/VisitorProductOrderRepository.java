package org.visitor.appportal.repository.newsite;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.visitor.appportal.repository.base.BaseRepository;
import org.visitor.appportal.visitor.domain.ProductOrder;

public interface VisitorProductOrderRepository extends
		BaseRepository<ProductOrder, Long> {
	@Query("select o from ProductOrder o where orderId = ?1")
	ProductOrder getOrderByOrderId(Long orderId);
	
	@Query("from ProductOrder where orderProductId = ?1")
	List<ProductOrder> getOrdersByProductId(Long pid);
	
	@Query("from ProductOrder where orderUserEmail = ?1")
	List<ProductOrder> getOrdersByBookUserEmail(String userEmail);
}
