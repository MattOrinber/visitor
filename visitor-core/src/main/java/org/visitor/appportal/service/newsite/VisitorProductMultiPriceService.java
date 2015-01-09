package org.visitor.appportal.service.newsite;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.visitor.appportal.repository.newsite.VisitorProductMultiPriceRepository;
import org.visitor.appportal.visitor.domain.ProductMultiPrice;

@Service("visitorProductMultiPriceService")
public class VisitorProductMultiPriceService {
	private static final Logger logger = LoggerFactory.getLogger(VisitorProductMultiPriceService.class);
	
	@Autowired
	VisitorProductMultiPriceRepository visitorProductMultiPriceRepository;
	
	@Transactional
	public void saveProductMultiPrice(ProductMultiPrice pmp) {
		visitorProductMultiPriceRepository.save(pmp);
		if (logger.isInfoEnabled()) {
			logger.info("save product multi price success!");
		}
	}
	
	@Transactional
	public List<ProductMultiPrice> getProductMultiPrices(Long pid) {
		return visitorProductMultiPriceRepository.findProductMultiPriceByProductId(pid);
	}
}
