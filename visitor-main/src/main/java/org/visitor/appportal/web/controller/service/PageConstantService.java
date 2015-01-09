package org.visitor.appportal.web.controller.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.visitor.appportal.service.newsite.VisitorFloopyThingService;
import org.visitor.appportal.service.newsite.redis.FloopyThingRedisService;
import org.visitor.appportal.visitor.domain.FloopyThing;

@Service("pageConstantService")
public class PageConstantService {
	protected static final Logger log = LoggerFactory.getLogger(PageConstantService.class);
	
	@Autowired
	private VisitorFloopyThingService visitorFloopyThingService;
	@Autowired
	private FloopyThingRedisService floopyThingRedisService;
	
	public Long getFloopyCount() {
		return visitorFloopyThingService.getFloopyCount();
	}
	
	public List<FloopyThing> getPagedFloopies(Long pageIdx) {
		return visitorFloopyThingService.getPagedFloopyThings(pageIdx);
	}
}
