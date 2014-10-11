/*

 * Template pack-mvc-3:src/main/java/web/controller/controllerwithPathVariable.e.vm.java
 */
package org.visitor.appportal.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.visitor.app.portal.model.TemplateItems;
import org.visitor.appportal.context.AccountContext;
import org.visitor.appportal.domain.Folder;
import org.visitor.appportal.domain.HtmlPage;
import org.visitor.appportal.domain.HtmlPage.PageTypeEnum;
import org.visitor.appportal.domain.PageContainer;
import org.visitor.appportal.domain.Template;
import org.visitor.appportal.redis.SiteRedisRepository;
import org.visitor.appportal.redis.TemplateRedisRepository;
import org.visitor.appportal.repository.FolderRepository;
import org.visitor.appportal.repository.HtmlPageRepository;
import org.visitor.appportal.repository.PageContainerRepository;
import org.visitor.appportal.repository.RecommandContainerRepository;
import org.visitor.appportal.repository.TemplateRepository;
import org.visitor.appportal.service.SystemPreference;
import org.visitor.appportal.service.TemplatePageService;
import org.visitor.appportal.web.utils.TemplatePage;

@Controller
@RequestMapping("/domain/htmlpage/")
public class HtmlPageControllerWithPathVariable {
    @Autowired
    private TemplatePage templatePage;
    @Autowired
    private HtmlPageRepository htmlPageRepository;
	@Autowired
	private TemplatePageService templatePageService;
    @Autowired
    private SiteRedisRepository siteRedisRepository;
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private TemplateRepository templateRepository;
    @Autowired
    private RecommandContainerRepository containerRepository;
    @Autowired
    private TemplateRedisRepository templateRedisRepository;
    @Autowired
    private PageContainerRepository pageContainerRepository;
    @Autowired
    private SystemPreference systemPreference;
    /**
     * This method is invoked by Spring MVC before the handler methods.
     * <p>
     * The path variable is converted by SpringMVC to a HtmlPage via the {@link HtmlPageFormatter}.
     * Before being passed as an argument to the handler, SpringMVC binds the attributes on the resulting model,
     * then each handler method may receive the entity, potentially modified, as an argument.
     */
    @ModelAttribute
    public HtmlPage getHtmlPage(@PathVariable("pk") Long pk) {
        return htmlPageRepository.findOne(pk);
    }

    /**
     * Serves the show view for the entity.
     */
    @RequestMapping("show/{pk}")
    public String show(@ModelAttribute HtmlPage htmlPage) {
        return "domain/htmlpage/show";
    }

    /**
     * Serves the update form view.
     */
    @RequestMapping(value = "update/{pk}", method = GET)
    public String update() {
        return "domain/htmlpage/update";
    }

    /**
     * Performs the update action and redirect to the show view.
     */
    @RequestMapping(value = "update/{pk}", method = { PUT, POST })
    public String update(@Valid @ModelAttribute HtmlPage htmlPage, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return update();
        }

        htmlPageRepository.save(htmlPage);
        return "redirect:/domain/htmlpage/show/" + htmlPage.getPrimaryKey();
    }

    /**
     * init cms htmlpage
     * @param folderId
     * @param type
     * @param model
     * @return
     */
    @RequestMapping(value = "edit/{pk}", method = { GET })
    public String edit(@ModelAttribute HtmlPage htmlPage, Model model) {
		
		List<PageContainer> pageContainers = pageContainerRepository.findByPageId(htmlPage.getPageId());
		
		model.addAttribute("pageContainers", pageContainers);
		model.addAttribute("folder", htmlPage.getFolder());
		PageTypeEnum type = PageTypeEnum.getInstance(htmlPage.getPageType());
		
		model.addAttribute("pageTypeName", type == null ? "" : type.getDisplayName());
    	return "domain/htmlpage/edit";
    }

    /**
     * init cms htmlpage
     * @param folderId
     * @param type
     * @param model
     * @return
     */
    @RequestMapping(value = "vedit/{pk}", method = { GET })
    public String vedit(@ModelAttribute HtmlPage htmlPage, Model model) {
		
		List<PageContainer> pageContainers = pageContainerRepository.findByPageId(htmlPage.getPageId());		
		List<TemplateItems> templates = templatePageService.getTemplateListFromContent(htmlPage.getMeta());
		
		
		//我们希望在界面用户可以直接选择模板，所以这里取出所有的模板，并将其按类型放到一个map中
		Iterable<Template> allTemp = this.templateRepository.findAll();
		
		StringBuffer[] bufferItem=new StringBuffer[4];
		for(int i=0;i<4;i++){
			bufferItem[i] = new StringBuffer();
			bufferItem[i].append("[");
		}
		
		StringBuffer templateInJson = new StringBuffer();
		
		
		templateInJson.append("[");
		
		for(Template tmp:allTemp){
			bufferItem[tmp.getType()].append("{\"tmpId\":\""+tmp.getTemplateId()+"\",\"tmpName\":\""+tmp.getName()+"\"},");
		}

		for(int i=0;i<4;i++){
			templateInJson.append("{\"item\":"+bufferItem[i].deleteCharAt(bufferItem[i].lastIndexOf(",")).append("]")+"},");
		}
		
		templateInJson = templateInJson.deleteCharAt(templateInJson.lastIndexOf(",")).append("]");
		
		//System.out.println(templateInJson);
		//将这个Map放到model中以供使用
		model.addAttribute("tempJson", templateInJson);
		
		model.addAttribute("pageContainers", pageContainers);
		model.addAttribute("templateList", templates);
		
		model.addAttribute("folder", htmlPage.getFolder());
		PageTypeEnum type = PageTypeEnum.getInstance(htmlPage.getPageType());
		
		model.addAttribute("pageTypeName", type == null ? "" : type.getDisplayName());
    	return "domain/htmlpage/vedit";
    }

    /**
     * save edit page
     */
    @RequestMapping(value = "edit/{pk}", method = { POST, PUT })
    public String edit(@ModelAttribute HtmlPage htmlPage, BindingResult bindingResult, Model model) {
		StringBuffer sb = new StringBuffer();
		
		List<Long> ids = templatePage.extractContainerIds(htmlPage.getTempMeta());
		if (ids != null && ids.size() > 0) {
			for(Long id : ids) {
				if(containerRepository.findOne(id) == null) {
					sb.append(id).append(";");
				}
			}
		}
		
		List<Long> fragementIds = templatePage.extractFragementIds(htmlPage.getTempMeta());
		List<Template> fragements = new ArrayList<Template>();
		if (fragementIds != null && fragementIds.size() > 0) {
			for(Long fragementId : fragementIds) {
				Template fragement = templateRepository.findOne(fragementId);
				if(fragement != null) {
					fragements.add(fragement);
				}
			}
		}
		htmlPage.setFragements(fragements);
		
		if(sb.length() > 0) {
			model.addAttribute("message", "[" + sb.toString() + "] 不存在");
			return this.edit(htmlPage, model);
		}
        Date cdate = new Date();
        String username = AccountContext.getAccountContext().getAccount().getUsername();
        htmlPage.setModBy(username);
        htmlPage.setModDate(cdate);
        htmlPage.setPublishStatus(HtmlPage.PublishStatusEnum.Unpublished.ordinal());
        htmlPage.setTemplate(this.templateRepository.findOne(htmlPage.getTemplateId()));
		if (htmlPage.getTempMeta().contains("lunxun")) {////////////
			htmlPage.setIfQueryElement(true);
		} else {
			htmlPage.setIfQueryElement(false);
		}
		templatePageService.saveHtmlPageWithContainerIds(htmlPage, ids);
		//创建预览页面。保存时只发布预览页面
		String content = templatePage.createHtmlPageFile(htmlPage, true);
		templateRedisRepository.publishHtmlPage(htmlPage, content, true);

		
		model.addAttribute("htmlpage", htmlPage);
    	return "redirect:/domain/folder/show/" + htmlPage.getFolderId();
    }
    
    /**
     * publish htmlpage
     */
    @RequestMapping(value = "publish/{pk}")
	public String publish(@ModelAttribute HtmlPage htmlPage, Model model) {
    	htmlPage.setMeta(htmlPage.getTempMeta());

		Date cdate = new Date();
		String username = AccountContext.getAccountContext().getUsername();

		htmlPage.setPublishBy(username);
		htmlPage.setPublishDate(cdate);
		htmlPage.setPublishStatus(HtmlPage.PublishStatusEnum.Published.ordinal());
		
		templatePageService.savePublishHtmlPage(htmlPage);
		
		//发布时发布实际内容
		String content = templatePage.createHtmlPageFile(htmlPage, false);
		templateRedisRepository.publishHtmlPage(htmlPage, content, false);
		
		Folder folder = this.folderRepository.findOne(htmlPage.getFolderId());
		if(null != folder) {
			siteRedisRepository.setFolder(folder.copy());
		}
		
		model.addAttribute("htmlpage", htmlPage);

		return "redirect:/domain/folder/show/" + htmlPage.getFolderId();
	}
    
    @RequestMapping(value = "preview/{pk}")
    public void preview(@ModelAttribute HtmlPage htmlPage, HttpServletRequest request,
    		HttpServletResponse response, Model model) {
    	String url = systemPreference.getWebappUrl();
    	String targetUrl = url + htmlPage.getPath() + ".htm?__preview__=true";
    	PageTypeEnum type = PageTypeEnum.getInstance(htmlPage.getPageType());
    	if(null != type && type == PageTypeEnum.Detail) {
    		targetUrl = url + htmlPage.getPath() + ".htm?__preview__=true&pid=9207";
    	}
		response.setStatus(301);
		response.setHeader("Location", response.encodeRedirectURL(targetUrl));
    }
}