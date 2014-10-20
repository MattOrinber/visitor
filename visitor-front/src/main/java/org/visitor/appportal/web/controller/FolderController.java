/*

 * Template pack-mvc-3:src/main/java/web/controller/controller.e.vm.java
 */
package org.visitor.appportal.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
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

import org.visitor.appportal.context.AccountContext;
import org.visitor.appportal.domain.Category;
import org.visitor.appportal.domain.Folder;
import org.visitor.appportal.domain.Picture;
import org.visitor.appportal.domain.ProductList;
import org.visitor.appportal.domain.ProductSiteFolder;
import org.visitor.appportal.redis.SiteRedisRepository;
import org.visitor.appportal.repository.FolderRepository;
import org.visitor.appportal.repository.PictureRepository;
import org.visitor.appportal.repository.ProductListRepository;
import org.visitor.appportal.repository.SiteRepository;
import org.visitor.appportal.repository.base.OrderByDirection;
import org.visitor.appportal.repository.base.SearchMode;
import org.visitor.appportal.repository.base.SearchTemplate;
import org.visitor.appportal.service.CategoryService;
import org.visitor.appportal.service.SiteFolderService;
import org.visitor.appportal.web.utils.SiteUtil;
import org.visitor.appportal.web.vo.FolderOption;
import org.visitor.appportal.web.vo.SiteFolderNode;

@Controller
@RequestMapping("/domain/folder/")
public class FolderController {
	
    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private FolderRepository folderRepository;
    
	@Autowired
	private ProductListRepository productListRepository;
	
	@Autowired
	private PictureRepository pictureRepository;
			
	@Autowired
	private SiteFolderService sitefolderService;
	
	@Autowired
	private CategoryService categoryService;

	@Autowired
	private SiteRedisRepository siteRedisRepository;
	
	@Autowired
	private Properties systemProperties;
	
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
    public String list(@ModelAttribute FolderSearchForm folderSearchForm, 
    	Model model,HttpServletRequest request) {
		
    	Folder example = folderSearchForm.getFolder();
    	
    	Integer siteId = SiteUtil.getSiteFromSession(request.getSession());
    	
    	if(siteId != null){
			if (example.getFolderId() != null) {
				final Folder o = folderRepository.findByFolderIdAndSiteId(folderSearchForm.getFolder().getPrimaryKey(),siteId);
				model.addAttribute("foldersCount", o != null ? 1 : 0);
				ArrayList<Folder> list = new ArrayList<Folder>();
				list.add(o);
				model.addAttribute("folders", list);
			} else {
				
				example.setSiteId(siteId);//for test 
				model.addAttribute("foldersCount", folderRepository.findCount(example, folderSearchForm.toSearchTemplate()));
		        model.addAttribute("folders", folderRepository.find(example, folderSearchForm.toSearchTemplate()));        
		        model.addAttribute("picDomain", systemProperties.getProperty("pic.domain"));
			}
			
	        return "domain/folder/list";
	        
    	}else {
    		
    		return "redirect:/login";
    		
    	}
    }

    /**
     * Serves the create form.
     */
    @RequestMapping(value = "create", method = GET)
    public String create(@ModelAttribute Folder folder,
    		@RequestParam(value = "parentFolderId", required = false) Long parentFolderId,
    		Model model,HttpServletRequest request) {
    	
    	Integer siteId = SiteUtil.getSiteFromSession(request.getSession());
    	
    	if (siteId != null) {
        	
    		folder.setSite(siteRepository.findOne(siteId));
    		List<FolderOption> listFO = sitefolderService.findFolderSelectTree(siteId);
    		model.addAttribute("folderList", listFO);
    	
	    	if (parentFolderId != null) {
	    		Folder parentFolder = folderRepository.findOne(parentFolderId);
	    		folder.setSite(parentFolder.getSite());
	    		folder.setParentFolder(parentFolder);
	    	}
	    	List<Picture> list = pictureRepository.findByType(Picture.PictureType.folder.getValue());
	    	model.addAttribute("pictureList", list);
	    	model.addAttribute("picDomain", systemProperties.getProperty("pic.domain"));
	    	model.addAttribute("safeTypeList", Arrays.asList(Folder.SafeTypeEnum.values()));
	    	
	    	return "domain/folder/create";
    	}else {
    		return "redirect:/login";
    	}
    }

    /**
     * Performs the create action and redirect to the show view.
     */
    @RequestMapping(value = "create", method = { POST, PUT })
    public String create(@Valid @ModelAttribute Folder folder, BindingResult bindingResult, Model model,
    		HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
        	
        	Long parentFolderId = (folder.getParentFolder() != null ? folder.getParentFolder().getFolderId() : null);
            return create(folder,parentFolderId,model,request);
        }
         
        Integer siteId = SiteUtil.getSiteFromSession(request.getSession());
        
        if(siteId != null){
	        Date cdate = new Date();
	        String username = AccountContext.getAccountContext().getUsername();
	        
	        folder.setStatus(Folder.ENABLE);
	        folder.setFolderType(Folder.FOLDER_TYPE_BUSSINESS);
	        folder.setCreateBy(username);
	        folder.setCreateDate(cdate);
	        folder.setModBy(username);
	        folder.setModDate(cdate);
	        folder.setSortOrder(cdate);
	        folder.setSiteId(siteId);
	        
	        sitefolderService.saveBussinessFolderInit(folder);
	        
	        return "redirect:/domain/folder/show/" + folder.getPrimaryKey();
        }else {
        	return "redirect:/login";
        }
    }
    
    /**
     * Serves get site model values form.
     */
    @RequestMapping(value = {"addproduct", ""})
    public String addproduct(@ModelAttribute ProductListSearchForm productListSearchForm,
    		@RequestParam(value = "folderId", required = true) Long folderId, Model model){
    	
    	ProductList productList = productListSearchForm.getProductList();
		model.addAttribute("billingTypeList", categoryService.findCategoryChild(Category.BILLING_TYPE, null));
		model.addAttribute("productCategoryList", categoryService.findCategorySelectTree(Category.PRODUCT_CATEGORY));
		model.addAttribute("importByList", categoryService.findCategoryChild(Category.IMPORTBY, null));
		model.addAttribute("productSourceList", categoryService.findCategoryChild(Category.PRODUCTSOURCE, null));
		model.addAttribute("operationList", categoryService.findCategoryChild(Category.OPERATION_MODEL, null));
		model.addAttribute("cooperationList", categoryService.findCategoryChild(Category.COOPERATION, null));
		model.addAttribute("operatorList", categoryService.findCategoryChild(Category.OPERATOR, null));
		model.addAttribute("createByList", productListRepository.getProductCreateBy());

		if (productList.getProductId() != null) {
			final ProductList o = productListRepository.findOne(productListSearchForm.getProductList().getPrimaryKey());
			model.addAttribute("productListsCount", o != null ? 1 : 0);
			List<ProductList> result = new ArrayList<ProductList>();
			result.add(o);
			model.addAttribute("productLists", result);
		} else {
			
			SearchTemplate search = productListSearchForm.toSearchTemplate();
			search.setSearchMode(SearchMode.ANYWHERE);
			search.addOrderBy("createDate", OrderByDirection.DESC);
			
			model.addAttribute("productListsCount", productListRepository.findCount(productListSearchForm.getProductList(), search));
			model.addAttribute("productLists", productListRepository.find(productListSearchForm.getProductList(), search));
		}
    	
    	model.addAttribute("folder", folderRepository.findOne(folderId));
    	return "domain/folder/addproduct";
    }
    
    /**
     * Serves get site model values form.
     */
    @RequestMapping(value = {"product", ""})
    public String product(@ModelAttribute ProductSiteFolderSearchForm productSiteFolderSearchForm,
    		@RequestParam(value = "folderId", required = true) Long folderId, Model model){
    	
    	Page<ProductSiteFolder> page = sitefolderService.findFolderProducts(folderId,
    			productSiteFolderSearchForm.getPageable(new Sort(new Order(Direction.DESC, "sortOrder"))));
    	
		model.addAttribute("productSiteFoldersCount", page.getTotalElements());
		model.addAttribute("productSiteFolders", page.getContent());
    	
    	model.addAttribute("folder", folderRepository.findOne(folderId));
    	return "domain/folder/product";
    }
    
    /**
     * Performs the tree action and redirect to the search view.
     */
    @RequestMapping(value = "tree", method = GET)
    public String tree(Model model,HttpServletRequest request) {
		
		Integer siteId = SiteUtil.getSiteFromSession(request.getSession());
		
		if(siteId != null) {
			List<SiteFolderNode> out = new ArrayList<SiteFolderNode>();		
			List<Folder> folders = folderRepository.findBySiteIdAndStatus(siteId, Folder.ENABLE);
			
			out.add(new SiteFolderNode(this.siteRepository.findOne(siteId), folders));
			model.addAttribute("siteTree", out);
	        return "domain/folder/tree";
		} else {
			return "redirect:/login";
		}
    }
    
	/**
	 * 站点和频道联动
	 * 
	 * @author mengw
	 * 
	 * @return
	 */
	@RequestMapping(value = "folderList/{pk}", method = GET)
	@ResponseBody
	public List<Map<String, String>> folderList(@PathVariable("pk") Integer siteId) {
		List<FolderOption> list = sitefolderService.findFolderSelectTree(siteId);
		List<Map<String, String>> out = new ArrayList<Map<String,String>>();
		for (FolderOption opt : list) {
			Map<String, String> map = new LinkedHashMap<String, String>();
			map.put("key", opt.getLabel());
			map.put("value", opt.getValue());
			out.add(map);
		}
		return out;
	}


	
	@RequestMapping(value = "/sort", method = GET)
	@ResponseBody
	public Boolean folderSort(@RequestParam(value = "folder", required = false) Long parentFolderId,
			@RequestParam(value = "folders", required = false) String folders) {
		Folder parentFolder = folderRepository.findOne(parentFolderId);
		if (StringUtils.isNotEmpty(folders)) {
			Date d = new Date();
			long times = 24*3600*1000;		
			String[] folderIds = folders.split(",");
			for (int i = 0; i < folderIds.length ;i++) {
				Long folderId = Long.valueOf(folderIds[i]);
				Folder f = folderRepository.findOne(folderId);
				f.setSortOrder(new Date(d.getTime()-(times*i)));
				folderRepository.save(f);
				siteRedisRepository.setFolderChildren(f.getSiteId(), parentFolder.getFolderId(),
						f.getFolderId(), f.getSortOrder().getTime());
			}
			
		} else {
			return false;
		}
		return true;
	}

    /**
     * Serves search by example form, search by pattern form and search by named query form.
     */
    @RequestMapping(value = "*", method = GET)
    public void catchAll(@ModelAttribute FolderSearchForm folderSearchForm) {
    }
}