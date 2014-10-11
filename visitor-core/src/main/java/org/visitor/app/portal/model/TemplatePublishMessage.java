/**
 * 
 */
package org.visitor.app.portal.model;

import java.util.HashMap;
import java.util.Map;


/**
 * 模板改变的消息
 * @author mengw
 *
 */
public class TemplatePublishMessage {
    private Long templateId; // 发布的模板ID
    private Integer siteId;//所在的站点。
    private Integer type; // 模板类型。0-模板；1-单独页；2-系统模板；3-tag标签
    private String metaKey;//模板内容使用的key。
//    private List<TemplateInSite> siteIds;//如果是模板，引用此模板的所有站点id；如果是单独页，此项为空

    private Map<String, String> templateAttributeMap = new HashMap<String, String>();
    public TemplatePublishMessage() {
    	
    }
    public TemplatePublishMessage(Long templateId, Integer type, String metaKey) {
    	this.templateId = templateId;
    	this.type = type;
    	this.metaKey = metaKey;
    }
    
	/**
	 * @return the templateId
	 */
	public Long getTemplateId() {
		return templateId;
	}
	/**
	 * @param templateId the templateId to set
	 */
	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}
	/**
	 * @return the type
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	/**
	 * @return the metaKey
	 */
	public String getMetaKey() {
		return metaKey;
	}
	/**
	 * @param metaKey the metaKey to set
	 */
	public void setMetaKey(String metaKey) {
		this.metaKey = metaKey;
	}
	/**
	 * @return the templateAttributeMap
	 */
	public Map<String, String> getTemplateAttributeMap() {
		return templateAttributeMap;
	}
	/**
	 * @param templateAttributeMap the templateAttributeMap to set
	 */
	public void setTemplateAttributeMap(Map<String, String> templateAttributeMap) {
		this.templateAttributeMap = templateAttributeMap;
	}
	public Integer getSiteId() {
		return siteId;
	}
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

}
