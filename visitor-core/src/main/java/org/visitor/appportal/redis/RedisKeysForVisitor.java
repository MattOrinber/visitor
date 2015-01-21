package org.visitor.appportal.redis;

public class RedisKeysForVisitor {
	public RedisKeysForVisitor() {
	}
	
	public static String getVisitorRedisSplit() {
    	return "--#%%#--";
    }
	
	public static String getVisitorRedisWeakSplit() {
		return "---";
	}
    
    // redis info for login use
    // the key should be email
    // the value should be userid+split+passwordmd5
    public static String getVisitorSiteUserPasswordFirstKey() {
    	return "visitor-site-userpasswordkey";
    }
    
    public static String getAllUserInfoKey() {
    	return "visitor-site-userinfokey";
    }
    
    public static String getVisitorSiteUserTokenInfoFirstKey() {
    	return "visitor-site-user-token-info";
    }
    
    // redis info for login use
    // the key should be userId
    // the value should be user output whole String
    public static String getVisitorSiteUserInfoFirstKey() {
    	return "visitor-site-userinfokey";
    }
    
    // redis info for user load owned product list use
    // the key should be email
    // the value should be productId1+split+productId2+...
    public static String getVisitorSiteUserOwnProductFirstKey() {
    	return "visitor-site-userownproductkey";
    }
    
    // redis info for user load purchased product list use
    // the key should be email
    // the value should be productId1+split+productId2+...
    public static String getVisitorSiteUserPayedProductFirstKey() {
    	return "visitor-site-userpayedproductkey";
    }
    
    // redis info for user load interested in product list use
    // the key should be email
    // the value should be productId1+split+productId2+...
    public static String getVisitorSiteUserInterestedProductFirstKey() {
    	return "visitor-site-userinterproductkey";
    }
    
    // redis info for login use
    // the key should be product id
    // the value should be product output whole String
    public static String getVisitorProductInfoKey() {
    	return "visitor-site-productinfokey";
    }
    
    public static String getVisitorProductCityKey() {
    	return "visitor-site-citykey";
    }
    public static String getVisitorProductCityIDKey() {
    	return "visitor-site-cityId";
    }
    
    public static String getVisitorProductCityNewOldKey() {
    	return "visitor-site-city-newold";
    }
    
    public static String getVisitorProductCityPriceKey() {
    	return "visitor-site-city-price";
    }
    
    public static String getVisitorUserProductInfoKey(String userId) {
    	return "visitor-site-user-productinfokey" + getVisitorRedisWeakSplit() + userId;
    }
    
    public static String getVisitorProductAddressInfoKey() {
    	return "visitor-site-product-address";
    }
    
    public static String getVisitorProductMultipriceInfoKey(Long pid) {
    	return "visitor-site-product-multiprice-" + String.valueOf(pid.longValue());
    }
    
    public static String getVisitorProductDetailInfoKey() {
    	return "visitor-site-product-detail-info";
    }
    
    public static String getVisitorTimeZoneKey() {
    	return "visitor-site-timezone";
    }
    
    public static String getVisitorLanguageKey() {
    	return "visitor-site-language";
    }
    
    public static String getVisitorUserTokenKey() {
    	return "visitor-site-user-token-";
    }
    
    public static String getVisitorProductOperationKey() {
    	return "visitor-site-product-operation";
    }
    
    public static String getVisitorProductPictureKey() {
    	return "visitor-site-product-picture";
    }
    public static String getVisitorProductPictureURLKey() {
    	return "visitor-site-product-picture-url";
    }
    
    public static String getUserInternalMailToMeKey() {
    	return "visitor-site-user-internal-mail-unread" + getVisitorRedisWeakSplit();
    }
    
    public static String getUserInternalMailFromMeKey() {
    	return "visitor-site-user-internal-mail-history" + getVisitorRedisWeakSplit();
    }
    
    public static String getUserInternalMailRepliedFromMeKey() {
    	return "visitor-site-user-internal-mail-replied" + getVisitorRedisWeakSplit();
    }
    
    public static String getUserInternalMailAlways() {
    	return "visitor-site-user-internal-mail-always";
    }
    
    //order related
    public static String getUserOrderKey() {
    	return "visitor-site-user-orders";
    }
    
    public static String getProductOrderKey() {
    	return "visitor-site-product-orders";
    }
    
    public static String getProductPayOrderKey() {
    	return "ppok-";
    }
    
    public static String getOrderToUserKey() {
    	return "otu";
    }
    
    public static String getPaypalTxnIdKey() {
    	return "paypal-txn-id-group";
    }
    
    public static String getPaypalUserToken() {
    	return "pout-";
    }
    
    public static String getResetPasswordKey() {
    	return "rp-";
    }
    
    public static String getContainerKey() {
    	return "visitor-site-containers";
    }
    
    public static String getArticleKey() {
    	return "visitor-site-articles";
    }
}
