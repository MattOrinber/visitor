package org.visitor.appportal.web.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.ui.Model;
import org.visitor.appportal.visitor.domain.Product;
import org.visitor.appportal.visitor.domain.ProductOperation;
import org.visitor.appportal.visitor.domain.User;
import org.visitor.appportal.web.utils.RegisterInfo.UserTypeEnum;

public class MixAndMatchUtils {
	public static final ResourceBundle mailRB = ResourceBundle.getBundle("mailUtils");
	public static final ResourceBundle mongoRB = ResourceBundle.getBundle("mongoUtils");
	
	public static final ResourceBundle awsRb = ResourceBundle.getBundle("visitor");
	
	/*
	 * 
	 * */
	public static final String accessKey = "thisCan.acsKey";
	public static final String secretKey = "thisCan.secKey";
	
	//------------------------------------------aws static image url
	public static final String awsImgStatic = "aws.imgBucketUseing";
	public static final String awsImgDomain = "aws.imgDomain";
	
	//------------------------------------------facebook related items
	public static final String facebookCallbackURL = "facebookCallbackURL";
	public static final String facebookAppId = "facebookAppId";
	public static final String facebookAppSecret = "facebookAppSecret";
	public static final String facebookLoginURL = "facebookLoginURL";
	public static final String facebookAccessTokenURL = "facebookAccessTokenURL";
	public static final String facebookGetUserInfoURL = "facebookGetUserInfoURL";
	public static final String facebookGetUserPictureURL = "facebookGetUserPictureURL";
	
	public static final String param_facebook_client_id = "client_id";
	public static final String param_facebook_client_secret = "client_secret";
	public static final String param_facebook_redirect_uri = "redirect_uri";
	public static final String param_facebook_display = "display";
	public static final String param_facebook_auth_code = "code";
	public static final String param_facebook_access_token = "access_token";
	public static final String param_facebook_expires = "expires";
	public static final String param_facebook_redirect = "redirect";
	
	public static final Integer param_user_token_expire = 3600;
	
	public static final String COOKIE_NAME_USER_ACCESS_TOKEN = "userAccessToken";
	public static final String COOKIE_NAME_USER_EMAIL = "userEmail";
	public static final String COOKIE_NAME_GLOBAL_CURRENCY = "globalCurrency";
	//------------------------------------------facebook related items end
	
	public static final String paypalClientID = "paypalClientID";
	public static final String paypalClientSecret = "paypalClientSecret";
	
	public static String getSystemConfig(String keyT)
	{
		return mailRB.getString(keyT);
	}
	
	public static String getMongoConfig(String keyT) {
		return mongoRB.getString(keyT);
	}
	
	public static String getSystemAwsPaypalConfig(String keyT)
	{
		return awsRb.getString(keyT);
	}
	
	public static Integer getGenderInteger(String genderStr) {
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
	
	public static void setUserCookie(HttpServletResponse response, String userEmailStr, String userAccessTokenStr, Integer expireSeconds) {
		Cookie cookie_token = new Cookie(COOKIE_NAME_USER_ACCESS_TOKEN, userAccessTokenStr);
		cookie_token.setMaxAge(expireSeconds);
		response.addCookie(cookie_token);
		
		Cookie cookie_email = new Cookie(COOKIE_NAME_USER_EMAIL, userEmailStr);
		cookie_email.setMaxAge(expireSeconds);
		response.addCookie(cookie_email);
	}
	
	public static void setUserModel(Model model, User user) {
		String loginFirstName = user.getUserFirstName();
		String loginLastName = user.getUserLastName();
		String loginEmail = user.getUserEmail();
		String loginUrl = user.getUserPhotourl();
		model.addAttribute("loginUserEmail", loginEmail);
		if (StringUtils.isNotEmpty(loginFirstName) && StringUtils.isNotEmpty(loginLastName)) {
			model.addAttribute("loginName", user.getUserFirstName() + " " + user.getUserLastName());
		} else {
			if (StringUtils.isNotEmpty(loginEmail)) {
				model.addAttribute("loginName", loginEmail);
			} else {
				model.addAttribute("loginName", "--");
			}
		}
		
		if (user.getUserType() == UserTypeEnum.FacebookUser.getValue()) {
			model.addAttribute("userIconUrl", loginUrl);
		} else {
			if (StringUtils.isNotEmpty(loginUrl)) {
				model.addAttribute("userIconUrl", loginUrl);
			}
			else {
				model.addAttribute("userIconUrl", "--");
			}
		}
	}
	
	
	//计算订单总金额的接口
	public static Double calculatePrice(int dayCount,
			DateTime startDate,
			DateTime endDate,
			List<ProductOperation> poList,
			Product product) {
		List<Double> resList = new ArrayList<Double>();
		List<DateTime> listDate = new ArrayList<DateTime>();
		
		for (int i = 0; i < dayCount; i ++) {
			listDate.add(startDate.plusDays(i));
		}
		
		for (DateTime dt : listDate) {
			boolean ifFound = false;
			for (ProductOperation po : poList) {
				DateTime poS = new DateTime(po.getPoStartDate());
				DateTime poE = new DateTime(po.getPoEndDate());
				
				if ((dt.isAfter(poS) && dt.isBefore(poE)) ||
						dt.isEqual(poS) || dt.isEqual(poE)) {
					ifFound = true;
					double priceSpecial = (double)po.getPoPricePerNight();
					resList.add(new Double(priceSpecial));
					break;
				}
			}
			
			if (!ifFound) {
				Integer basePrice = Integer.valueOf(product.getProductBaseprice());
				double basePriceD = (double)(basePrice.intValue());
				resList.add(new Double(basePriceD));
			}
		}
		
		Double result = new Double(0.0);
		
		for (Double item : resList) {
			result += item;
		}
		
		return result;
	}
}
