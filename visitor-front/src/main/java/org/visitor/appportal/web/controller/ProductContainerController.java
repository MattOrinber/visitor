/*

 * Template pack-mvc-3:src/main/java/web/controller/controller.e.vm.java
 */
package org.visitor.appportal.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.visitor.appportal.domain.ProductContainer;
import org.visitor.appportal.service.site.ProductContainerService;
import org.visitor.appportal.web.controller.common.BaseController;
import org.visitor.appportal.web.utils.SiteUtil;

@Controller
@RequestMapping("/domain/productcontainer/")
public class ProductContainerController extends BaseController{
	
    @InitBinder
    public void initDataBinder(WebDataBinder binder){
        binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }
	
	/**
	 * 通过产品选择容器以添加
	 * @param productContainer
	 * @param productId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "create/{pid}/{type}", method = GET)
	public String create(@ModelAttribute ProductContainer productContainer,//
			@PathVariable(value = "pid") Long productId, //
			@PathVariable(value = "type") Integer type, //
			Model model, HttpServletRequest request) {
		
		Integer currSiteId = SiteUtil.getSiteFromSession(request.getSession());
		if(currSiteId != null){
			
			ProductContainerService apcs = this.getServiceFactory().getProductContainerService(currSiteId);
			apcs.createForGet(productContainer,productId,type,model);
		}
		return "domain/productcontainer/create";
	}

	/**
	 * Performs the create action and redirect to the show view.
	 */
	@RequestMapping(value = "create", method = { POST, PUT })
	public String create(@Valid @ModelAttribute ProductContainer productContainer, BindingResult bindingResult,
			HttpServletRequest request, Model model) {
		
		Integer siteId = SiteUtil.getSiteFromSession(request.getSession());
		
		if(siteId != null) {
			ProductContainerService pcs = this.getServiceFactory().getProductContainerService(siteId);
			if(pcs != null) {
				Long productId = Long.parseLong(request.getParameter("pid"));
				if(productId != null){
					String path = pcs.createForPost(productContainer,bindingResult,model,productId);
					if(path != null) {
						return "redirect:/domain/"+path+"/show/" + productId;
					}else {
						return create(productContainer,productId,productContainer.getType(),model,request);
					}
				}
			}
		}
		
		return "redirect:/login";
	}

	/**
	 * Serves search by example form, search by pattern form and search by named
	 * query form.
	 */
	@RequestMapping(value = "*", method = GET)
	public void catchAll(@ModelAttribute ProductContainerSearchForm productContainerSearchForm) {
	}

}