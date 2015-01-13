/**
 * 
 */
package org.visitor.appportal.redis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.visitor.app.portal.model.CategoryNameComparator;
import org.visitor.app.portal.model.CategoryPublishMessage;
import org.visitor.app.portal.model.CategorySortComparator;
import org.visitor.app.portal.model.Product;
import org.visitor.app.portal.model.UserPreference;
import org.visitor.appportal.domain.Category;
import org.visitor.appportal.domain.ProductFile;
import org.visitor.appportal.domain.ProductFile.VersionOperatorEnum;
import org.visitor.appportal.domain.Site;
import org.visitor.appportal.domain.SiteValue;
import org.visitor.appportal.domain.SiteValue.TypeEnum;
import org.visitor.util.AppStringUtils;
import org.visitor.util.BusinessTools;
import org.visitor.util.UserAgentChecker;

/**
 * 用来解析用户的平台／版本／分辨率／站点
 * @author mengw
 *
 */
@Component
public class UserPreferenceResolver {
	protected static final Logger logger = LoggerFactory.getLogger(UserPreferenceResolver.class);

	@Autowired
	protected SiteRedisRepository siteRedisRepository;
	@Autowired
	private CategoryRedisRepository categoryRedisRepository;
	//缓存分辨率
	private List<Category> resolutionList = null;
	//缓存平台下的所有版本
	private Map<Long, List<Category>> platformVerionList = new HashMap<Long, List<Category>>();
	//缓存平台版本。
	private Map<Long, Category> platformVersionMap = new HashMap<Long, Category>();
	//缓存产品来源
	private Map<Long, Category> productSourceMap = new HashMap<Long, Category>();
	/**
	 * 
	 */
	public UserPreferenceResolver() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 获取手机平台版本，对应的模型为Category
	 * 1）通过参数获取平台版本，如果取到，则从redis中取出对应的版本信息
	 * 2）没有，则从User-Agent中获取平台信息
	 * @param request
	 * @return
	 */
	protected Category resolvePlatformId(String phoneUa, String ua, String requestParam) {
		String platform = new UserAgentChecker(phoneUa).getPlatformName();//
		Category cat = null;
		
		if(StringUtils.isNotBlank(platform)) {
			cat = categoryRedisRepository.getCategoryPlatformByName(platform);
		}
		if(cat == null) {
			if(logger.isInfoEnabled()) {
				logger.info("No platform[" + platform + "] found in " + phoneUa);
			}
			platform = new UserAgentChecker(ua).getPlatformName();
			if(logger.isInfoEnabled()) {
				logger.info("UA platform[" + platform + "]");
			}
			cat = categoryRedisRepository.getCategoryPlatformByName(platform);
			//需要通过请求参数来确定
			if(cat == null) {
				platform = new UserAgentChecker(requestParam).getPlatformName();
				if(logger.isInfoEnabled()) {
					logger.info("Request param platform[" + platform + "]");
				}
				cat = categoryRedisRepository.getCategoryPlatformByName(platform);
			}
		}
		return cat;
	}
	
	/**
	 * 根据用户的当前偏好，获取用户需要访问的站点。
	 * 1）按照域名进行判断
	 * 2）如果相同域名的站点有多个，则根据平台版本进行判断
	 * 3) 正常情况下，不会有多个域名相同，且平台版本也相同的站点，如果有，则取第一个，所以这里不需要使用List
	 * @param pref
	 * @return
	 * @throws MultipleSiteFoundException
	 */
	protected Site resolveSite(UserPreference pref) throws MultipleSiteFoundException {
		//否则查找所有站点
		List<Site> sites = this.siteRedisRepository.getSiteList();
		Site currSite = null;
		if(null != sites) {
			for(Site site : sites) {//首先过滤域名
				String[] domains = StringUtils.split(site.getSchemaName(), ",");//多个域名用“,”分割
				for(String domain : domains) {
					if(StringUtils.equalsIgnoreCase(domain, pref.getDomain())) {
						//如果找到匹配的站点信息，则将站点添加进去
						if(platformVersionMath(site.getSiteValues(), pref.getPlatformVersionId(), pref.getPlatformId())) {
							currSite = site;	
							break;
						}
					}
				}
			}
		}
		
		return currSite;//No site found, return null.
	}
	
	/**
	 * 用户的手机平台和版本是否和站点信息匹配
	 * @param siteValues 站点属性
	 * @param platformVersionId 用户手机平台版本
	 * @param platformId 用户手机平台
	 * @return 是否匹配
	 */
	protected boolean platformVersionMath(List<SiteValue> siteValues,
			Long platformVersionId, Long platformId) {
		for(SiteValue siteValue : siteValues) {
			TypeEnum type = TypeEnum.getInstance(siteValue.getType());
			//如果具体版本匹配，则认为匹配
			if(null != platformVersionId) {
				if(type == TypeEnum.PlatformVerion 
					&& siteValue.getValue().longValue() == platformVersionId.longValue()) {
					return true;
				}
			} else if(null != platformId){ //否则，如果平台匹配，也认为匹配
				if(type == TypeEnum.Platform && siteValue.getValue().longValue() == platformId.longValue()) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 解析并设定手机平台版本
	 * @param platformCategory
	 * @param request
	 * @return
	 */
	protected Category resolvePlatformVersionId(Category platformCategory, String phoneUa, String ua, String requestParam) {
		String version4PhoneUa = null;
		String version4Ua = null;
		String version4Pf = null;
		if(StringUtils.isNotBlank(phoneUa)) {
			UserAgentChecker checker = new UserAgentChecker(phoneUa);
		
			//获取手机平台
			version4PhoneUa = checker.getPlatformVersion();
			
			if(logger.isInfoEnabled()) {
				logger.info("Resolved Platform and Version: [" + 
						(platformCategory == null ? "null" : platformCategory.getName()) + "][" + version4PhoneUa + "]");
			}
		}
		//从Redis中获取手机平台版本
		Category versionCategory = categoryRedisRepository.getCategoryPlatformVersionByName(platformCategory, version4PhoneUa);
		if(null == versionCategory) {//Not found by default platform version.
			//如果没有，则从UserAgent中查找版本信息
			version4Ua = new UserAgentChecker(ua).getPlatformVersion();
			if(logger.isInfoEnabled()) {
				logger.info("Resolved Platform and Version from User-Agent: [" + 
						(platformCategory == null ? "null" : platformCategory.getName()) + "][" + version4Ua + "]");
			}
			versionCategory = categoryRedisRepository.getCategoryPlatformVersionByName(platformCategory, version4Ua);
			
			//如果还找不到，通过请求参数中获取
			if(null == versionCategory) {//Can NOT determine user platform version, load the minimum one.
				version4Pf = new UserAgentChecker(requestParam).getPlatformVersion();
				if(logger.isInfoEnabled()) {
					logger.info("Resolved Platform and Version from request param: [" + 
							(platformCategory == null ? "null" : platformCategory.getName()) + "][" + version4Pf + "]");
				}
				versionCategory = categoryRedisRepository.getCategoryPlatformVersionByName(platformCategory, version4Pf);
				//如果还找不到，则使用该平台最小的版本号
				if(null == versionCategory) {
					//取出平台对应的版本信息
					List<Category> list = categoryRedisRepository.getPlatformVersionList(platformCategory.getCategoryId());
					if(null != list && list.size() > 0) {
						Category[] arrays = new Category[list.size()];
						arrays = list.toArray(arrays);
						Arrays.sort(arrays, new CategoryNameComparator());
						
						if(StringUtils.isNotBlank(version4PhoneUa)) {
							for(Category cat : arrays) {
								if(cat.getName().compareTo(version4PhoneUa) >= 0){//PhoneUA是否可以进行匹配
									versionCategory = cat;
									break;
								}
							}
						}
						
						if(null == versionCategory && StringUtils.isNotBlank(version4Ua)) {//UA是否有匹配
							for(Category cat : arrays) {
								if(cat.getName().compareTo(version4Ua) >= 0){//已经排序了，只需要取第一个比当前用户版本大的就可以了就ok了。
									versionCategory = cat;
									break;
								}
							}							
						}
						if(null == versionCategory && StringUtils.isNotBlank(version4Pf)) {//Platfrom是否有匹配
							for(Category cat : arrays) {
								if(cat.getName().compareTo(version4Pf) >= 0){//已经排序了，只需要取第一个比当前用户版本大的就可以了就ok了。
									versionCategory = cat;
									break;
								}
							}							
						}						
						
						if(null == versionCategory &&
								(StringUtils.isNotBlank(version4PhoneUa) || StringUtils.isNotBlank(version4Ua) || StringUtils.isNotBlank(version4Pf))) {
							versionCategory = arrays[arrays.length - 1];//当前版本大于所有版本，直接把系统中的最大版本返回。
						}
					}
					
					//如果还是取不到版本信息的话，可能不是android平台
					if(versionCategory == null) {
						if(null != list && list.size() > 0) {
							String[] names = new String[list.size()];
							for(int i=0;i<list.size();i++) {
								names[i] = list.get(i).getName();
							}
							Arrays.sort(names);
							for(Category cat : list) {
								if(StringUtils.equalsIgnoreCase(names[0], cat.getName())) {
									return cat;
								}
							}
						}

					}
				}
			}
		}

		return versionCategory;
	}	
	
	
	/**
	 * 获取平台分辨率
	 * @param request
	 * @param platformVersion
	 * @return
	 */
	protected Category resolveResolutionId(Category platformVersion, String width, String height) {
		List<Category> resolutionList = this.categoryRedisRepository.getResolutionList();
		
		//取得宽度
		if(StringUtils.isNotBlank(width) && StringUtils.isNotBlank(height)) {
			//如果高和宽都是0，则再进行处理
			if(StringUtils.equals(width, "0") || StringUtils.equals(height, "0")) {
				if(StringUtils.equals(width, "0")) {
					width = "480";
				}
				if(StringUtils.equals(height, "0")) {
					height = "640";
				}				
			}
			return BusinessTools.getBestResolution(Integer.valueOf(width), Integer.valueOf(height), platformVersion, resolutionList);
		}
		return null;
	}

	public Product caculateProductInfo(Product product, UserPreference userPreferece) {

		if(userPreferece == null || userPreferece.getSite() == null) {
			return null;
		}
		
		if(null != product && product.isValid() && product.matchSiteId(userPreferece.getSite().getSiteId())) {
			
			//得到用户机型对应产品的文件信息
			List<ProductFile> files = getBestProductFile(userPreferece, product.getProductFiles());
			//Need show the exact file infor.
			if(null != files && files.size() > 0) {
				caculateProductSize(product, files);
				if(null != product.getFileId()) {//只有有合适的可下载文件，才会显示产品来源。
					Long sourceId = product.getProductList().getSourceId();
					if(null != sourceId) {
						Category c = productSourceMap.get(sourceId);
						if(null == c) {
							c = categoryRedisRepository.getCategoryById(sourceId);
							productSourceMap.put(sourceId, c);
						}
						if(null != c) {
							if(StringUtils.isNotBlank(c.getEnName())) {
								product.setProductSourceName(c.getEnName());
							} else {
								product.setProductSourceName(c.getName());
							}
						}
					}
				}
				
				//但不能忘记的是，在取得了最优文件之后，还需要将文件关联到产品中去
				product.setProductFiles(files);
				return product;
			}
		}
		return null;		
	}

	public void caculateProductSize(Product product, List<ProductFile> files) {
		if(null != files && files.size() > 0) {
			ProductFile file = files.get(0);
			product.setFileSize(AppStringUtils.getTruncatedFileSize(file.getFileSize()));
			product.setFileId(file.getFileId());
		}
	}
	
	/**
	 * 对于一个给定的产品文件列表，根据用户信息来判断其能访问哪些文件
	 * 新增功能（Symbian用户要可以访问Java版本，首先是平台就不匹配，那就需要增加平台版本的访问）
	 * @param userPreferece 用户信息
	 * @param productFiles 给定产品文件列表
	 * @return 给定产品文件列表的一个子集
	 */
	protected List<ProductFile> getBestProductFile(UserPreference userPreferece, List<ProductFile> productFiles) {
		//用于存子集的LIST
		List<ProductFile> result = new ArrayList<ProductFile>();
		
		//有效性验证
		if(null != productFiles && productFiles.size() > 0) {
			//Map<Long, ProductFile> map = new HashMap<Long, ProductFile>();
			//取得版本和分辨率
			Category verion = this.getPlatformVersionById(userPreferece.getPlatformVersionId());//modelList.getPlatformVersion();
			Category resolution = this.getResolutionById(userPreferece.getResolutionId());//modelList.getResolution();
			
			//用户平台
			Category platform = this.getPlatformVersionById(userPreferece.getPlatformId());
			Category phoneSys = null;//手机操作系统
			
			if(platform != null){
				phoneSys = this.getPlatformVersionById(platform.getParentCategoryId());
			}
			
			ProductFile javaFile = null;//专门用来存储Java文件，只有在特殊情况下才使用
			//屏幕面积
			Long areas = AppStringUtils.getScreenSize(resolution.getName());//Long.valueOf(Integer.valueOf(array[0]) * Integer.valueOf(array[1]));
			
			List<ProductFile> sortedProdctFiles = new ArrayList<ProductFile>();
			for(ProductFile file : productFiles) {
				if (file == null) {
					continue;
				}
				Category cat = this.getPlatformVersionById(file.getPlatformVersionId());
				boolean isAdded = false;
				for (int i = 0; i < sortedProdctFiles.size(); i++) {
					Category copyCat = this.getPlatformVersionById(sortedProdctFiles.get(i).getPlatformVersionId());
					if (copyCat == null || cat == null) {
						continue;
					}
					if (copyCat.getSortOrder() > cat.getSortOrder()) {
						continue;
					}
					else if (copyCat.getSortOrder() < cat.getSortOrder()) {
						sortedProdctFiles.add(i, file);
						isAdded = true;
						break;
					}
					else {
						if (sortedProdctFiles.get(i).getScreenSize() > file.getScreenSize()) {
							continue;
						}
						else if (sortedProdctFiles.get(i).getScreenSize() < file.getScreenSize()) {
							sortedProdctFiles.add(i, file);
							isAdded = true;
							break;
						}
						else {
							if (sortedProdctFiles.get(i).getCreateDate().compareTo(file.getCreateDate()) > 0) {
								continue;
							}
							else {
								sortedProdctFiles.add(i, file);
								isAdded = true;
								break;
							}
						}
					}
				}
				if (!isAdded) {
					sortedProdctFiles.add(file);
				}
			}
			
			productFiles = sortedProdctFiles;

			
			//遍历产品列表
			for(ProductFile file : productFiles) {
				if(null != file.getPlatformId() && null != file.getPlatformVersionId()) {
					//The same platform.
					//版本ID相当于分类的外键，所以平台ID的匹配，要取版本分类的父级ID
					
					if(file.getPlatformId().longValue() == verion.getParentCategoryId().longValue()) {
						//平台匹配，则继续看比较操作符
						VersionOperatorEnum operator = VersionOperatorEnum.getInstance(file.getVersionOperator());
						if(logger.isDebugEnabled()) {
							
							logger.debug("file:" + file.getFileId() + " operator: " + operator + " " + file.getPlatformVersionId()
									+ "area:" + areas + " fileareas:" + file.getScreenSize());
						}
						//
						if(areas >= file.getScreenSize()) {//手机分辨率不低于文件的分辨率，分辨率比较ok。
							if(null != operator ) {
								//取得该文件的版本，注意这里的cat指文件版本，而version指用户版本，这两者是有区别的
								Category cat = this.getPlatformVersionById(file.getPlatformVersionId());
								if(logger.isDebugEnabled()) {
									logger.debug("Category:" + cat.getSortOrder() + " current version:" + verion.getSortOrder() 
											+ "  " + (verion.getSortOrder().compareTo(cat.getSortOrder())) 
											+ " areas: " + areas + " filearea:" + file.getScreenSize());
								}
								if(null != cat) {
									int versionOrder = verion.getSortOrder();
									int catOrder = cat.getSortOrder();
									
									if(operator == VersionOperatorEnum.LE && versionOrder <= catOrder
										|| operator == VersionOperatorEnum.GE && versionOrder >= catOrder
										|| operator == VersionOperatorEnum.EQ && versionOrder == catOrder) {
										
										result.add(file);
										if(logger.isDebugEnabled()) {
											logger.debug("Add file: " + file.getFileId());
										}
										
										break;//退出循环
									}
								}//file version cannot be null
							}//operator cannot be null							
						}//screen size must match
					}else {//version must match
						//版本不匹配，那么这种情况下，考虑是否存在一个Java版本的文件
						//由于Java的版本只有一种，所以不用考虑版本信息，考虑平台即可
						if(null != phoneSys && phoneSys.getName().equals("Symbian") && javaFile == null){
							//产品的平台
							Category filePlatform = this.getPlatformVersionById(file.getPlatformId());
							
							if(null != filePlatform && filePlatform.getName().equals("Java")){
								//只要分辨率OK就行
								if(areas >= file.getScreenSize()) {
									javaFile = file;
								}
							}
						}
						//javaFile 不为NULL，并不代表该文件会被使用，只有循环完了，实在找不到其它文件了，再找本文件
					}
				}//version cannot be null
			}//for
			
			if(result.size() == 0 && javaFile != null){
				result.add(javaFile);
			}
		}//productSize>0
		return result;
	}
	
	
	/**
	 * 取产品文件的平台版本。
	 * @param platformVersionId
	 * @return
	 */
	private Category getPlatformVersionById(Long platformVersionId) {
		if(!this.platformVersionMap.containsKey(platformVersionId)) {
			platformVersionMap.put(platformVersionId,
					categoryRedisRepository.getCategoryById(platformVersionId));
		}
		return this.platformVersionMap.get(platformVersionId);
	}
	
	public List<Category> getResolutionList() {
		if(null == this.resolutionList) {
			this.resolutionList = categoryRedisRepository.getResolutionList();
		}
		return this.resolutionList;
	}

	public Category getResolutionById(Long categoryId) {
		List<Category> list = getResolutionList();
		for(Category c : list) {
			if(null != c && null != c.getCategoryId()) {
				if(c.getCategoryId().longValue() == categoryId.longValue()) {
					return c;
				}
			}
		}
		return null;
	}
	
	public UserPreference resolveUserPreference(UserPreference pref, String phoneUa, String ua,
			String requestParam, String width, String height) {
		//解析并设定手机平台
		Category platform = this.resolvePlatformId(phoneUa, ua, requestParam);
		pref.setPlatformId(platform == null ? null : platform.getCategoryId());
		
		Category platformVersion = null;
		if(null != platform) {
			//解析并设定手机平台版本
			platformVersion = resolvePlatformVersionId(platform, phoneUa, ua, requestParam);
			pref.setPlatformVersionId(platformVersion == null ? null : platformVersion.getCategoryId());
		}

		//解析并设定平台的分辨率
		Category resolution = resolveResolutionId(platformVersion, width, height);
		pref.setResolutionId(resolution == null ? null : resolution.getCategoryId());
		//设定屏幕尺寸
		if(null != resolution && StringUtils.isNotBlank(resolution.getName())) {
			pref.setScreenSize(AppStringUtils.getScreenSize(resolution.getName()));
			//用户的分辨率转换为经度
			pref.setLongitude(resolution.getLongitude());
		}
		
		if(null != platform && null != platformVersion) {
			//用户当前平台版本的最小分辨率
			float value = getPlatformVersionMinResolution(pref.getPlatformId(), pref.getPlatformVersionId());
			pref.setMinPlatformVersionResolution(value);
			//用户当前平台的最小版本
			value = getPlatformVersionMinNumber(pref.getPlatformId());
			pref.setMinPlatformVersionNumber(value);
			//用户的平台版本转换为纬度
			pref.setLatitude(platformVersion.getLatitude());
		}
		
		if(null == pref.getSite() || pref.getSite().getSiteId() == null || pref.getSite().getSiteId() == 0) {//如果没有站点
			Site site;
			try {
				site = this.resolveSite(pref);
				if(null != site && site.getSiteId()!=null && site.getSiteId()!=0) {
					pref.setSite(site);
				}
			} catch (MultipleSiteFoundException e) {
				logger.error(pref.toString(), e);
				if(null == pref.getSite() || null == pref.getSite().getSiteId() || pref.getSite().getSiteId() == 0) {
					pref.setSite(new Site());
				}
			}
			
		}
		return pref;
	}
	
	/**
	 * 平台版本的最低分辨率转换为经度。
	 * @param platformId
	 * @param platformVersionId
	 * @return
	 */
	protected float getPlatformVersionMinResolution(Long platformId, Long platformVersionId) {
		Category cat = categoryRedisRepository.getPlatformVersionDefaultResolution(platformId, platformVersionId);
		if(null != cat) {
			return AppStringUtils.getScreenSize(cat.getName()) / categoryRedisRepository.getResolution2LongitudeRate();
		}
		return 0;
	}
	
	/**
	 * 平台版本转换为经度。经度在取值之前已经转换完毕
	 * @param platformId
	 * @return
	 */
	protected float getPlatformVersionMinNumber(Long platformId) {
		Category platform = new Category();
		platform.setPrimaryKey(platformId);
		List<Category> list = this.getPlatformVersionList(platform);//categoryRedisRepository.getPlatformVersionList(platformId);
		if(null != list && list.size() > 0) {
			Collections.sort(list, new CategorySortComparator());
			Category cat = list.get(0);
			return cat == null || cat.getLatitude() == null ? 0 : cat.getLatitude();
		}
		return 0;
	}
	
	public List<Category> getPlatformVersionList(Category platform) {
		if(null != platform && null != platform.getCategoryId()) {
			if(!platformVerionList.containsKey(platform.getCategoryId())) {
				List<Category> list = categoryRedisRepository.getPlatformVersionList(platform.getCategoryId());
				this.platformVerionList.put(platform.getCategoryId(), list);
			} 
			return this.platformVerionList.get(platform.getCategoryId());
		}
		return null;
	}

	public Category getTagByTagId(Long tagId) {
		return categoryRedisRepository.getCategoryById(tagId);
	}
	
	/**
	 * 需要重置缓存
	 * @param message
	 */
	public void setCategoryMessage(CategoryPublishMessage message) {
		if(null != message.getType()) {
			if(message.getType().longValue() == Category.RESOLUTION) {
				this.resolutionList = null;
			} else if(message.getType().longValue() == Category.PLATFORM) {
				this.platformVerionList.put(message.getCategory().getCategoryId(), message.getChildren());
			} else if(message.getType().longValue() == Category.PLATFORMVERSION) {
				this.platformVerionList.remove(message.getCategory().getParentCategoryId());
			}
		}		
	}
}