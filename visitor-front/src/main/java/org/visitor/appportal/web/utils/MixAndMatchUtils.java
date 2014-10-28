package org.visitor.appportal.web.utils;

import java.util.ResourceBundle;

public class MixAndMatchUtils {
	public static final ResourceBundle mailRB = ResourceBundle.getBundle("mailUtils");
	
	public static String getSystemConfig(String keyT)
	{
		return mailRB.getString(keyT);
	}
}
