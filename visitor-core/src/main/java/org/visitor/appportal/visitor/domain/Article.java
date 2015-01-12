package org.visitor.appportal.visitor.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "article")
public class Article {
	private Long articleId;
	private String articleName;
	private String articleDesc;
	private String articleContent;
	
	@Column(name = "article_id", nullable = false, unique = true, precision = 20)
	@GeneratedValue
	@Id
	public Long getArticleId() {
		return articleId;
	}
	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}
	
	@Length(max = 127)
	@Column(name = "article_name", nullable = false, unique = true)
	public String getArticleName() {
		return articleName;
	}
	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}
	
	@Length(max = 255)
	@Column(name = "article_desc")
	public String getArticleDesc() {
		return articleDesc;
	}
	public void setArticleDesc(String articleDesc) {
		this.articleDesc = articleDesc;
	}
	
	@Lob
	@Column(name = "article_content",columnDefinition="TEXT")
	public String getArticleContent() {
		return articleContent;
	}
	public void setArticleContent(String articleContent) {
		this.articleContent = articleContent;
	}
}
