package org.visitor.appportal.web.controller.common;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.visitor.appportal.service.newsite.VisitorFloopyThingService;
import org.visitor.appportal.service.newsite.redis.FloopyThingRedisService;
import org.visitor.appportal.visitor.beans.FloopyInfo;
import org.visitor.appportal.visitor.beans.ResultJson;
import org.visitor.appportal.visitor.domain.FloopyThing;

@Controller
@RequestMapping("/floopy/")
public class FloopyThingController extends BasicController {
	protected static final Logger log = LoggerFactory.getLogger(FloopyThingController.class);
	
	@Autowired
	private VisitorFloopyThingService visitorFloopyThingService;
	@Autowired
	private FloopyThingRedisService floopyThingRedisService;
	
	@RequestMapping("publishAll")
	public void publishAllFloopyThings(HttpServletRequest request,
			HttpServletResponse response) {
		List<FloopyThing> lftTempList = visitorFloopyThingService.getTotalList();
		
		Integer count = 0;
		for (FloopyThing item : lftTempList) {
			floopyThingRedisService.saveFloopyThingToRedis(item);
			count ++;
		}
		
		if (log.isInfoEnabled()) {
			log.info("published floopy things count : >"+count+"<");
		}
		
		ResultJson rj = new ResultJson();
		
		rj.setResult(0);
		rj.setResultDesc(FloopyInfo.PUBLISH_SUCCESS);
		
		super.sendJSONResponse(rj, response);
	}
}
