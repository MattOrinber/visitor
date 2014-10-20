package org.visitor.appportal.service.site;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import org.visitor.appportal.context.AccountContext;
import org.visitor.appportal.domain.Folder;
import org.visitor.appportal.domain.ProductList;
import org.visitor.appportal.domain.ProductSiteFolder;
import org.visitor.appportal.domain.RecommendRule;
import org.visitor.appportal.repository.FolderRepository;
import org.visitor.appportal.repository.ProductDetailRepository;
import org.visitor.appportal.repository.ProductListRepository;
import org.visitor.appportal.repository.ProductSiteFolderRepository;
import org.visitor.appportal.repository.RecommendRuleRepository;
import org.visitor.appportal.repository.SiteRepository;
import org.visitor.appportal.service.ProductService;
import org.visitor.appportal.web.controller.RecommendRuleSearchForm;

public abstract class RecommendRuleService extends SiteService {
	
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

	public void list(RecommendRuleSearchForm recommendRuleSearchForm, Model model) {
    	RecommendRule example = recommendRuleSearchForm.getRecommendRule();
		if (example.getRuleId() != null) {
			final RecommendRule o = recommendRuleRepository.findByRuleIdAndSiteId(example.getRuleId(),getSiteId());
			model.addAttribute("recommendRulesCount", o != null ? 1 : 0);
			List<RecommendRule> list = new ArrayList<RecommendRule>();
			list.add(o);
			model.addAttribute("recommendRules", list);
		} else {
			example.setSiteId(getSiteId());
	        model.addAttribute("recommendRulesCount", recommendRuleRepository.findCount(recommendRuleSearchForm.getRecommendRule(), recommendRuleSearchForm.toSearchTemplate()));
	        model.addAttribute("recommendRules", recommendRuleRepository.find(recommendRuleSearchForm.getRecommendRule(), recommendRuleSearchForm.toSearchTemplate()));
		}
	}
	
	public ResultEnum create(RecommendRule recommendRule, BindingResult bindingResult, String productIds) {
		
        if (bindingResult.hasErrors()) {
            return ResultEnum.ERROR;
        }
        Date date = new Date();
        
        if (recommendRule.getManualNum() != null && recommendRule.getManualNum().intValue() > 0) {
        	if (StringUtils.isEmpty(recommendRule.getManualIds().trim())) {
        		bindingResult.addError(new FieldError("recommendRule", "manualIds", "请输入固定产品ID"));
        		return ResultEnum.ERROR;
        	} else {
        		if (!StringUtils.isNumeric((recommendRule.getManualIds().replaceAll(",", "")))) {
                	bindingResult.addError(new FieldError("recommendRule", "manualIds", "请输入数字，多个请用逗号隔开"));
                	return ResultEnum.ERROR;
        		}
        		String[] m_products = recommendRule.getManualIds().split(",");
        		for (int i=0 ; i <m_products.length ; i++) {
        			Long m_pid = 0L;
        			try {
        				m_pid = Long.valueOf(m_products[i]);
        			}catch(NumberFormatException e){
        				bindingResult.addError(new FieldError("recommendRule", "manualIds", m_products[i] + " 不是有效的数字"));
                    	return ResultEnum.ERROR;
        			}
        			ProductList m_p = productListRepository.findByPidWithSiteId(getSiteId(),m_pid);
        			if (m_p == null) {
                    	bindingResult.addError(new FieldError("recommendRule", "manualIds", String.valueOf(m_pid) + " 产品不存在或不属于本站点"));
                    	return ResultEnum.ERROR;
        			}
        		}
        	}
        }
        
        //产品
        if (recommendRule.getType() == RecommendRule.TYPE_PRODUCT.intValue()){
        	if (StringUtils.isEmpty(productIds)) {
            	bindingResult.addError(new FieldError("recommendRule", "productId", "必须输入"));
            	return ResultEnum.ERROR;
        	} else {
        		if (!StringUtils.isNumeric((productIds.replaceAll(",", "")))) {
                	bindingResult.addError(new FieldError("recommendRule", "productId", "请输入数字，多个请用逗号隔开"));
                	return ResultEnum.ERROR;
        		}
        		
        		String[] products = productIds.split(",");
        		if (products != null && products.length > 0) {
        			List<RecommendRule> rrs = new ArrayList<RecommendRule>();
        			
            		for (int i=0 ; i <products.length ; i++) {
            			Long pid = 0L;
            			try{
            				pid = Long.valueOf(products[i]);
            			}catch(NumberFormatException e){
            				bindingResult.addError(new FieldError("recommendRule", "productId", products[i] + " 产品不是有效的数字"));
                        	return ResultEnum.ERROR;
            			}
            			
            			RecommendRule rr = recommendRule.copy();
            			rr.setProductId(pid);
            			rr.setDisSort(recommendRule.formatDisSort());
            			ProductList p = productListRepository.findByProductId(pid);
            			if (p == null) {
                        	bindingResult.addError(new FieldError("recommendRule", "productId", String.valueOf(pid) + " 产品不存在"));
                        	return ResultEnum.ERROR;
            			}
                		RecommendRule recommendRule_ = recommendRuleRepository.findByProductIdAndSiteId(p.getProductId(),this.getSiteId());
                		if (recommendRule_ != null) {
                        	bindingResult.addError(new FieldError("recommendRule", "productId", String.valueOf(p.getProductId()) + " 产品推荐已存在"));
                        	return ResultEnum.ERROR;
                		}
                		
                		ProductSiteFolder psf = productSiteFolderRepository.findBySiteIdAndProductId(getSiteId(), pid);
                		if (psf == null) {
                			bindingResult.addError(new FieldError("recommendRule", "productId", String.valueOf(p.getProductId()) + " 产品不属于该站点"));
                        	return ResultEnum.ERROR;
                		}
            			
            			rr.setProduct(p);
            			rr.setCreateDate(date);
            			rr.setCreateBy(AccountContext.getAccountContext().getUsername());
            			rr.setModDate(date);
            			rr.setModBy(AccountContext.getAccountContext().getUsername());
            			rr.setSite(getSite());
            			rrs.add(rr);
            		}

            		recommendRuleRepository.save(rrs);
            		return ResultEnum.OK;
        		} else {
                	bindingResult.addError(new FieldError("recommendRule", "productId", "必须输入"));
                	return ResultEnum.ERROR;
        		}
        	}
        //频道
        } else {
        	if (recommendRule.getFolderId() == null) {
            	bindingResult.addError(new FieldError("recommendRule", "folderId", "必须输入"));
            	return ResultEnum.ERROR;
        	} else {
        		
        		Folder folder = folderRepository.findByFolderId(recommendRule.getFolderId());
    			if (folder == null) {
                	bindingResult.addError(new FieldError("recommendRule", "folderId", String.valueOf(recommendRule.getFolderId()) + " 频道不存在"));
                	return ResultEnum.ERROR;
    			}
        		
        		RecommendRule recommendRule_ = recommendRuleRepository.findByFolderId(recommendRule.getFolderId());
        		if (recommendRule_ != null) {
                	bindingResult.addError(new FieldError("recommendRule", "folderId", String.valueOf(recommendRule.getFolderId()) + " 频道推荐已存在"));
                	return ResultEnum.ERROR;
        		}
        		
        		if (folder.getSiteId() != getSiteId()) {
                	bindingResult.addError(new FieldError("recommendRule", "folderId", String.valueOf(recommendRule.getFolderId()) + " 频道不属于该站点"));
                	return ResultEnum.ERROR;
        		}
        		
        		recommendRule.setDisSort(recommendRule.formatDisSort());
        		recommendRule.setFolder(folder);
        		recommendRule.setCreateDate(date);
        		recommendRule.setCreateBy(AccountContext.getAccountContext().getUsername());
        		recommendRule.setModDate(date);
        		recommendRule.setModBy(AccountContext.getAccountContext().getUsername());
        		recommendRule.setSite(getSite());
            	recommendRuleRepository.save(recommendRule);
            	return ResultEnum.OK;
        	}
        }
	}
	
	public List<ProductList> getStorageProduct() {
		Integer siteId = this.getSiteId();
		List<ProductList> productLists = productListRepository.findStorageProductBySiteId(siteId);
		return productLists;
	}
	
}
