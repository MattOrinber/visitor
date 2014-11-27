package org.visitor.appportal.service.newsite.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.visitor.appportal.redis.ObjectMapperWrapperForVisitor;
import org.visitor.appportal.redis.RedisKeysForVisitor;
import org.visitor.appportal.visitor.domain.Product;
import org.visitor.appportal.visitor.domain.ProductAddress;
import org.visitor.appportal.visitor.domain.ProductDetailInfo;
import org.visitor.appportal.visitor.domain.ProductMultiPrice;
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
	
	public Product getUserProductFromRedis(User user, String productIdStr) {
		String keyT = RedisKeysForVisitor.getVisitorUserProductInfoKey(user.getUserId().toString());
		String hashKey = productIdStr;
		String valueT = (String) compressStringRedisVisitorTemplate.opsForHash().get(keyT, hashKey);
		if (StringUtils.isEmpty(valueT)) {
			return null;
		}
		Product productT = objectMapperWrapperForVisitor.convertToProduct(valueT);
		return productT;
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
	
	public void saveProductAddressToRedis(ProductAddress pa) {
		String key = RedisKeysForVisitor.getVisitorProductAddressInfoKey();
		String keyT = pa.getPaProductid().toString();
		String valueT = objectMapperWrapperForVisitor.convert2String(pa);
		compressStringRedisVisitorTemplate.opsForHash().put(key, keyT, valueT);
	}
	
	public ProductAddress getProductAddressFromRedis(Long pid) {
		String key = RedisKeysForVisitor.getVisitorProductAddressInfoKey();
		String keyT = pid.toString();
		String valueT = (String) compressStringRedisVisitorTemplate.opsForHash().get(key, keyT);
		
		ProductAddress pa = objectMapperWrapperForVisitor.convertToProductAddress(valueT);
		return pa;
	}
	
	//detail info part
	public void saveProductDetailInfoToRedis(ProductDetailInfo pdi) {
		String key = RedisKeysForVisitor.getVisitorProductDetailInfoKey();
		String keyT = String.valueOf(pdi.getPriProductId().longValue());
		String valueT = objectMapperWrapperForVisitor.convert2String(pdi);
		compressStringRedisVisitorTemplate.opsForHash().put(key, keyT, valueT);
	}
	
	public ProductDetailInfo getProductDetailInfoUsingProductId(Long pid) {
		String key = RedisKeysForVisitor.getVisitorProductDetailInfoKey();
		String keyT = String.valueOf(pid.longValue());
		
		String valueT = (String) compressStringRedisVisitorTemplate.opsForHash().get(key, keyT);
		ProductDetailInfo pdi = objectMapperWrapperForVisitor.convertToProductDetailInfo(valueT);
		return pdi;
	}
	
	//multi price set part
	public void saveProductMultiPriceToRedis(ProductMultiPrice pmp) {
		String key = RedisKeysForVisitor.getVisitorProductMultipriceInfoKey(pmp.getPmpProductId());
		String keyT = pmp.getPmpProductPriceKey();
		String valueT = objectMapperWrapperForVisitor.convert2String(pmp);
		compressStringRedisVisitorTemplate.opsForHash().put(key, keyT, valueT);
	}
	
	public ProductMultiPrice getProductMultiPriceSetByProductIdAndKey(Long pid, String keyStr) {
		String key = RedisKeysForVisitor.getVisitorProductMultipriceInfoKey(pid);
		String keyT = keyStr;
		String valueT = (String)compressStringRedisVisitorTemplate.opsForHash().get(key, keyT);
		
		ProductMultiPrice pmp = objectMapperWrapperForVisitor.convertToProductMultiPrice(valueT);
		return pmp;
	}
	
	public List<ProductMultiPrice> getAllMultiPricesSetsForProduct(Long pid) {
		List<ProductMultiPrice> list= new ArrayList<ProductMultiPrice>();
		String key = RedisKeysForVisitor.getVisitorProductMultipriceInfoKey(pid);
		
		Map<Object, Object> objMap = compressStringRedisVisitorTemplate.opsForHash().entries(key);
		for (Object obj: objMap.keySet()) {
			String valueT = (String) objMap.get(obj);
			
			ProductMultiPrice pmp = objectMapperWrapperForVisitor.convertToProductMultiPrice(valueT);
			list.add(pmp);
		}
		
		return list;
	}
	
	public Boolean ifContainsPriceKeySet(Long pid, String keyStr) {
		String key = RedisKeysForVisitor.getVisitorProductMultipriceInfoKey(pid);
		String keyT = keyStr;
		return compressStringRedisVisitorTemplate.opsForHash().hasKey(key, keyT);
	}
	
	public void removePriceKey(Long pid, String keyStr) {
		String key = RedisKeysForVisitor.getVisitorProductMultipriceInfoKey(pid);
		String keyT = keyStr;
		compressStringRedisVisitorTemplate.opsForHash().delete(key, keyT);
	}
}
