package org.visitor.appportal.service.site;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.visitor.appportal.context.AccountContext;
import org.visitor.appportal.domain.HtmlPage;
import org.visitor.appportal.domain.Template;
import org.visitor.appportal.domain.Template.PublishStatusEnum;
import org.visitor.appportal.redis.TemplateRedisRepository;
import org.visitor.appportal.repository.HtmlPageRepository;
import org.visitor.appportal.repository.TemplateRepository;
import org.visitor.appportal.web.controller.TemplateSearchForm;
import org.visitor.appportal.web.utils.AutoCompleteResult;
import org.visitor.appportal.web.utils.TemplatePage;

/**
 * 模板Service
 * @author mengw
 *
 */
public abstract class TemplateService extends SiteService {
    
	@Autowired
    private TemplatePage templatePage;
    @Autowired
    private HtmlPageRepository htmlPageRepository;
    @Autowired
    private TemplateRepository templateRepository;
    @Autowired
    private TemplateRedisRepository templateRedisRepository;

	/**
	 * 列表展示
	 * @param templateSearchForm
	 * @param model
	 */
	public void list(TemplateSearchForm templateSearchForm,Model model){
    	
		templateSearchForm.getTemplate().setSiteId(getSiteId());
		
		model.addAttribute("templatesCount", templateRepository.findCount(templateSearchForm.getTemplate(), templateSearchForm.toSearchTemplate()));
        model.addAttribute("templates", templateRepository.find(templateSearchForm.getTemplate(), templateSearchForm.toSearchTemplate()));

	}
	
	/**
	 * 创建
	 * @param template
	 * @param bindingResult
	 * @param model
	 * @return
	 */
	public ResultEnum create(Template template, BindingResult bindingResult, Model model){

    	Template t = templateRepository.findByNameAndSiteId(template.getName(), this.getSiteId());
    	//模板名称只在本站点内有效
    	if(null != t && null != t.getPrimaryKey() && (null != t.getSiteId() && t.getSiteId().intValue() == getSiteId().intValue())){
    		bindingResult.reject("name", new String[]{template.getName()}, 
    				"该模板："  + template.getName() + "已经存在");
    		return ResultEnum.ERROR;
    	}
        Date cdate = new Date();
        String username = AccountContext.getAccountContext().getUsername();

        template.setCreateBy(username);
        template.setCreateDate(cdate);
        template.setModBy(username);
        template.setModDate(cdate);
        template.setPublishStatus(Template.PublishStatusEnum.Published.ordinal());
        
        template.setSite(getSite());

        templateRepository.save(template);
        
        return ResultEnum.OK;
	}

	/**
	 * 提供自动提示的结果
	 * @param searchPattern
	 * @param search
	 * @return
	 */
	public List<AutoCompleteResult> autocomplete(String searchPattern) {
		// TODO Auto-generated method stub
		List<AutoCompleteResult> ret = new ArrayList<AutoCompleteResult>();
    	
        if (searchPattern != null && !searchPattern.isEmpty()) {
        	searchPattern = searchPattern.trim();
        }
        
        List<Template> ts = templateRepository.findByNameLike("%" + searchPattern + "%",getSiteId());
        for (Template template : ts) {
            String objectPk = template.getPrimaryKey().toString();
            String objectLabel = template.getName();
            ret.add(new AutoCompleteResult(objectPk, objectLabel));
        }
        
        return ret;
	}

	/**
	 * 更新模板
	 * @param template
	 * @param bindingResult
	 */
	public void update(Template template, BindingResult bindingResult) {
		// TODO Auto-generated method stub
		Date cdate = new Date();
        String username = AccountContext.getAccountContext().getUsername();
        template.setModBy(username);
        template.setModDate(cdate);

        templateRepository.save(template);
	}

	/**
	 * 修改模板的CMS信息
	 * @param template
	 */
	public void edit(Template template) {
		// TODO Auto-generated method stub
        Date cdate = new Date();
        String username = AccountContext.getAccountContext().getUsername();
        template.setModBy(username);
        template.setModDate(cdate);
        template.setPublishStatus(Template.PublishStatusEnum.Unpublished.ordinal());
        templateRepository.save(template);
	}

	/**
	 * 发布模板信息
	 * @param template
	 */
	public void publish(Template template) {
		// TODO Auto-generated method stub
		template.setMeta(template.getTempMeta());
		
		Date cdate = new Date();
		String username = AccountContext.getAccountContext().getAccount().getNickname();

		template.setPublishBy(username);
		template.setPublishDate(cdate);
		template.setPublishStatus(PublishStatusEnum.Published.ordinal());

		templateRepository.save(template);
		String content = templatePage.createTemplateFile(template);

		List<HtmlPage> list = htmlPageRepository.findByTemplateId(template.getPrimaryKey());
		templateRedisRepository.publishTemplate(template, list, content);
		
	}
	
}
