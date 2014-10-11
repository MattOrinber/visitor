/*

 * Template pack-mvc-3:src/main/java/web/controller/controller.e.vm.java
 */
package org.visitor.appportal.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.visitor.appportal.domain.Template;
import org.visitor.appportal.service.site.SiteService.ResultEnum;
import org.visitor.appportal.service.site.TemplateService;
import org.visitor.appportal.web.controller.common.BaseController;
import org.visitor.appportal.web.utils.AutoCompleteResult;
import org.visitor.appportal.web.utils.SearchParameters;
import org.visitor.appportal.web.utils.SiteUtil;

/**
 * 业务逻辑转移到service层
 * @author mengw
 *
 */
@Controller
@RequestMapping("/domain/template/")
public class TemplateController extends BaseController{
    
    @InitBinder
    public void initDataBinder(WebDataBinder binder){
        binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }
    
    /**
     * Performs the list action.
     */
    @RequestMapping(value = { "list", "" })
    public String list(@ModelAttribute TemplateSearchForm templateSearchForm, 
    		HttpServletRequest request , Model model) {
    	
    	TemplateService service = getServiceFromRequest(request);
    	
    	if(service != null) {
	    	service.list(templateSearchForm, model);
    		return "domain/template/list";
    	
    	} else {
    		return "redirect:/login";
    	}
    }

    /**
     * Serves the create form.
     */
    @RequestMapping(value = "create", method = GET)
    public String create(@ModelAttribute Template template, Model model) {
    	model.addAttribute("templateTypes", Template.TypeEnum.values());
    	if(null == template.getType()) {
    		template.setType(Template.TypeEnum.Template.getValue());
    	}
        return "domain/template/create";
    }

    /**
     * Performs the create action and redirect to the show view.
     */
    @RequestMapping(value = "create", method = { POST, PUT })
    public String create(@Valid @ModelAttribute Template template, BindingResult bindingResult, 
    		HttpServletRequest request , Model model) {
        
    	TemplateService service = getServiceFromRequest(request);
        
        if(service != null){
	    	
        	ResultEnum re = service.create(template, bindingResult, model);
	    	
	    	switch(re) {
	    		case ERROR:
	    			return create(template,model);
	    		case OK:
	    			return "redirect:/domain/template/show/" + template.getPrimaryKey();
	    		default:
	    			return "redirect:/login";
	    	}
	    	
        }else {
        	return "redirect:/login";
        }
    }

    /**
     * Serves search by example form, search by pattern form and search by named query form.
     */
    @RequestMapping(value = "*", method = GET)
    public void catchAll(@ModelAttribute TemplateSearchForm templateSearchForm, Model model) {
    	model.addAttribute("templateTypes", Template.TypeEnum.values());
    }
    
    @RequestMapping("/autocomplete")
    @ResponseBody
    public List<AutoCompleteResult> autocomplete(@RequestParam(value = "term", required = false) String searchPattern,
            SearchParameters search, HttpServletRequest request) {
    	
    	TemplateService service = getServiceFromRequest(request);
    	
    	if(service != null) {
    		return service.autocomplete(searchPattern);
    	}
    	
    	return null;
    	
    }
    
    /**
     * 根据session中的siteId获取对应的Service
     * @param request
     * @return
     */
    private TemplateService getServiceFromRequest(HttpServletRequest request){
    	
    	Integer siteId = SiteUtil.getSiteFromSession(request.getSession());
        
        if(siteId != null){
	    	return  getServiceFactory().getTemplateService(siteId);
        }
        
        return null;
    }

}