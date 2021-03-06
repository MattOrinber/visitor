/**
 * 
 */
package org.visitor.appportal.web.controller.common;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

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
import org.visitor.appportal.service.newsite.VisitorUserService;
import org.visitor.appportal.service.newsite.VisitorUserTokenInfoService;
import org.visitor.appportal.service.newsite.redis.ArticleRedisService;
import org.visitor.appportal.service.newsite.redis.FloopyThingRedisService;
import org.visitor.appportal.service.newsite.redis.OrderRedisService;
import org.visitor.appportal.service.newsite.redis.ProductRedisService;
import org.visitor.appportal.service.newsite.redis.TimezoneRedisService;
import org.visitor.appportal.service.newsite.redis.UserRedisService;
import org.visitor.appportal.service.newsite.redis.VisitorLanguageRedisService;
import org.visitor.appportal.visitor.domain.Article;
import org.visitor.appportal.visitor.domain.ProductOrder;
import org.visitor.appportal.visitor.domain.ProductPayOrder;
import org.visitor.appportal.visitor.domain.User;
import org.visitor.appportal.web.utils.HttpClientUtil;
import org.visitor.appportal.web.utils.PaypalInfo;
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
	
	@Autowired
	private OrderRedisService orderRedisService;
	@Autowired
	private ArticleRedisService articleRedisService;

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
		super.setModel(request, response, model, true);
		super.setContainerModel(request, response, model);
		
		return "index";
	}
	
	@RequestMapping({"privacy"})
	public String privacy(HttpServletRequest request, 
			HttpServletResponse response,
			Model model) {
		return "privacy";
	}

	@RequestMapping({"index1"})
	public String indexOne(HttpServletRequest request, HttpServletResponse response,  Model model) {
		super.setModel(request, response, model, true);
		return "index1";
	}
	
	@RequestMapping({"list_space"})
	public String dayListSpace(HttpServletRequest request, HttpServletResponse response, Model model) {
		super.setModel(request, response, model, false);
		model.addAttribute("pageName", "list_space");
		return "list_space";
	}
	
	@RequestMapping({"day/calendar"})
	public String dayCalendar(HttpServletRequest request, 
			@RequestParam(value = "pid", required = true) String productIdStr,
			HttpServletResponse response,
			Model model) {
		boolean ifLoggedIn = super.setModel(request, response, model, false);
		if (!ifLoggedIn) {
			return "redirect:/index";
		}
		
		User user = (User) request.getAttribute(WebInfo.UserID);
		boolean ifOwnedProduct = super.setProductModel(user, request, model, productIdStr);
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
		boolean ifLoggedIn = super.setModel(request, response, model, false);
		if (!ifLoggedIn) {
			return "redirect:/index";
		}
		
		User user = (User) request.getAttribute(WebInfo.UserID);
		boolean ifOwnedProduct = super.setProductModel(user, request, model, productIdStr);
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
		boolean ifLoggedIn = super.setModel(request, response, model, false);
		if (!ifLoggedIn) {
			return "redirect:/index";
		}
		
		User user = (User) request.getAttribute(WebInfo.UserID);
		boolean ifOwnedProduct = super.setProductModel(user, request, model, productIdStr);
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
		boolean ifLoggedIn = super.setModel(request, response, model, false);
		if (!ifLoggedIn) {
			return "redirect:/index";
		}
		
		User user = (User) request.getAttribute(WebInfo.UserID);
		boolean ifOwnedProduct = super.setProductModel(user, request, model, productIdStr);
		if (!ifOwnedProduct) {
			return "redirect:/index";
		}
		
		model.addAttribute("pageName", "photos");
		return "day/photos";
	}
	
	@RequestMapping({"day/terms"})
	public String dayTerms(HttpServletRequest request,
			@RequestParam(value = "pid", required = true) String productIdStr,
			HttpServletResponse response, 
			Model model) {
		boolean ifLoggedIn = super.setModel(request, response, model, false);
		if (!ifLoggedIn) {
			return "redirect:/index";
		}
		
		User user = (User) request.getAttribute(WebInfo.UserID);
		boolean ifOwnedProduct = super.setProductModel(user, request, model, productIdStr);
		if (!ifOwnedProduct) {
			return "redirect:/index";
		}

		model.addAttribute("pageName", "terms");
		return "day/terms";
	}
	
	@RequestMapping({"day/city"})
	public String dayCity(HttpServletRequest request,
			HttpServletResponse response, 
			@RequestParam(value = "c", required = true) String cityStr,
			@RequestParam(value = "o", required = true) Integer orderType,
			@RequestParam(value = "p", required = true) Long pageIdx,
			Model model) {
		try {
			super.setModel(request, response, model, false);
			String cityEncoded = URLEncoder.encode(cityStr, "UTF-8");
			super.setCityProductsModel(cityStr, request, model, orderType, pageIdx);
			
			model.addAttribute("currentCity", cityStr);
			model.addAttribute("currentCityOri", cityEncoded);
			model.addAttribute("pageName", "city");
			return "day/city";
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect:/index";
	}
	
	@RequestMapping({"day/dashboard"})
	public String dayDashboard(HttpServletRequest request,
			HttpServletResponse response, 
			Model model) {
		super.setModel(request, response, model, false);
		super.setInboxModel(request, model);
		model.addAttribute("pageName", "dashboard");
		return "day/dashboard";
	}
	
	@RequestMapping({"day/inbox"})
	public String dayInbox(HttpServletRequest request, HttpServletResponse response, Model model) {
		boolean ifLoggedIn = super.setModel(request, response, model, false);
		if (!ifLoggedIn) {
			return "redirect:/index";
		}
		
		super.setInboxModel(request, model);
		
		model.addAttribute("pageName", "inbox");
		return "day/inbox";
	}
	
	@RequestMapping({"day/host_profile"})
	public String dayHostprofile(HttpServletRequest request, 
			HttpServletResponse response, 
			@RequestParam(value="emailStr", required = true) String emailStr,
			Model model) {
		super.setModel(request, response, model, false);
		
		User user = userRedisService.getUserPassword(emailStr);
		model.addAttribute("userDisplay", user);
		
		model.addAttribute("pageName", "host_profile");
		return "day/host_profile";
	}
	
	@RequestMapping({"day/product"})
	public String dayProduct(HttpServletRequest request,
			@RequestParam(value = "pid", required = true) String productIdStr,
			HttpServletResponse response, 
			Model model) {
		super.setModel(request, response, model, false);
		
		boolean ifProductAvail = super.setProductInfoModel(request, model, productIdStr);
		if(!ifProductAvail) {
			boolean ifMyProductAvail = super.setPreviewProductInfoModel(request, model, productIdStr);
			if (!ifMyProductAvail) {
				return "redirect:/index";
			}
		}
		
		model.addAttribute("pageName", "product");
		return "day/product";
	}
	
	@RequestMapping({"day/previewproduct"})
	public String dayPreviewProduct(HttpServletRequest request,
			@RequestParam(value = "pid", required = true) String productIdStr,
			HttpServletResponse response, 
			Model model) {
		super.setModel(request, response, model, false);

		boolean ifMyProductAvail = super.setPreviewProductInfoModel(request, model, productIdStr);
		if (!ifMyProductAvail) {
			return "redirect:/index";
		}
	
		model.addAttribute("pageName", "product");
		return "day/product";
	}
	
	@RequestMapping({"day/your-listing"})
	public String dayYourListing(HttpServletRequest request,
			HttpServletResponse response, 
			Model model) {
		boolean ifLoggedIn = super.setModel(request, response, model, false);
		if (!ifLoggedIn) {
			return "redirect:/index";
		}
		
		User user = (User) request.getAttribute(WebInfo.UserID);
		super.setMyProductModel(user, request, model);
		model.addAttribute("pageName", "your-listing");
		return "day/your-listing";
	}
	
	@RequestMapping({"day/your-reservations"})
	public String dayYourReservation(HttpServletRequest request,
			HttpServletResponse response, 
			Model model) {
		boolean ifLoggedIn = super.setModel(request, response, model, false);
		if (!ifLoggedIn) {
			return "redirect:/index";
		}
		
		User user = (User) request.getAttribute(WebInfo.UserID);
		super.setMyToOrdersModel(user, request, model);
		model.addAttribute("pageName", "your-listing");
		return "day/your-reservations";
	}
	
	@RequestMapping({"day/your-trip"})
	public String dayYourTrip(HttpServletRequest request,
			HttpServletResponse response, 
			Model model) {
		boolean ifLoggedIn = super.setModel(request, response, model, false);
		if (!ifLoggedIn) {
			return "redirect:/index";
		}
		
		User user = (User) request.getAttribute(WebInfo.UserID);
		super.setMyTripModel(user, request, model);
		model.addAttribute("pageName", "yourtrip");
		return "day/your-trip";
	}
	
	@RequestMapping("day/toPayOrder")
	public String toPayOrderPage(HttpServletRequest request,
			@RequestParam(value = "pid", required = true) Long pid,
			@RequestParam(value = "poid", required = true) Long poid,
			@RequestParam(value = "ppoid", required = true) Long ppoid,
			HttpServletResponse response,
			Model model) {
		boolean ifLoggedIn = super.setModel(request, response, model, false);
		if (!ifLoggedIn) {
			return "redirect:/index";
		}
		
		User userTemp = (User) request.getAttribute(WebInfo.UserID);
		boolean ifProductAvail = super.setProductInfoModel(request, model, String.valueOf(pid.longValue()));
		if(!ifProductAvail) {
			return "redirect:/index";
		}
		
		ProductOrder po = orderRedisService.getUserOrder(userTemp, poid);
		ProductPayOrder ppo = orderRedisService.getProductPayOrderById(ppoid);
		
		model.addAttribute("order", po);
		model.addAttribute("payOrder", ppo);
		String menchantId = floopyThingRedisService.getFloopyValueSingle(PaypalInfo.floopy_paypalMerchantId);
		String callbackURL = floopyThingRedisService.getFloopyValueSingle(PaypalInfo.floopy_paypalCallBackURL);
		model.addAttribute("pathCallback", callbackURL);
		model.addAttribute("menchantId", menchantId);
		
		model.addAttribute("pageName", "book");
		
		return "day/toPayOrder";
	}
	
	@RequestMapping("day/result")
	public String toPayOrderPageResult(HttpServletRequest request,
			@RequestParam(value = "info", required = true) String infoStr,
			HttpServletResponse response,
			Model model) throws UnsupportedEncodingException {
		String infoFinal = URLDecoder.decode(infoStr, "UTF-8");
		
		model.addAttribute("info", infoFinal);
		
		return "day/result";
	}
	
	@RequestMapping({"day/edit"})
	public String dayUserEdit(HttpServletRequest request,
			HttpServletResponse response, 
			Model model) {
		boolean ifLoggedIn = super.setModel(request, response, model, false);
		if (!ifLoggedIn) {
			return "redirect:/index";
		}
		
		User user = (User) request.getAttribute(WebInfo.UserID);
		model.addAttribute("currentUser", user);
		model.addAttribute("pageName", "edit");
		return "day/edit";
	}
	
	@RequestMapping({"day/edit-photos"})
	public String dayUserEditPhotos(HttpServletRequest request,
			HttpServletResponse response, 
			Model model) {
		boolean ifLoggedIn = super.setModel(request, response, model, false);
		if (!ifLoggedIn) {
			return "redirect:/index";
		}
		
		User user = (User) request.getAttribute(WebInfo.UserID);
		model.addAttribute("currentUser", user);
		model.addAttribute("pageName", "edit");
		return "day/edit-photos";
	}
	
	@RequestMapping({"day/edit-changepassword"})
	public String dayUserEditChangePassword(HttpServletRequest request,
			HttpServletResponse response,
			Model model) {
		boolean ifLoggedIn = super.setModel(request, response, model, false);
		if (!ifLoggedIn) {
			return "redirect:/index";
		}
		
		User user = (User) request.getAttribute(WebInfo.UserID);
		model.addAttribute("currentUser", user);
		model.addAttribute("pageName", "edit");
		return "day/edit-changepassword";
	}
	
	@RequestMapping({"day/resetpass"})
	public String dayResetPassword(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="mail", required=true) String emailStr,
			@RequestParam(value="token", required=true) String tokenStr,
			Model model) {
		super.setModel(request, response, model, false);
		
		if (log.isInfoEnabled()) {
			log.info("user :"+emailStr+": try to reset password using token :"+tokenStr+":");
		}
		
		String tokenStrStored = userRedisService.getUserresetPasswordToken(emailStr);
		
		if (StringUtils.isEmpty(tokenStrStored) || !StringUtils.equals(tokenStrStored, tokenStr)) {
			model.addAttribute("canReset", 0);
		} else {
			model.addAttribute("canReset", 1);
			model.addAttribute("tokenUsed", tokenStrStored);
			model.addAttribute("mailUsed", emailStr);
		}
		
		model.addAttribute("pageName", "resetpass");
		return "day/resetpass";
	}
	
	@RequestMapping({"day/about"})
	public String dayAbout(HttpServletRequest request,
			HttpServletResponse response, 
			Model model) {
		super.setModel(request, response, model, false);
		
		Article art = articleRedisService.getArticleByName(WebInfo.ABOUT_INFO);
		if (art != null) {
			model.addAttribute("aboutContent", art.getArticleContent());
		}
		model.addAttribute("pageName", "inbox");
		return "day/about";
	}
	
	@RequestMapping({"day/footer-policy"})
	public String dayFooterPolicy(HttpServletRequest request,
			HttpServletResponse response, 
			Model model) {
		super.setModel(request, response, model, false);
		Article art = articleRedisService.getArticleByName(WebInfo.FOOTER_POLICY);
		if (art != null) {
			model.addAttribute("footerPolicyContent", art.getArticleContent());
		}
		model.addAttribute("pageName", "inbox");
		return "day/footer-policy";
	}
	
	@RequestMapping({"day/footer-terms"})
	public String dayFooterTerms(HttpServletRequest request,
			HttpServletResponse response, 
			Model model) {
		super.setModel(request, response, model, false);
		Article art = articleRedisService.getArticleByName(WebInfo.FOOTER_TERMS);
		if (art != null) {
			model.addAttribute("footerTermsContent", art.getArticleContent());
		}
		model.addAttribute("pageName", "inbox");
		return "day/footer-terms";
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
