/**
 * 
 */
package org.visitor.appportal.redis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import org.visitor.app.portal.model.HtmlPagePublishMessage;
import org.visitor.app.portal.model.TemplatePublishMessage;
import org.visitor.appportal.domain.HtmlPage;
import org.visitor.appportal.domain.Template;

/**
 * @author mengw
 *
 */
@Repository
public class TemplateRedisRepository {
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private ObjectMapperWrapper objectMapperWrapper;
	@Autowired
	private StringRedisTemplate compressStringRedisTemplate;	
	/**
	 * 
	 */
	public TemplateRedisRepository() {
		// TODO Auto-generated constructor stub
	}

	public HtmlPage getHtmlPageByPageId(Long pageId) {
		String key = RedisKeys.getHtmlPageIdKey();
		key = (String)compressStringRedisTemplate.opsForHash().get(key, String.valueOf(pageId));
		return this.objectMapperWrapper.convert2HtmlPage(key);
	}
	
	public void setHtmlPage(Long pageId, HtmlPage page) {
		String key = RedisKeys.getHtmlPageIdKey();
		String value = objectMapperWrapper.convert2String(page);
		stringRedisTemplate.opsForHash().put(key, String.valueOf(pageId), value);
	}
	
	/**
	 * 取页面编辑的内容
	 * @param pageId
	 * @return
	 */
	public String getHtmlPageContent(Long pageId) {
		String key = RedisKeys.getHtmlPageContentKey();
		return (String)compressStringRedisTemplate.opsForHash().get(key, String.valueOf(pageId));
	}

	public String getTemplateContent(Long templateId) {
		String key = RedisKeys.getTemplateContentKey();
		return (String)compressStringRedisTemplate.opsForHash().get(key, String.valueOf(templateId));
	}
	
	/**
	 * 发布页面
	 * @param htmlPage
	 * @param previewContent 是否预览内容。true-是，false-否
	 */
	public void publishHtmlPage(HtmlPage htmlPage, String content, boolean previewContent) {
		//保存模板内容
		compressStringRedisTemplate.opsForHash().put(RedisKeys.getHtmlPageContentKey(), 
				String.valueOf(htmlPage.getPrimaryKey()), content);

		//No need of writing meta content.
		HtmlPage temp = htmlPage.copy();
		temp.setMeta(null);
		temp.setTempMeta(null);
		//保存页面对象
		compressStringRedisTemplate.opsForHash().put(RedisKeys.getHtmlPageIdKey(), 
				String.valueOf(htmlPage.getPageId()), objectMapperWrapper.convert2String(temp));
		
		//生成模板发布消息。Thme meta key is changed to be tag name if needed.
		HtmlPagePublishMessage object = new HtmlPagePublishMessage();
		object.setSiteId(htmlPage.getSiteId());
		object.setPageId(htmlPage.getPageId());
		object.setPreview(previewContent);//是否是预览内容
//		object.setMetaKey(RedisConstants.getHtmlPageKey(htmlPage.getPageId()));
		//发布消息
		stringRedisTemplate.convertAndSend(RedisKeys.getHtmlPagePublishedMessage(), 
				objectMapperWrapper.convert2String(object));
	}
	
	/**
	 * 发布模板
	 * @param template
	 */
	public void publishTemplate(Template template, List<HtmlPage> referencePages, String content) {
		//2.1.0以后需要读取Template对象来显示。
		Template copy = template.copy();
		copy.setMeta(null);
		copy.setTempMeta(null);
		compressStringRedisTemplate.opsForHash().put(RedisKeys.getTemplateKey(), 
				String.valueOf(template.getPrimaryKey()), objectMapperWrapper.convert2String(copy));
		//保存模板内容
		compressStringRedisTemplate.opsForHash().put(RedisKeys.getTemplateContentKey(), 
				String.valueOf(template.getPrimaryKey()), StringUtils.isBlank(content) ? "" : content);
		//生成模板发布消息。Thme meta key is changed to be tag name if needed.
		TemplatePublishMessage object = new TemplatePublishMessage(template.getTemplateId(), 
				template.getType(), StringUtils.replace(template.getName(), ":", "/"));
		object.setSiteId(template.getSiteId());//必须指定siteId
		//发布消息
		stringRedisTemplate.convertAndSend(RedisKeys.getTemplatePublishedMessage(),
				objectMapperWrapper.convert2String(object));
	}

	public List<Template> getMultiTemplateByIds(Collection<Object> ids) {
		Collection<Object> geted = compressStringRedisTemplate.opsForHash().multiGet(RedisKeys.getTemplateKey(), ids);
		if(null != geted) {
			List<Template> list = new ArrayList<Template>();
			for(Object obj : geted) {
				list.add(objectMapperWrapper.convert2Template(String.valueOf(obj)));
			}
			return list;
		}
		return null;
	}
}
