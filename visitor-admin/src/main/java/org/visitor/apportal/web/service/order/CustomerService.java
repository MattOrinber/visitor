package org.visitor.apportal.web.service.order;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.visitor.apportal.web.repository.order.CustomerMybatisDao;
import org.visitor.apportal.web.repository.order.TimezoneMybatisDao;
import org.visitor.appportal.visitor.domain.TimeZone;
import org.visitor.appportal.visitor.domain.User;

@Component
@Transactional
public class CustomerService {

	@Autowired
	private CustomerMybatisDao customerDao;

	@Autowired
	private TimezoneMybatisDao timezoneDao;

	/**
	 * 创建分页查询.
	 * 
	 * @param filterParams
	 * @param pageNumber
	 * @param pageSize
	 * @param sortType
	 * @return Page<Test>
	 */
	public Page<User> getUser(Map<String, Object> filterParams, int pageNumber,
			int pageSize) {

		List<User> users = customerDao.find(filterParams,
				buildRowBounds(pageNumber, pageSize));
		int totalnum = customerDao.findTotalNum(filterParams);

		return new PageImpl<User>(users, new PageRequest(pageNumber - 1,
				pageSize), totalnum);
	}

	/**
	 * 查找所有的User.
	 * 
	 * @return List<User>
	 */
	public List<User> getAllUser() {
		return customerDao.findAll();
	}

	/**
	 * 查找所有的TimeZone.
	 * 
	 * @return List<TimeZone>
	 */
	public List<TimeZone> getAllTimeZone() {
		return timezoneDao.findAll();
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
	 * 保存User.
	 * 
	 * @param newUser
	 */
	public void saveUser(User newUser) {
		if (newUser.getUserId() != null) {
			customerDao.update(newUser);
		} else {
			customerDao.insert(newUser);
		}
	}

	/**
	 * 根据主键获取User
	 * 
	 * @param id
	 * @return User
	 */
	public User getUser(Long user_id) {
		return customerDao.findById(user_id);
	}

	/**
	 * 根据主键删除User
	 * 
	 * @param id
	 */
	public void deleteUser(Long user_id) {
		customerDao.delete(user_id);
	}

	/**
	 * 启用用户
	 * @param user_id
	 */
	public void enable(Long user_id) {
		customerDao.enable(user_id);
	}

	/**
	 * 禁用用户
	 * @param user_id
	 */
	public void disable(Long user_id) {
		customerDao.disable(user_id);
	}

}
