/*

 * Template pack-mvc-3:src/main/java/web/controller/controllerwithPathVariable.e.vm.java
 */
package org.visitor.appportal.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import org.visitor.app.portal.model.FolderRank.RankTypeEnum;
import org.visitor.app.portal.model.Product;
import org.visitor.appportal.context.AccountContext;
import org.visitor.appportal.domain.Folder;
import org.visitor.appportal.domain.ProductList;
import org.visitor.appportal.domain.RecommandContainer;
import org.visitor.appportal.domain.RecommendRuleAcross;
import org.visitor.appportal.domain.RecommendRuleAcross.RecommendRuleAcrossTypeEnum;
import org.visitor.appportal.domain.Site;
import org.visitor.appportal.redis.ProductAcrossRecommendRedisRepository;
import org.visitor.appportal.redis.ProductRedisRepository;
import org.visitor.appportal.redis.SiteRedisRepository;
import org.visitor.appportal.repository.FolderRepository;
import org.visitor.appportal.repository.ProductListRepository;
import org.visitor.appportal.repository.RecommandContainerRepository;
import org.visitor.appportal.repository.RecommendRuleAcrossRepository;
import org.visitor.appportal.service.ProductService;
import org.visitor.appportal.web.vo.RecommendProduct;

@Controller
@RequestMapping("/domain/recommendruleacross/")
public class RecommendRuleAcrossControllerWithPathVariable {
	
    @Autowired
    private RecommendRuleAcrossRepository recommendRuleAcrossRepository;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private ProductListRepository productListRepository;
    
    @Autowired
    private FolderRepository folderRepository;
    
    @Autowired
    private RecommandContainerRepository recommandContainerRepository;
    
    @Autowired
    private ProductAcrossRecommendRedisRepository productAcrossRecommendRedisRepository;
    
    @Autowired
    private ProductRedisRepository productRedisRepository;
    
    @Autowired
    private SiteRedisRepository siteRedisRepository;
        
    /**
     * This method is invoked by Spring MVC before the handler methods.
     * <p>
     * The path variable is converted by SpringMVC to a RecommendRule via the {@link RecommendRuleFormatter}.
     * Before being passed as an argument to the handler, SpringMVC binds the attributes on the resulting model,
     * then each handler method may receive the entity, potentially modified, as an argument.
     */
    @ModelAttribute
    public RecommendRuleAcross getRecommendRuleAcross(@PathVariable("pk") Long pk) {
    	RecommendRuleAcross recommendRuleAcross = recommendRuleAcrossRepository.findOne(pk);
        return recommendRuleAcross;
    }

    /**
     * Serves the show view for the entity.
     */
    @RequestMapping("show/{pk}")
    public String show(@ModelAttribute RecommendRuleAcross recommendRuleAcross, Model model) {
    	String manualIds = recommendRuleAcross.getManualIds();
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
    	
    	if (recommendRuleAcross.getFolderId() != null) {
    		Folder folder = folderRepository.findByFolderId(recommendRuleAcross.getFolderId());
    		model.addAttribute("folder", folder);
    	}
    	if (recommendRuleAcross.getContainerId() != null) {
    		RecommandContainer recommandContainer = recommandContainerRepository.findOne(recommendRuleAcross.getContainerId());
    		model.addAttribute("container", recommandContainer);
    	}
        return "domain/recommendruleacross/show";
    }

    /**
     * Serves the update form view.
     */
    @RequestMapping(value = "update/{pk}", method = GET)
    public String update() {
        return "domain/recommendruleacross/update";
    }

    /**
     * Performs the update action and redirect to the show view.
     */
    @RequestMapping(value = "update/{pk}", method = { PUT, POST })
    public String update(@Valid @ModelAttribute RecommendRuleAcross recommendRuleAcross, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return update();
        }
        
        if (recommendRuleAcross.getManualNum() != null && recommendRuleAcross.getManualNum().intValue() > 0) {
        	if (StringUtils.isEmpty(recommendRuleAcross.getManualIds().trim())) {
        		bindingResult.addError(new FieldError("recommendRuleAcross", "manualIds", "请输入固定产品ID"));
        		return update();
        	} else {
        		if (!StringUtils.isNumeric((recommendRuleAcross.getManualIds().replaceAll(",", "")))) {
                	bindingResult.addError(new FieldError("recommendRuleAcross", "manualIds", "请输入数字，多个请用逗号隔开"));
                	return update();
        		}
        		String[] m_products = recommendRuleAcross.getManualIds().split(",");
        		for (int i=0 ; i <m_products.length ; i++) {
        			Long m_pid = Long.valueOf(m_products[i]);
        			ProductList m_p = productListRepository.findByProductId(m_pid);
        			if (m_p == null) {
                    	bindingResult.addError(new FieldError("recommendRuleAcross", "manualIds", String.valueOf(m_pid) + " 产品不存在"));
                    	return update();
        			}
        		}
        	}
        }
        
        //校验频道ID
        if (recommendRuleAcross.getFolderNum() != null && recommendRuleAcross.getFolderNum().intValue() > 0) {
        	if (recommendRuleAcross.getFolderId() == null) {
        		bindingResult.addError(new FieldError("recommendRuleAcross", "folderId", "请输入频道ID"));
        		return update();
        	} else if (!StringUtils.isNumeric(recommendRuleAcross.getFolderId().toString())) {
        		bindingResult.addError(new FieldError("recommendRuleAcross", "folderId", "请输入正确的频道ID"));
        		return update();
        	} else {
        		Folder f = folderRepository.findByFolderId(recommendRuleAcross.getFolderId());
        		if (f == null) {
                	bindingResult.addError(new FieldError("recommendRuleAcross", "folderId", "频道不存在"));
                	return update();
        		}
        	}
        }
        
        //校验容器ID
        if (recommendRuleAcross.getContainerNum() != null && recommendRuleAcross.getContainerNum().intValue() > 0) {
        	if (recommendRuleAcross.getContainerId() == null) {
        		bindingResult.addError(new FieldError("recommendRuleAcross", "containerId", "请输入容器ID"));
        		return update();
        	} else {
        		RecommandContainer r = recommandContainerRepository.findOne(recommendRuleAcross.getContainerId());
        		if (r == null) {
                	bindingResult.addError(new FieldError("recommendRuleAcross", "containerId", "容器不存在"));
                	return update();
        		}
        	}
        }
        
        //产品
        if (recommendRuleAcross.getType() == RecommendRuleAcross.TYPE_PRODUCT.intValue()){
        	if (recommendRuleAcross.getServiceProductId() == null) {
            	bindingResult.addError(new FieldError("recommendRuleAcross", "serviceProductId", "必须输入"));
            	return update();
        	}
        //频道
        } else if(recommendRuleAcross.getType() == RecommendRuleAcross.TYPE_FOLDER.intValue()) {
        	if (recommendRuleAcross.getServiceFolderId() == null) {
            	bindingResult.addError(new FieldError("recommendRuleAcross", "serviceFolderId", "必须输入"));
            	return update();
        	}
        }
        
		recommendRuleAcross.setDisSort(recommendRuleAcross.formatDisSort());
		recommendRuleAcross.setModDate(new Date());
		recommendRuleAcross.setModBy(AccountContext.getAccountContext().getUsername());
    	recommendRuleAcrossRepository.save(recommendRuleAcross);
        return "redirect:/domain/recommendruleacross/show/" + recommendRuleAcross.getPrimaryKey();
    }

    /**
     * Serves the delete form asking the user if the entity should be really deleted.
     */
    @RequestMapping(value = "delete/{pk}", method = GET)
    public String delete() {
        return "domain/recommendruleacross/delete";
    }
    
    /**
     * Performs the delete action and redirect to the search view.
     */
    @RequestMapping(value = "delete/{pk}", method = { PUT, POST, DELETE })
    public String delete(@ModelAttribute RecommendRuleAcross recommendRuleAcross) {
    	productService.deleteRecommendRuleAcross(recommendRuleAcross);
        return "redirect:/domain/recommendruleacross/search";
    }
    
    @RequestMapping(value = "preview/{pk}", method = { GET })
    public String preview(@ModelAttribute RecommendRuleAcross recommendRuleAcross, Model model) {
    	List<RecommendProduct> rps = new ArrayList<RecommendProduct>();
    	if (StringUtils.isNotEmpty(recommendRuleAcross.getDisSort())) {
    		char[] disName = recommendRuleAcross.getDisSort().toCharArray();
        	for (int i=0;i< disName.length;i++) {
        		RecommendProduct rp = new RecommendProduct();
        		rp.setName(RecommendRuleAcrossTypeEnum.getInstance(String.valueOf(disName[i])).getDisplayName());
        		rp.setProductLists(getProductListsByDisName(String.valueOf(disName[i]), recommendRuleAcross));
        		
        		rps.add(rp);
        	}
    		model.addAttribute("recommendProducts", rps);
    	}   	
    	
    	return "domain/recommendruleacross/preview";
    }
    
    private List<ProductList> getProductListsByDisName(String disName, RecommendRuleAcross recommendRuleAcross) {
    	Set<Long> pids = new HashSet<Long>();
    	List<ProductList> productLists = new ArrayList<ProductList>();
    	//A
    	if (disName.equals(RecommendRuleAcrossTypeEnum.Behavior.getSuffix())) {
    		pids.addAll(productAcrossRecommendRedisRepository.getProductAcrossRecommendIds(
    				recommendRuleAcross.getServiceProductId(),
    				recommendRuleAcross.getServiceId(),
    				recommendRuleAcross.getServiceSiteId(),
    				null, recommendRuleAcross.getBehaviorNum()));
    		
    	//B
    	} else if (disName.equals(RecommendRuleAcrossTypeEnum.Storage.getSuffix())) {
    		Set<Long> pids_ = new HashSet<Long>();
    		List<Site> sites = siteRedisRepository.getSiteList();
    		for (Site site : sites) {
    			List<Long> ll = productRedisRepository.getProductRecommendStorage(site.getSiteId(), recommendRuleAcross.getStorageNum());
    			if (ll != null && ll.size() > 0) {
    				pids_.addAll(ll);
    			}
    		}
    		if (pids_.size() > 0 && pids_.size() >= recommendRuleAcross.getStorageNum()) {
    			for (Long pid : pids_) {
    				pids.add(pid);
    				if(pids.size() >= recommendRuleAcross.getStorageNum()) break;
    			}        		
    		}
    	//C
    	} else if (disName.equals(RecommendRuleAcrossTypeEnum.Manual.getSuffix())) {
    		String[] ids = recommendRuleAcross.getManualIds().split(",");
    		int count = Math.min(ids.length, recommendRuleAcross.getManualNum());
    		List<Long> idss = new ArrayList<Long>();
    		for(int i=0;i<ids.length;i++) {
    			idss.add(Long.valueOf(ids[i]));
    		}
    		pids.addAll(productService.randomProductids(idss, count));
    	//D
    	} else if (disName.equals(RecommendRuleAcrossTypeEnum.Folder.getSuffix())) {
    		Folder folder = folderRepository.findByFolderId(recommendRuleAcross.getFolderId());
    		List<Product> list = productRedisRepository.getProductFolderRankByRandom(folder.getSiteId(),
    				recommendRuleAcross.getFolderId(), RankTypeEnum.DailyDownload, 288l, 3, recommendRuleAcross.getFolderNum());
    		if (list != null && list.size() > 0) {
        		for(Product p : list) {
        			pids.add(p.getProductList().getProductId());
        		}
    		}
    	//E
    	} else if (disName.equals(RecommendRuleAcrossTypeEnum.Container.getSuffix())) {
    		List<Long> ids = new ArrayList<Long>();
    		Collection<? extends Long> pids__ = this.productRedisRepository.getProductIdsInContainer(
        				null, recommendRuleAcross.getContainerId(), 
        				569l, 1, recommendRuleAcross.getContainerNum());
    		if (pids__ != null) {
    			ids.addAll(pids__);
    		}
    		
    		pids.addAll(productService.randomProductids(ids, recommendRuleAcross.getContainerNum()));
    	}
    	
    	if (pids.size() > 0) {
        	for (Long pid : pids) {
        		productLists.add(productListRepository.findByProductId(pid));
        	}
    	}
    	
    	return productLists;
    }
    
    public static void main(String arg[]) {
    	char[] disName = "ABCS".toCharArray();
    	System.out.println(disName.length);
    	for (int i=0;i< disName.length;i++) {
    		System.out.println(String.valueOf(disName[i]));
    	}
    }

}