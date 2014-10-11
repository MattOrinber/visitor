/**
 * 
 */
package org.visitor.appportal.redis;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.springframework.stereotype.Component;

import org.visitor.app.portal.model.Product;
import org.visitor.app.portal.model.ProductIntelligentRecommend;
import org.visitor.app.portal.model.UserPreference;
import org.visitor.appportal.domain.Advertise;
import org.visitor.appportal.domain.Category;
import org.visitor.appportal.domain.Folder;
import org.visitor.appportal.domain.HtmlPage;
import org.visitor.appportal.domain.MessageContent;
import org.visitor.appportal.domain.ModelList;
import org.visitor.appportal.domain.Picture;
import org.visitor.appportal.domain.ProductContainer;
import org.visitor.appportal.domain.ProductFile;
import org.visitor.appportal.domain.ProductPic;
import org.visitor.appportal.domain.RecommendRuleAcross;
import org.visitor.appportal.domain.SearchKeyword;
import org.visitor.appportal.domain.Site;
import org.visitor.appportal.domain.Template;

import com.alibaba.fastjson.JSON;

/**
 * @author mengw
 *
 */
@Component
public class ObjectMapperWrapper {
	private ObjectMapper objectMapper;
	/**
	 * 
	 */
	public ObjectMapperWrapper() {
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

	public Category convert2Category(String key) {
		if(StringUtils.isBlank(key)) {
			return null;
		}
		return JSON.parseObject(key,Category.class);
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

	public Folder convert2Folder(String key) {
		if(StringUtils.isBlank(key)) {
			return null;
		}
		return JSON.parseObject(key, Folder.class);
	}

	public List<Long> convert2ListLong(String key) {
		if(StringUtils.isBlank(key)) {
			return null;
		}
		return JSON.parseArray(key, Long.class);
	}
	
	public List<ProductFile> convert2ProductFiles(String key) {
		if(StringUtils.isBlank(key)) {
			return null;
		}
		return JSON.parseArray(key, ProductFile.class);
	}

	public HtmlPage convert2HtmlPage(String key) {
		if(StringUtils.isBlank(key)) {
			return null;
		}
		return JSON.parseObject(key, HtmlPage.class);
	}

	public List<ProductPic> convert2ProductPic(String key) {
		if(StringUtils.isBlank(key)) {
			return null;
		}
		return JSON.parseArray(key, ProductPic.class);
	}

	public List<MessageContent> convert2ListMessageContent(String key) {
		if(StringUtils.isBlank(key)) {
			return null;
		}
		return JSON.parseArray(key, MessageContent.class);
	}

	public Product converter2Product(String key) {
		if(StringUtils.isBlank(key)) {
			return null;
		}
		return JSON.parseObject(key, Product.class);
	}

	public ProductContainer converter2ProductContainer(String key) {
		if(StringUtils.isBlank(key)) {
			return null;
		}
		return JSON.parseObject(key, ProductContainer.class);
	}

	public Site convert2Site(String key) {
		if(StringUtils.isBlank(key)) {
			return null;
		}
		return JSON.parseObject(key, Site.class);
	}

	public ModelList convert2ModelList(String key) {
		if(StringUtils.isBlank(key)) {
			return null;
		}
		return JSON.parseObject(key, ModelList.class);
	}

	public SearchKeyword convert2SearchKeyword(String key) {
		if(StringUtils.isBlank(key)) {
			return null;
		}
		return JSON.parseObject(key, SearchKeyword.class);
	}

	public UserPreference convert2UserPreference(String key) {
		if(StringUtils.isBlank(key)) {
			return null;
		}
		return JSON.parseObject(key, UserPreference.class);
	}

	public Template convert2Template(String key) {
		if(StringUtils.isBlank(key)) {
			return null;
		}
		return JSON.parseObject(key, Template.class);
	}

	public Picture convert2Picture(String key) {
		if(StringUtils.isBlank(key)) {
			return null;
		}
		return JSON.parseObject(key, Picture.class);		
	}
	
	public Advertise convert2Advertise(String key) {
		if(StringUtils.isBlank(key)) {
			return null;
		}
		return JSON.parseObject(key, Advertise.class);		
	}
	
	public ProductIntelligentRecommend convert2ProductIntelligentRecommend(String key) {
		if(StringUtils.isBlank(key)) {
			return null;
		}
		return JSON.parseObject(key, ProductIntelligentRecommend.class);		
	}

	public RecommendRuleAcross convert2RecommendRuleAcross(String key) {
		if(StringUtils.isBlank(key)) {
			return null;
		}
		return JSON.parseObject(key, RecommendRuleAcross.class);		
	}
}
