/**
 * 
 */
package org.visitor.appportal.redis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import org.visitor.appportal.domain.Advertise;

/**
 * @author mengw
 *
 */
@Repository
public class AdvertiseRedisRepository {
	protected static final Logger log = LoggerFactory.getLogger(AdvertiseRedisRepository.class);
	
	@Autowired
	private ObjectMapperWrapper objectMapperWrapper;
	@Autowired
	private StringRedisTemplate compressStringRedisTemplate;	
	
	/**
	 * 
	 */
	public AdvertiseRedisRepository() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 本方法用于将广告存储到redis中，由于广告类没有写service方法，所以需要在这里添加其外键的关联关系。
	 * @param advertise
	 */
	public void setAdvertise(Advertise advertise){
		String key = RedisKeys.getAdvertiseKey();
		
		String value = objectMapperWrapper.convert2String(advertise.copy());		
		if(StringUtils.isNotBlank(value)) {
			/*由于我们可以方便地取到广告ID，所以不需要把其作为一个参数来传递*/
			compressStringRedisTemplate.opsForHash().put(key, String.valueOf(advertise.getAdvertiseId()), value);
		}
	}

	/**
	 * 和图书不同，这里不需要找其关联的章节，因为广告嘛，没有关联
	 * @param pid
	 * @return
	 */
	public Advertise getAdvertiseById(Long pid) {
		String key = RedisKeys.getAdvertiseKey();
		key = (String)compressStringRedisTemplate.opsForHash().get(key, String.valueOf(pid));
		return this.objectMapperWrapper.convert2Advertise(key);
	}

	public List<Advertise> getAdvertiseByAdvertiseIds(Set<Long> keySet) {
		String key = RedisKeys.getAdvertiseKey();
		Collection<Object> hashKeys = new ArrayList<Object>();
		for(Long id : keySet) {
			hashKeys.add(String.valueOf(id));
		}
		hashKeys = this.compressStringRedisTemplate.opsForHash().multiGet(key, hashKeys);
		if(null != hashKeys) {
			List<Advertise> list = new ArrayList<Advertise>();
			for(Object s : hashKeys) {
				list.add(this.objectMapperWrapper.convert2Advertise((String)s));
			}
			return list;
		}
		return null;
	}

}
