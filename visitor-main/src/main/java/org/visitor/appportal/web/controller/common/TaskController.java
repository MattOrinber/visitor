package org.visitor.appportal.web.controller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.visitor.appportal.service.TaskService;

@Controller
@RequestMapping("/task/")
public class TaskController {
	
	@Autowired
	private TaskService taskService;
	
    @RequestMapping(value = { "publish/all/product/recommand/data/{siteId}", "" })
    public String publishProductRecommandData(@PathVariable("siteId") Integer siteId) {
    	taskService.publishProductRecommandData(siteId);
        return null;
    }
    
    @RequestMapping(value = { "publish/all/product/recommand/across/data", "" })
    public String publishProductRecommandAcrossData() {
    	taskService.publishProductRecommandAcrossData();
        return null;
    }
    
    @RequestMapping(value = { "publish/new/product/safetype/", "" })
    public String publishNewProductProductSafeType() {
    	taskService.publishNewProductProductSafeType();
        return null;
    }
    
    @RequestMapping(value = { "down/product/unsafe", "" })
    public String autoDownUnsafeProduct() {
    	taskService.autoDownUnsafeProduct();
        return null;
    }

    @RequestMapping(value = { "publish/recommand/productsbyday", "" })
    public String autoPublishRecommandProductsByDay() {
    	taskService.autoPublishRecommandProductsByDay();
        return "domain/task/index";
    }

    @RequestMapping(value = { "publish/recommand/productsbyweek", "" })
    public String autoPublishRecommandProductsByWeek() {
    	taskService.autoPublishRecommandProductsByWeek();
        return "domain/task/index";
    }
    
    @RequestMapping(value = { "account/changeAllpwd", "" })
    public String changeAllPwd() {
    	taskService.changeAllPwd();
        return "domain/task/index";
    }
    
	@RequestMapping("productContainer/{productContainerId}/{pageId}/{withProduct}")
	public String recommandContainer(
			@PathVariable("productContainerId") Long recommandContainerId,
			@PathVariable("pageId") Long pageId,
			@PathVariable("withProduct") String withProduct) {
		taskService.recommandContainer(recommandContainerId, pageId, withProduct);
		return "domain/task/index";
	}

}
