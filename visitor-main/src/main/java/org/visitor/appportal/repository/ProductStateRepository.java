package org.visitor.appportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.visitor.appportal.domain.ProductState;
import org.visitor.appportal.repository.base.BaseRepository;

public interface ProductStateRepository extends BaseRepository<ProductState, Long> {

	ProductState findByProductId(Long productId);
	
	@Query("select psf.productSiteFolderPk.productId , ps.totalDl from ProductSiteFolder psf," +
			" ProductList pl ,ProductState ps where psf.productSiteFolderPk.productId = pl.productId" +
			" and psf.productSiteFolderPk.productId = ps.productId" +
			" and psf.folderId = :folderId and pl.downStatus = 0")
	List<Object[]> getFolderProductTotalDl(@Param("folderId")Long folderId);
}
