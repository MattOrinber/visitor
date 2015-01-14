package org.visitor.appportal.web.controller.common;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.visitor.appportal.service.newsite.S3Service;
import org.visitor.appportal.service.newsite.VisitorUserService;
import org.visitor.appportal.service.newsite.VisitorUserTokenInfoService;
import org.visitor.appportal.service.newsite.redis.FloopyThingRedisService;
import org.visitor.appportal.service.newsite.redis.TimezoneRedisService;
import org.visitor.appportal.service.newsite.redis.UserRedisService;
import org.visitor.appportal.visitor.beans.ResultJson;
import org.visitor.appportal.visitor.beans.UserTemp;
import org.visitor.appportal.visitor.domain.User;
import org.visitor.appportal.visitor.domain.UserTokenInfo;
import org.visitor.appportal.web.mailutils.SendMailUtils;
import org.visitor.appportal.web.mailutils.UserMailException;
import org.visitor.appportal.web.utils.EncryptionUtil;
import org.visitor.appportal.web.utils.MixAndMatchUtils;
import org.visitor.appportal.web.utils.PaypalInfo;
import org.visitor.appportal.web.utils.RegisterInfo;
import org.visitor.appportal.web.utils.WebInfo;
import org.visitor.appportal.web.utils.ProductInfo.StatusTypeEnum;
import org.visitor.appportal.web.utils.RegisterInfo.UserTypeEnum;

@Controller
@RequestMapping("/registerUser/")
public class UserController extends BasicController{
	protected static final Logger log = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private VisitorUserService visitorUserService;
	@Autowired
	private VisitorUserTokenInfoService visitorUserTokenInfoService;
	@Autowired
	private UserRedisService userRedisService;
	@Autowired
	private TimezoneRedisService timezoneRedisService;
	@Autowired
	private S3Service s3Service;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private FloopyThingRedisService floopyThingRedisService;
	
	@RequestMapping("register")
    public void register(HttpServletRequest request,
    		HttpServletResponse response) {
		UserTemp ut = super.getUserJson(request);
		
		logTheJsonResult(ut);
		String mailStrParam = ut.getEmailStr();
		String passwordStrParam = ut.getPasswordStr();
		
		long count = visitorUserService.checkUserCount(mailStrParam);
		Integer result = 0;
		String resultDesc = "";
		ResultJson rj = new ResultJson();
		if (count == 0) {
			User user = new User();
			user.setUserEmail(mailStrParam);
			user.setUserPassword(passwordStrParam);
			user.setUserType(0); //0----normal user,1---facebook user
			user.setUserFirstName(ut.getFirstNameStr());
			user.setUserLastName(ut.getLastNameStr());
			user.setUserStatus(0);
			
			Date registerDate = new Date();
			user.setUserRegisterDate(registerDate);
			
			visitorUserService.saveUser(user);
			
			//send email
			
			//save redis
			userRedisService.saveUserPassword(user);
			
			String resultToken = this.getAndSaveUserToken(response, mailStrParam, passwordStrParam, true);
			
			String userNameReturned = "";
			if (StringUtils.isNotEmpty(user.getUserFirstName())) {
				userNameReturned = userNameReturned + user.getUserFirstName();
			}
			
			if (StringUtils.isNotEmpty(user.getUserLastName())) {
				userNameReturned = userNameReturned + user.getUserLastName();
			}
			
			if (StringUtils.isEmpty(userNameReturned)) {
				userNameReturned = mailStrParam.substring(0, mailStrParam.indexOf("@"));
			}
			rj.setUserName(userNameReturned);
			rj.setUserEmail(user.getUserEmail());
			rj.setToken(resultToken);
			
			result = 0;
			resultDesc = RegisterInfo.REGISTER_SUCCESS;
			
			logTheRegisterTime(mailStrParam);
		} else {
			result = -1;
			resultDesc = RegisterInfo.REGISTER_EMAIL_EXISTS;
			rj.setUserName("--");
		}
		
		rj.setUserPicUrl("--");
		
		rj.setResult(result);
		rj.setResultDesc(resultDesc);
		
		setResultToClient(response, rj);
	}
	
	@RequestMapping("login")
    public void login(HttpServletRequest request,
    		HttpServletResponse response) {
		UserTemp ut = super.getUserJson(request);
		
		logTheJsonResult(ut);
		String mailStrParam = ut.getEmailStr();
		String passwordStrParam = ut.getPasswordStr();
		
		ResultJson rj = checkIfTheUserLegal(mailStrParam, passwordStrParam);
		
		if (rj.getResult() >= 0) {
			
			User userT = userRedisService.getUserPassword(mailStrParam);
			
			if (userT.getUserStatus().intValue() == StatusTypeEnum.Active.ordinal()) {
				String tokenStr = this.getAndSaveUserToken(response, mailStrParam, passwordStrParam, false);
				userRedisService.saveUserToken(mailStrParam, tokenStr);
				
				rj.setToken(tokenStr);
				rj.setUserEmail(mailStrParam);
			} else {
				rj.setResult(-1);
				rj.setResultDesc("user disabled!");
			}
		}
		
		setResultToClient(response, rj);
	}
	
	@RequestMapping("logout")
    public void logout(HttpServletRequest request,
    		HttpServletResponse response) {
		UserTemp ut = super.getUserJson(request);
		
		logTheJsonResult(ut);
		
		ResultJson rj = null;
		
		String mailStrParam;
		try {
			String mailOri = ut.getEmailStr();
			String tokenStr = ut.getPasswordStr();
			if (StringUtils.isNotEmpty(mailOri) && StringUtils.isNotEmpty(tokenStr)) {
				mailStrParam = URLDecoder.decode(mailOri, "UTF-8");
				rj = this.checkIfTokenLegal(mailStrParam, tokenStr);
			} 
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setResultToClient(response, rj);
	}
	
	@RequestMapping("retrievepassword")
	public void retrieveUserPassword(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		//update mysql
		Integer result = 0;
		String resultDesc = "Success";
		
		UserTemp ut = super.getUserJson(request);
		logTheJsonResult(ut);
		
		String userEmailStr = ut.getEmailStr();
		if (StringUtils.isNotEmpty(userEmailStr)) {
			User user = userRedisService.getUserPassword(userEmailStr);
			
			if (user != null) {
				String resetTokenStr = EncryptionUtil.getMD5(UUID.randomUUID().toString());
				String resetURL = floopyThingRedisService.getFloopyValueSingle(PaypalInfo.floopy_paypalCallBackURL)+"/day/resetpass?token=" + resetTokenStr +"&mail=" + URLEncoder.encode(userEmailStr, "UTF-8");
				
				userRedisService.setUserResetPasswordToken(user.getUserEmail(), resetTokenStr);
				
				//send email
				
				String usernameStr = user.getUserFirstName();
				if (StringUtils.isEmpty(usernameStr)) {
					String emailNameStr = StringUtils.substring(userEmailStr, 0, StringUtils.indexOf(userEmailStr, '@'));
					usernameStr = emailNameStr;
				}
				
				SendMailUtils sendMU = new SendMailUtils();
				Locale tLocale = new Locale("en");
				sendMU.setTitle(MixAndMatchUtils.getLocaleSpecificString(messageSource, "type_user_action_resetpass", tLocale));
				sendMU.setContent(MixAndMatchUtils.getLocaleSpecificString(messageSource, "mail_reset_pwd_content_part_1", tLocale) 
						+ usernameStr
						+ MixAndMatchUtils.getLocaleSpecificString(messageSource, "mail_reset_pwd_content_part_2", tLocale)
						+ resetURL
						+ MixAndMatchUtils.getLocaleSpecificString(messageSource, "mail_reset_pwd_content_part_3", tLocale));
				sendMU.setToAddress(userEmailStr);
				try
				{
					sendMU.sendEmailHtml();
				} catch (UserMailException e)
				{
					if (log.isInfoEnabled()) {
						log.info(e.getMessage());
					}
					e.printStackTrace();
					result = -1;
					resultDesc = "send email failed";
				}
			} else {
				result = -1;
				resultDesc = "user not registered";
			}
		} else {
			result = -1;
			resultDesc = "user email passed is null";
		}
		ResultJson rj = new ResultJson();
		rj.setResult(result);
		rj.setResultDesc(resultDesc);
		
		setResultToClient(response, rj);
	}
	
	@RequestMapping("updateUserPassword")
	public void updateUserPassword(HttpServletRequest request,
			HttpServletResponse response) {
		UserTemp ut = super.getUserJson(request);
		logTheJsonResult(ut);
		
		ResultJson rj = new ResultJson();
		
		Integer result = 0;
		String resultDesc = "reset password success";
		
		String emailStr = ut.getEmailStr();
		String tokenStr = ut.getUserTokenStr();
		String newPassStr = ut.getNewPassStr();
		
		if (StringUtils.isNotEmpty(emailStr) && StringUtils.isNotEmpty(tokenStr) && StringUtils.isNotEmpty(newPassStr)) {
			String storedToken = userRedisService.getUserresetPasswordToken(emailStr);
			
			if (StringUtils.isNotEmpty(storedToken) && StringUtils.equals(tokenStr, storedToken)) {
				User user = userRedisService.getUserPassword(emailStr);
				
				if (user != null) {
					user.setUserPassword(newPassStr);
					
					visitorUserService.saveUser(user);
					userRedisService.saveUserPassword(user);
				} else {
					User userT = visitorUserService.findUserByEmail(emailStr);
					
					if (userT != null) {
						userT.setUserPassword(newPassStr);
						
						visitorUserService.saveUser(userT);
						userRedisService.saveUserPassword(userT);
					} else {
						result = -1;
						resultDesc = "user does not exist";
					}
				}
				
			} else {
				result = -1;
				resultDesc = "token expired";
			}
			
		} else {
			result = -1;
			resultDesc = "parammeter not right";
		}
		
		rj.setResult(result);
		rj.setResultDesc(resultDesc);
		
		setResultToClient(response, rj);
	}
	
	@RequestMapping("getInternalMailCount")
    public void getInternalMailCount(HttpServletRequest request,
    		HttpServletResponse response) {
		String userEmailStr = request.getParameter("userEmailStr");
		
		Integer count = userRedisService.getUserInternalMailUnreadCount(userEmailStr);
		
		ResultJson rj = new ResultJson();
		rj.setResult(count);
		rj.setResultDesc("success");
		
		setResultToClient(response, rj);
	}
	
	private ResultJson checkIfTokenLegal(String userMailStr, String userTokenInfoStr) {
		ResultJson rj = new ResultJson();
		Integer result = -1;
		String resultDesc = "failed to logout";
		
		UserTokenInfo userTi = userRedisService.getUserTokenInfo(userMailStr);
		
		if (userTi == null) {
			
			//get from database
			UserTokenInfo userTiNew = visitorUserTokenInfoService.getUserTokenInfoByUserEmail(userMailStr);
			
			if (userTiNew != null) {
				String storedAccessToken = userTiNew.getUfiAccessToken();
				Date expireDate = userTiNew.getUfiExpireDate();
				Date nowDate = new Date();
				
				if (StringUtils.isNotEmpty(storedAccessToken) && nowDate.before(expireDate)) {
					if (StringUtils.equals(userTokenInfoStr, storedAccessToken)) {
						userTiNew.setUfiExpireDate(nowDate);
						visitorUserTokenInfoService.saveUserTokenInfo(userTiNew);
						userRedisService.saveUserTokenInfo(userTiNew);
						
						result = 0;
						resultDesc = "logout success";
					}
				}
			}
			
		} else {
			String storedAccessToken = userTi.getUfiAccessToken();
			Date expireDate = userTi.getUfiExpireDate();
			Date nowDate = new Date();
			
			if (StringUtils.isNotEmpty(storedAccessToken) && nowDate.before(expireDate)) {
				if (StringUtils.equals(userTokenInfoStr, storedAccessToken)) {
					userTi.setUfiExpireDate(nowDate);
					visitorUserTokenInfoService.saveUserTokenInfo(userTi);
					userRedisService.saveUserTokenInfo(userTi);
					
					result = 0;
					resultDesc = "logout success";
				}
			}
		}
		
		rj.setResult(result);
		rj.setResultDesc(resultDesc);
		
		return rj;
	}
	
	@RequestMapping("postDetail")
	public void postUserDetail(HttpServletRequest request, HttpServletResponse response) throws ParseException {
		UserTemp ut = super.getUserJson(request);
		
		logTheJsonResult(ut);
		
		//update mysql
		String mailStrParam = ut.getEmailStr();
		String passwordStrParam = ut.getPasswordStr();
		Integer result = 0;
		String resultDesc = "";
		
		ResultJson rj = checkIfTheUserLegal(mailStrParam, passwordStrParam);
		
		if (rj.getResult().intValue() >= 0) {
			User user = userRedisService.getUserPassword(mailStrParam);
			
			//update mysql
			user.setUserAddress(ut.getAddressStr());
			String birthDateT = ut.getBirthDateStr();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
			Date dateT = sdf.parse(birthDateT);
			user.setUserBirthdate(dateT);
			
			user.setUserFirstName(ut.getFirstNameStr());
			user.setUserLastName(ut.getLastNameStr());
			user.setUserGender(MixAndMatchUtils.getGenderInteger(ut.getGenderStr()));
			user.setUserLanguage(ut.getLanguageSpokenSelect());
			user.setUserPhonenum(ut.getPhoneNumberStr()); //phone number not here
			user.setUserSchool(ut.getSchoolStr());
			user.setUserWork(ut.getWorkStr());
			String userEmergencyStr = ut.getEmerNameStr() + WebInfo.SPLIT 
					+ ut.getEmerEmailStr() + WebInfo.SPLIT
					+ ut.getEmerPhoneStr() + WebInfo.SPLIT
					+ ut.getEmerRelationshipStr();
			user.setUserEmergency(userEmergencyStr);
			
			Integer userTimeZoneInt = timezoneRedisService.getTimeZoneId(ut.getTimeZoneStr());
			
			user.setUserTimeZone(userTimeZoneInt);
			
			user.setUserDetail(ut.getDescriptionStr());
			
			//mysql
			visitorUserService.saveUser(user);
			//redis
			userRedisService.saveUserPassword(user);
			
			result = 0;
			resultDesc = RegisterInfo.UPDATE_SUCCESS;
			
			rj.setResult(result);
			rj.setResultDesc(resultDesc);
		}
		
		setResultToClient(response, rj);
	}
	
	private ResultJson checkIfTheUserLegal(String mailStrParam, String passwordStrParam) {
		Integer result = 0;
		String resultDesc = "";
		ResultJson resultJson = new ResultJson();
		
		resultJson.setUserName("--");
		
		User user = userRedisService.getUserPassword(mailStrParam);
		
		if (user == null) {
			
			//get from database
			User userT = visitorUserService.getUserFromEmailAndPassword(mailStrParam, passwordStrParam);
			
			if (userT == null) {
				result = -3;
				resultDesc = RegisterInfo.LOGIN_FAILED_USER_NOTEXISTED;
			} else {
				result = 0;
				resultDesc = RegisterInfo.LOGIN_SUCCESS;
				Date loginDate = new Date();
				userT.setUserLastLoginTime(loginDate);
				
				//save to database and redis
				visitorUserService.saveUser(userT);
				userRedisService.saveUserPassword(userT);
				resultJson.setUserLoginTime(loginDate);
				if (userT.getUserPhotourl() != null) {
					resultJson.setUserPicUrl(userT.getUserPhotourl());
				} else {
					resultJson.setUserPicUrl("--");
				}
				
				String userFirstNameStr = userT.getUserFirstName();
				String userLastNameStr = userT.getUserLastName();
				String loginEmail = userT.getUserEmail();
				
				String userNameReturned = "";
				if (StringUtils.isNotEmpty(userFirstNameStr)) {
					userNameReturned = userNameReturned + " " + userFirstNameStr;
				}
				
				if (StringUtils.isNotEmpty(userLastNameStr)) {
					userNameReturned = userNameReturned + " " + userLastNameStr;
				}
				
				if (StringUtils.isEmpty(userNameReturned)) {
					if (StringUtils.isNotEmpty(loginEmail)) {
						userNameReturned = loginEmail.substring(0, loginEmail.indexOf("@"));
					} else {
						userNameReturned = "--";
					}
				}

				resultJson.setUserName(userNameReturned);
				
				logTheLogintime(mailStrParam);
			}
			
		} else {
			if (StringUtils.equals(user.getUserPassword(), passwordStrParam)) {
				result = 0;
				resultDesc = RegisterInfo.LOGIN_SUCCESS;
				
				Date loginDate = new Date();
				user.setUserLastLoginTime(loginDate);
				
				visitorUserService.saveUser(user);
				userRedisService.saveUserPassword(user);
				resultJson.setUserLoginTime(loginDate);
				if (user.getUserPhotourl() != null) {
					resultJson.setUserPicUrl(user.getUserPhotourl());
				} else {
					resultJson.setUserPicUrl("--");
				}
				
				String userFirstNameStr = user.getUserFirstName();
				String userLastNameStr = user.getUserLastName();
				String loginEmail = user.getUserEmail();
				
				String userNameReturned = "";
				if (StringUtils.isNotEmpty(userFirstNameStr)) {
					userNameReturned = userNameReturned + " " + userFirstNameStr;
				}
				
				if (StringUtils.isNotEmpty(userLastNameStr)) {
					userNameReturned = userNameReturned + " " + userLastNameStr;
				}
				
				if (StringUtils.isEmpty(userNameReturned)) {
					if (StringUtils.isNotEmpty(loginEmail)) {
						userNameReturned = loginEmail.substring(0, loginEmail.indexOf("@"));
					} else {
						userNameReturned = "--";
					}
				}
				resultJson.setUserName(userNameReturned);
				
				logTheLogintime(mailStrParam);
			} else {
				result = -2;
				resultDesc = RegisterInfo.LOGIN_FAILED_PASSWORD_NOT_RIGHT;
			}
		}
		
		resultJson.setResult(result);
		resultJson.setResultDesc(resultDesc);
		return resultJson;
	}
	
	private String getAndSaveUserToken(HttpServletResponse response, String mailStrParam, String passwordStrParam, boolean ifNeedCreate) {
		User user = visitorUserService.getUserFromEmailAndPassword(mailStrParam, passwordStrParam);
		
		UserTokenInfo uti = null;
		
		if (ifNeedCreate) {
			uti = new UserTokenInfo();
		} else {
			uti = userRedisService.getUserTokenInfo(mailStrParam);
			if (uti == null) {
				uti = visitorUserTokenInfoService.getUserTokenInfoByUserEmail(mailStrParam);
			}
			
			if (uti == null) {
				uti = new UserTokenInfo();
			}
		}
		
		String accessTokenOri = UUID.randomUUID().toString();
		String accessTokenStr = EncryptionUtil.getMD5(accessTokenOri);
		uti.setUfiUserId(user.getUserId());
		uti.setUfiUserEmail(user.getUserEmail());
		
		long ufiExpireDateLong = System.currentTimeMillis() + 2592000000L;
		uti.setUfiExpireDate(new Date(ufiExpireDateLong));
		uti.setUfiAuthCode(accessTokenStr);
		uti.setUfiAccessToken(accessTokenStr);
		
		visitorUserTokenInfoService.saveUserTokenInfo(uti);
		userRedisService.saveUserTokenInfo(uti);
		MixAndMatchUtils.setUserCookie(response, user.getUserEmail(), uti.getUfiAccessToken(), MixAndMatchUtils.param_user_token_expire);
		
		return accessTokenStr;
	}
	
	private void setResultToClient(HttpServletResponse response, ResultJson resultJson) {
		sendJSONResponse(resultJson, response);
	}
	
	private void logTheRegisterTime(String emailStr) {
		if (log.isInfoEnabled()) {
			log.info("<user register>: >" + emailStr + "<");
		}
	}
	
	public static void main(String[]args) throws UnsupportedEncodingException {
		String toEncode = "http://ec2-54-169-2-129.ap-southeast-1.compute.amazonaws.com:8080/registerUser/facebook";
		String result = URLEncoder.encode(toEncode, "UTF-8");
		System.out.println(result);
		
		String emailStrOri = "wumengjz\u0040gmail.com";
		String strOri = URLDecoder.decode(emailStrOri, "UTF-8");
		System.out.println(strOri);
	}
}
