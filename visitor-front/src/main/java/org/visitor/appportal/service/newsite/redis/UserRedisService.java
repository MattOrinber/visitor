package org.visitor.appportal.service.newsite.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.visitor.appportal.redis.ObjectMapperWrapperForVisitor;
import org.visitor.appportal.redis.RedisKeysForVisitor;
import org.visitor.appportal.visitor.domain.User;
import org.visitor.appportal.visitor.domain.UserInternalMail;
import org.visitor.appportal.visitor.domain.UserTokenInfo;
import org.visitor.appportal.web.utils.RegisterInfo.UserMailStatusEnum;

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
	
	public void setUserInternalMailAlways(UserInternalMail uim) {
		String key = RedisKeysForVisitor.getUserInternalMailAlways();
		String hashKey = String.valueOf(uim.getUimId());
		
		String valueT = objectMapperWrapperForVisitor.convert2String(uim);
		compressStringRedisVisitorTemplate.opsForHash().put(key, hashKey, valueT);
	}
	
	public UserInternalMail getUserInternalMailAlways(String uimIdStr) {
		String key = RedisKeysForVisitor.getUserInternalMailAlways();
		String valueOri = (String) compressStringRedisVisitorTemplate.opsForHash().get(key, uimIdStr);
		UserInternalMail uimT = objectMapperWrapperForVisitor.convertToUserInternalMail(valueOri);
		return uimT;
	}
	
	public void setUserInternalMailUnread(UserInternalMail uim) {
		String keyTo = RedisKeysForVisitor.getUserInternalMailToMeKey() + uim.getUimToUserMail();
		String keyFrom = RedisKeysForVisitor.getUserInternalMailFromMeKey() + uim.getUimFromUserMail();
		String hashKey = String.valueOf(uim.getUimId());
		
		uim.setUimStatus(UserMailStatusEnum.Read.ordinal());
		String valueT = objectMapperWrapperForVisitor.convert2String(uim);
		
		uim.setUimStatus(UserMailStatusEnum.Unread.ordinal());
		String valueTo = objectMapperWrapperForVisitor.convert2String(uim);
		
		compressStringRedisVisitorTemplate.opsForHash().put(keyFrom, hashKey, valueT);
		compressStringRedisVisitorTemplate.opsForHash().put(keyTo, hashKey, valueTo);
	}
	
	public List<UserInternalMail> getUserInternalMailToMe(String userMailStr) {
		List<UserInternalMail> toMeList = new ArrayList<UserInternalMail>();
		
		String keyTo = RedisKeysForVisitor.getUserInternalMailToMeKey() + userMailStr;
		Map<Object, Object> entries = compressStringRedisVisitorTemplate.opsForHash().entries(keyTo);
		
		if (null != entries) {
			for(Object entry : entries.entrySet()) {
				String valueStr = (String)entries.get(entry);
				UserInternalMail uim = objectMapperWrapperForVisitor.convertToUserInternalMail(valueStr);
				toMeList.add(uim);
			}
		}
		return toMeList;
	}
	
	public List<UserInternalMail> getUserInternalMailFromMe(String userMailStr) {
		List<UserInternalMail> fromMeList = new ArrayList<UserInternalMail>();
		
		String keyTo = RedisKeysForVisitor.getUserInternalMailFromMeKey() + userMailStr;
		Map<Object, Object> entries = compressStringRedisVisitorTemplate.opsForHash().entries(keyTo);
		
		if (null != entries) {
			for(Object entry : entries.entrySet()) {
				String valueStr = (String)entries.get(entry);
				UserInternalMail uim = objectMapperWrapperForVisitor.convertToUserInternalMail(valueStr);
				fromMeList.add(uim);
			}
		}
		return fromMeList;
	}
}
