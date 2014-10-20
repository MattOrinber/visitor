/*

 * Template pack-mvc-3:src/main/java/web/controller/controller.e.vm.java
 */
package org.visitor.appportal.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import org.visitor.appportal.context.AccountContext;
import org.visitor.appportal.domain.Folder;
import org.visitor.appportal.domain.HtmlPage;
import org.visitor.appportal.domain.HtmlPage.PageTypeEnum;
import org.visitor.appportal.repository.FolderRepository;
import org.visitor.appportal.repository.HtmlPageRepository;
import org.visitor.appportal.repository.RecommandContainerRepository;
import org.visitor.appportal.repository.TemplateRepository;
import org.visitor.appportal.service.TemplatePageService;
import org.visitor.appportal.web.utils.TemplatePage;

@Controller
@RequestMapping("/domain/htmlpage/")
public class HtmlPageController {

	@Autowired
	private TemplatePage templatePage;
	@Autowired
	private TemplatePageService templatePageService;
    @Autowired
    private HtmlPageRepository htmlPageRepository;
    
    @Autowired
    private FolderRepository folderRepository;
    
    @Autowired
    private TemplateRepository templateRepository;
    
    @Autowired
    private RecommandContainerRepository containerRepository;

    /**
     * Serves the create form.
     */
    @RequestMapping(value = "create", method = GET)
    public String create(@ModelAttribute HtmlPage htmlPage) {
        return "domain/htmlpage/create";
    }

    /**
     * Performs the create action and redirect to the show view.
     */
    @RequestMapping(value = "create", method = { POST, PUT })
    public String create(@ModelAttribute HtmlPage htmlPage, BindingResult bindingResult, Model model) {
		StringBuffer sb = new StringBuffer();
		
		List<Long> ids = templatePage.extractContainerIds(htmlPage.getTempMeta());
		if (ids != null && ids.size() > 0) {
			for(Long id : ids) {
				if(containerRepository.findOne(id) == null) {
					sb.append(id).append(";");
				}
			}
		}
		if(sb.length() > 0) {
			model.addAttribute("message", "[" + sb.toString() + "] 不存在");
			return edit(htmlPage, htmlPage.getFolderId(), htmlPage.getPageType(), 0, model);
		}
        
        Date cdate = new Date();
        String username = AccountContext.getAccountContext().getAccount().getUsername();

		Folder folder = folderRepository.findOne(htmlPage.getFolderId());
		htmlPage.setName(PageTypeEnum.getInstance(htmlPage.getPageType()).getDisplayName());
		htmlPage.setPath(folder.getPath() + "/" + PageTypeEnum.getInstance(htmlPage.getPageType()).getSuffix()); // path
		htmlPage.setTemplate(templateRepository.findOne(htmlPage.getTemplateId()));
		htmlPage.setFolder(folder);
		htmlPage.setCreateBy(username);
		htmlPage.setCreateDate(cdate);
		htmlPage.setIfDefaultPage(false);
		htmlPage.setSiteId(folder.getSiteId());
        htmlPage.setModBy(username);
        htmlPage.setModDate(cdate);
        htmlPage.setPublishStatus(HtmlPage.PublishStatusEnum.Unpublished.ordinal());
		
		if (htmlPage.getTempMeta().contains("lunxun")) {////////////
			htmlPage.setIfQueryElement(true);
		} else {
			htmlPage.setIfQueryElement(false);
		}
		templatePageService.saveHtmlPageWithContainerIds(htmlPage, ids);
		model.addAttribute("htmlpage", htmlPage);
    	return "redirect:/domain/folder/show/" + htmlPage.getFolderId();
    }

    /**
     * Serves search by example form, search by pattern form and search by named query form.
     */
    @RequestMapping(value = "*", method = GET)
    public void catchAll(@ModelAttribute HtmlPageSearchForm htmlPageSearchForm) {
    }
    
    /**
     * init cms htmlpage
     * @param folderId
     * @param type
     * @param model
     * @return
     */
    @RequestMapping(value = "edit/{folderId}/{type}/{style}", method = { GET })
    public String edit(@ModelAttribute HtmlPage htmlPage, @PathVariable("folderId") Long folderId,
    		@PathVariable("type") Integer type, 
    		@PathVariable("style") Integer style,     		
    		Model model) {
    	HtmlPage h = htmlPageRepository.findByFolderIdAndPageType(folderId, type);
    	if (h == null) {
    		htmlPage.setFolderId(folderId) ;
    		htmlPage.setPageType(type);
    		model.addAttribute("folder", this.folderRepository.findOne(folderId));
    		PageTypeEnum pageType = PageTypeEnum.getInstance(htmlPage.getPageType());
    		model.addAttribute("pageTypeName", pageType == null ? "" : pageType.getDisplayName());
    		model.addAttribute("htmlpage", htmlPage);
    		return "domain/htmlpage/create";
    	} else {
    		if(style!=null && style.intValue() == 1){
    			//visual mode
    			return "redirect:/domain/htmlpage/vedit/" + h.getPrimaryKey();
    		}else {
    			return "redirect:/domain/htmlpage/edit/" + h.getPrimaryKey();
    		}
    	}
    }
    
	/**
	 * 频道和页面联动
	 * 
	 * @author mengw
	 * 
	 * @return
	 */
	@RequestMapping(value = "pageList/{pk}", method = GET)
	@ResponseBody
	public List<Map<String, String>> pageList(@PathVariable("pk") Long folderId ,HttpServletResponse resp) {
		List<HtmlPage> list = htmlPageRepository.findByFolderId(folderId);
		List<Map<String, String>> out = new ArrayList<Map<String,String>>();
		for (HtmlPage opt : list) {
			Map<String, String> map = new LinkedHashMap<String, String>();
			map.put("key", opt.getName());
			map.put("value", opt.getPageId()+"");
			out.add(map);
		}
		return out;
	}
}