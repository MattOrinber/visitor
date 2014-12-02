package org.visitor.appportal.service.newsite.redis;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.visitor.appportal.redis.ObjectMapperWrapperForVisitor;
import org.visitor.appportal.redis.RedisKeysForVisitor;
import org.visitor.appportal.visitor.domain.User;
import org.visitor.appportal.visitor.domain.UserTokenInfo;

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
	
	public User getUserPassword(String keyT) {
		String keyFirst = RedisKeysForVisitor.getVisitorSiteUserPasswordFirstKey();
		String result = (String)compressStringRedisVisitorTemplate.opsForHash().get(keyFirst, keyT);
		return objectMapperWrapperForVisitor.convertToUser(result);
	}
	
	public void saveUserTokenInfo(UserTokenInfo uti) {
		String keyT = RedisKeysForVisitor.getVisitorSiteUserTokenInfoFirstKey();
		String valueT = objectMapperWrapperForVisitor.convert2String(uti);
		
		compressStringRedisVisitorTemplate.opsForHash().put(keyT, uti.getUfiUserEmail(), valueT);
	}
	
	public UserTokenInfo getUserTokenInfo(String mailStr) {
		String keyFirst = RedisKeysForVisitor.getVisitorSiteUserTokenInfoFirstKey();
		String result = (String)compressStringRedisVisitorTemplate.opsForHash().get(keyFirst, mailStr);
		return objectMapperWrapperForVisitor.convertToUserTokenInfo(result);
	}
	
	public void saveUserToken(String userEmail, String userToken) {
		String keyT = RedisKeysForVisitor.getVisitorUserTokenKey() + userEmail;
		stringRedisVisitorTemplate.opsForValue().set(keyT, userToken, 2, TimeUnit.HOURS);
	}
	
	public String getUserToken(String userEmail) {
		String keyT = RedisKeysForVisitor.getVisitorUserTokenKey() + userEmail;
		String result = stringRedisVisitorTemplate.opsForValue().get(keyT);
		return result;
	}
}
