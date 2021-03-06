package org.visitor.appportal.web.utils;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.DeserializationConfig.Feature;

public class WebInfo {
	public static final String SPLIT = "---";
	
	public static final String UserEmailStr = "userLoginEmail";
	public static final String UserPasswordStr = "userLoginToken";
	
	public static final String UserID = "userLoginId";
	
	public static final String ABOUT_INFO = "page_about";
	public static final String FOOTER_POLICY = "page_footer_policy";
	public static final String FOOTER_TERMS = "page_footer_terms";
	
	public static final Long pageSize = 10L;
	
	private static ObjectMapper objectMapper;
	
	public static ObjectMapper getObjectMapperInstance() {
		if(null == objectMapper) {
			objectMapper = new ObjectMapper();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			objectMapper.setDateFormat(df);
			objectMapper.getDeserializationConfig().disable(Feature.FAIL_ON_UNKNOWN_PROPERTIES);
		}
		return objectMapper;
	}
	
	public static void sendJSONResponse(Object obj, HttpServletResponse response) throws Exception {
			String resultJsonStr = getObjectMapperInstance().writeValueAsString(obj);
			PrintWriter out = response.getWriter();
			out.print(resultJsonStr);
			out.flush();
	}
	
	public enum ManagementPageTypeEnum {
		UserMan(0), PageConstsMan(1), CityMan(2), CityRecommendMan(3), ActivitiesMan(4), OrderMan(5), ArticleMan(6), 
		UserDetail(7), ActivityDetail(8), OrderDetail(9), ArticleDetail(10);
		private Integer value;
		private String displayName;
		
		private ManagementPageTypeEnum(Integer value) {
			this.value = value;
			switch (value) {
			case 0:
				displayName = "User management page";
				break;
			case 1:
				displayName = "page constants management page";
				break;
			case 2:
				displayName = "city management page";
				break;
			case 3:
				displayName = "city recommend management page";
				break;
			case 4:
				displayName = "acticities management page";
				break;
			case 5:
				displayName = "orders management page";
				break;
			case 6:
				displayName = "article management page";
				break;
			case 7:
				displayName = "user detail page";
				break;
			case 8:
				displayName = "acticity detail page";
				break;
			case 9:
				displayName = "order detail page";
				break;
			case 10:
				displayName = "article detail page";
				break;
			}
		}
		
		/**
		 * @return the value
		 */
		public Integer getValue() {
			return value;
		}

		/**
		 * @return the displayName
		 */
		public String getDisplayName() {
			return displayName;
		}
		
		public static ManagementPageTypeEnum getInstance(Integer value) {
			if (null != value) {
				ManagementPageTypeEnum[] enums = ManagementPageTypeEnum.values();
				for (ManagementPageTypeEnum status : enums) {
					if (status.getValue().intValue() == value.intValue()) {
						return status;
					}
				}
			}
			return null;
		}
	}
}
