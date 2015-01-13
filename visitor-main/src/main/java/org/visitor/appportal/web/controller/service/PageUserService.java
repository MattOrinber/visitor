package org.visitor.appportal.web.controller.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.visitor.appportal.service.newsite.VisitorUserService;
import org.visitor.appportal.service.newsite.redis.UserRedisService;
import org.visitor.appportal.visitor.domain.User;

@Service("pageUserService")
public class PageUserService {
	protected static final Logger log = LoggerFactory.getLogger(PageUserService.class);
	
	@Autowired
	private VisitorUserService visitorUserService;
	@Autowired
	private UserRedisService userRedisService;
	
	public List<User> getUserList(Long pageIdx) {
		List<User> userList= visitorUserService.getPageUsers(pageIdx);
		return userList;
	}
	
	public Long getUserCount() {
		return visitorUserService.getUserCount();
	}
	
	public User getUserByEmail(String emailStr) {
		return visitorUserService.findUserByEmail(emailStr);
	}
}
