package org.visitor.appportal.service.newsite.redis;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.visitor.appportal.redis.ObjectMapperWrapperForVisitor;
import org.visitor.appportal.redis.RedisKeysForVisitor;
import org.visitor.appportal.visitor.domain.Container;

@Service("containerRedisService")
public class ContainerRedisService {
	@Autowired
	private StringRedisTemplate stringRedisVisitorTemplate;
	@Autowired
	private StringRedisTemplate compressStringRedisVisitorTemplate;
	@Autowired
	private ObjectMapperWrapperForVisitor objectMapperWrapperForVisitor;
	
	public void saveContainerToRedis(Container container) {
		String key = RedisKeysForVisitor.getContainerKey();
		String keyT = String.valueOf(container.getContainerId().longValue());
		String valueT = objectMapperWrapperForVisitor.convert2String(container);
		
		compressStringRedisVisitorTemplate.opsForHash().put(key, keyT, valueT);
	}
	
	public Container getContainerFromRedisById(Long cId) {
		String key = RedisKeysForVisitor.getContainerKey();
		String keyT = String.valueOf(cId.longValue());
		String valueT = (String) compressStringRedisVisitorTemplate.opsForHash().get(key, keyT);
		
		if (StringUtils.isNotEmpty(valueT)) {
			return objectMapperWrapperForVisitor.convertToContainer(valueT);
		} else {
			return null;
		}
	}
}
