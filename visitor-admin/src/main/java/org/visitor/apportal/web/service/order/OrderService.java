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
import org.visitor.apportal.web.entity.order.OrderDetailVO;
import org.visitor.apportal.web.entity.order.OrderVO;
import org.visitor.apportal.web.repository.order.OrderDetailMybatisDao;
import org.visitor.apportal.web.repository.order.OrderMybatisDao;

@Component
@Transactional(readOnly = true)
public class OrderService {

	@Autowired
	private OrderMybatisDao orderDao;
	@Autowired
	private OrderDetailMybatisDao orderDetailDao;

	/**
	 * 创建分页查询.
	 * 
	 * @param filterParams
	 * @param pageNumber
	 * @param pageSize
	 * @param sortType
	 * @return Page<Test>
	 */
	public Page<OrderVO> getOrder(Map<String, Object> filterParams,
			int pageNumber, int pageSize) {

		List<OrderVO> orders = orderDao.find(filterParams,
				buildRowBounds(pageNumber, pageSize));
		int totalnum = orderDao.findTotalNum(filterParams);

		return new PageImpl<OrderVO>(orders, new PageRequest(pageNumber - 1,
				pageSize), totalnum);
	}

	public List<OrderVO> getAllOrder() {
		return orderDao.findAllOrder();
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

	public OrderDetailVO getOrderDetail(Long orderId) {
		return orderDetailDao.findByOrderId(orderId);
	}

	/**
	 * 修改订单状态
	 * @param detail
	 */
	@Transactional(readOnly = false)
	public void saveStatus(OrderDetailVO detail) {
		orderDetailDao.updateStatus(detail);
	}
}
