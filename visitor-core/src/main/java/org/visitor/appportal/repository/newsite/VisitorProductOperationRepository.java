package org.visitor.appportal.repository.newsite;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.visitor.appportal.repository.base.BaseRepository;
import org.visitor.appportal.visitor.domain.ProductOperation;

public interface VisitorProductOperationRepository extends
		BaseRepository<ProductOperation, Long> {
	@Query("from ProductOperation where poProductid = ?1 and poType = ?2 and poStatus = 0")
	List<ProductOperation> getOperationListByProductIdAndType(Long pid, Integer type);
}
