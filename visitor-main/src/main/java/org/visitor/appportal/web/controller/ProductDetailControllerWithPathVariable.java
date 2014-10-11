/*

 * Template pack-mvc-3:src/main/java/web/controller/controllerwithPathVariable.e.vm.java
 */
package org.visitor.appportal.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import org.visitor.appportal.domain.ProductDetail;
import org.visitor.appportal.repository.ProductDetailRepository;
import org.visitor.appportal.service.site.ProductListService;
import org.visitor.appportal.service.site.SiteService.ResultEnum;
import org.visitor.appportal.web.controller.common.BaseController;
import org.visitor.appportal.web.utils.SiteUtil;

@Controller
@RequestMapping("/domain/productdetail/")
public class ProductDetailControllerWithPathVariable extends BaseController{
	@Autowired
	private ProductDetailRepository productDetailRepository;

	/**
	 * This method is invoked by Spring MVC before the handler methods.
	 * <p>
	 * The path variable is converted by SpringMVC to a ProductList via the
	 * {@link ProductListFormatter}. Before being passed as an argument to the
	 * handler, SpringMVC binds the attributes on the resulting model, then each
	 * handler method may receive the entity, potentially modified, as an
	 * argument.
	 */
	@ModelAttribute
	public ProductDetail getProductDetail(@PathVariable("pk") Long pk) {
		return productDetailRepository.findOne(pk);
	}

	/**
	 * Serves the update form view.
	 */
	@RequestMapping(value = "update/{pk}", method = GET)
	public String update(Model model) {
		return "domain/productdetail/update";
	}

	/**
	 * Performs the update action and redirect to the show view.
	 */
	@RequestMapping(value = "update/{pk}", method = { PUT, POST })
	public String update(@Valid @ModelAttribute ProductDetail productDetail, BindingResult bindingResult, Model model,
			 @RequestParam("picFile") MultipartFile picFile,
			 HttpServletRequest request) {
		
		Integer siteId = SiteUtil.getSiteFromSession(request.getSession());
		if(siteId != null){
			
			ProductListService pls = this.getServiceFactory().getProductListService(siteId);
			ResultEnum re = pls.updateProductDetail(productDetail,bindingResult,picFile);
			
			if(re != ResultEnum.OK){
				return update(model);
			}else {
				return "redirect:/domain/productlist/show/" + productDetail.getProductId();
			}
		} else {
			return "redirect:/login";
		}
	}

	/**
	 * Serves the delete form asking the user if the entity should be really
	 * deleted.
	 */
	@RequestMapping(value = "delete/{pk}", method = GET)
	public String delete(Model model) {
		return "domain/productdetail/delete";
	}

	/**
	 * Performs the delete action and redirect to the search view.
	 */
	@RequestMapping(value = "delete/{pk}", method = { PUT, POST, DELETE })
	public String delete(@ModelAttribute ProductDetail productDetail) {
		
		this.productDetailRepository.delete(productDetail.getPrimaryKey());
		
		return "redirect:/domain/productlist/show/" + productDetail.getProductId();
	}

}