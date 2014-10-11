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
public class CategoryNameComparator implements Comparator<Category> {

	/**
	 * 
	 */
	public CategoryNameComparator() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compare(Category o1, Category o2) {
		if(null != o1 && null != o2 && null != o1.getName() && null != o1.getName()) {
			return o1.getName().compareTo(o2.getName());
		}
		return 0;
	}

}
