package org.visitor.app.portal.model;

import java.util.Map;

import org.visitor.appportal.domain.Template;

public class TemplateItems {
	
	Template template;
	Map<String,String> params;
	
	/**
	 * @param template
	 * @param params
	 */
	public TemplateItems(Template template, Map<String, String> params) {
		super();
		this.template = template;
		this.params = params;
	}
		
	/**
	 * 
	 */
	public TemplateItems() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the template
	 */
	public Template getTemplate() {
		return template;
	}
	/**
	 * @param template the template to set
	 */
	public void setTemplate(Template template) {
		this.template = template;
	}
	/**
	 * @return the params
	 */
	public Map<String, String> getParams() {
		return params;
	}
	/**
	 * @param params the params to set
	 */
	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	
}
