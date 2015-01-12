package org.visitor.appportal.service.newsite.redis;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.visitor.appportal.redis.ObjectMapperWrapperForVisitor;
import org.visitor.appportal.redis.RedisKeysForVisitor;
import org.visitor.appportal.visitor.domain.Article;

@Service("articleRedisService")
public class ArticleRedisService {
	@Autowired
	private StringRedisTemplate stringRedisVisitorTemplate;
	@Autowired
	private StringRedisTemplate compressStringRedisVisitorTemplate;
	@Autowired
	private ObjectMapperWrapperForVisitor objectMapperWrapperForVisitor;
	
	public void saveArticleToRedis(Article art) {
		String key = RedisKeysForVisitor.getArticleKey();
		String keyT = art.getArticleName();
		
		String valueT = objectMapperWrapperForVisitor.convert2String(art);
		compressStringRedisVisitorTemplate.opsForHash().put(key, keyT, valueT);
	}
	
	public boolean hasKey(String nameStr) {
		String key = RedisKeysForVisitor.getArticleKey();
		String keyT = nameStr;
		return compressStringRedisVisitorTemplate.opsForHash().hasKey(key, keyT);
	}
	
	public Article getArticleByName(String nameStr) {
		String key = RedisKeysForVisitor.getArticleKey();
		String keyT = nameStr;
		String valueT = (String)compressStringRedisVisitorTemplate.opsForHash().get(key, keyT);
		if (StringUtils.isEmpty(valueT)) {
			return null;
		} else {
			return objectMapperWrapperForVisitor.convertToArticle(valueT);
		}
	}
}
