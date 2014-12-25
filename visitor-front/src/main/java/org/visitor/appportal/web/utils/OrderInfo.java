package org.visitor.appportal.web.utils;


public class OrderInfo {
	
	public static final String PRODUCT_AMOUNT_CALC_DONE = "total amount calc done";
	public static final String PRODUCT_BUY_TEMP_NOT_RIGHT = "params submitted not right";
	
	
	public enum ProductOrderStatusEnum {
		Init(0), WaitPay(1), EXPIRED(2), PAID(3), NEEDREFUND(4), REFUNDED(5);
		private Integer value;
		private String displayName;
		
		private ProductOrderStatusEnum(Integer value) {
			this.value = value;
			switch (value) {
			case 0:
				displayName = "Init";
				break;
			case 1:
				displayName = "WaitPay";
				break;
			case 2:
				displayName = "EXPIRED";
				break;
			case 3:
				displayName = "PAID";
				break;
			case 4:
				displayName = "NEEDREFUND";
				break;
			case 5:
				displayName = "REFUNDED";
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
		
		public static ProductOrderStatusEnum getInstance(Integer value) {
			if (null != value) {
				ProductOrderStatusEnum[] enums = ProductOrderStatusEnum.values();
				for (ProductOrderStatusEnum status : enums) {
					if (status.getValue().intValue() == value.intValue()) {
						return status;
					}
				}
			}
			return null;
		}
	}
	
	public enum ProductPayOrderStatusEnum {
		Init(0), WaitComplete(1), WaitValidate(2), Validated(3), Invalid(4);
		private Integer value;
		private String displayName;
		
		private ProductPayOrderStatusEnum(Integer value) {
			this.value = value;
			switch (value) {
			case 0:
				displayName = "Init";
				break;
			case 1:
				displayName = "WaitComplete";
				break;
			case 2:
				displayName = "WaitValidate";
				break;
			case 3:
				displayName = "Validated";
				break;
			case 4:
				displayName = "Invalid";
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
		
		public static ProductPayOrderStatusEnum getInstance(Integer value) {
			if (null != value) {
				ProductPayOrderStatusEnum[] enums = ProductPayOrderStatusEnum.values();
				for (ProductPayOrderStatusEnum status : enums) {
					if (status.getValue().intValue() == value.intValue()) {
						return status;
					}
				}
			}
			return null;
		}
	}
}
