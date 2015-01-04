package org.visitor.appportal.service.newsite.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.visitor.appportal.redis.ObjectMapperWrapperForVisitor;
import org.visitor.appportal.redis.RedisKeysForVisitor;
import org.visitor.appportal.visitor.beans.view.PageProduct;
import org.visitor.appportal.visitor.domain.Product;
import org.visitor.appportal.visitor.domain.ProductAddress;
import org.visitor.appportal.visitor.domain.ProductDetailInfo;
import org.visitor.appportal.visitor.domain.ProductMultiPrice;
import org.visitor.appportal.visitor.domain.ProductOperation;
import org.visitor.appportal.visitor.domain.ProductPicture;
import org.visitor.appportal.visitor.domain.User;

@Service("productRedisService")
public class ProductRedisService {
	@Autowired
	private StringRedisTemplate stringRedisVisitorTemplate;
	@Autowired
	private StringRedisTemplate compressStringRedisVisitorTemplate;
	@Autowired
	private ObjectMapperWrapperForVisitor objectMapperWrapperForVisitor;
	
	
	//indexed by cities
	public void saveProductToRedis(Product product) {
		
		String cityStr = product.getProductCity();
		
		String cityKey = RedisKeysForVisitor.getVisitorProductCityKey();
		if (!compressStringRedisVisitorTemplate.opsForHash().hasKey(cityKey, cityStr)) {
			compressStringRedisVisitorTemplate.opsForHash().put(cityKey, cityStr, cityStr);
		}
		
		String keyP = RedisKeysForVisitor.getVisitorProductInfoKey();
		String scoreP = String.valueOf(product.getProductId().longValue());
		String valueT = objectMapperWrapperForVisitor.convert2String(product);
		compressStringRedisVisitorTemplate.opsForHash().put(keyP, scoreP, valueT);
	}
	
	public void saveProductCityOrderToRedis(Product product) {
		String cityStr = product.getProductCity();
		
		String keyNewOld = RedisKeysForVisitor.getVisitorProductCityNewOldKey() + RedisKeysForVisitor.getVisitorRedisWeakSplit() + cityStr;
		String keyPrice = RedisKeysForVisitor.getVisitorProductCityPriceKey() + RedisKeysForVisitor.getVisitorRedisWeakSplit() + cityStr;
		
		Double scoreNewOld = new Double(product.getProductId().doubleValue());
		String productIdStr = String.valueOf(product.getProductId());
		Double scorePrice = Double.valueOf(product.getProductBaseprice());
		
		stringRedisVisitorTemplate.opsForZSet().add(keyPrice, productIdStr, scorePrice.doubleValue());
		stringRedisVisitorTemplate.opsForZSet().add(keyNewOld, productIdStr, scoreNewOld.doubleValue());
	}
	
	public List<String> getCities() {
		String cityKey = RedisKeysForVisitor.getVisitorProductCityKey();
		Map<Object, Object> entries = compressStringRedisVisitorTemplate.opsForHash().entries(cityKey);
		List<String> result = null;
		
		if (null != entries) {
			result = new ArrayList<String>();
			for(Object entry : entries.keySet()) {
				String valueStr = (String)entries.get(entry);
				result.add(valueStr);
			}
		}
		return result;
		
	}
	
	public PageProduct getCityProductListSize(String cityStr, Integer orderType, Long pageSize) {
		String keyT = "";
		PageProduct pageInfo = new PageProduct(); 
		
		switch(orderType) {
		case 0:
			keyT = RedisKeysForVisitor.getVisitorProductCityNewOldKey() + RedisKeysForVisitor.getVisitorRedisWeakSplit() + cityStr;
			break;
		case 1:
			keyT = RedisKeysForVisitor.getVisitorProductCityNewOldKey() + RedisKeysForVisitor.getVisitorRedisWeakSplit() + cityStr;
			break;
		case 2:
			keyT = RedisKeysForVisitor.getVisitorProductCityPriceKey() + RedisKeysForVisitor.getVisitorRedisWeakSplit() + cityStr;
			break;
		case 3:
			keyT = RedisKeysForVisitor.getVisitorProductCityPriceKey() + RedisKeysForVisitor.getVisitorRedisWeakSplit() + cityStr;
			break;
		default:
			break;
		}
		if (StringUtils.isNotEmpty(keyT)) {
			Long allSize = stringRedisVisitorTemplate.opsForZSet().size(keyT);
			Long pageNum = allSize/pageSize; 
			Long pageResidual = allSize%pageSize; 
			if (pageResidual > 0) {
				pageNum ++;;
			}
			if (pageNum > 1) {
				pageInfo.setIfPager(new Integer(1));
			} else {
				pageInfo.setIfPager(new Integer(0));
			}
			pageInfo.setTotalSize(allSize);
			pageInfo.setPageNum(pageNum);
		}
		
		return pageInfo;
	}
	
	public List<Product> getProductListFromRedis(String cityStr, Integer orderType, Long pageIdx, Long pageSize) {
		String keyT = "";
		List<Product> result = null;
		
		switch(orderType) {
		case 0:
			keyT = RedisKeysForVisitor.getVisitorProductCityNewOldKey() + RedisKeysForVisitor.getVisitorRedisWeakSplit() + cityStr;
			break;
		case 1:
			keyT = RedisKeysForVisitor.getVisitorProductCityNewOldKey() + RedisKeysForVisitor.getVisitorRedisWeakSplit() + cityStr;
			break;
		case 2:
			keyT = RedisKeysForVisitor.getVisitorProductCityPriceKey() + RedisKeysForVisitor.getVisitorRedisWeakSplit() + cityStr;
			break;
		case 3:
			keyT = RedisKeysForVisitor.getVisitorProductCityPriceKey() + RedisKeysForVisitor.getVisitorRedisWeakSplit() + cityStr;
			break;
		default:
			break;
		}
		if (StringUtils.isNotEmpty(keyT)) {
			long start = (pageIdx-1) * pageSize;
			long end = pageIdx * pageSize - 1;
			
			Set<String> listIds = null;
			if (orderType == 1 || orderType == 2) {
				listIds = stringRedisVisitorTemplate.opsForZSet().range(keyT, start, end);
			} else {
				listIds = stringRedisVisitorTemplate.opsForZSet().reverseRange(keyT, start, end);
			}
			
			if (null != listIds && listIds.size() > 0) {
				result = new ArrayList<Product>();
				for (String pid : listIds) {
					String keyP = RedisKeysForVisitor.getVisitorProductInfoKey();
					String scoreP = pid;
					String valueT = (String) compressStringRedisVisitorTemplate.opsForHash().get(keyP, scoreP);
					Product productT = objectMapperWrapperForVisitor.convertToProduct(valueT);
					result.add(productT);
				}
			}
		}
		return result;
	}
	
	public Product getProductFromRedisUsingCity(Long pid, String cityStr) {
		String keyT = RedisKeysForVisitor.getVisitorProductInfoKey() + RedisKeysForVisitor.getVisitorRedisWeakSplit() + cityStr;
		String scoreStr = String.valueOf(pid.longValue());
		String valueT = (String) compressStringRedisVisitorTemplate.opsForHash().get(keyT, scoreStr);
		return objectMapperWrapperForVisitor.convertToProduct(valueT);
	}
	
	public Product getProductFromRedis(Long pid) {
		String keyT = RedisKeysForVisitor.getVisitorProductInfoKey();
		String scoreStr = String.valueOf(pid.longValue());
		String valueT = (String) compressStringRedisVisitorTemplate.opsForHash().get(keyT, scoreStr);
		return objectMapperWrapperForVisitor.convertToProduct(valueT);
	}
	
	public void saveUserProductToRedis(User user, Product product) {
		String keyT = RedisKeysForVisitor.getVisitorUserProductInfoKey(user.getUserId().toString());
		String hashKey = product.getProductId().toString();
		
		String valueT = objectMapperWrapperForVisitor.convert2String(product);
		
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
			for(Object entry : entries.keySet()) {
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
	
	
	public void setProductOperationToRedis(ProductOperation entity) {
		String key = RedisKeysForVisitor.getVisitorProductOperationKey() + RedisKeysForVisitor.getVisitorRedisWeakSplit() + String.valueOf(entity.getPoProductid().longValue());
		String keyT = String.valueOf(entity.getPoOperationid());
		String valueT = objectMapperWrapperForVisitor.convert2String(entity);
		compressStringRedisVisitorTemplate.opsForHash().put(key, keyT, valueT);
	}
	
	public ProductOperation getProductOperationFromRedis(String pidStr, String poIdStr) {
		String key = RedisKeysForVisitor.getVisitorProductOperationKey() + RedisKeysForVisitor.getVisitorRedisWeakSplit() + pidStr;
		String keyT = poIdStr;
		String valueT = (String) compressStringRedisVisitorTemplate.opsForHash().get(key, keyT);
		return objectMapperWrapperForVisitor.convertToProductOperation(valueT);
	}
	
	public void deleteProductOperationToRedis(ProductOperation entity) {
		String key = RedisKeysForVisitor.getVisitorProductOperationKey() + RedisKeysForVisitor.getVisitorRedisWeakSplit() + String.valueOf(entity.getPoProductid().longValue());
		String keyT = String.valueOf(entity.getPoOperationid());
		compressStringRedisVisitorTemplate.opsForHash().delete(key, keyT);
	}
	
	public List<ProductOperation> getProductOperationList(Long pid) {
		List<ProductOperation> list = new ArrayList<ProductOperation>();
		String key = RedisKeysForVisitor.getVisitorProductOperationKey() + RedisKeysForVisitor.getVisitorRedisWeakSplit() + String.valueOf(pid.longValue());
		
		Map<Object, Object> objMap = compressStringRedisVisitorTemplate.opsForHash().entries(key);
		for (Object obj: objMap.keySet()) {
			String valueT = (String) objMap.get(obj);
			
			ProductOperation poT = objectMapperWrapperForVisitor.convertToProductOperation(valueT);
			list.add(poT);
		}
		
		return list;
	}
	
	//product picture
	
	public Boolean containsPicture(Long pid) {
		String key = RedisKeysForVisitor.getVisitorProductPictureKey() + RedisKeysForVisitor.getVisitorRedisWeakSplit() + String.valueOf(pid.longValue());
		return compressStringRedisVisitorTemplate.hasKey(key);
	}
	
	public void setProductPictureToRedis(ProductPicture productPic) {
		String key = RedisKeysForVisitor.getVisitorProductPictureKey() + RedisKeysForVisitor.getVisitorRedisWeakSplit() + String.valueOf(productPic.getProductPicProductId().longValue());
		String keyT = String.valueOf(productPic.getProductPicId().longValue());
		String valueT = objectMapperWrapperForVisitor.convert2String(productPic);
		
		compressStringRedisVisitorTemplate.opsForHash().put(key, keyT, valueT);
	}
	
	public ProductPicture getProductPictureFromRedis(Long pid, Long picId) {
		String key = RedisKeysForVisitor.getVisitorProductPictureKey() + RedisKeysForVisitor.getVisitorRedisWeakSplit() + String.valueOf(pid.longValue());
		String keyT = String.valueOf(picId.longValue());
		String valueT = (String) compressStringRedisVisitorTemplate.opsForHash().get(key, keyT);
		ProductPicture poT = null;
		if (StringUtils.isNotEmpty(valueT)) {
			poT = objectMapperWrapperForVisitor.convertToProductPicture(valueT);
		}
		
		return poT;
	}
	
	public void deleteProductPictureFromRedis(Long pid, Long picId) {
		String key = RedisKeysForVisitor.getVisitorProductPictureKey() + RedisKeysForVisitor.getVisitorRedisWeakSplit() + String.valueOf(pid.longValue());
		String keyT = String.valueOf(picId.longValue());
		
		compressStringRedisVisitorTemplate.opsForHash().delete(key, keyT);
		
		if (compressStringRedisVisitorTemplate.opsForHash().size(key) == 0) {
			compressStringRedisVisitorTemplate.delete(key);
		}
	}
	
	public List<ProductPicture> getPictureListOfOneProduct(Long pid) {
		List<ProductPicture> list = new ArrayList<ProductPicture>();
		String key = RedisKeysForVisitor.getVisitorProductPictureKey() + RedisKeysForVisitor.getVisitorRedisWeakSplit() + String.valueOf(pid.longValue());
		
		Map<Object, Object> objMap = compressStringRedisVisitorTemplate.opsForHash().entries(key);
		for (Object obj: objMap.keySet()) {
			String valueT = (String) objMap.get(obj);
			
			ProductPicture poT = objectMapperWrapperForVisitor.convertToProductPicture(valueT);
			list.add(poT);
		}
		
		return list;
	}
}
