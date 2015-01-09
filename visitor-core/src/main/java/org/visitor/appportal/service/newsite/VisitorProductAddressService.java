package org.visitor.appportal.service.newsite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.visitor.appportal.repository.newsite.VisitorProductAddressRepository;
import org.visitor.appportal.visitor.domain.ProductAddress;

@Service("visitorProductAddressService")
public class VisitorProductAddressService {
	private static final Logger logger = LoggerFactory.getLogger(VisitorProductAddressService.class);
	
	@Autowired
	VisitorProductAddressRepository visitorProductAddressRepository;
	
	@Transactional
	public void saveProductAddress(ProductAddress pa) {
		visitorProductAddressRepository.save(pa);
		if (logger.isInfoEnabled()) {
			logger.info("product address saved!");
		}
	}
	
	@Transactional
	public ProductAddress getProductAddressUsingProductId(Long pid) {
		ProductAddress pa = visitorProductAddressRepository.findProductAddressByProductId(pid);
		return pa;
	}
}
