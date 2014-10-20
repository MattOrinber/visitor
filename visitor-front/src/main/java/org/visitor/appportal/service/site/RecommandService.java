package org.visitor.appportal.service.site;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.visitor.appportal.context.AccountContext;
import org.visitor.appportal.domain.HtmlPage;
import org.visitor.appportal.domain.PageContainer;
import org.visitor.appportal.domain.ProductContainer;
import org.visitor.appportal.domain.RecommandContainer;
import org.visitor.appportal.repository.AdvertiseRepository;
import org.visitor.appportal.repository.FolderRepository;
import org.visitor.appportal.repository.HtmlPageRepository;
import org.visitor.appportal.repository.PageContainerRepository;
import org.visitor.appportal.repository.PictureRepository;
import org.visitor.appportal.repository.ProductContainerRepository;
import org.visitor.appportal.repository.ProductListRepository;
import org.visitor.appportal.repository.ProductSiteFolderRepository;
import org.visitor.appportal.repository.RecommandContainerRepository;
import org.visitor.appportal.web.controller.RecommandContainerSearchForm;

public abstract class RecommandService extends SiteService{
    
	@Autowired
    private RecommandContainerRepository recommandContainerRepository;
	@Autowired
	private PictureRepository pictureRepository;
	@Autowired
	private HtmlPageRepository htmlPageRepository;
    @Autowired
    private PageContainerRepository pageContainerRepository;
	@Autowired
	private FolderRepository folderRepository;
    @Autowired
    private ProductListRepository productListRepository;
	@Autowired
	private ProductContainerRepository productContainerRepository;
    @Autowired
    private AdvertiseRepository advertiseRepository;
    @Autowired
    private ProductSiteFolderRepository productSiteFolderRepository;

	/**
	 * 查询指定站点下的所有推荐位
	 * @param rcsf
	 * @param model
	 */
	public void list(RecommandContainerSearchForm rcsf, Model model){
        
    	RecommandContainer rc = rcsf.getRecommandContainer();
    	
	    rc.setSite(getSite());
	    	
	    model.addAttribute("recommandContainersCount", recommandContainerRepository.findCount(rc, rcsf.toSearchTemplate()));
	    model.addAttribute("recommandContainers", recommandContainerRepository.find(rc, rcsf.toSearchTemplate()));
	        
    }
	
	/**
	 * 创建指定站点下的推荐容器
	 * @param recommandContainer
	 * @param bindingResult
	 * @return
	 */
	public ResultEnum create(RecommandContainer recommandContainer, 
    		BindingResult bindingResult){
        
		if (bindingResult.hasErrors()) {
            return ResultEnum.ERROR;
        }
        
		Date cdate = new Date();
		String username = AccountContext.getAccountContext().getUsername();

		recommandContainer.setCreateBy(username);
		recommandContainer.setCreateDate(cdate);
		recommandContainer.setModBy(username);
		recommandContainer.setModDate(cdate);
		recommandContainer.setStatus(0);
		recommandContainer.setSite(getSite());

		if (null != recommandContainer.getPicId()) {
			recommandContainer.setPic(pictureRepository
					.findOne(recommandContainer.getPicId()));
		}
		
		recommandContainerRepository.save(recommandContainer);
		
		return ResultEnum.OK;
    }

	
	/**
	 * 添加推荐容器中的产品，GET
	 * @param productContainer
	 * @param rcId
	 * @param pageId
	 * @param model
	 */
	public void addproductForGet(ProductContainer productContainer, Long rcId,
			Long pageId, Model model) {
		// TODO Auto-generated method stub
		//设置容器
    	productContainer.setContainer(recommandContainerRepository.findOne(rcId));
    	productContainer.setSiteId(getSiteId());
    	
    	//可选的产品类型
	    model.addAttribute("containerTypes", ProductContainer.TypeEnum.values());
	    
	    //如果页面确定，则设置
    	if (pageId != null ) {
    		HtmlPage htmlpage = htmlPageRepository.findOne(pageId);
        	productContainer.setPage(htmlpage);
        	productContainer.setFolder(htmlpage.getFolder());
        	productContainer.setSite(htmlpage.getFolder().getSite());
    	}else{
    		/**不用再选择站点了，但是需要选择频道*/
    		Map<Long, String> map = new LinkedHashMap<Long, String>();
    		List<PageContainer> list = pageContainerRepository.findByContainerIdAndSiteId(rcId, getSiteId());
        	for (PageContainer opt : list) {
    			map.put(opt.getFolderId(), opt.getFolder().getName());
        	}
    		model.addAttribute("folderMap", map);
    	}
    			
	}

	/**
	 * 添加推荐产品的POST请求
	 * @param pc
	 * @param bindingResult
	 * @param model
	 * @return
	 */
	public ResultEnum addproductForPost(ProductContainer productContainer,
			BindingResult bindingResult, Model model,
			String product) {
		// TODO Auto-generated method stub
    	//0. 容器有效性处理
		if(productContainer.getSiteId() != getSiteId().intValue()){
			bindingResult.rejectValue("type", "productContainer.type.invalid", 
    				"该容器不属于当前站点，请选择本站点下的容器进行操作。");
    		//直接返回
    		return ResultEnum.ERROR;
		}
		
    	//1. 产品类型必须选择
    	Integer type = productContainer.getType();    	
    	if(type == null){
    		bindingResult.rejectValue("type", "productContainer.type.null", 
    				"请选择产品类型。");
    		//直接返回
    		return ResultEnum.ERROR;
    	}
    	
    	//2. 验证产品ID的合法性
    	product = StringUtils.trim(product);
    	product = StringUtils.replaceEach(product, new String[]{",", "，", " "}, new String[]{";",";",";"});
    	
		model.addAttribute("product", product);
    	String[] productSize = StringUtils.split(product, ";");
    	
    	List<Long> prdIds = new ArrayList<Long>();
    	
    	StringBuffer sb = new StringBuffer();
		for(String productId : productSize) {
			if(StringUtils.isNotBlank(productId) && StringUtils.isNumeric(productId)) {
				prdIds.add(Long.valueOf(productId));
			} else {
				sb.append(productId).append(" ");
			}
		}
    	if(sb.length() > 0) {
    		bindingResult.rejectValue("productId", "productContainer.productId.invalideNumber", 
    				"产品ID[。" + sb.toString() + "] 不是数字。");
    	}

    	if (bindingResult.hasErrors()) {
			return ResultEnum.ERROR;
    	} 
		
		// 验证产品是否存在（这里的产品是广义的，需要根据类型来确定）
    	sb = new StringBuffer();
    	StringBuffer sb1 = new StringBuffer();
    	
    	Object object=null;
    	List<ProductContainer> pcs = null;
 
    	ProductContainer.TypeEnum pct = ProductContainer.TypeEnum.getInstance(type);
    	Integer siteId = getSiteId();
		for(Long productId : prdIds) {
			
			if(pct == ProductContainer.TypeEnum.Product){
				pcs = productContainerRepository
				.findByPageIdAndContainerIdAndProductIdAndType(
						productContainer.getPageId(), productContainer.getContainerId(), productId, type);
				
				/**需要保证，这个产品必须绑定在当前频道，通过ProductSiteFolder和ProductList来查询*/
				object = this.productSiteFolderRepository.findBySiteIdWithProductList(siteId, productId);
				
			}else if(pct == ProductContainer.TypeEnum.Folder) {
				object = folderRepository.findByFolderIdAndSiteId(productId,siteId);
				pcs = productContainerRepository
				.findByPageIdAndContainerIdAndTfolderIdAndType(
						productContainer.getPageId(), productContainer.getContainerId(), productId, type);
			}else {
				object = advertiseRepository.findByAdvertiseIdAndSiteId(productId,siteId);
				pcs = productContainerRepository
				.findByPageIdAndContainerIdAndAdvertiseIdAndType(
						productContainer.getPageId(), productContainer.getContainerId(), productId, type);
			}
			
			if (object == null) {// 验证产品是否存在
				sb.append(productId).append(";");
			}
			
			if (pcs != null && pcs.size() > 0) {// 验证重复推荐
				sb1.append(productId).append(";");
			}
		}
		
		if (sb.length() > 0) {// 验证产品是否存在
			bindingResult.rejectValue("productId", "productContainer.productId.none", 
					new String[]{sb.toString()}, "产品[" + sb.toString() + "] 不存在，或者不属于当前站点。");
			return ResultEnum.ERROR;
		}
		
		if (sb1.length() > 0) {// 验证重复推荐
			bindingResult.rejectValue("productId", "productContainer.productId.binded", 
					new String[]{sb1.toString()}, "产品[" + sb1.toString() + "] 已经绑定了。");
			return ResultEnum.ERROR;
		}
		
		Date date = new Date();
		productContainer.setCreateDate(date);
		productContainer.setModDate(date);
		productContainer.setModBy(AccountContext.getAccountContext().getAccount().getUsername());
		productContainer.setCreateBy(AccountContext.getAccountContext().getAccount().getUsername());
		productContainer.setContainer(recommandContainerRepository.findOne(productContainer.getContainerId()));
		productContainer.setSite(getSite());
		productContainer.setFolder(folderRepository.findOne(productContainer.getFolderId()));
		productContainer.setPage(htmlPageRepository.findOne(productContainer.getPageId()));
		
		/*找最大的排序值*/
		Long sortOrder = productContainerRepository.findMaxSortOrder(productContainer.getSiteId(), 
				productContainer.getFolderId(), productContainer.getPageId(), productContainer.getContainerId());
		if (sortOrder == null) {
			sortOrder = 0l;
		}

		sortOrder += prdIds.size();
		//依次保存
		for(int i=0 ; i< prdIds.size() ; i++) {

			ProductContainer temp = productContainer.copy();
			Long pid = prdIds.get(i);
			
			if(pct == ProductContainer.TypeEnum.Product){
				temp.setProduct(productListRepository.findOne(pid));
			}else if(pct == ProductContainer.TypeEnum.Folder) {
				temp.setTfolder(folderRepository.findOne(pid));
			}else {
				temp.setAdvertise(advertiseRepository.findOne(pid));
			}
			
			temp.setSortOrder(sortOrder-i);
			productContainerRepository.save(temp);
		} 
		
		return null;
	}
	
	
}
