package org.visitor.appportal.web.controller.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.visitor.appportal.service.newsite.VisitorProductOperationService;
import org.visitor.appportal.service.newsite.VisitorProductOrderService;
import org.visitor.appportal.service.newsite.VisitorProductService;
import org.visitor.appportal.service.newsite.redis.OrderRedisService;
import org.visitor.appportal.service.newsite.redis.ProductRedisService;
import org.visitor.appportal.service.newsite.redis.UserRedisService;
import org.visitor.appportal.visitor.beans.BuyTemp;
import org.visitor.appportal.visitor.beans.ResultJson;
import org.visitor.appportal.visitor.domain.Product;
import org.visitor.appportal.visitor.domain.ProductOperation;
import org.visitor.appportal.visitor.domain.ProductOrder;
import org.visitor.appportal.visitor.domain.ProductPayOrder;
import org.visitor.appportal.visitor.domain.User;
import org.visitor.appportal.web.utils.MixAndMatchUtils;
import org.visitor.appportal.web.utils.OrderInfo;
import org.visitor.appportal.web.utils.OrderInfo.ProductOrderStatusEnum;
import org.visitor.appportal.web.utils.OrderInfo.ProductPayOrderStatusEnum;
import org.visitor.appportal.web.utils.ProductInfo;
import org.visitor.appportal.web.utils.WebInfo;

@Controller
@RequestMapping("order")
public class OrderController extends BasicController {
	protected static final Logger log = LoggerFactory.getLogger(OrderController.class);
	
	@Autowired
	private OrderRedisService orderRedisService;
	@Autowired
	private VisitorProductOrderService visitorProductOrderService;
	@Autowired
	private VisitorProductOperationService visitorProductOperationService;
	@Autowired
	private VisitorProductService visitorProductService;
	@Autowired
	private ProductRedisService productRedisService;
	@Autowired
	private UserRedisService userRedisService;
	
	@RequestMapping("/calcTotalPrice")
	public void calculateTotalPrice(HttpServletRequest request,
			HttpServletResponse response) {
		Integer result = 0;
		String resultDesc = OrderInfo.PRODUCT_AMOUNT_CALC_DONE;
		
		BuyTemp btTemp = super.getBuyTempJSON(request);
		User userTemp = (User) request.getAttribute(WebInfo.UserID);
		
		if (btTemp != null) {
			try {
				String pidStr = btTemp.getProductIdStr();
				String startDateStr = btTemp.getStartDate();
				String endDateStr = btTemp.getEndDate();
				
				if (StringUtils.isNotEmpty(pidStr) &&
						StringUtils.isNotEmpty(startDateStr) &&
						StringUtils.isNotEmpty(endDateStr)) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
					Date startDateT = sdf.parse(startDateStr);
					Date endDateT = sdf.parse(endDateStr);
					DateTime dtStart = new DateTime(startDateT);
					DateTime dtEnd = new DateTime(endDateT);
					
					int daysCount = 0;
					if (startDateT.before(endDateT)) {
						daysCount = dtEnd.getDayOfMonth() - dtStart.getDayOfMonth();
					} else {
						daysCount = 1;
					}
					
					//use product operation to select the special price set and calculate
					List<ProductOperation> list = new ArrayList<ProductOperation>();
					list = productRedisService.getProductOperationList(Long.valueOf(pidStr));
					Product product = productRedisService.getProductFromRedis(Long.valueOf(pidStr));
					
					Double resultPrice = MixAndMatchUtils.calculatePrice(daysCount, dtStart, dtEnd, list, product);
					
					if (resultPrice.compareTo(new Double(0)) > 0) {
						//generate a product order and ready for paypal order generation
						ProductOrder po = new ProductOrder();
						Date currentDate = new Date();
						po.setOrderCreateDate(new Date());
						po.setOrderStartDate(startDateT);
						po.setOrderEndDate(endDateT);
						po.setOrderProductId(product.getProductId());
						po.setOrderUpdateDate(currentDate);
						po.setOrderUserEmail(userTemp.getUserEmail());
						po.setOrderTotalAmount(resultPrice);
						po.setOrderStatus(ProductOrderStatusEnum.Init.ordinal());
						
						visitorProductOrderService.saveProductOrder(po);
						orderRedisService.saveUserOrders(userTemp, po);
						orderRedisService.saveProductOrders(po);
					}
				} else {
					result = -1;
					resultDesc = OrderInfo.PRODUCT_BUY_TEMP_NOT_RIGHT;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			result = -1;
			resultDesc = OrderInfo.PRODUCT_BUY_TEMP_NOT_RIGHT;
		}
		
		ResultJson rj = new ResultJson();
		rj.setResult(result);
		rj.setResultDesc(resultDesc);
		
		super.sendJSONResponse(rj, response);
	}
	
	@RequestMapping("/toPayOrder/{pid}/{orderId}")
	public String toPayOrder(HttpServletRequest request,
			@PathVariable Long orderId,
			@PathVariable Long pid,
			HttpServletResponse response) {
		User userTemp = (User) request.getAttribute(WebInfo.UserID);
		ProductOrder po = orderRedisService.getUserOrder(userTemp, orderId);
		
		if (po != null) {
			ProductPayOrder ppo = new ProductPayOrder();
			ppo.setPayOrderOids(String.valueOf(orderId.longValue()));
			ppo.setPayStatus(ProductPayOrderStatusEnum.Init.ordinal());
			
			visitorProductOrderService.saveProductPayOrder(ppo);
			po.setOrderPayOrderId(ppo.getPayOrderId());
			po.setOrderStatus(ProductOrderStatusEnum.WaitPay.ordinal());
			visitorProductOrderService.saveProductOrder(po);
			orderRedisService.saveUserOrders(userTemp, po);
			orderRedisService.saveProductOrders(po);
			orderRedisService.saveProductPayOrderById(ppo);
			return "/toPayOrder";
		} else {
			//stay on the product page
			return "redirect:/day/product?pid="+pid;
		}
	}
}
