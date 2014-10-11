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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import org.visitor.appportal.domain.Picture;
import org.visitor.appportal.repository.PictureRepository;
import org.visitor.appportal.repository.SiteRepository;
import org.visitor.appportal.service.SystemPreference;
import org.visitor.appportal.service.site.PictureService;
import org.visitor.appportal.service.site.ServiceFactory;
import org.visitor.appportal.web.utils.AutoCompleteResult;
import org.visitor.appportal.web.utils.SiteUtil;

@Controller
@RequestMapping("/domain/picture/")
public class PictureController {

	@Autowired
	private SystemPreference systemPreference;
    @Autowired
    private PictureRepository pictureRepository;
	@Autowired
    private SiteRepository siteRepository;
	@Autowired
	private ServiceFactory serviceFactory;
    
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
    public String list(@ModelAttribute PictureSearchForm pictureSearchForm, 
    		HttpServletRequest request, Model model) {
    	
    	Integer siteId = SiteUtil.getSiteFromSession(request.getSession());
    	if(siteId != null) {
    		
    		PictureService ps = this.serviceFactory.getPicService(siteId);
    		ps.list(pictureSearchForm, request, model);
	        
	        return "domain/picture/list";
	        
    	}else {
    		
    		return "redirect:/login";
    		
    	}
    }
    
    

    /**
     * Serves the create form.
     */
    @RequestMapping(value = "create", method = GET)
    public String create(@ModelAttribute Picture picture) {
        return "domain/picture/create";
    }

    /**
     * Performs the create action and redirect to the show view.
     */
    @RequestMapping(value = "create", method = { POST, PUT })
    public String create(@Valid @ModelAttribute Picture picture, 
    		BindingResult bindingResult, @RequestParam("picFile") MultipartFile picFile,
    		HttpServletRequest request) {
    	
    	Integer siteId = SiteUtil.getSiteFromSession(request.getSession());
    	
    	if(siteId != null) {
    		
    		PictureService ps = this.serviceFactory.getPicService(siteId);
    		
    		PictureService.ResultEnum re = ps.create(picture, bindingResult, picFile);
    		
    		switch(re){
    			case OK:
    				return "redirect:/domain/picture/show/" + picture.getPrimaryKey();
    			case ERROR:
    				return create(picture);
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
    public void catchAll(@ModelAttribute PictureSearchForm pictureSearchForm) {
    }

    @RequestMapping("autocomplete/{type}")
    @ResponseBody
    public List<AutoCompleteResult> autocomplete(@PathVariable("type") Integer type,
    		@RequestParam(value = "term", required = false) String searchPattern,
    		HttpServletRequest request) {

    	Integer siteId = SiteUtil.getSiteFromSession(request.getSession());
    	if(siteId != null) {
    		
    		PictureService ps = this.serviceFactory.getPicService(siteId);
	        
	        return ps.autocomplete(type, searchPattern);
	        
    	}else {
    		
    		return null;
    		
    	}

    }
}