package org.visitor.appportal.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import org.visitor.appportal.domain.ProductContainerTask;
import org.visitor.appportal.repository.base.BaseRepository;

/**
 * @author mengw
 *
 */
public interface ProductContainerTaskRepository extends BaseRepository<ProductContainerTask, Long> {

	List<ProductContainerTask> findByContainerId(Long recommandContainerId, Sort sort);

	List<ProductContainerTask> findByContainerIdAndType(Long recommandContainerId, Integer type, Sort sort);
	
	List<ProductContainerTask> findByContainerIdAndDisplayDate(Long recommandContainerId, Integer displayDate, Sort sort);
	
	List<ProductContainerTask> findByProductId(Long productId);
	//如果指定了推荐类型以及显示日期，那这个返回值应该是惟一的
	ProductContainerTask findByContainerIdAndDisplayDateAndProductId(Long recommandContainerId, Integer displayDate, Long productId);
	
	/**
	 * 根据广告ID查询
	 * @param advertiseId
	 * @return
	 */
	List<ProductContainerTask> findByAdvertiseId(Long advertiseId);
	ProductContainerTask findByContainerIdAndDisplayDateAndAdvertiseId(Long recommandContainerId, Integer displayDate, Long advertiseId);
	/**
	 * 根据频道ID查询
	 * @param tFolderId
	 * @return
	 */
	List<ProductContainerTask> findByTfolderId(Long tFolderId);
	ProductContainerTask findByContainerIdAndDisplayDateAndTfolderId(Long recommandContainerId, Integer displayDate, Long tfolderId);

	/**
	 * 根据定时日期来查询
	 * @param displayDate
	 * @param sort
	 * @return
	 */
	List<ProductContainerTask> findByDisplayDate(Integer displayDate, Sort sort);
	/**
	 * 根据日期和容器ID，查找出当天的最大排序号
	 * @param dispDate
	 * @param containerId
	 * @return
	 */
	@Query("select max(pt.sortOrder) from ProductContainerTask pt where pt.displayDate = :date and pt.containerId = :containerId")	
	Long findMaxOrderByDateAndContainerId(@Param("date")Integer dispDate, @Param("containerId")Long containerId);

	/**
	 * 查找在当天没有定时的推荐容器ID集合
	 * @param 当天有定时的容器ID集合
	 * @return
	 */
	@Query("select distinct(pt.containerId) from ProductContainerTask pt where  pt.containerId not in (:aSet)")
	List<Long> findRecommandIdsNotToday(@Param("aSet") Set<Long> ids);
	
	@Query("select distinct(pt.displayDate) from ProductContainerTask pt where pt.containerId = :containerId")
	List<Integer> findDisplayDateByContainerId(@Param("containerId")Long containerId, Sort sort);
	
	@Modifying
	@Transactional
	@Query("delete from ProductContainerTask pt where pt.displayDate = :date and pt.containerId = :containerId")
	void deleteByDateAndContainerId(@Param("date")Integer dispDate, @Param("containerId")Long containerId);
	
	@Query("select pt from ProductContainerTask pt where pt.containerId = :containerId and pt.displayDate >= :fromdate and pt.displayDate <= :todate order by pt.displayDate asc, pt.sortOrder desc")
	List<ProductContainerTask> findByContainerIdAndDisplayDateRange(@Param("containerId")Long containerId, @Param("fromdate")Integer fromdate, @Param("todate")Integer todate);
}
