package org.visitor.appportal.web.controller.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.visitor.appportal.redis.FloopyUtils;
import org.visitor.appportal.service.newsite.VisitorUserService;
import org.visitor.appportal.service.newsite.VisitorUserTokenInfoService;
import org.visitor.appportal.service.newsite.redis.FloopyThingRedisService;
import org.visitor.appportal.service.newsite.redis.ProductRedisService;
import org.visitor.appportal.service.newsite.redis.TimezoneRedisService;
import org.visitor.appportal.service.newsite.redis.UserRedisService;
import org.visitor.appportal.service.newsite.redis.VisitorLanguageRedisService;
import org.visitor.appportal.visitor.beans.BuyTemp;
import org.visitor.appportal.visitor.beans.PayTemp;
import org.visitor.appportal.visitor.beans.ProductAddressTemp;
import org.visitor.appportal.visitor.beans.ProductDetailTemp;
import org.visitor.appportal.visitor.beans.ProductOperationTemp;
import org.visitor.appportal.visitor.beans.ProductPriceMultiTemp;
import org.visitor.appportal.visitor.beans.ProductTemp;
import org.visitor.appportal.visitor.beans.UserInternalMailTemp;
import org.visitor.appportal.visitor.beans.UserTemp;
import org.visitor.appportal.visitor.beans.view.CityProduct;
import org.visitor.appportal.visitor.domain.Product;
import org.visitor.appportal.visitor.domain.ProductAddress;
import org.visitor.appportal.visitor.domain.ProductDetailInfo;
import org.visitor.appportal.visitor.domain.ProductMultiPrice;
import org.visitor.appportal.visitor.domain.ProductOperation;
import org.visitor.appportal.visitor.domain.ProductPicture;
import org.visitor.appportal.visitor.domain.TimeZone;
import org.visitor.appportal.visitor.domain.User;
import org.visitor.appportal.visitor.domain.UserTokenInfo;
import org.visitor.appportal.visitor.domain.VisitorLanguage;
import org.visitor.appportal.web.mailutils.SendMailUtils;
import org.visitor.appportal.web.mailutils.UserMailException;
import org.visitor.appportal.web.utils.MixAndMatchUtils;
import org.visitor.appportal.web.utils.ProductInfo.ProductOperationTypeEnum;

import com.alibaba.fastjson.JSON;

public class BasicController {
	protected static final Logger log = LoggerFactory.getLogger(BasicController.class);
	
	@Autowired
	private TimezoneRedisService timezoneRedisService;
	@Autowired
	private VisitorLanguageRedisService visitorLanguageRedisService;
	@Autowired
	private FloopyThingRedisService floopyThingRedisService;
	@Autowired
	private VisitorUserTokenInfoService visitorUserTokenInfoService;
	@Autowired
	private UserRedisService userRedisService;
	@Autowired
	private VisitorUserService visitorUserService;
	@Autowired
	private ProductRedisService productRedisService;
	
	private ObjectMapper objectMapper;
	
	protected String globalCurrencyStored;
	
	public BasicController() {	
	}
	
	protected String getJsonStr(HttpServletRequest request) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream in;
		try {
			in = request.getInputStream();
			byte[] buf = new byte[1024];
			for (;;) {
				int len = in.read(buf);
				if (len == -1) {
					break;
				}

				if (len > 0) {
					baos.write(buf, 0, len);
				}
			}
			if (baos.size() <= 0)
			{
				return null;
			}
			byte[] bytes = baos.toByteArray();
			String originStr = new String(bytes);
			
			return originStr;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public UserTemp getUserJson(HttpServletRequest request) {
		String originStr = getJsonStr(request);
		
		if (StringUtils.isNotEmpty(originStr)) {
			UserTemp userT = JSON.parseObject(originStr, UserTemp.class);
			return userT;
		}

		return null;
	}
	
	public PayTemp getPayJson(HttpServletRequest request) {
		String originStr = getJsonStr(request);
		
		if (StringUtils.isNotEmpty(originStr)) {
			PayTemp payT = JSON.parseObject(originStr, PayTemp.class);
			return payT;
		}
		
		return null;
	}
	
	public ProductTemp getProductTempJson(HttpServletRequest request) {
		String originStr = getJsonStr(request);
		
		if (StringUtils.isNotEmpty(originStr)) {
			ProductTemp productT = JSON.parseObject(originStr, ProductTemp.class);
			return productT;
		}
		
		return null;
	}
	
	public ProductDetailTemp getProductDetailTempJson(HttpServletRequest request) {
		String originStr = getJsonStr(request);
		
		if (StringUtils.isNotEmpty(originStr)) {
			ProductDetailTemp productDT = JSON.parseObject(originStr, ProductDetailTemp.class);
			return productDT;
		}
		
		return null;
	}
	
	public ProductAddressTemp getProductAddressTempJson(HttpServletRequest request) {
		String originStr = getJsonStr(request);
		
		if (StringUtils.isNotEmpty(originStr)) {
			ProductAddressTemp productAT = JSON.parseObject(originStr, ProductAddressTemp.class);
			return productAT;
		}
		
		return null;
	}
	
	public ProductPriceMultiTemp getProductPriceMultiTempJson(HttpServletRequest request) {
		String originStr = getJsonStr(request);
		
		if (StringUtils.isNotEmpty(originStr)) {
			ProductPriceMultiTemp productPMT = JSON.parseObject(originStr, ProductPriceMultiTemp.class);
			return productPMT;
		}
		
		return null;
	}
	
	public ProductOperationTemp getProductOperationTempJson(HttpServletRequest request) {
		String originStr = getJsonStr(request);
		
		if (StringUtils.isNotEmpty(originStr)) {
			ProductOperationTemp productOperT = JSON.parseObject(originStr, ProductOperationTemp.class);
			return productOperT;
		}
		return null;
	}
	
	public UserInternalMailTemp getUserInternalMailTempJson(HttpServletRequest request) {
		String originStr = getJsonStr(request);
		
		if (StringUtils.isNotEmpty(originStr)) {
			UserInternalMailTemp uimT = JSON.parseObject(originStr, UserInternalMailTemp.class);
			return uimT;
		}
		return null;
	}
	
	public BuyTemp getBuyTempJSON(HttpServletRequest request) {
		String originStr = getJsonStr(request);
		
		if (StringUtils.isNotEmpty(originStr)) {
			BuyTemp bt = JSON.parseObject(originStr, BuyTemp.class);
			return bt;
		}
		return null;
	}
	
	//send JSON response utility
	public void sendJSONResponse(Object obj, HttpServletResponse response) {
		try {
			String resultJsonStr = this.getObjectMapper().writeValueAsString(obj);
			PrintWriter out = response.getWriter();
			out.print(resultJsonStr);
			out.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public void logTheJsonResult(Object obj) {
		if (log.isInfoEnabled()) {
			String resultJsonStr;
			try {
				resultJsonStr = this.getObjectMapper().writeValueAsString(obj);
				log.info("json String result: >" + resultJsonStr + "<");
			} catch (JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @return the objectMapper
	 */
	private ObjectMapper getObjectMapper() {
		if(null == objectMapper) {
			this.objectMapper = new ObjectMapper();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			objectMapper.setDateFormat(df);
			objectMapper.getDeserializationConfig().disable(Feature.FAIL_ON_UNKNOWN_PROPERTIES);
		}
		return objectMapper;
	}
	
	
	//send email utility
	public void sendEmail(String title, String content, String toAddress) {
		SendMailUtils sendMU = new SendMailUtils();
		sendMU.setTitle(title);
		sendMU.setContent(content);
		sendMU.setToAddress(toAddress);
		try
		{
			sendMU.sendEmailHtml();
		} catch (UserMailException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (log.isInfoEnabled()) {
				log.info("sendmail exception: >"+e.getMessage()+"<");
			}
		}
	}
	
	public String getGlobalCurrencyStored() {
		return globalCurrencyStored;
	}

	public void setGlobalCurrencyStored(String globalCurrencyStored) {
		this.globalCurrencyStored = globalCurrencyStored;
	}
	
	protected void logTheLogintime(String emailStr) {
		if (log.isInfoEnabled()) {
			log.info("<user token login>: >" + emailStr + "<");
		}
	}
	
	//在页面插入后台配置的各项数据
	protected boolean setModel(HttpServletRequest request, 
			HttpServletResponse response,
			Model model, boolean b) {
		boolean ifloggedIn = false;
		try {
			ifloggedIn = checkIfTheUserTokenLegal(model, request, response, b);
			
			List<TimeZone> listTZ = timezoneRedisService.getAllTimezones();
			List<VisitorLanguage> listVL = visitorLanguageRedisService.getAllLanguages();
			
			List<String> homeTypeList = floopyThingRedisService.getFloopyValueList(FloopyUtils.HOME_TYPE_KEY);
			List<String> roomTypeList = floopyThingRedisService.getFloopyValueList(FloopyUtils.ROOM_TYPE_KEY);
			String singularAccomodates = floopyThingRedisService.getFloopyValueSingle(FloopyUtils.ACCOMODATES);
			if (StringUtils.isNotEmpty(singularAccomodates)) {
				Integer singularAccomodatesInt = Integer.valueOf(singularAccomodates);
				List<String> accomodatesList = floopyThingRedisService.getFloopySingularGeneratedList(singularAccomodatesInt);
				model.addAttribute("accomodatesList", accomodatesList);
			}
			
			List<String> currencyList = floopyThingRedisService.getFloopyValueList(FloopyUtils.CURRENCY_KEY);
			
			List<String> amenitiesMostCommon = floopyThingRedisService.getFloopyValueList(FloopyUtils.AMENITIES_MOST_COMMON);
			List<String> amenitiesExtras = floopyThingRedisService.getFloopyValueList(FloopyUtils.AMENITIES_EXTRAS);
			List<String> amenitiesSpecialFeatures = floopyThingRedisService.getFloopyValueList(FloopyUtils.AMENITIES_SPECIAL_FEATURES);
			List<String> amenitiesHomeSafty = floopyThingRedisService.getFloopyValueList(FloopyUtils.AMENITIES_HOME_SAFETY);
			
			List<String> bedroomNumberList = floopyThingRedisService.getFloopyValueList(FloopyUtils.BEDROOM_NUMBER);
			String bedsNumberListStr = floopyThingRedisService.getFloopyValueSingle(FloopyUtils.BED_NUMBER);
			
			if (StringUtils.isNotEmpty(bedsNumberListStr)) {
				Integer bedsNumberListSize = Integer.valueOf(bedsNumberListStr);
				List<String> bedsNumberList = floopyThingRedisService.getFloopySingularGeneratedList(bedsNumberListSize);
				model.addAttribute("bedsNumberList", bedsNumberList);
			}
			
			List<String> bathroomNumberList = floopyThingRedisService.getFloopyValueList(FloopyUtils.BATHROOM_NUMBER);
			
			List<String> checkinAfterList = floopyThingRedisService.getFloopyValueList(FloopyUtils.TERM_CHECKIN_AFTER);
			List<String> checkoutBeforeList = floopyThingRedisService.getFloopyValueList(FloopyUtils.TERM_CHECKOUT_BEFORE);
			List<String> cancellationPolicyList = floopyThingRedisService.getFloopyValueList(FloopyUtils.TERM_CANCELLATION_POLICY);
			
			model.addAttribute("timezones", listTZ);
			model.addAttribute("visitorlanguages", listVL);
			
			model.addAttribute("homeTypeList", homeTypeList);
			model.addAttribute("roomTypeList", roomTypeList);
			model.addAttribute("currencyList", currencyList);
			
			model.addAttribute("amenitiesMostCommon", amenitiesMostCommon);
			model.addAttribute("amenitiesExtras", amenitiesExtras);
			model.addAttribute("amenitiesSpecialFeatures", amenitiesSpecialFeatures);
			model.addAttribute("amenitiesHomeSafty", amenitiesHomeSafty);
			
			model.addAttribute("bedroomNumberList", bedroomNumberList);
			model.addAttribute("bathroomNumberList", bathroomNumberList);
			
			model.addAttribute("checkinAfterList", checkinAfterList);
			model.addAttribute("checkoutBeforeList", checkoutBeforeList);
			model.addAttribute("cancellationPolicyList", cancellationPolicyList);
			
			String imgPathOrigin = MixAndMatchUtils.getSystemAwsPaypalConfig(MixAndMatchUtils.awsImgDomain) + MixAndMatchUtils.getSystemAwsPaypalConfig(MixAndMatchUtils.awsImgStatic);
			
			model.addAttribute("imgPathOrigin", imgPathOrigin);
			
			List<String> listCity = productRedisService.getCities();
			
			model.addAttribute("productCities", listCity);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ifloggedIn;
	}
	
	//检查是否登陆， 如果登陆， 在页面插入用户数据
	protected boolean checkIfTheUserTokenLegal(Model model, HttpServletRequest request, HttpServletResponse response, boolean ifIndex) throws UnsupportedEncodingException {
		Cookie[] cookieArray = request.getCookies();
		if (cookieArray != null && cookieArray.length > 0) {
			
			Map<String, String> cookieMap = new HashMap<String, String>();
			
			for (int j = 0; j < cookieArray.length; j ++) {
				Cookie tmpCookie = cookieArray[j];
				cookieMap.put(tmpCookie.getName(), tmpCookie.getValue());
				//log.info("cookie name: >" + tmpCookie.getName() + "<");
				//log.info("cookie value: >" + tmpCookie.getValue() + "<");
			}
			
			String userMailStrOri = cookieMap.get(MixAndMatchUtils.COOKIE_NAME_USER_EMAIL);
			String userTokenInfoStr = cookieMap.get(MixAndMatchUtils.COOKIE_NAME_USER_ACCESS_TOKEN);
			
			String globalCurrency = cookieMap.get(MixAndMatchUtils.COOKIE_NAME_GLOBAL_CURRENCY);
			
			log.info("page user check userMailStrOri: >" + userMailStrOri + "<");
			log.info("page user check userTokenInfoStr: >" + userTokenInfoStr + "<");
			log.info("page user check globalCurrency: >" + globalCurrency + "<");
			
			if (StringUtils.isNotEmpty(globalCurrency)) {
				this.setGlobalCurrencyStored(globalCurrency);
				model.addAttribute("globalCurrencySetted", globalCurrency);
			}
			
			if (StringUtils.isNotEmpty(userMailStrOri) && StringUtils.isNotEmpty(userTokenInfoStr)) {
				String userMailStr = URLDecoder.decode(userMailStrOri, "UTF-8");
				UserTokenInfo userTi = userRedisService.getUserTokenInfo(userMailStr);
				
				if (userTi == null) {
					
					//get from database
					UserTokenInfo userTiNew = visitorUserTokenInfoService.getUserTokenInfoByUserEmail(userMailStr);
					
					if (userTiNew != null) {
						String storedAccessToken = userTiNew.getUfiAccessToken();
						Date expireDate = userTiNew.getUfiExpireDate();
						Date nowDate = new Date();
						
						if (StringUtils.isNotEmpty(storedAccessToken) && nowDate.before(expireDate)) {
							if (StringUtils.equals(userTokenInfoStr, storedAccessToken)) {
								User user = userRedisService.getUserPassword(userMailStr);
								if (ifIndex) {
									user.setUserLastLoginTime(nowDate);

									visitorUserService.saveUser(user);
									userRedisService.saveUserPassword(user);
									logTheLogintime(userMailStr);
								}
								
								MixAndMatchUtils.setUserModel(model, user);
								return true;
							}
						}
					}
					
				} else {
					String storedAccessToken = userTi.getUfiAccessToken();
					Date expireDate = userTi.getUfiExpireDate();
					Date nowDate = new Date();
					
					if (StringUtils.isNotEmpty(storedAccessToken) && nowDate.before(expireDate)) {
						if (StringUtils.equals(userTokenInfoStr, storedAccessToken)) {
							User user = userRedisService.getUserPassword(userMailStr);
							if (ifIndex) {
								user.setUserLastLoginTime(nowDate);

								visitorUserService.saveUser(user);
								userRedisService.saveUserPassword(user);
								logTheLogintime(userMailStr);
							}
							
							MixAndMatchUtils.setUserModel(model, user);
							return true;
						}
					}
				}
			} 
		}
		User user = new User();
		MixAndMatchUtils.setUserModel(model, user);
		return false;
	}
	
	protected boolean setMyProductModel(User user, HttpServletRequest request, Model model) {
		List<Product> list = productRedisService.getUserProducts(user);
		if (list != null && list.size() > 0) {
			model.addAttribute("productList", list);
			return true;
		} else {
			return false;
		}
	}
	
	protected boolean setCityProductsModel(String cityStr, HttpServletRequest request, Model model) {
		List<CityProduct> lcp = new ArrayList<CityProduct>();
		
		List<Product> list = productRedisService.getProductListFromRedis(cityStr);
		if (list!= null && list.size() > 0) {
			
			for (Product product : list) {
				CityProduct cp = new CityProduct();
				cp.setProduct(product);
				
				String userEmail = product.getProductPublishUserEmail();
				User host = userRedisService.getUserPassword(userEmail);
				cp.setOwner(host);
				lcp.add(cp);
			}
			
			model.addAttribute("productList", lcp);
			return true;
		} else {
			return false;
		}
	}
	
	//在页面中插入product数据
	protected boolean setProductModel(User user, HttpServletRequest request, Model model, String productIdStr) {
		Product product = productRedisService.getUserProductFromRedis(user, productIdStr);
		if (product == null) {
			return false;
		} else {
			model.addAttribute("productInfo", product);
			
			// start to calculate remain steps
			Integer stepsRemain = 5;
			// end to calculate remain steps
			if (StringUtils.isNotEmpty(product.getProductCurrency())) {
				stepsRemain --;
				model.addAttribute("productCurrencySetted", product.getProductCurrency());
			} else {
				model.addAttribute("productCurrencySetted", this.getGlobalCurrencyStored());
			}
			
			List<ProductMultiPrice> listpmp = productRedisService.getAllMultiPricesSetsForProduct(product.getProductId());
			model.addAttribute("multiPriceSet", listpmp);
			
			ProductDetailInfo productDetailInfo = productRedisService.getProductDetailInfoUsingProductId(product.getProductId());
			if (productDetailInfo != null) {
				model.addAttribute("productDetailInfo", productDetailInfo);
			}
			
			ProductAddress pa = productRedisService.getProductAddressFromRedis(product.getProductId());
			if (pa != null) {
				model.addAttribute("productAddress", pa);
				if (productDetailInfo != null) {
					stepsRemain --;
				}
			}
			
			List<ProductPicture> listPP = productRedisService.getPictureListOfOneProduct(product.getProductId());
			
			if (listPP != null && listPP.size() > 0) {
				List<String> productPicUrls = new ArrayList<String>();
				String awsBucketName = MixAndMatchUtils.getSystemAwsPaypalConfig(MixAndMatchUtils.awsImgStatic);
				String imgDomain = MixAndMatchUtils.getSystemAwsPaypalConfig(MixAndMatchUtils.awsImgDomain);
				
				for (ProductPicture pp : listPP) {
					String fileOriUrl = pp.getProductPicProductUrl();
					
					String displayUrl = imgDomain + awsBucketName + "/" + fileOriUrl;
					productPicUrls.add(displayUrl);
				}
				
				stepsRemain --;
				model.addAttribute("productPictureList", productPicUrls);
			}
			
			if (product.getProductCancellationpolicy() != null) {
				stepsRemain --;
			}
			
			if (product.getProductAvailabletype() != null) {
				stepsRemain --;
			}
			
			model.addAttribute("stepsRemain", stepsRemain);
			return true;
		}
	}
	
	//在页面中插入product数据
	protected boolean setProductInfoModel(HttpServletRequest request, Model model, String productIdStr) {
		Product product = productRedisService.getProductFromRedis(Long.valueOf(productIdStr));
		if (product == null) {
			return false;
		} else {
			model.addAttribute("productInfo", product);
			if (StringUtils.isNotEmpty(product.getProductCurrency())) {
				model.addAttribute("productCurrencySetted", product.getProductCurrency());
			} else {
				model.addAttribute("productCurrencySetted", this.getGlobalCurrencyStored());
			}
			
			ProductDetailInfo productDetailInfo = productRedisService.getProductDetailInfoUsingProductId(product.getProductId());
			if (productDetailInfo != null) {
				model.addAttribute("productDetailInfo", productDetailInfo);
			}
			
			List<ProductMultiPrice> listpmp = productRedisService.getAllMultiPricesSetsForProduct(product.getProductId());
			model.addAttribute("multiPriceSet", listpmp);
			
			ProductAddress pa = productRedisService.getProductAddressFromRedis(product.getProductId());
			if (pa != null) {
				model.addAttribute("productAddress", pa);
			}
			
			List<ProductOperation> listPO =  productRedisService.getProductOperationList(product.getProductId());
			
			List<String> unavailDateList = new ArrayList<String>();
			String jsonUnavailableDates = "[";
			
			int idx = 0;
			for (ProductOperation po : listPO) {
				if(po.getPoType().intValue() == ProductOperationTypeEnum.Publish_unavail.ordinal()) {
					DateTime startOne = new DateTime(po.getPoStartDate());
					DateTime endOne = new DateTime(po.getPoEndDate());
					if (endOne.isAfter(startOne)) {
						while (!startOne.isEqual(endOne)) {
							String getOne = startOne.toString("yyyy-MM-dd");
							if (idx == 0) {
								jsonUnavailableDates = jsonUnavailableDates + "\"" + getOne;
								idx ++;
							} else {
								jsonUnavailableDates = jsonUnavailableDates + "\",\"" + getOne;
							}
							unavailDateList.add(getOne);
							startOne = startOne.plusDays(1);
						}
					}
				}
			}
			
			jsonUnavailableDates = jsonUnavailableDates + "\"]";
			
			if (unavailDateList.size() > 0) {
				model.addAttribute("unavailDateList", jsonUnavailableDates);
			}
			
			List<ProductPicture> listPP = productRedisService.getPictureListOfOneProduct(product.getProductId());
			
			if (listPP != null && listPP.size() > 0) {
				List<String> productPicUrls = new ArrayList<String>();
				String awsBucketName = MixAndMatchUtils.getSystemAwsPaypalConfig(MixAndMatchUtils.awsImgStatic);
				String imgDomain = MixAndMatchUtils.getSystemAwsPaypalConfig(MixAndMatchUtils.awsImgDomain);
				
				for (ProductPicture pp : listPP) {
					String fileOriUrl = pp.getProductPicProductUrl();
					
					String displayUrl = imgDomain + awsBucketName + "/" + fileOriUrl;
					productPicUrls.add(displayUrl);
				}
				
				model.addAttribute("productPictureList", productPicUrls);
				if (productPicUrls.size() > 0) {
					model.addAttribute("productIcon", productPicUrls.get(0));
				}
			}
			
			String hostEmailStr = product.getProductPublishUserEmail();
			User hostUser = userRedisService.getUserPassword(hostEmailStr);
			
			model.addAttribute("hostInfo", hostUser);
			return true;
		}
	}
}
