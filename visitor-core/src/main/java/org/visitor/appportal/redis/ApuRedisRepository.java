package org.visitor.appportal.redis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import org.visitor.app.portal.model.Product;
import org.visitor.appportal.domain.ProductFile;
import org.visitor.appportal.domain.ProductSiteFolder;

/**
 * 与页面显示相关的数据存取处理类
 * @author mengw
 *
 */
@Repository
public class ApuRedisRepository {
	
	@Autowired
	private StringRedisTemplate apuStringRedisTemplate;
	
	private static final String OPENED = "1";
	private static final String CLOSED = "0";
	
	/**
	 * 获取指定用户的可更新产品列表（部分）
	 * @param userKey 用户
	 * @param start 数据开始位置
	 * @param end 结束位置
	 * @return Set集合的产品信息
	 */
	public Set<String> getUserApuUpdateProductId(String userKey,long start, long end) {
		String key = RedisKeys.getApuUserUpdateProductKey(userKey);
		return apuStringRedisTemplate.opsForZSet().range(key, start, end);
	}
	
	
	/**
	 * 忽略某产品对指定用户的更新，即将指定产品放入指定用户的忽略列表中
	 * @param userKey 用户
	 * @param productId 产品
	 * @param total_dl 排序值
	 */
	public void setUserApuIgnoreProductId(String userKey, Long productId, Long total_dl) {
		String key = RedisKeys.getApuUserIgnoreProductKey(userKey);
		apuStringRedisTemplate.opsForZSet().add(key, String.valueOf(productId), total_dl);
	}
	
	/**
	 * 获取指定用户的可更新软件数量，同时减去状态变量
	 * @param userKey 用户Uid
	 * @return 软件数量，Long型
	 */
	public Long getUserApuUpdateProductCount(String userKey){
		String key = RedisKeys.getApuUserUpdateProductKey(userKey);
		Long size = apuStringRedisTemplate.opsForZSet().size(key);
		if (size != null) {
			return size - 1;
		} else {
			return 0l;
		}
	}
	
	/**
	 * 获取指定用户的已忽略产品列表（部分）
	 * @param userKey 用户
	 * @param start 开始位置
	 * @param end 结束位置
	 * @return Set集合
	 */
	public Set<String> getUserApuIgnoreProductId(String userKey,long start, long end) {
		String key = RedisKeys.getApuUserIgnoreProductKey(userKey);
		return apuStringRedisTemplate.opsForZSet().range(key, start, end);
	}
	
	/**
	 * 根据包名和签名获取对应的产品文件信息
	 * @param packageName 包名
	 * @param identity 签名
	 * @return 对应产品信息
	 */
	public String getProductFileApkInfo(String packageName, String identity){
		String key = RedisKeys.getApuProductFileApkInfo();
		String name_key = packageName + RedisKeys.getApuRedisSplit() + identity;
		return (String)apuStringRedisTemplate.opsForHash().get(key, name_key);
	}
	
	/**
	 * 将产品文件信息信息存储Redis中，
	 * Key信息：产品文件的包名 ＋ 签名
	 * Value ： 产品ID ＋ 产品文件的VersionCode ＋ 文件版本
	 * @param productFile 要存储的产品文件
	 */
	public void setProductFileApkInfo(ProductFile productFile){
		String key = RedisKeys.getApuProductFileApkInfo();
		String name_key = productFile.getPackageName() + RedisKeys.getApuRedisSplit() + productFile.getIdentity();
		if (productFile.getVersionCode() != null && productFile.getVersionCode().intValue() > 0) {
			String value = String.valueOf(productFile.getProductId())
					+ RedisKeys.getApuRedisSplit() + String.valueOf(productFile.getVersionCode()
					+ RedisKeys.getApuRedisSplit() + productFile.getVersionName());
			apuStringRedisTemplate.opsForHash().put(key, name_key, value);
		}
	}
	
	/**
	 * 保存用户的软件更新设置
	 * @param userKey 用户
	 * @param updateEnabled 更新提醒是否打开
	 */
	public void setUserApuSettingStatus(String userKey,boolean updateEnabled){
		
		String key = RedisKeys.getApuSettingStatusKey(userKey);
		apuStringRedisTemplate.expire(key, 7, TimeUnit.DAYS);
		
		String value = updateEnabled ? OPENED : CLOSED;
		
		apuStringRedisTemplate.opsForValue().set(key, value);
		
	}
	
	/**
	 * 获取用户的软件更新设置，如果没取到，默认为打开
	 * @param userKey 用户 imei + '---' + imsi
	 * @return 更新提醒是否打开 
	 */
	public boolean getUserApuSettingStatus(String userKey){
		String key = RedisKeys.getApuSettingStatusKey(userKey);
		
		String value = apuStringRedisTemplate.opsForValue().get(key);
		
		if(value != null && (value.equals(OPENED) || value.equals(CLOSED))){
			return value.equals(CLOSED) ? false : true; 
		}
		
		return true;
	}

	/**
	 * 根据用户的imei + imsi和站点信息返回相应的软件更新数量
	 * 由于该Key第一个为状态值，所以总数应该减1
	 * @param userKey
	 * @param siteId
	 * @return
	 */
	public Long getUserApuUpdateProductCount(String userKey, Integer siteId) {
		// TODO Auto-generated method stub
		String key = RedisKeys.getApuUserUpdateProductKey(userKey,siteId);
		//apuStringRedisTemplate.delete(key);
		Long size = apuStringRedisTemplate.opsForZSet().size(key);
		if (size != null) {
			return size - 1;
		} else {
			return 0l;
		}
		
	}
	
	/**
	 * 从原Key中获取所有数据，然后更新到用户－站点Key中去
	 * 并返回新的统计数量
	 * @param userKey
	 * @param siteId
	 */
	public Long checkAndUpdateFromOld(String userKey,Integer siteId,List<Product> allList){
		/**如果有值，一般会执行到这个分支*/
		
		Long count = 0L;
		List<String> newList = new ArrayList<String>();
		String key = RedisKeys.getApuUserUpdateProductKey(userKey, siteId);
		
		/**正常情况下来说，如果是allCount值不为Null，则这个列表也不应该为Null*/
		if(allList != null && allList.size() > 0){
			for(Product product : allList){
				List<ProductSiteFolder> psfs = product.getFolders();
				
				if(psfs != null && psfs.size() > 0 && product.getProductFiles() != null && product.getProductFiles().size()>0){
					Integer versionCode = product.getProductFiles().get(0).getVersionCode();
					for (ProductSiteFolder psf : psfs){
						if(psf.getSiteId() != null && psf.getSiteId().intValue() == siteId){
							//该应用属于当前站点
							newList.add(product.getProductList().getProductId() + RedisKeys.getApuRedisSplit() + versionCode + RedisKeys.getApuRedisSplit() + product.getOldVersion());
							break;
						}
					}
				}
			}
			
			//这种情况下，需要将新数据发布到新的Key中,旧Key不动
			/**存储状态变量*/
			setStatusByUserAndSite(userKey,siteId,Status.DATAS);
			
			double score = 1;
			for(String data : newList){
				apuStringRedisTemplate.opsForZSet().add(key, data, score++);			
			}
			count = (long) newList.size();
		}else {
			setStatusByUserAndSite(userKey,siteId,Status.EMPTY);
		}
		
		return count;
	}
	
	/**
	 * 从原Key中获取所有数据，然后更新到用户Key中去
	 * @param userKey
	 */
	public void updateFromHbase(String userKey,List<String> datas){
		String key = RedisKeys.getApuUserUpdateProductKey(userKey);
		apuStringRedisTemplate.opsForZSet().removeRange(key, 0, 0);
		double score = 0;		
		/**先加入一个头信息*/
		apuStringRedisTemplate.opsForZSet().add(key, "0", score++);
		
		for(String data : datas){
			apuStringRedisTemplate.opsForZSet().add(key, data, score++);			
		}
	}


	public Collection<String> getUserApuUpdateProductIdAndSiteId(String userKey,
			Integer siteId, long start, long end) {
		// TODO Auto-generated method stub
		String key = RedisKeys.getApuUserUpdateProductKey(userKey,siteId);
		return apuStringRedisTemplate.opsForZSet().range(key, start, end);
	}

	/**
	 * 查找该用户站点Key是否存在
	 * 如果这个Key存在，再判断里面的状态
	 * @param userKey
	 * @param siteId
	 * @return
	 */
	public Status getStatusFromUserAndSite(String userKey, Integer siteId) {
		// TODO Auto-generated method stub
		String key = RedisKeys.getApuUserUpdateProductKey(userKey,siteId);
		Set<String> items = apuStringRedisTemplate.opsForZSet().range(key, 0, 0);

		Status st = Status.NOTEXISTED;
		
		if(items != null && items.size() > 0){
			Iterator<String> it = items.iterator();
			while(it.hasNext()) {
				
				st = Status.getValueByName(it.next());//取其第一个
				break;
			}
		}
		
		
		return st;
	}

	/**
	 * 查找该用户Key是否存在
	 * @param uid
	 * @param siteId
	 * @return
	 */
	public boolean getStatusFromUid(String uid,Integer siteId) {
		// TODO Auto-generated method stub
		String keyUid = RedisKeys.getApuUserUpdateProductKey(uid);
		String keyUidAndSite = RedisKeys.getApuUserUpdateProductKey(uid,siteId);
		
		if(apuStringRedisTemplate.hasKey(keyUid)){
			/**存在，需要进一步分析，用户是不需要更新呢，还是说有更新列表
			 * 这里暂时不同步到用户－站点Key中，而只是设置状态*/
			Long size = this.apuStringRedisTemplate.opsForZSet().size(keyUid);
			
			if(size == 1L){
				this.apuStringRedisTemplate.opsForZSet().add(keyUidAndSite, Status.EMPTY.name(), 0);
			}else {
				this.apuStringRedisTemplate.opsForZSet().add(keyUidAndSite, Status.DATAS.name(), 0);				
			}

			this.apuStringRedisTemplate.expire(keyUidAndSite, 1, TimeUnit.HOURS);

			return true;
		}
		
		return false;
	}
	
	public enum Status {
		
		EMPTY,NOTEXISTED,DATAS;
		
		public static Status getValueByName(String value){
			
			if(StringUtils.isNotBlank(value)){
				/**只有这三种值是有效的*/
				value = value.trim();				
				if(value.equals("EMPTY") || value.equals("NOTEXISTED") 
					|| value.equals("DATAS")){
					
					return Status.valueOf(value);
				}
			}
			
			return Status.NOTEXISTED;
		}
	}

	/**
	 * 设置用户－站点信息的状态
	 * @param userKey
	 * @param siteId
	 * @param datas
	 */
	public void setStatusByUserAndSite(String userKey, Integer siteId, Status datas) {
		// TODO Auto-generated method stub
		String keyUserAndSite = RedisKeys.getApuUserUpdateProductKey(userKey,siteId);
		/**删除原来的*/
		apuStringRedisTemplate.opsForZSet().removeRange(keyUserAndSite, 0, 0);
		
		this.apuStringRedisTemplate.opsForZSet().add(keyUserAndSite, datas.name(), 0);

		this.apuStringRedisTemplate.expire(keyUserAndSite, 1, TimeUnit.HOURS);
	}

	/**
	 * 判断用户－站点信息Key是否存在，只是判断，不做其它处理
	 * @param userKey
	 * @param siteId
	 * @return
	 */
	public boolean getInfoExisted(String userKey, Integer siteId) {
		// TODO Auto-generated method stub
		String key = RedisKeys.getApuUserUpdateProductKey(userKey,siteId);
		return apuStringRedisTemplate.hasKey(key);
	}
}
