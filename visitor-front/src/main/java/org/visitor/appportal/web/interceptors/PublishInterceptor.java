package org.visitor.appportal.web.interceptors;

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
import org.visitor.appportal.web.utils.EncryptionUtil;
import org.visitor.appportal.web.utils.WebInfo;

public class PublishInterceptor implements HandlerInterceptor {
	protected static final Logger log = LoggerFactory.getLogger("classLogger");
	
	@Autowired
	private UserRedisService userRedisService;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// TODO Auto-generated method stub
		String userEmailT = request.getParameter(WebInfo.UserEmailStr);
		String userPasswordT = request.getParameter(WebInfo.UserPasswordStr);
		
		if (StringUtils.isNotEmpty(userEmailT) && StringUtils.isNotEmpty(userPasswordT)) {
			User userT = userRedisService.getUserPassword(userEmailT);
			String md5Ori = userT.getUserEmail() + WebInfo.SPLIT + userT.getUserPassword();
			String md5Final = EncryptionUtil.getMD5(md5Ori);
			
			if (StringUtils.equals(userPasswordT, md5Final)) {
				if (log.isInfoEnabled()) {
					log.info("authorization passed for user: >"+userEmailT+"<");
				}
				return true;
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
