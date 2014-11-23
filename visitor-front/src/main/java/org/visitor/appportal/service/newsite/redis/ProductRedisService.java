package org.visitor.appportal.service.newsite.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.visitor.appportal.redis.ObjectMapperWrapperForVisitor;
import org.visitor.appportal.redis.RedisKeysForVisitor;
import org.visitor.appportal.visitor.domain.Product;
import org.visitor.appportal.visitor.domain.User;

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
		String keyT = RedisKeysForVisitor.getVisitorUserProductInfoKey(user.getUserId().toString());
		String hashKey = product.getProductId().toString();
		
		String valueT = objectMapperWrapperForVisitor.convert2String(user);
		
		compressStringRedisVisitorTemplate.opsForHash().put(keyT, hashKey, valueT);
	}
	
	public List<Product> getUserProducts(User user) {
		List<Product> result = new ArrayList<Product>();
		String keyT = RedisKeysForVisitor.getVisitorUserProductInfoKey(user.getUserId().toString());
		Map<Object, Object> entries = compressStringRedisVisitorTemplate.opsForHash().entries(keyT);
		
		if (null != entries) {
			for(Object entry : entries.entrySet()) {
				String valueStr = (String)entries.get(entry);
				Product productT = objectMapperWrapperForVisitor.convertToProduct(valueStr);
				result.add(productT);
			}
		}
		
		return result;
	}
}
