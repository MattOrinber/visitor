package org.visitor.appportal.web.controller.common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.visitor.appportal.service.newsite.VisitorUserService;
import org.visitor.appportal.service.newsite.VisitorUserTokenInfoService;
import org.visitor.appportal.service.newsite.redis.UserRedisService;
import org.visitor.appportal.visitor.domain.User;
import org.visitor.appportal.visitor.domain.UserTokenInfo;
import org.visitor.appportal.web.facebook.TokenBean;
import org.visitor.appportal.web.facebook.UserBean;
import org.visitor.appportal.web.utils.HttpClientUtil;
import org.visitor.appportal.web.utils.MixAndMatchUtils;
import org.visitor.appportal.web.utils.RegisterInfo.UserTypeEnum;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

@Controller
@RequestMapping("/facebook/")
public class FacebookController extends BasicController {
	protected static final Logger log = LoggerFactory.getLogger(FacebookController.class);
	
	@Autowired
	private VisitorUserTokenInfoService visitorUserTokenInfoService;
	@Autowired
	private VisitorUserService visitorUserService;
	@Autowired
	private UserRedisService userRedisService;
	
	@RequestMapping("callback")
	public String doFacebookReturn(HttpServletRequest request,
			HttpServletResponse response,
			Model model) {
		
		//get authorization code
		String codeStr = request.getParameter("code");
		if (StringUtils.isNotEmpty(codeStr)) {
			log.info("code: >"+codeStr+"<");
			try {
				//get the access token
				String facebookCallbackUrl = MixAndMatchUtils.getSystemAwsPaypalConfig(MixAndMatchUtils.facebookCallbackURL);
				String facebookAppId = MixAndMatchUtils.getSystemAwsPaypalConfig(MixAndMatchUtils.facebookAppId);
				String facebookAppSecret = MixAndMatchUtils.getSystemAwsPaypalConfig(MixAndMatchUtils.facebookAppSecret);
				String facebookAccessTokenURL = MixAndMatchUtils.getSystemAwsPaypalConfig(MixAndMatchUtils.facebookAccessTokenURL);
				String callURL = facebookAccessTokenURL + "?" + MixAndMatchUtils.param_facebook_client_id + "=" + facebookAppId
						+ "&" + MixAndMatchUtils.param_facebook_redirect_uri + "=" + URLEncoder.encode(facebookCallbackUrl, "UTF-8")
						+ "&" + MixAndMatchUtils.param_facebook_client_secret + "=" + facebookAppSecret
						+ "&" + MixAndMatchUtils.param_facebook_auth_code + "=" + codeStr;
				
				String tokenResult = HttpClientUtil.httpGet(callURL);
				if (StringUtils.isNotEmpty(tokenResult)) {
					TokenBean tbTemp = this.getAccessToken(tokenResult);
					
					if (StringUtils.isNotEmpty(tbTemp.getAccess_token()) && tbTemp.getExpires() != null && tbTemp.getExpires().longValue() > 0) {
						// get user info
						String getUserURL = MixAndMatchUtils.getSystemAwsPaypalConfig(MixAndMatchUtils.facebookGetUserInfoURL) + "?"
								+ MixAndMatchUtils.param_facebook_access_token + "=" + tbTemp.getAccess_token();
						
						String userInfoResult = HttpClientUtil.httpGet(getUserURL);
						
						log.info("userInfoResult: >" +userInfoResult+"<");
						if (StringUtils.isNotEmpty(userInfoResult)) {
							UserBean ubTemp = this.getUserInfo(userInfoResult);
							if (StringUtils.isNotEmpty(ubTemp.getId()) && StringUtils.isNotEmpty(ubTemp.getEmail())) {
								String getUserPictureURL = MixAndMatchUtils.getSystemAwsPaypalConfig(MixAndMatchUtils.facebookGetUserPictureURL) + "?"
										+ MixAndMatchUtils.param_facebook_access_token + "=" + tbTemp.getAccess_token() + "&" 
										+ MixAndMatchUtils.param_facebook_redirect + "=false";
								log.info("getUserPictureURL: >" + getUserPictureURL + "<");
								String userPictureResult = HttpClientUtil.httpGet(getUserPictureURL);
								log.info("userPictureResult: >" +userPictureResult +"<");
								String userPictureURLFinal = this.getUserPictureURLFinal(userPictureResult);
								log.info("userPictureURLFinal: >" + userPictureURLFinal +"<");
								
								//add user and add user
								String emailStrFacebook = ubTemp.getEmail();
								User user = userRedisService.getUserPassword(emailStrFacebook);
								if (user == null) {
									user = visitorUserService.findUserByEmail(emailStrFacebook);
								}
								
								if (user == null) {
									user = new User();
									UserTokenInfo uti = new UserTokenInfo();
									
									user.setUserEmail(ubTemp.getEmail());
									user.setUserPassword("123456");
									user.setUserType(UserTypeEnum.FacebookUser.getValue());
									user.setUserStatus(0);
									user.setUserFirstName(ubTemp.getFirst_name());
									user.setUserLastName(ubTemp.getLast_name());
									user.setUserGender(MixAndMatchUtils.getGenderInteger(ubTemp.getGender()));
									user.setUserFacebookId(ubTemp.getId());
									user.setUserPhotourl(userPictureURLFinal);
									long currentMilis = System.currentTimeMillis();
									Date registerDate = new Date(currentMilis);
									user.setUserRegisterDate(registerDate);
									
									visitorUserService.saveUser(user);
									userRedisService.saveUserPassword(user);
									
									uti.setUfiUserId(user.getUserId());
									uti.setUfiUserEmail(user.getUserEmail());
									uti.setUfiAuthCode(codeStr);
									uti.setUfiAccessToken(tbTemp.getAccess_token());
									uti.setUfiDetailUrl(ubTemp.getLink());
									
									long expireFinal = currentMilis + 1000*tbTemp.getExpires().longValue();
									uti.setUfiExpireDate(new Date(expireFinal));
									
									visitorUserTokenInfoService.saveUserTokenInfo(uti);
									userRedisService.saveUserTokenInfo(uti);
									MixAndMatchUtils.setUserCookie(response, user.getUserEmail(), tbTemp.getAccess_token(), tbTemp.getExpires().intValue());
									
									MixAndMatchUtils.setUserModel(model, user);
								} else {
									Date loginDate = new Date();
									user.setUserLastLoginTime(loginDate);
									visitorUserService.saveUser(user);
									userRedisService.saveUserPassword(user);
									
									UserTokenInfo uti = userRedisService.getUserTokenInfo(emailStrFacebook);
									if (uti == null) {
										uti = visitorUserTokenInfoService.getUserTokenInfoByUserEmail(emailStrFacebook);
									}
									if (uti == null) {
										uti = new UserTokenInfo();
									}
									
									uti.setUfiUserId(user.getUserId());
									uti.setUfiUserEmail(user.getUserEmail());
									uti.setUfiAuthCode(codeStr);
									uti.setUfiAccessToken(tbTemp.getAccess_token());
									uti.setUfiDetailUrl(ubTemp.getLink());
									
									long currentMilis = System.currentTimeMillis();
									
									long expireFinal = currentMilis + 1000*tbTemp.getExpires().longValue();
									uti.setUfiExpireDate(new Date(expireFinal));
									
									visitorUserTokenInfoService.saveUserTokenInfo(uti);
									userRedisService.saveUserTokenInfo(uti);
									MixAndMatchUtils.setUserCookie(response, user.getUserEmail(), tbTemp.getAccess_token(), tbTemp.getExpires().intValue());
									
									MixAndMatchUtils.setUserModel(model, user);
								}
							}
						}
					}
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
		} else {
			log.info("no authorization code returned");
		}
		
		super.setModel(request, response, model, true);
		return "redirect:/index";
	}
	
	private TokenBean getAccessToken(String sourceStr) {
		TokenBean tb = new TokenBean();
		String[] splitOne = sourceStr.split("&");
		Map<String, String> paramMaps = new LinkedHashMap<String, String>();
		if (splitOne.length >= 2) {
			for (int i = 0; i < splitOne.length; i ++) {
				String[] splitTwo = splitOne[i].split("=");
				
				if (splitTwo.length != 2) {
					log.info("innner params format not right! >"+splitOne[i]+"<");
				} else {
					paramMaps.put(splitTwo[0], splitTwo[1]);
				}
			}
			
			if (paramMaps.containsKey(MixAndMatchUtils.param_facebook_access_token)) {
				tb.setAccess_token(paramMaps.get(MixAndMatchUtils.param_facebook_access_token));
			}
			if (paramMaps.containsKey(MixAndMatchUtils.param_facebook_expires)) {
				String expireSecondsStr = paramMaps.get(MixAndMatchUtils.param_facebook_expires);
				Long expireSeconds = Long.valueOf(expireSecondsStr);
				tb.setExpires(expireSeconds);
			}
			
		} else {
			log.info("not right format accesstoken response: >"+sourceStr+"<");
		}
		
		return tb;
	}
	
	private UserBean getUserInfo(String userInfoStr) {
		UserBean ub = new UserBean();
		Map<String, String> mapOri = JSON.parseObject(userInfoStr, new TypeReference<Map<String, String>>(){});
		
		if (mapOri.containsKey("id")) {
			ub.setId(mapOri.get("id"));
		}
		
		if (mapOri.containsKey("email")) {
			ub.setEmail(mapOri.get("email"));
		}
		
		if (mapOri.containsKey("first_name")) {
			ub.setFirst_name(mapOri.get("first_name"));
		}
		
		if (mapOri.containsKey("gender")) {
			ub.setGender(mapOri.get("gender"));
		}
		
		if (mapOri.containsKey("last_name")) {
			ub.setLast_name(mapOri.get("last_name"));
		}
		
		if (mapOri.containsKey("link")) {
			ub.setLink(mapOri.get("link"));
		}
		
		return ub;
	}
	
	private String getUserPictureURLFinal(String userPictureStr) {
		String result = "";
		Map<String, String> mapOri = JSON.parseObject(userPictureStr, new TypeReference<Map<String, String>>(){});
		
		if (mapOri.containsKey("data")) {
			String dataStr = mapOri.get("data");
			Map<String, String> mapData = JSON.parseObject(dataStr, new TypeReference<Map<String, String>>(){});
			
			if (mapData.containsKey("url")) {
				result = mapData.get("url");
			}
		}
		
		return result;
	}
}
