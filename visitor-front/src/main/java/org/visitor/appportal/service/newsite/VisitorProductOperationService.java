package org.visitor.appportal.service.newsite;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.visitor.appportal.repository.newsite.VisitorProductOperationRepository;
import org.visitor.appportal.visitor.domain.ProductOperation;

@Service("visitorProductOperationService")
public class VisitorProductOperationService {
	private static final Logger logger = LoggerFactory.getLogger(VisitorProductOperationService.class);
	
	@Autowired
	VisitorProductOperationRepository visitorProductOperationRepository;
	
	@Transactional
	public void saveProductOperation(ProductOperation entity) {
		visitorProductOperationRepository.save(entity);
		if (logger.isInfoEnabled()) {
			logger.info("save product operation success!");
		}
	}
	
	@Transactional
	public List<ProductOperation> getProductOperationList(Long productId, Integer productType) {
		return visitorProductOperationRepository.getOperationListByProductIdAndType(productId, productType);
	}
}
