/**
 * 
 */
package org.visitor.appportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.visitor.appportal.domain.PageContainer;
import org.visitor.appportal.domain.PageContainerPk;
import org.visitor.appportal.repository.base.BaseRepository;

/**
 * @author mengw
 * 
 */
public interface PageContainerRepository extends
		BaseRepository<PageContainer, PageContainerPk> {

	@Query("from PageContainer where pageContainerPk.pageId = :pageId")
	List<PageContainer> findByPageId(@Param("pageId") Long pageId);

	List<PageContainer> findByfolderId(Long folderId);

	@Query("from PageContainer where pageContainerPk.containerId = :containerId")
	List<PageContainer> findByContainerId(@Param("containerId") Long containerId);

	@Query("from PageContainer where pageContainerPk.containerId = :containerId and siteId = :siteId")
	List<PageContainer> findByContainerIdAndSiteId(
			@Param("containerId") Long containerId, @Param("siteId") Integer siteId);

	@Query("from PageContainer where pageContainerPk.containerId = :containerId and siteId = :siteId and folderId = :folderId")
	List<PageContainer> findByContainerIdAndSiteIdAndFolderId(
			@Param("containerId") Long containerId, @Param("siteId") Integer siteId,
			@Param("folderId") Long folderId);
	
	/**
	 * 按页面容器关系表里的信息去查找站点列表，这样可有效地去掉一些没有意义的站点
	 * @return
	 */
	@Query("select distinct p.siteId from PageContainer p")
	List<Integer> findByContainerSiteId();	

	/**
	 * 按页面容器关系表里的站点信息去查找频道列表，这样可有效地去掉一些没有意义的频道
	 * @return
	 */
	@Query("select distinct p.folderId from PageContainer p where p.siteId =:siteId")
	List<Long> findByContainerFolderId(@Param("siteId") Integer siteId);	

	/**
	 * 按页面容器关系表里的站点信息去查找频道列表，这样可有效地去掉一些没有意义的频道
	 * @return
	 */
	@Query("select distinct p.pageContainerPk.pageId from PageContainer p where p.folderId =:folderId")
	List<Long> findByContainerPageId(@Param("folderId") Long folderId);	

	/**
	 * 根据容器ID和页面ID来确定一个惟一的对应关系
	 * @param containerId
	 * @param pageId
	 * @return
	 */
	@Query("from PageContainer where pageContainerPk.containerId = :containerId and pageContainerPk.pageId = :pageId")
	PageContainer findByContainerIdAndPageId(
			@Param("containerId") Long containerId, @Param("pageId") Long pageId);

	/**
	 * 根据PageId和当前站点来查找对应的容器－页面关联关系
	 * @param pageId
	 * @param siteId
	 * @return
	 */
	@Query("from PageContainer where pageContainerPk.pageId = :pageId and siteId = :siteId")
	List<PageContainer> findByPageIdAndSiteId(@Param("pageId")Long pageId, @Param("siteId")Integer siteId);
	
}
