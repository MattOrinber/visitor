/*

 * Template pack-mvc-3:src/main/java/web/controller/controllerwithPathVariable.e.vm.java
 */
package org.visitor.appportal.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.beans.PropertyEditorSupport;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.visitor.appportal.domain.PageContainer;
import org.visitor.appportal.domain.PageContainerPk;
import org.visitor.appportal.domain.PageContainer.ShowTypeEnum;
import org.visitor.appportal.repository.PageContainerRepository;

@Controller
@RequestMapping("/domain/pagecontainer/")
public class PageContainerControllerWithPathVariable {
    @Autowired
    private PageContainerRepository pageContainerRepository;

	
    @InitBinder
    public void initDataBinder(WebDataBinder binder){
        // you can explicitly allow certain fields or exclude them
        // binder.setAllowedFields(new String[]{"resourceAudio.resourceid", "resourceAudio.resourceName", "resourceAudio.userid", "resourceAudio.status", "resourceAudio.auditStatus", "resourceAudio.uploadBy", "resourceAudio.lastoperBy", "resourceAudio.createDt", "resourceAudio.lastoperDt", "resourceAudio.dlCount", "resourceAudio.author", "resourceAudio.audioProperty", "resourceAudio.recordCorp", "resourceAudio.version", "resourceAudio.language", "resourceAudio.region", "resourceAudio.rhythm", "resourceAudio.musicType", "resourceAudio.musicSuit", "resourceAudio.singer", "resourceAudio.lyricsAuthor", "resourceAudio.albumName", "resourceAudio.wayOperate", "resourceAudio.resourceSize", "resourceAudio.resourceFormat", "resourceAudio.audioSource", "resourceAudio.intro", "resourceAudio.tag", "resourceAudio.ifIndexed", "resourceAudio.fileCount", "resourceAudio.resourceStatus"});
        binder.registerCustomEditor(PageContainerPk.class, new PropertyEditorSupport() {  
        	  
            @Override  
            public void setAsText(String text) throws IllegalArgumentException {  
                if (!StringUtils.hasText(text)) {  
                    return;  
                }  
                {  
                    PageContainerPk info = PageContainerPk.fromString(text);//.findById(systemInfoId);  
                    setValue(info);  
                }  
            }  
        });          
    }
    
    /**
     * This method is invoked by Spring MVC before the handler methods.
     * <p>
     * The path variable is converted by SpringMVC to a PageContainer via the {@link PageContainerFormatter}.
     * Before being passed as an argument to the handler, SpringMVC binds the attributes on the resulting model,
     * then each handler method may receive the entity, potentially modified, as an argument.
     */
    @ModelAttribute
    public PageContainer getPageContainer(@PathVariable("pk") PageContainerPk pk) {
        return pageContainerRepository.findOne(pk);
    }

    /**
     * Serves the show view for the entity.
     */
    @RequestMapping("show/{pk}")
    public String show(@ModelAttribute PageContainer pageContainer) {
        return "domain/pagecontainer/show";
    }

    /**
     * Serves the update form view.
     */
    @RequestMapping(value = "update/{pk}", method = GET)
    public String update() {
        return "domain/pagecontainer/update";
    }

    /**
     * Performs the update action and redirect to the show view.
     */
    @RequestMapping(value = "update/{pk}", method = { PUT, POST })
    public String update(@Valid @ModelAttribute PageContainer pageContainer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return update();
        }

        pageContainerRepository.save(pageContainer);
        return "redirect:/domain/pagecontainer/show/" + pageContainer.getPrimaryKey();
    }
    
    /**
     * Serves the delete form asking the user if the entity should be really deleted.
     */
    @RequestMapping(value = "delete/{pk}", method = GET)
    public String delete() {
        return "domain/pagecontainer/delete1";
    }

    /**
     * Performs the delete action and redirect to the search view.
     */
    @RequestMapping(value = "delete/{pk}", method = { PUT, POST, DELETE })
    public String delete(@ModelAttribute PageContainer pageContainer) {
        pageContainerRepository.delete(pageContainer);
        return "redirect:/domain/pagecontainer/search";
    }

    /**
     * Serves the delete form asking the user if the entity should be really deleted.
     */
    @RequestMapping(value = "delete1/{pk}", method = GET)
    public String delete1() {
        return "domain/pagecontainer/delete1";
    }

    /**
     * Performs the delete action and redirect to the search view.
     */
    @RequestMapping(value = "delete1/{pk}", method = { PUT, POST, DELETE })
    public String delete1(@ModelAttribute PageContainer pageContainer) {
        pageContainerRepository.delete(pageContainer);
        return "redirect:/domain/htmlpage/edit/" + pageContainer.getPageId();
    }

    /**
     * Performs the update action and redirect to the show view.
     */
    @RequestMapping(value = "changetype/{pk}", method = GET)
	@ResponseBody
    public Boolean changetype(@ModelAttribute PageContainer pageContainer,
    		@RequestParam(value = "showType") Integer showType) {
        
    	if(showType == null || showType!= ShowTypeEnum.Manual.getValue() && showType != ShowTypeEnum.Auto.getValue()){
    		return false;
    	}else{    		
    		pageContainer.setShowType(showType);
    		pageContainerRepository.save(pageContainer);
            
            return true;
    	}
    }
    
}