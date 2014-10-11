/**
 * 
 */
package org.visitor.app.portal.model;

import java.util.Comparator;

import org.visitor.appportal.domain.Category;

/**
 * @author mengw
 *
 */
public class CategorySortComparator implements Comparator<Category>{

	/**
	 * 
	 */
	public CategorySortComparator() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compare(Category o1, Category o2) {
		if(null != o1 && null != o2 
				&& null != o1.getSortOrder() && null != o1.getSortOrder()) {
			return o1.getSortOrder().compareTo(o2.getSortOrder());
		}
		return 0;
	}

}
