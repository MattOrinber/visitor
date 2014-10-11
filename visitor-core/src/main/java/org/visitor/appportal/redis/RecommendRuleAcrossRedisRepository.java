/**
 * 
 */
package org.visitor.appportal.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import org.visitor.appportal.domain.RecommendRuleAcross;

/**
 * @author mengw
 *
 */
@Repository
public class RecommendRuleAcrossRedisRepository {
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private ObjectMapperWrapper objectMapperWrapper;
	@Autowired
	private StringRedisTemplate compressStringRedisTemplate;
	/**
	 * 
	 */
	public RecommendRuleAcrossRedisRepository() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 设置交叉推荐
	 * @param recommendRuleAcross
	 */
	public void setRecommendRuleAcross (RecommendRuleAcross recommendRuleAcross, Long platformId) {
		String key = "";
		if (recommendRuleAcross.getType().compareTo(RecommendRuleAcross.TYPE_SYSTEM) == 0) {
			key = RedisKeys.getRecommendRuleAcrossFieldKey(recommendRuleAcross.getServiceId(), recommendRuleAcross.getServiceSiteId(),
					null, platformId, RecommendRuleAcross.TYPE_SYSTEM);
		} else if (recommendRuleAcross.getType().compareTo(RecommendRuleAcross.TYPE_FOLDER) == 0) {
			key = RedisKeys.getRecommendRuleAcrossFieldKey(recommendRuleAcross.getServiceId(), recommendRuleAcross.getServiceSiteId(),
					recommendRuleAcross.getServiceFolderId(), platformId, RecommendRuleAcross.TYPE_FOLDER);
		} else if (recommendRuleAcross.getType().compareTo(RecommendRuleAcross.TYPE_PRODUCT) == 0) {
			key = RedisKeys.getRecommendRuleAcrossFieldKey(recommendRuleAcross.getServiceId(), recommendRuleAcross.getServiceSiteId(),
					recommendRuleAcross.getServiceProductId(), platformId, RecommendRuleAcross.TYPE_PRODUCT);
		}
		String value = this.objectMapperWrapper.convert2String(recommendRuleAcross);
		compressStringRedisTemplate.opsForHash().put(RedisKeys.getRecommendRuleAcrossKey(), key, value);		
	}
	
	public void deleteRecommendRuleAcross (RecommendRuleAcross recommendRuleAcross, Long platformId) {
		String key = "";
		if (recommendRuleAcross.getType().compareTo(RecommendRuleAcross.TYPE_SYSTEM) == 0) {
			key = RedisKeys.getRecommendRuleAcrossFieldKey(recommendRuleAcross.getServiceId(), recommendRuleAcross.getServiceSiteId(),
					null, platformId, RecommendRuleAcross.TYPE_SYSTEM);
		} else if (recommendRuleAcross.getType().compareTo(RecommendRuleAcross.TYPE_FOLDER) == 0) {
			key = RedisKeys.getRecommendRuleAcrossFieldKey(recommendRuleAcross.getServiceId(), recommendRuleAcross.getServiceSiteId(),
					recommendRuleAcross.getServiceFolderId(), platformId, RecommendRuleAcross.TYPE_FOLDER);
		} else if (recommendRuleAcross.getType().compareTo(RecommendRuleAcross.TYPE_PRODUCT) == 0) {
			key = RedisKeys.getRecommendRuleAcrossFieldKey(recommendRuleAcross.getServiceId(), recommendRuleAcross.getServiceSiteId(),
					recommendRuleAcross.getServiceProductId(), platformId, RecommendRuleAcross.TYPE_PRODUCT);
		}
		compressStringRedisTemplate.opsForHash().delete(RedisKeys.getRecommendRuleAcrossKey(), key);
	}

	/**
	 * 根据频道取交叉推荐规则
	 * @param serviceId
	 * @param siteId
	 * @param folderId
	 * @param platformId
	 * @return
	 */
	public RecommendRuleAcross getRecommendRuleAcrossByFolderId(int serviceId, 
			int siteId, Long folderId, Long platformId) {
		String key = RedisKeys.getRecommendRuleAcrossFieldKey(serviceId, siteId, folderId, platformId, RecommendRuleAcross.TYPE_FOLDER);
		key = (String)compressStringRedisTemplate.opsForHash().get(RedisKeys.getRecommendRuleAcrossKey(), key);
		RecommendRuleAcross across = this.objectMapperWrapper.convert2RecommendRuleAcross(key);
		if(null == across) {
			key = RedisKeys.getRecommendRuleAcrossFieldKey(serviceId, siteId, null, platformId, RecommendRuleAcross.TYPE_SYSTEM);
			key = (String)compressStringRedisTemplate.opsForHash().get(RedisKeys.getRecommendRuleAcrossKey(), key);
			across = this.objectMapperWrapper.convert2RecommendRuleAcross(key);
		}
		return across;
	}
	
	/**
	 * 根据产品取交叉推荐规则
	 * @param serviceId
	 * @param siteId
	 * @param productId
	 * @param platformId
	 * @return
	 */
	public RecommendRuleAcross getRecommendRuleAcrossByProductId(int serviceId, 
			int siteId, Long productId, Long platformId) {
		String key = RedisKeys.getRecommendRuleAcrossFieldKey(serviceId, siteId, productId, platformId, RecommendRuleAcross.TYPE_PRODUCT);
		key = (String)compressStringRedisTemplate.opsForHash().get(RedisKeys.getRecommendRuleAcrossKey(), key);
		return this.objectMapperWrapper.convert2RecommendRuleAcross(key);
	}
}
