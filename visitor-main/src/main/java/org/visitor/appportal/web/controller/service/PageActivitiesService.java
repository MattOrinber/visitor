package org.visitor.appportal.web.controller.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.visitor.appportal.service.newsite.VisitorProductService;
import org.visitor.appportal.service.newsite.redis.ProductRedisService;
import org.visitor.appportal.visitor.domain.Product;

@Service("pageActivitiesService")
public class PageActivitiesService {
	protected static final Logger log = LoggerFactory.getLogger(PageActivitiesService.class);
	
	@Autowired
	private VisitorProductService visitorProductService;
	@Autowired
	private ProductRedisService productRedisService;
	
	public Long getActivitiesCount() {
		return visitorProductService.countProduct();
	}
	
	public List<Product> getPagedProducts(Long pageIdx) {
		return visitorProductService.getPagedProducts(pageIdx);
	}
}
