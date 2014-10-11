package org.visitor.appportal.redis;

import org.visitor.app.portal.model.CacheElementPublishMessage;
import org.visitor.app.portal.model.SitePublishMessage;
import org.visitor.app.portal.model.UserPreference;
import org.visitor.appportal.domain.*;
import org.visitor.appportal.redis.support.AvroObjectWrapper;
import org.visitor.appportal.redis.support.StringByteRedisTemplate;
import org.visitor.util.AppConstants;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author mengw
 *
 */
@Repository
public class SiteRedisRepository {
	public static final String KEYWORD_OPERATIONAL_CACHE_NAME = SearchKeyword.class.getName() + ".Operational";

	protected static final Logger log = LoggerFactory.getLogger(SiteRedisRepository.class);
	@Autowired
	private AvroObjectWrapper avroObjectWrapper;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private ObjectMapperWrapper objectMapperWrapper;
	@Autowired
	private StringRedisTemplate compressStringRedisTemplate;	
	@Autowired
	private StringByteRedisTemplate userStringByteRedisTemplate;

	/**
	 * 设定站点
	 * @param site
	 */
	public void setSite(Site site) {
		String key = RedisKeys.getAllSiteKey();
		String value = objectMapperWrapper.convert2String(site);
		this.compressStringRedisTemplate.opsForHash().put(key, String.valueOf(site.getSiteId()), value);
		
		SitePublishMessage message = new SitePublishMessage(site.getSiteId());
		//发布消息
		stringRedisTemplate.convertAndSend(RedisKeys.getSitePublishedMessage(),
				objectMapperWrapper.convert2String(message));
	}

	
	/**
	 * 删除站点
	 * @param site
	 */
	public void deleteSite(Site site) {
		String key = RedisKeys.getAllSiteKey();
		compressStringRedisTemplate.opsForHash().delete(key, String.valueOf(site.getSiteId()));
		SitePublishMessage message = new SitePublishMessage(site.getSiteId());
		//发布消息
		stringRedisTemplate.convertAndSend(RedisKeys.getSitePublishedMessage(),
				objectMapperWrapper.convert2String(message));		
	}

	/**
	 * 取得全部站点
	 * @return
	 */
	public List<Site> getSiteList() {
		Map<Object, Object> entries = compressStringRedisTemplate.opsForHash().entries(RedisKeys.getAllSiteKey());
		if(null != entries) {
			List<Site> list = new ArrayList<Site>();
			for(Map.Entry<Object, Object> entry : entries.entrySet()) {
				list.add(objectMapperWrapper.convert2Site((String)entry.getValue()));
			}
			return list;
		}
		return null;
	}

	public void setFolder(Folder folder) {
		String key = RedisKeys.getFolderIdKey();
		String value = this.objectMapperWrapper.convert2String(folder.copy());
		compressStringRedisTemplate.opsForHash().put(key, String.valueOf(folder.getFolderId()), value);
		
		key = RedisKeys.getFolderPathKey();
		stringRedisTemplate.opsForHash().put(key, RedisKeys.getFolderPathHashKey(folder.getSiteId(), folder.getPath()), 
				String.valueOf(folder.getFolderId()));
	}
	
	public void deleteFolder(Folder folder) {
		compressStringRedisTemplate.opsForHash().delete(RedisKeys.getFolderIdKey(), String.valueOf(folder.getFolderId()));
		compressStringRedisTemplate.opsForHash().delete(RedisKeys.getFolderPathKey(), RedisKeys.getFolderPathHashKey(folder.getSiteId(), folder.getPath()));
	}
	
	public Folder getFolderById(Long folderId) {
		String key = RedisKeys.getFolderIdKey();
		key = (String)compressStringRedisTemplate.opsForHash().get(key, String.valueOf(folderId));
		return objectMapperWrapper.convert2Folder(key);
	}
	
	public Folder getFolderBySiteAndPath(Integer siteId, String path) {
		String key = RedisKeys.getFolderPathKey();
		//先找出频道ID，这个需要再核对一下频道是如何发布的
		key = (String)stringRedisTemplate.opsForHash().get(key, RedisKeys.getFolderPathHashKey(siteId, path));
		//再查找频道
		if(StringUtils.isNotBlank(key) && StringUtils.isNumeric(key)) {
			return getFolderById(Long.valueOf(key));
		}
		return null;
	}
	
	public void setFolderAncestors(Long folderId, List<Long> folderIds) {
		String key = RedisKeys.getFolderAncestorsKey();
		String value = objectMapperWrapper.convert2String(folderIds);
		stringRedisTemplate.opsForHash().put(key, String.valueOf(folderId), value);
	}
	
	public List<Folder> getFolderAncestors(Integer siteId, Long folderId) {
		String key = RedisKeys.getFolderAncestorsKey();
		key = (String)stringRedisTemplate.opsForHash().get(key, String.valueOf(folderId));
		if(StringUtils.isNotBlank(key)) {
			List<Long> ids = this.objectMapperWrapper.convert2ListLong(key);
			if(null != ids && !ids.isEmpty()) {
				return getMultipleFolder(ids);
			}
		}
		return null;
	}
	
	public void deleteFolderAncestors(Long folderId) {
		String key = RedisKeys.getFolderAncestorsKey();
		stringRedisTemplate.opsForHash().delete(key, String.valueOf(folderId));
	}

	protected List<Folder> getMultipleFolder(Collection<Long> ids) {
		Collection<Object> folders = new ArrayList<Object>();
		if(null != ids) {
			for(Long id : ids) {
				folders.add(String.valueOf(id));
			}
			folders = compressStringRedisTemplate.opsForHash().multiGet(RedisKeys.getFolderIdKey(), folders);
			if(null != folders) {
				List<Folder> result = new ArrayList<Folder>();
				for(Object value : folders) {
					result.add(objectMapperWrapper.convert2Folder((String)value));
				}
				return result;
			}
		}
		return null;
	}
	
	public void setFolderChildren(Integer siteId, Long folderId, Long childFolderId, Long score) {
		String key = RedisKeys.getFolderChildrenKey(siteId, folderId);
		stringRedisTemplate.opsForZSet().add(key, String.valueOf(childFolderId), score == null ? 0 : score);
	}
	
	public void deleteFolderChildren(Integer siteId, Long folderId, Long childFolderId) {
		String key = RedisKeys.getFolderChildrenKey(siteId, folderId);
		stringRedisTemplate.opsForZSet().remove(key, String.valueOf(childFolderId));
	}
	
	public List<Folder> getFolderChildren(Integer siteId, Long folderId) {
		String key = RedisKeys.getFolderChildrenKey(siteId, folderId);
		Set<String> values = this.stringRedisTemplate.opsForZSet().reverseRange(key, 0, -1);
		List<Long> list = new ArrayList<Long>();
		for(String id : values) {
			list.add(Long.valueOf(id));
		}
		return this.getMultipleFolder(list);
	}

    public String[] getDefaultSearchKeywords(int siteId) {
        return StringUtils.split(this.getDefaultSearchKeyword(siteId), AppConstants.COMMA_SEPARATOR);
    }

	public String getDefaultSearchKeyword(Integer siteId) {
		String value = (String)stringRedisTemplate.opsForHash().get(RedisKeys.getSystemPreferenceKey(), 
				RedisKeys.getDefaultSearchKeywordKey(siteId));
		if(StringUtils.isNotBlank(value)) {
			return value;
		}
		return "";
	}
//    public List<String> getDefaultSearchKeywordList(Integer siteId) {
//        String key = RedisKeys.getSystemPreferenceKey() + "_" +RedisKeys.getDefaultSearchKeywordKey(siteId);
//        List<String> value = stringRedisTemplate.opsForList().range(key, 0, 100);
//        if( value !=  null && value.size() > 0 ) {
//            return value;
//        }
//        return null;
//    }
	public void setDefaultSearchKeyword(Integer siteId, String keyword) {
		stringRedisTemplate.opsForHash().put(RedisKeys.getSystemPreferenceKey(), 
				RedisKeys.getDefaultSearchKeywordKey(siteId), keyword);
	}
//    public  void setDefaultSearchKeyword(Integer siteId, String... keywords) {
//        String key = RedisKeys.getSystemPreferenceKey() + "_" +RedisKeys.getDefaultSearchKeywordKey(siteId);
//        synchronized (this){
//            stringRedisTemplate.delete(key);
//            stringRedisTemplate.opsForList().leftPushAll( key, keywords);
//        }
//    }
	
	public List<SearchKeyword> getSearchKeywordsBySiteId(Integer siteId) {
		String key = RedisKeys.getSearchKeywordSortedKey(siteId);
		
		//这里的IDS，就是fieldKey
		Set<String> ids = stringRedisTemplate.opsForZSet().range(key, 0, -1);
		Collection<Object> objs = new ArrayList<Object>(ids);
		if(null != ids && ids.size() > 0) {
			objs = compressStringRedisTemplate.opsForHash().multiGet(RedisKeys.getSearchKeywordKey(), objs);
			if(null != objs) {
				List<SearchKeyword> list = new ArrayList<SearchKeyword>();
				for(Object value : objs) {
					SearchKeyword word = this.objectMapperWrapper.convert2SearchKeyword((String)value);
					//该关键词要在前台显示，必须满足两个条件，即热词，和可用
					if(null != word && word.getIsHot() == SearchKeyword.IsHotEnum.HOT.ordinal()
							&& word.getStatus() == SearchKeyword.StatusEnum.Enable.ordinal()) {
						list.add(word);
					}
				}
				return list;
			}
		}
		return null;
	}
	
	/**
	 * 发布关键字，同时需要将ID序列发布到ZSET里
	 * @param siteId
	 * @param searchKeywords
	 */
	public void setSearchKeywords(Integer siteId, List<SearchKeyword> searchKeywords) {
		
		String key = RedisKeys.getSearchKeywordKey();//搜索关键词的总KEY，与站点无关，所以这个KEY不能删除
		String sortKey = RedisKeys.getSearchKeywordSortedKey(siteId);
		stringRedisTemplate.delete(sortKey);//应该将这个KEY删除掉
		//stringRedisTemplate.delete(key);
		
		//由于前台取搜索关键词，会通用sortKey和fieldKey来区分，所以在重新发布的时候，key实际上是不应该删除的
		
		//但是必须把Redis中的相关信息清除掉先清除一次，
		Set<Object> keySets = compressStringRedisTemplate.opsForHash().keys(key);
		String keyPrex = "hk-keyword:" + siteId + ":";
		List<String> ids = new ArrayList<String>();

		for(Object objKey:keySets){
			//删除的条件是什么，只要其Key中包含当前站点，则可删除
			String strKey = String.valueOf(objKey);
			if(strKey.startsWith(keyPrex)){
				//这样可以保证只删除了当前站点的关键词信息
				compressStringRedisTemplate.opsForHash().delete(key, strKey);
				ids.add(strKey);
			}
		}
		
		if(null != searchKeywords && searchKeywords.size() > 0) {
			for(SearchKeyword searchKeyword: searchKeywords) {
				
				String fieldKey = RedisKeys.getSearchKeywordFieldKey(siteId, searchKeyword.getPlatformId(), searchKeyword.getName());
				
				compressStringRedisTemplate.opsForHash().put(key, fieldKey, objectMapperWrapper.convert2String(searchKeyword));
				//将ID序列写入ZSet里
				
				//我们只写那些可用的热词
				if(searchKeyword.getIsHot()==SearchKeyword.IsHotEnum.HOT.ordinal()
						&& searchKeyword.getStatus() == SearchKeyword.StatusEnum.Enable.ordinal()){
					//排序号
					stringRedisTemplate.opsForZSet().add(sortKey, fieldKey,searchKeyword.getSortOrder());
				}								
			}					
		}
		
		this.sendCacheElementMessage(KEYWORD_OPERATIONAL_CACHE_NAME, ids);	
		log.info("[redis] searchkeywords/" + ids + " publish finshed!");		
	}
	
	/**
	 * 删除历史纪录。
	 * @param siteId
	 * @param searchKeywords
	 */
	public void deleteSearchKeyword(Integer siteId, List<SearchKeyword> searchKeywords) {
		String key = RedisKeys.getSearchKeywordKey();
		String sortKey = RedisKeys.getSearchKeywordSortedKey(siteId);
		
		if(null != searchKeywords && searchKeywords.size() > 0) {
			for(SearchKeyword searchKeyword: searchKeywords) {
				compressStringRedisTemplate.opsForHash().put(key, 
					RedisKeys.getSearchKeywordFieldKey(siteId, searchKeyword.getPlatformId(), searchKeyword.getName()), 
					objectMapperWrapper.convert2String(searchKeyword));
				
				
				if(null == searchKeyword.getSiteId()) {//需要删除原来在站点中的关键词，现在不在了。
					stringRedisTemplate.opsForZSet().remove(sortKey, RedisKeys.getSearchKeywordFieldKey(siteId, searchKeyword.getPlatformId(), searchKeyword.getName()));
				} else {

				}
			}
		}
		
	}
	/**
	 * 发布单个关键字
	 * @param siteId
	 * @param searchKeyword
	 */
	public void setOneSearchKeyword(Integer siteId,SearchKeyword searchKeyword){
		String key = RedisKeys.getSearchKeywordKey();
		String hashKey = RedisKeys.getSearchKeywordFieldKey(siteId, searchKeyword.getPlatformId(), searchKeyword.getName());
		
		//删除原来的，如果有的话
		this.compressStringRedisTemplate.opsForHash().delete(key, hashKey);
		//添加新的
		compressStringRedisTemplate.opsForHash().put(key, hashKey, 
			objectMapperWrapper.convert2String(searchKeyword));

		/*将其放入到排序列表中*/
		String sortKey = RedisKeys.getSearchKeywordSortedKey(siteId);
		if(searchKeyword.getIsHot()==SearchKeyword.IsHotEnum.HOT.ordinal()
				&& searchKeyword.getStatus() == SearchKeyword.StatusEnum.Enable.ordinal()){
			stringRedisTemplate.opsForZSet().add(sortKey, hashKey, searchKeyword.getSortOrder());
		}
		
	}
	
	public void setFolderListByTagId(Long tagId, List<Long> folderIds) {
		String value = objectMapperWrapper.convert2String(folderIds);
		stringRedisTemplate.opsForHash().put(RedisKeys.getTagFolderKey(), String.valueOf(tagId), value);
	}

	public List<Folder> getFolderListByTagId(Long tagId) {
		String value = (String)stringRedisTemplate.opsForHash().get(RedisKeys.getTagFolderKey(), String.valueOf(tagId));
		if(StringUtils.isNotBlank(value)) {
			List<Long> ids = this.objectMapperWrapper.convert2ListLong(value);
			if(null != ids) {
				return getMultipleFolder(ids);
			}
		}
		return null;
	}

	public ModelList getTestModelList() {
		String value = (String)stringRedisTemplate.opsForHash().get(RedisKeys.getSystemPreferenceKey(), 
				RedisKeys.getTestModelListKey());
		if(StringUtils.isNotBlank(value)) {
			return this.objectMapperWrapper.convert2ModelList(value);
		}
		return null;
	}

	public Integer getScreenSizeLevel(Category platform, Long screenSize) {
		if(screenSize > 153600) {
			return 1;
		} else if(screenSize > 76800){
			return 2;
		} else {
			return 3;
		}
	}

	public Integer getProductDisplayPageSize() {
		String value = (String)stringRedisTemplate.opsForHash().get(RedisKeys.getSystemPreferenceKey(), 
				RedisKeys.getProductDisplayPageSizeKey());
		if(StringUtils.isNotBlank(value) && StringUtils.isNumeric(value)) {
			return Integer.valueOf(value);
		}
		return 30;
	}

	public Integer getSearchMaxShowPageNumber(Integer siteId) {
		String key = RedisKeys.getSearchMaxShowPageNumber(siteId);
		key = (String)stringRedisTemplate.opsForHash().get(RedisKeys.getSystemPreferenceKey(), key);
		return StringUtils.isNotBlank(key) && StringUtils.isNumeric(key) ? Integer.valueOf(key) : null;
	}

	public Integer getSearchMaxShowItemNumber(Integer siteId) {
		String key = RedisKeys.getSearchMaxShowItemNumberKey(siteId);
		key = (String)stringRedisTemplate.opsForHash().get(RedisKeys.getSystemPreferenceKey(), key);
		return StringUtils.isNotBlank(key) && StringUtils.isNumeric(key) ? Integer.valueOf(key) : null;
	}
	/**
	 * 是否显示产品中的评论信息。
	 * @param siteId
	 * @return
	 */
	public boolean isShowProductComments(Integer siteId) {
		String key = RedisKeys.getIfShowProductComments(siteId);
		key = (String)stringRedisTemplate.opsForHash().get(RedisKeys.getSystemPreferenceKey(), key);
		return StringUtils.equalsIgnoreCase(key, "true");
	}

	public boolean getShowProductListBySearch(Integer siteId) {
		String key = RedisKeys.getShowProductListBySearch(siteId);
		key = (String)stringRedisTemplate.opsForHash().get(RedisKeys.getSystemPreferenceKey(), key);
		return StringUtils.equalsIgnoreCase(key, "true");
	}

	public Integer getFolderRankMaxCount(Integer siteId) {
		String key = RedisKeys.getFolderRankMaxCount(siteId);
		key = (String)stringRedisTemplate.opsForHash().get(RedisKeys.getSystemPreferenceKey(), key);
		return StringUtils.isNotBlank(key) && StringUtils.isNumeric(key) ? Integer.valueOf(key) : null;
	}

	public Site getSiteBySiteId(Integer siteId) {
		String value = (String)compressStringRedisTemplate.opsForHash().get(RedisKeys.getAllSiteKey(), String.valueOf(siteId));
		if(StringUtils.isNotBlank(value)) {
			return this.objectMapperWrapper.convert2Site(value);
		}
		return null;
	}

	/**
	 * 产品详情页显示截图的数量。默认显示2。
	 * @param siteId
	 * @return
	 */
	public Integer getShowProductPicCount(Integer siteId) {
		String key = RedisKeys.getSystemPreferenceKey();
		String value = (String)stringRedisTemplate.opsForHash().get(key, 
				RedisKeys.getShowProductPicCountKey(siteId));
		if(StringUtils.isNotBlank(value) && StringUtils.isNumeric(value)) {
			return Integer.valueOf(value);
		}
		return 2;
	}

	public void setUserPrerence(String uid, UserPreference pref) {
		String key = RedisKeys.getUserPrerenceKey(uid);
		byte[] value = this.avroObjectWrapper.objectToByte(pref, UserPreference.class);
		//日期缩短为一个月，即30天
		if(StringUtils.length(uid) == 36 && StringUtils.contains(uid, "-")) {//自定义生成的uid只保留1天。
			userStringByteRedisTemplate.opsForValue().set(key, value, 1, TimeUnit.DAYS);
		} else {
			userStringByteRedisTemplate.opsForValue().set(key, value, 10, TimeUnit.DAYS);
		}
	}

	/**
	 * 获取用户参数,第一次的话，应该会返回NULL
	 * @param uid
	 * @return
	 */
	public UserPreference getUserPreference(String uid) {
		String prefkey = RedisKeys.getUserPrerenceKey(uid);
		byte[] value = userStringByteRedisTemplate.opsForValue().get(prefkey);		
		if(null != value && value.length > 0) {
			try {
				return this.avroObjectWrapper.convert2UserPreference(uid, value);
			}catch (Exception e) {
				log.error(uid, e);
				return null;
			}
		}
		return null;
	}

	public void setPicture(Picture picture) {
		if(null != picture) {
			compressStringRedisTemplate.opsForHash().put(RedisKeys.getPictureKey(), 
					String.valueOf(picture.getPictureId()), 
					objectMapperWrapper.convert2String(picture));
		}
	}

	public Picture getPicture(Long pictureid) {
		if(null != pictureid) {
			String key = (String)compressStringRedisTemplate.opsForHash().get(RedisKeys.getPictureKey(), 
					String.valueOf(pictureid));
			if(StringUtils.isNotBlank(key)) {
				return this.objectMapperWrapper.convert2Picture(key);
			}
		}
		return null;
	}

	public List<Folder> getMultiFolderByIds(Collection<Object> ids) {
		ids = compressStringRedisTemplate.opsForHash().multiGet(RedisKeys.getFolderIdKey(), ids);
		if(null != ids) {
			List<Folder> list = new ArrayList<Folder>();
			for(Object obj : ids) {
				list.add(this.objectMapperWrapper.convert2Folder((String)obj));
			}
			return list;
		}
		return null;
	}

	/**
	 * 
	 * @param keySet 实际为String类型的频道ID
	 * @return
	 */
	public List<Folder> getFolderByIds(Collection<Long> keySet) {
		if(null != keySet) {
			Collection<Object> keys = new ArrayList<Object>();
			for(Long i : keySet) {
				keys.add(String.valueOf(i));
			}
			keys = compressStringRedisTemplate.opsForHash().multiGet(RedisKeys.getFolderIdKey(), keys);
			List<Folder> list = new ArrayList<Folder>();
			for(Object obj : keys) {
				list.add(this.objectMapperWrapper.convert2Folder((String)obj));
			}
			return list;
		}
		return null;
	}

	public List<Picture> getPictureByPictureIds(Set<Long> keySet) {
		if(null != keySet && !keySet.isEmpty()) {
			Collection<Object> hashKeys = new ArrayList<Object>();
			for(Long id : keySet) {
				hashKeys.add(String.valueOf(id));
			}
			hashKeys = compressStringRedisTemplate.opsForHash().multiGet(RedisKeys.getPictureKey(), hashKeys);
			if(null != hashKeys) {
				List<Picture> list = new ArrayList<Picture>();
				for(Object s : hashKeys) {
					list.add(this.objectMapperWrapper.convert2Picture((String)s));
				}
				return list;
			}
		}
		return null;
	}

	/**
	 * 根据站点取相应的关键字集合
	 * @param siteId 站点
	 * @param platformId 历史遗留，没有用到
	 * @param keyword 关键词名
	 * @return
	 */
	public SearchKeyword getOperationalKeyword(Integer siteId, Long platformId, String keyword) {
		String hashKey = RedisKeys.getSearchKeywordKey();
		hashKey = (String) compressStringRedisTemplate.opsForHash().get(hashKey, RedisKeys.getSearchKeywordFieldKey(siteId, platformId, keyword));
		return objectMapperWrapper.convert2SearchKeyword(hashKey);
	}

	/**
	 * 需要在前台进行缓存的对象重新发布。
	 * @param cacheName
	 * @param folderIds
	 */
	public void sendCacheElementMessage(String cacheName, List<String> folderIds) {
		CacheElementPublishMessage message = new CacheElementPublishMessage(folderIds, cacheName);
		//发布消息
		stringRedisTemplate.convertAndSend(RedisKeys.getCacheElementPublishedMessage(),
				objectMapperWrapper.convert2String(message));		
	}
	
	public String getUserSiteData(String uid, Integer siteId) {
		String field = RedisKeys.getUserSiteDataFieldKey(uid, siteId);
		Object obj = userStringByteRedisTemplate.opsForHash().get(RedisKeys.getUserSiteDataKey(), field);
		if(null == obj) {
			return null;
		} else {
			return new String((byte[])obj);
		}
	}

	public void setUserSiteData(String uid, Integer siteId, String date) {
		String key = RedisKeys.getUserSiteDataKey();
		userStringByteRedisTemplate.opsForHash().put(key, RedisKeys.getUserSiteDataFieldKey(uid, siteId), date.getBytes());
	}
}
