/*

 * Template pack-mvc-3:src/main/java/web/controller/controllerwithPathVariable.e.vm.java
 */
package org.visitor.appportal.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.visitor.appportal.domain.Advertise;
import org.visitor.appportal.domain.Picture;
import org.visitor.appportal.domain.ProductContainer;
import org.visitor.appportal.repository.AdvertiseRepository;
import org.visitor.appportal.repository.FolderRepository;
import org.visitor.appportal.repository.PictureRepository;
import org.visitor.appportal.repository.ProductContainerRepository;
import org.visitor.appportal.repository.ProductListRepository;
import org.visitor.appportal.service.ProductService;
import org.visitor.appportal.service.SystemPreference;
import org.visitor.appportal.service.site.AdvertiseService;
import org.visitor.appportal.service.site.SiteService.ResultEnum;
import org.visitor.appportal.web.controller.common.BaseController;
import org.visitor.appportal.web.utils.SiteUtil;

@Controller
@RequestMapping("/domain/advertise/")
public class AdvertiseControllerWithPathVariable extends BaseController {

	@Autowired
	private ProductContainerRepository productContainerRepository;
	@Autowired
	private ProductListRepository productListRepository;
	@Autowired
	private FolderRepository folderRepository;
	@Autowired
	private PictureRepository pictureRepository;
    @Autowired
    private AdvertiseRepository advertiseRepository;
	@Autowired
	private SystemPreference systemPreference;
    @Autowired
    private ProductService productService;

    /**
     * This method is invoked by Spring MVC before the handler methods.
     * <p>
     * The path variable is converted by SpringMVC to a advertise via the {@link advertiseFormatter}.
     * Before being passed as an argument to the handler, SpringMVC binds the attributes on the resulting model,
     * then each handler method may receive the entity, potentially modified, as an argument.
     */
    @ModelAttribute
    public Advertise getAdvertise(@PathVariable("pk") Long pk) {
        return this.advertiseRepository.findOne(pk);
    }

    /**
     * Serves the show view for the entity.
     */
    @RequestMapping("show/{pk}")
    public String show(@ModelAttribute Advertise advertise, Model model,HttpSession session) {
    	
    	Integer siteId = SiteUtil.getSiteFromSession(session);
    	/**不显示非本站点的广告*/
    	if(siteId == null || advertise == null || siteId != advertise.getSiteId()){
    		return "redirect:/domain/advertise/search";
    	}
    	
    	model.addAttribute("picDomain", systemPreference.getPictureDomain());
		// 广告推荐
		List<ProductContainer> list = productContainerRepository.findByAdvertiseId(advertise.getAdvertiseId());
		model.addAttribute("recommandList", list);

        return "domain/advertise/show";
    }

    /**
     * Serves the update form view.
     */
    @RequestMapping(value = "update/{pk}", method = GET)
    public String update(Model model) {
    	model.addAttribute("picDomain", systemPreference.getPictureDomain());
    	List<Picture> waterMarkList = pictureRepository.findByType(Picture.PictureType.watermark.getValue());
    	model.addAttribute("waterMarkList", waterMarkList);
        return "domain/advertise/update";
    }

    /**
     * Performs the update action and redirect to the show view.
     */
    @RequestMapping(value = "update/{pk}", method = { PUT, POST })
    public String update(@Valid @ModelAttribute Advertise advertise, BindingResult bindingResult,
			@RequestParam("pictureSize") Integer pictureSize,
			@RequestParam(value="waterMarkIds", required = false) List<String> waterMarkIds,
			Model model, HttpServletRequest request) {
    	
        if (bindingResult.hasErrors()) {
            return update(model);
        }

        AdvertiseService service = this.getServiceFromRequest(request);
        
        if(service != null){
        	ResultEnum re = service.update(advertise,bindingResult,pictureSize,waterMarkIds,model);
        	if(re == ResultEnum.ERROR){
        		return update(model);
        	}
        }
		
        return "redirect:/domain/advertise/show/" + advertise.getPrimaryKey();
    }

    /**
     * Serves the delete form asking the user if the entity should be really deleted.
     */
    @RequestMapping(value = "delete/{pk}", method = GET)
    public String delete() {
    	
        return "domain/advertise/delete";
    }

    /**
     * Performs the delete action and redirect to the search view.
     */
    @RequestMapping(value = "delete/{pk}", method = { PUT, POST, DELETE })
    public String delete(@ModelAttribute Advertise advertise) {
    	/*逻辑删除而不是物理删除，那么将其状态置1即可*/
    	advertise.setStatus(Advertise.AdvertiseStatusEnum.Disable.ordinal());
        advertiseRepository.save(advertise);
        
        return "redirect:/domain/advertise/create";
    }
    
    
    /**
     * 根据Session中的站点信息来获取相应的service
     * @param request
     * @return
     */
    private AdvertiseService getServiceFromRequest(HttpServletRequest request){
    	
    	Integer siteId = SiteUtil.getSiteFromSession(request.getSession());
    	
    	if(siteId != null){
        	
    		return  this.getServiceFactory().getAdvertiseService(siteId);
    	}
    	
    	return null;
    }
}