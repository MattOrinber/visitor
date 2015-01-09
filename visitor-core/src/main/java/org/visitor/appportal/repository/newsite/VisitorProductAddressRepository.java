package org.visitor.appportal.repository.newsite;

import org.springframework.data.jpa.repository.Query;
import org.visitor.appportal.repository.base.BaseRepository;
import org.visitor.appportal.visitor.domain.ProductAddress;

public interface VisitorProductAddressRepository extends BaseRepository<ProductAddress, Long> {
	@Query("select p from ProductAddress p where paProductid = ?1")
	ProductAddress findProductAddressByProductId(Long pid);
}
