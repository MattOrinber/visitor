/**
 * 
 */
package org.visitor.appportal.web.controller.common;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
import org.visitor.appportal.redis.FloopyUtils;
import org.visitor.appportal.service.newsite.VisitorUserService;
import org.visitor.appportal.service.newsite.VisitorUserTokenInfoService;
import org.visitor.appportal.service.newsite.redis.FloopyThingRedisService;
import org.visitor.appportal.service.newsite.redis.TimezoneRedisService;
import org.visitor.appportal.service.newsite.redis.UserRedisService;
import org.visitor.appportal.service.newsite.redis.VisitorLanguageRedisService;
import org.visitor.appportal.visitor.domain.TimeZone;
import org.visitor.appportal.visitor.domain.User;
import org.visitor.appportal.visitor.domain.UserTokenInfo;
import org.visitor.appportal.visitor.domain.VisitorLanguage;
import org.visitor.appportal.web.utils.MixAndMatchUtils;

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
	
	/**
	 * 
	 */
	public IndexController() {
		// TODO Auto-generated constructor stub
	}
	
	private void setModel(HttpServletRequest request, 
			HttpServletResponse response,
			Model model) {
		model.addAttribute("username", "visitor");
		model.addAttribute("helloString", "you are welcome!");
		
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
		
		String imgPathOrigin = MixAndMatchUtils.getSystemAwsPaypalConfig(MixAndMatchUtils.awsImgStatic);
		
		model.addAttribute("imgPathOrigin", imgPathOrigin);
		try {
			checkIfTheUserTokenLegal(model, request, response);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping({"index", ""})
	public String index(HttpServletRequest request, 
			HttpServletResponse response,
			Model model) {
		this.setModel(request, response, model);
		
		return "index";
	}
	
	private void checkIfTheUserTokenLegal(Model model, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		
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
			
			log.info("cookie userMailStrOri: >" + userMailStrOri + "<");
			log.info("cookie userTokenInfoStr: >" + userTokenInfoStr + "<");
			
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
								user.setUserLastLoginTime(nowDate);
								
								visitorUserService.saveUser(user);
								userRedisService.saveUserPassword(user);
								
								MixAndMatchUtils.setUserModel(model, user);
								logTheLogintime(userMailStr);
								return;
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
							user.setUserLastLoginTime(nowDate);
							
							visitorUserService.saveUser(user);
							userRedisService.saveUserPassword(user);
							
							MixAndMatchUtils.setUserModel(model, user);
							logTheLogintime(userMailStr);
							return;
						}
					}
				}
			} 
		} 
		User user = new User();
		MixAndMatchUtils.setUserModel(model, user);
		
	}
	
	private void logTheLogintime(String emailStr) {
		if (log.isInfoEnabled()) {
			log.info("<user token login>: >" + emailStr + "<");
		}
	}

	@RequestMapping({"index1"})
	public String indexOne(HttpServletRequest request, HttpServletResponse response,  Model model) {
		this.setModel(request, response, model);
		
		return "index1";
	}
	
	@RequestMapping({"day/list_space"})
	public String dayListSpace(HttpServletRequest request, HttpServletResponse response, Model model) {
		this.setModel(request, response, model);
		return "day/list_space";
	}
	
	@RequestMapping({"day/calendar"})
	public String dayCalendar(HttpServletRequest request, HttpServletResponse response, Model model) {
		this.setModel(request, response, model);
		return "day/calendar";
	}
	
	@RequestMapping({"day/pricing"})
	public String dayPricing(HttpServletRequest request, HttpServletResponse response, Model model) {
		this.setModel(request, response, model);
		return "day/pricing";
	}
	
	@RequestMapping({"day/description"})
	public String dayDescription(HttpServletRequest request, HttpServletResponse response, Model model) {
		this.setModel(request, response, model);
		return "day/description";
	}
	
	@RequestMapping({"day/photos"})
	public String dayPhotos(HttpServletRequest request, HttpServletResponse response, Model model) {
		this.setModel(request, response, model);
		return "day/photos";
	}
	
	@RequestMapping({"day/terms"})
	public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
		this.setModel(request, response, model);
		return "day/terms";
	}
	
	@RequestMapping({"day/city"})
	public String dayCity(HttpServletRequest request, HttpServletResponse response, Model model) {
		this.setModel(request, response, model);
		return "day/city";
	}
	
	@RequestMapping({"day/dashboard"})
	public String dayDashboard(HttpServletRequest request, HttpServletResponse response, Model model) {
		this.setModel(request, response, model);
		return "day/dashboard";
	}
	
	@RequestMapping({"day/inbox"})
	public String dayInbox(HttpServletRequest request, HttpServletResponse response, Model model) {
		this.setModel(request, response, model);
		return "day/inbox";
	}
	
	@RequestMapping({"day/host_profile"})
	public String dayHostprofile(HttpServletRequest request, HttpServletResponse response, Model model) {
		this.setModel(request, response, model);
		return "day/host_profile";
	}
	
	@RequestMapping({"day/product"})
	public String dayProduct(HttpServletRequest request, HttpServletResponse response, Model model) {
		this.setModel(request, response, model);
		return "day/product";
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
}
