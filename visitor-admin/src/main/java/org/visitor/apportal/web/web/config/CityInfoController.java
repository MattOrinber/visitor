package org.visitor.apportal.web.web.config;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.web.Servlets;
import org.visitor.apportal.web.service.config.CityInfoService;
import org.visitor.apportal.web.util.Const;
import org.visitor.apportal.web.vo.DataTableParameter;
import org.visitor.appportal.visitor.domain.City;

import com.google.common.collect.Maps;

@Controller
@RequestMapping("/city")
public class CityInfoController {

	@Autowired
	private CityInfoService service;

	private static Map<String, String> allStatus = Maps.newHashMap();

	static {
		allStatus.put("0", "有效");
		allStatus.put("1", "无效");
	}

	@RequestMapping(value = {"", "/list"})
	public String list(
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			Model model, ServletRequest request) {

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(
				request, "search_");
		Page<City> city_infos = service.getCity_info(searchParams, pageNumber,
				Const.PAGE_SIZE);

		model.addAttribute("city_infos", city_infos);
		model.addAttribute("allStatus", allStatus);
		model.addAttribute("searchParams", Servlets
				.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "city_info/city_infoList";
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("city_info", new City());
		model.addAttribute("action", "create");
		model.addAttribute("allStatus", allStatus);
		return "city_info/city_infoForm";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid City newCity_info, BindingResult result,
			Model model, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			model.addAttribute("allStatus", allStatus);
		}

		service.saveCity_info(newCity_info);
		redirectAttributes.addFlashAttribute("message", "创建成功");
		return "redirect:/city/";
	}

	@RequestMapping(value = "update/{city_id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("city_id") Long city_id, Model model) {
		model.addAttribute("city_info", service.getCity_info(city_id));
		model.addAttribute("allStatus", allStatus);
		model.addAttribute("action", "update");
		return "city_info/city_infoForm";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(
			@Valid @ModelAttribute("preloadCity_info") City city_info,
			BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			model.addAttribute("allStatus", allStatus);
		}
		service.saveCity_info(city_info);
		redirectAttributes.addFlashAttribute("message", "更新成功");
		return "redirect:/city/";
	}

	@RequestMapping(value = "delete/{city_id}")
	public String delete(@PathVariable("city_id") Long city_id,
			RedirectAttributes redirectAttributes) {
		service.deleteCity_info(city_id);
		redirectAttributes.addFlashAttribute("message", "删除成功");
		return "redirect:/city/";
	}

	@RequestMapping(value = "getAllCity")
	@ResponseBody
	public String getAllCity() {
		JsonMapper mapper = new JsonMapper();
		List<City> city_infos = service.getAllCity_info();
		DataTableParameter<City> dtp = new DataTableParameter<City>(city_infos);
		System.out.println(mapper.toJson(dtp));
		return mapper.toJson(dtp);
	}

	@ModelAttribute("preloadCity_info")
	public City getCity_info(
			@RequestParam(value = "cityId", required = false) Long city_id) {
		if (city_id != null) {
			return service.getCity_info(city_id);
		}
		return null;
	}

}
