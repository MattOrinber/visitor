package org.visitor.apportal.web.web.order;

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
import org.visitor.apportal.web.service.order.ProductService;
import org.visitor.apportal.web.util.Const;
import org.visitor.appportal.visitor.domain.Product;
import org.visitor.appportal.web.utils.ProductInfo;

import com.google.common.collect.Maps;

@Controller
@RequestMapping("/product")
public class ProductController {
	
	private static Map<String, String> allAvailableTypes = Maps.newHashMap();
	private static Map<String, String> allStatus = Maps.newHashMap();

	static {
		/*allAvailableTypes.put(ProductInfo.EDIT_STATUS.toString(), ProductInfo.EDIT_STATUS.g);
		allAvailableTypes.put("1", "Facebook User");*/
		
		for (ProductInfo.StatusTypeEnum type : ProductInfo.StatusTypeEnum.values()) {
			allStatus.put(type.getValue().toString(), type.getDisplayName());
		}
		
		for (ProductInfo.ProductAvailableTypeEnum availableTypeEnum : ProductInfo.ProductAvailableTypeEnum.values()) {
			allAvailableTypes.put(availableTypeEnum.getValue().toString(), availableTypeEnum.getDisplayName());
		}
		allAvailableTypes.put(ProductInfo.AVAILABLE_TYPE_DEFAULT.toString(), "default");
	}

	@Autowired
	private ProductService service;

	@RequestMapping(value = {"", "/list"})
	public String list(
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			Model model, ServletRequest request) {

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(
				request, "search_");
		Page<Product> products = service.getProduct(searchParams, pageNumber,
				Const.PAGE_SIZE);

		model.addAttribute("products", products);
		model.addAttribute("searchParams", Servlets
				.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "product/productList";
	}

	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		model.addAttribute("product", new Product());
		model.addAttribute("action", "create");
		model.addAttribute("allAvailableTypes", allAvailableTypes);
		model.addAttribute("allStatus", allStatus);
		return "product/productForm";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid Product newProduct,
			RedirectAttributes redirectAttributes) {
		service.saveProduct(newProduct);
		redirectAttributes.addFlashAttribute("message", "创建成功");
		return "redirect:/product/";
	}

	@RequestMapping(value = "update/{product_id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("product_id") Long product_id,
			Model model) {
		model.addAttribute("product", service.getProduct(product_id));
		model.addAttribute("allAvailableTypes", allAvailableTypes);
		model.addAttribute("allStatus", allStatus);
		model.addAttribute("action", "update");
		return "product/productForm";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(
			@Valid @ModelAttribute("preloadProduct") Product product,
			RedirectAttributes redirectAttributes) {
		service.saveProduct(product);
		redirectAttributes.addFlashAttribute("message", "更新成功");
		return "redirect:/product/";
	}

	@RequestMapping(value = "delete/{product_id}")
	public String delete(@PathVariable("product_id") Long product_id,
			RedirectAttributes redirectAttributes) {
		service.deleteProduct(product_id);
		redirectAttributes.addFlashAttribute("message", "删除成功");
		return "redirect:/product";
	}

	@ModelAttribute("preloadProduct")
	public Product getProduct(
			@RequestParam(value = "productId", required = false) Long id) {
		if (id != null) {
			return service.getProduct(id);
		}
		return null;
	}

}
