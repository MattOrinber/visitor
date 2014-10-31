package org.visitor.appportal.redis;

public class RedisKeysForVisitor {
	public RedisKeysForVisitor() {
	}
	
	public static String getVisitorRedisSplit() {
    	return "--#%%#--";
    }
    
    // redis info for login use
    // the key should be email
    // the value should be userid+split+passwordmd5
    public static String getVisitorSiteUserPasswordFirstKey() {
    	return "visitor-site-userpasswordkey";
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
}
