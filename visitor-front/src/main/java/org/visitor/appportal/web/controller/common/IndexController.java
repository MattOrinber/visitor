/**
 * 
 */
package org.visitor.appportal.web.controller.common;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.visitor.appportal.redis.FloopyUtils;
import org.visitor.appportal.service.newsite.VisitorUserService;
import org.visitor.appportal.service.newsite.VisitorUserTokenInfoService;
import org.visitor.appportal.service.newsite.redis.FloopyThingRedisService;
import org.visitor.appportal.service.newsite.redis.ProductRedisService;
import org.visitor.appportal.service.newsite.redis.TimezoneRedisService;
import org.visitor.appportal.service.newsite.redis.UserRedisService;
import org.visitor.appportal.service.newsite.redis.VisitorLanguageRedisService;
import org.visitor.appportal.visitor.domain.Product;
import org.visitor.appportal.visitor.domain.ProductDetailInfo;
import org.visitor.appportal.visitor.domain.ProductPicture;
import org.visitor.appportal.visitor.domain.TimeZone;
import org.visitor.appportal.visitor.domain.User;
import org.visitor.appportal.visitor.domain.UserTokenInfo;
import org.visitor.appportal.visitor.domain.VisitorLanguage;
import org.visitor.appportal.web.utils.HttpClientUtil;
import org.visitor.appportal.web.utils.MixAndMatchUtils;
import org.visitor.appportal.web.utils.WebInfo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * @author mengw
 *
 */
@Controller
@RequestMapping("/")
public class IndexController extends BasicController {
	protected static final Logger log = LoggerFactory.getLogger(IndexController.class);
	
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
	
	private String globalCurrencyStored;

	/**
	 * 
	 */
	public IndexController() {
		// TODO Auto-generated constructor stub
	}
	
	@RequestMapping({"index", ""})
	public String index(HttpServletRequest request, 
			HttpServletResponse response,
			Model model) {
		this.setModel(request, response, model, true);
		
		return "index";
	}
	
	private void logTheLogintime(String emailStr) {
		if (log.isInfoEnabled()) {
			log.info("<user token login>: >" + emailStr + "<");
		}
	}

	@RequestMapping({"index1"})
	public String indexOne(HttpServletRequest request, HttpServletResponse response,  Model model) {
		this.setModel(request, response, model, true);
		return "index1";
	}
	
	@RequestMapping({"list_space"})
	public String dayListSpace(HttpServletRequest request, HttpServletResponse response, Model model) {
		this.setModel(request, response, model, false);
		model.addAttribute("pageName", "list_space");
		return "list_space";
	}
	
	@RequestMapping({"day/calendar"})
	public String dayCalendar(HttpServletRequest request, 
			@RequestParam(value = "pid", required = true) String productIdStr,
			HttpServletResponse response,
			Model model) {
		boolean ifLoggedIn = this.setModel(request, response, model, false);
		if (!ifLoggedIn) {
			return "redirect:/index";
		}
		
		User user = (User) request.getAttribute(WebInfo.UserID);
		boolean ifOwnedProduct = this.setProductModel(user, request, model, productIdStr);
		if (!ifOwnedProduct) {
			return "redirect:/index";
		}
		
		model.addAttribute("pageName", "calendar");
		return "day/calendar";
	}
	
	@RequestMapping({"day/pricing"})
	public String dayPricing(HttpServletRequest request,
			@RequestParam(value = "pid", required = true) String productIdStr,
			HttpServletResponse response, 
			Model model) {
		boolean ifLoggedIn = this.setModel(request, response, model, false);
		if (!ifLoggedIn) {
			return "redirect:/index";
		}
		
		User user = (User) request.getAttribute(WebInfo.UserID);
		boolean ifOwnedProduct = this.setProductModel(user, request, model, productIdStr);
		if (!ifOwnedProduct) {
			return "redirect:/index";
		}
		
		model.addAttribute("pageName", "pricing");
		return "day/pricing";
	}
	
	@RequestMapping({"day/description"})
	public String dayDescription(HttpServletRequest request,
			@RequestParam(value = "pid", required = true) String productIdStr,
			HttpServletResponse response, 
			Model model) {
		boolean ifLoggedIn = this.setModel(request, response, model, false);
		if (!ifLoggedIn) {
			return "redirect:/index";
		}
		
		User user = (User) request.getAttribute(WebInfo.UserID);
		boolean ifOwnedProduct = this.setProductModel(user, request, model, productIdStr);
		if (!ifOwnedProduct) {
			return "redirect:/index";
		}
		
		model.addAttribute("pageName", "description");
		return "day/description";
	}
	
	@RequestMapping({"day/photos"})
	public String dayPhotos(HttpServletRequest request,
			@RequestParam(value = "pid", required = true) String productIdStr,
			HttpServletResponse response, 
			Model model) {
		boolean ifLoggedIn = this.setModel(request, response, model, false);
		if (!ifLoggedIn) {
			return "redirect:/index";
		}
		
		User user = (User) request.getAttribute(WebInfo.UserID);
		boolean ifOwnedProduct = this.setProductModel(user, request, model, productIdStr);
		if (!ifOwnedProduct) {
			return "redirect:/index";
		}
		
		model.addAttribute("pageName", "photos");
		return "day/photos";
	}
	
	@RequestMapping({"day/terms"})
	public String login(HttpServletRequest request,
			@RequestParam(value = "pid", required = true) String productIdStr,
			HttpServletResponse response, 
			Model model) {
		boolean ifLoggedIn = this.setModel(request, response, model, false);
		if (!ifLoggedIn) {
			return "redirect:/index";
		}
		
		User user = (User) request.getAttribute(WebInfo.UserID);
		boolean ifOwnedProduct = this.setProductModel(user, request, model, productIdStr);
		if (!ifOwnedProduct) {
			return "redirect:/index";
		}

		model.addAttribute("pageName", "terms");
		return "day/terms";
	}
	
	@RequestMapping({"day/city"})
	public String dayCity(HttpServletRequest request,
			HttpServletResponse response, 
			Model model) {
		this.setModel(request, response, model, false);
		model.addAttribute("pageName", "city");
		return "day/city";
	}
	
	@RequestMapping({"day/dashboard"})
	public String dayDashboard(HttpServletRequest request,
			HttpServletResponse response, 
			Model model) {
		this.setModel(request, response, model, false);
		model.addAttribute("pageName", "dashboard");
		return "day/dashboard";
	}
	
	@RequestMapping({"day/inbox"})
	public String dayInbox(HttpServletRequest request, HttpServletResponse response, Model model) {
		this.setModel(request, response, model, false);
		model.addAttribute("pageName", "inbox");
		return "day/inbox";
	}
	
	@RequestMapping({"day/host_profile"})
	public String dayHostprofile(HttpServletRequest request, HttpServletResponse response, Model model) {
		this.setModel(request, response, model, false);
		model.addAttribute("pageName", "host_profile");
		return "day/host_profile";
	}
	
	@RequestMapping({"day/product"})
	public String dayProduct(HttpServletRequest request,
			@RequestParam(value = "pid", required = true) String productIdStr,
			HttpServletResponse response, 
			Model model) {
		this.setModel(request, response, model, false);
		model.addAttribute("pageName", "product");
		return "day/product";
	}
	
	@RequestMapping({"day/your-listing"})
	public String dayYourListing(HttpServletRequest request,
			HttpServletResponse response, 
			Model model) {
		boolean ifLoggedIn = this.setModel(request, response, model, false);
		if (!ifLoggedIn) {
			return "redirect:/index";
		}
		
		User user = (User) request.getAttribute(WebInfo.UserID);
		this.setMyProductModel(user, request, model);
		model.addAttribute("pageName", "inbox");
		return "day/your-listing";
	}
	
	@RequestMapping({"day/edit"})
	public String dayUserEdit(HttpServletRequest request,
			HttpServletResponse response, 
			Model model) {
		boolean ifLoggedIn = this.setModel(request, response, model, false);
		if (!ifLoggedIn) {
			return "redirect:/index";
		}
		
		User user = (User) request.getAttribute(WebInfo.UserID);
		model.addAttribute("currentUser", user);
		model.addAttribute("pageName", "edit");
		return "day/edit";
	}
	
	@RequestMapping({"publish"})
	public String publish(HttpServletRequest request, Model model) {
		return "publish";
	}
	
	@RequestMapping({"websocket"})
	public String websocket(HttpServletRequest request, Model model) {
		return "websocket";
		
	}
	
	@RequestMapping({"userPicUpload"})
	public String userPicUpload(HttpServletRequest request, Model model) {
		return "userPicUpload";
	}
	
	@RequestMapping({"productPicUpload"})
	public String productPicUpload(HttpServletRequest request, Model model) {
		return "productPicUpload";
	}
	
	//在页面插入后台配置的各项数据
	private boolean setModel(HttpServletRequest request, 
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
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ifloggedIn;
	}
	
	//检查是否登陆， 如果登陆， 在页面插入用户数据
	private boolean checkIfTheUserTokenLegal(Model model, HttpServletRequest request, HttpServletResponse response, boolean ifIndex) throws UnsupportedEncodingException {
		Cookie[] cookieArray = request.getCookies();
		if (cookieArray != null && cookieArray.length > 0) {
			
			Map<String, String> cookieMap = new HashMap<String, String>();
			
			for (int j = 0; j < cookieArray.length; j ++) {
				Cookie tmpCookie = cookieArray[j];
				cookieMap.put(tmpCookie.getName(), tmpCookie.getValue());
				log.info("cookie name: >" + tmpCookie.getName() + "<");
				log.info("cookie value: >" + tmpCookie.getValue() + "<");
			}
			
			String userMailStrOri = cookieMap.get(MixAndMatchUtils.COOKIE_NAME_USER_EMAIL);
			String userTokenInfoStr = cookieMap.get(MixAndMatchUtils.COOKIE_NAME_USER_ACCESS_TOKEN);
			
			String globalCurrency = cookieMap.get(MixAndMatchUtils.COOKIE_NAME_GLOBAL_CURRENCY);
			
			log.info("cookie userMailStrOri: >" + userMailStrOri + "<");
			log.info("cookie userTokenInfoStr: >" + userTokenInfoStr + "<");
			log.info("cookie globalCurrency: >" + globalCurrency + "<");
			
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
	
	private boolean setMyProductModel(User user, HttpServletRequest request, Model model) {
		List<Product> list = productRedisService.getUserProducts(user);
		if (list != null && list.size() > 0) {
			model.addAttribute("productList", list);
			return true;
		} else {
			return false;
		}
	}
	
	//在页面中插入product数据
	private boolean setProductModel(User user, HttpServletRequest request, Model model, String productIdStr) {
		Product product = productRedisService.getUserProductFromRedis(user, productIdStr);
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
			}
			return true;
		}
	}
	
	public String getGlobalCurrencyStored() {
		return globalCurrencyStored;
	}

	public void setGlobalCurrencyStored(String globalCurrencyStored) {
		this.globalCurrencyStored = globalCurrencyStored;
	}
	
	
	public static void main(String[] args) {
		String getCurrencyUrl = "http://www.freecurrencyconverterapi.com/api/v2/currencies";
		//String convertURL = "http://www.freecurrencyconverterapi.com/api/v2/convert?q=USD_CNY&compact=y"; //1 USD to CNY
		//HashMap<String, String> paramMap = new HashMap<String,String>();
		
		String jsonStr = HttpClientUtil.httpGetJSON(getCurrencyUrl);
		
		Map<String, String> mapOri = JSON.parseObject(jsonStr, new TypeReference<Map<String, String>>(){});
		
		if (mapOri.containsKey("results")) {
			String dataStr = mapOri.get("results");
			Map<String, String> mapData = JSON.parseObject(dataStr, new TypeReference<Map<String, String>>(){});
			String finalStr = "";
			
			int i = 0;
			for (String keyT : mapData.keySet()) {
				if (i == 0) {
					finalStr = finalStr + keyT;
				} else {
					finalStr = finalStr + "---" + keyT;
				}
				i ++;
			}
			
			System.out.println(finalStr);
		}
	}
}
