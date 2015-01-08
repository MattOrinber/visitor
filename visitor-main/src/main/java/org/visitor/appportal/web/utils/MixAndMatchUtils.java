package org.visitor.appportal.web.utils;

import java.util.ResourceBundle;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

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
	
	public static final String paypalClientID = "paypalClientID";
	public static final String paypalClientSecret = "paypalClientSecret";
	
	public static final String COOKIE_NAME_USER_ACCESS_TOKEN = "userToken";
	public static final String COOKIE_NAME_USER_EMAIL = "userEmail";
	
	public static final Integer param_user_token_expire = 3600;
	
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
	
	public static void setUserCookie(HttpServletResponse response, String userEmailStr, String userAccessTokenStr, Integer expireSeconds) {
		Cookie cookie_token = new Cookie(COOKIE_NAME_USER_ACCESS_TOKEN, userAccessTokenStr);
		cookie_token.setPath("/");
		cookie_token.setMaxAge(expireSeconds);
		response.addCookie(cookie_token);
		
		Cookie cookie_email = new Cookie(COOKIE_NAME_USER_EMAIL, userEmailStr);
		cookie_email.setPath("/");
		cookie_email.setMaxAge(expireSeconds);
		response.addCookie(cookie_email);
	}
}
