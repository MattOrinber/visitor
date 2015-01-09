package org.visitor.appportal.service.newsite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.visitor.appportal.repository.newsite.VisitorContainerRepository;
import org.visitor.appportal.visitor.domain.Container;

@Service("visitorContainerService")
public class VisitorContainerService {
	private static final Logger logger = LoggerFactory.getLogger(VisitorContainerService.class);
	
	@Autowired
	private VisitorContainerRepository visitorContainerRepository;
	
	@Transactional
	public void saveContainer(Container entity) {
		visitorContainerRepository.save(entity);
		if (logger.isInfoEnabled()) {
			logger.info("container saved for :"+entity.getContainerName()+":");
		}
	}
	
	@Transactional
	public Container getContainerById(Long containerId) {
		return visitorContainerRepository.findOne(containerId);
	}
}
