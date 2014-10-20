/*

 * Template pack-mvc-3:src/main/java/web/controller/controller.e.vm.java
 */
package org.visitor.appportal.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.visitor.appportal.context.AccountContext;
import org.visitor.appportal.domain.Advertise;
import org.visitor.appportal.domain.Folder;
import org.visitor.appportal.domain.ProductContainer;
import org.visitor.appportal.domain.ProductContainerTask;
import org.visitor.appportal.domain.ProductList;
import org.visitor.appportal.domain.RecommandContainer;
import org.visitor.appportal.redis.ProductRedisRepository;
import org.visitor.appportal.repository.AdvertiseRepository;
import org.visitor.appportal.repository.FolderRepository;
import org.visitor.appportal.repository.HtmlPageRepository;
import org.visitor.appportal.repository.PageContainerRepository;
import org.visitor.appportal.repository.PictureRepository;
import org.visitor.appportal.repository.ProductContainerRepository;
import org.visitor.appportal.repository.ProductContainerTaskRepository;
import org.visitor.appportal.repository.ProductListRepository;
import org.visitor.appportal.repository.RecommandContainerRepository;
import org.visitor.appportal.repository.SiteRepository;
import org.visitor.appportal.service.site.RecommandService;
import org.visitor.appportal.service.site.SiteService.ResultEnum;
import org.visitor.appportal.web.controller.common.BaseController;
import org.visitor.appportal.web.utils.SiteUtil;

@Controller
@RequestMapping("/domain/recommandcontainer/")
public class RecommandContainerController extends BaseController{

	@Autowired
    private SiteRepository siteRepository;
	@Autowired
	private PictureRepository pictureRepository;
	@Autowired
    private FolderRepository folderRepository;
    @Autowired
    private RecommandContainerRepository recommandContainerRepository;
    @Autowired
    private ProductListRepository productListRepository;
	@Autowired
	private ProductContainerRepository productContainerRepository;
	@Autowired
	private ProductContainerTaskRepository productContainerTaskRepository;
	@Autowired
	private PageContainerRepository pageContainerRepository;
    @Autowired
    private ProductRedisRepository productRedisRepository;
    @Autowired
    private HtmlPageRepository htmlPageRepository;
    @Autowired
    private AdvertiseRepository advertiseRepository;

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
    public String list(@ModelAttribute RecommandContainerSearchForm recommandContainerSearchForm,
    		Model model,HttpServletRequest request) {
        
    	Integer siteId = SiteUtil.getSiteFromSession(request.getSession());
    	
    	if(siteId != null) {
	    	
	    	RecommandService recommandService = this.getServiceFactory().getRecommandService(siteId);
	    	
	        recommandService.list(recommandContainerSearchForm, model);
	        
	        return "domain/recommandcontainer/list";
    	
    	}else {
    		
    		return "redirect:/login";
    	}
    }
    
    /**
     * Performs the list add page action.
     */
    @RequestMapping(value = { "list1", "" })
    public String list(@ModelAttribute RecommandContainerSearchForm recommandContainerSearchForm,
    		@RequestParam(value = "pageId", required = true) Integer pageId, Model model) {
    	Page<RecommandContainer> page = recommandContainerRepository.findAll(recommandContainerSearchForm.toSpecification(),
    			recommandContainerSearchForm.getPageable());
        model.addAttribute("recommandContainersCount", page.getTotalElements());
        model.addAttribute("recommandContainers", page.getContent());
        
        model.addAttribute("pageId", pageId);
        
        return "domain/recommandcontainer/list1";
    }

    /**
     * Serves the create form.
     */
    @RequestMapping(value = "create", method = GET)
    public String create(@ModelAttribute RecommandContainer recommandContainer) {
        return "domain/recommandcontainer/create";
    }

    /**
     * Performs the create action and redirect to the show view.
     */
    @RequestMapping(value = "create", method = { POST, PUT })
    public String create(@Valid @ModelAttribute RecommandContainer recommandContainer, 
    		BindingResult bindingResult,HttpServletRequest request) {
        
    	RecommandService rs = this.getServiceFromRequest(request);
        
        if(rs != null){
        	
        	ResultEnum re = rs.create(recommandContainer, bindingResult);
        	
        	if(re == ResultEnum.OK){
        		return "redirect:/domain/recommandcontainer/show/" + recommandContainer.getPrimaryKey();
        	}
        	
        }
        	
        return "redirect:/login";
    }
    
    /**
     * Serves search by example form, search by pattern form and search by named query form.
     */
    @RequestMapping(value = "search1", method = GET)
    public String search1(@ModelAttribute RecommandContainerSearchForm recommandContainerSearchForm,
    		@RequestParam(value = "pageId", required = true) Integer pageId, Model model) {
    	model.addAttribute("pageId", pageId);
    	return "domain/recommandcontainer/search1";
    }
    
    /**
     * 指定容器添加产品，这里的产品是广义的
     * @param productContainer 产品容器关系
     * @param recommandContainerId 容器ID
     * @param htmlpageId 具体某个页面（可选）
     * @param model 模型
     * @return 返回的视图
     */
    @RequestMapping(value = "addproduct", method = GET)
    public String addproduct(@ModelAttribute ProductContainer productContainer,
    		@RequestParam(value = "containerId", required = true) Long rcId,
    		@RequestParam(value = "pageId", required = false) Long pageId,
    		Model model,HttpServletRequest request) {
    	
    	Integer siteId = SiteUtil.getSiteFromSession(request.getSession());
    	
    	if(siteId != null){
    		RecommandService recommandService = getServiceFactory().getRecommandService(siteId);
    		
    		if(recommandService != null){
    			recommandService.addproductForGet(productContainer,rcId,pageId,model);
    		}
    	}
    	
    	return "domain/recommandcontainer/addproduct";
    }
    
    /**
     * 为指定容器添加定时产品，这里的产品是广义的
     * @param recommandContainerId 容器ID
     * @param model 模型
     * @return 返回的视图
     */
    @RequestMapping(value = "addproducttask", method = GET)
    public String addproducttask(@ModelAttribute ProductContainerTask productContainerTask,
    		@RequestParam(value = "containerId", required = true) Long recommandContainerId,
    		Model model) {
    	
    	productContainerTask.setContainerId(recommandContainerId);
 
    	List<String> dates = new ArrayList<String>();
    	for (int _day = 1; _day <= 30; _day++ ){
    		Calendar cal = Calendar.getInstance();
    		cal.add(Calendar.DATE, _day);
    		SimpleDateFormat day_df = new SimpleDateFormat("yyyyMMdd");
    		dates.add(day_df.format(cal.getTime()));
    	}
    	model.addAttribute("dates", dates);
    	
    	//日期的上下限
    	String leftDate = dates.get(0);
    	leftDate = leftDate.substring(0,4)+"-"+leftDate.substring(4,6)+"-"+leftDate.substring(6);
    	model.addAttribute("leftDate", leftDate);
    	
    	leftDate = dates.get(dates.size()-1);
    	leftDate = leftDate.substring(0,4)+"-"+leftDate.substring(4,6)+"-"+leftDate.substring(6);
    	
    	model.addAttribute("rightDate",leftDate);

    	return "domain/recommandcontainer/addproducttask";
    }

    /**
     * 为指定容器添加定时产品，处理表单提交，这里的产品是广义的
     * @param recommandContainerId 容器ID
     * @param model 模型
     * @return 返回的视图
     */
    @RequestMapping(value = "addproducttask", method = {POST,PUT})
    public String addproducttask(@ModelAttribute ProductContainerTask productContainerTask,
    		BindingResult bindingResult,HttpServletRequest request,Model model) {
    	
    	Integer siteId = SiteUtil.getSiteFromSession(request.getSession());
    	Long containerId = productContainerTask.getContainerId();
    
    	if(siteId == null){
    		bindingResult.rejectValue("type", "productContainerTask.site.required", "站点信息失效，请重新登录并选择。");
    		return addproducttask(productContainerTask,containerId, model);
    	}
    	
    	RecommandContainer rc = this.recommandContainerRepository.findOne(containerId);
    	//0. 容器有效性处理
		if(rc != null && rc.getSiteId() != siteId.intValue()){
			bindingResult.rejectValue("type", "productContainer.type.invalid", 
    				"该容器不属于当前站点，请选择本站点下的容器进行操作。");
    		//直接返回
    		return addproducttask(productContainerTask,containerId, model);
		}

    	//错误处理
    	//1. 产品类型必须选择
    	Integer type = productContainerTask.getType(); 
    	if(type == null){
    		bindingResult.rejectValue("type", "productContainerTask.type.null", "请选择产品类型。");
    		//直接返回
    		return addproducttask(productContainerTask,containerId, model);
    	}
    	
    	//2. 验证产品ID的合法性
     	String product = request.getParameter("productIds");
    	product = StringUtils.trim(product);
    	product = StringUtils.replaceEach(product, new String[]{",", "，", " "}, new String[]{";",";",";"});
    	
		model.addAttribute("product", product);
    	String[] productSize = StringUtils.split(product, ";");
    	
    	List<Long> prdIds = new ArrayList<Long>();
    	
    	StringBuffer sb = new StringBuffer();
		for(String productId : productSize) {
			if(StringUtils.isNotBlank(productId) && StringUtils.isNumeric(productId)) {
				Long lngProductId = Long.valueOf(productId);
				/**排重*/
				if(!prdIds.contains(lngProductId)){
					prdIds.add(lngProductId);
				}
			} else {
				sb.append(productId).append(" ");
			}
		}
    	if(sb.length() > 0) {
    		bindingResult.rejectValue("productId", "productContainerTask.productId.invalideNumber", 
    				"产品ID[。" + sb.toString() + "] 不是数字。");
    	}

    	if (bindingResult.hasErrors()) {
    		return addproducttask(productContainerTask,containerId, model);
		} 
		
		// 验证产品是否存在（这里的产品是广义的，需要根据类型来确定）
    	sb = new StringBuffer();
    	StringBuffer sb1 = new StringBuffer();
    	
    	//以下这段代码主要是根据类型来取时间
		//根据表单的填充情况，来设置日期列表
		List<Integer> displayDateList = new ArrayList<Integer>();
		//a.取日期类型
		String dateType = request.getParameter("add_type");
		if(dateType!=null ){
			if(dateType.equals("1")){
				String singleDate = request.getParameter("singleDate");
				if(StringUtils.isNotBlank(singleDate)){
					singleDate = singleDate.replaceAll("-", "");
					displayDateList.add(Integer.valueOf(singleDate));
				}
			}else if(dateType.equals("2")){
				//多个
				String dates[] = request.getParameterValues("multipleDate");
				if(dates!=null && dates.length > 0){
					for(String mulDate:dates){
						displayDateList.add(Integer.parseInt(mulDate));
					}
				}
			}else if(dateType.equals("3")){
				//第三种，范围，这种是比较纠结的
				String fromDate = request.getParameter("fromDate");
				String toDate = request.getParameter("toDate");
				
				if(fromDate !=null && toDate !=null && fromDate.compareTo(toDate)<0){
					//I 将fromDate转化为时间
					try {
						SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
						Date parseDate =sdf.parse(fromDate);
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(parseDate);
						
						String currDate = fromDate;
						while(currDate.compareTo(toDate)<=0){
							displayDateList.add(Integer.parseInt(currDate.replaceAll("-", "")));
							
							//增加一天
							calendar.add(Calendar.DATE, 1);
							currDate = sdf.format(calendar.getTime());
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
    	
    	
    	Object object=null;
    	ProductContainerTask pcs = null;//产品是否绑定，这个需要和日期关联
    	Integer disDate = 0;
     	ProductContainer.TypeEnum pct = ProductContainer.TypeEnum.getInstance(type);
		for(Long productId : prdIds) {
			
			if(pct == ProductContainer.TypeEnum.Product){
				object = productListRepository.findByPidWithSiteId(siteId, productId);//(productId);
				for(Integer dispDate:displayDateList){
					pcs = this.productContainerTaskRepository
						.findByContainerIdAndDisplayDateAndProductId(containerId, dispDate, productId);					
					if(pcs!=null){
						disDate = dispDate;
						break;
					}
				}
			}else if(pct == ProductContainer.TypeEnum.Folder) {
				object = folderRepository.findByFolderIdAndSiteId(productId, siteId);//(productId);
				for(Integer dispDate:displayDateList){
					pcs = this.productContainerTaskRepository
						.findByContainerIdAndDisplayDateAndTfolderId(containerId, dispDate, productId);					
					if(pcs!=null){
						disDate = dispDate;
						break;
					}
				}
			}else {
				object = advertiseRepository.findByAdvertiseIdAndSiteId(productId, siteId);//(productId);
				for(Integer dispDate:displayDateList){
					pcs = this.productContainerTaskRepository
						.findByContainerIdAndDisplayDateAndAdvertiseId(containerId, dispDate, productId);					
					if(pcs!=null){
						disDate = dispDate;
						break;
					}
				}
			}
			
			if (object == null) {// 验证产品是否存在
				sb.append(productId).append(";");
			}
			
			
			if (pcs != null) {// 验证重复推荐
				sb1.append(productId).append("-"+disDate).append(";");
			}
		}
		
		if (sb.length() > 0) {// 验证产品是否存在
			bindingResult.rejectValue("productId", "productContainerTask.productId.none", 
					new String[]{sb.toString()}, "产品[" + sb.toString() + "] 不存在或不属于当前站点。");
    		return addproducttask(productContainerTask,containerId, model);
		}
		
		if (sb1.length() > 0) {// 验证重复推荐
			bindingResult.rejectValue("productId", "productContainerTask.productId.binded", 
					new String[]{sb1.toString()}, "产品[" + sb1.toString() + "] 已经绑定了。");
    		return addproducttask(productContainerTask,containerId, model);
		}
		
		Date date = new Date();
		productContainerTask.setCreateDate(date);
		productContainerTask.setModDate(date);
		productContainerTask.setModBy(AccountContext.getAccountContext().getAccount().getUsername());
		productContainerTask.setCreateBy(AccountContext.getAccountContext().getAccount().getUsername());
		productContainerTask.setContainer(recommandContainerRepository.findOne(productContainerTask.getContainerId()));		
		

		//保存，外循环按日期的顺序来遍历
		for (Integer dispDate:displayDateList){
			//1.查找出当天该容器下的最大的排序值
			Long maxOrderBydate = productContainerTaskRepository.findMaxOrderByDateAndContainerId(dispDate,productContainerTask.getContainerId());
			if(maxOrderBydate == null){
				maxOrderBydate = 0L;
			}
			
			maxOrderBydate += prdIds.size();
			
			for(int i=0; i< prdIds.size(); i++){
				//倒序排列
				productContainerTask.setSortOrder(maxOrderBydate-i);
				
				if(pct == ProductContainer.TypeEnum.Product){
					ProductList p = productListRepository.findOne(prdIds.get(i));					
							
					ProductContainerTask temp = productContainerTask.copy();
					temp.setProduct(p);//与内循环相关				
					temp.setDisplayDate(dispDate);//与外循环相关
					productContainerTaskRepository.save(temp);
				}else if(pct == ProductContainer.TypeEnum.Folder){
					Folder f = folderRepository.findOne(prdIds.get(i));//, searchTemplate);
						
					ProductContainerTask temp = productContainerTask.copy();
					temp.setDisplayDate(dispDate);
					temp.setTfolder(f);
					productContainerTaskRepository.save(temp);
				}else {
					Advertise ad = advertiseRepository.findOne(prdIds.get(i));
						
					ProductContainerTask temp = productContainerTask.copy();
					temp.setDisplayDate(dispDate);
					temp.setAdvertise(ad);
					productContainerTaskRepository.save(temp);
				}
			}
		}
    	
    	return "redirect:/domain/recommandcontainer/showproducttask/"+containerId;
    }
    
    /**
     * 添加产品的处理方法
     * @param productContainer 产品容器关联
     * @param bindingResult 错误信息
     * @param request 请求
     * @param model 模型
     * @return 返回视图
     */
    @RequestMapping(value = "addproduct", method = {POST,PUT})
    public String addproduct(@Valid @ModelAttribute ProductContainer pc, BindingResult bindingResult,
    		HttpServletRequest request, Model model) {
    	
    	RecommandService recommandService = this.getServiceFromRequest(request);
    	
    	if(recommandService != null){
    		
         	String product = request.getParameter("productIds");

    		ResultEnum re = recommandService.addproductForPost(pc,bindingResult,model,product);
	    	
    		if(re == ResultEnum.ERROR){
    			return addproduct(pc,pc.getContainerId(), pc.getPageId() , model,request);
    		}
    		
    		return "redirect:/domain/recommandcontainer/show/" + pc.getContainer().getPrimaryKey();
    
    	}
    	
    	return "redirect:/login";
    } 
    /**
     * Serves delete product.
     */
    @RequestMapping(value = "delproduct/{productContainerId}", method = {GET})
    public String del(@PathVariable("productContainerId") Long productContainerId, Model model) {
    	model.addAttribute("productContainer", productContainerRepository.findOne(productContainerId));
    	return "domain/recommandcontainer/delproduct";
    }

    /**
     * Serves delete product.
     */
    @RequestMapping(value = "delproduct/{productContainerId}", method = {PUT, POST, DELETE})
    public String del(@PathVariable("productContainerId") Long productContainerId) {
    	ProductContainer productContainer = productContainerRepository.findOne(productContainerId);
    	productContainerRepository.delete(productContainer);
    	productRedisRepository.deleteProductRecommand(productContainer);
    	return "redirect:/domain/recommandcontainer/show/" + productContainer.getContainer().getPrimaryKey();
    }
    
	@RequestMapping(value = "/sort", method = GET)
	@ResponseBody
	public Boolean productSort(@RequestParam(value = "productContainers", required = false) String productContainers) {
		if (StringUtils.isNotEmpty(productContainers)) {
			String[] productContainerIds = productContainers.split(",");
			ProductContainer pc = new ProductContainer();
			if (productContainerIds != null && productContainerIds.length > 0) {
				int size = productContainerIds.length;
				for (int i = 0; i < size ;i++) {
					Long productContainerId = Long.valueOf(productContainerIds[i]);
					pc = productContainerRepository.findOne(productContainerId);
					pc.setSortOrder((long)(size-i));
					productContainerRepository.save(pc);
				}
			}
		} else {
			return false;
		}
		return true;
	}
	
	/**
	 * 排序操作与普通容器完全一样(为什么要倒序排列？)
	 * @param productContainers
	 * @return
	 */
	@RequestMapping(value = "/sortTask", method = GET)
	@ResponseBody
	public Boolean productSortTask(@RequestParam(value = "productContainers", required = false) String productContainers) {
		if (StringUtils.isNotEmpty(productContainers)) {
			String[] productContainerIds = productContainers.split(",");
			ProductContainerTask pc = new ProductContainerTask();
			if (productContainerIds != null && productContainerIds.length > 0) {
				int size = productContainerIds.length;
				for (int i = 0; i < size ;i++) {
					Long productContainerId = Long.valueOf(productContainerIds[i]);
					pc = productContainerTaskRepository.findOne(productContainerId);
					pc.setSortOrder((long)(size-i));
					productContainerTaskRepository.save(pc);
				}
			}
		} else {
			return false;
		}
		return true;
	}

	/**
	 * 按日期添加产品
	 * @param productContainers
	 * @return
	 */
	@RequestMapping(value = "/addproducttaskwithoutdate", method = GET)
	@ResponseBody
	public Boolean addProductTaskWithoutDate(@RequestParam(value = "productIds") String productIds,
			@RequestParam(value = "onedate") Integer displayDate, @RequestParam(value = "type") Integer type,
			@RequestParam(value = "containerId") Long containerId,
			HttpSession session) {
		
		Integer siteId = SiteUtil.getSiteFromSession(session);
		if(siteId == null){
			return false;
		}
    	
    	RecommandContainer rc = this.recommandContainerRepository.findOne(containerId);
    	//0. 容器有效性处理
		if(rc != null && rc.getSiteId() != siteId.intValue()){
			return false;
		}
		
		if (StringUtils.isNotBlank(productIds) && null != displayDate && type!=null && containerId != null) {
			ProductContainerTask productContainerTask = new ProductContainerTask();
	    	productContainerTask.setContainerId(containerId);
	    	productContainerTask.setType(type);

	    	//第一块. 验证产品ID的合法性
	    	String[] productSize = StringUtils.split(StringUtils.trim(productIds), ",");
	    	
	    	List<Long> prdIds = new ArrayList<Long>();
	    	StringBuffer sb = new StringBuffer();
			for(String productId : productSize) {
				if(StringUtils.isNotBlank(productId) && StringUtils.isNumeric(productId)) {
					Long lngProductId = Long.valueOf(productId);
					if(!prdIds.contains(lngProductId)){
						prdIds.add(lngProductId);
					}
				} else {
					sb.append(productId).append(" ");
				}
			}
	    	if(sb.length() > 0) {
	    		return false;
	    	}
			
			//第二块，验证产品是否存在（这里的产品是广义的，需要根据类型来确定）
	    	Object object=null;
	    	ProductContainerTask pcs = null;//产品是否绑定，这个需要和日期关联
	     	ProductContainer.TypeEnum pct = ProductContainer.TypeEnum.getInstance(type);
			for(Long productId : prdIds) {
				
				if(pct == ProductContainer.TypeEnum.Product){
					object = productListRepository.findByPidWithSiteId(siteId, productId);
					pcs = this.productContainerTaskRepository
						.findByContainerIdAndDisplayDateAndProductId(containerId, displayDate, productId);					
				}else if(pct == ProductContainer.TypeEnum.Folder) {
					object = folderRepository.findByFolderIdAndSiteId(productId, siteId);
					pcs = this.productContainerTaskRepository
						.findByContainerIdAndDisplayDateAndTfolderId(containerId, displayDate, productId);					
				}else {
					object = advertiseRepository.findByAdvertiseIdAndSiteId(productId, siteId);//(productId);
					pcs = this.productContainerTaskRepository
						.findByContainerIdAndDisplayDateAndAdvertiseId(containerId, displayDate, productId);					
				}
				
				if (object == null || pcs != null) {// 验证重复推荐
					return false; 
				}
			}
			//验证结束
			
			//第三块，执行添加操作
			Date date = new Date();
			productContainerTask.setCreateDate(date);
			productContainerTask.setModDate(date);
			
			String username = AccountContext.getAccountContext().getAccount().getUsername();
			productContainerTask.setModBy(username);
			productContainerTask.setCreateBy(username);
			productContainerTask.setContainer(recommandContainerRepository.findOne(containerId));		
			productContainerTask.setDisplayDate(displayDate);
			//依次保存，这里要注意的是，每次保存都需要copy一份
			Long maxOrderBydate = productContainerTaskRepository.findMaxOrderByDateAndContainerId(displayDate,productContainerTask.getContainerId());
			if(maxOrderBydate == null){
				maxOrderBydate = 0L;
			}
			
			maxOrderBydate += prdIds.size();

			int i = 0;
			if(pct == ProductContainer.TypeEnum.Product){
				for(Long productId : prdIds) {
					ProductContainerTask temp = productContainerTask.copy();
					temp.setProduct(productListRepository.findOne(productId));//
					temp.setSortOrder(maxOrderBydate-i);
					productContainerTaskRepository.save(temp);
					i++;
				}
			}else if(pct == ProductContainer.TypeEnum.Folder){
				for(Long productId : prdIds) {
					ProductContainerTask temp = productContainerTask.copy();
					temp.setTfolder(folderRepository.findOne(productId));//	
					temp.setSortOrder(maxOrderBydate-i);
					productContainerTaskRepository.save(temp);
					i++;
				}
			}else {
				for(Long productId : prdIds) {
					ProductContainerTask temp = productContainerTask.copy();
					temp.setAdvertise(advertiseRepository.findOne(productId));//
					temp.setSortOrder(maxOrderBydate-i);
					productContainerTaskRepository.save(temp);
					i++;
				}
			}
			
			return true;
			
		} else {
			return false;
		}
	}

	/**
	 * 更新日期
	 * @param productContainers
	 * @return
	 */
	@RequestMapping(value = "/changeDate", method = GET)
	@ResponseBody
	public Boolean changeDates(@RequestParam(value = "oldDates") String oldDates,
			@RequestParam(value = "newDates") String newDates,
			@RequestParam(value = "prd") Long prd,
			@RequestParam(value = "type") Integer type,
			@RequestParam(value = "containerId") Long containerId) {
		if (StringUtils.isNotEmpty(oldDates) && StringUtils.isNotBlank(newDates)
				&& prd!=null && type!=null && containerId !=null) {
			
			List<Integer> deleteDates = new ArrayList<Integer>();
			List<Integer> insertDates = new ArrayList<Integer>();
			
			String oldDateArr[] = oldDates.split(",");
			String newDateArr[] = newDates.split(",");
			
			/**
			 * 最简单的方法就是将原来的日期全部删除，然后添加新日期，实际上两者有一些或者是很多相同的日期，这些是不需要删除的
			 * 也无需添加，下面做简单的处理
			 */
			
			int oInt = 0,nInt = 0,oLen = oldDateArr.length, nLen = newDateArr.length;
			while(oInt < oLen && nInt < nLen){
				int cmpRst = oldDateArr[oInt].compareTo(newDateArr[nInt]);
				if(cmpRst < 0){
					//旧日期小于新日期，则旧日期入删除队列，同时比较下一个旧日期，新日期保持不变
					deleteDates.add(Integer.parseInt(oldDateArr[oInt]));
					oInt ++ ;
				}else if(cmpRst == 0){
					//两者相等，则比较下一对
					oInt ++ ;
					nInt ++ ;
				}else {
					//旧日期大于新日期，则新日期入添加队列，同时比较下一个新日期，旧日期不变
					insertDates.add(Integer.parseInt(newDateArr[nInt]));
					nInt ++ ;
				}
			}
			
			//如果旧日期还有，则全部入删除队列，如果新日期还有，则全部入添加队列
			while(oInt < oLen){
				deleteDates.add(Integer.parseInt(oldDateArr[oInt]));
				oInt ++ ;
			}
			while(nInt < nLen){
				insertDates.add(Integer.parseInt(newDateArr[nInt]));
				nInt ++ ;				
			}
			
			//删除旧数据
			for(Integer date:deleteDates){
				ProductContainerTask pc = null;
				if(type.intValue() ==1){
					pc = productContainerTaskRepository.findByContainerIdAndDisplayDateAndProductId(containerId, date, prd);
				}else if(type.intValue() == 2){
					pc = productContainerTaskRepository.findByContainerIdAndDisplayDateAndTfolderId(containerId, date, prd);
				}else{
					pc = productContainerTaskRepository.findByContainerIdAndDisplayDateAndAdvertiseId(containerId, date, prd);					
				}
				
				this.productContainerTaskRepository.delete(pc.getProductContainerTaskId());
			}
			
			//添加新数据
			Date dates = new Date();
			
			String username = AccountContext.getAccountContext().getAccount().getUsername();
			RecommandContainer rc = (recommandContainerRepository.findOne(containerId));
			ProductList pl = null;
			Advertise ad = null;
			Folder folder = null;
			if(type == 1){
				pl = this.productListRepository.findByProductId(prd);
			}else if(type == 2){
				folder = this.folderRepository.findByFolderId(prd);
			}else {
				ad = this.advertiseRepository.findByAdvertiseId(prd);
			}
			
			for(Integer date:insertDates){
				ProductContainerTask pc = new ProductContainerTask();
				pc.setContainerId(containerId);
				pc.setType(type);
				pc.setCreateDate(dates);
				pc.setModDate(dates);
				
				pc.setCreateBy(username);
				pc.setModBy(username);
				pc.setContainer(rc);
				
				pc.setDisplayDate(date);
				
				if(type.intValue() ==1){
					pc.setProduct(pl);
				}else if(type.intValue() == 2){
					pc.setTfolder(folder);
				}else{
					pc.setAdvertise(ad);
				}

				Long maxOrderBydate = productContainerTaskRepository.findMaxOrderByDateAndContainerId(date,containerId);
				if(maxOrderBydate == null){
					maxOrderBydate = 0L;
				}

				pc.setSortOrder(maxOrderBydate+1);
					
				this.productContainerTaskRepository.save(pc);
			}
			
			return true;
		} else {
			return false;
		}
	}
	
    /**
     * Serves search by example form, search by pattern form and search by named query form.
     */
    @RequestMapping(value = "*", method = GET)
    public void catchAll(@ModelAttribute RecommandContainerSearchForm recommandContainerSearchForm) {
    }

    /**
     * 根据session中的参数获取对应的推荐
     * @param request
     * @return
     */
    private RecommandService getServiceFromRequest(HttpServletRequest request){
    	
    	Integer siteId = SiteUtil.getSiteFromSession(request.getSession());
    	
    	if(siteId != null){
        	
    		return  this.getServiceFactory().getRecommandService(siteId);
    	}
    	
    	return null;
    }    
}