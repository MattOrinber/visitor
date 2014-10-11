/*


 * Template pack-mvc-3:src/main/java/web/controller/controller.e.vm.java
 */
package org.visitor.appportal.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import org.visitor.appportal.domain.ProductDetail;
import org.visitor.appportal.repository.ProductDetailRepository;
import org.visitor.appportal.repository.ProductListRepository;
import org.visitor.appportal.repository.SiteRepository;
import org.visitor.appportal.service.ProductService;
import org.visitor.appportal.service.site.ProductListService;
import org.visitor.appportal.service.site.SiteService.ResultEnum;
import org.visitor.appportal.web.controller.common.BaseController;
import org.visitor.appportal.web.utils.SiteUtil;

@Controller
@RequestMapping("/domain/productdetail/")
public class ProductDetailController extends BaseController	{
	@Autowired
	private ProductListRepository productListRepository;
	@Autowired
	private ProductDetailRepository productDetailRepository;
	@Autowired
	private ProductService productService;
	@Autowired
	private SiteRepository siteRepository;
	
    @InitBinder
    public void initDataBinder(WebDataBinder binder){
        // you can explicitly allow certain fields or exclude them
        // binder.setAllowedFields(new String[]{"resourceAudio.resourceid", "resourceAudio.resourceName", "resourceAudio.userid", "resourceAudio.status", "resourceAudio.auditStatus", "resourceAudio.uploadBy", "resourceAudio.lastoperBy", "resourceAudio.createDt", "resourceAudio.lastoperDt", "resourceAudio.dlCount", "resourceAudio.author", "resourceAudio.audioProperty", "resourceAudio.recordCorp", "resourceAudio.version", "resourceAudio.language", "resourceAudio.region", "resourceAudio.rhythm", "resourceAudio.musicType", "resourceAudio.musicSuit", "resourceAudio.singer", "resourceAudio.lyricsAuthor", "resourceAudio.albumName", "resourceAudio.wayOperate", "resourceAudio.resourceSize", "resourceAudio.resourceFormat", "resourceAudio.audioSource", "resourceAudio.intro", "resourceAudio.tag", "resourceAudio.ifIndexed", "resourceAudio.fileCount", "resourceAudio.resourceStatus"});
        binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }
    
	/**
	 * Serves the create form.
	 */
	@RequestMapping(value = "create", method = GET)
	public String create(@ModelAttribute ProductDetail productDetail,
		@RequestParam(value = "productId",required = true) Long productId) {
		
		if(productId != null){
			productDetail.setProduct(productListRepository.findOne(productId));
		}else {
			return "redirect:/index";
		}
		
		return "domain/productdetail/create";
	}

	/**
	 * Performs the create action and redirect to the show view.
	 */
	@RequestMapping(value = "create", method = { POST, PUT })
	public String create(@ModelAttribute ProductDetail productDetail, BindingResult bindingResult, 
		HttpServletRequest request, @RequestParam("picFile") MultipartFile picFile) {

		Integer siteId = SiteUtil.getSiteFromSession(request.getSession());
		if(siteId != null){
			
			ProductListService pls = this.getServiceFactory().getProductListService(siteId);
			
			ResultEnum re = pls.createProductDetail(bindingResult,productDetail,picFile);
			
			if (re == ResultEnum.ERROR) {
				return create(productDetail, productDetail.getProductId());
			}else {
				return "redirect:/domain/productlist/show/" + productDetail.getProductId();
			}
		}else {
			return "redirect:/login";
		}
	}

	/**
	 * Serves search by example form, search by pattern form and search by named
	 * query form.
	 */
	@RequestMapping(value = "*", method = GET)
	public void catchAll(@ModelAttribute ProductListSearchForm productListSearchForm, Model model) {
	}

}