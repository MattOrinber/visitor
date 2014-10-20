package org.visitor.appportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import org.visitor.appportal.domain.ProductPic;
import org.visitor.appportal.repository.base.BaseRepository;

public interface ProductPicRepository extends BaseRepository<ProductPic, Long> {
	
	@Query("from ProductPic where productId = ?1 and status = 0")
	List<ProductPic> getProductPics(Long productId);
	
	List<ProductPic> findByProductIdAndPicType(Long prouctId, Integer picType);

	@Modifying
	@Transactional
	@Query("delete from ProductPic p where p.productId = :productId and p.picType = :picType")
	void deleteByProductIdAndPicType(@Param("productId")Long productId, @Param("picType")int picType);
}
