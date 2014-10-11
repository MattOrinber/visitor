/*

 * Template pack-mvc-3:src/main/java/web/controller/controller.e.vm.java
 */
package org.visitor.appportal.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.visitor.appportal.service.ProductBatchProcessor;
import org.visitor.appportal.web.vo.Product4Json;

@Controller
@RequestMapping("/domain/batch/")
public class ProductBatchProcessorController {
	protected static final Logger logger = LoggerFactory.getLogger(ProductBatchProcessorController.class);
    @Autowired
    private ProductBatchProcessor productBatchProcessor;

    /**
     * Performs the list action.
     */
    @RequestMapping(value = { "search", "" })
    public String list() {
        return "domain/batch/search";
    }

    /**
     * Serves the create form.
     */
    @RequestMapping(value = "create", method = GET)
    public String create() {
        return "domain/batch/create";
    }
    
	@RequestMapping(value = "create", method = { POST, PUT })
    public String create(
    		@RequestParam(value = "path", required = false) String path, 
    		@RequestParam(value = "single", required = false) String single,
    		Model model) {
		if(StringUtils.isNotBlank(single)) {//Just load only one product
			File productFolder = new File(path);
			if(!productFolder.isDirectory() || !productFolder.exists()) {
				model.addAttribute("message", path + " 不存在或者不是目录。");
			} else {
				Product4Json product = productBatchProcessor.getProductInfor(productFolder);
				if(null != product) {
					productBatchProcessor.createProduct(product, productFolder);
				}
			}
		} else {
			File folder = new File(path, "recent");
			File[] files = folder.listFiles();
			if(null == files) {
				model.addAttribute("message", path + " 是空目录。");
			} else {
				Product4Json product = null;
				for(File file : files) {
					if(file.exists() && file.isDirectory()) {
						try {
							product = productBatchProcessor.getProductInfor(file);
							if(null != product) {
								productBatchProcessor.createProduct(product, file);
							}
						}catch (Exception e) {
							e.printStackTrace();
							logger.error("Parse product: " + file.getName() + " Error.", e);
						}
					}
				}
			}
		}
        return "redirect:/domain/batch/list";

	}
    
    /**
     * 更新单个或者多个产品。
     */
	@RequestMapping(value = "update", method = { POST, PUT })
    public String update(
    		@RequestParam(value = "path", required = false) String path, 
    		@RequestParam(value = "single", required = false) String single,
    		@RequestParam(value = "type", required = false) String type,
    		Model model) {
		if(logger.isDebugEnabled()) {
			logger.debug("Parameters: path:" + path + " single:" + single + " type:" + type);
		}
		if(StringUtils.isNotBlank(single)) {//Just load only one product
			File productFolder = new File(path);
			if(!productFolder.isDirectory() || !productFolder.exists()) {
				model.addAttribute("message", path + " 不存在或者不是目录。");
			} else {
				processSingleProduct(type, productFolder);
			}
		} else {//Update all the product list within one folder.
			processBatchProduct(path, type, model);
		}
        return "redirect:/domain/batch/list";
    }

    /**
     * 更新多个产品信息。在textarea中输入多个产品的绝对目录，并用“;”分隔
     */
	@RequestMapping(value = "updateProducts", method = { POST, PUT })
    public String updateProducts(
    		@RequestParam(value = "path", required = false) String path, 
    		@RequestParam(value = "type", required = false) String type,
    		Model model) {
		if(logger.isDebugEnabled()) {
			logger.debug("Parameters: path:" + path + " type:" + type);
		}
		if(StringUtils.isNotBlank(path)) {
			String[] arrays = StringUtils.split(StringUtils.trim(path), ";");
			if(null != arrays) {
				for(String s : arrays) {
					File productFile = new File(s);
					if(productFile.exists() && productFile.isDirectory()) {
						this.processSingleProduct(type, productFile);
					}
				}
			}
		}
		
        return "redirect:/domain/batch/list";
    }
	
	protected void processBatchProduct(String path, String type, Model model) {
		File folder = new File(path);
		File[] files = folder.listFiles();
		if(null == files) {
			model.addAttribute("message", path + " 是空目录。");
		} else {
			for(File file : files) {
				if(file.exists() && file.isDirectory()) {
					this.processSingleProduct(type, file);
					Thread.currentThread();
					try {
						Thread.sleep(512l);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	protected void processSingleProduct(String type, File productFolder) {
		try {
			Product4Json product = productBatchProcessor.getProductInfor(productFolder);
			if(null != product) {
				if(StringUtils.equalsIgnoreCase(type, "file")) {
					logger.debug("Update product file: " + product);
					productBatchProcessor.updateProductFiles(product, productFolder);
				} else if(StringUtils.equalsIgnoreCase(type, "comment")) {
					logger.debug("Update product comment: " + product);
					productBatchProcessor.updateProductComment(product, productFolder);
				} else if(StringUtils.equalsIgnoreCase(type, "pics")) {
					logger.debug("Update product pics: " + product);
					productBatchProcessor.updateProductImage(product, productFolder);
				} else if(StringUtils.equalsIgnoreCase(type, "all")) {
					logger.debug("Update product all infor: " + product);
					this.productBatchProcessor.createProduct(product, productFolder);
				} else if(StringUtils.equalsIgnoreCase(type, "folder")) {
					logger.debug("Update product folder: " + product);
					this.productBatchProcessor.updateProductFolder(product, productFolder);
				} else if(StringUtils.equalsIgnoreCase(type, "product")) {
					logger.debug("Update product info: " + product);
					this.productBatchProcessor.updateProductInfo(product, productFolder);
				}
			}
		}catch (Exception e) {
			logger.error("Parse product: " + productFolder.getName() + " Error.", e);
		}
	}
	
    /**
     * Serves search by example form, search by pattern form and search by named query form.
     */
    @RequestMapping(value = "*", method = GET)
    public void catchAll() {
    }

}