package org.visitor.appportal.service.newsite;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.visitor.appportal.repository.newsite.VisitorArticleRepository;
import org.visitor.appportal.service.newsite.searchforms.ArticleSearchForm;
import org.visitor.appportal.visitor.domain.Article;
import org.visitor.appportal.web.utils.WebInfo;

@Service("visitorArticleService")
public class VisitorArticleService {
	private static final Logger logger = LoggerFactory.getLogger(VisitorArticleService.class);
	
	@Autowired
	private VisitorArticleRepository visitorArticleRepository;
	
	@Transactional
	public void save(Article article) {
		visitorArticleRepository.save(article);
		if (logger.isInfoEnabled()) {
			logger.info("article :"+article.getArticleName()+": saved");
		}
	}
	
	@Transactional
	public Article getArticleByName(String nameStr) {
		return visitorArticleRepository.getArticleByName(nameStr);
	}
	
	@Transactional
	public List<Article> getArticleByPage(Long pageIdx) {
		ArticleSearchForm asf = new ArticleSearchForm();
		asf.getSp().setPageNumber(pageIdx.intValue());
		asf.getSp().setPageSize(WebInfo.pageSize.intValue());
		Page<Article> pagedArticle = visitorArticleRepository.findAll(asf.toSpecification(), asf.getPageable());
		
		return pagedArticle.getContent();
	}
}
