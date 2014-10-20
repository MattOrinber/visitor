/*

 * Template pack-mvc-3:src/main/java/web/controller/controller.e.vm.java
 */
package org.visitor.appportal.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.visitor.appportal.context.AccountContext;
import org.visitor.appportal.domain.Folder;
import org.visitor.appportal.domain.Site;
import org.visitor.appportal.redis.SiteRedisRepository;
import org.visitor.appportal.repository.FolderRepository;
import org.visitor.appportal.repository.SiteRepository;
import org.visitor.appportal.repository.TemplateRepository;
import org.visitor.appportal.service.SiteFolderService;
import org.visitor.appportal.web.utils.AutoCompleteResult;
import org.visitor.appportal.web.utils.SearchParameters;
import org.visitor.appportal.web.utils.SiteUtil;

@Controller
@RequestMapping("/domain/site/")
public class SiteController {
	
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private SiteFolderService folderService;
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private TemplateRepository templateRepository;
    @Autowired
    private SiteRedisRepository siteRedisRepository;

    @InitBinder
    public void initDataBinder(WebDataBinder binder){
        // you can explicitly allow certain fields or exclude them
        // binder.setAllowedFields(new String[]{"resourceAudio.resourceid", "resourceAudio.resourceName", "resourceAudio.userid", "resourceAudio.status", "resourceAudio.auditStatus", "resourceAudio.uploadBy", "resourceAudio.lastoperBy", "resourceAudio.createDt", "resourceAudio.lastoperDt", "resourceAudio.dlCount", "resourceAudio.author", "resourceAudio.audioProperty", "resourceAudio.recordCorp", "resourceAudio.version", "resourceAudio.language", "resourceAudio.region", "resourceAudio.rhythm", "resourceAudio.musicType", "resourceAudio.musicSuit", "resourceAudio.singer", "resourceAudio.lyricsAuthor", "resourceAudio.albumName", "resourceAudio.wayOperate", "resourceAudio.resourceSize", "resourceAudio.resourceFormat", "resourceAudio.audioSource", "resourceAudio.intro", "resourceAudio.tag", "resourceAudio.ifIndexed", "resourceAudio.fileCount", "resourceAudio.resourceStatus"});
        binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }
    
    /**
     * Performs the list action.
     */
    @RequestMapping(value = {"list", "" })
    public String list(Model model,HttpServletRequest request){
        List<Site> siteList = new ArrayList<Site>();
        
        Integer siteId = SiteUtil.getSiteFromSession(request.getSession());
        
        if(siteId != null) {
        	
        	siteList.add(this.siteRepository.findOne(siteId));
        	model.addAttribute("sites", siteList);
        	
        	return "domain/site/list";
        
        } else {
        	
        	return "redirect:/login";
        }
    }

    /**
     * Serves the create form.
     */
    @RequestMapping(value = "create", method = GET)
    public String create(@ModelAttribute Site site, Model model) {
        return "domain/site/create";
    }

    /**
     * Performs the create action and redirect to the show view.
     */
    @RequestMapping(value = "create", method = { POST, PUT })
    public String create(@Valid @ModelAttribute Site site, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return create(site, model);
        }
        Date cdate = new Date();
        String username = AccountContext.getAccountContext().getUsername();

        site.setStatus(Site.StatusEnum.Enable.ordinal());
        site.setCreateBy(username);
        site.setCreateDate(cdate);
        site.setModBy(username);
        site.setModDate(cdate);
        site.setDefaultPath("/home");
        
        site.setIndexTemplate(templateRepository.findOne(site.getIndexTemplateId()));
        site.setListTemplate(templateRepository.findOne(site.getListTemplateId()));
        site.setDetailTemplate(templateRepository.findOne(site.getDetailTemplateId()));
        site.setNaviTemplate(templateRepository.findOne(site.getNaviTemplateId()));
        
        folderService.saveSiteInit(site);
        initSiteRedisInfo(site);
        
        return "redirect:/domain/site/show/" + site.getPrimaryKey();
    }

    /**
     * Serves search by example form, search by pattern form and search by named query form.
     */
    @RequestMapping(value = "*", method = GET)
    public void catchAll(@ModelAttribute SiteSearchForm siteSearchForm) {
    }
    
    /**
     * 
     */
    @RequestMapping("autocomplete")
    @ResponseBody
    public List<AutoCompleteResult> autocomplete(@RequestParam(value = "term", required = false) String searchPattern,
            SearchParameters search) {
        if (searchPattern != null && !searchPattern.isEmpty()) {
        	searchPattern = searchPattern.trim();
        }
        List<AutoCompleteResult> ret = new ArrayList<AutoCompleteResult>();
        List<Site> list = siteRepository.findByNameLike("%" + searchPattern + "%");
        for (Site site : list) {
            String objectPk = site.getPrimaryKey().toString();
            String objectLabel = site.getName();
            ret.add(new AutoCompleteResult(objectPk, objectLabel));
        }
        return ret;
    }
    
    /**
     * init site redis data
     * @param site
     */
    private void initSiteRedisInfo (Site site) {

    	//init site
		siteRedisRepository.setSite(site);
//      [@temp]templatePage.bindSiteTemplate(site);
//      [@temp]redisRepository.siteMessage(site);
		
        //init folder
		List<Folder> folders = folderRepository.findBySiteIdAndFolderType(site.getSiteId(), Folder.FOLDER_TYPE_SYSTEM);
		if (folders != null && folders.size() > 0) {
			for (Folder f : folders) {
				siteRedisRepository.setFolder(f);
			}
		}
//		
//		//init index page
//		[@temp]templatePage.saveHtmlPage(f.getIndexPage());
//		[@temp]redisRepository.setHtmlPage(f.getIndexPage());
//		[@temp]redisRepository.pageMessage(f.getIndexPage());
//		//init list page
//		[@temp]templatePage.saveHtmlPage(f.getListPage());
//		[@temp]redisRepository.setHtmlPage(f.getListPage());
//		[@temp]redisRepository.pageMessage(f.getListPage());
//		//init detail page
//		[@temp]templatePage.saveHtmlPage(f.getDetailPage());
//		[@temp]redisRepository.setHtmlPage(f.getDetailPage());
//		[@temp]redisRepository.pageMessage(f.getDetailPage());
//		//init navi page
//		[@temp]templatePage.saveHtmlPage(f.getNaviPage());
//		[@temp]redisRepository.setHtmlPage(f.getNaviPage());
//		[@temp]redisRepository.pageMessage(f.getNaviPage());
		
    }

}