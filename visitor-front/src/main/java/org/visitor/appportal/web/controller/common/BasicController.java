package org.visitor.appportal.web.controller.common;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.visitor.appportal.web.mailutils.SendMailUtils;
import org.visitor.appportal.web.mailutils.UserMailException;

public class BasicController {
	protected static final Logger log = LoggerFactory.getLogger(BasicController.class);
	private ObjectMapper objectMapper;
	
	public BasicController() {	
	}
	
	//send JSON response utility
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
	
	
	//send email utility
	public void sendEmail(String title, String content, String toAddress) {
		SendMailUtils sendMU = new SendMailUtils();
		sendMU.setTitle(title);
		sendMU.setContent(content);
		sendMU.setToAddress(toAddress);
		try
		{
			sendMU.sendEmailHtml();
		} catch (UserMailException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (log.isInfoEnabled()) {
				log.info("sendmail exception: >"+e.getMessage()+"<");
			}
		}
	}
}
