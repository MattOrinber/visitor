package org.visitor.appportal.service.newsite.redis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.visitor.appportal.redis.RedisKeysForVisitor;
import org.visitor.appportal.visitor.domain.VisitorLanguage;

@Service("visitorLanguageRedisService")
public class VisitorLanguageRedisService {
	@Autowired
	private StringRedisTemplate stringRedisVisitorTemplate;
	
	public void saveAllVisitorLanguage(List<VisitorLanguage> list) {
		String keyOne = RedisKeysForVisitor.getVisitorLanguageKey();
		
		for (VisitorLanguage vl : list) {
			stringRedisVisitorTemplate.opsForHash().put(keyOne, String.valueOf(vl.getLanguageSerial().longValue()), vl.getLanguageName());
		}
	}
	
	public String getSpecificLanguage(Long serailNum) {
		String keyOne = RedisKeysForVisitor.getVisitorLanguageKey();
		
		String result = (String)stringRedisVisitorTemplate.opsForHash().get(keyOne, String.valueOf(serailNum.longValue()));
		
		return result;
	}
}
