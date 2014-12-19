package org.visitor.appportal.service.newsite;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.visitor.appportal.repository.newsite.VisitorProductPicRepository;
import org.visitor.appportal.visitor.domain.ProductPicture;

@Service("visitorProductPictureService")
public class VisitorProductPictureService {
	private static final Logger logger = LoggerFactory.getLogger(VisitorProductPictureService.class);
	@Autowired
	private VisitorProductPicRepository visitorProductPicRepository;
	
	@Transactional
	public void saveProductPicture(ProductPicture entity) {
		visitorProductPicRepository.save(entity);
		if (logger.isInfoEnabled()) {
			logger.info("save product picture success");
		}
	}
	
	@Transactional
	public List<ProductPicture> getProductPictureList(Long pid) {
		return visitorProductPicRepository.getProductPictureList(pid);
	}
}
