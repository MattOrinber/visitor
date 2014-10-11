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
import org.visitor.appportal.domain.Category;
import org.visitor.appportal.redis.CategoryRedisRepository;
import org.visitor.appportal.repository.CategoryRepository;
import org.visitor.appportal.service.CategoryService;
import org.visitor.appportal.web.utils.AutoCompleteResult;
import org.visitor.appportal.web.utils.SearchParameters;

@Controller
@RequestMapping("/domain/category/")
public class CategoryController {
	/**
	 * Session Attribute的key,用来保存用户正在操作的字典的根目录ID
	 */
	static protected String TREE_ROOT_ID = "tree_root_id";
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private CategoryRedisRepository categoryRedisRepository;
	
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
	public String list(@ModelAttribute CategorySearchForm categorySearchForm, Model model) {
		model.addAttribute("categoriesCount", categoryRepository.findCount(categorySearchForm.getCategory(), categorySearchForm.toSearchTemplate()));
		model.addAttribute("categories", categoryRepository.find(categorySearchForm.getCategory(), categorySearchForm.toSearchTemplate()));
		return "domain/category/list";
	}

	/**
	 * Serves the create form.
	 */
	@RequestMapping(value = "create", method = GET)
	public String create(@ModelAttribute Category category) {
		Integer sortOrder = categoryRepository.findMaxSortOrderByParentId(category.getParentCategoryId());
		category.setSortOrder(sortOrder + 1);
		return "domain/category/create";
	}

	/**
	 * Performs the create action and redirect to the show view.
	 */
	@RequestMapping(value = "create", method = { POST, PUT })
	public String create(@Valid @ModelAttribute Category category, BindingResult bindingResult,
			HttpServletRequest request) {
		List<Category> list = categoryRepository.findByParentCategoryIdAndName(category.getParentCategoryId(), category.getName());
		if(null != list && list.size() > 0) {
			bindingResult.rejectValue("name", "category_name_exist", new String[]{category.getName()},
					"名称：" + category.getName() + " 已经存在。");
		}
		if (bindingResult.hasErrors()) {
			category.setParentCategory(this.categoryRepository.findOne(category.getParentCategoryId()));
			return create(category);
		}

		Date date = new Date();
		category.setCreateDate(date);
		category.setModDate(date);
		category.setModBy(AccountContext.getAccountContext().getUsername());
		category.setCreateBy(AccountContext.getAccountContext().getUsername());
		category.setParentCategory(categoryRepository.findOne(category.getParentCategoryId()));
		categoryService.saveNode(category);
		
		//分类的子分类
		List<Category> categoryList = 
			categoryRepository.findByParentCategoryIdAndStatus(category.getCategoryId(), Category.ENABLE, null);
			
		List<Category> ancestors = categoryRepository.findAncestors(category.getCategoryId());

		categoryRedisRepository.categoryUpdate(category, categoryList, ancestors, null);
		
		return "redirect:/domain/category/showtree/" + request.getSession().getAttribute(TREE_ROOT_ID);
	}
	
	@RequestMapping("childcategory")
	@ResponseBody
	public List<AutoCompleteResult> autocomplete(@RequestParam(value = "term", required = false) String searchPattern,
			@RequestParam(value = "categoryId", required = true) Long categoryId,
			SearchParameters search) {

		List<AutoCompleteResult> ret = new ArrayList<AutoCompleteResult>();
		for (Category category : categoryRepository.findByParentCategoryIdAndStatus(categoryId, Category.ENABLE, null)) {
			String objectPk = category.getPrimaryKey().toString();
			String objectLabel = category.getName();
			//String objectLabel = formattingConversionService.convert(category, String.class);
			ret.add(new AutoCompleteResult(objectPk, objectLabel));
		}
		return ret;
	}
}