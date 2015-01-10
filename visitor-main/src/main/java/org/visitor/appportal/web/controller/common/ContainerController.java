package org.visitor.appportal.web.controller.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.visitor.appportal.service.newsite.VisitorContainerService;
import org.visitor.appportal.service.newsite.redis.ContainerRedisService;
import org.visitor.appportal.visitor.beans.ContainerTemp;
import org.visitor.appportal.visitor.domain.Container;

@Controller
@RequestMapping("container")
public class ContainerController extends BasicController {
	protected static final Logger log = LoggerFactory.getLogger(ContainerController.class);
	
	@Autowired
	private VisitorContainerService visitorContainerService;
	@Autowired
	private ContainerRedisService containerRedisService;
	
	@RequestMapping("addOne")
	public void addOne(HttpServletRequest request,
			HttpServletResponse response) {
		ContainerTemp ct = super.getContainerJson(request);
		
		Container newCon = new Container();
		newCon.setContainerName(ct.getNameStr());
		newCon.setContainerType(Integer.valueOf(ct.getTypeStr()));
		newCon.setContainerProductkey(ct.getValueStr());
		
		visitorContainerService.saveContainer(newCon);
		containerRedisService.saveContainerToRedis(newCon);
	}
	
	@RequestMapping("updateOne")
	public void updateOne(HttpServletRequest request,
			HttpServletResponse response) {
		ContainerTemp ct = super.getContainerJson(request);
		
		Container newCon = visitorContainerService.getContainerByName(ct.getNameStr());
		newCon.setContainerType(Integer.valueOf(ct.getTypeStr()));
		newCon.setContainerProductkey(ct.getValueStr());
		
		visitorContainerService.saveContainer(newCon);
		containerRedisService.saveContainerToRedis(newCon);
	}
}
