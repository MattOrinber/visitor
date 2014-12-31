package org.visitor.appportal.web.controller.common;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.visitor.appportal.service.newsite.VisitorFloopyThingService;
import org.visitor.appportal.service.newsite.VisitorProductOperationService;
import org.visitor.appportal.service.newsite.VisitorProductOrderService;
import org.visitor.appportal.service.newsite.VisitorProductService;
import org.visitor.appportal.service.newsite.redis.FloopyThingRedisService;
import org.visitor.appportal.service.newsite.redis.OrderRedisService;
import org.visitor.appportal.service.newsite.redis.ProductRedisService;
import org.visitor.appportal.service.newsite.redis.UserRedisService;
import org.visitor.appportal.visitor.beans.BuyTemp;
import org.visitor.appportal.visitor.beans.ResultJson;
import org.visitor.appportal.visitor.domain.FloopyThing;
import org.visitor.appportal.visitor.domain.Product;
import org.visitor.appportal.visitor.domain.ProductMultiPrice;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

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
	@Autowired
	private VisitorFloopyThingService visitorFloopyThingService;
	
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
						po.setOrderRemainAmount(resultPrice);
						po.setOrderCurrency(product.getProductCurrency());
						po.setOrderStatus(ProductOrderStatusEnum.Init.ordinal());
						visitorProductOrderService.saveProductOrder(po);
						
						ProductPayOrder ppo = new ProductPayOrder();
						ppo.setPayOrderOids(String.valueOf(po.getOrderId().longValue()));
						ppo.setPayStatus(ProductPayOrderStatusEnum.Init.ordinal());
						ppo.setPayOrderOwnerEmail(userTemp.getUserEmail());
						
						visitorProductOrderService.saveProductPayOrder(ppo);
						po.setOrderPayOrderId(ppo.getPayOrderId());
						po.setOrderStatus(ProductOrderStatusEnum.WaitPay.ordinal());
						visitorProductOrderService.saveProductOrder(po);
						orderRedisService.saveUserOrders(userTemp, po);
						orderRedisService.saveProductOrders(po);
						orderRedisService.saveProductPayOrderById(ppo);
						
						rj.setTotalPrice(resultPrice);
						rj.setProductId(product.getProductId());
						rj.setOrderId(po.getOrderId());
						rj.setPayOrderId(ppo.getPayOrderId());
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
	
	@RequestMapping("addToPrice")
	public void addToTotalPrice(HttpServletRequest request,
			HttpServletResponse response) {
		Integer result = 0;
		String resultDesc = OrderInfo.PRODUCT_AMOUNT_CALC_DONE;
		ResultJson rj = new ResultJson();
		
		BuyTemp btTemp = super.getBuyTempJSON(request);
		User userTemp = (User) request.getAttribute(WebInfo.UserID);
		
		if (btTemp != null) {
			try {
				String orderIdStr = btTemp.getOrderIdStr();
				String priceIdStr = btTemp.getPriceIdStr();
				
				if (StringUtils.isNotEmpty(orderIdStr) &&
						StringUtils.isNotEmpty(priceIdStr)) {
					ProductOrder po = orderRedisService.getUserOrder(userTemp, Long.valueOf(orderIdStr));
					ProductMultiPrice pmp = productRedisService.getProductMultiPriceSetByProductIdAndKey(po.getOrderProductId(), priceIdStr);
					
					Double pmpPrice = pmp.getPmpProductPriceValue();
					
					Double finalPrice = po.getOrderTotalAmount() + pmpPrice;
					po.setOrderTotalAmount(finalPrice);
					
					visitorProductOrderService.saveProductOrder(po);
					orderRedisService.saveUserOrders(userTemp, po);
					orderRedisService.saveProductOrders(po);
					
					rj.setPoPrice(finalPrice);
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
	
	//paypal express checkout pay order utilities
	@RequestMapping(value="expressCheckout/{poId}/{ppoId}")
	public String expressCheckout(HttpServletRequest request,
			@PathVariable Long poId,
			@PathVariable Long ppoId,
			HttpServletResponse response) throws UnsupportedEncodingException {
		String info = "success";
		
		User userTemp = (User) request.getAttribute(WebInfo.UserID);
		
		ProductOrder po = orderRedisService.getUserOrder(userTemp, poId);
		ProductPayOrder ppo = orderRedisService.getProductPayOrderById(ppoId);
		
		String targetECURL = floopyThingRedisService.getFloopyValueSingle(PaypalInfo.paypalExpressCheckoutURL);
		String redirectECURL = floopyThingRedisService.getFloopyValueSingle(PaypalInfo.paypalExpressCheckoutRedirectRUL);
		
		String redirectURLFinal = "";
		//cmd=_express-checkout&token=
		
		//set expressCheckout
		String ECUSER = floopyThingRedisService.getFloopyValueSingle(PaypalInfo.paypalExpressCheckoutUser);
		String ECPWD = floopyThingRedisService.getFloopyValueSingle(PaypalInfo.paypalExpressCheckoutPassword);
		String ECSIGNATURE = floopyThingRedisService.getFloopyValueSingle(PaypalInfo.paypalExpressCheckoutSignature);
		String ECVERSION = floopyThingRedisService.getFloopyValueSingle(PaypalInfo.paypalExpressCheckoutVersion);
		
		String ECMETHOD = floopyThingRedisService.getFloopyValueSingle(PaypalInfo.paypalExpressCheckoutMethodSet);
		
		String ECRETURNURL = floopyThingRedisService.getFloopyValueSingle(PaypalInfo.floopy_paypalCallBackURL) +"/ecReturn/"+String.valueOf(po.getOrderId().doubleValue()) + String.valueOf(ppo.getPayOrderId().doubleValue());
		String ECCANCELURL = floopyThingRedisService.getFloopyValueSingle(PaypalInfo.floopy_paypalCallBackURL) +"/ecCancel/"+String.valueOf(po.getOrderId().doubleValue()) + String.valueOf(ppo.getPayOrderId().doubleValue());
		
		String p_ECUSER = PaypalInfo.pec_p_user;
		String p_ECPWD = PaypalInfo.pec_p_password;
		String p_ECSIGNATURE = PaypalInfo.pec_p_signature;
		String p_ECVERSION = PaypalInfo.pec_p_version;
		
		String p_ECMETHOD = PaypalInfo.pec_p_method;
		String p_ECAMOUNT = PaypalInfo.pec_p_amount;
		String p_ECPAYMENTACTION = PaypalInfo.pec_p_paymentaction; //Sale
		String p_ECCURRENCYCODE = PaypalInfo.pec_p_currencycode; //
		
		String p_ECRETURNURL = PaypalInfo.pec_p_returnurl;
		String p_ECCANCELURL = PaypalInfo.pec_p_cancelurl;
		
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put(p_ECUSER, ECUSER);
		paramMap.put(p_ECPWD, ECPWD);
		paramMap.put(p_ECSIGNATURE, ECSIGNATURE);
		paramMap.put(p_ECVERSION, ECVERSION);
		paramMap.put(p_ECMETHOD, ECMETHOD);
		paramMap.put(p_ECAMOUNT, String.valueOf(po.getOrderRemainAmount()));
		paramMap.put(p_ECPAYMENTACTION, PaypalInfo.pec_p_fix_action);
		paramMap.put(p_ECCURRENCYCODE, po.getOrderCurrency());
		paramMap.put(p_ECRETURNURL, ECRETURNURL);
		paramMap.put(p_ECCANCELURL, ECCANCELURL);
		
		String paramFinal = "?";
		int idx = 0;
		for (String keyT : paramMap.keySet()) {
			String finalValue = URLEncoder.encode(paramMap.get(keyT), "UTF-8");
			if (idx == 0) {
				paramFinal = paramFinal + keyT + "=" + finalValue;
				idx ++;
			} else {
				paramFinal = paramFinal + "&" + keyT + "=" + finalValue;
			}
		}
		
		String setExpressCheckoutURL = targetECURL + paramFinal;
		
		String oriJsonResponseStr = HttpClientUtil.httpGetJSON(setExpressCheckoutURL);
		
		Map<String, String> jsonResponseOri = JSON.parseObject(oriJsonResponseStr, new TypeReference<Map<String, String>>(){});
		
		boolean ifCorrect = false;
		if (jsonResponseOri.size() > 0) {
			String r_ECACK = PaypalInfo.pec_r_ack;
			String r_ECTOKEN = PaypalInfo.pec_r_token;
			
			if (jsonResponseOri.containsKey(r_ECACK) && jsonResponseOri.containsKey(r_ECTOKEN)) {
				String ackStr = jsonResponseOri.get(r_ECACK);
				String tokenStr = jsonResponseOri.get(r_ECTOKEN);
				
				if (StringUtils.equals(ackStr, PaypalInfo.pec_r_fix_ack)) {
					// do redirect call
					if (StringUtils.isNotEmpty(tokenStr)) {
						orderRedisService.setPayPalTokenUser(userTemp, tokenStr);
						redirectURLFinal = "redirect:" + redirectECURL + "?" + r_ECTOKEN + "=" + tokenStr;
						ppo.setCustom(tokenStr);
						
						visitorProductOrderService.saveProductPayOrder(ppo);
						orderRedisService.saveProductPayOrderById(ppo);
						
						ifCorrect = true;
					} 
				} 
			} 
		} 
		
		if (!ifCorrect) {
			log.info("paypal response not right: >"+oriJsonResponseStr+"<");
			info = "Payment failed due to network error, please init payment again";
			redirectURLFinal = "redirect:/day/result?info=" + URLEncoder.encode(info, "UTF-8");
		}
		//DoExpressCheckoutPayment like the set above
		
		return redirectURLFinal;
	}
	
	@RequestMapping(value="ecReturn/{poId}/{ppoId}")
	public String expressCheckoutReturn(HttpServletRequest request,
			@PathVariable Long poId,
			@PathVariable Long ppoId,
			@RequestParam(value = "TOKEN", required = true) String tokenPassed,
			HttpServletResponse response) throws UnsupportedEncodingException {
		String redirectURLFinal = "";
		
		User userTemp = orderRedisService.getPayPalTokenUser(tokenPassed);
		
		ProductOrder po = orderRedisService.getUserOrder(userTemp, poId);
		ProductPayOrder ppo = orderRedisService.getProductPayOrderById(ppoId);
		
		if (userTemp != null && po != null && ppo != null && StringUtils.equals(tokenPassed, ppo.getCustom())) {
		
			String targetECURL = floopyThingRedisService.getFloopyValueSingle(PaypalInfo.paypalExpressCheckoutURL);
			
			String ECMETHOD = floopyThingRedisService.getFloopyValueSingle(PaypalInfo.paypalExpressCheckoutMethodDetail);
			String ECTOKEN = tokenPassed;
			
			String p_ECMETHOD = PaypalInfo.pec_p_method;
			String p_ECTOKEN = PaypalInfo.pec_r_token;
			
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put(p_ECMETHOD, ECMETHOD);
			paramMap.put(p_ECTOKEN, ECTOKEN);
			
			String paramFinal = "?";
			int idx = 0;
			for (String keyT : paramMap.keySet()) {
				String finalValue = URLEncoder.encode(paramMap.get(keyT), "UTF-8");
				if (idx == 0) {
					paramFinal = paramFinal + keyT + "=" + finalValue;
					idx ++;
				} else {
					paramFinal = paramFinal + "&" + keyT + "=" + finalValue;
				}
			}
			
			String setExpressCheckoutURL = targetECURL + paramFinal;
			
			String oriJsonResponseStr = HttpClientUtil.httpGetJSON(setExpressCheckoutURL);
			
			if (StringUtils.isNotEmpty(oriJsonResponseStr)) {
			
				Map<String, String> jsonResponseOri = JSON.parseObject(oriJsonResponseStr, new TypeReference<Map<String, String>>(){});
				
				boolean ifCorrect = false;
				if (jsonResponseOri.size() > 0) {
					String r_ECTOKEN = PaypalInfo.pec_r_token;
					
					if (jsonResponseOri.containsKey(r_ECTOKEN)) {
						String tokenStr = jsonResponseOri.get(r_ECTOKEN);
						
						if (StringUtils.equals(tokenStr, tokenPassed)) {
							// get the information and update pay order
							boolean ifErrorOccured = false;
							String checkoutErrorCodeStr = jsonResponseOri.get(PaypalInfo.pec_d_error_code);
							if (StringUtils.isNotEmpty(checkoutErrorCodeStr)) {
								ppo.setCheckoutErrorCode(checkoutErrorCodeStr);
								ifErrorOccured = true;
							}
							
							String coErrorShortMsg = jsonResponseOri.get(PaypalInfo.pec_d_error_short_message);
							if (StringUtils.isNotEmpty(coErrorShortMsg)) {
								ppo.setCheckoutErrorShortmsg(coErrorShortMsg);
							}
							
							if (!ifErrorOccured) {
							
								String checkoutStatusStr = jsonResponseOri.get(PaypalInfo.pec_d_checkoutstatus);
								if (StringUtils.isNotEmpty(checkoutStatusStr)) {
									ppo.setCheckoutStatus(checkoutStatusStr);
								}
								
								String buyerEmailStr = jsonResponseOri.get(PaypalInfo.pec_d_email);
								if (StringUtils.isNotEmpty(buyerEmailStr)) {
									ppo.setPayerEmail(buyerEmailStr);
								}
								
								String payerIdStr = jsonResponseOri.get(PaypalInfo.pec_d_payerid);
								if (StringUtils.isNotEmpty(payerIdStr)) {
									ppo.setPayerId(payerIdStr);
								}
								
								String payerStatusStr = jsonResponseOri.get(PaypalInfo.pec_d_payerstatus);
								if (StringUtils.isNotEmpty(payerStatusStr)) {
									ppo.setPayerStatus(payerStatusStr);
								}
								
								String countryCodeStr = jsonResponseOri.get(PaypalInfo.pec_d_countrycode);
								if (StringUtils.isNotEmpty(countryCodeStr)) {
									ppo.setAddressCountryCode(countryCodeStr);
								}
								
								String phoneNumStr = jsonResponseOri.get(PaypalInfo.pec_d_phonenum);
								if (StringUtils.isNotEmpty(phoneNumStr)) {
									ppo.setPayerPhoneNum(phoneNumStr);
								}
								
								String amountStr = jsonResponseOri.get(PaypalInfo.pec_d_amount);
								if (StringUtils.isNotEmpty(amountStr)) {
									Double amountValue = Double.valueOf(amountStr);
									ppo.setPaymentGross(amountValue);
									ppo.setMcGross(amountValue);
								}
								
								String currencyStr = jsonResponseOri.get(PaypalInfo.pec_d_currencycode);
								if (StringUtils.isNotEmpty(currencyStr)) {
									ppo.setMcCurrency(currencyStr);
								}
								
								String transactionIdStr = jsonResponseOri.get(PaypalInfo.pec_d_transactionid);
								if (StringUtils.isNotEmpty(transactionIdStr)) {
									ppo.setTransactionId(transactionIdStr);
								}
								
								String requestIdStr = jsonResponseOri.get(PaypalInfo.pec_d_requestid);
								if (StringUtils.isNotEmpty(requestIdStr)) {
									ppo.setPaymentRequestId(requestIdStr);
								}
								
								String taxIdTypeStr = jsonResponseOri.get(PaypalInfo.pec_d_taxidtype);
								if (StringUtils.isNotEmpty(taxIdTypeStr)) {
									ppo.setTxnType(taxIdTypeStr);
								}
								
								String taxIdStr = jsonResponseOri.get(PaypalInfo.pec_d_taxid);
								if (StringUtils.isNotEmpty(taxIdStr)) {
									ppo.setTxnId(taxIdStr);
								}
								
								String firstNameStr = jsonResponseOri.get(PaypalInfo.pec_d_firstname);
								if (StringUtils.isNotEmpty(firstNameStr)) {
									ppo.setFirstName(firstNameStr);
								}
								
								String lastNameStr = jsonResponseOri.get(PaypalInfo.pec_d_lastname);
								if (StringUtils.isNotEmpty(lastNameStr)) {
									ppo.setLastName(lastNameStr);
								}
								ifCorrect = true;
							}
							
							visitorProductOrderService.saveProductPayOrder(ppo);
							orderRedisService.saveProductPayOrderById(ppo);
						} 
					} 
				} 
				
				if (!ifCorrect) {
					if (log.isInfoEnabled()) {
						log.info("paypal response not right: >"+oriJsonResponseStr+"<");
					}
					redirectURLFinal = "redirect:/day/product?pid="+po.getOrderProductId();
				} else {
					if (log.isInfoEnabled()) {
						log.info("paypal response json: >"+ oriJsonResponseStr +"<");
					}
					
					redirectURLFinal = "redirect:/day/toPayOrder?pid="+po.getOrderProductId()+"&poid="+po.getOrderId()+"&ppoid="+ppo.getPayOrderId();
				}
			} else {
				if (log.isInfoEnabled()) {
					log.info("-----illegal callback from paypal");
				}
				redirectURLFinal = "redirect:/day/product?pid="+po.getOrderProductId();
			}
		} else {
			if (log.isInfoEnabled()) {
				log.info("-----illegal callback from paypal");
			}
			redirectURLFinal = "redirect:/day/product?pid="+po.getOrderProductId();
		}
		//DoExpressCheckoutPayment like the set above
		
		return redirectURLFinal;
	}
	
	@RequestMapping(value="ecCancel/{poId}/{ppoId}")
	public String expressCheckoutCancel(HttpServletRequest request,
			@PathVariable Long poId,
			@PathVariable Long ppoId,
			@RequestParam(value = "TOKEN", required = true) String tokenPassed,
			HttpServletResponse response) throws UnsupportedEncodingException {
		User userTemp = orderRedisService.getPayPalTokenUser(tokenPassed);
		
		ProductOrder po = orderRedisService.getUserOrder(userTemp, poId);
		ProductPayOrder ppo = orderRedisService.getProductPayOrderById(ppoId);
		
		po.setOrderStatus(ProductOrderStatusEnum.EXPIRED.ordinal());
		ppo.setPayStatus(ProductPayOrderStatusEnum.Invalid.ordinal());
		
		visitorProductOrderService.saveProductOrder(po);
		visitorProductOrderService.saveProductPayOrder(ppo);
		orderRedisService.saveUserOrders(userTemp, po);
		orderRedisService.saveProductOrders(po);
		orderRedisService.saveProductPayOrderById(ppo);
		
		if (log.isInfoEnabled()) {
			log.info("order id: >" + String.valueOf(po.getOrderId()) +"< canceled");
		}
		
		String redirectURLFinal = "";
		redirectURLFinal = "redirect:/day/product?pid="+po.getOrderProductId();
		
		return redirectURLFinal;
	}
	
	//final step payment
	@RequestMapping("toPayOrder/{poId}/{ppoId}")
	public String toPayOrder(HttpServletRequest request,
			@PathVariable Long poId,
			@PathVariable Long ppoId,
			HttpServletResponse response) {
		User userTemp = (User) request.getAttribute(WebInfo.UserID);
		
		ProductOrder po = orderRedisService.getUserOrder(userTemp, poId);
		
		if (po != null) {
			ProductPayOrder ppo = new ProductPayOrder();
			ppo.setPayOrderOids(String.valueOf(poId.longValue()));
			ppo.setPayStatus(ProductPayOrderStatusEnum.Init.ordinal());
			ppo.setPayOrderOwnerEmail(userTemp.getUserEmail());
			
			visitorProductOrderService.saveProductPayOrder(ppo);
			po.setOrderPayOrderId(ppo.getPayOrderId());
			po.setOrderStatus(ProductOrderStatusEnum.WaitPay.ordinal());
			visitorProductOrderService.saveProductOrder(po);
			orderRedisService.saveUserOrders(userTemp, po);
			orderRedisService.saveProductOrders(po);
			orderRedisService.saveProductPayOrderById(ppo);
			
			return "redirect:/day/toPayOrder?pid="+po.getOrderProductId()+"&poid="+po.getOrderId()+"&ppoid="+ppo.getPayOrderId();
		} else {
			//stay on the product page
			return "redirect:/index";
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
