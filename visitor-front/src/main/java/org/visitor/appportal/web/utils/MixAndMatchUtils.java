package org.visitor.appportal.web.utils;

import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;

public class MixAndMatchUtils {
	public static final ResourceBundle mailRB = ResourceBundle.getBundle("mailUtils");
	public static final ResourceBundle mongoRB = ResourceBundle.getBundle("mongoUtils");
	
	public static final ResourceBundle awsRb = ResourceBundle.getBundle("visitor");
	
	/*
	 * 
	 * */
	public static final String accessKey = "thisCan.acsKey";
	public static final String secretKey = "thisCan.secKey";
	public static final String imgBucketName = "aws.imgBucketName";
	public static final String fileBucketName = "aws.fileBucketName";
	public static final String merchantBucketName = "aws.merchantBucketName";
	public static final String awsFileDomain = "aws.fileDomain";
	public static final String awsImgDomain = "aws.imgDomain";
	
	//------------------------------------------aws static image url
	public static final String awsImgStatic = "aws.imgBucketUseing";
	
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
}
