package org.visitor.util;


public enum SiteEnum {

	ANDROID(13), SYMBIAN(14), GAME(15), IOS(16);

	private Integer value;

	private SiteEnum(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	public static SiteEnum getInstance(Integer id) {

		switch (id) {
			case 13:
				return ANDROID;
			case 14:
				return SYMBIAN;
			case 15:
				return GAME;
			case 16:
				return IOS;				
			default:
				return ANDROID;
		}
		
	}
}
