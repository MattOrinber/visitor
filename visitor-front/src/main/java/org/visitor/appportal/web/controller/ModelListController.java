/*

 * Template pack-mvc-3:src/main/java/web/controller/controller.e.vm.java
 */
package org.visitor.appportal.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import org.visitor.appportal.context.AccountContext;
import org.visitor.appportal.domain.Category;
import org.visitor.appportal.domain.ModelList;
import org.visitor.appportal.repository.ModelListRepository;
import org.visitor.appportal.service.CategoryService;

@Controller
@RequestMapping("/domain/modellist/")
public class ModelListController {

	@Autowired
	private ModelListRepository modelListRepository;
	@Autowired
	private CategoryService categoryService;

	/**
	 * Performs the list action.
	 */
	@RequestMapping(value = { "list", "" })
	public String list(@ModelAttribute ModelListSearchForm modelListSearchForm, Model model) {
		Page<ModelList> page = modelListRepository.findAll(modelListSearchForm.toSpecification(), modelListSearchForm.getPageable());
		model.addAttribute("modelListsCount", page.getTotalElements());
		model.addAttribute("modelLists", page.getContent());
		return "domain/modellist/list";
	}

	/**
	 * Serves the create form.
	 */
	@RequestMapping(value = "create", method = GET)
	public String create(@ModelAttribute ModelList modelList, Model model) {
    	model.addAttribute("platformList", categoryService.findCategoryChild(Category.PLATFORM, null));
		model.addAttribute("brandList", categoryService.findCategoryChild(Category.BRAND, null));
		model.addAttribute("resolutionList", categoryService.findCategoryChild(Category.RESOLUTION, null));

		if (modelList.getPlatformId() != null) {
			List<Category> list = categoryService.findCategoryChild(modelList.getPlatformId(), null);
			model.addAttribute("platformVersionList", list);
		}
		return "domain/modellist/create";
	}

	/**
	 * Performs the create action and redirect to the show view.
	 */
	@RequestMapping(value = "create", method = { POST, PUT })
	public String create(@Valid @ModelAttribute ModelList modelList, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			return create(modelList, model);
		}
		Date date = new Date();
		modelList.setCreateDate(date);
		modelList.setModDate(date);
		modelList.setModBy(AccountContext.getAccountContext().getUsername());
		modelList.setCreateBy(AccountContext.getAccountContext().getUsername());
		modelListRepository.save(modelList);
		return "redirect:/domain/modellist/show/" + modelList.getPrimaryKey();
	}

	/**
	 * Serves search by example form, search by pattern form and search by named
	 * query form.
	 */
	@RequestMapping(value = "*", method = GET)
	public void catchAll(@ModelAttribute ModelListSearchForm modelListSearchForm, Model model) {
	}

}