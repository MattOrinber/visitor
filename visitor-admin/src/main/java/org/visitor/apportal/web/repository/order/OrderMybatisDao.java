package org.visitor.apportal.web.repository.order;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.visitor.apportal.web.entity.order.OrderVO;
import org.visitor.apportal.web.repository.MyBatisRepository;
import org.visitor.appportal.visitor.domain.ProductOrder;

/**
 * 订单dao
 * @author yong.cao
 *
 */
@MyBatisRepository
public interface OrderMybatisDao {
	
    List<ProductOrder> findAll();
    
    List<OrderVO> findAllOrder();

	List<OrderVO> find(Map<String, Object> filterParams,
			RowBounds buildRowBounds);

	int findTotalNum(Map<String, Object> filterParams);

}
