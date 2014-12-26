package org.visitor.appportal.web.controller.common;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.visitor.appportal.service.newsite.VisitorProductOperationService;
import org.visitor.appportal.service.newsite.VisitorProductOrderService;
import org.visitor.appportal.service.newsite.VisitorProductService;
import org.visitor.appportal.service.newsite.redis.FloopyThingRedisService;
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
import org.visitor.appportal.web.utils.HttpClientUtil;
import org.visitor.appportal.web.utils.MixAndMatchUtils;
import org.visitor.appportal.web.utils.OrderInfo;
import org.visitor.appportal.web.utils.OrderInfo.ProductOrderStatusEnum;
import org.visitor.appportal.web.utils.OrderInfo.ProductPayOrderStatusEnum;
import org.visitor.appportal.web.utils.PaypalInfo;
import org.visitor.appportal.web.utils.WebInfo;

@Controller
@RequestMapping("/order/")
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
	@Autowired
	private FloopyThingRedisService floopyThingRedisService;
	
	@RequestMapping("calcTotalPrice")
	public void calculateTotalPrice(HttpServletRequest request,
			HttpServletResponse response) {
		Integer result = 0;
		String resultDesc = OrderInfo.PRODUCT_AMOUNT_CALC_DONE;
		ResultJson rj = new ResultJson();
		
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
						po.setOrderCurrency(product.getProductCurrency());
						
						visitorProductOrderService.saveProductOrder(po);
						orderRedisService.saveUserOrders(userTemp, po);
						orderRedisService.saveProductOrders(po);
						
						rj.setTotalPrice(resultPrice);
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
		
		rj.setResult(result);
		rj.setResultDesc(resultDesc);
		
		super.sendJSONResponse(rj, response);
	}
	
	@RequestMapping("toPayOrder/{pid}/{orderId}")
	public String toPayOrder(HttpServletRequest request,
			@PathVariable Long pid,
			@PathVariable Long orderId,
			HttpServletResponse response,
			Model model) {
		User userTemp = (User) request.getAttribute(WebInfo.UserID);
		ProductOrder po = orderRedisService.getUserOrder(userTemp, orderId);
		
		if (po != null) {
			ProductPayOrder ppo = new ProductPayOrder();
			ppo.setPayOrderOids(String.valueOf(orderId.longValue()));
			ppo.setPayStatus(ProductPayOrderStatusEnum.Init.ordinal());
			ppo.setPayOrderOwnerEmail(userTemp.getUserEmail());
			
			visitorProductOrderService.saveProductPayOrder(ppo);
			po.setOrderPayOrderId(ppo.getPayOrderId());
			po.setOrderStatus(ProductOrderStatusEnum.WaitPay.ordinal());
			visitorProductOrderService.saveProductOrder(po);
			orderRedisService.saveUserOrders(userTemp, po);
			orderRedisService.saveProductOrders(po);
			orderRedisService.saveProductPayOrderById(ppo);
			
			model.addAttribute("order", po);
			model.addAttribute("payOrder", ppo);
			String menchantId = floopyThingRedisService.getFloopyValueSingle(PaypalInfo.floopy_paypalMerchantId);
			model.addAttribute("menchantId", menchantId);
			
			return "toPayOrder";
		} else {
			//stay on the product page
			return "redirect:/day/product?pid="+pid;
		}
	}
	
	@RequestMapping(value="paypalcallback/{payorderid}",  method = { POST, PUT })
	public void paypalCallback(HttpServletRequest request,
			@PathVariable Long ppoId,
			HttpServletResponse response) {
		//get all the posted data from paypal
		String queryStr = super.getJsonStr(request); //get the raw parameterStr
		
		//immediately send the 200 response back
		try {
			response.setStatus(HttpServletResponse.SC_OK);
			PrintWriter out = response.getWriter();
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//log the received DATA from paypal
		if (log.isInfoEnabled()) {
			log.info("paypal queryStr: >" + queryStr + "<");
		}
		
		List<String> paramNameList = new ArrayList<String>();
		List<String> paramValueList = new ArrayList<String>();
		
		getParametersFromString(queryStr, paramNameList, paramValueList);
		
		if (!paramNameList.isEmpty()) {
			
			//do content verify as soon as possible
			paramNameList.add(0, "cmd");
			paramValueList.add(0, "_notify-validate");
			String paypalVerifyUrl = MixAndMatchUtils.getSystemAwsPaypalConfig(MixAndMatchUtils.paypalVerifyURL);
			
			String verifyResult = HttpClientUtil.httpPostToPaypal(paypalVerifyUrl, paramNameList, paramValueList);
			
			Map<String, String> paramMap = new HashMap<String, String>();
			for (int i = 0; i < paramNameList.size(); i ++) {
				paramMap.put(paramNameList.get(i), paramValueList.get(i));
			}
			
			String txnCheckStr = paramMap.get(PaypalInfo.txn_id);
			if (!orderRedisService.checkTxnId(txnCheckStr)) {
				ProductPayOrder ppo = orderRedisService.getProductPayOrderById(ppoId);
				setProductPayOrderInfo(ppo, paramMap, verifyResult);
			} else {
				if (log.isInfoEnabled()) {
					log.info("txn id of this request has been processed!");
				}
			}
		}
	}
	
	private String setProductPayOrderInfo(ProductPayOrder ppo,
			Map<String, String> paramMap, String verifyResult) {
		String result = "";
		
		try {
			ppo.setAddressCity(paramMap.get(PaypalInfo.address_city));
		
			ppo.setAddressCountry(paramMap.get(PaypalInfo.address_country));
			ppo.setAddressCountryCode(paramMap.get(PaypalInfo.address_country_code));
			ppo.setAddressName(paramMap.get(PaypalInfo.address_name));
			ppo.setAddressState(paramMap.get(PaypalInfo.address_state));
			ppo.setAddressStatus(paramMap.get(PaypalInfo.address_status));
			ppo.setAddressStreet(paramMap.get(PaypalInfo.address_street));
			ppo.setAddressZip(paramMap.get(PaypalInfo.address_zip));
			ppo.setCharset(paramMap.get(PaypalInfo.charset));
			ppo.setCustom(paramMap.get(PaypalInfo.custom));
			ppo.setFirstName(paramMap.get(PaypalInfo.first_name));
			ppo.setHandlingAmount(Double.valueOf(paramMap.get(PaypalInfo.handling_amount)));
			ppo.setItemName(paramMap.get(PaypalInfo.item_name));
			ppo.setItemNumber(paramMap.get(PaypalInfo.item_number));
			ppo.setLastName(paramMap.get(PaypalInfo.last_name));
			ppo.setMcCurrency(paramMap.get(PaypalInfo.mc_currency));
			ppo.setMcFee(Double.valueOf(paramMap.get(PaypalInfo.mc_fee)));
			ppo.setMcGross(Double.valueOf(paramMap.get(PaypalInfo.mc_gross)));
			ppo.setNotifyVersion(paramMap.get(PaypalInfo.notify_version));
			ppo.setPayerEmail(paramMap.get(PaypalInfo.payer_email));
			ppo.setPayerId(paramMap.get(PaypalInfo.payer_id));
			ppo.setPayerStatus(paramMap.get(PaypalInfo.payer_status));
			
			String paymentDateOri = paramMap.get(PaypalInfo.payment_date);
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss MMM dd, yyyy PST");
			Date paymentDate = sdf.parse(paymentDateOri);
			
			ppo.setPaymentDate(paymentDate);
			ppo.setPaymentFee(Double.valueOf(paramMap.get(PaypalInfo.payment_fee)));
			ppo.setPaymentGross(Double.valueOf(paramMap.get(PaypalInfo.payment_gross)));
			ppo.setPaymentStatus(paramMap.get(PaypalInfo.payment_status));
			ppo.setPaymentType(paramMap.get(PaypalInfo.payment_type));
			ppo.setProtectionEligibility(paramMap.get(PaypalInfo.protection_eligibility));
			ppo.setQuantity(Integer.valueOf(paramMap.get(PaypalInfo.quantity)));
			ppo.setReceiverEmail(paramMap.get(PaypalInfo.receiver_email));
			ppo.setReceiverId(paramMap.get(PaypalInfo.receiver_id));
			ppo.setResidenceCountry(paramMap.get(PaypalInfo.residence_country));
			ppo.setShipping(Double.valueOf(paramMap.get(PaypalInfo.shipping)));
			ppo.setTax(Double.valueOf(paramMap.get(PaypalInfo.tax)));
			ppo.setTestIpn(Integer.valueOf(paramMap.get(PaypalInfo.test_ipn)));
			ppo.setTransactionSubject(paramMap.get(PaypalInfo.transaction_subject));
			ppo.setTxnId(paramMap.get(PaypalInfo.txn_id));
			ppo.setTxnType(paramMap.get(PaypalInfo.txn_type));
			ppo.setVerifySign(paramMap.get(PaypalInfo.verify_sign));
			
			result = ppo.getPaymentStatus();
			if (StringUtils.isNotEmpty(verifyResult) &&
					StringUtils.equals(verifyResult, PaypalInfo.status_paymentVerified) &&
					StringUtils.isNotEmpty(result) && 
					StringUtils.equals(result, PaypalInfo.status_paymentCompleted)) { //Completed-----every thing looks fine
				checkOrder(ppo); //check and update actual order
			} else { //payment try invalid
				ppo.setPayStatus(ProductPayOrderStatusEnum.Invalid.ordinal());
				visitorProductOrderService.saveProductPayOrder(ppo);
				orderRedisService.deleteProductPayOrderById(ppo);
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	private void checkOrder(ProductPayOrder ppo) {
		String merchantEmail = floopyThingRedisService.getFloopyValueSingle(PaypalInfo.floopy_paypalMerchantEmail);
		
		String orderOwnerEmail = ppo.getPayOrderOwnerEmail();
		User user = userRedisService.getUserPassword(orderOwnerEmail);
		
		Long orderId = Long.valueOf(ppo.getPayOrderOids());
		ProductOrder po = orderRedisService.getUserOrder(user, orderId);
		
		if (StringUtils.equals(merchantEmail, ppo.getReceiverEmail()) &&
				StringUtils.equals(po.getOrderCurrency(), ppo.getMcCurrency())) {
			if (po.getOrderTotalAmount().equals(ppo.getMcGross())) {
				//update product order to paid
				ppo.setPayStatus(ProductPayOrderStatusEnum.Completed.ordinal());
				
				po.setOrderStatus(ProductOrderStatusEnum.PAID.ordinal());
				
				visitorProductOrderService.saveProductOrder(po);
				orderRedisService.saveProductOrders(po);
				orderRedisService.saveUserOrders(user, po);
			} else if (po.getOrderTotalAmount().compareTo(ppo.getMcGross()) > 0 &&
					ppo.getMcGross().compareTo(new Double(0)) > 0) {
				//update product order to partially paid
				ppo.setPayStatus(ProductPayOrderStatusEnum.Completed.ordinal());
				po.setOrderStatus(ProductOrderStatusEnum.PARTIAL_PAID.ordinal());
				po.setOrderRemainAmount(po.getOrderTotalAmount()-ppo.getMcGross());
				
				visitorProductOrderService.saveProductOrder(po);
				orderRedisService.saveProductOrders(po);
				orderRedisService.saveUserOrders(user, po);
			} else {
				ppo.setPayStatus(ProductPayOrderStatusEnum.Invalid.ordinal());
			}
		} else {
			ppo.setPayStatus(ProductPayOrderStatusEnum.Invalid.ordinal());
		}
		visitorProductOrderService.saveProductPayOrder(ppo);
		orderRedisService.saveProductPayOrderById(ppo);
	}
	
	private void getParametersFromString(String oriStr,
			List<String> paramNameList,
			List<String> paramValueList) {
		String firstArray[] = oriStr.split("&");
		if (firstArray.length > 0) {
			
			try {
				for (int i = 0; i < firstArray.length; i ++) {
					String secondOne[] = firstArray[i].split("=");
					
					if (secondOne.length != 2) {
						paramNameList.clear();
						paramValueList.clear();
						break;
					} else {
						paramNameList.add(secondOne[0]);
						paramValueList.add(URLDecoder.decode(secondOne[1], "UTF-8"));
						
					}
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		//String a = "a=8&b=7&aaa=65&rere=dsfdsd";
		List<String> paramNameList = new ArrayList<String>();
		List<String> paramValueList = new ArrayList<String>();
		
		//getParametersFromString(a, paramNameList, paramValueList);
		for (String name : paramNameList) {
			System.out.println(">"+name+"<");
		}
		System.out.println("----------");
		for (String value : paramValueList) {
			System.out.println(">"+value+"<");
		}
	}
}
