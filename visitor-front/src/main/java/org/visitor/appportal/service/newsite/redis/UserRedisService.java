package org.visitor.appportal.service.newsite.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.visitor.appportal.redis.ObjectMapperWrapperForVisitor;
import org.visitor.appportal.redis.RedisKeysForVisitor;
import org.visitor.appportal.visitor.domain.User;

@Service("userRedisService")
public class UserRedisService {
	@Autowired
	private StringRedisTemplate stringRedisVisitorTemplate;
	@Autowired
	private StringRedisTemplate compressStringRedisVisitorTemplate;
	@Autowired
	private ObjectMapperWrapperForVisitor objectMapperWrapperForVisitor;
	
	public void saveUserPassword(User user) {
		String keyT = RedisKeysForVisitor.getVisitorSiteUserPasswordFirstKey();
		String valueT = objectMapperWrapperForVisitor.convert2String(user);
		compressStringRedisVisitorTemplate.opsForHash().put(keyT, user.getUserEmail(), valueT);
	}
}
