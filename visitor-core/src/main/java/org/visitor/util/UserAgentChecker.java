/**
 * 
 */
package org.visitor.util;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;

/**
 * @author mengw
 *
 */
public class UserAgentChecker {
	private String browserUa;
	
	private static final String PLATFORM_ANDROID = "Android";
	private static final String PLATFORM_SERIES60 = "Series60";
	private static final String PLATFORM_SERIES_B60 = "Series 60";
	private static final String PLATFORM_JAVA = "MIDP";
	private static final String PLATFORM_BLACKBERY = "BlackBerry";

	private static final String[] platforms = {PLATFORM_SERIES60, PLATFORM_SERIES_B60, PLATFORM_ANDROID, PLATFORM_JAVA, PLATFORM_BLACKBERY};
	private static final String[] platformVersionSplit = {";", " ", "/"};
	private static final String[] platformVersions = {"Series60/", "Android ", "MIDP-", "Android/", "BlackBerry", "Android_"};
	
	public static final String S60 = "S60";
	public static final String ANDROID = "Android";
	public static final String JAVA = "J2ME";
	public static final String BLACK_BERY = "BlackBerry";
	
	public static final String S60_V2 = "V2";
	public static final String S60_V3 = "V3";
	public static final String S60_V5 = "V5";
	private static final String SYMBIAN = "Symbian^3";
	private static final String SYMBIAN_V1 = "V1";
	
	public UserAgentChecker(String browserUa) {
		this.browserUa = browserUa;
	}
	
	/**
	 * 通过传入的浏览器UA与OperaUA获取平台和平台版本
	 * 
	 * 1）查找X-OperaMini-Phone-UA是否包括Android，Series60，Series 60或MIDP，有则直接取其值，并转入2，继续处理
	 * 2）将名称调整为系统可识别的名称
	 * 3）返回平台名称，可能为NULL
	 * 
	 * @return
	 */
	public String getPlatformName() {
		int index = 0;
		String name = null;
		for(String platform : platforms) {
			index = StringUtils.indexOf(this.browserUa, platform);
			if(index != -1) {//We find the platform
				name = platform;
				break;
			}
		}
		
		if(StringUtils.isNotBlank(name)) {
			//Android
			if(StringUtils.equalsIgnoreCase(name, PLATFORM_ANDROID)) {
				name = ANDROID;
			} else if(StringUtils.equalsIgnoreCase(name, PLATFORM_SERIES60) 
					|| StringUtils.equalsIgnoreCase(name, PLATFORM_SERIES_B60) ){
				if(StringUtils.contains(browserUa, "Symbian/3")) {
					name = SYMBIAN;
				} else {
					name = S60;
				}
			} else if(StringUtils.equalsIgnoreCase(name, PLATFORM_JAVA)) {
				name = JAVA;
			} else if(StringUtils.equalsIgnoreCase(name, PLATFORM_BLACKBERY)) {
				name = JAVA;
			}
		}
		return name;
	}
	
	/**
	 * 获取手机平台版本
	 * @return
	 */
	public String getPlatformVersion() {
		String platformVersion = null;
		for(String platform : platformVersions) {
			//通过字符截取的方式从Header信息中截取版本信息，这一步去除版本信息左边的字符串
			platformVersion = StringUtils.substringAfter(this.browserUa, platform);
			if(StringUtils.isNotBlank(platformVersion)) {
				break;
			}
		}
		
		//对截取出来的版本字符串做进一步处理，去除版本信息右边的字符串
		if(StringUtils.isNotBlank(platformVersion)) {
			//找到版本信息结束的位置，可能是platformVersionSplit中的任意一种结束符
			int index = StringUtils.indexOfAny(platformVersion, platformVersionSplit);
			if(index > 0) {
				//去除右边的多余字符串
				platformVersion = StringUtils.substring(platformVersion, 0, index);
			}
		}
		
		String platform = this.getPlatformName();
		
		//根据平台信息返回对应的版本信息，需要注意的是这儿没有JAVA对应的版本信息
		if(StringUtils.equalsIgnoreCase(platform, S60)) {
			//
			if(StringUtils.startsWith(platformVersion, "1")) {
				return S60_V2;
			} else if(StringUtils.startsWith(platformVersion, "2")) {
				return S60_V2;
			} else if(StringUtils.startsWith(platformVersion, "3")) {
				return S60_V3;
			} else if(StringUtils.startsWith(platformVersion, "5")) {
				return S60_V5;
			}
		} else if(StringUtils.equalsIgnoreCase(platform, SYMBIAN)) {
			return SYMBIAN_V1;
		} else if(StringUtils.equalsIgnoreCase(platform, ANDROID)) {
			platformVersion = StringUtils.substring(platformVersion, 0, 3);
		}
		return platformVersion;
	}
	
	public static void main(String[] args) {
//		String ua = "Opera/9.80 (BlackBerry; Opera Mini/4.4.34190/26.1507; U; zh) Presto/2.8.119 Version/10.54";
		String ua = "Mozilla/5.0 (Linux; U; Android_2.1-update; zh-cn; TCL A906 Build/FSR) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";
		//ua = "Mozilla/5.0 (Linux; U; Android 2.1-update1; zh-cn; HTC Magic Build/EPF30) AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0 Mobile Safari/530.17";
		//ua = "Mozilla/5.0 (Linux; U; Android 2.2.1; zh-tw; Desire HD Build/FRG83D) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";
		//ua = "K-Touch_W606_CUCC/1.0 Android/2.1 Browser/Google 2.1 (compatible; Mozilla/5.0)ver";
		//ua = "Opera/9.80 (Series 60; Opera Mini/6.1.11250/25.494; U; en) Presto/2.5.25 Version/10.54";
		UserAgentChecker checker = new UserAgentChecker(ua);
		System.err.println(StringUtils.substringAfter(ua, "Android"));
		System.err.println(checker.getPlatformName() + "  " + checker.getPlatformVersion());
		
		String[] abc = new String[]{"1.6", "1.5", "2.3", "2.1", "4.0", "4.2", "4.1"};
		String version = "2.1";
		Arrays.sort(abc);
		for(String s : abc) {
			System.err.println(s);
			if(s.compareTo(version) > 0) {
				System.err.println("---->" + s);
			}
		}
	}
}
