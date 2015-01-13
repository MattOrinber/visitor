package org.visitor.appportal.web.controller.common;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.visitor.appportal.service.newsite.VisitorArticleService;
import org.visitor.appportal.service.newsite.redis.ArticleRedisService;
import org.visitor.appportal.visitor.beans.ArticleTemp;
import org.visitor.appportal.visitor.beans.ResultJson;
import org.visitor.appportal.visitor.domain.Article;
import org.visitor.appportal.web.utils.FloopyInfo;

@Controller
@RequestMapping("/article/")
public class ArticleController extends BasicController {
	protected static final Logger log = LoggerFactory.getLogger(ArticleController.class);
	
	@Autowired
	private VisitorArticleService visitorArticleService;
	@Autowired
	private ArticleRedisService articleRedisService;
	
	@RequestMapping(value = "addOne", method = POST)
	public void addOne(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		ArticleTemp at = super.getArticleJSON(request);
		ResultJson rj = new ResultJson();
		
		rj.setResult(0);
		rj.setResultDesc(FloopyInfo.PUBLISH_SUCCESS);
		
		String nameStr = at.getNameStr();
		if (!articleRedisService.hasKey(nameStr)) {
			Article atNew = new Article();
			atNew.setArticleName(nameStr);
			
			String descStrT = URLDecoder.decode(at.getDescStr(), "UTF-8");
			String contentStrT = URLDecoder.decode(at.getContentStr(), "UTF-8");
			
			atNew.setArticleDesc(descStrT);
			atNew.setArticleContent(contentStrT);
			
			visitorArticleService.save(atNew);
			articleRedisService.saveArticleToRedis(atNew);
		} else {
			rj.setResult(-1);
			rj.setResultDesc("already has one :"+at.getNameStr());
		}
		
		super.sendJSONResponse(rj, response);
	}
	
	@RequestMapping(value = "updateOne", method = POST)
	public void updateOne(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		ArticleTemp at = super.getArticleJSON(request);
		ResultJson rj = new ResultJson();
		
		rj.setResult(0);
		rj.setResultDesc(FloopyInfo.PUBLISH_SUCCESS);
		
		String nameStr = at.getNameStr();
		Article atRev = articleRedisService.getArticleByName(nameStr);
		
		String descStrT = URLDecoder.decode(at.getDescStr(), "UTF-8");
		String contentStrT = URLDecoder.decode(at.getContentStr(), "UTF-8");
		
		atRev.setArticleDesc(descStrT);
		atRev.setArticleContent(contentStrT);
		
		visitorArticleService.save(atRev);
		articleRedisService.saveArticleToRedis(atRev);
		
		super.sendJSONResponse(rj, response);
	}
}
