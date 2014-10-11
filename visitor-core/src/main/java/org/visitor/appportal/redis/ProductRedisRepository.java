/**
 * 
 */
package org.visitor.appportal.redis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import org.visitor.app.portal.model.FolderRank.RankTypeEnum;
import org.visitor.app.portal.model.PagerDisplay;
import org.visitor.app.portal.model.Product;
import org.visitor.app.portal.model.ProductIntelligentRecommend;
import org.visitor.app.portal.model.UserPreference;
import org.visitor.appportal.domain.Folder;
import org.visitor.appportal.domain.HtmlPage;
import org.visitor.appportal.domain.MessageContent;
import org.visitor.appportal.domain.ProductContainer;
import org.visitor.appportal.domain.ProductFile;
import org.visitor.appportal.domain.ProductPic;
import org.visitor.appportal.domain.ProductSiteFolder;
import org.visitor.appportal.domain.RecommandContainer;
import org.visitor.util.AppStringUtils;
/**
 * @author mengw
 *
 */
@Repository
public class ProductRedisRepository {
	protected static final Logger log = LoggerFactory.getLogger(ProductRedisRepository.class);
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private ObjectMapperWrapper objectMapperWrapper;
	@Autowired
	private StringRedisTemplate compressStringRedisTemplate;	
	/**
	 * 
	 */
	public ProductRedisRepository() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 频道排行添加产品
	 */
	public void setProductsByFolderRank(Integer siteId, Long folderId, Long productId,
			RankTypeEnum rankType, Long platformVersionId, Integer level, Long score){
		String key = RedisKeys.getFolderRankKey(siteId, folderId, rankType, platformVersionId , level);
		
		stringRedisTemplate.opsForZSet().add(key, String.valueOf(productId), score == null ? 0 : score);
	}
	
	/**
	 * 删除排行产品
	 */
	public void deleteProductsByFolderRank(Integer siteId, Long folderId, Long productId){
		String key = RedisKeys.getFolderRankKey4Delete(siteId, folderId);
		Set<String> keys = stringRedisTemplate.keys(key);
		if(null != keys && keys.size() > 0) {
			for(String d : keys) {
				stringRedisTemplate.opsForZSet().remove(d, String.valueOf(productId));
			}
		}
	}

	/**
	 * 根据频道得到频道排行
	 * @param siteId 频道所在站点ID
	 * @param folderId 频道ID
	 * @param level 用户的机型所属等级
	 * @param rankType 排行方式。
	 * @param maxCount 
	 * @return
	 */
	public List<Product> getProductsByFolderRank(Integer siteId, Long folderId, RankTypeEnum rankType, 
			Long platformVersionId, Integer level, Integer maxCount) {
		String key = RedisKeys.getFolderRankKey(siteId, folderId, rankType, platformVersionId, level);
		Set<String> productIds = stringRedisTemplate.opsForZSet().reverseRange(key, 0, maxCount - 1);
		if(log.isDebugEnabled()) {
			log.debug("getProductsByFolderRank() key:" + key + " \t productIds:" + productIds);
		}
		if(null != productIds && productIds.size() > 0) {
			return getMultipleProductWithProductFiles(productIds);
		}
		return null;
	}

	/**
	 * 根据多个产品ID,取产品信息以及各个产品的资源文件信息。
	 * @param productIds
	 * @return
	 */
	public List<Product> getMultipleProductWithProductFiles(Collection<String> productIds) {
		Collection<Object> productKeys = new ArrayList<Object>();//产品信息
		for(String pid : productIds) {
			productKeys.add(pid);
		}
		Collection<Object> products = compressStringRedisTemplate.opsForHash().multiGet(RedisKeys.getProductKey(), productKeys);
		Collection<Object> productFileKeys = compressStringRedisTemplate.opsForHash().multiGet(RedisKeys.getProductFileKey(), productKeys);
		return convertProductWithFiles(products, productFileKeys);
	}

	/**
	 * 根据多个产品ID,取产品信息以及各个产品的资源文件信息。
	 * 并设置产品版本
	 * @param productIds
	 * @return
	 */
	public List<Product> getMultipleProductWithProductFilesAndVersion(Collection<String> productIdsAndVersions) {
		Collection<Object> productKeys = new ArrayList<Object>();//产品信息
		
		Map<Long,String> productVersions = new HashMap<Long,String>();
		for(String pidAndVersion : productIdsAndVersions) {
			String [] datas = pidAndVersion.split(RedisKeys.getApuRedisSplit());
			if(null != datas && datas.length >= 3) {
				Object pid = datas[0];//第一项为产品ID
				String version = datas[2];//第三项为产品原版本
			
				productKeys.add(pid);
				productVersions.put(Long.valueOf(pid.toString()), version);
			}
		}
		Collection<Object> products = compressStringRedisTemplate.opsForHash().multiGet(RedisKeys.getProductKey(), productKeys);
		Collection<Object> productFileKeys = compressStringRedisTemplate.opsForHash().multiGet(RedisKeys.getProductFileKey(), productKeys);
		
		List<Product> productsList = convertProductWithFiles(products, productFileKeys);
		
		/**填充旧版本信息*/
		for (Product product : productsList) {
			product.setOldVersion(productVersions.get(product.getProductList().getProductId()));
			
		}
		
		
		return productsList;
	}
	
	
	/**
	 * 显示产品推荐中的产品
	 * @param siteId
	 * @param folderId
	 * @param pageId
	 * @param containerId
	 * @return
	 */
	public List<Product> getProductsByRecommand(Integer siteId, Long folderId, Long pageId, Long containerId) {
		String key = RedisKeys.getProductRecommandKey(siteId, folderId, pageId, containerId);
		Set<String> productIds= stringRedisTemplate.opsForZSet().reverseRange(key, 0, -1);
		if(log.isDebugEnabled()) {
			log.debug("getProductsByRecommand() key:" + key + " \tproductIds:" + productIds);
		}
		return this.getMultipleProductWithProductFiles(productIds);
	}
	
	/**
	 * 设定产品推荐产品。key为站点、频道、页面和容器的组合,值为产品Id,按照sortOrder进行排序
	 * @param pc
	 * @param sortOrder
	 */
	public void setProductRecommand(ProductContainer pc, Long sortOrder) {

		String key = RedisKeys.getProductRecommandKey(pc.getSiteId(), pc.getFolderId(), pc.getPageId(), pc.getContainerId());
		String value = this.objectMapperWrapper.convert2String(pc);
		String id = String.valueOf(pc.getProductContainerId());
		String pcKey = RedisKeys.getProductRecommandKey();

		//删除无效的产品推荐。
		if(null != pc.getStatus() && pc.getStatus().intValue() == ProductContainer.DISABLE) {
			stringRedisTemplate.opsForZSet().remove(key, id);
			this.compressStringRedisTemplate.opsForHash().delete(pcKey, id);
		} else {
			//删除无效的产品推荐内容
			boolean delete = false;
			if(null != pc.getProduct() && !pc.getProduct().isEnable()) {
				delete = true;
			}
			if(null != pc.getAdvertise() && !pc.getAdvertise().isEnable()) {
				delete = true;
			}
			if(null != pc.getFolder() && !pc.getFolder().isEnable()) {
				delete = true;
			}
			if(delete) {
				stringRedisTemplate.opsForZSet().remove(key, id);
				this.compressStringRedisTemplate.opsForHash().delete(pcKey, id);
			} else {
				stringRedisTemplate.opsForZSet().add(key, id, sortOrder == null ? 0 : sortOrder);
				this.compressStringRedisTemplate.opsForHash().put(pcKey, id, value);
			}
		}
	}
	
	/**
	 * 删除产品推荐
	 * @param pc
	 */
	public void deleteProductRecommand(ProductContainer pc) {
		//站点,频道,页面,容器,四个参数
		String key = RedisKeys.getProductRecommandKey(pc.getSiteId(), pc.getFolderId(), pc.getPageId(), pc.getContainerId());
		
		//这儿应该是删除容器关联关系ID
		stringRedisTemplate.opsForZSet().remove(key, String.valueOf(pc.getProductContainerId()));
	}
	
	/**
	 * 设定产品基本信息
	 * @param productId
	 * @param product
	 */
	public void setProduct(Long productId, Product product) {
		String value = objectMapperWrapper.convert2String(product);
		if(StringUtils.isNotBlank(value)) {
			String key = RedisKeys.getProductKey();
			compressStringRedisTemplate.opsForHash().put(key, String.valueOf(productId), value);
		}
	}
	
	/**
	 * 删除产品基本信息
	 * @param productId
	 */
	public void deleteProduct (Long productId) {
		compressStringRedisTemplate.opsForHash().delete(RedisKeys.getProductKey(), String.valueOf(productId));
	}
	
	/**
	 * 产品截图
	 * @param productId
	 * @return
	 */
	public List<ProductPic> getProductPics(String productId) {
		String value = (String)compressStringRedisTemplate.opsForHash()
			.get(RedisKeys.getProductPicListKey(), String.valueOf(productId));
		return this.objectMapperWrapper.convert2ProductPic(value);
	}
	
	/**
	 * 设保存产品截图
	 * @param productId
	 * @param pics
	 */
	public void setProductPics(Long productId, List<ProductPic> pics) {
		String value = objectMapperWrapper.convert2String(pics);
		if(StringUtils.isNotBlank(value)) {
			String key = RedisKeys.getProductPicListKey();
			compressStringRedisTemplate.opsForHash().put(key, String.valueOf(productId), value);
		}
	}
	
	/**
	 * 删除产品截图
	 * @param productId
	 */
	public void deleteProductPics(Long productId){
		compressStringRedisTemplate.opsForHash().delete(RedisKeys.getProductPicListKey(), String.valueOf(productId));
	}
	
	/**
	 * 设定产品文件
	 * @param productId
	 * @param productFiles
	 */
	public void setProductFiles(Long productId, List<ProductFile> productFiles) {
		Iterator<ProductFile> it = productFiles.iterator();
		while(it.hasNext()) {
			ProductFile file = it.next();
			if(!(null != file && file.isValid())) {
				it.remove();
			}
		}
		String value = objectMapperWrapper.convert2String(productFiles);
		if(StringUtils.isNotBlank(value)) {
			String key = RedisKeys.getProductFileKey();
			compressStringRedisTemplate.opsForHash().put(key,String.valueOf(productId),value);
		}
	}
	
	/**
	 * 删除产品文件
	 * @param productId
	 */
	public void deleteProductFiles(Long productId){
		compressStringRedisTemplate.opsForHash().delete(RedisKeys.getProductFileKey(),String.valueOf(productId));
	}
	
	/**
	 * 设定频道绑产品
	 * @param siteId
	 * @param folderId
	 * @param level
	 * @param productId
	 * @param platformVersionId
	 * @param score
	 */
	public void setProductSiteFolder(ProductSiteFolder productSiteFolder, Integer level, Long platformVersionId) {
		String key = RedisKeys.getPageableProductKey(productSiteFolder.getSiteId(),
				productSiteFolder.getFolderId(), String.valueOf(platformVersionId), 
				String.valueOf(level));
		stringRedisTemplate.opsForZSet().add(key, String.valueOf(productSiteFolder.getProductId()), 
				productSiteFolder.getSortOrder() == null ? 0 : productSiteFolder.getSortOrder());
	}

	/**
	 * 更新产品与频道的绑定中的排序
	 * @param productSiteFolder
	 */
	public void updateProductSiteFolder(ProductSiteFolder productSiteFolder) {
		String key = RedisKeys.getPageableProductKey(productSiteFolder.getSiteId(), 
				productSiteFolder.getFolderId(),"*", "?");
		Set<String> keys = stringRedisTemplate.keys(key);
		if(null != keys) {
			for(String d : keys) {
				stringRedisTemplate.opsForZSet().add(d, String.valueOf(productSiteFolder.getProductId()), productSiteFolder.getSortOrder());
			}
		}
	}
	
	/**
	 * 删除站点频道全部列表
	 * @param siteId
	 * @param folderId
	 */
	public void deleteSiteFolderKeys(Integer siteId, Long folderId){
		String key1 = RedisKeys.getPageableProductKey(siteId, folderId,"*", "?");
		Set<String> keys1 = stringRedisTemplate.keys(key1);
		if (keys1 != null && keys1.size() > 0) {
			stringRedisTemplate.delete(keys1);
		}
		
		String key2 = RedisKeys.getFolderRankKey4Delete(siteId, folderId);
		Set<String> keys2 = stringRedisTemplate.keys(key2);
		if (keys2 != null && keys2.size() > 0) {
			stringRedisTemplate.delete(keys2);
		}
	}
	
	/**
	 * 频道下架产品
	 * @param productSiteFolder
	 */
	public void deleteProductSiteFolder(ProductSiteFolder productSiteFolder) {
		String key = RedisKeys.getPageableProductKey(productSiteFolder.getSiteId(), productSiteFolder.getFolderId(),"*", "?");
		Set<String> keys = stringRedisTemplate.keys(key);
		if(null != keys && keys.size() > 0) {
			for(String d : keys) {
				stringRedisTemplate.opsForZSet().remove(d, String.valueOf(productSiteFolder.getProductId()));
			}
		}
	}
	
	public void setProductState(String productId, RankTypeEnum rank, Long value) {
		String key = RedisKeys.getProductStateKey(rank);
		stringRedisTemplate.opsForHash().put(key, productId, String.valueOf(value));
	}
	
	public Long increaseProductState(Long productId, RankTypeEnum rank) {
		String key = RedisKeys.getProductStateKey(rank);
		return stringRedisTemplate.opsForHash().increment(key, String.valueOf(productId), 1);
	}
	
	public Long getProductState(String productId, RankTypeEnum rank) {
		String key = RedisKeys.getProductStateKey(rank);
		key = (String)stringRedisTemplate.opsForHash().get(key, productId);
		if(StringUtils.isNotBlank(key) && StringUtils.isNumeric(key)) {
			return Long.valueOf(key);
		}
		return 0l;
	}
	
	public PagerDisplay<Product> getProductsByPager(Integer siteId, Long folderId, Long platformVersionId, Integer level,
			Integer pageNumber, Integer pageSize) {
		String key = RedisKeys.getPageableProductKey(siteId, folderId, String.valueOf(platformVersionId), String.valueOf(level));
		long totalSize = stringRedisTemplate.opsForZSet().size(key);
		
		if(log.isDebugEnabled()) {
			log.debug("getProductsByPager() key:" + key + " \t totalSize:" + totalSize);
		}
		if(totalSize > 0) {
			Set<String> pids = stringRedisTemplate.opsForZSet().reverseRange(key, (pageNumber - 1) * pageSize, pageNumber * pageSize - 1);
			if(log.isDebugEnabled()) {
				log.debug("getProductsByPager() key:" + key + " \t productIds:" + pids);
			}
			List<Product> list = getMultipleProductWithProductFiles(pids);
			if(null != list && list.size() > 0) {
				return new PagerDisplay<Product>(pageSize, pageNumber, totalSize, list);
			}
		}
		return new PagerDisplay<Product>(pageSize, pageNumber, totalSize, null);
	}

	protected List<Product> convertProductWithFiles(Collection<Object> productKeys,
			Collection<Object> productFileKeys) {
		Map<Long, List<ProductFile>> files = new HashMap<Long, List<ProductFile>>();
		for(Object value : productFileKeys) {
			List<ProductFile> productFiles = objectMapperWrapper.convert2ProductFiles((String)value);
			if(null != productFiles && productFiles.size() > 0) {
				files.put(productFiles.get(0).getProductId(), productFiles);
			}
		}
		List<Product> list = convert2Product(productKeys);
		for(Product p : list) {
			p.setProductFiles(files.get(p.getProductList().getProductId()));
		}
		if(log.isDebugEnabled()) {
			log.debug("convertProductWithFiles() product count:" + list.size());
		}
		return list;
	}

	protected List<Product> convert2Product(Collection<Object> jsonValues) {
		List<Product> list = new ArrayList<Product>();
		for(Object value : jsonValues) {
			Product product = this.objectMapperWrapper.converter2Product((String)value);
			if(null != product) {
				list.add(product);
			}
		}
		return list;
	}
	
	/**
	 * 根据产品ID取产品信息。
	 * @param productId
	 * @param includeProductFiles true-包含产品文件；false-不包含产品文件
	 * @return
	 */
	public Product getProductByProductId(Long productId, boolean includeProductFiles) {
		String key = RedisKeys.getProductKey();
		String value = (String)compressStringRedisTemplate.opsForHash().get(key, String.valueOf(productId));
		Product p = this.objectMapperWrapper.converter2Product(value);
		if(null != p) {
			if(includeProductFiles) {
				value = (String)compressStringRedisTemplate.opsForHash().get(RedisKeys.getProductFileKey(), String.valueOf(productId));
				p.setProductFiles(objectMapperWrapper.convert2ProductFiles(value));
			}
			return p;
		}
		return null;
	}
	
	/**
	 * 取产品评论
	 * @param productId
	 * @return
	 */
	public List<MessageContent> getProductComments(String productId) {
		String key = RedisKeys.getProductCommentKey();
		key = (String)compressStringRedisTemplate.opsForHash().get(key, String.valueOf(productId));
		return this.objectMapperWrapper.convert2ListMessageContent(key);
	}
	
	/**
	 * 保存产品评论
	 * @param productId
	 * @param comments
	 */
	public void setProductComments(Long productId, List<MessageContent> comments) {
		String value = objectMapperWrapper.convert2String(comments);
		String key = RedisKeys.getProductCommentKey();
		this.compressStringRedisTemplate.opsForHash().put(key, String.valueOf(productId), value);
	}

	public void setRecommandContainer(RecommandContainer container) {
		if(null != container) {
			container = container.copy();
		}
		String value = objectMapperWrapper.convert2String(container);
		String key = RedisKeys.getRecommandContainerKey();
		compressStringRedisTemplate.opsForHash().put(key, String.valueOf(container.getContainerId()), value);
	}

	/**
	 * 频道排行产品制定的排序阀值。
	 * @return
	 */
	public Integer getFolderProductMaxSortOrder() {
		String key = RedisKeys.getSystemPreferenceKey();
		key = (String)stringRedisTemplate.opsForHash().get(key, RedisKeys.getFolderProductMaxSortOrderKey());
		if(StringUtils.isNotBlank(key) && StringUtils.isNumeric(key)) {
			return Integer.valueOf(key);
		}
		return 20000;
	}

	/**
	 * 最新发布产品,按照站点。
	 * @param productList
	 * @param publishDate
	 * @param productSites 
	 */
	public void publishProductFirst(Long productId, Date publishDate, List<ProductSiteFolder> productSites) {
		for(ProductSiteFolder site : productSites) {
			String key = RedisKeys.getProductPublishFirstKey(site.getSiteId());
			this.stringRedisTemplate.opsForZSet().add(key, String.valueOf(productId), publishDate.getTime());
		}
	}
	
	/**
	 * 站点排行添加产品
	 */
	public void setProductsBySiteRank(Integer siteId, Long productId, RankTypeEnum rankType, Long platformVersionId, Integer level, Long score){
		String key = RedisKeys.getSiteRankKey(siteId, rankType, platformVersionId , level);
		stringRedisTemplate.opsForZSet().add(key, String.valueOf(productId), score == null ? 0 : score);
	}
	
	/**
	 * 删除排行产品
	 */
	public void deleteProductsBySiteRank(Integer siteId, Long productId){
		String key = RedisKeys.getSiteRankKey4Delete(siteId);
		Set<String> keys = stringRedisTemplate.keys(key);
		if(null != keys && keys.size() > 0) {
			for(String d : keys) {
				stringRedisTemplate.opsForZSet().remove(d, String.valueOf(productId));
			}
		}
	}
	
	/**
	 * 产品的站点级别排行
	 * @param siteId
	 * @param rank
	 * @param platformVersionId
	 * @param level
	 * @param maxCount
	 * @return
	 */
	public List<Product> getProductsBySiteRank(Integer siteId,
			RankTypeEnum rank, Long platformVersionId, Integer level,Integer pageId, Integer maxCount) {
		if(null != rank) {
			String key = null;
			key = RedisKeys.getSiteRankKey(siteId, rank, platformVersionId , level);
			if(StringUtils.isNotBlank(key)) {
				long start = (pageId - 1) * maxCount;
				long end = pageId * maxCount - 1;
				Set<String> productIds = stringRedisTemplate.opsForZSet().reverseRange(key, start, end);
				if(log.isDebugEnabled()) {
					log.debug("getProductsByFolderRank() key:" + key + " \t productIds:" + productIds);
				}
				if(null != productIds && productIds.size() > 0) {
					return getMultipleProductWithProductFiles(productIds);
				}
			}
		}
		return null;
	}

	/**
	 * 随机获取容器列表
	 * @param page
	 * @param containerId
	 * @param maxCount
	 * @return
	 */
	public List<ProductContainer> getProductContainerListByRandom(
			HtmlPage page, Long containerId, Integer maxCount) {
		
		//四个参数,站点,频道,页面,容器ID
		//为什么需要四个参数,因为同一个容器,在不同页面下可以推荐不同数量及类型的产品
		String key = RedisKeys.getProductRecommandKey(page.getSiteId(), 
				page.getFolderId(), page.getPageId(), containerId);
		Long size = stringRedisTemplate.opsForZSet().size(key);
		Set<String> productIds = new HashSet<String>();
		
		//如果当前页面产品数多于要显示的产品数,则做随机取
		if(maxCount < size) {
			//产生maxCount个随机数,其中的最大值不会超过size-1
			Integer[] indexs = AppStringUtils.getRandomNumbers(maxCount, size.intValue() - 1);
			for(Integer i : indexs) {
				if(i <= size.intValue()) {
					productIds.addAll(this.stringRedisTemplate.opsForZSet().range(key, i, i));
				}
			}
		} else {
			//否则,全部取出,这是顺序取出,为了适应,需要反序取出
			productIds = this.stringRedisTemplate.opsForZSet().reverseRange(key, 0, maxCount -1);
		}
		
		if(null != productIds) {
			Collection<Object> hashKeys = new ArrayList<Object>();
			for(String id : productIds) {
				hashKeys.add(id);
			}
			
			//从redis中取出产品信息,并存入到hashKeys
			hashKeys = compressStringRedisTemplate.opsForHash().multiGet(RedisKeys.getProductRecommandKey(), hashKeys);

			List<ProductContainer> result = new ArrayList<ProductContainer>();
			for(Object json : hashKeys) {
				ProductContainer productContainer = this.objectMapperWrapper.converter2ProductContainer((String)json);
				if(null != productContainer) {
					result.add(productContainer);
				}
			}
			return result;
		}
		return null;
	}

	public long getProductSiteRankCount(Integer siteId, RankTypeEnum rankType,
			Long platFormId, Integer level) {
		String key = RedisKeys.getSiteRankKey(siteId, rankType, platFormId, level);
		Long count = stringRedisTemplate.opsForZSet().size(key);
		return count == null ? 0l : count;
	}

	public List<Product> getProductSiteRankList(Integer siteId,
			RankTypeEnum rankType, Long platFormId, Integer level,
			Integer pageSize, Integer pageIndex) {
		String key = RedisKeys.getSiteRankKey(siteId, rankType, platFormId, level);
		
		if(null == pageSize) {
			pageSize = 30;
		}
		long start = (pageIndex - 1) * pageSize;
		long end = pageIndex * pageSize - 1;
		
		Set<String> productIds = stringRedisTemplate.opsForZSet().reverseRange(key, start, end);
		
		if(log.isDebugEnabled()) {
			log.debug("getProductFolderRankList() key:" + key + " \t productIds:" + productIds);
		}
		
		if(null != productIds && productIds.size() > 0) {
			return getMultipleProductWithProductFiles(productIds);
		}
		return null;
	}

	public List<Product> getProductSiteRanksByRandom(Integer siteId,
			RankTypeEnum rankType, Long platformId, Integer level, Integer maxCount) {
		String key = RedisKeys.getSiteRankKey(siteId, rankType, platformId, level);
		Long size = stringRedisTemplate.opsForZSet().size(key);
		Set<String> productIds = new HashSet<String>();
		if(maxCount < size) {
			Integer[] indexs = AppStringUtils.getRandomNumbers(maxCount, size.intValue() - 1);
			for(Integer i : indexs) {
				if(i <= size.intValue()) {
					productIds.addAll(this.stringRedisTemplate.opsForZSet().range(key, i, i));
				}
			}
		} else {
			productIds = this.stringRedisTemplate.opsForZSet().range(key, 0, maxCount -1);
		}
		if(null != productIds) {
			Collection<Object> hashKeys = new ArrayList<Object>();
			for(String id : productIds) {
				hashKeys.add(id);
			}
			hashKeys = compressStringRedisTemplate.opsForHash().multiGet(RedisKeys.getProductKey(), hashKeys);

			List<Product> result = new ArrayList<Product>();
			for(Object json : hashKeys) {
				result.add(this.objectMapperWrapper.converter2Product((String)json));
			}
			return result;
		}
		return null;
	}

	public List<Product> getProductFolderRankByRandom(Integer siteId,
			Long folderId, RankTypeEnum rankType, Long platformId,
			Integer level, Integer maxCount) {
		String key = RedisKeys.getFolderRankKey(siteId, folderId, rankType, platformId, level);
		Long size = stringRedisTemplate.opsForZSet().size(key);
		Set<String> productIds = new HashSet<String>();
		if(maxCount < size) {
			Integer[] indexs = AppStringUtils.getRandomNumbers(maxCount, size.intValue() - 1);
			for(Integer i : indexs) {
				if(i <= size.intValue()) {
					productIds.addAll(this.stringRedisTemplate.opsForZSet().range(key, i, i));
				}
			}
		} else {
			productIds = this.stringRedisTemplate.opsForZSet().range(key, 0, maxCount -1);
		}
		if(null != productIds && productIds.size() > 0) {
			return getMultipleProductWithProductFiles(productIds);
		}
		return null;
	}
	
	/**
	 * 取一个频道在某个站点的某个排行下的产品数量
	 * @param siteId 站点ID
	 * @param folderId 频道ID
	 * @param rankType 排行类型
	 * @param platformId 平台
	 * @param level 
	 * @return 数量
	 */
	public Long getProductCountByFolder(Integer siteId,
			Long folderId, RankTypeEnum rankType, Long platformId,
			Integer level){
		
		String key = RedisKeys.getFolderRankKey(siteId, folderId, rankType, platformId, level);
		if(key==null) return 0L;
		
		return stringRedisTemplate.opsForZSet().size(key);
	}

	/**
	 * 取得排行的数据
	 * @param folder
	 * @param rankType
	 * @param platformId
	 * @param level
	 * @return
	 */
	public long getProductFolderRankCount(Folder folder, RankTypeEnum rankType,
			Long platformId, Integer level) {
		String key = RedisKeys.getFolderRankKey(folder.getSiteId(), 
				folder.getFolderId(), rankType, platformId, level);
		Long count = stringRedisTemplate.opsForZSet().size(key);
		return count == null ? 0l : count;
	}

	public List<Product> getProductFolderRankList(Folder folder,
			RankTypeEnum rankType, Long platformId, Integer level,
			Integer pageSize, Integer pageIndex) {
		String key = RedisKeys.getFolderRankKey(folder.getSiteId(), 
				folder.getFolderId(), rankType, platformId, level);
		
		if(null == pageSize) {
			pageSize = 30;
		}
		long start = (pageIndex - 1) * pageSize;
		long end = pageIndex * pageSize - 1;
		
		Set<String> productIds = stringRedisTemplate.opsForZSet().reverseRange(key, start, end);
		
		if(log.isDebugEnabled()) {
			log.debug("getProductsByFolderRank() key:" + key + " \t productIds:" + productIds);
		}
		
		if(null != productIds && productIds.size() > 0) {
			return getMultipleProductWithProductFiles(productIds);
		}
		return null;
	}

	public long getProductContainerCount(HtmlPage page, Long containerId,
			Long platformId, Integer level) {
		if(null == page) {
			return 0l;
		}
		String key = RedisKeys.getProductRecommandKey(page.getSiteId(), 
				page.getFolderId(), page.getPageId(), containerId);
		return this.stringRedisTemplate.opsForZSet().size(key);
	}

	/**
	 * 根据各种参数获取产品容器关系列表
	 * @param page 页面
	 * @param containerId 容器ID
	 * @param platformId 平台版本
	 * @param level 分辨率
	 * @param pageSize 每页显示数量
	 * @param pageIndex 当前页数
	 * @return
	 */
	public List<ProductContainer> getProductContainerList(HtmlPage page,
			Long containerId, Long platformId1, Integer level1, Integer pageSize,
			Integer pageIndex) {
		if(null == pageSize) {
			pageSize = 30;
		}
		
		//范围设定
		long start = (pageIndex - 1) * pageSize;
		long end = pageIndex * pageSize - 1;
		
		String key = RedisKeys.getProductRecommandKey(page.getSiteId(), 
				page.getFolderId(), page.getPageId(), containerId);
		Set<String> productIds = stringRedisTemplate.opsForZSet().reverseRange(key, start, end);
		
		if(null != productIds) {
			Collection<Object> hashKeys = new ArrayList<Object>();
			for(String id : productIds) {
				hashKeys.add(id);
			}
			hashKeys = compressStringRedisTemplate.opsForHash().multiGet(RedisKeys.getProductRecommandKey(), hashKeys);
			List<ProductContainer> result = new ArrayList<ProductContainer>();
			if(null != hashKeys) {
				for(Object json : hashKeys) {
					ProductContainer productContainer = objectMapperWrapper.converter2ProductContainer((String)json);
					if(null != productContainer) {
						result.add(productContainer);
					}
				}
			}
			return result;
		}
		return null;
	}

	/**
	 * 
	 * @param keySet
	 * @param pref
	 * @return
	 */
	public List<Product> getProductByProductIds(Set<Long> keySet, UserPreference pref) {
		if(null != keySet) {
			Collection<String> hashKeys = new ArrayList<String>();
			for(Long id : keySet) {
				hashKeys.add(String.valueOf(id));
			}
			return getMultipleProductWithProductFiles(hashKeys);
		}
		return null;
	}
	
	/* 智能（intelligent）推荐部分*/
	
	/**
	 * 推荐对象
	 */
	public void setIntelligentRecommend(Integer siteId, Long productId, ProductIntelligentRecommend productIntelligentRecommend) {
		String value = objectMapperWrapper.convert2String(productIntelligentRecommend);
		if(StringUtils.isNotBlank(value)) {
			String key = RedisKeys.getProductIntelligentRecommendKey(siteId);
			compressStringRedisTemplate.opsForHash().put(key,String.valueOf(productId),value);
		}
	}
	
	public ProductIntelligentRecommend getIntelligentRecommend(Integer siteId, Long productId) {
		String value = (String)compressStringRedisTemplate.opsForHash().get(RedisKeys.getProductIntelligentRecommendKey(siteId), String.valueOf(productId));
		if(StringUtils.isNotBlank(value)) {
			return this.objectMapperWrapper.convert2ProductIntelligentRecommend(value);
		}
		return null;
	}
	
	public void deleteIntelligentRecommend(Integer siteId, Long productId) {
		compressStringRedisTemplate.opsForHash().delete(RedisKeys.getProductIntelligentRecommendKey(siteId), String.valueOf(productId));
	}
	
	/**
	 * 频道推荐对象
	 */
	public void setFolderIntelligentRecommend(Long folderId, ProductIntelligentRecommend productIntelligentRecommend) {
		String value = objectMapperWrapper.convert2String(productIntelligentRecommend);
		if(StringUtils.isNotBlank(value)) {
			String key = RedisKeys.getFolderProductIntelligentRecommendKey();
			compressStringRedisTemplate.opsForHash().put(key,String.valueOf(folderId),value);
		}
	}
	
	public ProductIntelligentRecommend getFolderIntelligentRecommend(Long folderId) {
		String value = (String)compressStringRedisTemplate.opsForHash().get(RedisKeys.getFolderProductIntelligentRecommendKey(), String.valueOf(folderId));
		return this.objectMapperWrapper.convert2ProductIntelligentRecommend(value);
	}
	
	public void deleteFolderIntelligentRecommend(Long folderId) {
		compressStringRedisTemplate.opsForHash().delete(RedisKeys.getFolderProductIntelligentRecommendKey(), String.valueOf(folderId));
	}
	
	/**
	 * 下载推荐数据
	 * @param productId
	 * @param productIds
	 */
	public void setProductRecommendBehaviors(Integer siteId, Long productId, List<Long> productIds) {
		String key = RedisKeys.getProductRecommendBehaviorsKey(siteId);
		if (productIds != null && productIds.size() > 0) {
			String value = objectMapperWrapper.convert2String(productIds);
			compressStringRedisTemplate.opsForHash().put(key,String.valueOf(productId),value);
		} else {
			compressStringRedisTemplate.opsForHash().delete(key,String.valueOf(productId));
		}
	}
	
	/**
	 * 返回按用户行为得到的产品相似度
	 * @param productId
	 * @return
	 */
	public List<Long> getProductRecommendBehaviors(Integer siteId, Long productId) {
		String value = (String)compressStringRedisTemplate.opsForHash().get(RedisKeys.getProductRecommendBehaviorsKey(siteId), String.valueOf(productId));
		if (StringUtils.isNotEmpty(value)) {
			return this.objectMapperWrapper.convert2ListLong(value);
		} else {
			return null;
		}
	}
	
	public void deleteProductRecommendBehaviors(Integer siteId, Long productId) {
		compressStringRedisTemplate.opsForHash().delete(RedisKeys.getProductRecommendBehaviorsKey(siteId) ,String.valueOf(productId));
	}
	
	/**
	 * 相似推荐数据
	 * @param productId
	 * @param productIds
	 */
	public void setProductRecommendSimilars(Integer siteId, Long productId, List<Long> productIds) {
		String key = RedisKeys.getProductRecommendSimilarsKey(siteId);
		if (productIds != null && productIds.size() > 0) {
			String value = objectMapperWrapper.convert2String(productIds);
			compressStringRedisTemplate.opsForHash().put(key,String.valueOf(productId),value);
		} else {
			compressStringRedisTemplate.opsForHash().delete(key,String.valueOf(productId));
		}
	}
	
	public List<Long> getProductRecommendSimilars(Integer siteId, Long productId) {
		String value = (String)compressStringRedisTemplate.opsForHash().get(RedisKeys.getProductRecommendSimilarsKey(siteId), String.valueOf(productId));
		if (StringUtils.isNotEmpty(value)) {
			return this.objectMapperWrapper.convert2ListLong(value);
		} else {
			return null;
		}
	}
	
	public void deleteProductRecommendSimilars(Integer siteId, Long productId) {
		compressStringRedisTemplate.opsForHash().delete(RedisKeys.getProductRecommendSimilarsKey(siteId) ,String.valueOf(productId));
	}
	
	/**
	 * 推荐库数据
	 * @param siteId
	 * @param productId
	 */
	public void addProductRecommendStorage(Integer siteId, Long productId) {
		String key = RedisKeys.getProductRecommendStorageKey(siteId);
		stringRedisTemplate.opsForZSet().add(key, String.valueOf(productId), 0);
	}
	
	public void deleteProductRecommendStorage(Integer siteId, Long productId) {
		String key = RedisKeys.getProductRecommendStorageKey(siteId);
		stringRedisTemplate.opsForZSet().remove(key, String.valueOf(productId));
	}

	public void clearProductRecommendStorage(Integer siteId) {
		String key = RedisKeys.getProductRecommendStorageKey(siteId);
		stringRedisTemplate.delete(key);
	}
	
	public List<Long> getProductRecommendStorage(Integer siteId,int theSize) {
		String key = RedisKeys.getProductRecommendStorageKey(siteId);
		Set<String> productIds = stringRedisTemplate.opsForZSet().reverseRange(key, 0, theSize - 1);
		List<Long> pids = new ArrayList<Long>();
		if (productIds != null && productIds.size() > 0) {
			for (String pid : productIds) {
				pids.add(Long.valueOf(pid));
			}
			return pids;
		} else {
			return null;
		}
	}

	public void setFolderRankDaily(Folder folder,
			RankTypeEnum rankType, Long platformVersionId, Long resolutionId, List<Product> list) {
		String key = RedisKeys.getFolderRankDailyKey(folder.getFolderId(), rankType, platformVersionId, resolutionId);
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<list.size();i++) {
			Product p = list.get(i);
			if(null != p && null != p.getProductList()) {
				if(i == list.size() -1) {
					sb.append(p.getProductList().getProductId());
				} else {
					sb.append(p.getProductList().getProductId()).append(";");
				}
			}
		}
		stringRedisTemplate.opsForValue().set(key, sb.toString(), 2, TimeUnit.DAYS);
	}

	public List<Product> getFolderRankDaily(Folder folder, RankTypeEnum rankType, Long platformVersionId, Long resolutionId) {
		String key = RedisKeys.getFolderRankDailyKey(folder.getFolderId(), rankType, platformVersionId, resolutionId);
		key = stringRedisTemplate.opsForValue().get(key);
		if(StringUtils.isNotBlank(key)) {
			String[] keys = key.split(";");
			return getMultipleProductWithProductFiles(Arrays.asList(keys));
		}
		return null;
	}
	
	/**
	 * 设定推荐产品
	 * @param siteId
	 * @param containerId
	 * @param pc
	 * @param platformVersionId
	 * @param level
	 */
	public void setProductIdsInContainer(int siteId, Long containerId, ProductContainer pc, Long platformVersionId, int level) {
		String key = RedisKeys.getSiteContainerKey(siteId, containerId, platformVersionId, level);
		stringRedisTemplate.opsForZSet().add(key, String.valueOf(pc.getProductId()), pc.getSortOrder());
	}
	
	/**
	 * 删除推荐中的产品
	 * @param siteId
	 * @param containerId
	 * @param pc
	 */
	public void deleteProductIdInContainer(Integer siteId, Long recommandContainerId) {
		String key = RedisKeys.getSiteContainerKey4Delete(siteId, recommandContainerId);
		Set<String> keys = stringRedisTemplate.keys(key);
		if (keys != null && keys.size() > 0) {
			for (String d : keys) {
				stringRedisTemplate.delete(d);
			}
		}
	}

	/**
	 * 根据站点／平台版本／分辨率等级取容器中的产品ID列表。
	 * @param siteId
	 * @param containerId
	 * @param platformVersionId
	 * @param level
	 * @param maxCount
	 * @return
	 */
	public List<Long> getProductIdsInContainer(Integer siteId, Long containerId, Long platformVersionId, int level, Integer maxCount) {
		String key = RedisKeys.getSiteContainerKey(siteId, containerId, platformVersionId, level);
		Set<String> productIds = stringRedisTemplate.opsForZSet().reverseRange(key, 0, maxCount - 1);
		List<Long> pids = new ArrayList<Long>();
		if (productIds != null && productIds.size() > 0) {
			for (String pid : productIds) {
				pids.add(Long.valueOf(pid));
			}
			return pids;
		} else {
			return null;
		}
	}
	
	/**
	 * 将当前KEY删除，以清除其对应的原有信息
	 * @param pages
	 * @param containerId
	 */
	public void clearItem(HtmlPage page,Long containerId){
		
		String key = RedisKeys.getProductRecommandKey(page.getSiteId(), 
			page.getFolderId(), page.getPageId(), containerId);
		this.stringRedisTemplate.delete(key);
	}
	
}
