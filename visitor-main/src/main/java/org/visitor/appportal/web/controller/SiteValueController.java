/*

 * Template pack-mvc-3:src/main/java/web/controller/controller.e.vm.java
 */
package org.visitor.appportal.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import org.visitor.appportal.domain.Category;
import org.visitor.appportal.domain.SiteValue;
import org.visitor.appportal.domain.SiteValue.TypeEnum;
import org.visitor.appportal.redis.SiteRedisRepository;
import org.visitor.appportal.repository.CategoryRepository;
import org.visitor.appportal.repository.SiteRepository;
import org.visitor.appportal.repository.SiteValueRepository;
import org.visitor.appportal.service.SiteFolderService;
import org.visitor.appportal.service.site.CategoryService;
import org.visitor.appportal.web.controller.common.BaseController;

@Controller
@RequestMapping("/domain/sitevalue/")
public class SiteValueController extends BaseController{

    @Autowired
    private SiteRepository siteRepository;
	@Autowired
	private CategoryRepository categoryRepository;
    @Autowired
    private SiteValueRepository siteValueRepository;
    @Autowired
    private SiteFolderService siteFolderService;
    @Autowired
    private SiteRedisRepository siteRedisRepository;
    
    /**
     * Performs the list action.
     */
    @RequestMapping(value = { "list", "" })
    public String list(@ModelAttribute SiteValueSearchForm siteValueSearchForm, Model model) {
    	model.addAttribute("siteValuesCount", siteValueRepository.findCount(siteValueSearchForm.getSiteValue(), siteValueSearchForm.toSearchTemplate()));
        model.addAttribute("siteValues", siteValueRepository.find(siteValueSearchForm.getSiteValue(), siteValueSearchForm.toSearchTemplate()));
        return "domain/sitevalue/list";
    }

    /**
     * Serves the create form.
     */
    @RequestMapping(value = "create", method = GET)
    public String create(@ModelAttribute SiteValue siteValue) {
        return "domain/sitevalue/create";
    }

    /**
     * Performs the create action and redirect to the show view.
     */
    @RequestMapping(value = "create", method = { POST, PUT })
    public String create(@Valid @ModelAttribute SiteValue siteValue, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return create(siteValue);
        }

        siteValueRepository.save(siteValue);
        
        //[@temp]redisRepository.siteAll();
        
        return "redirect:/domain/sitevalue/show/" + siteValue.getPrimaryKey();
    }
    
    /**
     * Serves the list form.
     */
    @RequestMapping(value = "editSiteValue", method = GET)
    public String editSiteValue(@ModelAttribute SiteValueSearchForm siteValueSearchForm, Model model) {
    	SiteValue siteValue = siteValueSearchForm.getSiteValue();
    	
    	model.addAttribute("site", siteRepository.findOne(siteValue.getSiteId()));
        List<Category> sitevalues = siteValueRepository.getSiteValueCategory(siteValue.getSiteId(), siteValue.getType());
        model.addAttribute("siteValuesCount", sitevalues.size());
        
        List<Long> values = new ArrayList<Long>();
        if (sitevalues != null && sitevalues.size() > 0) {
        	for (int i =0;i < sitevalues.size(); i++) {
        		values.add(sitevalues.get(i).getCategoryId());
        	}
        	siteValueSearchForm.setSite_values(values);
        }
        
        CategoryService cs = this.getServiceFactory().getCategoryService(siteValue.getSiteId());
        if (siteValueSearchForm.getSiteValue().getType().compareTo(TypeEnum.OperaVersion.getValue()) == 0) {
        	List<Category> result = cs.getCategoryResult(Category.OPERA_VERSION);
        	//this.getCategoryResult(Category.OPERA_VERSION);
        	model.addAttribute("operaversions", result);//.getCategoryList(Category.OPERA_VERSION, 1l));
        	return "domain/sitevalue/operaversion";
        } else if (siteValueSearchForm.getSiteValue().getType().compareTo(TypeEnum.PlatformVerion.getValue()) == 0) {
        	List<Category> result = cs.getCategoryResult(Category.PLATFORM);
        	if (result != null && result.size() > 0) {
            	for (Category c : result) {
            		c.setChildren(this.categoryRepository.findByParentCategoryId(c.getCategoryId()));
            		for (Category b : c.getChildren()) {
            			b.setChildren(this.categoryRepository.findByParentCategoryId(b.getCategoryId()));
            		}
            	}
        	}
        	model.addAttribute("platforms", result);//.findCategorySelectTree(Category.PLATFORM));
        	return "domain/sitevalue/platform";
        } else if (siteValueSearchForm.getSiteValue().getType().compareTo(TypeEnum.Brand.getValue()) == 0) {
        	List<Category> result = this.getCategoryResult(Category.BRAND);
        	model.addAttribute("brands", result);
        	return "domain/sitevalue/brand";
        } else {
        	List<Category> result = this.getCategoryResult(Category.RESOLUTION);
        	model.addAttribute("resolutions", result);
        	return "domain/sitevalue/resolution";
        }
    }
    
    private List<Category> getCategoryResult(long value) {
    	List<Category> platforms = this.categoryRepository.findByParentCategoryIdAndStatusWithChildren(value, Category.ENABLE);
    	List<Category> result = new ArrayList<Category>();
    	if(null != platforms) {
    		for(Category c : platforms) {
    			if(c.getParentCategoryId() != null && c.getParentCategoryId().longValue() == value) {
    				result.add(c);
    			}
    		}
    	}
		return result;
	}

	/**
     * Serves update site values form.
     */
    @RequestMapping(value = "updateSiteValues", method = { POST, PUT })
    public String updateSiteValues(@ModelAttribute SiteValueSearchForm siteValueSearchForm){
    	SiteValue siteValue = siteValueSearchForm.getSiteValue();
    	siteFolderService.updateSiteValues(siteValue.getSiteId(), siteValue.getType(), siteValueSearchForm.getSite_values());   
    	siteRedisRepository.setSite(siteRepository.findOne(siteValue.getSiteId()));
    	return "redirect:/domain/site/show/" + siteValue.getSiteId();
    }
 
    /**
     * 更新产品的theme属性。
     * @param siteValueSearchForm
     * @return
     */
    @RequestMapping(value = "updateSiteValue", method = { POST, PUT })
    public String updateSiteValue(@ModelAttribute SiteValueSearchForm siteValueSearchForm){
    	SiteValue siteValue = siteValueSearchForm.getSiteValue();
    	List<Long> values = new ArrayList<Long>();
    	values.add(siteValue.getValue());
    	siteFolderService.updateSiteValues(siteValue.getSiteId(), siteValue.getType(), values);   
    	siteRedisRepository.setSite(siteRepository.findOne(siteValue.getSiteId()));
    	return "redirect:/domain/site/show/" + siteValue.getSiteId();
    }    
    /**
     * Serves search by example form, search by pattern form and search by named query form.
     */
    @RequestMapping(value = "*", method = GET)
    public void catchAll(@ModelAttribute SiteValueSearchForm siteValueSearchForm) {
    }

}