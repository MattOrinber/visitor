package org.visitor.appportal.repository.newsite;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.visitor.appportal.repository.base.BaseRepository;
import org.visitor.appportal.visitor.domain.ProductPicture;

public interface VisitorProductPicRepository extends BaseRepository<ProductPicture, Long> {
	@Query("from ProductPicture where productPicProductId = ?1 and productPicStatus = 0")
	List<ProductPicture> getProductPictureList(Long pid);
}
