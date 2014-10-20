package org.visitor.appportal.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;

import org.visitor.app.portal.model.Product;
import org.visitor.appportal.domain.Account;
import org.visitor.appportal.domain.Advertise;
import org.visitor.appportal.domain.Folder;
import org.visitor.appportal.domain.HtmlPage;
import org.visitor.appportal.domain.PageContainer;
import org.visitor.appportal.domain.PageContainer.ShowTypeEnum;
import org.visitor.appportal.domain.Picture;
import org.visitor.appportal.domain.ProductAcrossRecommend;
import org.visitor.appportal.domain.ProductContainer;
import org.visitor.appportal.domain.ProductContainerTask;
import org.visitor.appportal.domain.ProductFile;
import org.visitor.appportal.domain.ProductList;
import org.visitor.appportal.domain.ProductSiteFolder;
import org.visitor.appportal.domain.ProductSiteRecommend;
import org.visitor.appportal.domain.RecommandContainer;
import org.visitor.appportal.domain.RecommendRule;
import org.visitor.appportal.redis.AdvertiseRedisRepository;
import org.visitor.appportal.redis.ProductAcrossRecommendRedisRepository;
import org.visitor.appportal.redis.ProductRedisRepository;
import org.visitor.appportal.redis.SiteRedisRepository;
import org.visitor.appportal.repository.AccountRepository;
import org.visitor.appportal.repository.AdvertiseRepository;
import org.visitor.appportal.repository.FolderRepository;
import org.visitor.appportal.repository.HtmlPageRepository;
import org.visitor.appportal.repository.PageContainerRepository;
import org.visitor.appportal.repository.PictureRepository;
import org.visitor.appportal.repository.ProductAcrossRecommendRepository;
import org.visitor.appportal.repository.ProductContainerRepository;
import org.visitor.appportal.repository.ProductContainerTaskRepository;
import org.visitor.appportal.repository.ProductFileRepository;
import org.visitor.appportal.repository.ProductListRepository;
import org.visitor.appportal.repository.ProductSiteFolderRepository;
import org.visitor.appportal.repository.ProductSiteRecommendRepository;
import org.visitor.appportal.repository.RecommandContainerRepository;
import org.visitor.appportal.repository.SiteRepository;
import org.visitor.util.AppStringUtils;

@Service("taskService")
public class TaskService {
	
	@Autowired
	private ProductSiteRecommendRepository productSiteRecommendRepository;
	@Autowired
	private ProductAcrossRecommendRepository productAcrossRecommendRepository;
	@Autowired
	private ProductListRepository productListRepository;
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductRedisRepository productRedisRepository;
	@Autowired
	private ProductAcrossRecommendRedisRepository productAcrossRecommendRedisRepository;
	@Autowired
	private ProductFileRepository productFileRepository;

	//newly added
	@Autowired
	private ProductContainerTaskRepository productContainerTaskRepository;
	@Autowired
	private SiteRedisRepository siteRedisRepository;
	@Autowired
	private AdvertiseRedisRepository advertiseRedisRepository;
	@Autowired
	private AdvertiseRepository advertiseRepository;
	@Autowired
	private FolderRepository folderRepository;
	@Autowired
	private PictureRepository pictureRepository;
    @Autowired
    private PageContainerRepository pageContainerRepository;
    @Autowired
    private RecommandContainerRepository recommandContainerRepository;
    @Autowired
    private HtmlPageRepository htmlPageRepository;
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private AccountRepository accountRepository;
	@Autowired
	private ProductContainerRepository productContainerRepository;
	@Autowired
	private ProductSiteFolderRepository productSiteFolderRepository;

	@PersistenceContext
	private EntityManager em;
	@Autowired
	SystemPreference systemPreference;
    
	private static final Logger logger = LoggerFactory.getLogger(TaskService.class);
	
	/**
	 * 发布智能推荐数据
	 */
	public void publishProductRecommandData(Integer siteId) {
		List<ProductSiteRecommend> productSiteRecommend = productSiteRecommendRepository.findBySiteId(siteId);
		for(ProductSiteRecommend par : productSiteRecommend) {
			RecommendRule rr = productService.getProductRecommendRule(siteId, par.getProductId());
			if (rr != null && par != null) {
				//取得对应算法数据
				Integer behavior_type = rr.getBehaviorId();
				String behavior_str = "";
				switch (behavior_type.intValue()) {
					case 1:
						behavior_str = par.getBehaviors1();
						break;
					case 2:
						behavior_str = par.getBehaviors2();
						break;
					case 3:
						behavior_str = par.getBehaviors3();
						break;
					case 4:
						behavior_str = par.getBehaviors4();
						break;
					case 5:
						behavior_str = par.getBehaviors5();
						break;
				}

				logger.info("[redis] recommend data " + par.getProductId().toString() + " publish finshed!");
				
				productRedisRepository.setProductRecommendBehaviors(siteId, par.getProductId(), AppStringUtils.formatStringArrayToLongList(behavior_str, ","));
				
				productRedisRepository.setProductRecommendSimilars(siteId, par.getProductId(), AppStringUtils.formatStringArrayToLongList(par.getSimilars(), ","));
			}
		}
	}
	
	/**
	 * 发布交叉推荐数据
	 */
	public void publishProductRecommandAcrossData() {
		Iterator<ProductAcrossRecommend> productAcrossRecommends = productAcrossRecommendRepository.findAll().iterator();
		while(productAcrossRecommends.hasNext()) {
			ProductAcrossRecommend par = productAcrossRecommends.next();
			
			productAcrossRecommendRedisRepository.setProductAcrossRecommendIds(par, null);
			
			logger.info("[redis] recommend across data " + par.getProductId().toString() + " publish finshed!");
		}
	}
	
	/**
	 * 发布几天内的新品
	 * 
	 * @param days
	 */
	public void publishNewProductProductForDays(int days) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, days);
		List<ProductList> productLists = productListRepository.findByCreateDateGreaterThan(cal.getTime());
		if (!productLists.isEmpty()) {
			for (ProductList productList : productLists) {
				productService.setProduct2Redis(productList);
				logger.info("[task] publis new product for 2 days " + productList.getProductId().toString() + " finshed!");
			}
		}		
	}
	
	public void publishNewProductProductSafeType() {
    	this.autoDownUnsafeProduct();
    	this.publishNewProductProductForDays(-3);
	}
	
	/**
	 * 病毒产品自动下架
	 */
	public void autoDownUnsafeProduct() {
		
		//找出 风险&病毒 产品
		List<ProductList> productLists = productListRepository.findBySafeType(ProductList.SafeTypeEnum.Unsafe.ordinal());
		
		//下架
		if (productLists != null && productLists.size() > 0) {
			for (ProductList productList : productLists) {
				if (productList.getDownStatus() != null && productList.getDownStatus().intValue() == ProductList.ENABLE) {
					
					//需要判断该产品所包含的文件是否需要下架
					boolean needDown = false;
					List<ProductFile> productFiles = this.productFileRepository.findFileByProductId(productList.getPrimaryKey());
					
					if(productFiles !=null && productFiles.size()>0){
						for(ProductFile pf:productFiles){
							//下架条件
							//由于这是自动下架，所以首先该产品应该是被自动扫描过
							//其次，该产品扫描的结果不是安全，也不是未知
							if(pf.getAutoScan() == ProductFile.AUTOSCAN_OPEN.intValue()
								 && pf.getSafeType() != ProductFile.SafeTypeEnum.Safe.ordinal()
								 && pf.getSafeType() != ProductFile.SafeTypeEnum.Unknown.ordinal()){

								needDown = true;
								break;
							}
						}
					}
					
					if(needDown){//需要下架
						productList.setDownStatus(ProductList.DISNABLE);
						productList.setDownReason("unsafe product");
						
						this.productListRepository.save(productList);
						productService.setProduct2Redis(productList);
						logger.info("[task] down unsafe product " + productList.getProductId().toString() + " finshed!");
					}
				}
			}
		}
	}
	
	/**
	 * 定时发布当天的定时产品到相应的容器
	 * 2013－04－27：功能上需要做一定修改
	 * 如果当天没有定时任务，则将redis中的内容重置为运营库中的内容
	 * 另外对本方法进行Code-Review，确保没有其它问题
	 */
	public void autoPublishRecommandProductsByDay(){
		logger.info("auto publish starting----");
		//1. 取得当天的定时产品列表，
        /**1.1 生成排序规则，按推荐容器ID升序，同一推荐容器按倒序排列*/
		List<Order> list = new ArrayList<Order>();
    	list.add(new Order(Direction.ASC, "containerId"));
    	list.add(new Order(Direction.DESC, "sortOrder"));
    	Sort sort = new Sort(list);
    	
    	/**1.2 取得今天的日期，并转化为整数，形如20130427*/
    	Calendar cal = Calendar.getInstance();
    	SimpleDateFormat day_df = new SimpleDateFormat("yyyyMMdd");
    	String today = day_df.format(cal.getTime());
		Integer currDate = Integer.parseInt(today);	
		
		/**1.3 取得满足条件的定时数据*/
		List<ProductContainerTask> pctList = this.productContainerTaskRepository.findByDisplayDate(currDate, sort);
		
		/**2. 将结果转化为一个Map对象，其中，Key为容器ID，值为对应的当天的定时产品列表，*/
		//保持倒序排列
		//由于结果是有序的，我们有理由相信相同推荐容器的ID的产品列表，是排列在一起的
	    Map<Long, List<ProductContainerTask>> containerListMap = new LinkedHashMap<Long, List<ProductContainerTask>>();
	    int i = 0;
	    
	    while(i< pctList.size()){
	    	ProductContainerTask temp = pctList.get(i);
	    	
	    	List<ProductContainerTask> listInMap = containerListMap.get(temp.getContainerId());
	    	
	    	if(listInMap == null){
	    		/**如果为Null，则放置一个新的List到Map*/
	    		listInMap = new ArrayList<ProductContainerTask>();
	    		containerListMap.put(temp.getContainerId(), listInMap);
	    	}
	    	
	    	containerListMap.get(temp.getContainerId()).add(temp);
	    	
	    	i++;
	    }
	    
		/**3. 遍历Map，针对其每个容器ID，找出与之关联的页面ID列表，并进行发布处理*/
	    Iterator<Entry<Long, List<ProductContainerTask>>> iter = containerListMap.entrySet().iterator();
	    Sort sort2 = new Sort(new Order(Direction.DESC, "sortOrder"));
	   
	    while (iter.hasNext()){
	    	Map.Entry<Long, List<ProductContainerTask>> entry = iter.next();
	    	Long containerId = entry.getKey();
	    	
	    	List<PageContainer> pages = pageContainerRepository.findByContainerId(containerId);
	    	
	    	//4. 遍历页面ID列表，如果该页面类型设置为显示定时产品，则用定时产品列表进行发布操作，否则略过
	    	List<ProductContainerTask> smallPctList = entry.getValue();
	    	
	    	if(pages!=null && pages.size() > 0){
		    	for(PageContainer page : pages){
		    		List<ProductContainer> pcList = null;
		    		HtmlPage htmlPage = this.htmlPageRepository.findOne(page.getPageId());
		    		
		    		if(page.getShowType() == ShowTypeEnum.Auto.getValue().intValue()){
		    			pcList = productService.getPcListFromTaskList(smallPctList,htmlPage);
		    		}else {
		    			//把当前运营设置的信息重新发布一次
					    /*在这里，我们只取和当前页面相关的推荐信息*/
						pcList = productContainerRepository.findByPageIdAndContainerId(page.getPageId(), containerId, sort2);
		    		}
		    		
		    		publishToRedisByListAndPage(pcList,htmlPage,containerId);
		    	}
	    	}
	    }//while
	    
	    /**对于那些在定时库，但定时日期不在当天的推荐容器，我们也需要进行发布处理
	     * 更深一层的限制条件是，找出这些推荐容器后，还需要到PageContainer中找其关联的页面是否设置了自动发布，但这样找是没有意义的
	     * 1.首先是一般情况下，出现在定时库中的推荐容器，相应的PageContainer中都会设置自动发布
	     * 2.其次，即使设置为了运营发布，也不代表之前没有设置过自动发布，上一状态是不可知的
	     * 所以以下的逻辑是，找出所有定时库中，定时时间不在当天的推荐容器，并将他们所关联的运营产品重新发布一次
	     * */
	    publishFormalProductInfoToRedis(containerListMap.keySet(),sort2);
	    
	    logger.info("auto publish finished!");
	}
	
	/**
	 * 找出定时定时不在当天的所有定时库中的容器，并将其原来设置的产品信息重新发布一次
	 * @param ids 当天有定时的容器Id Set
	 */
	public void publishFormalProductInfoToRedis(Set<Long> ids ,Sort sort){
		
		if(ids.isEmpty()){
			ids = new HashSet<Long>();//这是一个问题
			ids.add(0L);/**作为搜索条件的set,不能为空集*/
		}
		/**1.首先我们需要找出满足条件的容器ID，由于我们并不需要推荐库中的数据，所以容器ID即可，并且去重*/
		List<Long> recommandIds = this.productContainerTaskRepository.findRecommandIdsNotToday(ids);
		
		if(recommandIds != null && recommandIds.size() > 0){
				
				for(Long recommandId : recommandIds){
						/**2. 找到该容器对应的页面*/
						List<PageContainer> pages = pageContainerRepository.findByContainerId(recommandId);
				    	
				    	if(pages!=null && pages.size() > 0){
						    	for(PageContainer page : pages){
						    			/**3. 这里我们不区分该页面是否设置为定时或运营，反正都需要发布的*/
						    			HtmlPage htmlPage = this.htmlPageRepository.findOne(page.getPageId());
						    			/**4. 把当前运营设置的信息重新发布一次*/
									    List<ProductContainer> pcList = productContainerRepository.findByPageIdAndContainerId(page.getPageId(), recommandId, sort);

									    publishToRedisByListAndPage(pcList,htmlPage,recommandId);
						    	}
				    	}
				}
		}
	}
	

	
	/**
	 * 执行具体的将ProductContainer列表发布到Redis中的操作
	 * @param smallPctList
	 * @param page
	 */
	private void publishToRedisByListAndPage(List<ProductContainer> pcList,HtmlPage page,Long recommandContainerId){
		
		productRedisRepository.clearItem(page, recommandContainerId);						
		productRedisRepository.deleteProductIdInContainer(page.getSiteId(), recommandContainerId);
		
		//只要推荐容器准备好了，按原来的方式发布即可
		for(ProductContainer pc : pcList) {
			//1.第一步，发布推荐信息，也是最主要的一步
			productRedisRepository.setProductRecommand(pc, pc.getSortOrder());
			
			//2.对推荐容器中的元素进行检查，如果没有发布，则重新发布一次
			if(pc.getType().intValue() == ProductContainer.TypeEnum.Product.getValue()){
				
				//2.1 从Redis中查找
				Product product = productRedisRepository.getProductByProductId(pc.getProductId(), false);
				//2.2 如果Redis中没有，则该产品应该发布，则再发布
				if(null == product) {
					ProductList temp = this.productListRepository.findByProductId(pc.getProductId());
					if(null != temp && temp.isEnable()) {//只发布有效产品。
						this.productService.setProduct2Redis(temp);
					}
				}
			}else if(pc.getType().intValue() == ProductContainer.TypeEnum.Folder.getValue()){
				Folder folder = siteRedisRepository.getFolderById(pc.getTfolderId());
				if(null == folder) {
					this.folder(pc.getTfolderId());// this.productListRepository.findOne(pc.getProductId()));
				}
			}else {
				Advertise advertise = advertiseRedisRepository.getAdvertiseById(pc.getAdvertiseId());
				if(null == advertise) {
					Advertise temp = this.advertiseRepository.findOne(pc.getAdvertiseId());
					if(null != temp && temp.isEnable()) {
						//advertiseRedisRepository.setAdvertise(temp);
						//重新执行一次发广告的操作
						this.advertise(pc.getAdvertiseId());
					}
				}
			}		
		}
	}

    /**
     * 发布频道信息
     * @param folderId
     */
    public void folder(Long folderId) {
		Folder folder = folderRepository.findOne(folderId);
		
		//只发布可用的频道信息
		if(folder.getStatus() != null && folder.getStatus().intValue() == 0) {
			siteRedisRepository.setFolder(folder);
			List<Folder> folders = folderRepository.findFolderBread(folder.getFolderId());
			List<Long> folderIds = new ArrayList<Long>();
			for (Folder f : folders) {
				folderIds.add(f.getFolderId());
			}
			siteRedisRepository.setFolderAncestors(folder.getFolderId(), folderIds);
			
			if (folder.getParentFolder() != null) {
				siteRedisRepository.setFolderChildren(folder.getSiteId(), folder.getParentFolderId(),
						folder.getFolderId(), folder.getSortOrder().getTime());
			}
		} else {
			siteRedisRepository.deleteFolder(folder);
			siteRedisRepository.deleteFolderAncestors(folder.getFolderId());
			if (folder.getParentFolder() != null) {
				siteRedisRepository.deleteFolderChildren(folder.getSiteId(), folder.getParentFolderId(),
						folder.getFolderId());
			}
		}
		        
        List<String> ids = new ArrayList<String>();
        ids.add(String.valueOf(folderId));
		this.siteRedisRepository.sendCacheElementMessage(Folder.class.getName(), ids);
    }

    public void advertise(Long advertiseId) {
		
		Advertise ad = advertiseRepository.findOne(advertiseId);
		/**
		 * 设置和广告相关的图片
		 * 这里我们直接使用在SiteRedisRepository中的setPicture方法
		 */
		siteRedisRepository.setPicture(pictureRepository.findOne(ad.getPictureId()).copy());
		
		/*将指定产品存入到redis中*/
		advertiseRedisRepository.setAdvertise(ad);
		
		
		/**
		 * 根据广告类型来决定是否发布对应的产品和频道
		 */
		int adType = ad.getType();
		if(adType == Advertise.AdvertiseTypeEnum.Folder.ordinal()){
			this.folder(ad.getFolderId());
			logger.info("[redis] advertise/folder/" + advertiseId + " publish finshed!");
		}else if(adType == Advertise.AdvertiseTypeEnum.Product.ordinal()){
			productService.setProduct2Redis(productListRepository.findByProductId(ad.getProductId()));
			logger.info("[redis] advertise/product/" + advertiseId + " publish finshed!");
		}
		
		//清除广告的缓存
		List<String> ids = new ArrayList<String>();
		ids.add(String.valueOf(advertiseId));
		this.siteRedisRepository.sendCacheElementMessage(Advertise.class.getName(), ids);

		List<String> picIds = new ArrayList<String>();
		picIds.add(String.valueOf(ad.getPictureId()));
		siteRedisRepository.sendCacheElementMessage(Picture.class.getName(), picIds);
		
		/*记录日志*/
		logger.info("[redis] advertise/" + advertiseId + " publish finshed!");
		logger.info("[redis] picture/" + ad.getPictureId() + " publish finshed!");
    }

    public void autoPublishRecommandProductsByWeek(){
    	String containerIds = this.systemPreference.getContainerIds();
    	String categoryIds = this.systemPreference.getCategoryIds();
    	
    	if(containerIds!=null && categoryIds != null){
    		String ctIds[] = containerIds.split("/");
    		String caIds[] = categoryIds.split("/");
    		
        	for(int i = 0;i< 2;i++){
        		
        		//周排行
        		autoPublishRecommandProductsByWeek(Long.parseLong(ctIds[i]),Long.parseLong(caIds[i]),"weekDl");
        		//总排行
        		autoPublishRecommandProductsByWeek(Long.parseLong(ctIds[i+2]),Long.parseLong(caIds[i]),"totalDl");
        	}
   		
    	}
    }
    
	/**
	 * 按周下载或者是月下载发布排行数据到定时表
	 */
	public void autoPublishRecommandProductsByWeek(Long containerId, Long categoryId,String orderBy) {
		// TODO Auto-generated method stub
		List<Long> productIds = getRankList(categoryId,orderBy);
		
		if(productIds != null && productIds.size() > 0){
			SimpleDateFormat day_df = new SimpleDateFormat("yyyyMMdd");
			
			Date date = new Date();
	    	String today = day_df.format(date);
			Integer currDate = Integer.parseInt(today);	
			RecommandContainer rc = recommandContainerRepository.findOne(containerId);
			
			Long size = productIds.size() + 0L;
			
			for(Long productId:productIds){
				//System.out.println(productId);
				
				if(this.productContainerTaskRepository.findByContainerIdAndDisplayDateAndProductId(containerId, currDate, productId)!=null){
					continue;//已经保存过的就不再保存了
				}
				
				ProductContainerTask pct = new ProductContainerTask();
				pct.setContainer(rc);
				pct.setDisplayDate(currDate);
				pct.setType(1);
				pct.setProduct(this.productListRepository.findByProductId(productId));
				pct.setCreateBy("auto_script");
				pct.setCreateDate(date);
				pct.setModBy("auto_script");
				pct.setModDate(date);
				
				pct.setSortOrder(size--);
				
				this.productContainerTaskRepository.save(pct);
				
			}
		}
	}

	private List<Long> getRankList(Long categoryId,String orderBy){
		
		TypedQuery<Long> query = em.createQuery("select ps.productId from ProductState ps, ProductList pl ,Category ca " +
				" where ps.productId = pl.productId and pl.categoryId = ca.categoryId" +
				" and ca.parentCategoryId = :parentCategoryId and pl.downStatus = 0 order by ps."+orderBy+" desc ", Long.class);
		
		query.setParameter("parentCategoryId", categoryId);
		query.setFirstResult(0);
		query.setMaxResults(50);
		
		return query.getResultList();
	}

	public void changeAllPwd() {
		// TODO Auto-generated method stub
		Md5PasswordEncoder md5 = new Md5PasswordEncoder();
		
		Iterable<Account> accounts = this.accountRepository.findAll();
		
		for(Account account : accounts){
			if(account.getPassword().equals(account.getUsername())){
				account.setPassword(md5.encodePassword(account.getPassword(),null));
				this.accountRepository.save(account);
			}
		}
	}
	
	public void recommandContainer(Long recommandContainerId, Long pageId, String withProduct) {
		RecommandContainer container = recommandContainerRepository.findOne(recommandContainerId);
		if(null != container && null != container.getPrimaryKey()) {
			//发布容器本身
			productRedisRepository.setRecommandContainer(container);			
			//发布容器中的产品
			if(StringUtils.equalsIgnoreCase(withProduct, "withProduct")) {
				
				//按理说，在这里应该清理一下该推荐位的的旧元素，以更新
				HtmlPage page = htmlPageRepository.findOne(pageId);
				if (page != null) {
					productRedisRepository.clearItem(page, recommandContainerId);
					
					List<Order> list = new ArrayList<Order>();
				    list.add(new Order(Direction.DESC, "sortOrder"));
				    Sort sort = new Sort(list);
									
				    /*在这里，我们只取和当前页面相关的推荐信息*/
					List<ProductContainer> pcList = productContainerRepository.findByPageIdAndContainerId(pageId, recommandContainerId, sort);

					productRedisRepository.deleteProductIdInContainer(page.getSiteId(), recommandContainerId);
					
					/*以下为发布过程，和原来保持一致*/
					if(null != pcList) {
						for(ProductContainer pc : pcList) {
							productRedisRepository.setProductRecommand(pc, pc.getSortOrder());
							//产品本身还没有发布，需要重新发布产品。实际这是不应该的事情。
							//2011-12-30: 由于产品容器中可能还包括频道和广告，所以这里需要做进一步的调整，不能一直发布产品
							if(pc.getType() == ProductContainer.TypeEnum.Product.getValue()){
								
								List<ProductSiteFolder> psfs = productSiteFolderRepository.findProductSiteFolders(pc.getProductId());
								if (psfs != null && psfs.size()> 0) {
									for (ProductSiteFolder psf : psfs) {
										List<String> match_res = productService.matchVersionLevel(psf);
										if (match_res != null && match_res.size() > 0) {
											for (String match : match_res) {
												Long versionId = Long.valueOf(match.split(" ")[0]);
												Integer level = Integer.valueOf(match.split(" ")[1]);
												productRedisRepository.setProductIdsInContainer(
														psf.getSiteId(),
														pc.getContainerId(),
														pc,
														versionId, level);
											}
										}
									}
								}
								
								Product product = productRedisRepository.getProductByProductId(pc.getProductId(), false);
								if(null == product) {
									//对于已经下架的产品，不需要再次发布产品了。
									ProductList temp = this.productListRepository.findByProductId(pc.getProductId());
									if(null != temp && temp.isEnable()) {
										this.productService.setProduct2Redis(temp);
										//更新容器自身产品
									}
								}
							}else if(pc.getType() == ProductContainer.TypeEnum.Folder.getValue()){
								Folder folder = siteRedisRepository.getFolderById(pc.getTfolderId());
								if(null == folder) {
									this.folder(pc.getTfolderId());// this.productListRepository.findOne(pc.getProductId()));
								}
							}else {
								Advertise advertise = advertiseRedisRepository.getAdvertiseById(pc.getAdvertiseId());
								if(null == advertise) {
									Advertise temp = this.advertiseRepository.findOne(pc.getAdvertiseId());
									if(null != temp && temp.isEnable()) {
										//advertiseRedisRepository.setAdvertise(temp);
										//重新执行一次发广告的操作
										this.advertise(pc.getAdvertiseId());
									}
								}
							}						
						}
					}
				}
			}
		}
	}
}
