package org.visitor.appportal.repository.newsite;

import org.springframework.data.jpa.repository.Query;
import org.visitor.appportal.repository.base.BaseRepository;
import org.visitor.appportal.visitor.domain.Article;

public interface VisitorArticleRepository extends BaseRepository<Article, Long> {
	@Query("select a from Article a where articleName = ?1")
	Article getArticleByName(String articleName);
}
