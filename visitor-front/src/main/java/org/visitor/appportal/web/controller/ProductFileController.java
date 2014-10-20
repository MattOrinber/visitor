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

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import org.visitor.appportal.context.AccountContext;
import org.visitor.appportal.domain.Category;
import org.visitor.appportal.domain.ProductFile;
import org.visitor.appportal.domain.ProductFile.VersionOperatorEnum;
import org.visitor.appportal.domain.ProductList;
import org.visitor.appportal.domain.Site;
import org.visitor.appportal.repository.CategoryRepository;
import org.visitor.appportal.repository.ProductFileRepository;
import org.visitor.appportal.repository.ProductListRepository;
import org.visitor.appportal.service.CategoryService;
import org.visitor.appportal.service.ProductService;
import org.visitor.appportal.service.SystemPreference;
import org.visitor.appportal.web.utils.AutoCompleteResult;
import org.visitor.appportal.web.utils.EncryptionUtil;
import org.visitor.util.AppStringUtils;
import org.visitor.util.SiteEnum;

@Controller
@RequestMapping("/domain/productfile/")
public class ProductFileController {
	protected static final Logger log = LoggerFactory.getLogger(ProductFileController.class);
	@Autowired
	private ProductListRepository productListRepository;
	@Autowired
	private ProductFileRepository productFileRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private SystemPreference systemPreference;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ProductService productService;

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
	public String list(@ModelAttribute ProductFileSearchForm productFileSearchForm, Model model) {
		Pageable pageable = null;
		if(productFileSearchForm.getSp().hasSortColumnKey()) {
			Sort sort = new Sort(Sort.Direction.DESC, "product.productId");
			pageable = productFileSearchForm.getPageable(sort);
		} else {
			pageable = productFileSearchForm.getPageable();
		}
		Page<ProductFile> pager = productFileRepository.findAll(productFileSearchForm.toSpecification(), pageable);
		model.addAttribute("productFilesCount", pager.getTotalElements());
		model.addAttribute("productFiles", pager.getContent());
		return "domain/productfile/list";
	}

	/**
	 * Serves the create form.
	 */
	@RequestMapping(value = "create", method = GET)
	public String create(@ModelAttribute ProductFileSearchForm productFileSearchForm,//
			@RequestParam(value = "pid", required = true) Long productId,//
			Model model) {
		model.addAttribute("safeTypeList", Arrays.asList(ProductFile.SafeTypeEnum.values()));
		model.addAttribute("productList", productListRepository.findOne(productId));
		Site site = AccountContext.getAccountContext().getSite();
		if(null != site && null != site.getSiteId() && site.getSiteId().intValue() == SiteEnum.IOS.getValue().intValue()) {
			productFileSearchForm.getProductFile().setOs(this.categoryRepository.findOne(317l));
			productFileSearchForm.getProductFile().setPlatform(this.categoryRepository.findOne(391l));
			productFileSearchForm.getProductFile().setPlatformVersion(this.categoryRepository.findOne(392l));
			productFileSearchForm.getProductFile().setResolution(this.categoryRepository.findOne(334l));
			return "domain/productfile/createios";
		}
		return "domain/productfile/create";
	}

	/**
	 * Performs the createPageGame action and redirect to the show view.
	 */
	@RequestMapping(value = "createios", method = { POST, PUT })
	public String createIos(@ModelAttribute ProductFileSearchForm productFileSearchForm, BindingResult bindingResult,
			@RequestParam(value = "pid", required = true) Long productId, 
			Model model, HttpServletRequest request) {
		ProductFile productFile = productFileSearchForm.getProductFile();

		if(StringUtils.isEmpty(StringUtils.trimToEmpty(productFile.getFileUrl()))) {
			bindingResult.rejectValue("productFile.fileUrl", "productFile.fileUrl.empty", "外部URL不能为空！");
		}
		if(StringUtils.isEmpty(StringUtils.trimToEmpty(productFile.getFileSize()))) {
			bindingResult.rejectValue("productFile.fileSize", "productFile.fileSize.empty", "文件大小不能为空！");
		}	
		
		if(bindingResult.hasErrors()) {
			return create(productFileSearchForm, productId, model);
		}
		
		Date date = new Date();
		productFile.setFileName("");//置空，只是方便调用。
		List<ProductFile> pfs = productFileRepository.findByFileNameAndFileUrl(productFile.getFileName(), productFile.getFileUrl());
				
		if (pfs != null && pfs.size() > 0) {
			String productIds = "";
			for (ProductFile pf : pfs) {
				productIds = productIds + String.valueOf(pf.getProductId()) + " ";
			}
			bindingResult.rejectValue("productFile.fileUrl","repeat name or url","产品 " + productIds + "存在相同的跳链地址");
			return create(productFileSearchForm, productId, model);
		}		
		
		productFile.setProduct(productListRepository.findOne(productId));
		productFile.setCreateDate(date);
		productFile.setModDate(date);
		productFile.setModBy(AccountContext.getAccountContext().getUsername());
		productFile.setCreateBy(AccountContext.getAccountContext().getUsername());
		productFile.setDlCount(0L);
		productFile.setStatus(ProductFile.ENABLE);
		//添加默认值
		productFile.setSafeType(ProductFile.SafeTypeEnum.Safe.getValue());
		productFile.setAutoScan(ProductFile.AUTOSCAN_CLOSE);
		
		if (productFile.getOsId() != null) {
			productFile.setOs(categoryRepository.findOne(productFile.getOsId()));
		}
		
		if (productFile.getPlatformId() != null) {
			productFile.setPlatform(categoryRepository.findOne(productFile.getPlatformId()));
		}
		
		if (productFile.getPlatformVersionId() != null) {
			productFile.setPlatformVersion(categoryRepository.findOne(productFile.getPlatformVersionId()));
		}
		
		if (productFile.getResolutionId() != null) {
			productFile.setResolution(categoryRepository.findOne(productFile.getResolutionId()));
		}
		if (StringUtils.isNotEmpty(productFile.getVersionOperator())) {
			productFile.setVersionOperator(VersionOperatorEnum.getInstance(Integer.valueOf(productFile.getVersionOperator())).getDisplayName());
		}
		
		// 验证平台版本
		if (productFile.getPlatformVersion() == null || productFile.getPlatformVersionId() == null) {
        	Order order = new Sort.Order(Direction.ASC, "sortOrder");
        	Sort sort = new Sort(order);
			List<Category> cls = categoryService.findCategoryChild(productFile.getPlatformId(), sort);
			if (!cls.isEmpty()) {
				productFile.setPlatformVersion(cls.get(0));
				productFile.setVersionOperator(VersionOperatorEnum.GE.getDisplayName());
			}
		}
		
		// 验证分辨率
		if (productFile.getResolutionId() == null) {
			productFile.setScreenSize(0l);
		}
		try {
			productFileRepository.save(productFile);
		}catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return "redirect:/domain/productlist/show/" + productFile.getProductId();
	}
	/**
	 * Performs the create action and redirect to the show view.
	 */
	@RequestMapping(value = "create", method = { POST, PUT })
	public String create(@ModelAttribute ProductFileSearchForm productFileSearchForm, //
			@RequestParam(value = "pid", required = true) Long productId, 
			@RequestParam("jarFile") MultipartFile install, @RequestParam("jadFile") MultipartFile jad, 
			Model model, HttpServletRequest request) {
		Date date = new Date();
		ProductFile productFile = productFileSearchForm.getProductFile();
		productFile.setProduct(productListRepository.findOne(productId));
		productFile.setCreateDate(date);
		productFile.setModDate(date);
		productFile.setModBy(AccountContext.getAccountContext().getUsername());
		productFile.setCreateBy(AccountContext.getAccountContext().getUsername());
		productFile.setDlCount(0L);
		productFile.setStatus(ProductFile.ENABLE);
		
		//添加默认值
		productFile.setSafeType(ProductFile.SafeTypeEnum.Unknown.getValue());
		productFile.setAutoScan(ProductFile.AUTOSCAN_OPEN);
		
		if (productFile.getOsId() != null) {
			productFile.setOs(categoryRepository.findOne(productFile.getOsId()));
		}
		
		if (productFile.getPlatformId() != null) {
			productFile.setPlatform(categoryRepository.findOne(productFile.getPlatformId()));
		}
		
		if (productFile.getPlatformVersionId() != null) {
			productFile.setPlatformVersion(categoryRepository.findOne(productFile.getPlatformVersionId()));
		}
		
		if (productFile.getResolutionId() != null) {
			productFile.setResolution(categoryRepository.findOne(productFile.getResolutionId()));
		}
		
		if (StringUtils.isNotEmpty(productFile.getVersionOperator())) {
			productFile.setVersionOperator(VersionOperatorEnum.getInstance(Integer.valueOf(productFile.getVersionOperator())).getDisplayName());
		}
		
		// 验证平台版本
		if (productFile.getPlatformVersion() == null || productFile.getPlatformVersionId() == null) {
        	Order order = new Sort.Order(Direction.ASC, "sortOrder");
        	Sort sort = new Sort(order);
			List<Category> cls = categoryService.findCategoryChild(productFile.getPlatformId(), sort);
			if (!cls.isEmpty()) {
				productFile.setPlatformVersion(cls.get(0));
				productFile.setVersionOperator(VersionOperatorEnum.GE.getDisplayName());
			}
		}
		
		// 验证分辨率
		if (productFile.getResolutionId() == null) {
			productFile.setScreenSize(0l);
		}
		
		// 复制文件信息
		ProductFile pf = null;
		if (productFileSearchForm.getFileId() != null) {
			pf = productFileRepository.findOne(productFileSearchForm.getFileId());
		}
		
		if (pf != null && pf.getFileId() != null) {//复制相同文件
			productFile.setFileName(pf.getFileName());
			productFile.setFilePath(pf.getFilePath());
			productFile.setFileSize(pf.getFileSize());
			productFile.setFileSuffix(pf.getFileSuffix());
			productFile.setJarMd5(pf.getJarMd5());
			productFile.setJadMd5(pf.getJadMd5());
		} else {
			pf = new ProductFile();
			productFile.setFileName(install.getOriginalFilename());
			productFile.setFileSize(AppStringUtils.byteCountToDisplaySize(install.getSize()));
			productFile.setFileSuffix(jad == null || jad.isEmpty() ? FilenameUtils.getExtension(install.getOriginalFilename()) : "jad");
			
			try {
				productFile.setJarMd5(EncryptionUtil.getMD5(install.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(null != jad && !jad.isEmpty()) {
				try {
					productFile.setJadMd5(EncryptionUtil.getMD5(jad.getInputStream()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		this.productService.saveProductFile(productFile);
		
		String fileRootPath = systemPreference.getProductFileRoot();
		//保存安装文件
		if (null != install && !install.isEmpty()) {
			String filePath = ProductFile.getPath(productFile.getProductId(), 
					productFile.getFileId(), install.getOriginalFilename());
			File outputFile = new File(fileRootPath, filePath);
			if (!outputFile.getParentFile().exists()) {
				outputFile.getParentFile().mkdirs();
			}
			try {
				install.transferTo(outputFile);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//保存APK信息
		try {
			this.productService.saveProductFileApkInfo(productFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//保存引导文件
		if (null != jad && !jad.isEmpty()) {
			String filePath = ProductFile.getPath(productFile.getProductId(), 
					productFile.getFileId(), jad.getOriginalFilename());
			File outputFile = new File(fileRootPath, filePath);
			if (!outputFile.getParentFile().exists()) {
				outputFile.getParentFile().mkdirs();
			}
			try {
				jad.transferTo(outputFile);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (!productFileSearchForm.getMatched().isEmpty()) {
			List<Long> resolationIds = productFileSearchForm.getMatched();
			for (int i = 0; i < resolationIds.size() ; i++) {
				ProductFile pf1 = new ProductFile();
				if(i > 0) {
					pf1 = productFile.copy();
					pf1.setFileId(null);
				} else {
					pf1 = productFile;
				}
				pf1.setResolution(categoryRepository.findOne(resolationIds.get(i)));
				productFileRepository.save(pf1);
			}
		}
		
		//由于新增了文件，需要将其所属的产品安全类型改为未知
		ProductList productList = productFile.getProduct();
		if(productList != null) {
			if (StringUtils.isNotEmpty(productFile.getVersionName())) {
				productList.setProductVersion(productFile.getVersionName());
			}
			productList.setSafeType(ProductList.SafeTypeEnum.Unknown.ordinal());
			productListRepository.save(productList);
		}
		
		return "redirect:/domain/productlist/show/" + productFile.getProductId();
	}
	
	/**
	 * Serves the createPageGame form.
	 */
	@RequestMapping(value = "createpagegame", method = GET)
	public String createPageGame(@ModelAttribute ProductFileSearchForm productFileSearchForm,//
			@RequestParam(value = "pid", required = true) Long productId,//
			Model model) {
		
		List<ProductFile> pf =  productFileRepository.findFileByProductId(productId);
		if (pf == null || pf.size() <= 0) {
			model.addAttribute("available", true);
		}
		model.addAttribute("productList", productListRepository.findOne(productId));
		return "domain/productfile/createpagegame";
	}
	
	/**
	 * Performs the createPageGame action and redirect to the show view.
	 */
	@RequestMapping(value = "createpagegame", method = { POST, PUT })
	public String createPageGame(@ModelAttribute ProductFileSearchForm productFileSearchForm, BindingResult bindingResult,
			@RequestParam(value = "pid", required = true) Long productId, 
			Model model, HttpServletRequest request) {
				
		Date date = new Date();
		ProductFile productFile = productFileSearchForm.getProductFile();
		
		List<ProductFile> pfs = productFileRepository.findByFileNameAndFileUrl(productFile.getFileName(), productFile.getFileUrl());
				
		if (pfs != null && pfs.size() > 0) {
			String productIds = "";
			for (ProductFile pf : pfs) {
				productIds = productIds + String.valueOf(pf.getProductId()) + " ";
			}
			bindingResult.rejectValue("productFile.fileName","repeat name or url","产品 " + productIds + "存在相同的跳链（名称+地址）");
			return createPageGame(productFileSearchForm, productId, model);
		}		
		
		productFile.setProduct(productListRepository.findOne(productId));
		productFile.setCreateDate(date);
		productFile.setModDate(date);
		productFile.setModBy(AccountContext.getAccountContext().getUsername());
		productFile.setCreateBy(AccountContext.getAccountContext().getUsername());
		productFile.setDlCount(0L);
		
		//添加默认值
		productFile.setSafeType(ProductFile.SafeTypeEnum.Safe.getValue());
		productFile.setAutoScan(ProductFile.AUTOSCAN_CLOSE);
		
		if (productFile.getOsId() != null) {
			productFile.setOs(categoryRepository.findOne(productFile.getOsId()));
		}
		
		if (productFile.getPlatformId() != null) {
			productFile.setPlatform(categoryRepository.findOne(productFile.getPlatformId()));
		}
		
		if (productFile.getPlatformVersionId() != null) {
			productFile.setPlatformVersion(categoryRepository.findOne(productFile.getPlatformVersionId()));
		}
		
		if (productFile.getResolutionId() != null) {
			productFile.setResolution(categoryRepository.findOne(productFile.getResolutionId()));
		}
		
		if (StringUtils.isNotEmpty(productFile.getVersionOperator())) {
			productFile.setVersionOperator(VersionOperatorEnum.getInstance(Integer.valueOf(productFile.getVersionOperator())).getDisplayName());
		}
		
		// 验证平台版本
		if (productFile.getPlatformVersion() == null || productFile.getPlatformVersionId() == null) {
        	Order order = new Sort.Order(Direction.ASC, "sortOrder");
        	Sort sort = new Sort(order);
			List<Category> cls = categoryService.findCategoryChild(productFile.getPlatformId(), sort);
			if (!cls.isEmpty()) {
				productFile.setPlatformVersion(cls.get(0));
				productFile.setVersionOperator(VersionOperatorEnum.GE.getDisplayName());
			}
		}
		
		// 验证分辨率
		if (productFile.getResolutionId() == null) {
			productFile.setScreenSize(0l);
		}
		
		// 生成跳转url
		String filePath = productService.addSoupengUrl(productFile.getFileName(), productFile.getFileUrl());	
		productFile.setFilePath(filePath);
		
		productFileRepository.save(productFile);
		
		return "redirect:/domain/productlist/show/" + productFile.getProductId();
	}
	
	@RequestMapping("/autoproductfile")
	@ResponseBody
	public List<AutoCompleteResult> autofilepath(@RequestParam(value = "term", required = false) String searchPattern,
			@RequestParam(value = "pid", required = false) Long productId){
		
		List<AutoCompleteResult> ret = new ArrayList<AutoCompleteResult>();
		for (ProductFile productFile : productFileRepository.findFileByProductId(productId)) {
			String objectPk = productFile.getFileId().toString();
			String objectLabel = productFile.getFilePath();
			AutoCompleteResult a = new AutoCompleteResult(objectPk, objectLabel);
			
			a.setLabel_1(productFile.getFileName());
			a.setLabel_2(productFile.getFileSize());
			a.setLabel_3(productFile.getFileSuffix());
			a.setLabel_4(productFile.getJarMd5());
			
			ret.add(a);
		}
		return ret;
	}

	/**
	 * Serves search by example form, search by pattern form and search by named
	 * query form.
	 */
	@RequestMapping(value = "*", method = GET)
	public void catchAll(@ModelAttribute ProductFileSearchForm productFileSearchForm, Model model) {

    	model.addAttribute("safeTypeList", Arrays.asList(ProductFile.SafeTypeEnum.values()));
		model.addAttribute("productCategoryList", categoryService.findCategorySelectTree(Category.PRODUCT_CATEGORY));
		model.addAttribute("operatorList", categoryService.findCategoryChild(Category.OPERATOR, null));
		model.addAttribute("productSourceList", categoryService.findCategoryChild(Category.PRODUCTSOURCE, null));
		model.addAttribute("productTypeList", Arrays.asList(ProductList.ProductTypeEnum.values()));
	}
}