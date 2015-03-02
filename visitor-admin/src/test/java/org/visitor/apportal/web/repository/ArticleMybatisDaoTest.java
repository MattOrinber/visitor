package org.visitor.apportal.web.repository;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springside.modules.test.spring.SpringTransactionalTestCase;
import org.visitor.apportal.web.repository.config.ArticleMybatisDao;
import org.visitor.appportal.visitor.domain.Article;

@DirtiesContext
@ContextConfiguration(locations = "/applicationContext.xml")
public class ArticleMybatisDaoTest extends SpringTransactionalTestCase {

	@Autowired
	private ArticleMybatisDao dao;

	@Test
	public void testFindAll() {
		Article article = dao.findById(4L, "test");
		Assert.assertNotNull(article);
	}

}
