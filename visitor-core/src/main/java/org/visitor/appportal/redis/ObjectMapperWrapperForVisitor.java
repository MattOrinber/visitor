package org.visitor.appportal.redis;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.visitor.appportal.visitor.domain.Container;
import org.visitor.appportal.visitor.domain.Product;
import org.visitor.appportal.visitor.domain.ProductAddress;
import org.visitor.appportal.visitor.domain.ProductDetailInfo;
import org.visitor.appportal.visitor.domain.ProductMultiPrice;
import org.visitor.appportal.visitor.domain.ProductOperation;
import org.visitor.appportal.visitor.domain.ProductOrder;
import org.visitor.appportal.visitor.domain.ProductPayOrder;
import org.visitor.appportal.visitor.domain.ProductPicture;
import org.visitor.appportal.visitor.domain.User;
import org.visitor.appportal.visitor.domain.UserInternalMail;
import org.visitor.appportal.visitor.domain.UserTokenInfo;

import com.alibaba.fastjson.JSON;

@Component
public class ObjectMapperWrapperForVisitor {
	/**
	 * 
	 */
	public ObjectMapperWrapperForVisitor() {
		// TODO Auto-generated constructor stub
	}
	
	public String convert2String(Object object) {
		String jsonStr = JSON.toJSONString(object);
		return jsonStr;
	}
	
	public User convertToUser(String key) {
		if (StringUtils.isBlank(key)) {
			return null;
		}
		return JSON.parseObject(key, User.class);
	}
	
	public Container convertToContainer(String key) {
		if (StringUtils.isBlank(key)) {
			return null;
		}
		return JSON.parseObject(key, Container.class);
	}
	
	public UserInternalMail convertToUserInternalMail(String key) {
		if (StringUtils.isBlank(key)) {
			return null;
		}
		return JSON.parseObject(key, UserInternalMail.class);
	}
	
	public UserTokenInfo convertToUserTokenInfo(String key) {
		if (StringUtils.isBlank(key)) {
			return null;
		}
		return JSON.parseObject(key, UserTokenInfo.class);
	}
	
	public Product convertToProduct(String key) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}
		return JSON.parseObject(key, Product.class);
	}
	
	public ProductAddress convertToProductAddress(String key) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}
		return JSON.parseObject(key, ProductAddress.class);
	}
	
	public ProductOperation convertToProductOperation(String key) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}
		return JSON.parseObject(key, ProductOperation.class);
	}
	
	public ProductPicture convertToProductPicture(String key) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}
		return JSON.parseObject(key, ProductPicture.class);
	}
	
	public ProductDetailInfo convertToProductDetailInfo(String key) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}
		return JSON.parseObject(key, ProductDetailInfo.class);
	}
	
	public ProductMultiPrice convertToProductMultiPrice(String key) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}
		return JSON.parseObject(key, ProductMultiPrice.class);
	}
	
	public ProductOrder convertToProductOrder(String key) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}
		return JSON.parseObject(key, ProductOrder.class);
	}
	
	public ProductPayOrder convertToProductPayOrder(String key) {
		if (StringUtils.isEmpty(key)) {
			return null;
		}
		return JSON.parseObject(key, ProductPayOrder.class);
	}
}
