package org.visitor.appportal.repository;

import java.util.List;

import org.visitor.appportal.domain.Advertise;
import org.visitor.appportal.repository.base.BaseRepository;

/**
 * 本类用于处理广告类的相关业务逻辑
 * @author mengw
 *
 */
public interface AdvertiseRepository extends BaseRepository<Advertise, Long>{
	
	Advertise findByAdvertiseId(Long advertiseId);
	
	List<Advertise> findByPictureId(Long pictureId);

	/**
	 * 根据广告ID和站点ID查找相应的广告
	 * @param productId
	 * @param siteId
	 * @return
	 */
	Advertise findByAdvertiseIdAndSiteId(Long productId, Integer siteId);
}
