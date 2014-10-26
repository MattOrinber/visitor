/**
 * 
 */
package org.visitor.appportal.service.newsite;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author mengw
 *
 */
@Service
public class SystemPreference {

	private String pictureDomain;
	private String productPicRoot;
	private String productOrgPicRoot;
	private String templateDefaultPath;//模板默认目录
	private String templateContainerPath;//系统模板/单独页默认目录
	private String templateTagPath; //Tag标签默认目录
	private String htmlPageBasePath;
	private String webappUrl;
	private String productFileRoot;
	private String productImportRoot;
	
	private String productFileDirectory;
	private String productPicDirectory;
	
	private String containerIds;
	private String categoryIds;
	
	private String appApkInfo;
	
	//Soupeng api
	private String soupengApiAdd;
	private String soupengApiUpdate;
	private String soupengAppkey;
	private String soupengSid;
	
	//按照合作方式进行排序
	private Map<Long, Integer> gameCoorperationMap;
	private Map<Long, Integer> androidCoorperationMap;

	/**
	 * 
	 */
	public SystemPreference() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the pictureDomain
	 */
	public String getPictureDomain() {
		return pictureDomain;
	}

	/**
	 * @param pictureDomain the pictureDomain to set
	 */
	@Value("#{systemProperties['pic.domain']}")
	public void setPictureDomain(String pictureDomain) {
		this.pictureDomain = pictureDomain;
	}

	/**
	 * @return the productOrgPicRoot
	 */
	public String getProductOrgPicRoot() {
		return productOrgPicRoot;
	}

	/**
	 * @param productOrgPicRoot the productOrgPicRoot to set
	 */
	@Value("#{systemProperties['product.org.pic.root']}")
	public void setProductOrgPicRoot(String productOrgPicRoot) {
		this.productOrgPicRoot = productOrgPicRoot;
	}
	
	/**
	 * @return the productPicRoot
	 */
	public String getProductPicRoot() {
		return productPicRoot;
	}

	/**
	 * @param productPicRoot the productPicRoot to set
	 */
	@Value("#{systemProperties['product.pic.root']}")
	public void setProductPicRoot(String productPicRoot) {
		this.productPicRoot = productPicRoot;
	}

	public String getTemplateDefaultPath() {
		return templateDefaultPath;
	}

	public String getHtmlPageBasePath() {
		return htmlPageBasePath;
	}

	public String getTemplateContainerPath() {
		return templateContainerPath;
	}

	/**
	 * @param templateDefaultPath the templateDefaultPath to set
	 */
	@Value("#{systemProperties['template.default.path']}")
	public void setTemplateDefaultPath(String templateDefaultPath) {
		this.templateDefaultPath = templateDefaultPath;
	}

	/**
	 * @param templateContainerPath the templateContainerPath to set
	 */
	@Value("#{systemProperties['template.container.path']}")
	public void setTemplateContainerPath(String templateContainerPath) {
		this.templateContainerPath = templateContainerPath;
	}
	
	/**
	 * @param htmlPageBasePath the htmlPageBasePath to set
	 */
	@Value("#{systemProperties['htmlPage.base.path']}")
	public void setHtmlPageBasePath(String htmlPageBasePath) {
		this.htmlPageBasePath = htmlPageBasePath;
	}

	/**
	 * @return the templateTagPath
	 */
	public String getTemplateTagPath() {
		return templateTagPath;
	}

	/**
	 * @param templateTagPath the templateTagPath to set
	 */
	@Value("#{systemProperties['template.tag.path']}")
	public void setTemplateTagPath(String templateTagPath) {
		this.templateTagPath = templateTagPath;
	}

	public String getWebappUrl() {
		return webappUrl;
	}

	/**
	 * @param webappUrl the webappUrl to set
	 */
	@Value("#{systemProperties['webapp.url']}")
	public void setWebappUrl(String webappUrl) {
		this.webappUrl = webappUrl;
	}

	public String getProductFileRoot() {
		return productFileRoot;
	}

	/**
	 * @param productFileRoot the productFileRoot to set
	 */
	@Value("#{systemProperties['product.file.root']}")
	public void setProductFileRoot(String productFileRoot) {
		this.productFileRoot = productFileRoot;
	}
	
	/**
	 * @return the productImportRoot
	 */
	public String getProductImportRoot() {
		return productImportRoot;
	}

	/**
	 * @param productImportRoot the productImportRoot to set
	 */
	@Value("#{systemProperties['product.import.root']}")
	public void setProductImportRoot(String productImportRoot) {
		this.productImportRoot = productImportRoot;
	}
	


	/**
	 * @return the productFileDirectory
	 */
	public String getProductFileDirectory() {
		return productFileDirectory;
	}

	/**
	 * @param productFileDirectory the productFileDirectory to set
	 */
	@Value("#{systemProperties['appad.file.directory']}")
	public void setProductFileDirectory(String productFileDirectory) {
		this.productFileDirectory = productFileDirectory;
	}

	/**
	 * @return the productPicDirectory
	 */
	public String getProductPicDirectory() {
		return productPicDirectory;
	}

	/**
	 * @param productPicDirectory the productPicDirectory to set
	 */
	@Value("#{systemProperties['appad.pic.directory']}")
	public void setProductPicDirectory(String productPicDirectory) {
		this.productPicDirectory = productPicDirectory;
	}

	/**
	 * @return the containerIds
	 */
	public String getContainerIds() {
		return containerIds;
	}

	/**
	 * @param containerIds the containerIds to set
	 */
	@Value("#{systemProperties['app.task.containerIds']}")
	public void setContainerIds(String containerIds) {
		this.containerIds = containerIds;
	}

	/**
	 * @return the categoryIds
	 */
	public String getCategoryIds() {
		return categoryIds;
	}

	/**
	 * @param categoryIds the categoryIds to set
	 */
	@Value("#{systemProperties['app.task.categoryIds']}")
	public void setCategoryIds(String categoryIds) {
		this.categoryIds = categoryIds;
	}

	/**
	 * @return the appApkInfo
	 */
	public String getAppApkInfo() {
		return appApkInfo;
	}

	/**
	 * @param appApkInfo the appApkInfo to set
	 */
	@Value("#{systemProperties['app.apk.info']}")
	public void setAppApkInfo(String appApkInfo) {
		this.appApkInfo = appApkInfo;
	}

	/**
	 * @return the soupengApiAdd
	 */
	public String getSoupengApiAdd() {
		return soupengApiAdd;
	}

	/**
	 * @param soupengApiAdd the soupengApiAdd to set
	 */
	@Value("#{systemProperties['soupeng.api.add']}")
	public void setSoupengApiAdd(String soupengApiAdd) {
		this.soupengApiAdd = soupengApiAdd;
	}

	/**
	 * @return the soupengApiUpdate
	 */
	public String getSoupengApiUpdate() {
		return soupengApiUpdate;
	}

	/**
	 * @param soupengApiUpdate the soupengApiUpdate to set
	 */
	@Value("#{systemProperties['soupeng.api.update']}")
	public void setSoupengApiUpdate(String soupengApiUpdate) {
		this.soupengApiUpdate = soupengApiUpdate;
	}

	/**
	 * @return the soupengAppkey
	 */
	public String getSoupengAppkey() {
		return soupengAppkey;
	}

	/**
	 * @param soupengAppkey the soupengAppkey to set
	 */
	@Value("#{systemProperties['soupeng.appkey']}")
	public void setSoupengAppkey(String soupengAppkey) {
		this.soupengAppkey = soupengAppkey;
	}

	/**
	 * @return the soupengSid
	 */
	public String getSoupengSid() {
		return soupengSid;
	}

	/**
	 * @param soupengSid the soupengSid to set
	 */
	@Value("#{systemProperties['soupeng.sid']}")
	public void setSoupengSid(String soupengSid) {
		this.soupengSid = soupengSid;
	}
	
	@Value("#{systemProperties['game.cooperationid.sortorder']}")
	public void setGameCooperationModelSortOrder(String order) {
		if(this.gameCoorperationMap == null) {
			this.gameCoorperationMap = new LinkedHashMap<Long, Integer>();
		}
		if(StringUtils.isNotBlank(order)) {
			String[] arrays = StringUtils.split(order, ";");
			for(int i=0;i<arrays.length;i++) {
				if(StringUtils.isNotBlank(arrays[i]) && StringUtils.isNumeric(arrays[i])) {
					this.gameCoorperationMap.put(Long.valueOf(arrays[i]), i);
				}
			}
		}
	}

	public Map<Long, Integer> getGameCooperationModelSortOrder() {
		return this.gameCoorperationMap;
	}

	@Value("#{systemProperties['app.cooperationid.sortorder']}")
	public void setAndroidCooperationModelSortOrder(String order) {
		if(this.androidCoorperationMap == null) {
			this.androidCoorperationMap = new LinkedHashMap<Long, Integer>();
		}
		
		if(StringUtils.isNotBlank(order)) {
			String[] arrays = StringUtils.split(order, ";");
			for(int i=0;i<arrays.length;i++) {
				if(StringUtils.isNotBlank(arrays[i]) && StringUtils.isNumeric(arrays[i])) {
					this.androidCoorperationMap.put(Long.valueOf(arrays[i]), i);
				}
			}
		}
	}
	public Map<Long, Integer> getAndroidCooperationModelSortOrder() {
		return this.androidCoorperationMap;
	}

	public Map<Long, Integer> getIosCooperationModelSortOrder() {
		return new HashMap<Long, Integer>();
	}

	public Map<Long, Integer> getSymbianCooperationModelSortOrder() {
		return this.getAndroidCooperationModelSortOrder();
	}
	
}
