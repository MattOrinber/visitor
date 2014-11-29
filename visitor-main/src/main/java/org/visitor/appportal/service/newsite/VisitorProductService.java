package org.visitor.appportal.service.newsite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.visitor.appportal.repository.newsite.VisitorProductRepository;
import org.visitor.appportal.visitor.domain.Product;

@Service("visitorProductService")
public class VisitorProductService {
	private static final Logger logger = LoggerFactory.getLogger(VisitorProductService.class);
	
	@Autowired
	VisitorProductRepository visitorProductRepository;
	
	@Transactional
	public void saveProduct(Product product) {
		visitorProductRepository.save(product);
		if (logger.isInfoEnabled()) {
			logger.info("product saved!");
		}
	}
	
	@Transactional
	public Product getProductById(Long pid) {
		Product product = visitorProductRepository.findProductByProductId(pid);
		return product;
	}
}
