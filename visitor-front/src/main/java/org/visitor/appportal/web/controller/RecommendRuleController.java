/*

 * Template pack-mvc-3:src/main/java/web/controller/controller.e.vm.java
 */
package org.visitor.appportal.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.visitor.appportal.domain.ProductDetail;
import org.visitor.appportal.domain.ProductDetail.RecommendStorageEnum;
import org.visitor.appportal.domain.ProductList;
import org.visitor.appportal.domain.ProductSiteFolder;
import org.visitor.appportal.domain.ProductSiteFolderPk;
import org.visitor.appportal.domain.RecommendRule;
import org.visitor.appportal.repository.FolderRepository;
import org.visitor.appportal.repository.ProductDetailRepository;
import org.visitor.appportal.repository.ProductListRepository;
import org.visitor.appportal.repository.ProductSiteFolderRepository;
import org.visitor.appportal.repository.RecommendRuleRepository;
import org.visitor.appportal.repository.SiteRepository;
import org.visitor.appportal.service.ProductService;
import org.visitor.appportal.service.site.RecommendRuleService;
import org.visitor.appportal.service.site.SiteService.ResultEnum;
import org.visitor.appportal.web.controller.common.BaseController;
import org.visitor.appportal.web.utils.SiteUtil;

@Controller
@RequestMapping("/domain/recommendrule/")
public class RecommendRuleController extends BaseController{

    @Autowired
    private RecommendRuleRepository recommendRuleRepository;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private ProductListRepository productListRepository;
    
    @Autowired
    private FolderRepository folderRepository;
    
    @Autowired
    private SiteRepository siteRepository;
    
	@Autowired
	private ProductSiteFolderRepository productSiteFolderRepository;
	
	@Autowired
	private ProductDetailRepository productDetailRepository;
    
    @InitBinder
    public void initDataBinder(WebDataBinder binder){
        // you can explicitly allow certain fields or exclude them
        // binder.setAllowedFields(new String[]{"resourceAudio.resourceid", "resourceAudio.resourceName", "resourceAudio.userid", "resourceAudio.status", "resourceAudio.auditStatus", "resourceAudio.uploadBy", "resourceAudio.lastoperBy", "resourceAudio.createDt", "resourceAudio.lastoperDt", "resourceAudio.dlCount", "resourceAudio.author", "resourceAudio.audioProperty", "resourceAudio.recordCorp", "resourceAudio.version", "resourceAudio.language", "resourceAudio.region", "resourceAudio.rhythm", "resourceAudio.musicType", "resourceAudio.musicSuit", "resourceAudio.singer", "resourceAudio.lyricsAuthor", "resourceAudio.albumName", "resourceAudio.wayOperate", "resourceAudio.resourceSize", "resourceAudio.resourceFormat", "resourceAudio.audioSource", "resourceAudio.intro", "resourceAudio.tag", "resourceAudio.ifIndexed", "resourceAudio.fileCount", "resourceAudio.resourceStatus"});
        binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }

    /**
     * Performs the list action.
     */
    @RequestMapping(value = { "list", "" })
    public String list(@ModelAttribute RecommendRuleSearchForm recommendRuleSearchForm, HttpServletRequest request, Model model) {
    	
    	Integer siteId = SiteUtil.getSiteFromSession(request.getSession());
    	
    	if(siteId != null){
        	
    		RecommendRuleService rs = this.getServiceFactory().getRecommendRuleService(siteId);
    		
    		rs.list(recommendRuleSearchForm,model);
	    			
	        return "domain/recommendrule/list";
	        
    	}else {
    		return "redirect:/login";
    	}
    }

    /**
     * Serves the create form.
     */
    @RequestMapping(value = "create", method = GET)
    public String create(@ModelAttribute RecommendRule recommendRule) {
        return "domain/recommendrule/create";
    }

    /**
     * Performs the create action and redirect to the show view.
     */
    @RequestMapping(value = "create", method = { POST, PUT })
    public String create(@Valid @ModelAttribute RecommendRule recommendRule, BindingResult bindingResult, HttpServletRequest request,
    		@RequestParam(value = "productIds", required = false) String productIds) {
    	
    	Integer siteId = SiteUtil.getSiteFromSession(request.getSession());
		
		if(siteId != null) {
			RecommendRuleService as = this.getServiceFactory().getRecommendRuleService(siteId);
			
			ResultEnum re = as.create(recommendRule,bindingResult,productIds);
			
			switch(re){
				case OK:
					if (StringUtils.isNotEmpty(productIds) && productIds.split(",").length >= 1) {
						return "redirect:/domain/recommendrule/search";
					}
						
				case ERROR:
					return create(recommendRule);
				default:
					return "redirect:/login";
			}
		}else{
			return "redirect:/login";
		}
    }
    
    /**
     * 查看推荐库
     * @param model
     * @param siteId
     * @return
     */
    @RequestMapping(value = { "storage", "" })
    public String storage(Model model, HttpServletRequest request) {
    	
    	Integer siteId = SiteUtil.getSiteFromSession(request.getSession());
    	
    	if (siteId != null) {
    		List<ProductList> productLists = productListRepository.findStorageProductBySiteId(siteId);
    		if (productLists != null && productLists.size() > 0) {
    			model.addAttribute("productLists", productLists);
    		}
    		return "domain/recommendrule/storage";
		}else{
			return "redirect:/login";
		}
    	
    }
    
    /**
     * 添加推荐库产品
     * @param model
     * @param siteId
     * @return
     */
    @RequestMapping(value = { "storage/add", "" })
    public String addstorage(@ModelAttribute RecommendRule recommendRule, HttpServletRequest request,  Model model) {
    	Integer siteId = SiteUtil.getSiteFromSession(request.getSession());
    	if (siteId != null) {
	    	return "domain/recommendrule/storageadd";
		}else{
			return "redirect:/login";
		}
    }
    
    @RequestMapping(value = "storage/add", method = { PUT, POST, DELETE })
    public String addstorage(@Valid @ModelAttribute RecommendRule recommendRule, BindingResult bindingResult, HttpServletRequest request,  Model model,
    		@RequestParam(value = "productIds", required = true)  String productIds) {
    	model.addAttribute("productIds", productIds);
    	Integer siteId = SiteUtil.getSiteFromSession(request.getSession());
    	if (siteId != null) {
	    	//验证
	    	if (StringUtils.isEmpty(productIds)) {
	        	bindingResult.addError(new FieldError("recommendRule", "productId", "必须输入"));
	        	return addstorage(recommendRule,request,model);
	    	} else {
	    		if (!StringUtils.isNumeric((productIds.replaceAll(",", "")))) {
	            	bindingResult.addError(new FieldError("recommendRule", "productId", "请输入数字，多个请用逗号隔开"));
	            	return addstorage(recommendRule,request,model);
	    		}
	    		String[] products = productIds.split(",");
	    		if (products != null && products.length > 0){
	    			List<Long> pss = new ArrayList<Long>();
	    			for (int j=0 ; j<products.length; j++) {
	        			pss.add(Long.valueOf(products[j]));
	    			}
	
	    			List<ProductDetail> productDetail_s = new ArrayList<ProductDetail>();
	           		List<ProductList> productLists = productListRepository.findByProductIds(pss);
	           		if (productLists != null && productLists.size() > 0) {
	               		for (ProductList productList : productLists) {
	        				ProductSiteFolderPk pk = new ProductSiteFolderPk(productList.getProductId(), siteId);
	        				ProductSiteFolder psf = productSiteFolderRepository.findOne(pk);
	        				//匹配验证
	        				if (psf == null) {
	        	            	bindingResult.addError(new FieldError("recommendRule", "productId", "产品 " + productList.getProductId() + " 不存在或不属于该站点"));
	        	            	return addstorage(recommendRule,request,model);
	        				}
	        				
	        				ProductDetail pd = productDetailRepository.findByProductIdAndSiteId(productList.getProductId(), siteId);
	        				//
	        				if (pd == null) {
	        					ProductDetail add_ap = new ProductDetail();
	        					add_ap.setProduct(productList);
	        					add_ap.setSite(psf.getSite());
	        					add_ap.setSortId(0);
	        					add_ap.setRecommendStorage(RecommendStorageEnum.YES.ordinal());
	        					add_ap.setStatus(ProductDetail.ENABLE);
	        					productDetail_s.add(add_ap);
	        				} else {
	        					pd.setRecommendStorage(RecommendStorageEnum.YES.ordinal());
	        					productDetail_s.add(pd.copy());
	        				}
	        				
	        				
	               		}
	           		} else {
		            	bindingResult.addError(new FieldError("recommendRule", "productId", "产品不存在"));
		            	return addstorage(recommendRule,request,model);
	           		}
	           		productDetailRepository.save(productDetail_s);
	    		}
	    	}
	    	return "redirect:/domain/recommendrule/storage/";
		}else{
			return "redirect:/login";
		}
    }
    
    /**
     * 删除推荐库产品
     * @param model
     * @param siteId
     * @param productId
     * @return
     */
    @RequestMapping(value = "storage/delete", method = GET)
    public String deletestorage(Model model, HttpServletRequest request, @RequestParam(value = "productId", required = true)  Long productId) {
    	Integer siteId = SiteUtil.getSiteFromSession(request.getSession());
    	if (siteId != null) {
	    	model.addAttribute("productList", productListRepository.findByProductId(productId));
	    	model.addAttribute("siteId", siteId);
	        return "domain/recommendrule/storagedelete";
		}else{
			return "redirect:/login";
		}
    }
    
    @RequestMapping(value = "storage/delete", method = { PUT, POST, DELETE })
    public String deletestorage(HttpServletRequest request, @RequestParam(value = "productId", required = true)  Long productId) {

    	Integer siteId = SiteUtil.getSiteFromSession(request.getSession());
    	if (siteId != null) {
	    	ProductDetail productDetail = productDetailRepository.findByProductIdAndSiteId(productId, siteId);
	    	productDetail.setRecommendStorage(RecommendStorageEnum.NO.ordinal());
	    	productDetailRepository.save(productDetail);
	    	return "redirect:/domain/recommendrule/storage";
		}else{
			return "redirect:/login";
		}
    }
    
    @RequestMapping(value = "storage/batchdelete", method = { PUT, POST, DELETE })
    public String batchdeletestorage(HttpServletRequest request, @ModelAttribute RecommendRule recommendRule,
    		@RequestParam(value = "products", required = false) List<String> products) {
    	Integer siteId = SiteUtil.getSiteFromSession(request.getSession());
    	if (siteId != null) {
	    	if (products != null && products.size() > 0) {
	    		List<ProductDetail> ProductDetails = new ArrayList<ProductDetail>();
	    		for (String product : products) {
	    			ProductDetail p = productDetailRepository.findByProductIdAndSiteId(Long.valueOf(product), siteId);
	    			p.setRecommendStorage(RecommendStorageEnum.NO.ordinal());
	    			ProductDetails.add(p);
	    		}
	    		productDetailRepository.save(ProductDetails);
	    	}
	
	    	return "redirect:/domain/recommendrule/storage";
		}else{
			return "redirect:/login";
		}
    }

	@RequestMapping("storage/publish")
	public String storagepublish(HttpServletRequest request){
		Integer siteId = SiteUtil.getSiteFromSession(request.getSession());
		if (siteId != null) {
			productService.publishRecmmendStorage(siteId);
			return "domain/common/publish";
		}else{
			return "redirect:/login";
		}
	}
    
    @RequestMapping(value = "batchdelete", method = GET)
	public String batchdelete(HttpServletRequest request, @RequestParam(value="ids") String ids) {
    	Integer siteId = SiteUtil.getSiteFromSession(request.getSession());
    	if (siteId != null) {
			if(null != ids && !ids.trim().equals("")){
	        	String tmp[]=ids.split("_");
	        	for(int i=0;i<tmp.length;i++){
	        		RecommendRule recommendRule = recommendRuleRepository.findByRuleId(Long.parseLong(tmp[i]));
	        		productService.deleteRecommendRule(recommendRule);
	        	}
	    	}
			
			return "redirect:/domain/recommendrule/search";
		}else{
			return "redirect:/login";
		}
	}

    /**
     * Serves search by example form, search by pattern form and search by named query form.
     */
    @RequestMapping(value = "*", method = GET)
    public void catchAll(@ModelAttribute RecommendRuleSearchForm recommendRuleSearchForm) {
    }

}