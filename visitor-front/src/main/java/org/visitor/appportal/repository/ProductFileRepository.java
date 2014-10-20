package org.visitor.appportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import org.visitor.appportal.domain.ProductFile;
import org.visitor.appportal.repository.base.BaseRepository;

/**
 * @author mengw
 *
 */
public interface ProductFileRepository extends BaseRepository<ProductFile, Long> {

	/**
	 * 
	 * @param productId
	 * @return productFiles
	 */
	@Query("select p from ProductFile p where productId = ?1 and status = 0 " +
			"order by versionCode desc " +
			"order by os.name asc " +
			"order by platform.name asc " +
			"order by platformVersion.name desc " +
			"order by screenSize")
	List<ProductFile> findFileByProductId(Long productId);
	
	List<ProductFile> findByProductId(Long productId);
	
	ProductFile findByProductIdAndFileUrl(Long productId, String fileUrl);
	
	ProductFile findByProductIdAndJarMd5(Long productId, String jarMd5);
	
	List<ProductFile> findByFileNameAndFileUrl(String fileName, String fileUrl);

	List<ProductFile> findByPackageNameAndIdentity(String string, String string2);

	/**
	 * 根据状态获取产品的文件列表
	 * @param productId
	 * @param status
	 * @return
	 */
	List<ProductFile> findFileByProductIdAndStatus(Long productId, int status);
	
	@Query("select p from ProductFile p where productId = ?1" +
			" and platformId = ?2" +
			" and platformVersionId = ?3" +
			" and resolutionId = ?4" +
			" and status = ?5 " +
			"order by versionCode desc")
	List<ProductFile> findSimilarFileByProductIdAndPlatform(Long productId, Long platformId, Long platformVersionId, Long resolutionId, int status);
}
