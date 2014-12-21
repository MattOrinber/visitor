package org.visitor.appportal.web.controller.common;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.visitor.appportal.service.newsite.S3Service;
import org.visitor.appportal.service.newsite.VisitorUserService;
import org.visitor.appportal.service.newsite.VisitorUserTokenInfoService;
import org.visitor.appportal.service.newsite.redis.TimezoneRedisService;
import org.visitor.appportal.service.newsite.redis.UserRedisService;
import org.visitor.appportal.visitor.beans.ResultJson;
import org.visitor.appportal.visitor.beans.UserTemp;
import org.visitor.appportal.visitor.domain.User;
import org.visitor.appportal.web.utils.MixAndMatchUtils;
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

	
	@RequestMapping("/postUserDetail")
	public void postUserNewDetail(HttpServletRequest request, HttpServletResponse response) {
		try {
			UserTemp ut = super.getUserJson(request);
			logTheJsonResult(ut);
			
			User userTemp = (User) request.getAttribute(WebInfo.UserID);
			
			//update mysql
			Integer result = 0;
			String resultDesc = "";
			
			userTemp.setUserAddress(ut.getAddressStr());
			String birthDateT = ut.getBirthDateStr();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
			Date dateT = sdf.parse(birthDateT);
			userTemp.setUserBirthdate(dateT);
				
			userTemp.setUserFirstName(ut.getFirstNameStr());
			userTemp.setUserLastName(ut.getLastNameStr());
			userTemp.setUserGender(MixAndMatchUtils.getGenderInteger(ut.getGenderStr()));
			userTemp.setUserPhonenum(ut.getPhoneNumberStr()); //phone number not here
			userTemp.setUserWork(ut.getWorkStr());
				
			Integer userTimeZoneInt = timezoneRedisService.getTimeZoneId(ut.getTimeZoneStr());
			userTemp.setUserTimeZone(userTimeZoneInt);
			userTemp.setUserDetail(ut.getDescriptionStr());
			
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
	
	private void setResultToClient(HttpServletResponse response, ResultJson resultJson) {
		sendJSONResponse(resultJson, response);
	}
}
