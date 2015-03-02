package org.visitor.apportal.web.web.order;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;
import org.visitor.apportal.web.entity.Test;
import org.visitor.apportal.web.entity.order.OrderDetailVO;
import org.visitor.apportal.web.entity.order.OrderVO;
import org.visitor.apportal.web.service.order.OrderService;
import org.visitor.apportal.web.util.Const;
import org.visitor.appportal.web.utils.OrderInfo;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private OrderService service;

	@RequestMapping(value = {"", "/list"})
	public String list(
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			@RequestParam(value = "status", defaultValue = "0") int status,
			Model model, ServletRequest request) {

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(
				request, "search_");
		if (status > 0) {
			searchParams.put("status", status);
		}

		Page<OrderVO> orders = service.getOrder(searchParams, pageNumber,
				Const.PAGE_SIZE);

		model.addAttribute("orders", orders);
		model.addAttribute("searchParams", Servlets
				.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "order/orderList";
	}

	@RequestMapping(value = "update/{orderId}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("orderId") Long id, Model model) {
		model.addAttribute("detail", service.getOrderDetail(id));
		model.addAttribute("action", "update");
		return "order/orderForm";
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@Valid @ModelAttribute("test") Test test,
			BindingResult result, Model model,
			RedirectAttributes redirectAttributes) {

		return "redirect:/order/";
	}

	@RequestMapping(value = "fulfill", method = RequestMethod.POST)
	public String fulfill(OrderDetailVO detail, Model model,
			RedirectAttributes redirectAttributes) {
		if (detail.getOrderId() > 0) {
			detail.setOrderStatus(OrderInfo.ProductOrderStatusEnum.REFUNDED
					.getValue());// REFUNED ORDERED
			service.saveStatus(detail);
			redirectAttributes.addFlashAttribute("message", "订单支付成功");
		} else {
			redirectAttributes.addFlashAttribute("message", "订单修改失败，请刷新重试");
		}
		return "redirect:/order/detail/" + detail.getOrderId();
	}

}
