/*

 * Template pack-mvc-3:src/main/java/web/controller/controller.e.vm.java
 */
package org.visitor.appportal.web.controller.common;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.visitor.appportal.service.ProductBatchProcessor;
import org.visitor.appportal.service.SystemPreference;
import org.visitor.appportal.web.vo.Product4Json;

@Controller
@RequestMapping("/import/")
public class ProductImportController {
	
	protected static final Logger logger = LoggerFactory.getLogger(ProductImportController.class);
	
    @Autowired
    private ProductBatchProcessor productBatchProcessor;
    
	@Autowired
	private SystemPreference systemPreference;
    
	/**
	 * 取得导入日期目录
	 * @param sourceId
	 * @param type
	 * @param importDate
	 * @return dir
	 */
	private String getImportRoot(String source, Date importDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return systemPreference.getProductImportRoot() + source + "/" + sdf.format(importDate) + "/oupeng";
	}
        
    /**
     * 更新单个或者多个产品。
     */
	@RequestMapping(value = "day", method = { GET })
	@ResponseBody
    public String update(@RequestParam(value = "source", required = true) String source) {
		String path = getImportRoot(source , new Date());
		processBatchProduct(path);
		return "";
    }
	
	protected void processBatchProduct(String path) {
		File folder = new File(path);
		File[] files = folder.listFiles();
		if(null != files) {
			for(File file : files) {
				if(file.exists() && file.isDirectory()) {
					this.processSingleProduct(file);
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
	
    public void downProducts(String root){
    	root = root.replace("/json", "");
    	String path = root + "/down.log";
    	productBatchProcessor.excuteDownProducts(path);
    }

	protected void processSingleProduct(File productFolder) {
		try {
			Product4Json product = productBatchProcessor.getProductInfor(productFolder);
			if(null != product) {
				this.productBatchProcessor.createProduct(product, productFolder);
			}
		}catch (Exception e) {
			logger.error("Parse product: " + productFolder.getName() + " Error.", e);
		}
	}
}