package org.visitor.appportal.service.site;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import org.visitor.appportal.context.AccountContext;
import org.visitor.appportal.domain.SearchKeyword;
import org.visitor.appportal.repository.SearchKeywordRepository;
import org.visitor.appportal.web.controller.SearchKeywordSearchForm;

public abstract class SearchKeywordService extends SiteService{

	@Autowired
	private SearchKeywordRepository searchKeywordRepository;
	
	public void list(SearchKeywordSearchForm searchKeywordSearchForm, 
    		Model model){
		
		searchKeywordSearchForm.getSearchKeyword().setSiteId(getSiteId());
		
		model.addAttribute("searchKeywordsCount", searchKeywordRepository.findCount(searchKeywordSearchForm.getSearchKeyword(), searchKeywordSearchForm.toSearchTemplate()));
		model.addAttribute("searchKeywords", searchKeywordRepository.find(searchKeywordSearchForm.getSearchKeyword(), searchKeywordSearchForm.toSearchTemplate()));       

	}
	
	/**
	 * 创建关键字
	 * @param searchKeyword
	 * @param bindingResult
	 * @param model
	 * @return
	 */
	public ResultEnum create(SearchKeyword searchKeyword,
			BindingResult bindingResult,Model model){

		if (bindingResult.hasErrors()) {
			return ResultEnum.ERROR;
		}
		
		if (!searchKeywordRepository.findBySiteIdAndName(getSiteId(), searchKeyword.getName(),null).isEmpty()) {
			bindingResult.addError(new FieldError("searchKeyword", "name",  
					"关键词：" + searchKeyword.getName() + "已经在站点：" + getDefaultSite().getName() + "设定。"));
			return ResultEnum.ERROR;
		}

		Date date = new Date();
		searchKeyword.setCreateBy(AccountContext.getAccountContext().getUsername());
		searchKeyword.setCreateDate(date);
		searchKeyword.setModBy(AccountContext.getAccountContext().getUsername());
		searchKeyword.setModDate(date);
		searchKeyword.setSortOrder(0l);
		searchKeyword.setSite(getSite());

		searchKeywordRepository.save(searchKeyword);
		
		return ResultEnum.OK;
		
	}
}
