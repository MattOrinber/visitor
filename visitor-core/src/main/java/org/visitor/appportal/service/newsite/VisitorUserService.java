package org.visitor.appportal.service.newsite;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.visitor.appportal.repository.newsite.VisitorUserRepository;
import org.visitor.appportal.service.newsite.searchforms.UserSearchForm;
import org.visitor.appportal.visitor.domain.User;
import org.visitor.appportal.web.utils.WebInfo;

@Service("visitorUserService")
public class VisitorUserService {
	private static final Logger logger = LoggerFactory.getLogger(VisitorUserService.class);
	@Autowired
	VisitorUserRepository visitorUserRepository;
	
	@Transactional
	public void saveUser(User user) {
		visitorUserRepository.save(user);
		if (logger.isInfoEnabled()) {
			logger.info("user info changed to below---\n"+user.toString());
		}
	}
	
	@Transactional
	public User findUserByEmail(String emailStr) {
		User user = visitorUserRepository.findUserByEmail(emailStr);
		return user;
	}
	
	@Transactional
	public long checkUserCount(String emailStr) {
		long userCount = visitorUserRepository.findUserCountByEmail(emailStr);
		return userCount;
	}
	
	@Transactional
	public long checkUserFacebookIdCount(String faceBookIdStr) {
		long userCount = visitorUserRepository.findUserCountByFacebookId(faceBookIdStr);
		return userCount;
	}
	
	@Transactional
	public User getUserFromEmailAndPassword(String emailStr, String passwordStr) {
		User user = visitorUserRepository.findUserByEmailAndPassword(emailStr, passwordStr);
		return user;
	}
	
	@Transactional
	public List<User> getPageUsers(Long pageIdx) {
		UserSearchForm usf = new UserSearchForm();
		usf.getSp().setPageNumber(pageIdx.intValue());
		usf.getSp().setPageSize(WebInfo.pageSize.intValue());
		Page<User> allUsers= visitorUserRepository.findAll(usf.toSpecification(), usf.getPageable());
		
		return allUsers.getContent();
	}
	
	@Transactional
	public Long getUserCount() {
		Long count = visitorUserRepository.count();
		
		return count;
	}
	
	@Transactional
	public User getUserById(Long userId) {
		return visitorUserRepository.findOne(userId);
	}
}
