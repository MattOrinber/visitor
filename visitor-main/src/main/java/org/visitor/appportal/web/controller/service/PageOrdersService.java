package org.visitor.appportal.web.controller.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.visitor.appportal.service.newsite.VisitorProductOrderService;
import org.visitor.appportal.service.newsite.redis.OrderRedisService;
import org.visitor.appportal.visitor.domain.ProductOrder;

@Service("pageOrdersService")
public class PageOrdersService {
	protected static final Logger log = LoggerFactory.getLogger(PageOrdersService.class);
	
	@Autowired
	private VisitorProductOrderService visitorProductOrderService;
	@Autowired
	private OrderRedisService orderRedisService;
	
	public Long countOrders() {
		return visitorProductOrderService.countOrders();
	}
	
	public List<ProductOrder> getPagedOrders(Long pageIdx) {
		return visitorProductOrderService.getPagedOrder(pageIdx);
	}
}
