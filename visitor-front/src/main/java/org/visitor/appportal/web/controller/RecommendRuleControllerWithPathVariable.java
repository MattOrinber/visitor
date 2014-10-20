/*

 * Template pack-mvc-3:src/main/java/web/controller/controllerwithPathVariable.e.vm.java
 */
package org.visitor.appportal.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.visitor.appportal.context.AccountContext;
import org.visitor.appportal.domain.ProductList;
import org.visitor.appportal.domain.RecommendRule;
import org.visitor.appportal.repository.FolderRepository;
import org.visitor.appportal.repository.ProductListRepository;
import org.visitor.appportal.repository.RecommendRuleRepository;
import org.visitor.appportal.service.ProductService;

@Controller
@RequestMapping("/domain/recommendrule/")
public class RecommendRuleControllerWithPathVariable {
	
    @Autowired
    private RecommendRuleRepository recommendRuleRepository;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private ProductListRepository productListRepository;
    
    @Autowired
    private FolderRepository folderRepository;
    
    
    /**
     * This method is invoked by Spring MVC before the handler methods.
     * <p>
     * The path variable is converted by SpringMVC to a RecommendRule via the {@link RecommendRuleFormatter}.
     * Before being passed as an argument to the handler, SpringMVC binds the attributes on the resulting model,
     * then each handler method may receive the entity, potentially modified, as an argument.
     */
    @ModelAttribute
    public RecommendRule getRecommendRule(@PathVariable("pk") Long pk) {
    	RecommendRule recommendRule = recommendRuleRepository.findOne(pk);
        return recommendRule;
    }

    /**
     * Serves the show view for the entity.
     */
    @RequestMapping("show/{pk}")
    public String show(@ModelAttribute RecommendRule recommendRule, Model model) {
    	String manualIds = recommendRule.getManualIds();
    	if (!manualIds.isEmpty()) {
    		String ids[] = manualIds.split(",");
    		if (ids != null && ids.length > 0) {
    			List<Long> pss = new ArrayList<Long>();
    			for (int j=0 ; j<ids.length; j++) {
        			pss.add(Long.valueOf(ids[j]));
    			}
    			List<ProductList> productLists = productListRepository.findByProductIds(pss);
    			model.addAttribute("productLists", productLists);
    		}
    	}
        return "domain/recommendrule/show";
    }

    /**
     * Serves the update form view.
     */
    @RequestMapping(value = "update/{pk}", method = GET)
    public String update() {
        return "domain/recommendrule/update";
    }

    /**
     * Performs the update action and redirect to the show view.
     */
    @RequestMapping(value = "update/{pk}", method = { PUT, POST })
    public String update(@Valid @ModelAttribute RecommendRule recommendRule, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return update();
        }
        
        if (recommendRule.getManualNum() != null && recommendRule.getManualNum().intValue() > 0) {
        	if (StringUtils.isEmpty(recommendRule.getManualIds().trim())) {
        		bindingResult.addError(new FieldError("recommendRule", "manualIds", "请输入固定产品ID"));
        		return update();
        	} else {
        		if (!StringUtils.isNumeric((recommendRule.getManualIds().replaceAll(",", "")))) {
                	bindingResult.addError(new FieldError("recommendRule", "manualIds", "请输入数字，多个请用逗号隔开"));
                	return update();
        		}
        		String[] m_products = recommendRule.getManualIds().split(",");
        		for (int i=0 ; i <m_products.length ; i++) {
        			Long m_pid = Long.valueOf(m_products[i]);
        			ProductList m_p = productListRepository.findByProductId(m_pid);
        			if (m_p == null) {
                    	bindingResult.addError(new FieldError("recommendRule", "manualIds", String.valueOf(m_pid) + " 产品不存在"));
                    	return update();
        			}
        		}
        	}
        }
        
        //产品
        if (recommendRule.getType() == RecommendRule.TYPE_PRODUCT.intValue()){
        	if (recommendRule.getProductId() == null) {
            	bindingResult.addError(new FieldError("recommendRule", "productId", "必须输入"));
            	return update();
        	} else {
    			recommendRule.setProduct(productListRepository.findByProductId(recommendRule.getProductId()));
        	}
        //频道
        } else {
        	if (recommendRule.getFolderId() == null) {
            	bindingResult.addError(new FieldError("recommendRule", "folderId", "必须输入"));
            	return update();
        	} else {
        		recommendRule.setFolder(folderRepository.findByFolderId(recommendRule.getFolderId()));
        	}
        }
        
		recommendRule.setDisSort(recommendRule.formatDisSort());
		recommendRule.setModDate(new Date());
		recommendRule.setModBy(AccountContext.getAccountContext().getUsername());
    	recommendRuleRepository.save(recommendRule);
        return "redirect:/domain/recommendrule/show/" + recommendRule.getPrimaryKey();
    }

    /**
     * Serves the delete form asking the user if the entity should be really deleted.
     */
    @RequestMapping(value = "delete/{pk}", method = GET)
    public String delete() {
        return "domain/recommendrule/delete";
    }
    
    /**
     * Performs the delete action and redirect to the search view.
     */
    @RequestMapping(value = "delete/{pk}", method = { PUT, POST, DELETE })
    public String delete(@ModelAttribute RecommendRule recommendRule) {
    	productService.deleteRecommendRule(recommendRule);
        return "redirect:/domain/recommendrule/search";
    }

}