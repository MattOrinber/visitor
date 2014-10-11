package org.visitor.appportal.service.site;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import org.visitor.appportal.domain.Category;
import org.visitor.appportal.repository.CategoryRepository;

/**
 * 分站点处理和分类相关的业务
 * @author mengw
 *
 */
public abstract class CategoryService extends SiteService{
	@Autowired
	private CategoryRepository categoryRepository;

	
	public void showTree(Category category, Model model) {
		// TODO Auto-generated method stub
		List<Category> list = getCategoryResult(category.getCategoryId());
		
		model.addAttribute("categoryNodes", list);
	}
	
	/**
	 * 不同站点对应的平台不一样
	 * @return
	 */
	public abstract List<Long> getPlatformIds();

	/**
	 * 查找指定类型的分类
	 * @param operaVersion
	 * @return
	 */
	public List<Category> getCategoryResult(long operaVersion) {
		// TODO Auto-generated method stub
    	List<Category> platforms = this.categoryRepository.findByParentCategoryIdAndStatusWithChildren(operaVersion, Category.ENABLE);
		
		List<Category> list = new ArrayList<Category>();

    	if(operaVersion == Category.PLATFORM) {//操作系统
			for(Category c : platforms) {
				if(getPlatformIds().contains(c.getCategoryId()) && null != c.getParentCategoryId() && c.getParentCategoryId().longValue() == operaVersion) {
					list.add(c);
				}
			}
		}else {
			
			for(Category c : platforms) {
				
				if(null != c.getParentCategoryId() && c.getParentCategoryId().longValue() == operaVersion) {
					list.add(c);
				}
			}
			
		}
    	
		return list;	
	}

}
