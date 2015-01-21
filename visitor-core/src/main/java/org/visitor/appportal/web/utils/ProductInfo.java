package org.visitor.appportal.web.utils;

public class ProductInfo {
	
	public static final String AMENITIES_INDEX_SPLIT = ":";
	public static final String AMENITIES_ITEM_SPLIT = ";";
	
	public static final Integer AVAILABLE_TYPE_ALWAYS = 0;
	public static final Integer AVAILABLE_TYPE_SOMETIMES = 1;
	public static final Integer AVAILABLE_TYPE_ONETIME = 2;
	
	public static final Integer AVAILABLE_TYPE_DEFAULT = 5;
	
	public static final Integer EDIT_STATUS = 0;
	public static final Integer ONLINE_STATUS = 1;
	public static final Integer OFFLINE_STATUS = 2;
	
	public static final Integer AMENITIES_TYPE_MOSTCOMMON = 0;
	public static final Integer AMENITIES_TYPE_EXTRAS = 1;
	public static final Integer AMENITIES_TYPE_SPECIALFEATURE = 2;
	public static final Integer AMENITIES_TYPE_HOMESAFETY = 3;
	
	public static final String PRODUCT_PAGE_SIZE = "product_page_size";
	public static final String ORDER_PAGE_SIZE = "order_page_size";
	
	public static final String PRODUCT_CREATE_SUCCESS = "product ceated successfully";
	
	public static final String PRODUCT_DETAIL_UPDATE_SUCCESS = "product detail update success";//0
	public static final String PRODUCT_CANCELLATION_POLICY_UPDATE_SUCCESS = "product cancellation policy update success";//0
	public static final String PRODUCT_PUBLISH_SUCCESS = "product cancellation policy update success";//0
	public static final String PRODUCT_ADDRESS_SAVE_SUCCESS = "product address save success";// 0
	public static final String PRODUCT_EXTRA_PRICE_SET_SAVE_SUCCESS = "product extra price set save success";//0
	public static final String PRODUCT_OPERATION_SAVE_SUCCESS = "product operation set success";
	public static final String PRODUCT_PICTURE_SAVE_SUCCESS = "product picture save success";
	public static final String PRODUCT_NOTFOUND_FORUPDATE = "product not found in redis for update";//-1
	public static final String PRODUCT_PICTURE_NOTFOUND_FORUPDATE = "product picture not found in redis for update";//-1
	public static final String PRODUCT_NOTFOUND_FOR_OPERATION_UPDATE = "product not found in redis for update";//-1
	
	public enum ProductAvailableTypeEnum {
		Always(0), Sometimes(1), OneTime(2);
		private Integer value;
		private String displayName;
		
		private ProductAvailableTypeEnum(Integer value) {
			this.value = value;
			switch (value) {
			case 0:
				displayName = "Always";
				break;
			case 1:
				displayName = "Sometimes";
				break;
			case 2:
				displayName = "One Time";
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
		
		public static ProductAvailableTypeEnum getInstance(Integer value) {
			if (null != value) {
				ProductAvailableTypeEnum[] enums = ProductAvailableTypeEnum.values();
				for (ProductAvailableTypeEnum status : enums) {
					if (status.getValue().intValue() == value.intValue()) {
						return status;
					}
				}
			}
			return null;
		}
	}
	
	public enum ProductOperationTypeEnum {
		Publish_avail(0), Publish_unavail(1);
		private Integer value;
		private String displayName;
		
		private ProductOperationTypeEnum(Integer value) {
			this.value = value;
			switch (value) {
			case 0:
				displayName = "Publish available";
				break;
			case 1:
				displayName = "Publish unavailable";
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
		
		public static ProductOperationTypeEnum getInstance(Integer value) {
			if (null != value) {
				ProductOperationTypeEnum[] enums = ProductOperationTypeEnum.values();
				for (ProductOperationTypeEnum status : enums) {
					if (status.getValue().intValue() == value.intValue()) {
						return status;
					}
				}
			}
			return null;
		}
	}
	
	public enum StatusTypeEnum {
		Active(0), Inactive(1);
		private Integer value;
		private String displayName;
		
		private StatusTypeEnum(Integer value) {
			this.value = value;
			switch (value) {
			case 0:
				displayName = "Active";
				break;
			case 1:
				displayName = "Inactive";
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
		
		public static StatusTypeEnum getInstance(Integer value) {
			if (null != value) {
				StatusTypeEnum[] enums = StatusTypeEnum.values();
				for (StatusTypeEnum status : enums) {
					if (status.getValue().intValue() == value.intValue()) {
						return status;
					}
				}
			}
			return null;
		}
	}
	
	public enum ContainerTypeEnum {
		Product(0), City(1);
		private Integer value;
		private String displayName;
		
		private ContainerTypeEnum(Integer value) {
			this.value = value;
			switch (value) {
			case 0:
				displayName = "Product";
				break;
			case 1:
				displayName = "City";
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
		
		public static ContainerTypeEnum getInstance(Integer value) {
			if (null != value) {
				ContainerTypeEnum[] enums = ContainerTypeEnum.values();
				for (ContainerTypeEnum status : enums) {
					if (status.getValue().intValue() == value.intValue()) {
						return status;
					}
				}
			}
			return null;
		}
	}
}
