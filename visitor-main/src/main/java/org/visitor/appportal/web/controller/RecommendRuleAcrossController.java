/*

 * Template pack-mvc-3:src/main/java/web/controller/controller.e.vm.java
 */
package org.visitor.appportal.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.visitor.appportal.context.AccountContext;
import org.visitor.appportal.domain.Folder;
import org.visitor.appportal.domain.ProductList;
import org.visitor.appportal.domain.RecommandContainer;
import org.visitor.appportal.domain.RecommendRuleAcross;
import org.visitor.appportal.repository.FolderRepository;
import org.visitor.appportal.repository.ProductListRepository;
import org.visitor.appportal.repository.RecommandContainerRepository;
import org.visitor.appportal.repository.RecommendRuleAcrossRepository;
import org.visitor.appportal.service.DataReadService;
import org.visitor.appportal.service.ProductService;

@Controller
@RequestMapping("/domain/recommendruleacross/")
public class RecommendRuleAcrossController {
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private ProductListRepository productListRepository;
    
    @Autowired
    private FolderRepository folderRepository;
    
    @Autowired
    private RecommandContainerRepository recommandContainerRepository;
    
    @Autowired
    private RecommendRuleAcrossRepository recommendRuleAcrossRepository;
    
    @Autowired
    private DataReadService dataReadService;
    
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
    public String list(@ModelAttribute RecommendRuleAcrossSearchForm recommendRuleAcrossSearchForm, Model model) {
    	RecommendRuleAcross example = recommendRuleAcrossSearchForm.getRecommendRuleAcross();
		if (example.getRuleId() != null) {
			final RecommendRuleAcross o = recommendRuleAcrossRepository.findByRuleId(example.getRuleId());
			model.addAttribute("recommendRuleAcrosssCount", o != null ? 1 : 0);
			List<RecommendRuleAcross> list = new ArrayList<RecommendRuleAcross>();
			list.add(o);
			model.addAttribute("recommendRuleAcrosss", list);
		} else {
	        model.addAttribute("recommendRuleAcrosssCount", recommendRuleAcrossRepository.findCount(recommendRuleAcrossSearchForm.getRecommendRuleAcross(), recommendRuleAcrossSearchForm.toSearchTemplate()));
	        model.addAttribute("recommendRuleAcrosss", recommendRuleAcrossRepository.find(recommendRuleAcrossSearchForm.getRecommendRuleAcross(), recommendRuleAcrossSearchForm.toSearchTemplate()));
		}
	    return "domain/recommendruleacross/list";
    }

    /**
     * Serves the create form.
     */
    @RequestMapping(value = "create", method = GET)
    public String create(@ModelAttribute RecommendRuleAcross recommendRuleAcross) {
        return "domain/recommendruleacross/create";
    }

    /**
     * Performs the create action and redirect to the show view.
     */
    @RequestMapping(value = "create", method = { POST, PUT })
    public String create(@Valid @ModelAttribute RecommendRuleAcross recommendRuleAcross, BindingResult bindingResult,
    		@RequestParam(value = "productIds", required = false)  String productIds, Model model) {    	
        if (bindingResult.hasErrors()) {
            return create(recommendRuleAcross);
        }
        Date date = new Date();
        
        //校验固定产品ID
        if (recommendRuleAcross.getManualNum() != null && recommendRuleAcross.getManualNum().intValue() > 0) {
        	if (StringUtils.isEmpty(recommendRuleAcross.getManualIds().trim())) {
        		bindingResult.addError(new FieldError("recommendRuleAcross", "manualIds", "请输入固定产品ID"));
        		return create(recommendRuleAcross);
        	} else {
        		if (!StringUtils.isNumeric((recommendRuleAcross.getManualIds().replaceAll(",", "")))) {
                	bindingResult.addError(new FieldError("recommendRuleAcross", "manualIds", "请输入数字，多个请用逗号隔开"));
                	return create(recommendRuleAcross);
        		}
        		String[] m_products = recommendRuleAcross.getManualIds().split(",");
        		for (int i=0 ; i <m_products.length ; i++) {
        			Long m_pid = Long.valueOf(m_products[i]);
        			ProductList m_p = productListRepository.findByProductId(m_pid);
        			if (m_p == null) {
                    	bindingResult.addError(new FieldError("recommendRuleAcross", "manualIds", String.valueOf(m_pid) + " 产品不存在"));
                    	return create(recommendRuleAcross);
        			}
        		}
        	}
        }
        
        //校验频道ID
        if (recommendRuleAcross.getFolderNum() != null && recommendRuleAcross.getFolderNum().intValue() > 0) {
        	if (recommendRuleAcross.getFolderId() == null) {
        		bindingResult.addError(new FieldError("recommendRuleAcross", "folderId", "请输入频道ID"));
        		return create(recommendRuleAcross);
        	} else if (!StringUtils.isNumeric(recommendRuleAcross.getFolderId().toString())) {
            		bindingResult.addError(new FieldError("recommendRuleAcross", "folderId", "请输入正确的频道ID"));
            		return create(recommendRuleAcross);
        	} else {
        		Folder f = folderRepository.findByFolderId(recommendRuleAcross.getFolderId());
        		if (f == null) {
                	bindingResult.addError(new FieldError("recommendRuleAcross", "folderId", "频道不存在"));
                	return create(recommendRuleAcross);
        		}
        	}
        }
        
        //校验容器ID
        if (recommendRuleAcross.getContainerNum() != null && recommendRuleAcross.getContainerNum().intValue() > 0) {
        	if (recommendRuleAcross.getContainerId() == null) {
        		bindingResult.addError(new FieldError("recommendRuleAcross", "containerId", "请输入容器ID"));
        		return create(recommendRuleAcross);
        	} else {
        		RecommandContainer r = recommandContainerRepository.findOne(recommendRuleAcross.getContainerId());
        		if (r == null) {
                	bindingResult.addError(new FieldError("recommendRuleAcross", "containerId", "容器不存在"));
                	return create(recommendRuleAcross);
        		}
        	}
        }
        
        //产品
        if (recommendRuleAcross.getType() == RecommendRuleAcross.TYPE_PRODUCT.intValue()){
        	if (StringUtils.isEmpty(productIds)) {
            	bindingResult.addError(new FieldError("recommendRuleAcross", "serviceProductId", "必须输入"));
            	return create(recommendRuleAcross);
        	} else {
        		if (!StringUtils.isNumeric((productIds.replaceAll(",", "")))) {
                	bindingResult.addError(new FieldError("recommendRuleAcross", "serviceProductId", "请输入数字，多个请用逗号隔开"));
                	return create(recommendRuleAcross);
        		}
        		
        		String[] products = productIds.split(",");
        		if (products != null && products.length > 0) {
        			List<RecommendRuleAcross> rrs = new ArrayList<RecommendRuleAcross>();
        			
            		for (int i=0 ; i <products.length ; i++) {
            			if (!StringUtils.isEmpty(products[i])) {
            				Long pid = Long.valueOf(products[i]);
                			String name = dataReadService.getProductName(pid);
                			
                			RecommendRuleAcross rr = recommendRuleAcross.copy();
                			if (StringUtils.isEmpty(name)) {
                            	bindingResult.addError(new FieldError("recommendRuleAcross", "serviceProductId", String.valueOf(pid) + " 产品不存在"));
                            	return create(recommendRuleAcross);
                			} else {
                				rr.setName(name);
                			}
                			rr.setServiceProductId(pid);
                			rr.setDisSort(recommendRuleAcross.formatDisSort());
                    		RecommendRuleAcross recommendRuleAcross_ = recommendRuleAcrossRepository.
                    			findByServiceIdAndServiceSiteIdAndServiceProductId(rr.getServiceId(), rr.getServiceSiteId(), rr.getServiceProductId());
                    		if (recommendRuleAcross_ != null) {
                            	bindingResult.addError(new FieldError("recommendRuleAcross", "serviceProductId", String.valueOf(rr.getServiceProductId()) + " 产品推荐已存在"));
                            	return create(recommendRuleAcross);
                    		}
                			
                			rr.setCreateDate(date);
                			rr.setCreateBy(AccountContext.getAccountContext().getUsername());
                			rr.setModDate(date);
                			rr.setModBy(AccountContext.getAccountContext().getUsername());
                			rrs.add(rr);
            			}
            		}

            		recommendRuleAcrossRepository.save(rrs);
            		if (rrs.size() == 1) {
            			return "redirect:/domain/recommendruleacross/show/" + rrs.get(0).getPrimaryKey();
            		}
            		return "redirect:/domain/recommendruleacross/search";
        		} else {
                	bindingResult.addError(new FieldError("recommendRuleAcross", "serviceProductId", "必须输入"));
                	return create(recommendRuleAcross);
        		}
        	}
        //频道
        } else {
        	if (recommendRuleAcross.getServiceFolderId() == null) {
            	bindingResult.addError(new FieldError("recommendRuleAcross", "serviceFolderId", "必须输入"));
            	return create(recommendRuleAcross);
        	} else {
        		
    			RecommendRuleAcross recommendRuleAcross_ = recommendRuleAcrossRepository.
    				findByServiceIdAndServiceSiteIdAndServiceFolderId(recommendRuleAcross.getServiceId(), recommendRuleAcross.getServiceSiteId(), recommendRuleAcross.getServiceFolderId());
        		if (recommendRuleAcross_ != null) {
                	bindingResult.addError(new FieldError("recommendRuleAcross", "serviceFolderId", String.valueOf(recommendRuleAcross.getServiceFolderId()) + " 频道推荐已存在"));
                	return create(recommendRuleAcross);
        		}
        		recommendRuleAcross.setDisSort(recommendRuleAcross.formatDisSort());
        		recommendRuleAcross.setCreateDate(date);
        		recommendRuleAcross.setCreateBy(AccountContext.getAccountContext().getUsername());
        		recommendRuleAcross.setModDate(date);
        		recommendRuleAcross.setModBy(AccountContext.getAccountContext().getUsername());
            	recommendRuleAcrossRepository.save(recommendRuleAcross);
            	return "redirect:/domain/recommendruleacross/show/" + recommendRuleAcross.getPrimaryKey();
        	}
        }
    }
        
    @RequestMapping(value = "batchdelete", method = GET)
	public String batchdelete(@RequestParam(value="ids") String ids) {
		
		if(null != ids && !ids.trim().equals("")){
        	String tmp[]=ids.split("_");
        	for(int i=0;i<tmp.length;i++){
        		RecommendRuleAcross recommendRuleAcross = recommendRuleAcrossRepository.findByRuleId(Long.parseLong(tmp[i]));
        		productService.deleteRecommendRuleAcross(recommendRuleAcross);
        	}
    	}
		
		return "redirect:/domain/recommendruleacross/search";
	}
    
	@RequestMapping(value = "siteList/{serviceId}", method = GET)
	@ResponseBody
	public List<Map<String, String>> siteList(@PathVariable("serviceId") Integer serviceId) {
		List<Map<String, String>> out = dataReadService.getRecommendSite(serviceId);
		return out;
	}
    
	@RequestMapping(value = "folderList/{siteId}", method = GET)
	@ResponseBody
	public List<Map<String, String>> folderList(@PathVariable("siteId") Integer siteId) {
		List<Map<String, String>> out = dataReadService.getRecommendSiteFolder(siteId);
		return out;
	}

    /**
     * Serves search by example form, search by pattern form and search by named query form.
     */
    @RequestMapping(value = "*", method = GET)
    public void catchAll(@ModelAttribute RecommendRuleAcrossSearchForm recommendRuleAcrossSearchForm) {
    }

}