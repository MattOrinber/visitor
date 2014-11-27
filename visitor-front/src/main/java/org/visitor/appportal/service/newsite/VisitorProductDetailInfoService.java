package org.visitor.appportal.service.newsite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.visitor.appportal.repository.newsite.VisitorProductDetailInfoRepository;
import org.visitor.appportal.visitor.domain.ProductDetailInfo;

@Service("visitorProductDetailInfoService")
public class VisitorProductDetailInfoService {
	private static final Logger logger = LoggerFactory.getLogger(VisitorProductDetailInfoService.class);
	
	@Autowired
	VisitorProductDetailInfoRepository visitorProductDetailInfoRepository;
	
	@Transactional
	public void saveProductDetailInfo(ProductDetailInfo pdi) {
		visitorProductDetailInfoRepository.save(pdi);
		if (logger.isInfoEnabled()) {
			logger.info("save Product detail info success!");
		}
	}
	
	@Transactional
	public ProductDetailInfo getProductDetailInfoByProductId(Long pid) {
		return visitorProductDetailInfoRepository.findProductDetailInfoByProductId(pid);
	}
}
