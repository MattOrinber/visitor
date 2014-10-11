/*

 * Template pack-backend:src/main/java/project/manager/ManagerImpl.e.vm.java
 */
package org.visitor.appportal.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import org.visitor.appportal.domain.Category;
import org.visitor.appportal.repository.CategoryRepository;
import org.visitor.appportal.web.vo.CategoryOption;

/**
 * 
 * Default implementation of the {@link CategoryService} interface
 * 
 * @see CategoryService
 */
@Service("categoryService")
public class CategoryService {

	private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);
	
	private long spaceNeeded = 2l;
	
	@PersistenceContext
	private EntityManager em; 

	@Autowired
	public CategoryRepository categoryRepository;

	public List<Category> findCategoryChild(Long parentCategoryId, Sort sort) {
		return this.categoryRepository.findByParentCategoryIdAndStatus(parentCategoryId, Category.ENABLE, sort);
	}
	
	public boolean deleteCategory(Category category) {
		List<Category> list = this.categoryRepository.findByParentCategoryIdAndStatus(
				category.getCategoryId(), Category.ENABLE, new Sort(new Sort.Order(Direction.ASC, "categoryId")));
		if (list.size() > 0) {
			return false;
		}
		category.setStatus(Category.DISABLE);
		categoryRepository.save(category);
		return true;
	}

	
	private void makeOptionTree(Category node, List<Category> children, List<CategoryOption> out) {
		CategoryOption co = new CategoryOption(node);
		if(!out.contains(co)) {
			out.add(co);
		}
		if (children != null && children.size() > 0) {
			for (Category obj : children) {
				makeOptionTree(obj, obj.getChildren(), out);
			}
		}
	}

	public List<CategoryOption> findCategorySelectTree(Long parentId) {
		List<Category> it = categoryRepository.findByParentCategoryIdAndStatusWithChildren(parentId, Category.ENABLE);
		
		List<CategoryOption> list = new ArrayList<CategoryOption>();
		List<Category> top = new ArrayList<Category>();
		for(Category f : it) {
			if(f.getParentCategoryId() != null && f.getParentCategoryId().longValue() == parentId.longValue()) {
				top.add(f);
			}
		}
		for(Category f : top) {
			this.makeOptionTree(f, f.getChildren(), list);
		}
		return list;
	}

	public void saveNode(Category node) {
		if (null == node.getId()) {// create new category
			long newLeft = 1l;
			long newRight = 2l;
			// Create new root category.
			Session sess = (Session)em.getDelegate();
			if (node.getParentCategory() == null) {
				// Root node of a tree, new thread
				node.setNsLeft(newLeft);
				node.setNsRight(newRight);
				node.setNsThread(newRight);
				categoryRepository.save(node);
				node.setNsThread(node.getId());
				org.hibernate.Query updateRight = sess.createQuery(
						"update Category n set " + " n.nsThread = :thread "
								+ " where n.categoryId = :categoryId ");
				updateRight.setParameter("categoryId", node.getId());
				updateRight.setParameter("thread", node.getId());
				updateRight.executeUpdate();
			} else {// Has root, then create child of root.
				// Child node of a parent
				Long parentThread = node.getParentCategory().getNsThread();
				logger.debug((node == null) + "  " + (node.getParentCategory() == null) + "  "
						+ (node.getParentCategory().getNsRight()));
				newLeft = node.getParentCategory().getNsRight();
				newRight = newLeft + spaceNeeded - 1;

				// update right
				org.hibernate.Query updateRight = sess.createQuery("update Category n set "
						+ " n.nsRight = n.nsRight + :spaceNeeded "
						+ " where n.nsThread = :thread and n.nsRight >= :right");
				
				updateRight.setParameter("spaceNeeded", spaceNeeded);
				updateRight.setParameter("thread", parentThread);
				updateRight.setParameter("right", newLeft);
				updateRight.executeUpdate();

				// Update left
				org.hibernate.Query updateLeft = sess.createQuery("update Category n set "
						+ " n.nsLeft = n.nsLeft + :spaceNeeded "
						+ " where n.nsThread = :thread and n.nsLeft > :right");
				updateLeft.setParameter("spaceNeeded", spaceNeeded);
				updateLeft.setParameter("thread", parentThread);
				updateLeft.setParameter("right", newLeft);
				updateLeft.executeUpdate();

				node.setNsLeft(newLeft);
				node.setNsRight(newRight);
				node.setNsThread(parentThread);
				categoryRepository.save(node);
			}
		}
		categoryRepository.save(node);
	}

	public List<Category> getSpecialPlatList() {
		// TODO Auto-generated method stub
		//固定的一组数
		String [][] PLATFORMS = {{"287","Android"},{"306","S60 V2"},{"307","S60 V3&V5"},{"312","Java"}};
		
		List<Category> list = new ArrayList<Category>();
		for(int i= 0;i<PLATFORMS.length;i++){
			Category cat  = new Category();
			cat.setCategoryId(Long.valueOf(PLATFORMS[i][0]));
			cat.setName(PLATFORMS[i][1]);
			
			list.add(cat);
		}
		return list;
	}
}