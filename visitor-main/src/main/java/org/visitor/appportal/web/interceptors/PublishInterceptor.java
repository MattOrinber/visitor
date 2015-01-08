package org.visitor.appportal.web.interceptors;

import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.visitor.appportal.service.newsite.redis.UserRedisService;
import org.visitor.appportal.visitor.beans.ResultJson;
import org.visitor.appportal.visitor.domain.User;
import org.visitor.appportal.visitor.domain.UserTokenInfo;
import org.visitor.appportal.web.utils.MixAndMatchUtils;
import org.visitor.appportal.web.utils.WebInfo;

public class PublishInterceptor implements HandlerInterceptor {
	protected static final Logger log = LoggerFactory.getLogger("classLogger");
	
	@Autowired
	private UserRedisService userRedisService;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// TODO Auto-generated method stub
		
		Cookie[] cookieArray = request.getCookies();
		if (cookieArray != null && cookieArray.length > 0) {
			
			Map<String, String> cookieMap = new HashMap<String, String>();
			
			for (int j = 0; j < cookieArray.length; j ++) {
				Cookie tmpCookie = cookieArray[j];
				cookieMap.put(tmpCookie.getName(), tmpCookie.getValue());
			}
			
			String userMailStrOri = cookieMap.get(MixAndMatchUtils.COOKIE_NAME_USER_EMAIL);
			String userTokenInfoStr = cookieMap.get(MixAndMatchUtils.COOKIE_NAME_USER_ACCESS_TOKEN);
			
			log.info("interceptor mail: >" + userMailStrOri + "<");
			log.info("interceptor token: >" + userTokenInfoStr + "<");
		
			if (StringUtils.isNotEmpty(userMailStrOri) && StringUtils.isNotEmpty(userTokenInfoStr)) {
				String userMailStr = URLDecoder.decode(userMailStrOri, "UTF-8");
				String userTokenStored = userRedisService.getUserToken(userMailStrOri);
				
				if (StringUtils.isNotEmpty(userTokenStored)) {
					if (StringUtils.equals(userTokenInfoStr, userTokenStored)) {
						User userT = userRedisService.getUserPassword(userMailStr);
						request.setAttribute(WebInfo.UserID, userT);
						return true;
					}
				}
			} 
		}
		
		Integer result = -1;
		String resultDesc = "Not authorized";
		ResultJson resultJson = new ResultJson();
		resultJson.setResult(result);
		resultJson.setResultDesc(resultDesc);
		
		WebInfo.sendJSONResponse(resultJson, response);
		
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
