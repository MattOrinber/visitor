package org.visitor.appportal.web.controller.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.visitor.appportal.visitor.beans.PayTemp;
import org.visitor.appportal.visitor.beans.UserTemp;
import org.visitor.appportal.web.mailutils.SendMailUtils;
import org.visitor.appportal.web.mailutils.UserMailException;

import com.alibaba.fastjson.JSON;

public class BasicController {
	protected static final Logger log = LoggerFactory.getLogger(BasicController.class);
	private ObjectMapper objectMapper;
	
	public BasicController() {	
	}
	
	public UserTemp getUserJson(HttpServletRequest request) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream in;
		try {
			in = request.getInputStream();
			byte[] buf = new byte[1024];
			for (;;) {
				int len = in.read(buf);
				if (len == -1) {
					break;
				}

				if (len > 0) {
					baos.write(buf, 0, len);
				}
			}
			if (baos.size() <= 0)
			{
				return null;
			}
			byte[] bytes = baos.toByteArray();
			String originStr = new String(bytes);
			
			UserTemp userT = JSON.parseObject(originStr, UserTemp.class);
			return userT;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public PayTemp getPayJson(HttpServletRequest request) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream in;
		try {
			in = request.getInputStream();
			byte[] buf = new byte[1024];
			for (;;) {
				int len = in.read(buf);
				if (len == -1) {
					break;
				}

				if (len > 0) {
					baos.write(buf, 0, len);
				}
			}
			if (baos.size() <= 0)
			{
				return null;
			}
			byte[] bytes = baos.toByteArray();
			String originStr = new String(bytes);
			
			PayTemp payT = JSON.parseObject(originStr, PayTemp.class);
			return payT;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
	
	public void logTheJsonResult(Object obj) {
		if (log.isInfoEnabled()) {
			String resultJsonStr;
			try {
				resultJsonStr = this.getObjectMapper().writeValueAsString(obj);
				log.info("json String result: >" + resultJsonStr + "<");
			} catch (JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
