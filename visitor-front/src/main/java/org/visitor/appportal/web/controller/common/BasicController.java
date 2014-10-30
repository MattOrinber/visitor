package org.visitor.appportal.web.controller.common;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.DeserializationConfig.Feature;

public class BasicController {
	private ObjectMapper objectMapper;
	
	public BasicController() {	
	}
	
	public void sendJSONResponse(Object obj, HttpServletResponse response) {
		try {
			String resultJsonStr = this.getObjectMapper().writeValueAsString(obj);
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
	private ObjectMapper getObjectMapper() {
		if(null == objectMapper) {
			this.objectMapper = new ObjectMapper();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			objectMapper.setDateFormat(df);
			objectMapper.getDeserializationConfig().disable(Feature.FAIL_ON_UNKNOWN_PROPERTIES);
		}
		return objectMapper;
	}
}
