package org.visitor.appportal.service.newsite;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.visitor.appportal.repository.newsite.VisitorProductOrderRepository;
import org.visitor.appportal.repository.newsite.VisitorProductPayOrderRepository;
import org.visitor.appportal.visitor.domain.ProductOrder;
import org.visitor.appportal.visitor.domain.ProductPayOrder;

@Service("visitorProductOrderService")
public class VisitorProductOrderService {
	private static final Logger logger = LoggerFactory.getLogger(VisitorProductOrderService.class);
	
	@Autowired
	private VisitorProductPayOrderRepository visitorProductPayOrderRepository;
	@Autowired
	private VisitorProductOrderRepository visitorProductOrderRepository;
	
	@Transactional
	public void saveProductOrder(ProductOrder entity) {
		visitorProductOrderRepository.save(entity);
		
		if (logger.isInfoEnabled()) {
			logger.info("product order saved!");
		}
	}
	
	@Transactional
	public ProductOrder getProductOrderById(Long productOrderId) {
		return visitorProductOrderRepository.getOrderByOrderId(productOrderId);
	}
	
	@Transactional
	public List<ProductOrder> getProductOrdersByProductId(Long productId) {
		return visitorProductOrderRepository.getOrdersByProductId(productId);
	}
	
	@Transactional
	public List<ProductOrder> getProductOrdersByUserEmail(String userEmail) {
		return visitorProductOrderRepository.getOrdersByBookUserEmail(userEmail);
	}
	
	@Transactional
	public void saveProductPayOrder(ProductPayOrder ppo) {
		visitorProductPayOrderRepository.save(ppo);
		
		if (logger.isInfoEnabled()) {
			logger.info("product pay order saved!");;
		}
	}
	
	@Transactional
	public ProductPayOrder getProductPayOrderById(Long ppoId) {
		return visitorProductPayOrderRepository.getProductPayOrderByPayOrderId(ppoId);
	}
}
