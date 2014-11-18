/**
 * 
 */
package org.visitor.appportal.web.controller.common;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
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
		
		model.addAttribute("timezones", listTZ);
		model.addAttribute("visitorlanguages", listVL);
		
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
