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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
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

import org.visitor.appportal.context.AccountContext;
import org.visitor.appportal.domain.Folder;
import org.visitor.appportal.domain.HtmlPage;
import org.visitor.appportal.domain.PageContainer;
import org.visitor.appportal.domain.PageContainerPk;
import org.visitor.appportal.domain.PageContainer.ShowTypeEnum;
import org.visitor.appportal.repository.FolderRepository;
import org.visitor.appportal.repository.HtmlPageRepository;
import org.visitor.appportal.repository.PageContainerRepository;
import org.visitor.appportal.repository.RecommandContainerRepository;

@Controller
@RequestMapping("/domain/pagecontainer/")
public class PageContainerController {

    @Autowired
    private PageContainerRepository pageContainerRepository;
    
    @Autowired
    private HtmlPageRepository htmlPageRepository;
    
    @Autowired
    private RecommandContainerRepository recommandContainerRepository;
    @Autowired
    private FolderRepository folderRepository;
    
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
    public String list(@ModelAttribute PageContainerSearchForm pageContainerSearchForm, Model model) {
    	model.addAttribute("pageContainersCount", pageContainerRepository.findCount(pageContainerSearchForm.getPageContainer(), pageContainerSearchForm.toSearchTemplate()));
        model.addAttribute("pageContainers", pageContainerRepository.find(pageContainerSearchForm.getPageContainer(), pageContainerSearchForm.toSearchTemplate()));
        return "domain/pagecontainer/list";
    }
    
    /**
     * Serves the create form.
     */
    @RequestMapping(value = "create", method = GET)
    public String create(@ModelAttribute PageContainer pageContainer) {
        return "domain/pagecontainer/create";
    }

    /**
     * Performs the create action and redirect to the show view.
     */
    @RequestMapping(value = "create", method = { POST, PUT })
    public String create(@Valid @ModelAttribute PageContainer pageContainer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return create(pageContainer);
        }
        
        Date cdate = new Date();
        String username = AccountContext.getAccountContext().getUsername();
        
        pageContainer.setCreateBy(username);
        pageContainer.setCreateDate(cdate);
        pageContainer.setModBy(username);
        pageContainer.setModDate(cdate);

        pageContainerRepository.save(pageContainer);
        return "redirect:/domain/pagecontainer/show/" + pageContainer.getPrimaryKey();
    }
    
    /**
     * Performs the add container the page view.
     */
    @RequestMapping(value = "add", method = GET)
    public String add(@RequestParam(value = "pageId", required = true) Long pageId,
    		@RequestParam(value = "containerId", required = true) Long containId){
    	
    	PageContainerPk pageContainerPk = new PageContainerPk(containId, pageId);
    	
    	if (pageContainerRepository.findOne(pageContainerPk) == null) {
    		
        	PageContainer pageContainer = new PageContainer();
        	pageContainer.setContainer(recommandContainerRepository.findOne(containId));
        	
        	HtmlPage htmlpage = htmlPageRepository.findOne(pageId);
        	
        	pageContainer.setPage(htmlpage);
        	pageContainer.setFolder(htmlpage.getFolder());
        	pageContainer.setSite(htmlpage.getFolder().getSite());
        	
            Date cdate = new Date();
            String username = AccountContext.getAccountContext().getUsername();
            
            pageContainer.setCreateBy(username);
            pageContainer.setCreateDate(cdate);
            pageContainer.setModBy(username);
            pageContainer.setModDate(cdate);
            
            pageContainer.setShowType(ShowTypeEnum.Manual.getValue());
            
            pageContainerRepository.save(pageContainer);
    	}
    	
    	return "redirect:/domain/htmlpage/edit/" + pageId;
    }
	
	@RequestMapping(value = "containerList/{pk}/{siteId}", method = GET)
	@ResponseBody
	public List<Map<String, String>> containerList(@PathVariable("pk") Long pageId ,
		@PathVariable("siteId") Integer siteId, HttpServletResponse resp) {
		List<PageContainer> list = pageContainerRepository.findByPageIdAndSiteId(pageId,siteId);
		List<Map<String, String>> out = new ArrayList<Map<String,String>>();
		for (PageContainer opt : list) {
			Map<String, String> map = new LinkedHashMap<String, String>();
			map.put("key", opt.getContainer().getName());
			map.put("value", opt.getContainer().getContainerId()+"");
			out.add(map);
		}
		return out;
	}
	
    @RequestMapping("autosite/{containerId}")
    @ResponseBody
    public List<Map<String, String>> autosite(@PathVariable("containerId") Long containerId) {
    	List<PageContainer> list = pageContainerRepository.findByContainerId(containerId);
    	List<Map<String, String>> out = new ArrayList<Map<String,String>>();
    	for (PageContainer opt : list) {
    		Map<String, String> map = new LinkedHashMap<String, String>();
			map.put("key", opt.getSite().getName());
			map.put("value", opt.getSiteId()+"");
			if (!out.contains(map)) {
				out.add(map);
			}
    	}
    	return out;
    }
    
    @RequestMapping("autofolder/{containerId}/{siteId}")
    @ResponseBody
    public List<Map<String, String>> autofolder(@PathVariable("containerId") Long containerId,
    		@PathVariable("siteId") Integer siteId) {
    	List<PageContainer> list = pageContainerRepository.findByContainerIdAndSiteId(containerId, siteId);
    	List<Map<String, String>> out = new ArrayList<Map<String,String>>();
    	for (PageContainer opt : list) {
    		Map<String, String> map = new LinkedHashMap<String, String>();
			map.put("key", opt.getFolder().getName());
			map.put("value", opt.getFolderId()+"");
			if (!out.contains(map)) {
				out.add(map);
			}
    	}
    	return out;
    }

	/**
	 * 根据站点选择其在容器关联表里的频道信息
	 * @return
	 */
	@RequestMapping(value = "autofolderList/{pk}", method = GET)
	@ResponseBody
	public List<Map<String, String>> autofolderList(@PathVariable("pk") Integer siteId) {
		List<Long> folderIds = pageContainerRepository.findByContainerFolderId(siteId);
		List<Map<String, String>> out = new ArrayList<Map<String,String>>();
		for (Long folderId : folderIds) {
			Map<String, String> map = new LinkedHashMap<String, String>();
			Folder folder = folderRepository.findOne(folderId);
			map.put("key", folder.getName());
			map.put("value",folderId+"");
			out.add(map);
		}
		return out;
	}

	/**
	 * 根据频道选择其在容器关联表里的页面列表
	 * @return
	 */
	@RequestMapping(value = "autopageList/{pk}", method = GET)
	@ResponseBody
	public List<Map<String, String>> autopageList(@PathVariable("pk") Long folderId ,HttpServletResponse resp) {
		List<Long> pageIds = pageContainerRepository.findByContainerPageId(folderId);
		List<Map<String, String>> out = new ArrayList<Map<String,String>>();
		for (Long pageId : pageIds) {
			Map<String, String> map = new LinkedHashMap<String, String>();
			HtmlPage htmlPage = htmlPageRepository.findOne(pageId);
			map.put("key", htmlPage.getName());
			map.put("value", htmlPage.getPageId()+"");
			out.add(map);
		}
		return out;
	}
	
    @RequestMapping("autopage/{containerId}/{siteId}/{folderId}")
    @ResponseBody
    public List<Map<String, String>> autofolder(@PathVariable("containerId") Long containerId,
    		@PathVariable("siteId") Integer siteId,@PathVariable("folderId") Long folderId) {
    	List<PageContainer> list = pageContainerRepository.findByContainerIdAndSiteIdAndFolderId(containerId, siteId, folderId);
    	List<Map<String, String>> out = new ArrayList<Map<String,String>>();
    	for (PageContainer opt : list) {
    		Map<String, String> map = new LinkedHashMap<String, String>();
			map.put("key", opt.getPage().getName());
			map.put("value", opt.getPageId()+"");
			if (!out.contains(map)) {
				out.add(map);
			}
    	}
    	return out;
    }
	
    /**
     * Serves search by example form, search by pattern form and search by named query form.
     */
    @RequestMapping(value = "*", method = GET)
    public void catchAll(@ModelAttribute PageContainerSearchForm pageContainerSearchForm){
    }

}