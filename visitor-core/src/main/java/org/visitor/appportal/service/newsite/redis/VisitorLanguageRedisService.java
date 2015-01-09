package org.visitor.appportal.service.newsite.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
			stringRedisVisitorTemplate.opsForHash().put(keyOne, String.valueOf(vl.getLanguageSerial().longValue()), vl.getLanguageId()+RedisKeysForVisitor.getVisitorRedisSplit()+vl.getLanguageName());
		}
	}
	
	public String getSpecificLanguage(Long serailNum) {
		String keyOne = RedisKeysForVisitor.getVisitorLanguageKey();
		
		String resultT = (String)stringRedisVisitorTemplate.opsForHash().get(keyOne, String.valueOf(serailNum.longValue()));
		String result = resultT.split(RedisKeysForVisitor.getVisitorRedisSplit())[1];
		
		return result;
	}
	
	public List<VisitorLanguage> getAllLanguages() {
		List<VisitorLanguage> listRes = new ArrayList<VisitorLanguage>();
		String keyOne = RedisKeysForVisitor.getVisitorLanguageKey();
		Map<Object, Object> mapAll = stringRedisVisitorTemplate.opsForHash().entries(keyOne);
		for (Object objT : mapAll.keySet()) {
			String valueT = (String) mapAll.get(objT);
			Long serialT = Long.valueOf((String)objT);
			String[]bundles = valueT.split(RedisKeysForVisitor.getVisitorRedisSplit());
			Integer idT = Integer.valueOf(bundles[0]);
			String nameT = bundles[1];
			
			VisitorLanguage vlT = new VisitorLanguage();
			vlT.setLanguageId(idT);
			vlT.setLanguageSerial(serialT);
			vlT.setLanguageName(nameT);
			
			listRes.add(vlT);
		}
		
		return listRes;
	}
}
