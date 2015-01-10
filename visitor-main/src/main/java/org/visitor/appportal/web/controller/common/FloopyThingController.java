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
import org.visitor.appportal.visitor.beans.FloopyTemp;
import org.visitor.appportal.visitor.beans.ResultJson;
import org.visitor.appportal.visitor.domain.FloopyThing;
import org.visitor.appportal.web.utils.FloopyInfo;
import org.visitor.appportal.web.utils.ProductInfo.StatusTypeEnum;

@Controller
@RequestMapping("floopy")
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
	
	@RequestMapping("addOne")
	public void addOne(HttpServletRequest request,
			HttpServletResponse response) {
		FloopyTemp ftemp = super.getFloopyJson(request);
		
		FloopyThing ft = new FloopyThing();
		ft.setFloopyKey(ftemp.getKeyStr());
		ft.setFloopyValue(ftemp.getValueStr());
		ft.setFloopyDesc(ftemp.getDescStr());
		ft.setFloopyStatus(StatusTypeEnum.Active.ordinal());
		
		visitorFloopyThingService.saveFloopyThing(ft);
		floopyThingRedisService.saveFloopyThingToRedis(ft);
		
		ResultJson rj = new ResultJson();
		
		rj.setResult(0);
		rj.setResultDesc(FloopyInfo.PUBLISH_SUCCESS);
		
		super.sendJSONResponse(rj, response);
	}
	
	@RequestMapping("updateOne")
	public void updateOne(HttpServletRequest request,
			HttpServletResponse response) {
		FloopyTemp ftemp = super.getFloopyJson(request);
		
		String keyToUse = ftemp.getKeyStr();
		
		FloopyThing ft = visitorFloopyThingService.getFloopyThingUsingKey(keyToUse);
		
		ft.setFloopyValue(ftemp.getValueStr());
		ft.setFloopyDesc(ftemp.getDescStr());
		ft.setFloopyStatus(StatusTypeEnum.Active.ordinal());
		
		visitorFloopyThingService.saveFloopyThing(ft);
		floopyThingRedisService.saveFloopyThingToRedis(ft);
		
		ResultJson rj = new ResultJson();
		
		rj.setResult(0);
		rj.setResultDesc(FloopyInfo.PUBLISH_SUCCESS);
		
		super.sendJSONResponse(rj, response);
	}
}
