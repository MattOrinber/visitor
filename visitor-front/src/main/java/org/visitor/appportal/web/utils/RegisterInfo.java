package org.visitor.appportal.web.utils;

public class RegisterInfo {
	public static final String REGISTER_SUCCESS = "register success";
	public static final String REGISTER_EMAIL_EXISTS = "register email has been used";
	
	public static final String LOGIN_SUCCESS = "login success";
	
	public static final String UPDATE_SUCCESS = "update success";
	
	public static final String LOGIN_FAILED_USER_NOTEXISTED = "login failed--user not exist";
	public static final String LOGIN_FAILED_PASSWORD_NOT_RIGHT = "login failed--password wrong";
	
	
	public static final String USER_ICON_DATA_NULL = "user icon data is empty!";
	
	public static final String USER_PARAM_ILLEGAL = "please input right user and password";
	
	public static final String USER_ICON_SET_SUCCESS = "user icon been successfully set";
	
	public static final String USER_INTERNALMAIL_SAVE_SUCCESS = "user internal mail save success";
	public static final String USER_INTERNALMAIL_SAVE_FAIL = "user internal mail save fail";
	public static final String USER_NOT_LOGGED_IN = "user not logged in";
	
	public static final String USER_EMAIL_PREFIX_FROM = "0";
	public static final String USER_EMAIL_PREFIX_TO = "1";
	public static final String USER_EMAIL_SPLIT = "---";
	public static final String USER_EMAIL_CONTENT_SPLIT = "--%#%--";
	
	//用户的类型，枚举
	public enum UserTypeEnum {
		NormalUser(0),FacebookUser(1);
		private Integer value;
		private String displayName;

		private UserTypeEnum(Integer value) {
			this.value = value;
			switch (value) {
			case 0:
				displayName = "normal user";
				break;
			case 1:
				displayName = "facebook user";
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

		public static UserTypeEnum getInstance(Integer value) {
			if (null != value) {
				UserTypeEnum[] enums = UserTypeEnum.values();
				for (UserTypeEnum status : enums) {
					if (status.getValue().intValue() == value.intValue()) {
						return status;
					}
				}
			}
			return null;
		}
	}
	
	public enum UserMailStatusEnum {
		Unread(0),Read(1);
		private Integer value;
		private String displayName;

		private UserMailStatusEnum(Integer value) {
			this.value = value;
			switch (value) {
			case 0:
				displayName = "Unread";
				break;
			case 1:
				displayName = "Read";
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

		public static UserMailStatusEnum getInstance(Integer value) {
			if (null != value) {
				UserMailStatusEnum[] enums = UserMailStatusEnum.values();
				for (UserMailStatusEnum status : enums) {
					if (status.getValue().intValue() == value.intValue()) {
						return status;
					}
				}
			}
			return null;
		}
	}
}
