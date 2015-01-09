package org.visitor.appportal.service.newsite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.visitor.appportal.repository.newsite.VisitorUserInternalMailRepository;
import org.visitor.appportal.visitor.domain.UserInternalMail;

@Service("visitorUserInternalMailService")
public class VisitorUserInternalMailService {
	private static final Logger logger = LoggerFactory.getLogger(VisitorUserInternalMailService.class);
	
	@Autowired
	VisitorUserInternalMailRepository visitorUserInternalMailRepository;
	
	@Transactional
	public void saveVisitorUserInternalMail(UserInternalMail entity) {
		visitorUserInternalMailRepository.save(entity);
		if (logger.isInfoEnabled()) {
			logger.info("save user internal mail success!");
		}
	}
	
	@Transactional
	public UserInternalMail getVisitorUserInternalMail(Long pid, String fromUserEmail) {
		UserInternalMail uimTemp = visitorUserInternalMailRepository.getUserInternalMailByPid(pid, fromUserEmail);
		return uimTemp;
	}
}
