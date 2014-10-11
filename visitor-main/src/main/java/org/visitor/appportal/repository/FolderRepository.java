package org.visitor.appportal.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.visitor.appportal.domain.Folder;
import org.visitor.appportal.repository.base.BaseRepository;

public interface FolderRepository extends BaseRepository<Folder, Long> {
	
	Folder findByFolderId(Long folderId);

	@Query("from Folder where siteId = ?1 and parentFolderId is null")
	List<Folder> findSiteTopFolder(Integer siteId);
	
	List<Folder> findBySiteIdAndPath(Integer siteId, String path);
	
	List<Folder> findBySiteIdAndFolderType(Integer siteId, Integer folderType);
	
	/**
	 * folder's bread
	 * 
	 * @param folderId
	 * @return folders
	 */
	@Query("select n from Folder as n , Folder as p where p.nsLeft between n.nsLeft and n.nsRight"
			+ " and p.folderId = ?1 and n.siteId = p.siteId and p.nsThread = n.nsThread"
			+ " and n.status = 0 order by n.folderId asc")
	List<Folder> findFolderBread(Long folderId);

	List<Folder> findByParentFolderIdAndStatus(Long folderId, Integer status, Sort sort);

	@Query("select psf.folder from ProductSiteFolder psf where psf.productSiteFolderPk.productId = :productId")
	List<Folder> findByProductId(@Param("productId")Long productId);

	List<Folder> findByTagId(Long categoryId);

	List<Folder> findBySiteIdAndStatus(Integer siteId, int enable);
	
	List<Folder> findByPicId(Long picId);

	/**
	 * 根据站点和频道来查询某个频道
	 * @param productId
	 * @param siteId
	 * @return
	 */
	Folder findByFolderIdAndSiteId(Long productId, Integer siteId);
}
