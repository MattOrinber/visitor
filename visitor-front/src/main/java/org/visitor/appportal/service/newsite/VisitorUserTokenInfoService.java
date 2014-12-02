package org.visitor.appportal.service.newsite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.visitor.appportal.repository.newsite.VisitorUserTokenInfoRepository;
import org.visitor.appportal.visitor.domain.UserTokenInfo;

@Service("visitorUserTokenInfoService")
public class VisitorUserTokenInfoService {
	private static final Logger logger = LoggerFactory.getLogger(VisitorUserTokenInfoService.class);
	
	@Autowired
	private VisitorUserTokenInfoRepository visitorUserTokenInfoRepository;
	
	@Transactional
	public void saveUserTokenInfo(UserTokenInfo entity) {
		visitorUserTokenInfoRepository.save(entity);
		if (logger.isInfoEnabled()) {
			logger.info("user token info saved!");
		}
	}
	
	@Transactional
	public UserTokenInfo getUserTokenInfoByUserId(Long uid) {
		return visitorUserTokenInfoRepository.getUserTokenInfoByUserId(uid);
	}
	
	@Transactional
	public UserTokenInfo getUserTokenInfoByUserEmail(String userEmail) {
		return visitorUserTokenInfoRepository.getUserTokenInfoByUserEmail(userEmail);
	}
}
