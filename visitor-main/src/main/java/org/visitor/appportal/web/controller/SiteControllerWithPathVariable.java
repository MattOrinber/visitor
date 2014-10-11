/*

 * Template pack-mvc-3:src/main/java/web/controller/controllerwithPathVariable.e.vm.java
 */
package org.visitor.appportal.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.visitor.appportal.context.AccountContext;
import org.visitor.appportal.domain.Folder;
import org.visitor.appportal.domain.HtmlPage;
import org.visitor.appportal.domain.Site;
import org.visitor.appportal.domain.SiteValue.TypeEnum;
import org.visitor.appportal.redis.SiteRedisRepository;
import org.visitor.appportal.redis.TemplateRedisRepository;
import org.visitor.appportal.repository.FolderRepository;
import org.visitor.appportal.repository.HtmlPageRepository;
import org.visitor.appportal.repository.SiteRepository;
import org.visitor.appportal.repository.SiteValueRepository;
import org.visitor.appportal.repository.TemplateRepository;
import org.visitor.appportal.service.TemplatePageService;
import org.visitor.appportal.web.utils.TemplatePage;

@Controller
@RequestMapping("/domain/site/")
public class SiteControllerWithPathVariable {
	@Autowired
	private SiteValueRepository siteValueRepository;
	@Autowired
    private FolderRepository folderRepository;
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private TemplatePage templatePage;
    @Autowired
    private TemplateRepository templateRepository;
    @Autowired
    private TemplateRedisRepository templateRedisRepository;
    @Autowired
    private HtmlPageRepository htmlPageRepository;
    @Autowired
    private TemplatePageService templatePageService;
    @Autowired
    private SiteRedisRepository siteRedisRepository;
    
    /**
     * This method is invoked by Spring MVC before the handler methods.
     * <p>
     * The path variable is converted by SpringMVC to a Site via the {@link SiteFormatter}.
     * Before being passed as an argument to the handler, SpringMVC binds the attributes on the resulting model,
     * then each handler method may receive the entity, potentially modified, as an argument.
     */
    @ModelAttribute
    public Site getSite(@PathVariable("pk") Integer pk) {
        return siteRepository.findOne(pk);
    }

    /**
     * Serves the show view for the entity.
     */
    @RequestMapping("show/{pk}")
    public String show(@ModelAttribute Site site, Model model) {
    	//只查找可用的节点
		List<Folder> folders = folderRepository.findBySiteIdAndStatus(site.getSiteId(), Folder.ENABLE);
		List<Folder> result = new ArrayList<Folder>();
		
		for(Folder f : folders) {
			if(f.getParentFolder() == null) {
				//只添加顶层频道
				result.add(f);
			}
		}
		
		//频道节点，这个一定得有
    	model.addAttribute("folderNodes", result);
    	
    	model.addAttribute("platforms", siteValueRepository.getSiteValueCategory(site.getSiteId(), TypeEnum.PlatformVerion.getValue()));
    	model.addAttribute("operaversions", siteValueRepository.getSiteValueCategory(site.getSiteId(), TypeEnum.OperaVersion.getValue()));
    	model.addAttribute("resolutions", siteValueRepository.getSiteValueCategory(site.getSiteId(), TypeEnum.Resolution.getValue()));
        return "domain/site/show";    	
    }

    /**
     * Serves the update form view.
     */
    @RequestMapping(value = "update/{pk}", method = GET)
    public String update() {
        return "domain/site/update";
    }

    /**
     * Performs the update action and redirect to the show view.
     */
    @RequestMapping(value = "update/{pk}", method = { PUT, POST })
    public String update(@Valid @ModelAttribute Site site, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return update();
        }
        
        Date cdate = new Date();
        String username = AccountContext.getAccountContext().getUsername();
        site.setModBy(username);
        site.setModDate(cdate);

        siteRepository.save(site);
        siteRedisRepository.setSite(site);
                
        return "redirect:/domain/site/show/" + site.getPrimaryKey();
    }

    /**
     * Serves the delete form asking the user if the entity should be really deleted.
     */
    @RequestMapping(value = "delete/{pk}", method = GET)
    public String delete() {
        return "domain/site/delete";
    }

    /**
     * Performs the delete action and redirect to the search view.
     */
    @RequestMapping(value = "delete/{pk}", method = { PUT, POST, DELETE })
    public String delete(@ModelAttribute Site site) {
        siteRepository.delete(site);
        siteRedisRepository.deleteSite(site);
        return "redirect:/domain/site/search";
    }

//    /**
//     * Performs the get site top folders.
//     */
//    @RequestMapping(value = "topfolder/{pk}/", method = GET)
//    public String showtree(@PathVariable("pk") Integer siteId, Model model){    	
//    	List<FolderNode> folders = folderService.findSiteFolderTree(siteId);
//    	model.addAttribute("folders", folders);
//    	
//    	return "domain/site/topfolder";
//    }
    
    /**
     * Serves the template edit.
     */
    @RequestMapping(value = "template/{pk}/{type}", method = GET)
    public String template(@PathVariable("type") Integer type, @ModelAttribute Site site, Model model) {
    	model.addAttribute("type", type);
        return "domain/site/template";
    }
    
    /**
     * Serves the template edit.
     */
    @RequestMapping(value = "template/{pk}/{type}", method = { PUT, POST })
	public String template(@PathVariable("type") Integer type, @ModelAttribute Site site) {
		Long templateId = null;
		switch (type) {
		case 0:
			templateId = site.getIndexTemplateId();
			site.setIndexTemplate(this.templateRepository.findOne(templateId));
			break;
		case 1:
			templateId = site.getListTemplateId();
			site.setListTemplate(this.templateRepository.findOne(templateId));
			break;
		case 2:
			templateId = site.getDetailTemplateId();
			site.setDetailTemplate(this.templateRepository.findOne(templateId));
			break;
		case 3:
			templateId = site.getNaviTemplateId();
			site.setNaviTemplate(this.templateRepository.findOne(templateId));
			break;
		}
		templatePageService.changeTemplate(templateId, site, type);
		return "redirect:/domain/site/show/" + site.getPrimaryKey();
	}
    
    
    /**
     * 发布站点的默认模板。实际是将默认htmlpage页面进行发布，因为其模板发生了改变。
     * @param type
     * @param site
     * @return
     */
    @RequestMapping(value = "publishtemplate/{pk}/{type}", method = { PUT, POST })
	public String publishtemplate(@PathVariable("type") Integer type, @ModelAttribute Site site) {
		HtmlPage page = htmlPageRepository.findBySiteIdAndIfDefaultPageAndPageType(
				site.getSiteId(), HtmlPage.DEFAULT_PAGE, type);
		//去页面内容
		String content = templatePage.createHtmlPageFile(page, false);
		//发布内容
		templateRedisRepository.publishHtmlPage(page, content, false);
		//将站点信息发布，默认模板发生了改变。
		siteRedisRepository.setSite(site);
		return "redirect:/domain/site/show/" + site.getPrimaryKey();
	}
}