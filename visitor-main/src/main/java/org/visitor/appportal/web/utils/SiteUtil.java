package org.visitor.appportal.web.utils;

import javax.servlet.http.HttpSession;

public class SiteUtil {

	private final static String SITE_KEY = "siteId";
	
	public static Integer getSiteFromSession(HttpSession session){
		
		if(session != null){
			
			return (Integer)session.getAttribute(SITE_KEY);
		}
		
		return null;
	}
	
	public static void setSiteToSession(HttpSession session,Integer site) {
		if(session != null) {
			session.setAttribute(SITE_KEY, site);
		}
	}
	
	public enum IDEnum {
		ANDROID(13),SYMBIAN(14),GAME(15), IOS(16);
		
		private Integer value;
		private IDEnum(Integer value){
			this.value = value;
		}
		
		public Integer getValue(){
			return value;
		}
		
		public static IDEnum getInstance(Integer id){
			switch(id){
				case 13:
					return ANDROID;
				case 14:
					return SYMBIAN;
				case 15:
					return GAME;
				case 16:
					return IOS;					
				default :
					return ANDROID;
			}
		}
	}
}
