package org.visitor.appportal.service.newsite.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.visitor.appportal.redis.ObjectMapperWrapperForVisitor;
import org.visitor.appportal.redis.RedisKeysForVisitor;
import org.visitor.appportal.visitor.domain.Product;
import org.visitor.appportal.visitor.domain.ProductOrder;
import org.visitor.appportal.visitor.domain.ProductPayOrder;
import org.visitor.appportal.visitor.domain.User;

@Service("orderRedisService")
public class OrderRedisService {
	@Autowired
	private StringRedisTemplate stringRedisVisitorTemplate;
	@Autowired
	private StringRedisTemplate compressStringRedisVisitorTemplate;
	@Autowired
	private ObjectMapperWrapperForVisitor objectMapperWrapperForVisitor;
	
	public void saveUserOrders(User user, ProductOrder pOrder) {
		String key = RedisKeysForVisitor.getUserOrderKey() + RedisKeysForVisitor.getVisitorRedisWeakSplit() + user.getUserEmail();
		String keyT = String.valueOf(pOrder.getOrderId().longValue());
		
		String valueT = objectMapperWrapperForVisitor.convert2String(pOrder);
		
		compressStringRedisVisitorTemplate.opsForHash().put(key, keyT, valueT);
	}
	
	public ProductOrder getUserOrder(User user, Long poId) {
		if (user != null && poId != null) {
			String key = RedisKeysForVisitor.getUserOrderKey() + RedisKeysForVisitor.getVisitorRedisWeakSplit() + user.getUserEmail();
			String keyT = String.valueOf(poId.longValue());
			String valueT = (String) compressStringRedisVisitorTemplate.opsForHash().get(key, keyT);
			if (StringUtils.isNotEmpty(valueT)) {
				return objectMapperWrapperForVisitor.convertToProductOrder(valueT);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	public void saveProductOrders(ProductOrder pOrder) {
		String key = RedisKeysForVisitor.getProductOrderKey() + RedisKeysForVisitor.getVisitorRedisWeakSplit() + pOrder.getOrderProductId();
		String keyT = String.valueOf(pOrder.getOrderId().longValue());
		
		String valueT = objectMapperWrapperForVisitor.convert2String(pOrder);
		
		compressStringRedisVisitorTemplate.opsForHash().put(key, keyT, valueT);
	}
	
	public List<ProductOrder> getUserOrders(User user) {
		List<ProductOrder> list = new ArrayList<ProductOrder>(); 
		String key = RedisKeysForVisitor.getUserOrderKey() + RedisKeysForVisitor.getVisitorRedisWeakSplit() + user.getUserEmail();
		
		Map<Object, Object> entries = compressStringRedisVisitorTemplate.opsForHash().entries(key);
		
		if (null != entries) {
			for(Object entry : entries.keySet()) {
				String valueStr = (String)entries.get(entry);
				ProductOrder po = objectMapperWrapperForVisitor.convertToProductOrder(valueStr);
				list.add(po);
			}
		}
		
		return list;
	}
	
	public List<ProductOrder> getProductOrders(Product product) {
		List<ProductOrder> list = new ArrayList<ProductOrder>(); 
		String key = RedisKeysForVisitor.getProductOrderKey() + RedisKeysForVisitor.getVisitorRedisWeakSplit() + product.getProductId();
		
		Map<Object, Object> entries = compressStringRedisVisitorTemplate.opsForHash().entries(key);
		
		if (null != entries) {
			for(Object entry : entries.keySet()) {
				String valueStr = (String)entries.get(entry);
				ProductOrder po = objectMapperWrapperForVisitor.convertToProductOrder(valueStr);
				list.add(po);
			}
		}
		
		return list;
	}
	
	//缓存1天， 拿不到就去库里取
	public void saveProductPayOrderById(ProductPayOrder ppo) {
		String key = RedisKeysForVisitor.getProductPayOrderKey() + ppo.getPayOrderId();
		String valueT = objectMapperWrapperForVisitor.convert2String(ppo);
		stringRedisVisitorTemplate.opsForValue().set(key, valueT);
		stringRedisVisitorTemplate.expire(key, 1, TimeUnit.DAYS);
	}
	
	public void deleteProductPayOrderById(ProductPayOrder ppo) {
		String key = RedisKeysForVisitor.getProductPayOrderKey() + ppo.getPayOrderId();
		stringRedisVisitorTemplate.delete(key);
	}
	
	public ProductPayOrder getProductPayOrderById(Long ppoId) {
		String key = RedisKeysForVisitor.getProductPayOrderKey() + ppoId;
		String valueT = stringRedisVisitorTemplate.opsForValue().get(key);
		if (StringUtils.isNotEmpty(valueT)) {
			return objectMapperWrapperForVisitor.convertToProductPayOrder(valueT);
		} else {
			return null;
		}
	}
	
	
	public void saveTxnId(String txnIdStr) {
		String key = RedisKeysForVisitor.getPaypalTxnIdKey();
		String keyT = txnIdStr;
		String valueT = "-";
		stringRedisVisitorTemplate.opsForHash().put(key, keyT, valueT);
	}
	
	public Boolean checkTxnId(String txnIdStr) {
		String key = RedisKeysForVisitor.getPaypalTxnIdKey();
		String keyT = txnIdStr;
		return stringRedisVisitorTemplate.opsForHash().hasKey(key, keyT);
	}
	
	public void setPayPalTokenUser(User user, String tokenStr) {
		String key = RedisKeysForVisitor.getPaypalUserToken() + tokenStr;
		String valueT = user.getUserEmail();
		
		stringRedisVisitorTemplate.opsForValue().set(key, valueT);
		stringRedisVisitorTemplate.expire(key, 3, TimeUnit.HOURS);
	}
	
	public User getPayPalTokenUser(String tokenStr) {
		String key = RedisKeysForVisitor.getPaypalUserToken() + tokenStr;
		String emailStr = (String)stringRedisVisitorTemplate.opsForValue().get(key);
		if (StringUtils.isNotEmpty(emailStr)) {
			String keyFirst = RedisKeysForVisitor.getVisitorSiteUserPasswordFirstKey();
			String result = (String)compressStringRedisVisitorTemplate.opsForHash().get(keyFirst, emailStr);
			return objectMapperWrapperForVisitor.convertToUser(result);
		} else {
			return null;
		}
	}
}
