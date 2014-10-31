package org.visitor.appportal.web.controller.common;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.visitor.appportal.service.newsite.VisitorUserService;
import org.visitor.appportal.visitor.beans.RegisterInfo;
import org.visitor.appportal.visitor.beans.ResultJson;
import org.visitor.appportal.visitor.domain.User;

@Controller
@RequestMapping("/registerUser/")
public class UserController extends BasicController{
	protected static final Logger log = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private VisitorUserService visitorUserService;
	
	@RequestMapping("register/{emailStr}/{passMd5}")
    public void register(@PathVariable("emailStr") String mailStrParam, 
    		@PathVariable("passMd5") String passwordStrParam,
    		HttpServletResponse response) {
		long count = visitorUserService.checkUserCount(mailStrParam);
		Integer result = 0;
		String resultDesc = "";
		if (count == 0) {
			User user = new User();
			user.setUserEmail(mailStrParam);
			user.setUserPassword(passwordStrParam);
			user.setUserType(0);
			user.setUserStatus(-1);
			
			Date registerDate = new Date();
			user.setUserRegisterDate(registerDate);
			
			visitorUserService.saveUser(user);
			
			//send email
			
			//save redis
			
			result = 0;
			resultDesc = RegisterInfo.REGISTER_SUCCESS;
		} else {
			result = 1;
			resultDesc = RegisterInfo.REGISTER_EMAIL_EXISTS;
		}
		
		ResultJson resultJson = new ResultJson();
		resultJson.setResult(result);
		resultJson.setResultDesc(resultDesc);
		
		sendJSONResponse(resultJson, response);
	}
	
	@RequestMapping("register/{emailStr}/{passMd5}")
    public void login(@PathVariable("emailStr") String mailStrParam, 
    		@PathVariable("passMd5") String passwordStrParam,
    		HttpServletResponse response) {
		long count = visitorUserService.checkUserCount(mailStrParam);
		Integer result = 0;
		String resultDesc = "";
		if (count == 0) {
			User user = new User();
			user.setUserEmail(mailStrParam);
			user.setUserPassword(passwordStrParam);
			user.setUserType(0);
			user.setUserStatus(-1);
			
			Date registerDate = new Date();
			user.setUserRegisterDate(registerDate);
			
			visitorUserService.saveUser(user);
			
			//send email
			
			//save redis
			
			result = 0;
			resultDesc = RegisterInfo.REGISTER_SUCCESS;
		} else {
			result = 1;
			resultDesc = RegisterInfo.REGISTER_EMAIL_EXISTS;
		}
		
		ResultJson resultJson = new ResultJson();
		resultJson.setResult(result);
		resultJson.setResultDesc(resultDesc);
		
		sendJSONResponse(resultJson, response);
	}
}
