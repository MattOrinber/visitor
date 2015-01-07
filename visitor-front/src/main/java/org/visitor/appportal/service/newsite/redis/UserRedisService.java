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
	
	//存储邮件实体
	public void setUserInternalMailAlways(UserInternalMail uim) {
		String key = RedisKeysForVisitor.getUserInternalMailAlways();
		String hashKey = String.valueOf(uim.getUimId());
		
		String valueT = objectMapperWrapperForVisitor.convert2String(uim);
		compressStringRedisVisitorTemplate.opsForHash().put(key, hashKey, valueT);
	}
	//获取邮件实体
	public UserInternalMail getUserInternalMailAlways(String uimIdStr) {
		String key = RedisKeysForVisitor.getUserInternalMailAlways();
		String valueOri = (String) compressStringRedisVisitorTemplate.opsForHash().get(key, uimIdStr);
		UserInternalMail uimT = objectMapperWrapperForVisitor.convertToUserInternalMail(valueOri);
		return uimT;
	}
	
	//设置收方未读列表
	public void setUserInternalMailUnread(UserInternalMail uim) {
		String keyTo = RedisKeysForVisitor.getUserInternalMailToMeKey() + uim.getUimToUserMail();
		String hashKey = String.valueOf(uim.getUimId());
		String valueTo = "-";
		compressStringRedisVisitorTemplate.opsForHash().put(keyTo, hashKey, valueTo);
	}
	//设置发方发送列表
	public void setUserInternalMailFromMe(User user, UserInternalMail uim) {
		String keyFrom = RedisKeysForVisitor.getUserInternalMailFromMeKey() + user.getUserEmail();
		String hashKey = String.valueOf(uim.getUimId());
		String valueTo = "-";
		compressStringRedisVisitorTemplate.opsForHash().put(keyFrom, hashKey, valueTo);
	}
	//设置发方已回复列表
	public void setUserInternalMailReplied(User user, UserInternalMail uim) {
		String keyFrom = RedisKeysForVisitor.getUserInternalMailRepliedFromMeKey() + user.getUserEmail();
		String hashKey = String.valueOf(uim.getUimId());
		String valueTo = "-";
		compressStringRedisVisitorTemplate.opsForHash().put(keyFrom, hashKey, valueTo);
	}
	
	//查看本人的未读邮件列表
	public List<UserInternalMail> getUserInternalMailToMe(String userMailStr) {
		List<UserInternalMail> toMeList = new ArrayList<UserInternalMail>();
		
		String keyTo = RedisKeysForVisitor.getUserInternalMailToMeKey() + userMailStr;
		Map<Object, Object> entries = compressStringRedisVisitorTemplate.opsForHash().entries(keyTo);
		
		String keyA = RedisKeysForVisitor.getUserInternalMailAlways();
		
		if (null != entries) {
			for(Object entry : entries.keySet()) {
				String uimIdStr = (String)entry;
				String valueOri = (String) compressStringRedisVisitorTemplate.opsForHash().get(keyA, uimIdStr);
				UserInternalMail uimT = objectMapperWrapperForVisitor.convertToUserInternalMail(valueOri);
				toMeList.add(uimT);
			}
		}
		return toMeList;
	}
	//查看本人的已发送邮件列表
	public List<UserInternalMail> getUserInternalMailFromMe(String userMailStr) {
		List<UserInternalMail> toMeList = new ArrayList<UserInternalMail>();
		
		String keyTo = RedisKeysForVisitor.getUserInternalMailFromMeKey() + userMailStr;
		Map<Object, Object> entries = compressStringRedisVisitorTemplate.opsForHash().entries(keyTo);
		
		String keyA = RedisKeysForVisitor.getUserInternalMailAlways();
		
		if (null != entries) {
			for(Object entry : entries.keySet()) {
				String uimIdStr = (String)entry;
				String valueOri = (String) compressStringRedisVisitorTemplate.opsForHash().get(keyA, uimIdStr);
				UserInternalMail uimT = objectMapperWrapperForVisitor.convertToUserInternalMail(valueOri);
				toMeList.add(uimT);
			}
		}
		return toMeList;
	}
	//查看本人的已回复邮件列表
	public List<UserInternalMail> getUserInternalMailRepliedFromMe(String userMailStr) {
		List<UserInternalMail> toMeList = new ArrayList<UserInternalMail>();
		
		String keyTo = RedisKeysForVisitor.getUserInternalMailRepliedFromMeKey() + userMailStr;
		Map<Object, Object> entries = compressStringRedisVisitorTemplate.opsForHash().entries(keyTo);
		
		String keyA = RedisKeysForVisitor.getUserInternalMailAlways();
		
		if (null != entries) {
			for(Object entry : entries.keySet()) {
				String uimIdStr = (String)entry;
				String valueOri = (String) compressStringRedisVisitorTemplate.opsForHash().get(keyA, uimIdStr);
				UserInternalMail uimT = objectMapperWrapperForVisitor.convertToUserInternalMail(valueOri);
				toMeList.add(uimT);
			}
		}
		return toMeList;
	}
	
	//获取我的未读邮件的个数
	public Integer getUserInternalMailUnreadCount(String userMailStr) {
		Integer count = 0;
		String keyTo = RedisKeysForVisitor.getUserInternalMailToMeKey() + userMailStr;
		Map<Object, Object> entries = compressStringRedisVisitorTemplate.opsForHash().entries(keyTo);
		if (entries != null && entries.size() > 0) {
			count = entries.size();
		}
		return count;
	}
	//获取我发送的邮件的个数
	public Integer getUserInternalMailFromMeCount(String userMailStr) {
		Integer count = 0;
		String keyTo = RedisKeysForVisitor.getUserInternalMailFromMeKey() + userMailStr;
		Map<Object, Object> entries = compressStringRedisVisitorTemplate.opsForHash().entries(keyTo);
		if (entries != null && entries.size() > 0) {
			count = entries.size();
		}
		return count;
	}
	//获取我回复的邮件的个数
	public Integer getUserInternalMailRepliedFromMeCount(String userMailStr) {
		Integer count = 0;
		String keyTo = RedisKeysForVisitor.getUserInternalMailRepliedFromMeKey() + userMailStr;
		Map<Object, Object> entries = compressStringRedisVisitorTemplate.opsForHash().entries(keyTo);
		if (entries != null && entries.size() > 0) {
			count = entries.size();
		}
		return count;
	}
	
	//挪出我的未读列表
	public void deleteUserInternalMailUnread(String userMailStr, String uimIdStr) {
		String keyTo = RedisKeysForVisitor.getUserInternalMailToMeKey() + userMailStr;
		compressStringRedisVisitorTemplate.opsForHash().delete(keyTo, uimIdStr);
	}
}
