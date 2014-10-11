/*

 * Template pack-mvc-3:src/main/java/web/controller/controller.e.vm.java
 */
package org.visitor.appportal.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.visitor.appportal.domain.Advertise;
import org.visitor.appportal.domain.Category;
import org.visitor.appportal.domain.Folder;
import org.visitor.appportal.domain.ProductList;
import org.visitor.appportal.domain.SearchKeyword;
import org.visitor.appportal.domain.Site;
import org.visitor.appportal.redis.SiteRedisRepository;
import org.visitor.appportal.repository.AdvertiseRepository;
import org.visitor.appportal.repository.FolderRepository;
import org.visitor.appportal.repository.ProductListRepository;
import org.visitor.appportal.repository.SearchKeywordRepository;
import org.visitor.appportal.repository.SiteRepository;
import org.visitor.appportal.repository.base.OrderByDirection;
import org.visitor.appportal.repository.base.SearchMode;
import org.visitor.appportal.repository.base.SearchTemplate;
import org.visitor.appportal.service.CategoryService;
import org.visitor.appportal.service.site.SearchKeywordService;
import org.visitor.appportal.service.site.SiteService.ResultEnum;
import org.visitor.appportal.web.controller.common.BaseController;
import org.visitor.appportal.web.utils.SiteUtil;

@Controller
@RequestMapping("/domain/searchkeyword/")
public class SearchKeywordController extends BaseController {

    @Autowired
    private SearchKeywordRepository searchKeywordRepository;    
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private SiteRedisRepository siteRedisRepository;
    @Autowired
    private ProductListRepository productListRepository;
    @Autowired
    private FolderRepository folderRepository;
	@Autowired
	private Properties systemProperties;
    @Autowired
    private AdvertiseRepository advertiseRepository;
	@Autowired
	private CategoryService categoryService;
    
    @InitBinder
    public void initDataBinder(WebDataBinder binder){
        // you can explicitly allow certain fields or exclude them
        // binder.setAllowedFields(new String[]{"resourceAudio.resourceid", "resourceAudio.resourceName", "resourceAudio.userid", "resourceAudio.status", "resourceAudio.auditStatus", "resourceAudio.uploadBy", "resourceAudio.lastoperBy", "resourceAudio.createDt", "resourceAudio.lastoperDt", "resourceAudio.dlCount", "resourceAudio.author", "resourceAudio.audioProperty", "resourceAudio.recordCorp", "resourceAudio.version", "resourceAudio.language", "resourceAudio.region", "resourceAudio.rhythm", "resourceAudio.musicType", "resourceAudio.musicSuit", "resourceAudio.singer", "resourceAudio.lyricsAuthor", "resourceAudio.albumName", "resourceAudio.wayOperate", "resourceAudio.resourceSize", "resourceAudio.resourceFormat", "resourceAudio.audioSource", "resourceAudio.intro", "resourceAudio.tag", "resourceAudio.ifIndexed", "resourceAudio.fileCount", "resourceAudio.resourceStatus"});
        binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }
    /**
     * Performs the list action.
     */
    @RequestMapping(value = { "list", "" })
    public String list(@ModelAttribute SearchKeywordSearchForm searchKeywordSearchForm, 
    		HttpServletRequest request, Model model) {
    	
    	Integer siteId = SiteUtil.getSiteFromSession(request.getSession());
    	
    	if(siteId != null) {
    		
    		SearchKeywordService sks = getServiceFactory().getSearchKeywordService(siteId);
    		sks.list(searchKeywordSearchForm, model);
    		
    		return "domain/searchkeyword/list";
    	}else {
    		return "redirect:/login";
    	}
    }


    /**
     * 添加搜索关键词，需要事先准备站点列表，以及所有的平台列表
     * @param searchKeyword
     * @param model
     * @return
     */
    @RequestMapping(value = "create", method = GET)
    public String create(@ModelAttribute SearchKeyword searchKeyword,Model model) {
    	//model.addAttribute("platList", catList);
        return "domain/searchkeyword/create";
    }

    /**
     * Performs the create action and redirect to the show view.
     */
	@RequestMapping(value = "create", method = { POST, PUT })
	public String create(@Valid @ModelAttribute SearchKeyword searchKeyword,
			BindingResult bindingResult,Model model, HttpServletRequest request) {
		
		Integer siteId = SiteUtil.getSiteFromSession(request.getSession());
		
		if(siteId != null) {
			
			SearchKeywordService sks = getServiceFactory().getSearchKeywordService(siteId);
			ResultEnum re = sks.create(searchKeyword, bindingResult, model);
			
			switch(re) {
				case OK:
					return "redirect:/domain/searchkeyword/show/" + searchKeyword.getPrimaryKey(); 
				case ERROR:
					return create(searchKeyword,model);
				default:
					return "redirect:/login";
			}
		}else {
			return "redirect:/login";
		}
		
	}
    

    /**
     * 
     * 前端展示，需要做的调整是，只显示热词，同时也只对热词进行排序
     * @param siteId
     * @param model
     * @return
     */
    @RequestMapping(value = "showfront", method = GET)
    public String showfront(@RequestParam(value = "siteId", required = false) Integer siteId, Model model,
    		HttpServletRequest request) {
    	if(null != siteId) {
	    	Site site = siteRepository.findOne(siteId);
	    	if (site != null) {        	
	    		String defaultkeyword = siteRedisRepository.getDefaultSearchKeyword(site.getSiteId());
	        	SearchKeyword searchKeyword = new SearchKeyword();
	        	searchKeyword.setStatus(0);
	        	searchKeyword.setSite(site);
	        	
	        	/*指定站点下的可用的热词*/
	        	List<SearchKeyword> searchKeywords = 
	        		searchKeywordRepository.findBySiteIdAndStatusAndIsHot(site.getSiteId(), 
	        		SearchKeyword.StatusEnum.Enable.ordinal(),SearchKeyword.IsHotEnum.HOT.ordinal(), 
	        		new Sort(new Order(Direction.ASC, "sortOrder")));
	        	
	        	model.addAttribute("searchKeywords", searchKeywords);
	        	model.addAttribute("defaultkeyword", defaultkeyword);
	        	model.addAttribute("site", site);
	    	} else {
	    		List<Site> sites = siteRepository.findByStatus(Site.StatusEnum.Enable.ordinal());
	    		model.addAttribute("sites", sites);
	    	}
    	} else {
    		
    		Integer currSiteId = SiteUtil.getSiteFromSession(request.getSession());
    		
    		if(currSiteId != null){
	    		Site site = siteRepository.findOne(currSiteId);
	    		Map<Site, String> map = new HashMap<Site, String>();
	    		map.put(site, this.siteRedisRepository.getDefaultSearchKeyword(site.getSiteId()));
	    		model.addAttribute("sitesMap", map);
    		}
    	}
        return "domain/searchkeyword/showfront";
    }

    /**
     * Serves search by example form, search by pattern form and search by named query form.
     */
    @RequestMapping(value = "*", method = GET)
    public void catchAll(@ModelAttribute SearchKeywordSearchForm searchKeywordSearchForm, Model model) {
		List<Site> sites = siteRepository.findByStatus(Site.StatusEnum.Enable.ordinal());
		model.addAttribute("sites", sites);
    }
    
	@RequestMapping(value = "/sort", method = GET)
	@ResponseBody
	public Boolean keywordSort(@RequestParam(value = "siteId", required = false) Integer siteId,
			@RequestParam(value = "searchKeywords", required = false) String searchKeywords) {
		if (StringUtils.isNotEmpty(searchKeywords)) {
			String[] searchKeywordIds = searchKeywords.split(",");
			SearchKeyword sk = new SearchKeyword();
			
			for (int i = 0; i < searchKeywordIds.length ;i++) {
				Long SearchKeywordId = Long.valueOf(searchKeywordIds[i]);
				sk = searchKeywordRepository.findOne(SearchKeywordId);
				sk.setSortOrder((long)(i+1));
				searchKeywordRepository.save(sk);
			}
		} else {
			return false;
		}
		return true;
	}
	
	@RequestMapping(value = "/defaultkeyword", method = POST)
	@ResponseBody
	public Boolean defaultkeyword(@RequestParam(value = "siteId", required = false) Integer siteId,
			@RequestParam(value = "defaultkeyword", required = false) String defaultkeyword) {
		if (siteId != null && defaultkeyword != null) {
            String[] wordList = defaultkeyword.split(";");
           siteRedisRepository.setDefaultSearchKeyword(siteId, defaultkeyword);
		} else {
			return false;
		}
		return true;
	}

	/**
	 * Performs the list action.
	 */
	@RequestMapping(value = { "prdlist" })
	public String prdlist(@ModelAttribute ProductListSearchForm productListSearchForm, Model model) {
		ProductList example = productListSearchForm.getProductList();
		example.setDownStatus(ProductList.ENABLE);
		
		if (example.getProductId() != null) {
			final ProductList o = productListRepository.findOne(productListSearchForm.getProductList().getPrimaryKey());
			model.addAttribute("productListsCount", o != null ? 1 : 0);
			List<ProductList> list = new ArrayList<ProductList>();
			list.add(o);
			model.addAttribute("productLists", list);
		} else {
			SearchTemplate search = productListSearchForm.toSearchTemplate();
			search.setSearchMode(SearchMode.ANYWHERE);
			search.addOrderBy("modDate", OrderByDirection.DESC);
			
			int count = productListRepository.findCount(example, search);
			model.addAttribute("productListsCount", count);
			if(count > 0) {
				model.addAttribute("productLists", productListRepository.find(example, search));
			}
		}
		return "domain/searchkeyword/prdlist";
	}	
	
    @RequestMapping(value = { "search", "" })
    public String search(Model model) {
    	if(!model.containsAttribute("searchKeywordSearchForm")) {
    		model.addAttribute("searchKeywordSearchForm", new SearchKeywordSearchForm());
    	}
    	
        return "domain/searchkeyword/search";
    }
    
    @RequestMapping(value = "deletemulti/{itemIds}", method = GET)
    public String deleteMulti(@PathVariable("itemIds") String itemIds){
    	if(StringUtils.isNotBlank(itemIds)){
			String arrOfItem[] = itemIds.split("_");
			for(String itemId:arrOfItem){
				searchKeywordRepository.delete(searchKeywordRepository.findOne(Long.valueOf(itemId)));
			}
    	}
    	
    	return "redirect:/domain/searchkeyword/search";
    }
    
	@RequestMapping(value = { "fldlist" })
	public String fldlist(@ModelAttribute FolderSearchForm folderSearchForm, Model model) {
		
    	Folder example = folderSearchForm.getFolder();
    	example.setStatus(Folder.ENABLE);//只查找 可用的列表
		if (example.getFolderId() != null) {
			final Folder o = folderRepository.findOne(folderSearchForm.getFolder().getPrimaryKey());
			model.addAttribute("foldersCount", o != null ? 1 : 0);
			ArrayList<Folder> list = new ArrayList<Folder>();
			list.add(o);
			model.addAttribute("folders", list);
		} else {
			model.addAttribute("foldersCount", folderRepository.findCount(example, folderSearchForm.toSearchTemplate()));
	        model.addAttribute("folders", folderRepository.find(example, folderSearchForm.toSearchTemplate()));        
	        model.addAttribute("picDomain", systemProperties.getProperty("pic.domain"));
		}

		return "domain/searchkeyword/fldlist";
	}	

	@RequestMapping(value = { "advlist" })
	public String advlist(@ModelAttribute AdvertiseSearchForm advertiseSearchForm, Model model) {
		
    	Advertise example = advertiseSearchForm.getAdvertise();//.getFolder();
    	example.setStatus(Advertise.AdvertiseStatusEnum.Enable.ordinal());
		if (example.getAdvertiseId() != null) {
			final Advertise o = advertiseRepository.findOne(example.getAdvertiseId());
			model.addAttribute("advertiseCount", o != null ? 1 : 0);
			ArrayList<Advertise> list = new ArrayList<Advertise>();
			list.add(o);
			model.addAttribute("advertises", list);
		} else {
	    	model.addAttribute("advertiseCount", advertiseRepository.findCount(example, advertiseSearchForm.toSearchTemplate()));
	        model.addAttribute("advertises", advertiseRepository.find(example, advertiseSearchForm.toSearchTemplate()));
		}
        return "domain/searchkeyword/advlist";
   	
	}	

    /**
     * 选择频道
     * @param searchKeyword
     * @param model
     * @return
     */
    @RequestMapping(value = "selectfld", method = GET)
    public String selectfld(@RequestParam(value = "kyid", required = true) Long keywordId,Model model){
		if(!model.containsAttribute("folderSearchForm")){
			model.addAttribute("folderSearchForm", new FolderSearchForm());
		}
		
		SearchKeyword sk = this.searchKeywordRepository.findOne(keywordId);
		model.addAttribute("searchKeyword",sk);
		
		return "domain/searchkeyword/selectfld";
    }
    
    /**
     * 选择广告
     * @param searchKeyword
     * @param model
     * @return
     */
    @RequestMapping(value = "selectadv", method = GET)
    public String selectadv(@RequestParam(value = "kyid", required = true) Long keywordId,Model model){
		if(!model.containsAttribute("advertiseSearchForm")){
			model.addAttribute("advertiseSearchForm", new AdvertiseSearchForm());
		}
		
		SearchKeyword sk = this.searchKeywordRepository.findOne(keywordId);
		model.addAttribute("searchKeyword",sk);
		
		return "domain/searchkeyword/selectadv";
    }	
    
    /**
     * 选择产品
     * @param searchKeyword
     * @param model
     * @return
     */
    @RequestMapping(value = "selectprd", method = GET)
    public String selectprd(@RequestParam(value = "kyid", required = true) Long keywordId,Model model){
    	if(!model.containsAttribute("productListSearchForm")) {
    		//按理说，只应该显示该搜索词所属的站点及平台版本下的产品，但遗憾的是产品没有和它们进行直接
    		//关联，所以这里无法很方便地去实现
    		model.addAttribute("productListSearchForm", new ProductListSearchForm());
    	}

		model.addAttribute("productCategoryList", categoryService.findCategorySelectTree(Category.PRODUCT_CATEGORY));
		model.addAttribute("billingTypeList", categoryService.findCategoryChild(Category.BILLING_TYPE, null));
		model.addAttribute("operationList", categoryService.findCategoryChild(Category.OPERATION_MODEL, null));
		model.addAttribute("cooperationList", categoryService.findCategoryChild(Category.COOPERATION, null));
		model.addAttribute("operatorList", categoryService.findCategoryChild(Category.OPERATOR, null));
		model.addAttribute("createByList", productListRepository.getProductCreateBy());
		model.addAttribute("productSourceList", categoryService.findCategoryChild(Category.PRODUCTSOURCE, null));
		model.addAttribute("merchantList", categoryService.findCategoryChild(Category.MERCHANT, null));
		
		SearchKeyword sk = this.searchKeywordRepository.findOne(keywordId);
		model.addAttribute("searchKeyword",sk);
		
		return "domain/searchkeyword/selectprd";
    }	
    
}