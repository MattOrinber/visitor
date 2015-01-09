package org.visitor.appportal.web.controller.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.visitor.appportal.service.newsite.VisitorCityService;
import org.visitor.appportal.service.newsite.redis.ProductRedisService;
import org.visitor.appportal.visitor.domain.City;

@Service("pageCityService")
public class PageCityService {
	protected static final Logger log = LoggerFactory.getLogger(PageCityService.class);
	
	@Autowired
	private VisitorCityService visitorCityService;
	@Autowired
	private ProductRedisService productRedisService;
	
	public Long getCityCount() {
		return visitorCityService.getCityCount();
	}
	
	public List<City> getPagedCities(Long pageIdx) {
		return visitorCityService.getPagedCityList(pageIdx);
	}
}
