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
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.visitor.appportal.domain.HtmlPage;
import org.visitor.appportal.domain.Template;
import org.visitor.appportal.repository.HtmlPageRepository;
import org.visitor.appportal.repository.TemplateRepository;
import org.visitor.appportal.service.site.TemplateService;
import org.visitor.appportal.web.controller.common.BaseController;
import org.visitor.appportal.web.utils.SiteUtil;

@Controller
@RequestMapping("/domain/template/")
public class TemplateControllerWithPathVariable extends BaseController{
	
    @Autowired
    private HtmlPageRepository htmlPageRepository;
    @Autowired
    private TemplateRepository templateRepository;


    /**
     * This method is invoked by Spring MVC before the handler methods.
     * <p>
     * The path variable is converted by SpringMVC to a Template via the {@link TemplateFormatter}.
     * Before being passed as an argument to the handler, SpringMVC binds the attributes on the resulting model,
     * then each handler method may receive the entity, potentially modified, as an argument.
     */
    @ModelAttribute
    public Template getTemplate(@PathVariable("pk") Long pk) {
        return templateRepository.findOne(Long.valueOf(pk));
    }

    /**
     * Serves the show view for the entity.
     */
    @RequestMapping("show/{pk}")
    public String show(@ModelAttribute Template template) {
        return "domain/template/show";
    }

    /**
     * Serves the update form view.
     */
    @RequestMapping(value = "update/{pk}", method = GET)
    public String update() {
        return "domain/template/update";
    }

    /**
     * Performs the update action and redirect to the show view.
     */
    @RequestMapping(value = "update/{pk}", method = { PUT, POST })
    public String update(@Valid @ModelAttribute Template template, BindingResult bindingResult,
    		HttpServletRequest request) {
        
    	if (bindingResult.hasErrors()) {
            return update();
        }
    	
    	/**业务逻辑*/
    	TemplateService service = this.getServiceFromRequest(request);
    	if(service != null){
    		service.update(template,bindingResult);
    	}
    	
        return "redirect:/domain/template/show/" + template.getPrimaryKey();
    }

    /**
     * Serves the delete form asking the user if the entity should be really deleted.
     */
    @RequestMapping(value = "delete/{pk}", method = GET)
    public String delete(@ModelAttribute Template template, Model model) {
    	List<HtmlPage> hps = htmlPageRepository.findByTemplateId(template.getTemplateId());
    	model.addAttribute("htmlPages", hps);
        return "domain/template/delete";
    }

    /**
     * Performs the delete action and redirect to the search view.
     */
    @RequestMapping(value = "delete/{pk}", method = { PUT, POST, DELETE })
    public String delete(@ModelAttribute Template template) {
        templateRepository.delete(template);
        return "redirect:/domain/template/search";
    }
    
    @RequestMapping(value = "edit/{pk}", method = GET)
    public String edit() {
    	return "domain/template/edit";
    }
        
    /**
     * CMS edit template
     */
    @RequestMapping(value = "edit/{pk}", method = { POST, PUT })
    public String edit(@ModelAttribute Template template, HttpServletRequest request) {
    	
    	TemplateService service = this.getServiceFromRequest(request);
    	
    	if(service != null){
    		
    		service.edit(template);
    		
    	}
    	return "redirect:/domain/template/edit/" + template.getPrimaryKey();
    }
    
    /**
     * publish template
     */
    @RequestMapping(value = "publish/{pk}")
	public String publish(@ModelAttribute Template template, HttpServletRequest request) {
    	
    	TemplateService service = this.getServiceFromRequest(request);
    	
    	if(service != null){
    		
    		service.publish(template);
			
    	}
		return "redirect:/domain/template/show/" + template.getPrimaryKey(); //
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