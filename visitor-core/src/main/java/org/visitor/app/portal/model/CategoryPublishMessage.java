/**
 * 
 */
package org.visitor.app.portal.model;

import java.util.List;

import org.visitor.appportal.domain.Category;

/**
 * @author mengw
 *
 */
public class CategoryPublishMessage {
	private Category category;
	private List<Category> children;
	private Long type;
	/**
	 * 
	 */
	public CategoryPublishMessage() {
		// TODO Auto-generated constructor stub
	}
	public CategoryPublishMessage(Category category, long type) {
		this.category = category;
		this.type = type;
	}
	public CategoryPublishMessage(Category category, long type,
			List<Category> children) {
		this(category, type);
		this.children = children;
	}
	/**
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(Category category) {
		this.category = category;
	}
	/**
	 * @return the type
	 */
	public Long getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(Long type) {
		this.type = type;
	}
	/**
	 * @return the children
	 */
	public List<Category> getChildren() {
		return children;
	}
	/**
	 * @param children the children to set
	 */
	public void setChildren(List<Category> children) {
		this.children = children;
	}

}
