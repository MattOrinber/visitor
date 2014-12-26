package org.visitor.appportal.web.utils;


public class OrderInfo {
	
	public static final String PRODUCT_AMOUNT_CALC_DONE = "total amount calc done";
	public static final String PRODUCT_BUY_TEMP_NOT_RIGHT = "params submitted not right";
	
	
	public enum ProductOrderStatusEnum {
		Init(0), WaitPay(1), EXPIRED(2), PARTIAL_PAID(3), PAID(4), NEEDREFUND(5), REFUNDED(6);
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
				displayName = "PARTIAL_PAID";
				break;
			case 4:
				displayName = "PAID";
				break;
			case 5:
				displayName = "NEEDREFUND";
				break;
			case 6:
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
		Init(0), Completed(1), Validated(2), Invalid(3);
		private Integer value;
		private String displayName;
		
		private ProductPayOrderStatusEnum(Integer value) {
			this.value = value;
			switch (value) {
			case 0:
				displayName = "Init";
				break;
			case 1:
				displayName = "Completed";
				break;
			case 2:
				displayName = "Validated";
				break;
			case 3:
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
