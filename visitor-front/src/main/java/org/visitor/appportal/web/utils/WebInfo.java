package org.visitor.appportal.web.utils;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.DeserializationConfig.Feature;

public class WebInfo {
	public static final String SPLIT = "---";
	
	public static final String UserEmailStr = "userLoginEmail";
	public static final String UserPasswordStr = "userLoginToken";
	
	public static final String UserID = "userLoginId";
	
	private static ObjectMapper objectMapper;
	
	public static ObjectMapper getObjectMapperInstance() {
		if(null == objectMapper) {
			objectMapper = new ObjectMapper();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			objectMapper.setDateFormat(df);
			objectMapper.getDeserializationConfig().disable(Feature.FAIL_ON_UNKNOWN_PROPERTIES);
		}
		return objectMapper;
	}
	
	public static void sendJSONResponse(Object obj, HttpServletResponse response) throws Exception {
			String resultJsonStr = getObjectMapperInstance().writeValueAsString(obj);
			PrintWriter out = response.getWriter();
			out.print(resultJsonStr);
			out.flush();
	}
}
