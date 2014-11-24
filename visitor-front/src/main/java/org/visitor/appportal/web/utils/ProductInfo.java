package org.visitor.appportal.web.utils;

public class ProductInfo {
	
	public static final String AMENITIES_INDEX_SPLIT = ":";
	public static final String AMENITIES_ITEM_SPLIT = ";";
	
	public static final Integer AVAILABLE_TYPE_ALWAYS = 0;
	public static final Integer AVAILABLE_TYPE_SOMETIMES = 1;
	public static final Integer AVAILABLE_TYPE_ONETIME = 2;
	
	public static final Integer EDIT_STATUS = 0;
	public static final Integer ONLINE_STATUS = 1;
	public static final Integer OFFLINE_STATUS = 2;
	
	public static final Integer AMENITIES_TYPE_MOSTCOMMON = 0;
	public static final Integer AMENITIES_TYPE_EXTRAS = 1;
	public static final Integer AMENITIES_TYPE_SPECIALFEATURE = 2;
	public static final Integer AMENITIES_TYPE_HOMESAFETY = 3;
	
	public static final String PRODUCT_CREATE_SUCCESS = "product ceated successfully";
	
	public static final String PRODUCT_DETAIL_UPDATE_SUCCESS = "product detail update success";//0
	public static final String PRODUCT_ADDRESS_SAVE_SUCCESS = "product address save success";// 0
	public static final String PRODUCT_EXTRA_PRICE_SET_SAVE_SUCCESS = "product extra price set save success";//0
	public static final String PRODUCT_NOTFOUND_FORUPDATE = "product not found in redis for update";//-1
}
