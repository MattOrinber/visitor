package org.visitor.appportal.repository.newsite;

import org.springframework.data.jpa.repository.Query;
import org.visitor.appportal.repository.base.BaseRepository;
import org.visitor.appportal.visitor.domain.Product;

public interface VisitorProductRepository extends BaseRepository<Product, Long> {
	@Query("select p from Product p where productId = ?1 and productStatus = 0 ")
	Product findProductByProductId(Long pid);
}
