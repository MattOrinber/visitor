package org.visitor.appportal.web.controller.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.visitor.app.portal.model.Product;
import org.visitor.appportal.context.AccountContext;
import org.visitor.appportal.domain.Category;
import org.visitor.appportal.domain.Folder;
import org.visitor.appportal.domain.ProductFile;
import org.visitor.appportal.domain.ProductList;
import org.visitor.appportal.domain.ProductSiteFolder;
import org.visitor.appportal.domain.Site;
import org.visitor.appportal.redis.ProductRedisRepository;
import org.visitor.appportal.repository.CategoryRepository;
import org.visitor.appportal.repository.FolderRepository;
import org.visitor.appportal.repository.ProductFileRepository;
import org.visitor.appportal.repository.ProductListRepository;
import org.visitor.appportal.repository.ProductSiteFolderRepository;
import org.visitor.appportal.repository.SiteRepository;
import org.visitor.appportal.repository.base.OrderByDirection;
import org.visitor.appportal.repository.base.SearchMode;
import org.visitor.appportal.repository.base.SearchTemplate;
import org.visitor.appportal.service.CategoryService;
import org.visitor.appportal.service.ProductService;
import org.visitor.appportal.service.SiteFolderService;
import org.visitor.appportal.web.controller.ProductListSearchForm;
import org.visitor.appportal.web.vo.FolderOption;

@Controller
@RequestMapping("/domain/productbatch/")
public class ProductBatchController {
	
	private static final Logger logger = LoggerFactory.getLogger(ProductBatchController.class);
	
	@Autowired
	private ProductListRepository productListRepository;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private ProductService productService;
	@Autowired
	private FolderRepository folderRepository;
	@Autowired
	private ProductSiteFolderRepository productSiteFolderRepository;
	@Autowired
	private ProductRedisRepository productRedisRepository;
	@Autowired
	private ProductFileRepository productFileRepository;
	@Autowired
	private SiteRepository siteRepository;
	@Autowired
	private SiteFolderService siteFolderService;
	
    @InitBinder
    public void initDataBinder(WebDataBinder binder){
        // you can explicitly allow certain fields or exclude them
        // binder.setAllowedFields(new String[]{"resourceAudio.resourceid", "resourceAudio.resourceName", "resourceAudio.userid", "resourceAudio.status", "resourceAudio.auditStatus", "resourceAudio.uploadBy", "resourceAudio.lastoperBy", "resourceAudio.createDt", "resourceAudio.lastoperDt", "resourceAudio.dlCount", "resourceAudio.author", "resourceAudio.audioProperty", "resourceAudio.recordCorp", "resourceAudio.version", "resourceAudio.language", "resourceAudio.region", "resourceAudio.rhythm", "resourceAudio.musicType", "resourceAudio.musicSuit", "resourceAudio.singer", "resourceAudio.lyricsAuthor", "resourceAudio.albumName", "resourceAudio.wayOperate", "resourceAudio.resourceSize", "resourceAudio.resourceFormat", "resourceAudio.audioSource", "resourceAudio.intro", "resourceAudio.tag", "resourceAudio.ifIndexed", "resourceAudio.fileCount", "resourceAudio.resourceStatus"});
        binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }
	
    @RequestMapping(value = {"products", ""})
    public String addproduct(@ModelAttribute ProductListSearchForm productListSearchForm, Model model,
    		@RequestParam(value = "productids", required = false) String products){
    	
    	ProductList productList = productListSearchForm.getProductList();
		model.addAttribute("billingTypeList", categoryService.findCategoryChild(Category.BILLING_TYPE, null));
		model.addAttribute("productCategoryList", categoryService.findCategorySelectTree(Category.PRODUCT_CATEGORY));
		model.addAttribute("importByList", categoryService.findCategoryChild(Category.IMPORTBY, null));
		model.addAttribute("productSourceList", categoryService.findCategoryChild(Category.PRODUCTSOURCE, null));
		model.addAttribute("operationList", categoryService.findCategoryChild(Category.OPERATION_MODEL, null));
		model.addAttribute("cooperationList", categoryService.findCategoryChild(Category.COOPERATION, null));
		model.addAttribute("operatorList", categoryService.findCategoryChild(Category.OPERATOR, null));
		model.addAttribute("createByList", productListRepository.getProductCreateBy());
		
		List<Site> sites = siteRepository.findByStatus(Site.StatusEnum.Enable.ordinal());
		if (sites != null && sites.size() > 0) {
			List<FolderOption> folders = siteFolderService.findFolderSelectTree(sites.get(0).getSiteId());
			model.addAttribute("sites", siteRepository.findByStatus(Site.StatusEnum.Enable.ordinal()));
			model.addAttribute("folders", folders);
		}


		if (StringUtils.isNotEmpty(products)) {
			products.replaceAll("|", ",").replaceAll(";", ",").replaceAll("，", ",");
			String[] productids = products.split(",");
			if (productids.length > 0) {
				List<Long> pids = new ArrayList<Long>();
				for (int i = 0; i < productids.length; i++) {
					pids.add(Long.valueOf(productids[i].trim()));
				}
				List<ProductList> list = productListRepository.findByProductIds(pids);
				if (list != null) {
					model.addAttribute("productListsCount", list.size());
				}
				model.addAttribute("productids", products.replaceAll("\r\n", ""));
				model.addAttribute("productLists", list);
			}
			
		} else if (productList.getProductId() != null) {
			final ProductList o = productListRepository.findOne(productListSearchForm.getProductList().getPrimaryKey());
			model.addAttribute("productListsCount", o != null ? 1 : 0);
			List<ProductList> list = new ArrayList<ProductList>();
			model.addAttribute("productLists", list);
			
		} else if (productListSearchForm.getProductList().getName() != null) {
			
			SearchTemplate search = productListSearchForm.toSearchTemplate();
			search.setSearchMode(SearchMode.ANYWHERE);
			search.addOrderBy("createDate", OrderByDirection.DESC);
			//search.setMaxResults(size);
			
			model.addAttribute("productListsCount", productListRepository.findCount(productListSearchForm.getProductList(), search));
			model.addAttribute("productLists", productListRepository.find(productListSearchForm.getProductList(), search));
		}
    	
    	return "domain/common/batch/products";
    }
    
    @RequestMapping(value = {"upproducts", ""})
    public String upproducts(@ModelAttribute ProductListSearchForm productListSearchForm, Model model,
    		@RequestParam(value = "products", required = false) List<String> products){
    	if (products != null && products.size() > 0) {
			Date date = new Date();
    		List<ProductList> productLists = new ArrayList<ProductList>();
    		for (String product : products) {
    			ProductList p = productListRepository.findByProductId(Long.valueOf(product));
    			if (p.getSafeType().intValue() != ProductList.SafeTypeEnum.Unsafe.ordinal()) {
        			p.setModDate(date);
        			p.setModBy(AccountContext.getAccountContext().getUsername());
        			p.setPublishDate(date);
        			p.setPublishBy(AccountContext.getAccountContext().getUsername());
        			p.setDownStatus(ProductList.ENABLE);
        			productLists.add(p);
    			}
    		}
    		productListRepository.save(productLists);
    		model.addAttribute("products", productLists);
    	}
		return "domain/common/batch/updateproduct";
    }
    
    @RequestMapping(value = {"downproducts", ""})
    public String downproducts(@ModelAttribute ProductListSearchForm productListSearchForm, Model model, 
    		@RequestParam(value = "downReason", required = true) String downReason,
    		@RequestParam(value = "products", required = false) List<String> products){
    	if (products != null && products.size() > 0) {
			Date date = new Date();
    		List<ProductList> productLists = new ArrayList<ProductList>();
    		for (String product : products) {
    			ProductList p = productListRepository.findByProductId(Long.valueOf(product));
    			
    			p.setModDate(date);
    			p.setModBy(AccountContext.getAccountContext().getUsername());
    			p.setPublishDate(date);
    			p.setPublishBy(AccountContext.getAccountContext().getUsername());
    			
    			p.setDownReason(downReason);
    			p.setDownStatus(ProductList.DISNABLE);
    			
    			productLists.add(p);
    		}
    		productListRepository.save(productLists);
    		model.addAttribute("products", productLists);
    		return "domain/common/batch/updateproduct";
    	}
    	return null;
    }
    
    @RequestMapping(value = {"bindcategory", ""})
    public String bindcategory(@ModelAttribute ProductListSearchForm productListSearchForm, Model model,
    		@RequestParam(value = "categoryId", required = true) Long categoryId,
    		@RequestParam(value = "products", required = false) List<String> products){
    	if (products != null && products.size() > 0) {
    		if (categoryId != null) {
    			Category category = categoryRepository.findOne(categoryId);
    			if (category != null) {
    				Date date = new Date();
    	    		List<ProductList> productLists = new ArrayList<ProductList>();
    	    		for (String product : products) {
    	    			ProductList p = productListRepository.findByProductId(Long.valueOf(product));
    	    			
    	    			p.setModDate(date);
    	    			p.setModBy(AccountContext.getAccountContext().getUsername());
    	    			p.setPublishDate(date);
    	    			p.setPublishBy(AccountContext.getAccountContext().getUsername());
    	    			
    	    			p.setCategory(category);
    	    			p.setCategoryId(category.getCategoryId());
    	    			
    	    			productLists.add(p);
    	    		}
    	    		productListRepository.save(productLists);
    	    		model.addAttribute("products", productLists);
    	    		return "domain/common/batch/updateproduct";
    			}
    		}
    	}
    	return null;
    }
    
    /**
     * 批量绑定
     * @param productListSearchForm
     * @param model
     * @param folderId
     * @param products
     * @return
     */
    @RequestMapping(value = {"bindfolder", ""})
    public String bindfolder(@ModelAttribute ProductListSearchForm productListSearchForm, Model model,
    		@RequestParam(value = "folderId", required = true) Long folderId,
    		@RequestParam(value = "products", required = false) List<String> products,
    		@RequestParam(value = "type", required = false) String type){
    	if (products != null && products.size() > 0) {
    		if (folderId != null) {
    			Folder folder = folderRepository.findByFolderId(folderId);
    			if (folder != null) {
    				    				
    				List<ProductList> product_ed = new ArrayList<ProductList>();  //变更绑定
    				List<ProductList> product_su = new ArrayList<ProductList>();  //可以绑定
    				List<ProductList> product_er = new ArrayList<ProductList>();  //匹配失败
    				List<ProductList> product_nofile = new ArrayList<ProductList>();  //产品无文件
    				
    				List<ProductSiteFolder> psfs_del = new ArrayList<ProductSiteFolder>();  //删除关联
    				List<ProductSiteFolder> psfs_save = new ArrayList<ProductSiteFolder>();  //保存关联
    				List<Folder> folders = new ArrayList<Folder>(); //频道
    				
    				Integer maxSortOrder = productRedisRepository.getFolderProductMaxSortOrder();
    				Integer sort = productSiteFolderRepository.getMaxSortOrder(folderId, maxSortOrder);
    				if (sort == null) {
    					sort = 0;
    				}
    				
    	    		for (String product : products) {
    	    			ProductList p = productListRepository.findByProductId(Long.valueOf(product));
    	    			List<ProductFile> pfs = productFileRepository.findFileByProductId(Long.valueOf(product));
    	    			if (pfs == null || pfs.size() <=0) {
    	    				product_nofile.add(p);
    	    			} else {
        	    			ProductSiteFolder psf_save = new ProductSiteFolder();
        	    			psf_save.setFolder(folder);
        	    			psf_save.setSite(folder.getSite());
        	    			psf_save.setProduct(p);
            	    		List<String> res = productService.matchVersionLevel(psf_save);
            	    		if (res != null && res.size() > 0) {
            	    			sort = sort + 1;
            	    			int s = sort;
            	    			psf_save.setSortOrder(s);
            	    			psf_save.setCreateBy(AccountContext.getAccountContext().getUsername());
            	    			psf_save.setCreateDate(new Date());
            	    			product_su.add(p);
            	    			psfs_save.add(psf_save);
            	    			
            	    			if (!folders.contains(folder)) {
            	    				folders.add(folder);
            	    			}
                	    		
            	    			ProductSiteFolder psf_del = productSiteFolderRepository.findBySiteIdAndProductId(folder.getSiteId(), Long.valueOf(product));
            	    			if (psf_del != null) {
            	    				product_ed.add(p);
            	    				psfs_del.add(psf_del);
            	    				if (!folders.contains(psf_del.getFolder())) {
            	    					folders.add(psf_del.getFolder());
            	    				}
            	    			}
            	    			
            	    		} else {
            	    			product_er.add(p);
            	    		}
            	    	}
    	    		}
    	    		
    	    		model.addAttribute("product_ed", product_ed);
    	    		model.addAttribute("product_su", product_su);
    	    		model.addAttribute("product_er", product_er);
    	    		model.addAttribute("product_nofile", product_nofile);
    	    		
    	    		model.addAttribute("psfs_del", psfs_del);
    	    		
    	    		if (StringUtils.isNotEmpty(type) && "save".equals(type)) {
    	    			model.addAttribute("folders", folders);
    	    			productService.bindFolderProducts(psfs_del, psfs_save);
    	    			return "domain/common/batch/bindfolder";
    	    		} else {
    	    			return "domain/common/batch/checkbindfolder";
    	    		}
    			}
    		}
    	}
    	return null;
    }
    
    @RequestMapping(value = {"redis/updateproducts", ""})
    public String updateRedisProducts(@ModelAttribute ProductListSearchForm productListSearchForm, Model model,
    		@RequestParam(value = "products", required = false) List<String> products){
    	if (products != null && products.size() > 0) {
        	List<Product> ps = new ArrayList<Product>();
        	List<Folder> fs = new ArrayList<Folder>();
    		for(String pid : products) {
    			ProductList productList = productListRepository.findByProductId(Long.valueOf(pid));
    			Product pro = productService.setProduct2Redis1(productList);
    			if (pro != null) {
    				ps.add(pro);
    			}
    			if (ps.size() >= 1000) {
    				productService.setProducts2Search(ps);
    				ps.clear();
    			}
    			if (productList.getFolders() != null) {
        			for (Folder f : productList.getFolders()) {
            			if (!fs.contains(f)) {
            				fs.add(f);
            			}
        			}
    			}
    		}
    		productService.setProducts2Search(ps);
    		model.addAttribute("productListsCount", products.size());
    		model.addAttribute("folders", fs);
    	}		
		return "domain/common/batch/updatelist";
    }
    
    @RequestMapping(value = {"redis/updatelist", ""})
    public String updateRedisProductList(@ModelAttribute ProductListSearchForm productListSearchForm, Model model,
    		@RequestParam(value = "folders", required = false) List<String> folders){
    	if (folders != null && folders.size() > 0) {
    		for (String fid : folders) {
        		Folder folder = folderRepository.findByFolderId(Long.valueOf(fid));
        		productRedisRepository.deleteSiteFolderKeys(folder.getSiteId(), folder.getFolderId());
        		logger.info("[batch] delete folder[" + fid + "] list keys complete !");
        		
        		List<ProductSiteFolder> list = productSiteFolderRepository.findByFolderId(Long.valueOf(fid));
        		if(null != list) {
        			for(ProductSiteFolder psf : list) {
        				this.productService.redisAddFolderProduct(psf);
        			}
        			logger.info("[batch] update folder[" + fid +"] product list publish finshed!Total :" + list.size() + "!");
        		}
    		}
    	}		
		return "domain/common/batch/success";
    }
    
    @RequestMapping(value = {"insertrecommend", ""})
    public String insertrecommend(@ModelAttribute ProductListSearchForm productListSearchForm, Model model,
    		@RequestParam(value = "products", required = false) List<String> products){
    	if (products != null && products.size() > 0) {
			Date date = new Date();
    		List<ProductList> productLists = new ArrayList<ProductList>();
    		for (String product : products) {
    			ProductList p = productListRepository.findByProductId(Long.valueOf(product));
    			p.setModDate(date);
    			p.setModBy(AccountContext.getAccountContext().getUsername());
    			p.setRecommendStorage(1);
    			productLists.add(p);
    		}
    		productListRepository.save(productLists);
    		model.addAttribute("products", productLists);
    	}
		return "domain/common/batch/success";
    }
    
    @RequestMapping(value = {"removerecommend", ""})
    public String removerecommend(@ModelAttribute ProductListSearchForm productListSearchForm, Model model,
    		@RequestParam(value = "products", required = false) List<String> products){
    	if (products != null && products.size() > 0) {
			Date date = new Date();
    		List<ProductList> productLists = new ArrayList<ProductList>();
    		for (String product : products) {
    			ProductList p = productListRepository.findByProductId(Long.valueOf(product));
    			p.setModDate(date);
    			p.setModBy(AccountContext.getAccountContext().getUsername());
    			p.setRecommendStorage(0);
    			productLists.add(p);
    		}
    		productListRepository.save(productLists);
    		model.addAttribute("products", productLists);
    	}
		return "domain/common/batch/success";
    }

}
