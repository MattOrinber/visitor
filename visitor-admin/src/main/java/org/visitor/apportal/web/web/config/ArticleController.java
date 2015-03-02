package org.visitor.apportal.web.web.config;

import java.util.Map;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.web.Servlets;
import org.visitor.apportal.web.service.config.ArticleService;
import org.visitor.apportal.web.util.Const;
import org.visitor.appportal.visitor.domain.Article;

@Controller
@RequestMapping("/article")
public class ArticleController {

	@Autowired
	private ArticleService service;

	@RequestMapping(value = {"", "/list"})
	public String list(
			@RequestParam(value = "page", defaultValue = "1") int pageNumber,
			Model model, ServletRequest request) {

		Map<String, Object> searchParams = Servlets.getParametersStartingWith(
				request, "search_");
		Page<Article> articles = service.getArticle(searchParams, pageNumber,
				Const.PAGE_SIZE);

		model.addAttribute("articles", articles);
		model.addAttribute("searchParams", Servlets
				.encodeParameterStringWithPrefix(searchParams, "search_"));

		return "article/articleList";
	}

/*	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String createForm(Model model) {
		Article article = new Article();
		article.setArticleName("test");
		model.addAttribute("article", article);
		model.addAttribute("action", "create");
		return "article/articleForm";
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@Valid Article newArticle,
			RedirectAttributes redirectAttributes) {
		service.saveArticle(newArticle);
		redirectAttributes.addFlashAttribute("message", "创建成功");
		return "redirect:/article/";
	}*/

	@RequestMapping(value = "update/{article_id}", method = RequestMethod.GET)
	public String updateForm(@PathVariable("article_id") Long article_id,
			Model model) {
		model.addAttribute("article", service.getArticle(article_id));
		model.addAttribute("action", "update");
		return "article/articleForm";
	}

	@RequiresRoles("admin")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(
			@Valid @ModelAttribute("preloadArticle") Article article,
			RedirectAttributes redirectAttributes) {
		service.saveArticle(article);
		redirectAttributes.addFlashAttribute("message", "更新成功");
		return "redirect:/article/";
	}

/*	@RequestMapping(value = "delete/{article_id}/{article_name}")
	public String delete(@PathVariable("article_id") Long article_id,
			@PathVariable("article_name") String article_name,
			RedirectAttributes redirectAttributes) {
		service.deleteArticle(article_id, article_name);
		redirectAttributes.addFlashAttribute("message", "删除成功");
		return "redirect:/article";
	}*/

	@ModelAttribute("preloadArticle")
	public Article getArticle(
			@RequestParam(value = "articleId", required = false) Long articleId,
			@RequestParam(value = "articleName", required = false) String articleName) {
		if (articleId != null) {
			return service.getArticle(articleId, articleName);
		}
		return null;
	}

}
