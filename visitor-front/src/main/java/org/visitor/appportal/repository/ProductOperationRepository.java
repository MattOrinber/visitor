package org.visitor.appportal.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.visitor.appportal.domain.ProductOperation;
import org.visitor.appportal.repository.base.BaseRepository;

public interface ProductOperationRepository extends BaseRepository<ProductOperation, Long> {

	@Query("select count (distinct p.productId) from ProductOperation p " +
			"where p.createDate >= :date and p.type = :type")
	long findCreateCount(@Param("date")Date date, @Param("type")Integer type);
	
	@Query("select count (distinct p.productId) from ProductOperation p " +
		"where p.createDate >= :date and p.type != :type")
	long findUpdateCount(@Param("date")Date date, @Param("type")Integer type);

}
