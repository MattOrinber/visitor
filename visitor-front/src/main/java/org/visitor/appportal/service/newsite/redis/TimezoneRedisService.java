package org.visitor.appportal.service.newsite.redis;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.visitor.appportal.redis.RedisKeysForVisitor;
import org.visitor.appportal.visitor.domain.TimeZone;

@Service("timezoneRedisService")
public class TimezoneRedisService {
	@Autowired
	private StringRedisTemplate stringRedisVisitorTemplate;
	
	public void saveTimeZoneRedis(List<TimeZone> list) {
		String keyOne = RedisKeysForVisitor.getVisitorTimeZoneKey();
		
		for (TimeZone tz : list) {
			stringRedisVisitorTemplate.opsForHash().put(keyOne, tz.getTimeZoneCity(), tz.getTimeZoneId().toString()+RedisKeysForVisitor.getVisitorRedisSplit()+tz.getTimeZoneName());
		}
	}
	
	public Integer getTimeZoneId(String city) {
		String keyOne = RedisKeysForVisitor.getVisitorTimeZoneKey();
		String intValueTotal = (String) stringRedisVisitorTemplate.opsForHash().get(keyOne, city);
		String intValueString = intValueTotal.split(RedisKeysForVisitor.getVisitorRedisSplit())[0];
		Integer intValue = Integer.valueOf(intValueString);
		return intValue;
	}
	
	public String getTimeZoneName(String city) {
		String keyOne = RedisKeysForVisitor.getVisitorTimeZoneKey();
		String intValueTotal = (String) stringRedisVisitorTemplate.opsForHash().get(keyOne, city);
		String intValueString = intValueTotal.split(RedisKeysForVisitor.getVisitorRedisSplit())[1];
		return intValueString;
	}
}
