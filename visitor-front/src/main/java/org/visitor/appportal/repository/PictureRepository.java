/**
 * 
 */
package org.visitor.appportal.repository;

import java.util.List;

import org.visitor.appportal.domain.Picture;
import org.visitor.appportal.repository.base.BaseRepository;

/**
 * @author mengw
 *
 */
public interface PictureRepository extends BaseRepository<Picture, Long> {

	List<Picture> findByPathLike(String path);

	List<Picture> findByTypeAndNameLikeAndSiteId(Integer type, String name, Integer siteId);
	
	List<Picture> findByType(Integer type);

}
