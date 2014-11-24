package org.visitor.appportal.redis;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.springframework.stereotype.Component;
import org.visitor.appportal.visitor.domain.Product;
import org.visitor.appportal.visitor.domain.ProductAddress;
import org.visitor.appportal.visitor.domain.User;

import com.alibaba.fastjson.JSON;

@Component
public class ObjectMapperWrapperForVisitor {
	private ObjectMapper objectMapper;
	/**
	 * 
	 */
	public ObjectMapperWrapperForVisitor() {
		// TODO Auto-generated constructor stub
	}

	public ObjectMapper getObjectMapper() {
		if(null == objectMapper) {
			objectMapper = new ObjectMapper();
			objectMapper.getDeserializationConfig().disable(Feature.FAIL_ON_UNKNOWN_PROPERTIES);
			objectMapper.getSerializationConfig().setSerializationInclusion(Inclusion.NON_NULL);
		}
		return this.objectMapper;
	}
	
	public String convert2String(Object object) {
		try {
			return this.getObjectMapper().writeValueAsString(object);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public User convertToUser(String key) {
		if (StringUtils.isBlank(key)) {
			return null;
		}
		return JSON.parseObject(key, User.class);
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
}
