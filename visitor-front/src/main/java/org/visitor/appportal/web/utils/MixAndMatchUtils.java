package org.visitor.appportal.web.utils;

import java.util.ResourceBundle;

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
}
