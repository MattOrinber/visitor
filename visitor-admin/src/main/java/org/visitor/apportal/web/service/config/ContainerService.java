package org.visitor.apportal.web.service.config;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.visitor.apportal.web.repository.config.ContainerMybatisDao;
import org.visitor.appportal.visitor.domain.Container;

@Component
@Transactional
public class ContainerService {

	@Autowired
	private ContainerMybatisDao containerDao;

	/**
	 * 创建分页查询.
	 * 
	 * @param filterParams
	 * @param pageNumber
	 * @param pageSize
	 * @param sortType
	 * @return Page<Test>
	 */
	public Page<Container> getContainer(Map<String, Object> filterParams,
			int pageNumber, int pageSize) {

		List<Container> containers = containerDao.find(filterParams,
				buildRowBounds(pageNumber, pageSize));
		int totalnum = containerDao.findTotalNum(filterParams);

		return new PageImpl<Container>(containers, new PageRequest(
				pageNumber - 1, pageSize), totalnum);
	}

	/**
	 * 查找所有的Container.
	 * 
	 * @return List<Container>
	 */
	public List<Container> getAllContainer() {
		return containerDao.findAll();
	}

	/**
	 * 创建分页请求.
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @return RowBounds
	 */
	private RowBounds buildRowBounds(int pageNumber, int pageSize) {
		return new RowBounds((pageNumber - 1) * pageSize, pageSize);
	}

	/**
	 * 保存Container.
	 * 
	 * @param newContainer
	 */
	public void saveContainer(Container newContainer) {
		if (newContainer.getContainerId() != null) {
			containerDao.update(newContainer);
		} else {
			containerDao.insert(newContainer);
		}
	}

	/**
	 * 根据主键获取Container
	 * 
	 * @param id
	 * @return Container
	 */
	public Container getContainer(Long container_id) {
		return containerDao.findById(container_id);
	}

	/**
	 * 根据主键删除Container
	 * 
	 * @param id
	 */
	public void deleteContainer(Long container_id, String container_name) {
		containerDao.delete(container_id, container_name);
	}

}
