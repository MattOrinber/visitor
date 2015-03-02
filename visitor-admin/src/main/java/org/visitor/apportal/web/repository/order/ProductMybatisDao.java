package org.visitor.apportal.web.repository.order;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.visitor.apportal.web.repository.MyBatisRepository;
import org.visitor.appportal.visitor.domain.Product;

@MyBatisRepository
public interface ProductMybatisDao {

	Product findByProductId(Long product_id);

	List<Product> findAll();

	List<Product> find(Map<String, Object> filterParams, RowBounds bounds);

	int findTotalNum(Map<String, Object> filterParams);

	void insert(Product product);

	void update(Product product);

	void delete(Long product_id);
}
