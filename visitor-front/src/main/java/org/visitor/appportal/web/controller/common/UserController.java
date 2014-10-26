package org.visitor.appportal.web.controller.common;

import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
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
public class UserController {
	protected static final Logger log = LoggerFactory.getLogger(UserController.class);
	
	private ObjectMapper objectMapper;
	
	@Autowired
	private VisitorUserService visitorUserService;
	
	@RequestMapping("register/{emailStr}/{passMd5}")
    public void folder(@PathVariable("emailStr") String mailStrParam, 
    		@PathVariable("emailStr") String passwordStrParam,
    		HttpServletResponse response) {
		long count = visitorUserService.checkUserCount(mailStrParam);
		Integer result = 0;
		String resultDesc = "";
		if (count == 0) {
			User user = new User();
			user.setUserEmail(mailStrParam);
			user.setUserPassword(passwordStrParam);
			
			visitorUserService.saveUser(user);
			
			result = 0;
			resultDesc = RegisterInfo.REGISTER_SUCCESS;
		} else {
			result = 1;
			resultDesc = RegisterInfo.REGISTER_EMAIL_EXISTS;
		}
		
		ResultJson resultJson = new ResultJson();
		resultJson.setResult(result);
		resultJson.setResultDesc(resultDesc);
		
		try {
			String resultJsonStr = URLEncoder.encode(this.getObjectMapper().writeValueAsString(resultJson), "utf-8");
			PrintWriter out = response.getWriter();
			out.print(resultJsonStr);
			out.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	/**
	 * @return the objectMapper
	 */
	public ObjectMapper getObjectMapper() {
		if(null == objectMapper) {
			this.objectMapper = new ObjectMapper();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			objectMapper.setDateFormat(df);
			objectMapper.getDeserializationConfig().disable(Feature.FAIL_ON_UNKNOWN_PROPERTIES);
		}
		return objectMapper;
	}
}
