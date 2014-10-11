/*

 * Template pack-mvc-3:src/main/java/web/controller/controller.e.vm.java
 */
package org.visitor.appportal.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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

import org.visitor.appportal.domain.Advertise;
import org.visitor.appportal.domain.Picture;
import org.visitor.appportal.repository.AdvertiseRepository;
import org.visitor.appportal.repository.FolderRepository;
import org.visitor.appportal.repository.PictureRepository;
import org.visitor.appportal.repository.ProductListRepository;
import org.visitor.appportal.service.ProductService;
import org.visitor.appportal.service.site.AdvertiseService;
import org.visitor.appportal.service.site.SiteService.ResultEnum;
import org.visitor.appportal.web.controller.common.BaseController;
import org.visitor.appportal.web.utils.SiteUtil;

@Controller
@RequestMapping("/domain/advertise/")
public class AdvertiseController extends BaseController {

	@Autowired
	private ProductListRepository productListRepository;
	@Autowired
	private FolderRepository folderRepository;
	@Autowired
	private PictureRepository pictureRepository;
    @Autowired
    private AdvertiseRepository advertiseRepository;
    @Autowired
    private ProductService productService;
	@Autowired
	private Properties systemProperties;

	@InitBinder
    public void initDataBinder(WebDataBinder binder){
        binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }

	/**
	 * Serves the create form.
	 */
	@RequestMapping(value = "create", method = GET)
	public String create(@ModelAttribute Advertise advertise,Model model) {
		
    	model.addAttribute("picDomain", systemProperties.getProperty("pic.domain"));
    	List<Picture> waterMarkList = pictureRepository.findByType(Picture.PictureType.watermark.getValue());
    	model.addAttribute("waterMarkList", waterMarkList);
		return "domain/advertise/create";
	}

	/**
	 * Performs the create action and redirect to the show view.
	 */
	@RequestMapping(value = "create", method = { POST, PUT })
	public String create(@Valid @ModelAttribute Advertise advertise, 
		BindingResult bindingResult,
		HttpServletRequest request,
		@RequestParam("pictureSize") Integer pictureSize,
		@RequestParam(value="waterMarkIds", required = false) List<String> waterMarkIds,
		Model model) {

		AdvertiseService service = this.getServiceFromRequest(request);
		
		if(service != null) {
			
			ResultEnum re = service.create(advertise,bindingResult,pictureSize,waterMarkIds,model);
			
			switch(re){
				case OK:
					return "redirect:/domain/advertise/show/" + advertise.getPrimaryKey();
				case ERROR:
					return create(advertise,model);
				default:
					return "redirect:/login";
			}
		}else{
			return "redirect:/login";
		}
		
	}

    
    /**
     * Performs the list action.
     */
    @RequestMapping(value = { "list", "" })
    public String list(@ModelAttribute AdvertiseSearchForm advertiseSearchForm, 
    	HttpServletRequest request, Model model) {
    	
    	AdvertiseService service = this.getServiceFromRequest(request);
    	
    	if(service != null){
    		service.list(advertiseSearchForm,model);
	        return "domain/advertise/list";
    	}else {
    		return "redirect:/login";
    	}
    	
    }

    /**
     * Serves search by example form, search by pattern form and search by named query form.
     * 这就是为什么默认情况也到了search的原因吧
     */
    @RequestMapping(value = "*", method = GET)
    public void catchAll(@ModelAttribute AdvertiseSearchForm advertiseSearchForm) {
    }
    
    public AdvertiseService getServiceFromRequest(HttpServletRequest request){
    	
    	Integer siteId = SiteUtil.getSiteFromSession(request.getSession());
    	
    	if(siteId != null){
        	
    		return  this.getServiceFactory().getAdvertiseService(siteId);
    	}
    	
    	return null;
    }

}