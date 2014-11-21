package org.visitor.appportal.service.newsite.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.visitor.appportal.redis.FloopyUtils;
import org.visitor.appportal.redis.ObjectMapperWrapperForVisitor;
import org.visitor.appportal.redis.RedisKeysForVisitor;
import org.visitor.appportal.visitor.domain.FloopyThing;

@Service("floopyThingRedisService")
public class FloopyThingRedisService {
	@Autowired
	private StringRedisTemplate stringRedisVisitorTemplate;
	@Autowired
	private StringRedisTemplate compressStringRedisVisitorTemplate;
	@Autowired
	private ObjectMapperWrapperForVisitor objectMapperWrapperForVisitor;
	
	public void saveFloopyThingToRedis(FloopyThing ftTemp) {
		String key = FloopyUtils.FLOOPY_PREFIX + ftTemp.getFloopyKey();
		Integer status = ftTemp.getFloopyStatus();
		if (status.intValue() == 0) {
			Integer count = 0;
			String valueTotal = ftTemp.getFloopyValue();
			String[] arr = valueTotal.split(RedisKeysForVisitor.getVisitorRedisWeakSplit());
			for (String str : arr) {
				String keyH = String.valueOf(count.intValue());
				stringRedisVisitorTemplate.opsForHash().put(key, keyH, str);
				count ++;
			}
			
		} else {
			stringRedisVisitorTemplate.delete(key);
		}
	}
	
	public List<String> getFloopyValueList(String key) {
		List<String> result = new ArrayList<String>();
		
		String keyT = FloopyUtils.FLOOPY_PREFIX + key;
		Map<Object, Object> entries = stringRedisVisitorTemplate.opsForHash().entries(keyT);
		if (null != entries) {
			for(Object entry : entries.entrySet()) {
				String valueStr = (String)entries.get(entry);
				result.add(valueStr);
			}
		}
		
		return result;
	}
}
