/*

 * Template pack-mvc-3:src/main/java/web/controller/controllerwithPathVariable.e.vm.java
 */
package org.visitor.appportal.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import org.visitor.appportal.context.AccountContext;
import org.visitor.appportal.domain.Category;
import org.visitor.appportal.domain.ProductFile;
import org.visitor.appportal.domain.ProductFile.VersionOperatorEnum;
import org.visitor.appportal.domain.ProductList;
import org.visitor.appportal.repository.CategoryRepository;
import org.visitor.appportal.repository.ProductFileRepository;
import org.visitor.appportal.repository.ProductListRepository;
import org.visitor.appportal.service.CategoryService;
import org.visitor.appportal.service.ProductService;
import org.visitor.appportal.service.SystemPreference;
import org.visitor.appportal.web.utils.EncryptionUtil;
import org.visitor.util.AppStringUtils;

@Controller
@RequestMapping("/domain/productfile/")
public class ProductFileControllerWithPathVariable {
	@Autowired
	private ProductFileRepository productFileRepository;
	@Autowired
	private ProductListRepository productListRepository;
	@Autowired
	private SystemPreference systemPreference;
	@Autowired
	private ProductService productService;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private CategoryService categoryService;
	/**
	 * This method is invoked by Spring MVC before the handler methods.
	 * <p>
	 * The path variable is converted by SpringMVC to a ProductFile via the
	 * {@link ProductFileFormatter}. Before being passed as an argument to the
	 * handler, SpringMVC binds the attributes on the resulting model, then each
	 * handler method may receive the entity, potentially modified, as an
	 * argument.
	 */
	@ModelAttribute
	public ProductFile getProductFile(@PathVariable("pk") Long pk) {
		return productFileRepository.findOne(pk);
	}

	/**
	 * Serves the show view for the entity.
	 */
	@RequestMapping("show/{pk}")
	public String show(@ModelAttribute ProductFile productFile) {
		productFile.setVersionOperator(VersionOperatorEnum.getInstance(productFile.getVersionOperator()).getValue().toString());
		return "domain/productfile/show";
	}
	
	/**
	 * Serves the show page game view for the entity.
	 */
	@RequestMapping("showpagegame/{pk}")
	public String showPageGame(@ModelAttribute ProductFile productFile) {
		return "domain/productfile/showpagegame";
	}

	/**
	 * 换包页面
	 */
	@RequestMapping(value = "replace/{pk}", method = GET)
	public String replace(@ModelAttribute ProductFile productFile, Model model) {
		Long os = productFile.getOsId();
		if(null != os && os.longValue() == 317l) {//IOS的产品文件
			model.addAttribute("productList", this.productListRepository.findOne(productFile.getProductId()));
			return "domain/productfile/updateios";
		}
		return "domain/productfile/replace";
	}

	/**
	 * 换包
	 */
	@RequestMapping(value = "replace/{pk}", method = { PUT, POST })
	public String replace(@ModelAttribute ProductFile productFile, //
			@RequestParam(value = "file", required = false) Long productFileId, 
			@RequestParam("jarFile") MultipartFile install, @RequestParam("jadFile") MultipartFile jad, 
			Model model) {

		if (productFileId != null) {
			ProductFile pf = productFileRepository.findOne(productFileId);
			productFile.setFileName(pf.getFileName());
			productFile.setFilePath(pf.getFilePath());
			productFile.setFileSize(pf.getFileSize());
			productFile.setFileSuffix(pf.getFileSuffix());
			productFile.setJarMd5(pf.getJarMd5());
			productFile.setJadMd5(pf.getJadMd5());
		} else {
			productFile.setFileName(FilenameUtils.getBaseName(install.getOriginalFilename()));
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
			productFile.setFilePath(filePath);
			
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
			productFile.setFilePath(filePath);
		}

		productFile.setSafeType(ProductFile.SafeTypeEnum.Unknown.ordinal());
		productFile.setAutoScan(ProductFile.AUTOSCAN_OPEN);
		
		//换包后，需要将其对应的产品的安全类型更改为未知
		ProductList productList = productFile.getProduct();
		if(productList != null) {
			if (StringUtils.isNotEmpty(productFile.getVersionName())) {
				productList.setProductVersion(productFile.getVersionName());
			}
			productList.setSafeType(ProductList.SafeTypeEnum.Unknown.ordinal());
			productListRepository.save(productList);
		}
		
		productService.saveReplacedProductFile(productFile);
				
		return "redirect:/domain/productlist/show/" + productFile.getProductId();
	}

	/**
	 * Serves the update form view.
	 */
	@RequestMapping(value = "update/{pk}", method = GET)
	public String update(@ModelAttribute ProductFile productFile, Model model) {
		model.addAttribute("safeTypeList", Arrays.asList(ProductFile.SafeTypeEnum.values()));
		productFile.setVersionOperator(VersionOperatorEnum.getInstance(productFile.getVersionOperator()).getValue().toString());
		Long os = productFile.getOsId();
		if(null != os && os.longValue() == 317l) {//IOS的产品文件
			return "domain/productfile/updateios";
		}
		return "domain/productfile/update";
	}
	
	/**
	 * Performs the update action and redirect to the show view.
	 */
	@RequestMapping(value = "updateios/{pk}", method = { PUT, POST })
	public String updateIos(@Valid @ModelAttribute ProductFile productFile, BindingResult bindingResult, Model model) {
		if(StringUtils.isEmpty(StringUtils.trimToEmpty(productFile.getFileUrl()))) {
			bindingResult.rejectValue("fileUrl", "productFile.fileUrl.empty", "外部URL不能为空！");
		}
		if(StringUtils.isEmpty(StringUtils.trimToEmpty(productFile.getFileSize()))) {
			bindingResult.rejectValue("fileSize", "productFile.fileSize.empty", "文件大小不能为空！");
		}
		
		if (bindingResult.hasErrors()) {
			return "domain/productfile/updateios";
		}
		
		if (StringUtils.isNotEmpty(productFile.getVersionOperator())) {
			productFile.setVersionOperator(VersionOperatorEnum.getInstance(Integer.valueOf(productFile.getVersionOperator())).getDisplayName());
		}
		
		productFile.setFileName("");//只是为了调用方便，没有实际意义。
		List<ProductFile> pfs = productFileRepository.findByFileNameAndFileUrl(productFile.getFileName(), productFile.getFileUrl());
		
		if (pfs != null && pfs.size() > 0) {
			String productIds = "";
			for (ProductFile pf : pfs) {
				if (pf.getFileId().intValue() != productFile.getFileId()) {
					productIds = productIds + String.valueOf(pf.getProductId()) + " ";
				}
			}
			if (StringUtils.isNotEmpty(productIds)) {
				bindingResult.rejectValue("fileUrl","repeat name or url","产品 " + productIds + "存在相同的跳链地址");
				return update(productFile, model);
			}
		}
		productFileRepository.save(productFile);
		return "redirect:/domain/productfile/show/" + productFile.getPrimaryKey();
	}
	

	/**
	 * Performs the update action and redirect to the show view.
	 */
	@RequestMapping(value = "update/{pk}", method = { PUT, POST })
	public String update(@Valid @ModelAttribute ProductFile productFile, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			return update(productFile, model);
		}
		
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
        	Order order = new Sort.Order(Direction.ASC, "name");
        	Sort sort = new Sort(order);
        	List<Category> cls = categoryService.findCategoryChild(productFile.getPlatform().getCategoryId(), sort);
			if (!cls.isEmpty()) {
				productFile.setPlatformVersion(cls.get(0));
				productFile.setVersionOperator(VersionOperatorEnum.GE.getDisplayName());
			}			
		}
		
		if (productFile.getResolutionId() == null) {
			productFile.setScreenSize(0l);
		}
		
		productFile.setModBy(AccountContext.getAccountContext().getUsername());
		productFile.setModDate(new Date());

		productFileRepository.save(productFile);
		return "redirect:/domain/productfile/show/" + productFile.getPrimaryKey();
	}
	
	/**
	 * Serves the update form view.
	 */
	@RequestMapping(value = "updatepagegame/{pk}", method = GET)
	public String updatePageGame(@ModelAttribute ProductFile productFile, Model model) {
		List<ProductFile> pf =  productFileRepository.findFileByProductId(productFile.getProductId());
		if (pf == null || pf.size() < 0) {
			model.addAttribute("available", true);
		}
		return "domain/productfile/updatepagegame";
	}

	/**
	 * Performs the update action and redirect to the show view.
	 */
	@RequestMapping(value = "updatepagegame/{pk}", method = { PUT, POST })
	public String updatePageGame(@Valid @ModelAttribute ProductFile productFile, BindingResult bindingResult, Model model) {
		
		if (bindingResult.hasErrors()) {
			return updatePageGame(productFile, model);
		}
		List<ProductFile> pfs = productFileRepository.findByFileNameAndFileUrl(productFile.getFileName(), productFile.getFileUrl());
		
		if (pfs != null && pfs.size() > 0) {
			String productIds = "";
			for (ProductFile pf : pfs) {
				if (pf.getFileId().intValue() != productFile.getFileId()) {
					productIds = productIds + String.valueOf(pf.getProductId()) + " ";
				}
			}
			if (StringUtils.isNotEmpty(productIds)) {
				bindingResult.rejectValue("fileName","repeat name or url","产品 " + productIds + "存在相同的跳链（名称+地址）");
				return updatePageGame(productFile, model);
			}
		}
		
		String filePath = productService.updateSoupengUrl(productFile.getFilePath(), productFile.getFileName(), productFile.getFileUrl());	
		productFile.setFilePath(filePath);
		
		productFile.setModBy(AccountContext.getAccountContext().getUsername());
		productFile.setModDate(new Date());

		productFileRepository.save(productFile);
		return "redirect:/domain/productfile/showpagegame/" + productFile.getPrimaryKey();
	}

	/**
	 * Serves the delete form asking the user if the entity should be really
	 * deleted.
	 */
	@RequestMapping(value = "delete/{pk}", method = GET)
	public String delete() {
		return "domain/productfile/delete";
	}

	/**
	 * Performs the delete action and redirect to the search view.
	 */
	@RequestMapping(value = "delete/{pk}", method = { PUT, POST, DELETE })
	public String delete(@ModelAttribute ProductFile productFile) {
		String userName = AccountContext.getAccountContext().getUsername();
		this.productService.deleteProductFile(productFile, userName);
		return "redirect:/domain/productlist/show/" + productFile.getProductId();
	}
	
	/**
	 * Serves the delete form asking the user if the entity should be really
	 * deleted.
	 */
	@RequestMapping(value = "online/{pk}", method = GET)
	public String online(Model model) {
		model.addAttribute("type", "online");
		return "domain/productfile/delete";
	}

	/**
	 * Performs the delete action and redirect to the search view.
	 */
	@RequestMapping(value = "online/{pk}", method = { PUT, POST, DELETE })
	public String online(@ModelAttribute ProductFile productFile) {
		String userName = AccountContext.getAccountContext().getUsername();
		this.productService.onlineProductFile(productFile, userName);
		return "redirect:/domain/productlist/show/" + productFile.getProductId();
	}	
}