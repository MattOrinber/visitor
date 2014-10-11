/**
 * 
 */
package org.visitor.appportal.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.visitor.app.portal.model.Product;
import org.visitor.appportal.domain.Category;
import org.visitor.appportal.domain.ProductFile;
import org.visitor.appportal.domain.ProductList;
import org.visitor.appportal.domain.ProductSiteFolder;
import org.visitor.appportal.redis.CategoryRedisRepository;
import org.visitor.appportal.redis.ObjectMapperWrapper;
import org.visitor.util.SiteEnum;

/**
 * @author mengw
 *
 */
@Service("searchIndexService")
public class SearchIndexService {
	protected static final Logger log = LoggerFactory.getLogger(SearchIndexService.class);
	
	@Autowired
	private AnalysisService analysisService;
	@Resource(name="appUpdateSolrServer")
	private SolrServer appUpdateSolrServer;
	@Resource(name="gameUpdateSolrServer")
	private SolrServer gameUpdateSolrServer;
	@Autowired
	private SystemPreference systemPreference;
	@Autowired
	private ObjectMapperWrapper objectMapperWrapper;
	@Autowired
	private CategoryRedisRepository categoryRedisRepository;

	/**
	 * 
	 */
	public SearchIndexService() {
		// TODO Auto-generated constructor stub
	}

	
	public void createIndex(List<Product> products) {
		List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
		List<SolrInputDocument> docsForGame = new ArrayList<SolrInputDocument>();
		if(null != products) {
			for(Product product : products) {
				log.debug("添加产品索引：" + product.getProductList().getName());
				//这里需要根据Product的所属的站点来决定发布到哪一个Solr中
				if(null != product.getFolders() && product.getProductList() != null) {
					if(product.getProductList().getProductType() == null || 
						product.getProductList().getProductType().intValue() != ProductList.ProductTypeEnum.PageGame.ordinal()) {
						/**根据频道绑定关系来发布产品到不同的Solr*/
						for(ProductSiteFolder folder : product.getFolders()) {
							SiteEnum type = SiteEnum.getInstance(folder.getSiteId());
							SolrInputDocument doc = this.initCommonInfo(product, folder);
							if(type == SiteEnum.GAME){
								Map<Long, Integer> categorySortMap = systemPreference.getGameCooperationModelSortOrder();
								if(null != categorySortMap && categorySortMap.containsKey(product.getProductList().getCooperationModelId())) {
									doc.addField("productSourceOrder", categorySortMap.get(product.getProductList().getCooperationModelId()));//按照cooperationModelId顺序排序
								}								
								/**添加到Game中*/
								docsForGame.add(doc);	
							} else if(type == SiteEnum.ANDROID){
								Map<Long, Integer> categorySortMap = systemPreference.getAndroidCooperationModelSortOrder();
								if(null != categorySortMap && categorySortMap.containsKey(product.getProductList().getCooperationModelId())) {
									doc.addField("productSourceOrder", categorySortMap.get(product.getProductList().getCooperationModelId()));//按照cooperationModelId顺序排序
								}									
								/**添加到Android中*/
								docs.add(doc);
							} else if(type == SiteEnum.SYMBIAN){
								Map<Long, Integer> categorySortMap = systemPreference.getSymbianCooperationModelSortOrder();
								if(null != categorySortMap && categorySortMap.containsKey(product.getProductList().getCooperationModelId())) {
									doc.addField("productSourceOrder", categorySortMap.get(product.getProductList().getCooperationModelId()));//按照cooperationModelId顺序排序
								}									
								/**添加到Symbian中*/
								docs.add(doc);
							}
						}
					}
				}
			}
		}
		
		try {
			
			if(docs.size() > 0) {
				UpdateResponse res = this.appUpdateSolrServer.add(docs);
				appUpdateSolrServer.commit();
				if(log.isDebugEnabled()) {
					log.debug("commit and get repsonse", res.getResponse());
				}
				docs.clear();
			}
			
			if(docsForGame.size() > 0) {
				
				UpdateResponse res = this.gameUpdateSolrServer.add(docsForGame);
				gameUpdateSolrServer.commit();
				if(log.isDebugEnabled()) {
					log.debug("commit and get response for game", res.getResponse());
				}
				docsForGame.clear();
			}
			
		} catch (Exception se) {
			try{
				if(docs.size() > 0){
					appUpdateSolrServer.rollback();
				}
				
				if(docsForGame.size() > 0){
					gameUpdateSolrServer.rollback();
				}
				
			}catch(Exception e2){
				log.error("add index rollback error:", se);
			}
			log.error("add index error:", se);
		}
	}

	/**
	 * 根据产品和其绑定的频道，初始化公用的Solr 信息
	 * @param product
	 * @param folder
	 * @return
	 */
	private SolrInputDocument initCommonInfo(Product product , ProductSiteFolder folder){
		SolrInputDocument doc = new SolrInputDocument();
		
		/**频道绑定关系*/
		doc.addField("productWebsiteId", folder.getSiteId());
		doc.addField("productFolderId", folder.getFolderId());
		doc.addField(getProductSortOrderInForder(folder.getFolderId()), folder.getSortOrder() == null ? 0 : folder.getSortOrder());
		doc.addField("description_pinyin", product.getProductList().getDescription());
		doc.addField("productId", product.getProductList().getProductId());
		doc.addField("name", product.getProductList().getName());
		doc.addField("name_pinyin", product.getProductList().getName());
		String name = StringUtils.remove(product.getProductList().getName(), " ");
		List<String> pinyin = ICUTransformUtils.transliterate(StringUtils.lowerCase(name));
		if(null != pinyin) {
			for(String s :  pinyin) {
				doc.addField("name_pinyin", s);
			}
		}
		doc.addField("description", product.getProductList().getDescription());
		doc.addField("tagLine", product.getProductList().getTagLine());
		doc.addField("label", product.getProductList().getLabel());
		doc.addField("billingTypeId", product.getProductList().getBillingTypeId());
		doc.addField("price", product.getProductList().getPrice());
		doc.addField("publishDate", product.getProductList().getPublishDate());
		doc.addField("productVersion", product.getProductList().getProductVersion());
		doc.addField("merchantId", product.getProductList().getMerchantId());//用来判断是否是广告主产品
		
		doc.addField("modDate", product.getProductList().getModDate());
		doc.addField("createDate", product.getProductList().getCreateDate());
		doc.addField("operationModelId", product.getProductList().getOperationModelId());
		doc.addField("cooperationModelId", product.getProductList().getCooperationModelId());
		doc.addField("productSource", product.getProductList().getSourceId());
//		Integer sortOrder = categoryRedisRepository.getCategorySortOrder(product.getProductList().getSourceId());
//		doc.addField("productSourceOrder", sortOrder);
		doc.addField("starLevel", product.getProductList().getStarLevel());
		doc.addField("miniLab", product.getProductList().getMiniLab());
		//这个如何加权重？
		doc.addField("operatorId", product.getProductList().getOperatorId());
		//发布安全类型
		doc.addField("safeType", product.getProductList().getSafeType());
		//产品文件，需要生成位置信息
		if(null != product.getProductFiles()) {
			Map<String, Boolean> map = new HashMap<String, Boolean>();
			StringBuffer sb = new StringBuffer("[");
			for(ProductFile file : product.getProductFiles()) {
				String point = getFileSuite(file);
				doc.addField("productSuite", point);
				sb.append("(").append(point).append("),");
				map.put(file.getPackageName(), true);
			}
			sb.append("]");
			log.debug("产品支持的平台版本与分辨率：" + sb.toString());
			//产品文件，需要过滤掉下线状态的包
			List<ProductFile> files = product.getProductFiles();
			if(null != files) {
				Iterator<ProductFile> it = files.iterator();
				while(it.hasNext()) {
					ProductFile file = it.next();
					if(!(null != file && file.isValid())) {
						it.remove();
					}
				}
				if(!files.isEmpty()) {
					doc.addField("productFile", objectMapperWrapper.convert2String(files));
				}
			}
			
			for(Map.Entry<String, Boolean> entry : map.entrySet()) {
				if(StringUtils.isNotBlank(entry.getKey())) {
					doc.addField("text", entry.getKey());
				}
			}
		}
		
		//封面图
		if(null != product.getCoverPic()) {
			doc.addField("coverPic", objectMapperWrapper.convert2String(product.getCoverPic()));
		}
		
		//图标
		if(null != product.getIconPic()) {
			doc.addField("iconPic", objectMapperWrapper.convert2String(product.getIconPic()));
		}	
		
		return doc;
	}
	
	/**
	 * 将产品绑定到频道的排序通过dynamicField排序
	 * @param folderId
	 * @return
	 */
	private String getProductSortOrderInForder(Long folderId) {
		return "f_" + folderId + "_i";
	}
	
	/**
	 * 文件适配
	 * @param file
	 * @return
	 */
	private String getFileSuite(ProductFile file) {
		Category version = categoryRedisRepository.getCategoryById(file.getPlatformVersionId());
		Category resolution = categoryRedisRepository.getCategoryById(file.getResolutionId());
		if (version == null) {
			return 0 + "," + 0;
		}else if(null == resolution) {
			return version.getLatitude() + "," + 0;
		} else {
			return version.getLatitude() + "," + (resolution.getLongitude() == null ? 0 : resolution.getLongitude());
		}
	}
	
	public void createIndex(Product product) {
		this.createIndex(Arrays.asList(product));
	}


	public void deleteIndex(List<String> productIds) {
		try {
			UpdateResponse rsp = appUpdateSolrServer.deleteById(productIds);
			appUpdateSolrServer.commit();
			for(String productId : productIds) {
				log.debug("del index ["+productId+"] :"+rsp.getResponse());
			}
			UpdateResponse rsp2 = gameUpdateSolrServer.deleteById(productIds);
			gameUpdateSolrServer.commit();
			for(String productId : productIds) {
				log.debug("del index ["+productId+"] :"+rsp2.getResponse());
			}
		} catch (Exception e) {
			try{
				appUpdateSolrServer.rollback();
				gameUpdateSolrServer.rollback();
			}catch(Exception e2){
				log.error("del index rollback error:",e);
			}
			log.error("del index error:",e);
		} 
	}	
}
