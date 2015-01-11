package org.visitor.appportal.web.controller.common;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.visitor.appportal.service.newsite.VisitorCityService;
import org.visitor.appportal.service.newsite.VisitorLanguageService;
import org.visitor.appportal.service.newsite.VisitorTimezoneService;
import org.visitor.appportal.service.newsite.redis.ProductRedisService;
import org.visitor.appportal.service.newsite.redis.TimezoneRedisService;
import org.visitor.appportal.service.newsite.redis.VisitorLanguageRedisService;
import org.visitor.appportal.visitor.beans.ResultJson;
import org.visitor.appportal.visitor.domain.City;
import org.visitor.appportal.visitor.domain.TimeZone;
import org.visitor.appportal.visitor.domain.VisitorLanguage;
import org.visitor.appportal.web.utils.ProductInfo.StatusTypeEnum;

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
	@Autowired
	private ProductRedisService productRedisService;
	@Autowired
	private VisitorCityService visitorCityService;

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
	
	@RequestMapping("cityFromRedisToDB")
	public void cityFromRedisToDB(HttpServletRequest request,
			HttpServletResponse response) {
		Integer result = 0;
		String resultDesc = "success";
		ResultJson resultJson = new ResultJson();
		resultJson.setResult(result);
		resultJson.setResultDesc(resultDesc);
		
		List<String> listCityStr = productRedisService.getCities();
		
		for (String cityStr : listCityStr) {
			City cityTemp = visitorCityService.getCityByName(cityStr);
			if (cityTemp == null) {
				cityTemp = new City();
				cityTemp.setCityName(cityStr);
				cityTemp.setCityStatus(StatusTypeEnum.Active.ordinal());
				
				visitorCityService.saveCity(cityTemp);
				productRedisService.saveCityToRedis(cityTemp);
				productRedisService.saveCityIdToKeyToRedis(cityTemp);
				
				if (log.isInfoEnabled()) {
					log.info("saved city :" + cityStr + ":");
				}
			} else {
				productRedisService.saveCityToRedis(cityTemp);
				productRedisService.saveCityIdToKeyToRedis(cityTemp);
			}
		}
		
		sendJSONResponse(resultJson, response);
	}
	
	@RequestMapping("cityFromDBToRedis")
	public void cityFromDBToRedis(HttpServletRequest request,
			HttpServletResponse response) {
		Integer result = 0;
		String resultDesc = "success";
		ResultJson resultJson = new ResultJson();
		resultJson.setResult(result);
		resultJson.setResultDesc(resultDesc);
		
		List<City> listCities = visitorCityService.getAllCities();
		
		for (City cityTemp : listCities) {
			productRedisService.saveCityToRedis(cityTemp);
			productRedisService.saveCityIdToKeyToRedis(cityTemp);
			
			if (log.isInfoEnabled()) {
				log.info("saved city to redis :" + cityTemp.getCityName() + ":");
			}
		}
		
		sendJSONResponse(resultJson, response);
	}
}
