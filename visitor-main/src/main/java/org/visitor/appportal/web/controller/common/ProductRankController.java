package org.visitor.appportal.web.controller.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.visitor.app.portal.model.FolderRank.RankTypeEnum;
import org.visitor.appportal.domain.Category;
import org.visitor.appportal.domain.ProductList;
import org.visitor.appportal.domain.ProductSiteFolder;
import org.visitor.appportal.domain.Site;
import org.visitor.appportal.domain.SiteValue.TypeEnum;
import org.visitor.appportal.redis.RedisKeys;
import org.visitor.appportal.repository.ProductListRepository;
import org.visitor.appportal.repository.ProductSiteFolderRepository;
import org.visitor.appportal.repository.ProductStateRepository;
import org.visitor.appportal.repository.SiteRepository;
import org.visitor.appportal.repository.SiteValueRepository;
import org.visitor.appportal.service.ProductService;
import org.visitor.appportal.service.SiteFolderService;
import org.visitor.appportal.web.vo.FolderOption;

@Controller
@RequestMapping("/domain/productrank/")
public class ProductRankController {
	
	protected static final Logger logger = LoggerFactory.getLogger(ProductRankController.class);
	
	final static private String DATE_FORMAT = "yyyy-MM-dd hhmm";
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private SiteRepository siteRepository;
	@Autowired
	private SiteValueRepository siteValueRepository;
	@Autowired
	private ProductListRepository productListRepository;
	@Autowired
	private SiteFolderService siteFolderService;
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductSiteFolderRepository productSiteFolderRepository;
	@Autowired
	private ProductStateRepository productStateRepository;
	
    @InitBinder
    public void initDataBinder(WebDataBinder binder){
        binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }

    /**
     * 查看站点、频道 （日、周、月）榜
     * @param rankSearchForm
     * @param model
     * @return
     */
	@RequestMapping(value = {"show", ""})
	public String showRankList(@ModelAttribute RankSearchForm rankSearchForm, Model model){
		if (rankSearchForm.getSiteId() != null) {
			//取得：频道所在站点下的所有平台版本
			Site s = siteRepository.findOne(rankSearchForm.getSiteId());
			
			List<Category> versions = siteValueRepository.getSiteValueCategory(s.getSiteId(), TypeEnum.PlatformVerion.getValue());
			List<FolderOption> folders = siteFolderService.findFolderSelectTree(s.getSiteId());
			
			model.addAttribute("versions", versions);
			model.addAttribute("folders", folders);
			
			if(rankSearchForm.getVersion() == null && versions != null && versions.size() > 0){
				rankSearchForm.setVersion(versions.get(0).getCategoryId());
			}

			String key = "";
			
			if (rankSearchForm.getFolderId() != null) {
				key = RedisKeys.getFolderRankKey(rankSearchForm.getSiteId(), rankSearchForm.getFolderId(),
						RankTypeEnum.getInstance(rankSearchForm.getType()), rankSearchForm.getVersion(), rankSearchForm.getLevel());
			} else {
				key = RedisKeys.getSiteRankKey(rankSearchForm.getSiteId(), 
						RankTypeEnum.getInstance(rankSearchForm.getType()), rankSearchForm.getVersion(), rankSearchForm.getLevel());
			}		
			
			Set<String> pids = stringRedisTemplate.opsForZSet().reverseRange(
					key, rankSearchForm.getFirstResults(), rankSearchForm.getMaxResults());
			
			if (pids != null && pids.size() > 0) {
				List<ProductList> productLists = new ArrayList<ProductList>();
				for (String pid : pids) {
					try{
						ProductList productList = productListRepository.findByProductId(Long.valueOf(pid));
						productList.setProductState(productStateRepository.findByProductId(Long.valueOf(pid)));
						productLists.add(productList);
					}catch(Exception e){
						logger.error("can't find the product with id :" + pid);
					}
				}
				model.addAttribute("productLists", productLists);
			}
		}
		return "domain/common/rank/show";
	}
	
	/**
	 * 
	 * @param rankSearchForm
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"new", ""})
	public String showNewProduct(@ModelAttribute RankSearchForm rankSearchForm, Model model){
		if (rankSearchForm.getSiteId() != null) {
			//取得：频道所在站点下的所有平台版本
			Site s = siteRepository.findOne(rankSearchForm.getSiteId());
			List<Category> versions = siteValueRepository.getSiteValueCategory(s.getSiteId(), TypeEnum.PlatformVerion.getValue());
			model.addAttribute("versions", versions);
			
			if(rankSearchForm.getVersion() == null ){
				rankSearchForm.setVersion(versions.get(0).getCategoryId());
			}
			rankSearchForm.setType(RankTypeEnum.NewUpdate.ordinal());
			
			String key = RedisKeys.getSiteRankKey(rankSearchForm.getSiteId(), RankTypeEnum.NewUpdate, rankSearchForm.getVersion() , rankSearchForm.getLevel());
			Set<TypedTuple<String>> res = stringRedisTemplate.opsForZSet().reverseRangeWithScores(key, rankSearchForm.getFirstResults(), rankSearchForm.getMaxResults());
			if (res != null && res.size() > 0) {
				List<ProductList> productLists = new ArrayList<ProductList>();
				for (TypedTuple<String> val : res) {
					ProductList productList = productListRepository.findByProductId(Long.valueOf(val.getValue()));
					productList.setPublishDate(convertSortToDate(val.getScore().longValue()));
					productLists.add(productList);
				}
				model.addAttribute("productLists", productLists);
			}			
		}		

		return "domain/common/rank/show_new";
	}
	
	@RequestMapping(value = {"new/update", ""})
	public String updateNewProduct(@ModelAttribute RankSearchForm rankSearchForm, Model model,
			@RequestParam(value = "time", required = false) String time){
		if (rankSearchForm.getProductId() != null) {
			ProductList productList = productListRepository.findByProductId(rankSearchForm.getProductId());
			if (productList != null) {
				List<ProductSiteFolder> psf = productSiteFolderRepository.findProductSiteFolders(productList.getProductId());
				if (psf != null && psf.size() > 0) {
					Long sort = convertDateToSort(rankSearchForm.getPublishDate(), time);
					for (ProductSiteFolder p : psf) {
						productService.publishNewProduct(p, sort);
					}
				}
			}
		}
		return "redirect:/domain/productrank/new?siteId=" + rankSearchForm.getSiteId();
	}
	
	@RequestMapping(value = {"new/reset", ""})
	public String updateNewProduct(@ModelAttribute RankSearchForm rankSearchForm, Model model){
		if (rankSearchForm.getProductId() != null) {
			ProductList productList = productListRepository.findByProductId(rankSearchForm.getProductId());
			if (productList != null) {
				List<ProductSiteFolder> psf = productSiteFolderRepository.findProductSiteFolders(productList.getProductId());
				if (psf != null && psf.size() > 0) {
					for (ProductSiteFolder p : psf) {
						productService.publishNewProduct(p, p.getProduct().getCreateDate().getTime());
					}
				}
			}
		}
		return "redirect:/domain/productrank/new?siteId=" + rankSearchForm.getSiteId();
	}
	
	private Long convertDateToSort(Date date, String time){
		if (StringUtils.isEmpty(time)) {
			time = "0000";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		String cur = sdf.format(date);
		cur = cur.substring(0, 11) + time;
		try {
			return sdf.parse(cur).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	
	private Date convertSortToDate(Long sort){
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(sort);
		return cal.getTime();
	}
	
	public static void main(String arg[]) {
		System.out.println(RankTypeEnum.getInstance(0));
		System.out.println(RankTypeEnum.getInstance(1));
		System.out.println(RankTypeEnum.getInstance(2));
		System.out.println(RankTypeEnum.getInstance(3));
		System.out.println(RankTypeEnum.getInstance(4));
		System.out.println(RankTypeEnum.getInstance(5));
	}
}
