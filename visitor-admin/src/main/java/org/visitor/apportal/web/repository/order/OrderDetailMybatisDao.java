package org.visitor.apportal.web.repository.order;

import org.visitor.apportal.web.entity.order.OrderDetailVO;
import org.visitor.apportal.web.repository.MyBatisRepository;

/**
 * 订单明细dao
 * 
 * @author yong.cao
 * 
 */
@MyBatisRepository
public interface OrderDetailMybatisDao {
	OrderDetailVO findByOrderId(Long orderId);

	void updateStatus(OrderDetailVO detail);
}
