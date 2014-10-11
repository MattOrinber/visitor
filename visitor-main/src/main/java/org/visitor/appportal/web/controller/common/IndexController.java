/**
 * 
 */
package org.visitor.appportal.web.controller.common;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import org.visitor.app.portal.model.FolderRank.RankTypeEnum;
import org.visitor.appportal.context.AccountContext;
import org.visitor.appportal.domain.ProductList;
import org.visitor.appportal.domain.ProductOperation;
import org.visitor.appportal.domain.ProductState;
import org.visitor.appportal.domain.Site;
import org.visitor.appportal.redis.RedisKeys;
import org.visitor.appportal.redis.TemplateRedisRepository;
import org.visitor.appportal.repository.HtmlPageRepository;
import org.visitor.appportal.repository.PageContainerRepository;
import org.visitor.appportal.repository.ProductListRepository;
import org.visitor.appportal.repository.ProductOperationRepository;
import org.visitor.appportal.repository.ProductStateRepository;
import org.visitor.appportal.repository.SiteRepository;
import org.visitor.appportal.web.utils.TemplatePage;

/**
 * @author mengw
 *
 */
@Controller
@RequestMapping("/")
public class IndexController {
	protected static final Logger log = LoggerFactory.getLogger(IndexController.class);
	@Autowired
	private ProductOperationRepository productOperationRepository;
	@Autowired
	private ProductListRepository productListRepository;
	@Autowired
	private TemplateRedisRepository templateRedisRepository;
	@Autowired
	private HtmlPageRepository htmlPageRepository;
	@Autowired
	private TemplatePage templatePage;
    @Autowired
    private PageContainerRepository pageContainerRepository;
	@Autowired
	private PageContentReader pageContentappportal;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;	
	@Autowired
	private SiteRepository siteRepository;
    @Autowired
    private ProductStateRepository productStateRepository;
	
	/**
	 * 
	 */
	public IndexController() {
		// TODO Auto-generated constructor stub
	}

	@RequestMapping({"index", ""})
	public String index(HttpServletRequest request, Model model) {
		Date date = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
		long create = productOperationRepository.findCreateCount(date, 
				ProductOperation.OperationTypeEnum.Create.ordinal());
		long update = productOperationRepository.findUpdateCount(date,
				ProductOperation.OperationTypeEnum.Create.ordinal());
		model.addAttribute("createdCount", create);
		model.addAttribute("updatedCount", update);
		return "index";
	}
	
	@RequestMapping({"login"})
	public String login(HttpServletRequest request, Model model) {
		if(AccountContext.getAccountContext().getAccount() != null) {
			return "redirect:/index";
		} else {
			
			/**获取所有可用站点列表*/
			List<Site> sites = this.siteRepository.findByStatus(Site.StatusEnum.Enable.ordinal());
			model.addAttribute("siteList", sites);
			return "login";
		}
	}
	@RequestMapping("export/product/dailynotice/{type}")
	public ModelAndView exportNotice(@PathVariable("type") Integer type, HttpServletRequest request, Model model) {
		List<ProductList> list = null;
		Date date = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
		if(type == 1) {
			list = productListRepository.findByDailyCreate(date, 
					ProductOperation.OperationTypeEnum.Create.ordinal());
			model.addAttribute("data", list);
			return new ModelAndView("DailyCreateProductExcelView", model.asMap());
		} else {
			list = productListRepository.findByDailyUpdate(date, 
					ProductOperation.OperationTypeEnum.Create.ordinal());
			model.addAttribute("data", list);
			return new ModelAndView("DailyUpdateProductExcelView", model.asMap());
		}
	}
	
	
	@RequestMapping("export/product/totaldownload")
	public String updateDlData() {
		String key = RedisKeys.getProductStateKey(RankTypeEnum.TotalDownload);
		Map<Object, Object> map = stringRedisTemplate.opsForHash().entries(key);
		for(Map.Entry<Object, Object> entry : map.entrySet()) {
			if(null != entry && null != entry.getValue()) {
				Long value = Long.decode(String.valueOf(entry.getValue()));
				if(log.isInfoEnabled()) {
					log.info("Get pid: " + entry.getKey() + " vaue:" + entry.getValue());
				}
				if(null != value && value.longValue() <= 100l) {
					Long productId = Long.decode(String.valueOf(entry.getKey()));
					ProductState state = this.productStateRepository.findOne(productId);
					if(null != state && null != state.getTotalDl()) {
						if(log.isInfoEnabled()) {
							log.info("Putting new totaldownload pid: " + entry.getKey() + " vaue:" + state.getTotalDl());
						}						
						stringRedisTemplate.opsForHash().put(key, String.valueOf(productId), String.valueOf(state.getTotalDl()));
					}
				}
			}
		}
		
		return "";
	}
}
