package org.visitor.apportal.web.web.config;

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;
import org.visitor.apportal.web.service.config.ContainerService;
import org.visitor.apportal.web.util.Const;
import org.visitor.appportal.visitor.domain.Container;

@Controller
@RequestMapping("/container")
public class ContainerController {

	@Autowired
	private ContainerService service;

	@RequestMapping(value = {"", "/list"})
	public String list(
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			Model model, ServletRequest request) {

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(
				request, "search_");
		Page<Container> containers = service.getContainer(searchParams,
				pageNumber, Const.PAGE_SIZE);

		model.addAttribute("containers", containers);
		model.addAttribute("searchParams", Servlets
				.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "container/containerList";
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("container", new Container());
		model.addAttribute("action", "create");
		return "container/containerForm";
	}

	@RequiresRoles("admin")
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid Container newContainer,
			BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			model.addAttribute("action", "create");
			return "container/containerForm";
		}
		service.saveContainer(newContainer);
		redirectAttributes.addFlashAttribute("message", "创建成功");
		return "redirect:/container/";
	}

	@RequestMapping(value = "update/{container_id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("container_id") Long container_id,
			Model model) {
		model.addAttribute("container", service.getContainer(container_id));
		model.addAttribute("action", "update");
		return "container/containerForm";
	}

	@RequiresRoles("admin")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(
			@Valid @ModelAttribute("preloadContainer") Container container,
			BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			model.addAttribute("action", "update");
			return "container/containerForm";
		}
		service.saveContainer(container);
		redirectAttributes.addFlashAttribute("message", "更新成功");
		return "redirect:/container/";
	}

	@RequiresRoles("admin")
	@RequestMapping(value = "delete/{container_id}/{container_name}")
	public String delete(@PathVariable("container_id") Long container_id,
			@PathVariable("container_name") String container_name,
			RedirectAttributes redirectAttributes) {
		service.deleteContainer(container_id, container_name);
		redirectAttributes.addFlashAttribute("message", "删除成功");
		return "redirect:/container";
	}

	@ModelAttribute("preloadContainer")
	public Container getContainer(
			@RequestParam(value = "containerId", required = false) Long container_id) {
		if (container_id != null) {
			return service.getContainer(container_id);
		}
		return null;
	}

}
