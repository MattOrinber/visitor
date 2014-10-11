/**
 * 
 */
package org.visitor.appportal.redis;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import org.visitor.appportal.domain.ProductAcrossRecommend;
import org.visitor.util.AppStringUtils;

/**
 * @author mengw
 *
 */
@Repository
public class ProductAcrossRecommendRedisRepository {
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private ObjectMapperWrapper objectMapperWrapper;
	/**
	 * 
	 */
	public ProductAcrossRecommendRedisRepository() {
		// TODO Auto-generated constructor stub
	}

	public Collection<? extends Long> getProductAcrossRecommendIds(Long productId, 
			int serviceId, int siteId, Long platformId, Integer maxCount) {
		Set<Long> set = new LinkedHashSet<Long>();
		String key = RedisKeys.getProductAcrossRecommandKey();
		key = (String)stringRedisTemplate.opsForHash().get(key, RedisKeys.getProductAcrossRecommandFieldKey(productId, serviceId, siteId, platformId));
		if(StringUtils.isNotBlank(key)) {
			List<Long> array = objectMapperWrapper.convert2ListLong(key);
			if (!array.isEmpty()) {
				if(null == maxCount) {
					maxCount = array.size();
				}
				if(maxCount < array.size()) {//随机显示产品
					//产生maxCount个随机数,其中的最大值不会超过size-1
					Integer[] indexs = AppStringUtils.getRandomNumbers(maxCount, array.size() - 1);
					for(Integer i : indexs) {
						if(i <= array.size() - 1) {
							set.add(array.get(i));
						}
					}
				} else {
					set.addAll(array);
				}
			}
		}
		return set;
	}
	
	public void setProductAcrossRecommendIds(ProductAcrossRecommend productAcrossRecommend, Long platformId) {
		String productids = productAcrossRecommend.getBehaviors();
		if (StringUtils.isNotEmpty(productids)) {
			String key = RedisKeys.getProductAcrossRecommandFieldKey(
							productAcrossRecommend.getProductId(),
							productAcrossRecommend.getServiceId(),
							productAcrossRecommend.getServiceSiteId(), platformId);
			
			String value = objectMapperWrapper.convert2String(AppStringUtils.formatStringArrayToLongList(productids,","));
			stringRedisTemplate.opsForHash().put(RedisKeys.getProductAcrossRecommandKey(),
					key, value);
		}
	}
}
