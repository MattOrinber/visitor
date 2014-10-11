/*


 * Template pack-mvc-3:src/main/java/web/controller/controller.e.vm.java
 */
package org.visitor.appportal.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.visitor.appportal.domain.*;
import org.visitor.appportal.repository.LabelRepository;
import org.visitor.appportal.web.utils.SiteUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import org.visitor.appportal.context.AccountContext;
import org.visitor.appportal.domain.ProductDetail.RecommendStorageEnum;
import org.visitor.appportal.repository.CategoryRepository;
import org.visitor.appportal.repository.ProductListRepository;
import org.visitor.appportal.repository.SiteRepository;
import org.visitor.appportal.repository.base.OrderByDirection;
import org.visitor.appportal.repository.base.SearchMode;
import org.visitor.appportal.repository.base.SearchTemplate;
import org.visitor.appportal.service.CategoryService;
import org.visitor.appportal.service.ProductService;
import org.visitor.appportal.service.SiteFolderService;
import org.visitor.appportal.service.SystemPreference;

@Controller
@SessionAttributes("productListSearchForm")
@RequestMapping("/domain/productlist/")
public class ProductListController {
	@Autowired
	private ProductListRepository productListRepository;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private SystemPreference systemPreference;
	@Autowired
	private ProductService productService;
	@Autowired
	private SiteRepository siteRepository;
	@Autowired
	private SiteFolderService siteFolderService;
    @Autowired
    private LabelRepository labelRepository;
	
    @InitBinder
    public void initDataBinder(WebDataBinder binder){
        // you can explicitly allow certain fields or exclude them
        // binder.setAllowedFields(new String[]{"resourceAudio.resourceid", "resourceAudio.resourceName", "resourceAudio.userid", "resourceAudio.status", "resourceAudio.auditStatus", "resourceAudio.uploadBy", "resourceAudio.lastoperBy", "resourceAudio.createDt", "resourceAudio.lastoperDt", "resourceAudio.dlCount", "resourceAudio.author", "resourceAudio.audioProperty", "resourceAudio.recordCorp", "resourceAudio.version", "resourceAudio.language", "resourceAudio.region", "resourceAudio.rhythm", "resourceAudio.musicType", "resourceAudio.musicSuit", "resourceAudio.singer", "resourceAudio.lyricsAuthor", "resourceAudio.albumName", "resourceAudio.wayOperate", "resourceAudio.resourceSize", "resourceAudio.resourceFormat", "resourceAudio.audioSource", "resourceAudio.intro", "resourceAudio.tag", "resourceAudio.ifIndexed", "resourceAudio.fileCount", "resourceAudio.resourceStatus"});
        binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }

    @RequestMapping(value = { "search", "" })
    public String search(Model model) {
    	model.addAttribute("safeTypeList", Arrays.asList(ProductList.SafeTypeEnum.values()));
    	model.addAttribute("productTypeList", Arrays.asList(ProductList.ProductTypeEnum.values()));
    	
    	if(!model.containsAttribute("productEntitySearchForm")) {
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
        
		model.addAttribute("siteList",this.siteRepository.findByStatus(Site.StatusEnum.Enable.ordinal()));
		
		return "domain/productlist/search";
    }

	/**
	 * Performs the list action.
	 */
	@RequestMapping(value = { "list" })
	public String list(@ModelAttribute ProductListSearchForm productListSearchForm, Model model) {
		ProductList example = productListSearchForm.getProductList();
		if (example.getProductId() != null) {
			final ProductList o = productListRepository.findOne(productListSearchForm.getProductList().getPrimaryKey());
			model.addAttribute("productListsCount", o != null ? 1 : 0);
			List<ProductList> list = new ArrayList<ProductList>();
			list.add(o);
			model.addAttribute("productLists", list);
		}else if(productListSearchForm.getProductSiteFolder() != null &&
				productListSearchForm.getProductSiteFolder().getPrimaryKey() != null &&
				productListSearchForm.getProductSiteFolder().getPrimaryKey().getSiteId() != null) {
			Integer siteId = productListSearchForm.getProductSiteFolder().getPrimaryKey().getSiteId();
			Long count = siteFolderService.countSiteProducts(siteId, productListSearchForm.getProductList(), productListSearchForm.getDateRanges());
			model.addAttribute("productListsCount", count.intValue());
			if(count > 0) {
				model.addAttribute("productLists", siteFolderService.findSiteProducts(siteId, productListSearchForm.getProductList(), productListSearchForm.getDateRanges(), productListSearchForm.getPageable()));
			}
		}else if(productListSearchForm.getBind() != null) {
			Integer bind = productListSearchForm.getBind();
			Long count = siteFolderService.countBindProducts(bind, productListSearchForm.getProductList(), productListSearchForm.getDateRanges());
			model.addAttribute("productListsCount", count.intValue());
			if(count > 0) {
				model.addAttribute("productLists", siteFolderService.findBindProducts(bind, productListSearchForm.getProductList(), productListSearchForm.getDateRanges(), productListSearchForm.getPageable()));
			}
		}else {
			SearchTemplate search = productListSearchForm.toSearchTemplate();
			search.setSearchMode(SearchMode.ANYWHERE);
			search.addOrderBy("modDate", OrderByDirection.DESC);
				
			int count = productListRepository.findCount(productListSearchForm.getProductList(), search);
			model.addAttribute("productListsCount", count);
			if(count > 0) {
				model.addAttribute("productLists", productListRepository.find(productListSearchForm.getProductList(), search));
			}
		}
		return "domain/productlist/list";
	}

	/**
	 * Serves the create form.
	 */
	@RequestMapping(value = "create", method = GET)
	public String create(@ModelAttribute ProductList productList, Model model) {
		model.addAttribute("safeTypeList", Arrays.asList(ProductList.SafeTypeEnum.values()));
		model.addAttribute("productTypeList", Arrays.asList(ProductList.ProductTypeEnum.values()));
		if (productList.getStarLevel() == null) {
			productList.setStarLevel("3");
		}
		model.addAttribute("productCategoryList", categoryService.findCategorySelectTree(Category.PRODUCT_CATEGORY));
		model.addAttribute("billingTypeList", categoryService.findCategoryChild(Category.BILLING_TYPE, null));
		model.addAttribute("operationList", categoryService.findCategoryChild(Category.OPERATION_MODEL, null));
		model.addAttribute("cooperationList", categoryService.findCategoryChild(Category.COOPERATION, null));
		model.addAttribute("operatorList", categoryService.findCategoryChild(Category.OPERATOR, null));
		model.addAttribute("importByList", categoryService.findCategoryChild(Category.IMPORTBY, null));
		model.addAttribute("productSourceList", categoryService.findCategoryChild(Category.PRODUCTSOURCE, null));
		model.addAttribute("merchantList", categoryService.findCategoryChild(Category.MERCHANT, null));
		return "domain/productlist/create";
	}

	/**
	 * Performs the create action and redirect to the show view.
	 */
	@RequestMapping(value = "create", method = { POST, PUT })
	public String create(@Valid @ModelAttribute ProductList productList, BindingResult bindingResult, 
			@RequestParam("cover") MultipartFile cover, @RequestParam("icon") MultipartFile icon, 
			Model model, HttpServletRequest request) {

		if (bindingResult.hasErrors()) {
			return create(productList, model);
		}
		
		productList.setRecommendStorage(RecommendStorageEnum.NO.ordinal());
		
		if (productList.getPrice() < 0) {
			bindingResult.addError(new FieldError("prodcutList", "price", "价格不能为负数"));
			return create(productList, model);
		}
		
		if (productList.getCategoryId() != null) {
			productList.setCategory(categoryRepository.findOne(productList.getCategoryId()));
		}
		
		if (productList.getBillingTypeId() != null) {
			productList.setBillingType(categoryRepository.findOne(productList.getBillingTypeId()));
		}
		
		if (productList.getOperationModelId() != null) {
			productList.setOperationModel(categoryRepository.findOne(productList.getOperationModelId()));
		}
		
		if (productList.getCooperationModelId() != null) {
			productList.setCooperationModel(categoryRepository.findOne(productList.getCooperationModelId()));
		}
		
		if (productList.getOperatorId() != null) {
			productList.setOperator(categoryRepository.findOne(productList.getOperatorId()));
		}
		
		if (productList.getSourceId() != null) {
			productList.setProductSource(categoryRepository.findOne(productList.getSourceId()));
		}
		
		if (productList.getMerchantId() != null) {
			productList.setMerchant(categoryRepository.findOne(productList.getMerchantId()));
		}
		
		// 保存产品
		Date date = new Date();
		productList.setCreateDate(date);
		productList.setCreateBy(AccountContext.getAccountContext().getUsername());
		productList.setModDate(date);
		productList.setModBy(AccountContext.getAccountContext().getUsername());
		productList.setSafeType(ProductList.SafeTypeEnum.Unknown.getValue());
		
		ProductPic coverPic = ProductPicController.getProductPic(cover, ProductPic.COVER);
		ProductPic iconPic = ProductPicController.getProductPic(icon, ProductPic.ICON);
		productService.saveProductWithCoverAndIcon(productList, coverPic, iconPic);
		
		// 保存图片对象
		String picRootPath = systemPreference.getProductPicRoot();
		File outputFile = new File(picRootPath, coverPic.getPicPath());
		if (!outputFile.getParentFile().exists()) {
			outputFile.getParentFile().mkdirs();
		}
		try {
			cover.transferTo(outputFile);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		outputFile = new File(picRootPath, iconPic.getPicPath());
		if (!outputFile.getParentFile().exists()) {
			outputFile.getParentFile().mkdirs();
		}
		try {
			icon.transferTo(outputFile);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "redirect:/domain/productlist/show/" + productList.getPrimaryKey();
	}

	/**
	 * Serves search by example form, search by pattern form and search by named
	 * query form.
	 */
	@RequestMapping(value = "*", method = GET)
	public void catchAll(@ModelAttribute ProductListSearchForm productListSearchForm, Model model) {
	}


    @RequestMapping(value = "/label/labelList")
    public String labelList(@ModelAttribute LabelSearchForm labelSearchForm,
                            Model model, HttpServletRequest request) {
        Label example = labelSearchForm.getLabel();
        Integer siteId = SiteUtil.getSiteFromSession(request.getSession());

        if (siteId != null) {
            if (example.getLabelId() != null) {
                final Label o = labelRepository.findByLabelId(example.getLabelId());
                model.addAttribute("LabelsCount", o != null ? 1 : 0);
                ArrayList<Label> list = new ArrayList<Label>();
                list.add(o);
                model.addAttribute("labels", list);
            } else {

                example.setSiteId(siteId);//for test
                model.addAttribute("labelsCount", labelRepository.findCount(example, labelSearchForm.toSearchTemplate()));
                model.addAttribute("labels", labelRepository.find(example, labelSearchForm.toSearchTemplate()));
            }

            return "domain/productlist/label/labelList";

        } else {

            return "redirect:/login";

        }
    }

}