package org.visitor.appportal.service.newsite.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	
	public List<TimeZone> getAllTimezones() {
		String keyOne = RedisKeysForVisitor.getVisitorTimeZoneKey();
		Map<Object, Object> mapAll = stringRedisVisitorTemplate.opsForHash().entries(keyOne);
		
		List<TimeZone> listRes = new ArrayList<TimeZone>();
		
		for (Object objT : mapAll.keySet()) {
			String valueT = (String) mapAll.get(objT);
			String cityT = (String)objT;
			String[] bundles = valueT.split(RedisKeysForVisitor.getVisitorRedisSplit());
			
			Integer idT = Integer.valueOf(bundles[0]);
			String nameT = bundles[1];
			
			TimeZone tzT = new TimeZone();
			tzT.setTimeZoneId(idT);
			tzT.setTimeZoneCity(cityT);
			tzT.setTimeZoneName(nameT);
			listRes.add(tzT);
		}
		
		return listRes;
	}
}
