package org.visitor.appportal.service.newsite.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.visitor.appportal.redis.ObjectMapperWrapperForVisitor;
import org.visitor.appportal.redis.RedisKeysForVisitor;
import org.visitor.appportal.visitor.domain.Product;
import org.visitor.appportal.visitor.domain.User;
import org.visitor.appportal.web.utils.WebInfo;

@Service("productRedisService")
public class ProductRedisService {
	@Autowired
	private StringRedisTemplate stringRedisVisitorTemplate;
	@Autowired
	private StringRedisTemplate compressStringRedisVisitorTemplate;
	@Autowired
	private ObjectMapperWrapperForVisitor objectMapperWrapperForVisitor;
	
	public void saveProductToRedis(Product product) {
		String keyT = RedisKeysForVisitor.getVisitorProductInfoKey();
		String valueT = objectMapperWrapperForVisitor.convert2String(product);
		String scoreStr = String.valueOf(product.getProductId().longValue());
		Double score = Double.valueOf(scoreStr);
		compressStringRedisVisitorTemplate.opsForZSet().add(keyT, valueT, score);
	}
	
	public void saveUserProductToRedis(User user, Product product) {
		String keyT = RedisKeysForVisitor.getVisitorUserProductInfoKey();
		String hashKey = product.getProductId().toString() + WebInfo.SPLIT + user.getUserId().toString();
		
		compressStringRedisVisitorTemplate.opsForHash().put(keyT, hashKey, product);
	}
}
