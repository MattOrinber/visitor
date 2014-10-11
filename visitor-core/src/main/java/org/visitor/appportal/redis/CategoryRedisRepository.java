/**
 * 
 */
package org.visitor.appportal.redis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import org.visitor.app.portal.model.CategoryPublishMessage;
import org.visitor.appportal.domain.Category;
import org.visitor.util.AppStringUtils;

/**
 * @author mengw
 *
 */
@Repository
public class CategoryRedisRepository {
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private ObjectMapperWrapper objectMapperWrapper;
	@Autowired
	private StringRedisTemplate compressStringRedisTemplate;
	/**
	 * 
	 */
	public CategoryRedisRepository() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * update category info
	 * 
	 * @param category
	 * @param children 
	 * @param ancestorIds 
	 * @param defaultResolution 设置平台版本的默认分辨率
	 */
	public void categoryUpdate(Category category, List<Category> children, List<Category> ancestorIds, String defaultResolution) {
		
		//BRAND
		if (category.getNsThread().equals(Category.BRAND)) {
			
		//RESOLUTION
		} else if (category.getNsThread().equals(Category.RESOLUTION)) {
			Category temp = this.getCategoryById(category.getCategoryId());
			//设置分辨率为经度longitude (-180 +180)
			if(null != temp && null != temp.getLongitude()) {//保留原来的经度
				category.setLongitude(temp.getLongitude());
			} else {
				Long size = AppStringUtils.getScreenSize(category.getName());
				if(null != size) {
					category.setLongitude(size / this.getResolution2LongitudeRate());
				}
			}
			this.setCategoryResolutionByName(category);
			//生成模板发布消息。Thme meta key is changed to be tag name if needed.
			CategoryPublishMessage object = new CategoryPublishMessage(category, Category.RESOLUTION);
			//发布消息
			stringRedisTemplate.convertAndSend(RedisKeys.getCategoryPublishedMessage(),
					objectMapperWrapper.convert2String(object));
		//PLATFORM
		} else if (category.getNsThread().equals(Category.PLATFORM) && 
				category.getParentCategory() != null &&
				category.getParentCategory().getParentCategory() != null &&
				category.getParentCategory().getParentCategoryId().equals(Category.PLATFORM)) {
			this.setCategoryPlatformByName(category);
			//生成模板发布消息。Thme meta key is changed to be tag name if needed.
			CategoryPublishMessage object = new CategoryPublishMessage(category, Category.PLATFORM, children);
			//发布消息
			stringRedisTemplate.convertAndSend(RedisKeys.getCategoryPublishedMessage(),
					objectMapperWrapper.convert2String(object));
		//PLATFORM VERSION
		} else if (category.getParentCategory() != null && 
				category.getParentCategory().getParentCategory() != null && 
				category.getParentCategory().getParentCategory().getParentCategory() != null &&
				category.getParentCategory().getParentCategory().getParentCategoryId().equals(Category.PLATFORM)) {
			Category temp = this.getCategoryById(category.getCategoryId());
			//设置分辨率为纬度latitude （-90 +90）
			if(null != temp && null != temp.getLatitude()) {//保持原来的纬度
				category.setLatitude(temp.getLatitude());
			} else {
				Integer platform = null;
				Integer os = null;
				Integer version = category.getSortOrder();
				if(null != category.getParentCategory()) {
					platform = category.getParentCategory().getSortOrder();
					if(null != category.getParentCategory().getParentCategory()) {
						os = category.getParentCategory().getParentCategory().getSortOrder();
					}
				}
				if(version != null && platform != null) {
					Float latitude = new Float(version / 10.0 + (platform + os));
					category.setLatitude(latitude);
				}
			}
			setCategoryPlatformVersionByName(category);
			
			//生成模板发布消息。Thme meta key is changed to be tag name if needed.
			CategoryPublishMessage object = new CategoryPublishMessage(category, Category.PLATFORMVERSION);
			//发布消息
			stringRedisTemplate.convertAndSend(RedisKeys.getCategoryPublishedMessage(),
					objectMapperWrapper.convert2String(object));
			
			if(StringUtils.isNotBlank(defaultResolution) && StringUtils.isNumeric(defaultResolution)) {
				//平台版本的默认分辨率。
				setPlatformVersionDefaultResolution(category.getParentCategoryId(), category.getCategoryId(), defaultResolution);
			}
		//TAG
		} else if (category.getNsThread().equals(Category.TAG)) {
			this.setCategoryTagByName(category);
		} else if(category.getNsThread().equals(Category.PRODUCTSOURCE)) {
			//生成模板发布消息。Thme meta key is changed to be tag name if needed.
			CategoryPublishMessage object = new CategoryPublishMessage(category, Category.PRODUCTSOURCE, children);
			//发布消息
			stringRedisTemplate.convertAndSend(RedisKeys.getCategoryPublishedMessage(),
					objectMapperWrapper.convert2String(object));
		}
		//保存子分类
		List<Long> childrenIds = new ArrayList<Long>();
		if (null != children && !children.isEmpty()) {
			for (Category c : children) {
				childrenIds.add(c.getCategoryId());
			}
		}
		setCategoryChildrenById(category.getCategoryId(), childrenIds);
		setCategoryById(category.getCategoryId(), category);
	}
	
	/**
	 * delete category info
	 * 
	 * @param category
	 */
	public void categoryDelete(Category category) {
		compressStringRedisTemplate.opsForHash().delete(RedisKeys.getCategoryIdKey(), String.valueOf(category.getCategoryId()));
		compressStringRedisTemplate.opsForHash().delete(RedisKeys.getCategoryChildrenKey(), String.valueOf(category.getCategoryId()));
		this.categoryDelete(category, category.getName(), category.getEnName());
	}
	public void categoryDelete(Category category, String name, String enname) {
		
		//BRAND
		if (category.getNsThread().equals(Category.BRAND)) {
			
		//RESOLUTION
		} else if (category.getNsThread().equals(Category.RESOLUTION)) {
			
		//PLATFORM
		} else if (category.getNsThread().equals(Category.PLATFORM) && 
				category.getParentCategory() != null &&
				category.getParentCategory().getParentCategory() != null &&
				category.getParentCategory().getParentCategoryId().equals(Category.PLATFORM)) {
		
		//PLATFORM VERSION
		} else if (category.getParentCategory() != null && 
				category.getParentCategory().getParentCategory() != null && 
				category.getParentCategory().getParentCategory().getParentCategory() != null &&
				category.getParentCategory().getParentCategory().getParentCategoryId().equals(Category.PLATFORM)) {
			
		
		//TAG
		} else if (category.getNsThread().equals(Category.TAG)) {
			
		}
	}

	/**
	 * id getter method
	 * 
	 * @param categoryId
	 * @return
	 */
	public Category getCategoryById(Long categoryId) {
		String key = RedisKeys.getCategoryIdKey();
		key = (String)compressStringRedisTemplate.opsForHash().get(key, String.valueOf(categoryId));
		if(StringUtils.isNotBlank(key)) {
			return this.objectMapperWrapper.convert2Category(key);
		}
		return null;
	}
	
	/**
	 * id setter method
	 * 
	 * @param categoryId
	 * @param category
	 */
	public void setCategoryById(Long categoryId, Category category) {
		String key = RedisKeys.getCategoryIdKey();
		String value = this.objectMapperWrapper.convert2String(category);
		this.compressStringRedisTemplate.opsForHash().put(key, String.valueOf(categoryId), value);
	}
	
	/**
	 * get category all child'category
	 */
	public List<Long> getCategoryChildrenById(Long categoryId) {
		String key = RedisKeys.getCategoryChildrenKey();
		key = (String)compressStringRedisTemplate.opsForHash().get(key, String.valueOf(categoryId));
		if(StringUtils.isNotBlank(key)) {
			return this.objectMapperWrapper.convert2ListLong(key);
		}
		return null;
	}
	
	/**
	 * set category all child'category
	 */
	public void setCategoryChildrenById(Long categoryId, List<Long> categoryIds) {
		String key = RedisKeys.getCategoryChildrenKey();
		String value = objectMapperWrapper.convert2String(categoryIds);
		this.compressStringRedisTemplate.opsForHash().put(key, String.valueOf(categoryId), value);
	}
	
	/**
	 * 当前分类的排序。比如产品来源，平台版本等等
	 * @param categoryId
	 * @return
	 */
	public Integer getCategorySortOrder(Long categoryId) {
		Category category = getCategoryById(categoryId);
		if(null != category) {
			return category.getSortOrder();
		}
		return null;
	}

	/**
	 * 系统设置中需要通过外部链接进行下载的CP id列表。
	 * @return
	 */
	public List<Long> getConfigure4ExternalUrlCpIds() {
		String value = (String)stringRedisTemplate.opsForHash().get(RedisKeys.getSystemPreferenceKey(),
				RedisKeys.getExternalUrlCpIdsKey());
		if(StringUtils.isNotBlank(value)) {
			return this.objectMapperWrapper.convert2ListLong(value);
		}
		return null;
	}
	
	/**
	 * 设定分辨率
	 * @param categoryId
	 * @param category
	 */
	public void setCategoryResolutionByName(Category category) {
		String key = RedisKeys.getCategoryResolutionKey();
		this.compressStringRedisTemplate.opsForHash().put(key, category.getName(), String.valueOf(category.getCategoryId()));
	}
	
	/**
	 * 取得分辨率
	 * @param categoryId
	 * @return
	 */
	public Category getCategoryResolutionByName(String name) {
		String key = RedisKeys.getCategoryResolutionKey();
		key = (String)compressStringRedisTemplate.opsForHash().get(key, name);
		if(StringUtils.isNotBlank(key) && StringUtils.isNumeric(key)) {
			return this.getCategoryById(Long.valueOf(key));
		}
		return null;
	}
	
	/**
	 * 设定手机平台
	 * @param categoryId
	 * @param category
	 */
	public void setCategoryPlatformByName(Category category) {
		String key = RedisKeys.getCategoryPlatformKey();
		String enname = category.getEnName();
		enname = StringUtils.replace(enname, ";", ",");
		if (StringUtils.isNotEmpty(enname)) {
			String[] en = enname.split(",");
			for (int i=0 ; i < en.length ; i++) {
				this.compressStringRedisTemplate.opsForHash().put(key, en[i], String.valueOf(category.getCategoryId()));
			}
		}
		this.compressStringRedisTemplate.opsForHash().put(key, category.getName(), String.valueOf(category.getCategoryId()));
	}
	
	/**
	 * 取得手机平台
	 * @param platformName
	 * @return
	 */
	public Category getCategoryPlatformByName(String platformName) {
		if(StringUtils.isNotBlank(platformName)) {
			String key = RedisKeys.getCategoryPlatformKey();
			key = (String)compressStringRedisTemplate.opsForHash().get(key, platformName);
			if(StringUtils.isNotBlank(key) && StringUtils.isNumeric(key)) {
				return this.getCategoryById(Long.valueOf(key));
			}
		}
		return null;
	}
	
	/**
	 * 设定平台版本
	 * @param categoryId
	 * @param category
	 */
	public void setCategoryPlatformVersionByName(Category category) {
		String key = RedisKeys.getCategoryPlatformVersionKey();
		String value = String.valueOf(category.getCategoryId());
		String enname = category.getEnName();
		enname = StringUtils.replace(enname, ";", ",");
		if (StringUtils.isNotEmpty(enname)) {
			String[] en = enname.split(",");
			for (int i=0 ; i < en.length ; i++) {
				this.stringRedisTemplate.opsForHash().put(key, RedisKeys.getCategoryPlatformVersionNameKey(category.getParentCategory().getName(), en[i]), value);
			}
		}
		this.stringRedisTemplate.opsForHash().put(key, RedisKeys.getCategoryPlatformVersionNameKey(category.getParentCategory().getName(), category.getName()), value);
	}
	
	/**
	 * 取得平台版本
	 * @param platformId
	 * @return
	 */
	public Category getCategoryPlatformVersionByName(Category platform, String versionName) {
		if(null == platform) {
			return null;
		}
		String key = RedisKeys.getCategoryPlatformVersionKey();
		key = (String)stringRedisTemplate.opsForHash().get(key, 
				RedisKeys.getCategoryPlatformVersionNameKey(platform.getName(), versionName));
		if(StringUtils.isNotBlank(key) && StringUtils.isNumeric(key)) {
			return this.getCategoryById(Long.valueOf(key));
		}
		return null;
	}
	
	/**
	 * 设定频道标签
	 * @param categoryId
	 * @param category
	 */
	public void setCategoryTagByName(Category category) {
		String key = RedisKeys.getCategoryTagKey();
		this.compressStringRedisTemplate.opsForHash().put(key, category.getName(), String.valueOf(category.getCategoryId()));
	}

	/**
	 * 系统设置中设置的客户端的测试平台。
	 * @return
	 */
	public Category getTestPlatform() {
		String value = (String)stringRedisTemplate.opsForHash().get(RedisKeys.getSystemPreferenceKey(), 
				RedisKeys.getTestPlatformKey());
		if(StringUtils.isNotBlank(value) && StringUtils.isNumeric(value)) {
			return this.getCategoryById(Long.valueOf(value));
		}
		return null;
	}

	public Category getTestPlatformVersion() {
		String value = (String)stringRedisTemplate.opsForHash().get(RedisKeys.getSystemPreferenceKey(), 
				RedisKeys.getTestPlatformVersionKey());
		if(StringUtils.isNotBlank(value) && StringUtils.isNumeric(value)) {
			return this.getCategoryById(Long.valueOf(value));
		}
		return null;
	}

	public Category getTestResolution() {
		String value = (String)stringRedisTemplate.opsForHash().get(RedisKeys.getSystemPreferenceKey(), 
				RedisKeys.getTestResolutionKey());
		if(StringUtils.isNotBlank(value) && StringUtils.isNumeric(value)) {
			return this.getCategoryById(Long.valueOf(value));
		}
		return null;
	}

	/**
	 * 取所有的分辨率。
	 * @return
	 */
	public List<Category> getResolutionList() {
		String key = RedisKeys.getCategoryResolutionKey();
		Map<Object, Object> entries = compressStringRedisTemplate.opsForHash().entries(key);
		if(null != entries) {
			List<Category> list = new ArrayList<Category>();
			Collection<Object> values = compressStringRedisTemplate.opsForHash().multiGet(RedisKeys.getCategoryIdKey(), entries.values());
			if(null != values) {
				for(Object value : values) {
					list.add(this.objectMapperWrapper.convert2Category((String)value));
				}
			}
			return list;
		}
		return null;
	}

	/**
	 * 前台：系统设置中平台版本的默认分辨率
	 * @param platformId
	 * @param platformVersionId
	 * @return
	 */
	public Category getPlatformVersionDefaultResolution(Long platformId,
			Long platformVersionId) {
		String key = RedisKeys.getPlatformVersionDefaultResolutionKey(platformId, platformVersionId);
		key = (String)stringRedisTemplate.opsForHash().get(RedisKeys.getSystemPreferenceKey(), key);
		if(StringUtils.isNotBlank(key) && StringUtils.isNumeric(key)) {
			return this.getCategoryById(Long.valueOf(key));
		}
		return null;
	}

	/**
	 * 
	 * @param categoryId
	 * @param defaultResolution
	 */
	public void setPlatformVersionDefaultResolution(Long platformId, Long platformVersionId,
			String defaultResolution) {
		String key = RedisKeys.getPlatformVersionDefaultResolutionKey(platformId, platformVersionId);
		stringRedisTemplate.opsForHash().put(RedisKeys.getSystemPreferenceKey(), key, defaultResolution);
	}
	
	/**
	 * 平台下的所有版本
	 * @param platformId
	 * @return
	 */
	public List<Category> getPlatformVersionList(Long platformId) {
		List<Long> ids = this.getCategoryChildrenById(platformId);
		if(null != ids && ids.size() > 0) {
			Collection<Object> hashKeys = new ArrayList<Object>();
			for(Long id : ids) {
				hashKeys.add(String.valueOf(id));
			}
			String key = RedisKeys.getCategoryIdKey();
			hashKeys = compressStringRedisTemplate.opsForHash().multiGet(key, hashKeys);
			if(null != hashKeys) {
				List<Category> list = new ArrayList<Category>();
				for(Object obj : hashKeys) {
					Category cat = this.objectMapperWrapper.convert2Category((String)obj);
					if(null != cat) {
						list.add(cat);
					}
				}
				return list;
			}
		}
		return null;
	}

	/**
	 * 分辨率到经度的转换率
	 * @return
	 */
	public float getResolution2LongitudeRate() {
		return 1280 * 1024 / 180;
	}
	
	/**
	 * 取得分类默认显示名称
	 * @param categoryId
	 * @return
	 */
	public String getPlatformDefaultDisName(String categoryId){
		String key = RedisKeys.getCategoryDefaultDisName(categoryId);
		return (String)stringRedisTemplate.opsForHash().get(RedisKeys.getSystemPreferenceKey(), key);
	}
}
