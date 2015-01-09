package org.visitor.appportal.service.newsite;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.visitor.appportal.repository.newsite.VisitorContainerRepository;
import org.visitor.appportal.service.newsite.searchforms.ContainerSearchForm;
import org.visitor.appportal.visitor.domain.Container;
import org.visitor.appportal.web.utils.WebInfo;

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
	
	@Transactional
	public Long getContainerCount() {
		return visitorContainerRepository.count();
	}
	
	@Transactional
	public List<Container> getPagedContainerList(Long pageIdx) {
		ContainerSearchForm csf = new ContainerSearchForm();
		csf.getSp().setPageNumber(pageIdx.intValue());
		csf.getSp().setPageSize(WebInfo.pageSize.intValue());
		Page<Container> pContan = visitorContainerRepository.findAll(csf.toSpecification(), csf.getPageable());
		
		return pContan.getContent();
	}
}
