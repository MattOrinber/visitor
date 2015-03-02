package org.visitor.apportal.web.web.order;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;
import org.visitor.apportal.web.service.order.CustomerService;
import org.visitor.apportal.web.util.Const;
import org.visitor.appportal.visitor.domain.TimeZone;
import org.visitor.appportal.visitor.domain.User;

import com.google.common.collect.Maps;

@Controller
@RequestMapping("/customer")
public class CustomerController {

	private static Map<String, String> allUserTypes = Maps.newHashMap();
	private static Map<String, String> allStatus = Maps.newHashMap();
	private static Map<String, String> allGenders = Maps.newHashMap();

	static {
		allStatus.put("0", "有效");
		allStatus.put("1", "无效");

		allGenders.put("0", "male");
		allGenders.put("1", "female");

		allUserTypes.put("0", "Normal User");
		allUserTypes.put("1", "Facebook User");
	}

	@Autowired
	private CustomerService service;

	@RequestMapping(value = {"", "/list"})
	public String list(
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			Model model, ServletRequest request) {

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(
				request, "search_");
		Page<User> users = service.getUser(searchParams, pageNumber,
				Const.PAGE_SIZE);

		model.addAttribute("users", users);
		model.addAttribute("allStatus", allStatus);
		model.addAttribute("searchParams", Servlets
				.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "user/userList";
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("action", "create");
		return "user/userForm";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid User newUser,
			RedirectAttributes redirectAttributes) {
		service.saveUser(newUser);
		redirectAttributes.addFlashAttribute("message", "创建成功");
		return "redirect:/customer/";
	}

	@RequestMapping(value = "update/{user_id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("user_id") Long user_id, Model model) {
		List<TimeZone> allTimeZones = service.getAllTimeZone();
		
		model.addAttribute("user", service.getUser(user_id));
		model.addAttribute("allTimeZones", allTimeZones);
		model.addAttribute("allUserTypes", allUserTypes);
		model.addAttribute("allStatus", allStatus);
		model.addAttribute("allGenders", allGenders);
		model.addAttribute("action", "update");
		return "user/userForm";
	}
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("preloadUser") User user,
			RedirectAttributes redirectAttributes) {
		service.saveUser(user);
		redirectAttributes.addFlashAttribute("message", "更新成功");
		return "redirect:/customer/";
	}

	@RequestMapping(value = "delete/{user_id}")
	public String delete(@PathVariable("user_id") Long user_id,
			RedirectAttributes redirectAttributes) {
		service.deleteUser(user_id);
		redirectAttributes.addFlashAttribute("message", "删除成功");
		return "redirect:/user";
	}

	@ModelAttribute("preloadUser")
	public User getUser(
			@RequestParam(value = "userId", required = false) Long userId) {
		if (userId != null) {
			return service.getUser(userId);
		}
		return null;
	}

}
