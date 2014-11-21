package org.visitor.appportal.web.controller.common;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.visitor.appportal.service.newsite.S3Service;
import org.visitor.appportal.service.newsite.SystemPreference;
import org.visitor.appportal.service.newsite.VisitorUserService;
import org.visitor.appportal.service.newsite.mongo.UserMongoService;
import org.visitor.appportal.service.newsite.redis.TimezoneRedisService;
import org.visitor.appportal.service.newsite.redis.UserRedisService;
import org.visitor.appportal.visitor.beans.RegisterInfo;
import org.visitor.appportal.visitor.beans.ResultJson;
import org.visitor.appportal.visitor.beans.UserTemp;
import org.visitor.appportal.visitor.beans.mongo.UserMongoBean;
import org.visitor.appportal.visitor.domain.User;
import org.visitor.appportal.web.utils.EncryptionUtil;
import org.visitor.appportal.web.utils.WebInfo;

import com.amazonaws.services.s3.model.ObjectMetadata;

@Controller
@RequestMapping("/registerUser/")
public class UserController extends BasicController{
	protected static final Logger log = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private VisitorUserService visitorUserService;
	@Autowired
	private UserMongoService userMongoService;
	@Autowired
	private UserRedisService userRedisService;
	@Autowired
	private TimezoneRedisService timezoneRedisService;
	@Autowired
	private S3Service s3Service;
	@Autowired
	private SystemPreference systemPreference;
	
	@RequestMapping("register/{emailStr}/{passMd5}")
    public void register(@PathVariable("emailStr") String mailStrParam, 
    		@PathVariable("passMd5") String passwordStrParam,
    		HttpServletResponse response) {
		long count = visitorUserService.checkUserCount(mailStrParam);
		Integer result = 0;
		String resultDesc = "";
		ResultJson rj = new ResultJson();
		if (count == 0) {
			User user = new User();
			user.setUserEmail(mailStrParam);
			user.setUserPassword(passwordStrParam);
			user.setUserType(0); //0--admin, 1----normal user
			user.setUserStatus(0);
			
			Date registerDate = new Date();
			user.setUserRegisterDate(registerDate);
			
			visitorUserService.saveUser(user);
			
			//send email
			
			//save redis
			userRedisService.saveUserPassword(user);
			
			result = 0;
			resultDesc = RegisterInfo.REGISTER_SUCCESS;
			
			logTheRegisterTime(mailStrParam);
		} else {
			result = -1;
			resultDesc = RegisterInfo.REGISTER_EMAIL_EXISTS;
		}
		
		rj.setResult(result);
		rj.setResultDesc(resultDesc);
		
		setResultToClient(response, rj);
	}
	
	@RequestMapping("login/{emailStr}/{passMd5}")
    public void login(@PathVariable("emailStr") String mailStrParam, 
    		@PathVariable("passMd5") String passwordStrParam,
    		HttpServletResponse response) {
		
		ResultJson rj = checkIfTheUserLegal(mailStrParam, passwordStrParam);
		
		if (rj.getResult() >= 0) {
			String tokenStr = EncryptionUtil.getToken(mailStrParam, passwordStrParam, rj.getUserLoginTime());
			userRedisService.saveUserToken(mailStrParam, tokenStr);
			rj.setToken(tokenStr);
			rj.setUserEmail(mailStrParam);
		}
		
		setResultToClient(response, rj);
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
			user.setUserGender(getGenderInteger(ut.getGenderStr()));
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
			
			//mysql
			visitorUserService.saveUser(user);
			//redis
			userRedisService.saveUserPassword(user);
			//save mongo
			UserMongoBean userMongoBean = new UserMongoBean();
			userMongoBean.setUser_email(user.getUserEmail());
			userMongoBean.setUser_description(ut.getDescriptionStr());
			userMongoBean.setLast_login_forward_ip("192.168.1.1");
			userMongoService.saveUserDetail(userMongoBean);
			
			result = 0;
			resultDesc = RegisterInfo.UPDATE_SUCCESS;
			
			rj.setResult(result);
			rj.setResultDesc(resultDesc);
		}
		
		setResultToClient(response, rj);
	}
	
	@RequestMapping("usericon/create")
	public void userIconCreate(HttpServletRequest request, 
			HttpServletResponse response,
			@RequestParam("fileUserIcon") MultipartFile uploadFile, 
			@RequestParam("emailStr") String userEmailStr,
			@RequestParam("passwordStr") String userPasswordStr) {
		
		Integer result = 0;
		String resultDesc = "";
		ResultJson resultJ = new ResultJson();
		
		if (uploadFile != null && !uploadFile.isEmpty()) {
			if (StringUtils.isNotEmpty(userEmailStr) && StringUtils.isNotEmpty(userPasswordStr)) {
				ResultJson rj = checkIfTheUserLegal(userEmailStr, userPasswordStr);
				
				if (rj.getResult().intValue() >=0 ) {
					
					// do actual file upload
					User user = userRedisService.getUserPassword(userEmailStr);
					ObjectMetadata meta = new ObjectMetadata();
					meta.setContentLength(uploadFile.getSize());
					meta.setContentType(uploadFile.getContentType());
					try {
						s3Service.createNewFile("/user/icon"+user.getUserId()+"/"+uploadFile.getOriginalFilename(), uploadFile.getInputStream(), systemPreference.getAwsImgDomain(), meta);
						
						result = 0;
						resultDesc = RegisterInfo.USER_ICON_SET_SUCCESS;
						resultJ.setResult(result);
						resultJ.setResultDesc(resultDesc);
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					resultJ.setResult(rj.getResult());
					resultJ.setResultDesc(rj.getResultDesc());
				}
			} else {
				result = -5;
				resultDesc = RegisterInfo.USER_PARAM_ILLEGAL;
				
				resultJ.setResult(result);
				resultJ.setResultDesc(resultDesc);
			}
		} else {
			result = -4;
			resultDesc = RegisterInfo.USER_ICON_DATA_NULL;
			
			resultJ.setResult(result);
			resultJ.setResultDesc(resultDesc);
		}
		
		setResultToClient(response, resultJ);
	}
	
	private ResultJson checkIfTheUserLegal(String mailStrParam, String passwordStrParam) {
		Integer result = 0;
		String resultDesc = "";
		ResultJson resultJson = new ResultJson();
		
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
				
				logTheLogintime(mailStrParam);
			}
			
		} else {
			if (StringUtils.equals(user.getUserPassword(), passwordStrParam)) {
				result = 0;
				resultDesc = RegisterInfo.LOGIN_SUCCESS;
				
				Date loginDate = new Date();
				user.setUserLastLoginTime(loginDate);
				
				visitorUserService.saveUser(user);
				resultJson.setUserLoginTime(loginDate);
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
	
	private void setResultToClient(HttpServletResponse response, ResultJson resultJson) {
		sendJSONResponse(resultJson, response);
	}
	
	private void logTheLogintime(String emailStr) {
		if (log.isInfoEnabled()) {
			log.info("<user login>: >" + emailStr + "<");
		}
	}
	
	private void logTheRegisterTime(String emailStr) {
		if (log.isInfoEnabled()) {
			log.info("<user register>: >" + emailStr + "<");
		}
	}
	
	private Integer getGenderInteger(String genderStr) {
		Integer result = -1;
		if (StringUtils.equalsIgnoreCase(genderStr, "Male")) {
			result = 0;
		} else if(StringUtils.equalsIgnoreCase(genderStr, "Female")) {
			result = 1;
		} else if (StringUtils.equalsIgnoreCase(genderStr, "Other")) {
			result = 2;
		}
		return result;
	}
}
