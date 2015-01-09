package org.visitor.appportal.service.newsite;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.visitor.appportal.repository.newsite.VisitorUserRepository;
import org.visitor.appportal.visitor.domain.User;

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
	public List<User> getAllUsers() {
		List<User> allUsers= (List<User>) visitorUserRepository.findAll();
		
		return allUsers;
	}
}
