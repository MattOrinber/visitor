package org.visitor.apportal.web.service.config;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.visitor.apportal.web.repository.config.ArticleMybatisDao;
import org.visitor.appportal.visitor.domain.Article;

@Component
@Transactional
public class ArticleService {

	@Autowired
	private ArticleMybatisDao articleDao;

	/**
	 * 创建分页查询.
	 * 
	 * @param filterParams
	 * @param pageNumber
	 * @param pageSize
	 * @param sortType
	 * @return Page<Test>
	 */
	public Page<Article> getArticle(Map<String, Object> filterParams,
			int pageNumber, int pageSize) {

		List<Article> articles = articleDao.find(filterParams,
				buildRowBounds(pageNumber, pageSize));
		int totalnum = articleDao.findTotalNum(filterParams);

		return new PageImpl<Article>(articles, new PageRequest(pageNumber - 1,
				pageSize), totalnum);
	}

	/**
	 * 查找所有的Article.
	 * 
	 * @return List<Article>
	 */
	public List<Article> getAllArticle() {
		return articleDao.findAll();
	}

	/**
	 * 创建分页请求.
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @return RowBounds
	 */
	private RowBounds buildRowBounds(int pageNumber, int pageSize) {
		return new RowBounds((pageNumber - 1) * pageSize, pageSize);
	}

	/**
	 * 保存Article.
	 * 
	 * @param newArticle
	 */
	public void saveArticle(Article newArticle) {
		if (newArticle.getArticleId() != null
				&& newArticle.getArticleName() != null) {
			articleDao.update(newArticle);
		} else {
			articleDao.insert(newArticle);
		}
	}
	
	/**
	 * 根据主键获取Article
	 * 
	 * @param id
	 * @return Article
	 */
	public Article getArticle(Long article_id) {
		return articleDao.findArticleById(article_id);
	}

	/**
	 * 根据主键获取Article
	 * 
	 * @param id
	 * @return Article
	 */
	public Article getArticle(Long article_id, String article_name) {
		return articleDao.findById(article_id,
				article_name);
	}

	/**
	 * 根据主键删除Article
	 * 
	 * @param id
	 */
	public void deleteArticle(Long article_id, String article_name) {
		articleDao.delete(article_id, article_name);
	}

}
