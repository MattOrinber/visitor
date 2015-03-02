package org.visitor.apportal.web.repository.config;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.visitor.apportal.web.repository.MyBatisRepository;
import org.visitor.appportal.visitor.domain.Article;

/**
 * 通过@MapperScannerConfigurer扫描目录中的所有接口, 动态在Spring Context中生成实现.
 * 方法名称必须与Mapper.xml中保持一致.
 * 
 * @author yong.cao
 * @create-time 2015-2-18
 * @revision 1.0.0
 * @E_mail yong.cao@renren-inc.com
 */
@MyBatisRepository
public interface ArticleMybatisDao {

	Article findById(@Param("articleId") Long article_id,
			@Param("articleName") String article_name);

	Article findArticleById(Long article_id);

	List<Article> findAll();

	List<Article> find(Map<String, Object> parameters, RowBounds bounds);

	int findTotalNum(Map<String, Object> parameters);

	void insert(Article article);

	void update(Article article);

	void delete(Long article_id, String article_name);

}
