package org.visitor.appportal.web.controller.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.visitor.appportal.service.newsite.S3Service;
import org.visitor.appportal.service.newsite.VisitorUserService;
import org.visitor.appportal.service.newsite.VisitorUserTokenInfoService;
import org.visitor.appportal.service.newsite.redis.FloopyThingRedisService;
import org.visitor.appportal.service.newsite.redis.TimezoneRedisService;
import org.visitor.appportal.service.newsite.redis.UserRedisService;
import org.visitor.appportal.visitor.beans.ResultJson;
import org.visitor.appportal.visitor.beans.UserTemp;
import org.visitor.appportal.visitor.domain.User;
import org.visitor.appportal.web.mailutils.SendMailUtils;
import org.visitor.appportal.web.mailutils.UserMailException;
import org.visitor.appportal.web.utils.EncryptionUtil;
import org.visitor.appportal.web.utils.MixAndMatchUtils;
import org.visitor.appportal.web.utils.PaypalInfo;
import org.visitor.appportal.web.utils.RegisterInfo;
import org.visitor.appportal.web.utils.WebInfo;

import com.amazonaws.services.s3.model.ObjectMetadata;

@Controller
@RequestMapping("updateUser")
public class UserUpdateController extends BasicController {
	protected static final Logger log = LoggerFactory.getLogger(UserUpdateController.class);
	
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

	
	@RequestMapping("/postUserDetail")
	public void postUserNewDetail(HttpServletRequest request, HttpServletResponse response) {
		try {
			UserTemp ut = super.getUserJson(request);
			logTheJsonResult(ut);
			
			User userTemp = (User) request.getAttribute(WebInfo.UserID);
			
			//update mysql
			Integer result = 0;
			String resultDesc = "";
			
			String userAddressStr = ut.getAddressStr();
			if (StringUtils.isNotEmpty(userAddressStr)) {
				userTemp.setUserAddress(userAddressStr);
			}
			
			String birthDateT = ut.getBirthDateStr();
			if (StringUtils.isNotEmpty(birthDateT)) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
				Date dateT = sdf.parse(birthDateT);
				userTemp.setUserBirthdate(dateT);
			}
			
			String firstNameStr = ut.getFirstNameStr();
			if (StringUtils.isNotEmpty(firstNameStr)) {
				userTemp.setUserFirstName(firstNameStr);
			}
			
			String lastNameStr = ut.getLastNameStr();
			if (StringUtils.isNotEmpty(lastNameStr)) {
				userTemp.setUserLastName(lastNameStr);
			}
			
			String userGenderStr = ut.getGenderStr();
			if (StringUtils.isNotEmpty(userGenderStr)) {
				userTemp.setUserGender(MixAndMatchUtils.getGenderInteger(userGenderStr));
			}
			
			String phoneNumberStr = ut.getPhoneNumberStr();
			if (StringUtils.isNotEmpty(phoneNumberStr)) {
				userTemp.setUserPhonenum(phoneNumberStr); //phone number not here
			}
			String workStr = ut.getWorkStr();
			if (StringUtils.isNotEmpty(workStr)) {
				userTemp.setUserWork(workStr);
			}
			String paypalNumStr = ut.getUserPalpalNumStr();
			if (StringUtils.isNotEmpty(paypalNumStr)) {
				userTemp.setUserPaypalnum(paypalNumStr);
			}
			
			String timeZoneStrT = ut.getTimeZoneStr();
			if (StringUtils.isNotEmpty(timeZoneStrT)) {
				Integer userTimeZoneInt = timezoneRedisService.getTimeZoneId(timeZoneStrT);
				userTemp.setUserTimeZone(userTimeZoneInt);
			}
			
			String userDetailStr = ut.getDescriptionStr();
			if (StringUtils.isNotEmpty(userDetailStr)) {
				userTemp.setUserDetail(userDetailStr);
			}
			
			//mysql
			visitorUserService.saveUser(userTemp);
			//redis
			userRedisService.saveUserPassword(userTemp);
				
			result = 0;
			resultDesc = RegisterInfo.UPDATE_SUCCESS;
			ResultJson rj = new ResultJson();
			rj.setResult(result);
			rj.setResultDesc(resultDesc);
			
			setResultToClient(response, rj);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/usericon")
	public void userIconCreate(HttpServletRequest request, 
			HttpServletResponse response,
			@RequestParam("fileUserIcon") MultipartFile uploadFile) {
		User userTemp = (User) request.getAttribute(WebInfo.UserID);
		
		Integer result = 0;
		String resultDesc = "";
		ResultJson resultJ = new ResultJson();
		
		if (uploadFile != null && !uploadFile.isEmpty()) {
			
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentLength(uploadFile.getSize());
			meta.setContentType(uploadFile.getContentType());
			try {
				String awsBucketName = MixAndMatchUtils.getSystemAwsPaypalConfig(MixAndMatchUtils.awsImgStatic);
				String imgDomain = MixAndMatchUtils.getSystemAwsPaypalConfig(MixAndMatchUtils.awsImgDomain);
				
				if (log.isInfoEnabled()) {
					if (userTemp == null) {
						log.info("user null");
					} else {
						log.info("user can be used");
					}
				}
				
				String fileOriUrl = "user/icon-"+userTemp.getUserId()+"/"+uploadFile.getOriginalFilename();
				
				s3Service.createNewFile(fileOriUrl, uploadFile.getInputStream(), awsBucketName, meta);
				
				String finalFileUrl = imgDomain + awsBucketName + "/" + fileOriUrl;
				userTemp.setUserPhotourl(finalFileUrl);
				
				visitorUserService.saveUser(userTemp);
				userRedisService.saveUserPassword(userTemp);
				
				result = 0;
				resultDesc = RegisterInfo.USER_ICON_SET_SUCCESS;
				resultJ.setResult(result);
				resultJ.setResultDesc(resultDesc);
				resultJ.setImageUrl(finalFileUrl);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		setResultToClient(response, resultJ);
	}
	
	@RequestMapping("/retrievepassword")
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
	
	private void setResultToClient(HttpServletResponse response, ResultJson resultJson) {
		sendJSONResponse(resultJson, response);
	}
}
