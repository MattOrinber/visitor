package org.visitor.apportal.web.repository.order;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.visitor.apportal.web.repository.MyBatisRepository;
import org.visitor.appportal.visitor.domain.User;

@MyBatisRepository
public interface CustomerMybatisDao {

	User findById(Long id);

	List<User> findAll();

	List<User> find(Map<String, Object> parameters, RowBounds bounds);

	int findTotalNum(Map<String, Object> parameters);

	void insert(User user);

	void update(User user);

	void delete(Long user_id);

	void enable(Long user_id);

	void disable(Long user_id);
}
