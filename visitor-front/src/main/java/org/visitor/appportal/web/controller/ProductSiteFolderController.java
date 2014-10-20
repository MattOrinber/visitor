/*

 * Template pack-mvc-3:src/main/java/web/controller/controller.e.vm.java
 */
package org.visitor.appportal.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.visitor.appportal.context.AccountContext;
import org.visitor.appportal.domain.Category;
import org.visitor.appportal.domain.Folder;
import org.visitor.appportal.domain.ProductFile;
import org.visitor.appportal.domain.ProductList;
import org.visitor.appportal.domain.ProductSiteFolder;
import org.visitor.appportal.domain.ProductSiteFolderPk;
import org.visitor.appportal.domain.SiteValue;
import org.visitor.appportal.domain.SiteValue.TypeEnum;
import org.visitor.appportal.redis.ProductRedisRepository;
import org.visitor.appportal.repository.CategoryRepository;
import org.visitor.appportal.repository.FolderRepository;
import org.visitor.appportal.repository.ProductFileRepository;
import org.visitor.appportal.repository.ProductListRepository;
import org.visitor.appportal.repository.ProductSiteFolderRepository;
import org.visitor.appportal.service.ProductService;
import org.visitor.util.AppStringUtils;

@Controller
@RequestMapping("/domain/productsitefolder/")
public class ProductSiteFolderController {
	@Autowired
	private ProductListRepository productListRepository;
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductFileRepository productFileRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private FolderRepository folderRepository;
	@Autowired
	private ProductSiteFolderRepository productSiteFolderRepository;
	@Autowired
	private ProductRedisRepository productRedisRepository;

//	/**
//	 * show site tree
//	 */
//	@RequestMapping(value = "showtree")
//	public String showTree(Model model, @RequestParam(value = "pid", required = true) Long productId,//
//			HttpServletRequest request) {
//		model.addAttribute("siteTree", siteFolderService.findTreeForBind());
//		model.addAttribute("pid",productId);
//		return "domain/productsitefolder/showtree";
//	}

	/**
	 * Serves the create form.
	 */
	@RequestMapping(value = "create", method = GET)
	public String create(@ModelAttribute ProductSiteFolder productSiteFolder, Model model) {
		Long id = productSiteFolder.getProductId();
		if(null != id) {
			productSiteFolder.setProduct(this.productListRepository.findOne(id));
		}
		return "domain/productsitefolder/create";
	}

	/**
	 * Performs the create action and redirect to the show view.
	 */
	@RequestMapping(value = "create", method = { POST, PUT })
	public String create(
			@ModelAttribute ProductSiteFolder productSiteFolder, 
			BindingResult bindingResult, 
			@RequestParam("folderIds") String folderId , 
			@RequestParam(value = "sortOrders", required = false) String sortOrder, Model model) {
		model.addAttribute("folderIds", folderId);
		model.addAttribute("sortOrders", sortOrder);
		String[] folderIds = StringUtils.split(folderId, ";");
		
		// 产品文件
		List<ProductFile> pflist = productFileRepository.findFileByProductId(productSiteFolder.getProductId());
		if(null == pflist || pflist.size() == 0) {
			bindingResult.rejectValue("folderId", "productsitefolder_folderId_filesNotFound", null, "产品无产品文件。");
			return this.create(productSiteFolder, model);
		}
		String user = AccountContext.getAccountContext().getUsername();
		ProductList product = productListRepository.findOne(productSiteFolder.getProductId());

		Map<Long, Folder> map = new HashMap<Long, Folder>();
		for(int i=0;i<folderIds.length;i++) {
			long fid = 0L;
			try {
				fid = Long.valueOf(folderIds[i].trim());
			}catch (NumberFormatException e){
				System.err.println("invalid number");
			}
			Folder folder = this.folderRepository.findOne(fid);
			if(null == folder) {
				bindingResult.rejectValue("folderId", "productSiteFolder_folderId_notExist", "频道：" + folderIds[i] + "不存在");
			} else {
				//检查频道是否存在
				for(Folder f : map.values()) {
					if(f.getSiteId().intValue() == folder.getSiteId().intValue()) {
						bindingResult.rejectValue("folderId", "productSiteFolder_folderId_identicalFolderIds", 
								new Long[]{folder.getFolderId(), f.getFolderId()}, 
								"频道：" + folder.getFolderId() + "与" + f.getFolderId() + "属于同一个站点：" + folder.getSiteId());
					}
				}
				map.put(folder.getFolderId(), folder);
				//是否已经绑定到同一个站点
				ProductSiteFolderPk pk = new ProductSiteFolderPk(product.getProductId(), folder.getSiteId());
				ProductSiteFolder psf = productSiteFolderRepository.findOne(pk);
				if(null != psf) {
					bindingResult.rejectValue("folderId", "productSiteFolder_folderId_bindingExist", 
							new String[]{String.valueOf(folder.getFolderId()), String.valueOf(folder.getSiteId())}, 
							"已经绑定到频道：" + folder.getFolderId() + "所在的站点：" + folder.getSiteId());
				}
				
				//站点属性是否匹配
				List<SiteValue> sitevalues = folder.getSite().getSiteValues();
				Map<Long, List<Long>> platformVerions = new HashMap<Long, List<Long>>();
				List<Long> resolutions = new ArrayList<Long>();
				
				for (SiteValue sv : sitevalues) {
					if (sv.getType().compareTo(TypeEnum.PlatformVerion.getValue()) == 0) {
						Category version = categoryRepository.findOne(sv.getValue());
						List<Long> verions = new ArrayList<Long>();
						
						if (platformVerions.containsKey(version.getParentCategoryId())) {
							verions = platformVerions.get(version.getParentCategoryId());
						}
						verions.add(sv.getValue());
						platformVerions.put(version.getParentCategoryId(), verions);
					} else if (sv.getType().compareTo(TypeEnum.Resolution.getValue()) == 0) {
						resolutions.add(sv.getValue());
					}
				}
				
				if (platformVerions.isEmpty()) {
					bindingResult.rejectValue("folderId", "productSiteFolder_folderId_notSiteInfo", 
							new String[]{String.valueOf(folder.getSiteId()), String.valueOf(folder.getFolderId())},
							"产品与站点：" + folder.getSiteId() + "无关联属性。")	;			//站点无关联属性
				} else {
					boolean verEquals = false;
					for (ProductFile file : pflist) {
						if (platformVerions.containsKey(file.getPlatformId())
								&& checkPlatformVersion(platformVerions.get(file.getPlatformId()), file)
								&& checkResolution(resolutions, file.getScreenSize())) {
							verEquals = true;
							break;
						}
					}
					
					if(!verEquals) {
						bindingResult.rejectValue("folderId", "productSiteFolder_folderId_productFileNotMatch", 
								new String[]{String.valueOf(folder.getSiteId()), String.valueOf(folder.getFolderId())},
								"产品的平台版本与站点：" + folder.getSiteId() + "平台版本不匹配。")	;			//站点无关联属性
					}
				}				
			}
		}
		
		List<ProductSiteFolder> productSiteFolders = new ArrayList<ProductSiteFolder>();
		if(bindingResult.hasErrors()) {
			return this.create(productSiteFolder, model);
		} else {
			Integer maxSortOrder = this.productRedisRepository.getFolderProductMaxSortOrder();
			
			for(int i=0;i<folderIds.length;i++) {
				ProductSiteFolder psf = new ProductSiteFolder();
				psf.setProduct(product);
				psf.setFolder(map.get(Long.valueOf(folderIds[i].trim())));
				psf.setSite(map.get(Long.valueOf(folderIds[i].trim())).getSite());
				psf.setCreateBy(user);
				psf.setCreateDate(new Date());
				
				Integer sort = productSiteFolderRepository.getMaxSortOrder(psf.getFolderId(), maxSortOrder);
				if (sort == null) {
					sort = 0;
				}
				psf.setSortOrder(sort + 1);
				
				productSiteFolders.add(psf);
			}
		}
		
		if(null != productSiteFolders && productSiteFolders.size() > 0) {
			productSiteFolderRepository.save(productSiteFolders);
		}
		return "redirect:/domain/productlist/show/" + productSiteFolder.getProductId();
	}
	
	private boolean checkPlatformVersion(List<Long> verions, ProductFile file){
		for (Long versionId : verions) {
			Category c = categoryRepository.findOne(versionId);
			if (this.productService.validatePlatformVersion(c, file)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean checkResolution(List<Long> resolutions, Long screenSize){
		if (resolutions.isEmpty()) {
			return true;
		}
		for (Long resolutionId : resolutions) {
			Category c = categoryRepository.findOne(resolutionId);
			Long size = AppStringUtils.getScreenSize(c.getName());
			if(size.compareTo(screenSize) >= 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Serves search by example form, search by pattern form and search by named
	 * query form.
	 */
	@RequestMapping(value = "*", method = GET)
	public void catchAll(@ModelAttribute ProductSiteFolderSearchForm productSiteFolderSearchForm) {
	}
	
	@RequestMapping(value = "/bind", method = GET)
	@ResponseBody
	public Map<String, Object> folderBind(@RequestParam(value = "pid", required = false) Long productId,
			@RequestParam(value = "folderId", required = false) Long folderId) {
		
		Map<String, Object>  result= new HashMap<String, Object>();
		result.put("flag", false);
		
		Folder folder = folderRepository.findByFolderId(folderId);
		
		// 该频道所属站点是否已经绑定过
		List<ProductSiteFolder> psfs = productSiteFolderRepository.findByProductId(productId);
		if (psfs != null && psfs.size() > 0) {
			for (ProductSiteFolder ps : psfs) {
				if (ps.getSiteId() == folder.getSiteId()) {
					result.put("err", "该频道所属站点已经绑定过");
					return result;
				}
			}
		}		

		ProductList productList = productListRepository.findByProductId(productId);
		List<ProductFile> pfs = productFileRepository.findFileByProductId(productId);
		
		ProductSiteFolder productSiteFolder = new ProductSiteFolder();
		productSiteFolder.setFolder(folder);
		productSiteFolder.setSite(folder.getSite());
		productSiteFolder.setProduct(productList);
		
		// 产品文件
		if (pfs != null && pfs.size() > 0) {
			//匹配
			List<String> res = productService.matchVersionLevel(productSiteFolder);
			
			if (res == null || res.size() <= 0) {
				result.put("err", "站点与产品的平台版本不匹配");
				return result;
			}
			
		} else {
			result.put("err", "该产品没有资源文件");
			return result;
		}
		
		Integer maxSortOrder = productRedisRepository.getFolderProductMaxSortOrder();
		Integer sort = productSiteFolderRepository.getMaxSortOrder(folderId, maxSortOrder);
		if (sort == null) {
			sort = 0;
		}
		
		productSiteFolder.setSortOrder(sort + 1);
		productSiteFolder.setCreateBy(AccountContext.getAccountContext().getUsername());
		productSiteFolder.setCreateDate(new Date());
		
		productSiteFolderRepository.save(productSiteFolder);
		
		result.put("flag", true);
		return result;
	}
	
    
    /**
     * delete select product
     */
    @RequestMapping(value = {"deleteselect", ""})
    public String deleteSelect(@ModelAttribute ProductSiteFolderSearchForm productSiteFolderSearchForm,
    		Model model, @RequestParam(value = "folderId", required = true) Long folderId,
    		@RequestParam(value = "productsitefolders", required = false) List<String> productsitefolders) {
    	if (productsitefolders != null && productsitefolders.size() > 0) {
    		
    		for (String pk : productsitefolders) {
    			String[] pks = pk.split(":");
    			ProductSiteFolder psf = productSiteFolderRepository.findOne(
    					new ProductSiteFolderPk(Long.valueOf(pks[0]),Integer.valueOf(pks[1])));
    			
    			//productSiteFolders.add(psf);
    			productSiteFolderRepository.delete(psf);
    	    	productService.deleteFolderProduct(psf);
    	    	productService.setProduct2Redis(psf.getProduct());
    		}
    	} 
    	
        return "redirect:/domain/folder/product?folderId=" + folderId;
    }
}