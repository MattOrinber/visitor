package org.visitor.appportal.service.newsite.redis;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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
			if (arr.length > 1) {
				for (String str : arr) {
					String keyH = String.valueOf(count.intValue());
					stringRedisVisitorTemplate.opsForHash().put(key, keyH, str);
					count ++;
				}
			} else {
				stringRedisVisitorTemplate.opsForValue().set(key, ftTemp.getFloopyValue());
			}
			
		} else {
			stringRedisVisitorTemplate.delete(key);
		}
	}
	
	public List<String> getFloopyValueList(String key) {
		List<String> result = new ArrayList<String>();
		
		String keyT = FloopyUtils.FLOOPY_PREFIX + key;
		Map<Object, Object> entries = stringRedisVisitorTemplate.opsForHash().entries(keyT);
		
		Map<String, String> sortedList = new LinkedHashMap<String, String>();
		
		if (null != entries) {
			for(Object entry : entries.keySet()) {
				String keyStr = (String)entry;
				String valueStr = (String)entries.get(entry);
				sortedList.put(keyStr, valueStr);
			}
			
			for (String keyType : sortedList.keySet()) {
				String tmpResult = sortedList.get(keyType);
				result.add(tmpResult);
			}
		}
		
		return result;
	}
	
	public String getFloopyValueSingle(String key) {
		String keyT = FloopyUtils.FLOOPY_PREFIX + key;
		String result = stringRedisVisitorTemplate.opsForValue().get(keyT);
		return result;
	}
	
	public List<String> getFloopySingularGeneratedList(Integer singularValue) {
		List<String> accomodatesList = new ArrayList<String>();
		
		if (singularValue.intValue() > 0) {
			Integer countAccomodates = 1;
			
			while (countAccomodates.intValue() < singularValue.intValue()) {
				accomodatesList.add(String.valueOf(countAccomodates.intValue()));
				countAccomodates ++;
			}
			
			accomodatesList.add(String.valueOf(countAccomodates.intValue())+"+");
		}
		
		return accomodatesList;
	}
}
