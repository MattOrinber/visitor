package org.visitor.appportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import org.visitor.appportal.domain.ProductSiteFolder;
import org.visitor.appportal.domain.ProductSiteFolderPk;
import org.visitor.appportal.repository.base.BaseRepository;

public interface ProductSiteFolderRepository extends BaseRepository<ProductSiteFolder, ProductSiteFolderPk> {
	
	@Query("from ProductSiteFolder where productSiteFolderPk.productId = ?1")
	List<ProductSiteFolder> findProductSiteFolders(Long productId);
	
	@Modifying
	@Transactional
	@Query("delete from ProductSiteFolder u where u.folderId = :folderId")
	void deleteByFolderId(@Param("folderId")Long folderId);
	
	List<ProductSiteFolder> findByFolderId(Long folderId);
	
	@Query("select count(productSiteFolderPk.productId) from ProductSiteFolder where folderId = ?1")
	Long countFolderProducts(Long folderId);

	@Query("select p from ProductSiteFolder p where p.productSiteFolderPk.productId = :productId")
	List<ProductSiteFolder> findByProductId(@Param("productId")Long primaryKey);
	
	@Query("select p from ProductSiteFolder p where p.productSiteFolderPk.siteId = :siteId")
	List<ProductSiteFolder> findBySiteId(@Param("siteId")Integer siteId);

	@Query("select max(p.sortOrder) from ProductSiteFolder p where p.folderId = :folderId and p.sortOrder < :max")
	Integer getMaxSortOrder(@Param("folderId")Long folderId, @Param("max")Integer maxSortOrder);
	
	@Query("select p from ProductSiteFolder p where p.folderId = :folderId order by p.createDate desc ")
	List<ProductSiteFolder> findByFolderIdO(@Param("folderId")Long folderId);
	
	@Query("select p from ProductSiteFolder p where p.productSiteFolderPk.siteId = :siteId and p.productSiteFolderPk.productId = :productId")
	ProductSiteFolder findBySiteIdAndProductId(@Param("siteId")Integer siteId, @Param("productId")Long productId);

	@Query("select p from ProductSiteFolder p,ProductList pl where p.productSiteFolderPk.siteId = :siteId and p.productSiteFolderPk.productId = :productId and pl.productId = p.productSiteFolderPk.productId")
	ProductSiteFolder findBySiteIdWithProductList(@Param("siteId")Integer siteId, @Param("productId")Long productId);

}
