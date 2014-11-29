package org.visitor.appportal.web.controller.common;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.visitor.appportal.service.newsite.VisitorLanguageService;
import org.visitor.appportal.service.newsite.VisitorTimezoneService;
import org.visitor.appportal.service.newsite.redis.TimezoneRedisService;
import org.visitor.appportal.service.newsite.redis.VisitorLanguageRedisService;
import org.visitor.appportal.visitor.beans.ResultJson;
import org.visitor.appportal.visitor.domain.TimeZone;
import org.visitor.appportal.visitor.domain.VisitorLanguage;

@Controller
@RequestMapping("/publishinfo/")
public class PublishController extends BasicController {
	protected static final Logger log = LoggerFactory.getLogger("classLogger");
	
	@Autowired
	private VisitorLanguageService visitorLanguageService;
	@Autowired
	private VisitorTimezoneService visitorTimezoneService;
	@Autowired
	private TimezoneRedisService timezoneRedisService;
	@Autowired
	private VisitorLanguageRedisService visitorLanguageRedisService;

	@RequestMapping("timezone")
    public void timezone(HttpServletResponse response) {
		Integer result = 0;
		String resultDesc = "success";
		ResultJson resultJson = new ResultJson();
		resultJson.setResult(result);
		resultJson.setResultDesc(resultDesc);
		
		List<TimeZone> list = visitorTimezoneService.getAll();
		
		timezoneRedisService.saveTimeZoneRedis(list);
		
		sendJSONResponse(resultJson, response);
	}
	
	@RequestMapping("language")
    public void language(HttpServletResponse response) {
		Integer result = 0;
		String resultDesc = "success";
		ResultJson resultJson = new ResultJson();
		resultJson.setResult(result);
		resultJson.setResultDesc(resultDesc);
		
		List<VisitorLanguage> list = visitorLanguageService.getAll();
		
		visitorLanguageRedisService.saveAllVisitorLanguage(list);
		
		sendJSONResponse(resultJson, response);
	}
}
