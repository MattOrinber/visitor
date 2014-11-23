/**
 * 
 */
package org.visitor.appportal.web.controller.common;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.visitor.appportal.redis.FloopyUtils;
import org.visitor.appportal.service.newsite.redis.FloopyThingRedisService;
import org.visitor.appportal.service.newsite.redis.TimezoneRedisService;
import org.visitor.appportal.service.newsite.redis.VisitorLanguageRedisService;
import org.visitor.appportal.visitor.domain.TimeZone;
import org.visitor.appportal.visitor.domain.VisitorLanguage;

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
	
	/**
	 * 
	 */
	public IndexController() {
		// TODO Auto-generated constructor stub
	}

	@RequestMapping({"index", ""})
	public String index(HttpServletRequest request, Model model) {
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
		
		return "index";
	}
	
	@RequestMapping({"login"})
	public String login(HttpServletRequest request, Model model) {
		return "login";
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
