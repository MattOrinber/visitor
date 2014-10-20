/*

 * Template pack-mvc-3:src/main/java/web/controller/controllerwithPathVariable.e.vm.java
 */
package org.visitor.appportal.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.visitor.appportal.domain.Category;
import org.visitor.appportal.domain.ModelList;
import org.visitor.appportal.repository.ModelListRepository;
import org.visitor.appportal.service.CategoryService;

@Controller
@RequestMapping("/domain/modellist/")
public class ModelListControllerWithPathVariable {
    @Autowired
    private ModelListRepository modelListRepository;
	@Autowired
	private CategoryService categoryService;
    /**
     * This method is invoked by Spring MVC before the handler methods.
     * <p>
     * The path variable is converted by SpringMVC to a ModelList via the {@link ModelListFormatter}.
     * Before being passed as an argument to the handler, SpringMVC binds the attributes on the resulting model,
     * then each handler method may receive the entity, potentially modified, as an argument.
     */
    @ModelAttribute
    public ModelList getModelList(@PathVariable("pk") Long pk) {
        return modelListRepository.findOne(pk);
    }

    /**
     * Serves the show view for the entity.
     */
    @RequestMapping("show/{pk}")
    public String show(@ModelAttribute ModelList modelList) {
        return "domain/modellist/show";
    }

    /**
     * Serves the update form view.
     */
    @RequestMapping(value = "update/{pk}", method = GET)
    public String update(@ModelAttribute ModelList modelList,Model model) {
    	model.addAttribute("platformList", categoryService.findCategoryChild(Category.PLATFORM, null));
		model.addAttribute("brandList", categoryService.findCategoryChild(Category.BRAND, null));
		model.addAttribute("resolutionList", categoryService.findCategoryChild(Category.RESOLUTION, null));
		
		if (modelList.getPlatformId() != null) {
			List<Category> list = categoryService.findCategoryChild(modelList.getPlatformId(), null);
			model.addAttribute("platformVersionList", list);
		}
        return "domain/modellist/update";
    }

    /**
     * Performs the update action and redirect to the show view.
     */
    @RequestMapping(value = "update/{pk}", method = { PUT, POST })
    public String update(@Valid @ModelAttribute ModelList modelList, BindingResult bindingResult,Model model) {
        if (bindingResult.hasErrors()) {
            return update(modelList,model);
        }
//        modelList.setBrand(null);
//        modelList.setPlatform(null);
//        modelList.setPlatformVersion(null);
//        modelList.setResolution(null);
        modelListRepository.save(modelList.copy());
        return "redirect:/domain/modellist/show/" + modelList.getPrimaryKey();
    }

    /**
     * Serves the delete form asking the user if the entity should be really deleted.
     */
    @RequestMapping(value = "delete/{pk}", method = GET)
    public String delete() {
        return "domain/modellist/delete";
    }

    /**
     * Performs the delete action and redirect to the search view.
     */
    @RequestMapping(value = "delete/{pk}", method = { PUT, POST, DELETE })
    public String delete(@ModelAttribute ModelList modelList,Model model) {
        modelListRepository.delete(modelList);
        return "redirect:/domain/modellist/search";
    }

}