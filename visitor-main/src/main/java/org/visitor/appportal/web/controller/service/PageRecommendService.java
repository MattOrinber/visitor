package org.visitor.appportal.web.controller.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.visitor.appportal.service.newsite.VisitorContainerService;
import org.visitor.appportal.service.newsite.redis.ContainerRedisService;
import org.visitor.appportal.visitor.domain.Container;

@Service("pageRecommendService")
public class PageRecommendService {
	protected static final Logger log = LoggerFactory.getLogger(PageRecommendService.class);
	
	@Autowired
	private VisitorContainerService visitorContainerService;
	@Autowired
	private ContainerRedisService containerRedisService;
	
	public Long getContainerCount() {
		return visitorContainerService.getContainerCount();
	}
	
	public List<Container> getPagedContainers(Long pageIdx) {
		return visitorContainerService.getPagedContainerList(pageIdx);
	}
}
