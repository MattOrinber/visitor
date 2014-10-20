/*

 * Template pack-mvc-3:src/main/java/web/controller/controllerwithPathVariable.e.vm.java
 */
package org.visitor.appportal.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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

import org.visitor.appportal.domain.Advertise;
import org.visitor.appportal.domain.Folder;
import org.visitor.appportal.domain.PageContainer;
import org.visitor.appportal.domain.PageContainer.ShowTypeEnum;
import org.visitor.appportal.domain.ProductContainer;
import org.visitor.appportal.domain.ProductContainerTask;
import org.visitor.appportal.domain.ProductList;
import org.visitor.appportal.domain.RecommandContainer;
import org.visitor.appportal.repository.PageContainerRepository;
import org.visitor.appportal.repository.PictureRepository;
import org.visitor.appportal.repository.ProductContainerRepository;
import org.visitor.appportal.repository.ProductContainerTaskRepository;
import org.visitor.appportal.repository.RecommandContainerRepository;
import org.visitor.appportal.service.ProductService;
import org.visitor.appportal.web.utils.SiteUtil;
import org.visitor.appportal.web.vo.ContainerPagesProducts;

@Controller
@RequestMapping("/domain/recommandcontainer/")
public class RecommandContainerControllerWithPathVariable {
	@Autowired
	private PictureRepository pictureRepository;
    @Autowired
    private RecommandContainerRepository recommandContainerRepository;
	@Autowired
	private ProductContainerRepository productContainerRepository;
	@Autowired
	private ProductService productService;
	@Autowired
	private ProductContainerTaskRepository productContainerTaskRepository;
    @Autowired
    private PageContainerRepository pageContainerRepository;

    /**
     * This method is invoked by Spring MVC before the handler methods.
     * <p>
     * The path variable is converted by SpringMVC to a RecommandContainer via the {@link RecommandContainerFormatter}.
     * Before being passed as an argument to the handler, SpringMVC binds the attributes on the resulting model,
     * then each handler method may receive the entity, potentially modified, as an argument.
     */
    @ModelAttribute
    public RecommandContainer getRecommandContainer(@PathVariable("pk") Long pk) {
        return recommandContainerRepository.findOne(pk);
    }

    /**
     * Serves the show view for the entity.
     * MapKey改用PageContainer
     */
    @RequestMapping("show/{pk}")
    public String show(@ModelAttribute RecommandContainer recommandContainer, Model model,
    		HttpSession session) {
    	//List<HtmlPage> pages = htmlPageRepository.findPagesByContainerId(recommandContainer.getContainerId());
    	Integer siteId = SiteUtil.getSiteFromSession(session);
    	if(recommandContainer == null || siteId == null 
    		|| siteId.intValue() != recommandContainer.getSiteId()){
    		return "redirect:/domain/recommandcontainer/search";
    	}
    	List<PageContainer> pages = pageContainerRepository.findByContainerId(recommandContainer.getContainerId());
    	
    	Map<PageContainer, List<ProductContainer>> pageContainers = new HashMap<PageContainer, List<ProductContainer>>();
    	
	    List<Order> list = new ArrayList<Order>();
	    list.add(new Order(Direction.DESC, "sortOrder"));
	    Sort sort = new Sort(list);

	    for (PageContainer page : pages) {
	    	//page.getp
			pageContainers.put(page, productContainerRepository
					.findByPageIdAndContainerId(page.getPageId(),
							recommandContainer.getContainerId(), sort));
		}
	    
    	model.addAttribute("pageProductMap", pageContainers);
    	
        return "domain/recommandcontainer/show";
    }

    /**
     * Serves the update form view.
     */
    @RequestMapping(value = "update/{pk}", method = GET)
    public String update() {
        return "domain/recommandcontainer/update";
    }

    /**
     * Performs the update action and redirect to the show view.
     */
    @RequestMapping(value = "update/{pk}", method = { PUT, POST })
    public String update(@Valid @ModelAttribute RecommandContainer recommandContainer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return update();
        }
        if(null != recommandContainer.getPicId()) {
        	recommandContainer.setPic(pictureRepository.findOne(recommandContainer.getPicId()));
        }
        recommandContainerRepository.save(recommandContainer);
        return "redirect:/domain/recommandcontainer/show/" + recommandContainer.getPrimaryKey();
    }

    /**
     * Serves the delete form asking the user if the entity should be really deleted.
     */
    @RequestMapping(value = "delete/{pk}", method = GET)
    public String delete2(@ModelAttribute RecommandContainer recommandContainer,Model model) {
    	//查找是否还有关联关系，有的话不能删除
    	List<PageContainer> pages = pageContainerRepository.findByContainerId(recommandContainer.getContainerId());
    	if(pages!=null && pages.size() > 0){
    		model.addAttribute("candelete", 0);//不可删除
    	}else {
    		List<ProductContainer> pcs = productContainerRepository.findByContainerId(recommandContainer.getContainerId());
    		if(pcs != null && pcs.size() > 0){
    			model.addAttribute("candelete", 0);//不可删除
    		}else {
    			model.addAttribute("candelete", 1);//可删除
    		}
    	}
        return "domain/recommandcontainer/delete";
    }

    /**
     * Performs the delete action and redirect to the search view.
     */
    @RequestMapping(value = "delete/{pk}", method = { PUT, POST, DELETE })
    public String delete(@ModelAttribute RecommandContainer recommandContainer) {
    	
        recommandContainerRepository.delete(recommandContainer);
        return "redirect:/domain/recommandcontainer/search";
    }
    
    /**
     * Show recommand container products.
     */
    @RequestMapping(value = "showproduct/{pk}", method = { GET })
	public String showProduct(
			@ModelAttribute RecommandContainer recommandContainer,
			@RequestParam(value = "pageId", required = true) Long pageId,
			Model model) {

		ContainerPagesProducts containerPagesProducts = productService
				.findProductsByContainerAndPage(recommandContainer.getContainerId(), pageId);
		model.addAttribute("containerPagesProducts", containerPagesProducts);
		return "domain/recommandcontainer/showproduct";
	}
    
    /**
     * 
     * show product task
     * 
     * @param recommandContainer
     * @param model
     * @return
     */
    @RequestMapping("showproducttask/{pk}")
    public String showProductTask(@ModelAttribute RecommandContainer recommandContainer,
    		@RequestParam(value = "showby",required=false) Integer showby, Model model) {
    	
    	if (showby == null) showby = 0;
    	model.addAttribute("showby", showby);
	    
    	// 按日期查看
    	if (showby.intValue() == 0){
    		
        	List<Order> list = new ArrayList<Order>();
    	    list.add(new Order(Direction.ASC, "displayDate"));
    	    list.add(new Order(Direction.DESC, "sortOrder"));
    	    Sort sort = new Sort(list);
    	    
    	    List<ProductContainerTask> productContainerTasks = productContainerTaskRepository.findByContainerId(recommandContainer.getContainerId(), sort);
    	    
    	    //组织展示MAP（key = displayDate , list）

    	    SimpleDateFormat day_df = new SimpleDateFormat("yyyyMMdd");
   		 	Calendar cal = Calendar.getInstance();
   		 	Integer today = Integer.parseInt(day_df.format(cal.getTime()));
   		 	model.addAttribute("today", today);
   		 	cal.add(Calendar.DAY_OF_MONTH, -1);
    		Integer yesteday = Integer.parseInt(day_df.format(cal.getTime()));
    		
    	    if (!productContainerTasks.isEmpty()) {
        	    Map<Integer, List<ProductContainerTask>> productContainerTaskMap = new LinkedHashMap<Integer, List<ProductContainerTask>>();
    	    	for (ProductContainerTask pct : productContainerTasks) {
        	    	List<ProductContainerTask> pcts = new ArrayList<ProductContainerTask>();
        	    	if (productContainerTaskMap.containsKey(pct.getDisplayDate()) && 
        	    			!productContainerTaskMap.get(pct.getDisplayDate()).isEmpty()) {
        	    		pcts = productContainerTaskMap.get(pct.getDisplayDate());
        	    	}
        	    	
        	    	if(pct.getDisplayDate()>=yesteday){
        	    		pcts.add(pct);
        	    		productContainerTaskMap.put(pct.getDisplayDate(), pcts);
        	    	}
       	    	}
   	    		 
    	    	model.addAttribute("productContainerTaskMap", productContainerTaskMap);
    	    	
    	    	List<PageContainer> pages = pageContainerRepository.findByContainerId(recommandContainer.getContainerId());
    	    	
    	    	if(pages!=null && pages.size() > 0){
    	    		for(PageContainer page : pages){
    	    			if(page.getShowType() == ShowTypeEnum.Auto.getValue().intValue()){
    	    				model.addAttribute("showType", 1);
    	    				break;
    	    			}
    	    		}
    	    	}    	    	
    	    }
    	    
    	//按产品
    	} else if (showby.intValue() == 1){
    		
    		List<Order> list = new ArrayList<Order>();
    	    list.add(new Order(Direction.ASC, "productId"));
    	    list.add(new Order(Direction.ASC, "displayDate"));
    	    Sort sort = new Sort(list);
    	    

      	  	//MAP（key = product , list<displayDate>）
			List<ProductContainerTask> productTasks = productContainerTaskRepository
					.findByContainerIdAndType(recommandContainer.getContainerId(),
							ProductContainerTask.TypeEnum.Product.getValue(),
							sort);
    	    if (!productTasks.isEmpty()) {
        	    Map<ProductList, List<Integer>> productTaskMap = new HashMap<ProductList, List<Integer>>();
	    	    for (ProductContainerTask pct : productTasks) {
	    	    	List<Integer> disDates = new ArrayList<Integer>();
	    	    	if (productTaskMap.containsKey(pct.getProduct()) &&
	    	    			!productTaskMap.get(pct.getProduct()).isEmpty()) {
	    	    		disDates = productTaskMap.get(pct.getProduct());
	    	    	}
	    	    	disDates.add(pct.getDisplayDate());
	    	    	productTaskMap.put(pct.getProduct(), disDates);
	    	    }
	    	    model.addAttribute("productTaskMap", productTaskMap);
    	    }

			//MAP（key = folder , list<displayDate>）
			List<ProductContainerTask> folderTasks = productContainerTaskRepository
					.findByContainerIdAndType(recommandContainer.getContainerId(),
							ProductContainerTask.TypeEnum.Folder.getValue(),
							sort);
		    if (!folderTasks.isEmpty()) {
		    	Map<Folder, List<Integer>> folderTaskMap = new HashMap<Folder, List<Integer>>();
			    for (ProductContainerTask pct : folderTasks) {
			    	List<Integer> disDates = new ArrayList<Integer>();
			    	if (folderTaskMap.containsKey(pct.getTfolder()) &&
			    			!folderTaskMap.get(pct.getTfolder()).isEmpty()) {
			    		disDates = folderTaskMap.get(pct.getTfolder());
			    	}
			    	disDates.add(pct.getDisplayDate());
			    	folderTaskMap.put(pct.getTfolder(), disDates);
			    }
			    model.addAttribute("folderTaskMap", folderTaskMap);
		    }
		    
			//MAP（key = advertise , list<displayDate>）
			List<ProductContainerTask> advertiseTasks = productContainerTaskRepository
					.findByContainerIdAndType(recommandContainer.getContainerId(),
							ProductContainerTask.TypeEnum.Advertise.getValue(),
							sort);
		    if (!advertiseTasks.isEmpty()) {
		    	Map<Advertise, List<Integer>> advertiseTaskMap = new HashMap<Advertise, List<Integer>>();
			    for (ProductContainerTask pct : advertiseTasks) {
			    	List<Integer> disDates = new ArrayList<Integer>();
			    	if (advertiseTaskMap.containsKey(pct.getAdvertise()) &&
			    			!advertiseTaskMap.get(pct.getAdvertise()).isEmpty()) {
			    		disDates = advertiseTaskMap.get(pct.getAdvertise());
			    	}
			    	disDates.add(pct.getDisplayDate());
			    	advertiseTaskMap.put(pct.getAdvertise(), disDates);
			    }
			    model.addAttribute("advertiseTaskMap", advertiseTaskMap);
		    }
		    
		    //Valid Dates
	    	List<String> dates = new ArrayList<String>();
	    	for (int _day = 1; _day <= 30; _day++ ){
	    		Calendar cal = Calendar.getInstance();
	    		cal.add(Calendar.DATE, _day);
	    		SimpleDateFormat day_df = new SimpleDateFormat("yyyyMMdd");
	    		dates.add(day_df.format(cal.getTime()));
	    	}
	    	model.addAttribute("dates", dates);
		    
    	}
    	
    	return "domain/recommandcontainer/showproducttask";
    }
    
    /**
     * 
     * show product task
     * 
     * @param recommandContainer
     * @param model
     * @return
     */
    @RequestMapping("showcurrproduct/{pk}")
    public String showcurrproduct(@ModelAttribute RecommandContainer recommandContainer,
    		@RequestParam(value = "pageId",required=false) Integer pageId,
    		Model model) {
    	
    	// 按日期查看
    		
        List<Order> list = new ArrayList<Order>();
    	list.add(new Order(Direction.DESC, "sortOrder"));
    	Sort sort = new Sort(list);
    	
    	
    	Calendar cal = Calendar.getInstance();
    	SimpleDateFormat day_df = new SimpleDateFormat("yyyyMMdd");
    	String today = day_df.format(cal.getTime());
		Integer currDate = Integer.parseInt(today);
    	List<ProductContainerTask> productContainerTasks = productContainerTaskRepository.findByContainerIdAndDisplayDate(recommandContainer.getContainerId(), currDate,sort);
    	    
    	model.addAttribute("todayDate", today);
    	model.addAttribute("pageId", pageId);
    	
    	if (!productContainerTasks.isEmpty()) {	   
    	    model.addAttribute("productContainerTasks", productContainerTasks);
    	}
    	    
    	return "domain/recommandcontainer/showcurrproduct";
    }
    
    /**
     * 
     * show product task
     * 
     * @param recommandContainer
     * @param model
     * @return
     */
    @RequestMapping("showproducttask/history/{pk}")
    public String showproducttaskhistory(@ModelAttribute RecommandContainer recommandContainer,
    		@RequestParam(value = "fromDate",required=false) String fromDate,
    		@RequestParam(value = "toDate",required=false) String toDate,
    		Model model) {
    	
    	if (StringUtils.isNotEmpty(fromDate) && StringUtils.isNotEmpty(toDate)) {
    		
    		Integer disFromDate = Integer.valueOf(fromDate.replace("-", ""));
    		Integer disToDate = Integer.valueOf(toDate.replace("-", ""));
    		List<ProductContainerTask> productContainerTasks = 
    				productContainerTaskRepository.findByContainerIdAndDisplayDateRange(recommandContainer.getContainerId(), disFromDate, disToDate);
    		
    		if (!productContainerTasks.isEmpty()) {
        	    Map<Integer, List<ProductContainerTask>> productContainerTaskMap = new LinkedHashMap<Integer, List<ProductContainerTask>>();
    	    	for (ProductContainerTask pct : productContainerTasks) {
        	    	List<ProductContainerTask> pcts = new ArrayList<ProductContainerTask>();
        	    	if (productContainerTaskMap.containsKey(pct.getDisplayDate()) && 
        	    			!productContainerTaskMap.get(pct.getDisplayDate()).isEmpty()) {
        	    		pcts = productContainerTaskMap.get(pct.getDisplayDate());
        	    	}
        	    	pcts.add(pct);
        	    	productContainerTaskMap.put(pct.getDisplayDate(), pcts);
       	    	}
   	    		 
    	    	model.addAttribute("productContainerTaskMap", productContainerTaskMap);
    	    }
    		model.addAttribute("fromDate", fromDate);
    		model.addAttribute("toDate", toDate);
    		
    	}
    	    
    	return "domain/recommandcontainer/showproducttaskhistory";
    }
    
    /**
     * 
     * 手动更新
     * 
     * @param recommandContainer
     * @param model
     * @return
     */
    @RequestMapping("replaceproduct/{pk}")
    public String replaceproduct(@ModelAttribute RecommandContainer recommandContainer,
    		@RequestParam(value = "pageId",required=false) Long pageId) {
    	
    	//1. 找到该容器当天的定时产品		
        List<Order> list = new ArrayList<Order>();
    	list.add(new Order(Direction.DESC, "sortOrder"));
    	Sort sort = new Sort(list);
    	
    	
    	Calendar cal = Calendar.getInstance();
    	SimpleDateFormat day_df = new SimpleDateFormat("yyyyMMdd");
    	String today = day_df.format(cal.getTime());
		Integer currDate = Integer.parseInt(today);
    	List<ProductContainerTask> productContainerTasks = productContainerTaskRepository.findByContainerIdAndDisplayDate(recommandContainer.getContainerId(), currDate,sort);
    	
    	
    	if(productContainerTasks != null && productContainerTasks.size() > 0){
    		//2. 开始替换过程
    		//2.1 找到原来的
    		ContainerPagesProducts cpp = productService
    				.findProductsByContainerAndPage(recommandContainer.getContainerId(), pageId);
    		List<ProductContainer> productContainers = cpp.getProductContainers();
    		
    		//2.2 删除原来的
    		for(ProductContainer pc:productContainers){
    			this.productContainerRepository.delete(pc.getProductContainerId());
    		}
    		
    		//2.3 添加新的
    		for(ProductContainerTask pct:productContainerTasks){
    			ProductContainer pc = new ProductContainer();
    			//复制已有的信息
    			pc.setContainer(pct.getContainer());
    			pc.setType(pct.getType());
    			//类型
    			if(pct.getType() == 1){
    				pc.setProduct(pct.getProduct());
    			}else if(pct.getType() == 2){
    				pc.setTfolder(pct.getTfolder());
    			}else {
    				pc.setAdvertise(pct.getAdvertise());
    			}
    			
    			pc.setStatus(pct.getStatus());
    			pc.setSortOrder(pct.getSortOrder());
    			pc.setCreateBy(pct.getCreateBy());
    			pc.setCreateDate(pct.getCreateDate());
    			pc.setModBy(pct.getModBy());
    			pc.setModDate(pct.getModDate());
    			
    			//设置站点，频道，页面等信息
    			pc.setSite(cpp.getSite());
    			pc.setFolder(cpp.getPageContainer().getFolder());
    			pc.setPage(cpp.getPageContainer().getPage());
    			
    			this.productContainerRepository.save(pc);//保存
    		}
    	}
    	
    	return "redirect:/domain/recommandcontainer/showproduct/"+recommandContainer.getPrimaryKey()+"?pageId="+pageId;
    }
 

}