package org.visitor.appportal.web.vo;

import org.visitor.appportal.domain.Category;


public class CategoryOption {
	private int level;
	public Category category;
	public String label;
	public String value;
	
	public CategoryOption() {
		
	}
	
	public CategoryOption(Category node) {
		this.category = node;
		if(null != node) {
			Category parent = node.getParentCategory();
			while(parent != null) {
				level ++;
				parent = parent.getParentCategory();
			}
			
			StringBuffer _ = new StringBuffer();
			for (long i = 1; i <= level; i++) {
				_.append("--");
			}
			label = _.append(node.getName()).toString();
			value = String.valueOf(node.getCategoryId());
		}
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}


	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}
	/**
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CategoryOption other = (CategoryOption) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	
	
}
