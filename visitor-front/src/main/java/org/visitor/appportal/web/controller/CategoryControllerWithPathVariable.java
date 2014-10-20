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

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.visitor.appportal.context.AccountContext;
import org.visitor.appportal.domain.Category;
import org.visitor.appportal.redis.CategoryRedisRepository;
import org.visitor.appportal.repository.CategoryRepository;
import org.visitor.appportal.service.CategoryService;
import org.visitor.appportal.web.controller.common.BaseController;
import org.visitor.appportal.web.utils.SiteUtil;

@Controller
@RequestMapping("/domain/category/")
public class CategoryControllerWithPathVariable extends BaseController {
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private CategoryRedisRepository categoryRedisRepository;
	
	/**
	 * This method is invoked by Spring MVC before the handler methods.
	 * <p>
	 * The path variable is converted by SpringMVC to a Category via the
	 * {@link CategoryFormatter}. Before being passed as an argument to the
	 * handler, SpringMVC binds the attributes on the resulting model, then each
	 * handler method may receive the entity, potentially modified, as an
	 * argument.
	 */
	@ModelAttribute
	public Category getCategory(@PathVariable("pk") Long pk) {
		return categoryRepository.findOne(pk);
	}

	/**
	 * 显示产品分类树
	 */
	@RequestMapping(value = "showtree/{pk}")
	public String showTree(@ModelAttribute Category category, Model model, HttpServletRequest request) {
		
		Integer siteId = SiteUtil.getSiteFromSession(request.getSession());
		if(siteId != null) {
			
			this.getServiceFactory().getCategoryService(siteId).showTree(category,model);
			
			request.getSession().setAttribute(CategoryController.TREE_ROOT_ID, category.getCategoryId());

			return "domain/category/showtree";
		}else{
			return "redirect:/login";
		}
	}

	/**
	 * 添加子分类
	 */
	@RequestMapping("createChild/{pk}")
	public String createChild(@ModelAttribute Category parentCategory, Model model) {
		Category obj = new Category();
		obj.setParentCategory(parentCategory);
		Integer sortOrder = categoryRepository.findMaxSortOrderByParentId(parentCategory.getCategoryId());
		if (sortOrder == null) {
			sortOrder = 0;
		}
		obj.setSortOrder(sortOrder + 1);

		model.addAttribute("category", obj);
		model.addAttribute("nsThread", parentCategory.getNsThread());
		return "domain/category/create";
	}

	/**
	 * 添加同级分类
	 */
	@RequestMapping("createBrother/{pk}")
	public String createBrother(@ModelAttribute Category category, Model model) {
		Category obj = new Category();
		obj.setParentCategory(category.getParentCategory());
		Integer sortOrder = categoryRepository.findMaxSortOrderByParentId(obj.getParentCategoryId());
		obj.setSortOrder(sortOrder + 1);
		model.addAttribute("category", obj);
		model.addAttribute("nsThread", category.getParentCategory().getNsThread());
		return "domain/category/create";
	}

	/**
	 * Serves the show view for the entity.
	 */
	@RequestMapping("show/{pk}")
	public String show(@ModelAttribute Category category) {
		return "domain/category/show";
	}

	/**
	 * Serves the update form view.
	 */
	@RequestMapping(value = "update/{pk}", method = GET)
	public String update(@ModelAttribute Category category, Model model) {
		model.addAttribute("nsThread", category.getNsThread());
		model.addAttribute("nsRight", category.getNsRight());
		model.addAttribute("nsLeft", category.getNsLeft());
		
		//分辨率特殊处理
		if (category.getNsThread() == Category.PLATFORM) {
			Order order = new Order(Direction.ASC, "sortOrder");
			Sort sort = new Sort(order);
			List<Category> resolutions = categoryRepository.findByParentCategoryIdAndStatus(Category.RESOLUTION,Category.ENABLE, sort);
			model.addAttribute("resolutions", resolutions);
			
			Long platformId = category.getParentCategoryId();
			Long platformVersionId = category.getCategoryId();
			Category res = categoryRedisRepository.getPlatformVersionDefaultResolution(platformId, platformVersionId);
			model.addAttribute("resolution", res);
		}
		if(null == category.getSortOrder()) {
			Integer sortOrder = categoryRepository.findMaxSortOrderByParentId(category.getParentCategoryId());
			if(null == sortOrder) {
				sortOrder = 0;
			}
			category.setSortOrder(sortOrder);
		}
		return "domain/category/update";
	}

	/**
	 * Performs the update action and redirect to the show view.
	 */
	@RequestMapping(value = "update/{pk}", method = { PUT, POST })
	public String update(@ModelAttribute Category category, HttpServletRequest request, Model model, 
			@RequestParam(value = "oldname", required = false) String oldname,
			@RequestParam(value = "oldenname", required = false) String oldenname,
			@RequestParam(value = "resolution", required = false) String resolution) {
		
		category.setModBy(AccountContext.getAccountContext().getUsername());
		category.setModDate(new Date());

		categoryRepository.save(category);
		//清理修改前的数据
		categoryRedisRepository.categoryDelete(category, oldname, oldenname);
		//分类的子分类
		List<Category> categoryList = 
			categoryRepository.findByParentCategoryIdAndStatus(category.getCategoryId(), Category.ENABLE, null);
			
		List<Long> categoryIds = new ArrayList<Long>();
		if (!categoryList.isEmpty()) {
			for (Category c : categoryList) {
				categoryIds.add(c.getCategoryId());
			}
		}
		List<Category> ancestors = categoryRepository.findAncestors(category.getCategoryId());

		categoryRedisRepository.categoryUpdate(category, categoryList, ancestors, resolution);
		
		return "redirect:/domain/category/showtree/"+request.getSession().getAttribute(CategoryController.TREE_ROOT_ID);
	}
	
	/**
	 * Performs the delete action and redirect to the search view.
	 */
	@RequestMapping(value = "delete/{pk}", method = GET)
	public String delete(@ModelAttribute Category category, HttpServletRequest request, Model model) {
		List<Category> cs = categoryRepository.findByParentCategoryIdAndStatus(category.getCategoryId(), Category.ENABLE, null);
		if (cs != null & cs.size() > 0) {
			model.addAttribute("categorys", cs);
		}
		return "domain/category/delete";
	}

	/**
	 * Performs the delete action and redirect to the search view.
	 */
	@RequestMapping(value = "delete/{pk}", method = { PUT, POST, DELETE })
	public String delete(@ModelAttribute Category category, HttpServletRequest request) {
			if (categoryService.deleteCategory(category)) {
				categoryRedisRepository.categoryDelete(category);
			}
		return "redirect:/domain/category/showtree/"+request.getSession().getAttribute(CategoryController.TREE_ROOT_ID);
	}

}