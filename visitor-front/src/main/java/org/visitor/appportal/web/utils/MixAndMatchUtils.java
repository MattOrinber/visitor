package org.visitor.appportal.web.utils;

import java.util.ResourceBundle;

public class MixAndMatchUtils {
	public static final ResourceBundle mailRB = ResourceBundle.getBundle("mailUtils");
	public static final ResourceBundle mongoRB = ResourceBundle.getBundle("mongoUtils");
	
	public static String getSystemConfig(String keyT)
	{
		return mailRB.getString(keyT);
	}
	
	public static String getMongoConfig(String keyT) {
		return mongoRB.getString(keyT);
	}
}
