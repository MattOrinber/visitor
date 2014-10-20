/*

 * Template pack-mvc-3:src/main/java/web/controller/controller.e.vm.java
 */
package org.visitor.appportal.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.visitor.appportal.context.AccountContext;
import org.visitor.appportal.domain.ProductContainerTask;
import org.visitor.appportal.domain.RecommandContainer;
import org.visitor.appportal.repository.AdvertiseRepository;
import org.visitor.appportal.repository.FolderRepository;
import org.visitor.appportal.repository.ProductContainerTaskRepository;
import org.visitor.appportal.repository.ProductListRepository;

@Controller
@RequestMapping("/domain/productcontainertask/")
public class ProductContainerTaskController {

	@Autowired
	private ProductListRepository productListRepository;
	@Autowired
	private FolderRepository folderRepository;
	@Autowired	
	private AdvertiseRepository advertiseRepository;
	@Autowired
	private ProductContainerTaskRepository productContainerTaskRepository;
	
    @InitBinder
    public void initDataBinder(WebDataBinder binder){
        // you can explicitly allow certain fields or exclude them
        // binder.setAllowedFields(new String[]{"resourceAudio.resourceid", "resourceAudio.resourceName", "resourceAudio.userid", "resourceAudio.status", "resourceAudio.auditStatus", "resourceAudio.uploadBy", "resourceAudio.lastoperBy", "resourceAudio.createDt", "resourceAudio.lastoperDt", "resourceAudio.dlCount", "resourceAudio.author", "resourceAudio.audioProperty", "resourceAudio.recordCorp", "resourceAudio.version", "resourceAudio.language", "resourceAudio.region", "resourceAudio.rhythm", "resourceAudio.musicType", "resourceAudio.musicSuit", "resourceAudio.singer", "resourceAudio.lyricsAuthor", "resourceAudio.albumName", "resourceAudio.wayOperate", "resourceAudio.resourceSize", "resourceAudio.resourceFormat", "resourceAudio.audioSource", "resourceAudio.intro", "resourceAudio.tag", "resourceAudio.ifIndexed", "resourceAudio.fileCount", "resourceAudio.resourceStatus"});
        binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }
	

	/**
	 * create product_task
	 * 
	 * @param productContainer
	 * @param productId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "create", method = GET)
	public String create(@ModelAttribute ProductContainerTask productContainerTask,//
			@RequestParam(value = "containerid", required = true) RecommandContainer container, //
			Model model) {
    	List<String> dates = new ArrayList<String>();
    	for (int _day = 1; _day <= 30; _day++){
    		Calendar cal = Calendar.getInstance();
    		cal.add(Calendar.DATE, _day);
    		SimpleDateFormat day_df = new SimpleDateFormat("yyyyMMdd");
    		dates.add(day_df.format(cal.getTime()));
    	}
    	model.addAttribute("dates", dates);
    	
		return "domain/productcontainertask/create";
	}

	/**
	 * Performs the create action and redirect to the show view.
	 */
	@RequestMapping(value = "create", method = { POST, PUT })
	public String create(@Valid @ModelAttribute ProductContainerTask productContainerTask, BindingResult bindingResult,
			@RequestParam(value = "containerid", required = true) RecommandContainer container,
    		@RequestParam(value = "displayDates", required = true) List<Integer> displayDates,
    		@RequestParam(value = "ids", required = true) List<Long> ids,
			HttpServletRequest request, Model model) {
		
		//验证输入ID是否存在
		if (productContainerTask.getType().intValue() == ProductContainerTask.TypeEnum.Product.ordinal()) {
			for (Long productId : ids) {
				//ProductList productList = productListRepository.findByProductId(productId);
				if(!productListRepository.exists(productId)){
					bindingResult.addError(new FieldError("productContainerTask", "productId", "产品ID " + String.valueOf(productId) + " 不存在"));
					return create(productContainerTask, container, model);
				}
			}
		} else if (productContainerTask.getType().intValue() == ProductContainerTask.TypeEnum.Folder.ordinal()) {
			for (Long folderId : ids) {
				//Folder folder = folderRepository.findByFolderId(folderId);
				if(!folderRepository.exists(folderId)){
					bindingResult.addError(new FieldError("productContainerTask", "productId", "频道ID " + String.valueOf(folderId) + " 不存在"));
					return create(productContainerTask, container, model);
				}
			}
		} else if (productContainerTask.getType().intValue() == ProductContainerTask.TypeEnum.Advertise.ordinal()) {
			for (Long advertiseId : ids) {
				//Advertise advertiseList = advertiseRepository.findByAdvertiseId(advertiseId);
				if(!advertiseRepository.exists(advertiseId)){
					bindingResult.addError(new FieldError("productContainerTask", "productId", "广告ID " + String.valueOf(advertiseId) + " 不存在"));
					return create(productContainerTask, container, model);
				}
			}
		}
		
		List<ProductContainerTask> productContainerTasks = new ArrayList<ProductContainerTask>();
		Date date = new Date();
		for (Integer displayDate : displayDates) {
			//1、取最大排序 推荐 + 日期 
			Integer sort = 0;
			List<ProductContainerTask> pcts = productContainerTaskRepository
				.findByContainerIdAndDisplayDate(container.getContainerId(), displayDate, new Sort(new Order(Direction.ASC, "sortOrder")));
			if (!pcts.isEmpty()) {
				sort = pcts.size();
			}
			
			for (Long id : ids) {
				boolean add_flag = true;
				ProductContainerTask new_pct = new ProductContainerTask();
				
				if (!pcts.isEmpty()) {
					//2、判断 ID 是否存在
					for (ProductContainerTask p : pcts) {
						if ((productContainerTask.getType().intValue() == ProductContainerTask.TypeEnum.Product.ordinal()
								&& p.getProductId() == id) ||
						   (productContainerTask.getType().intValue() == ProductContainerTask.TypeEnum.Folder.ordinal()
								&& p.getTfolderId() == id) ||
						   (productContainerTask.getType().intValue() == ProductContainerTask.TypeEnum.Advertise.ordinal()
								&& p.getAdvertiseId() == id))
						{
							add_flag = false;
							break;
						}
					}
				}
				
				//3、保存
				if (add_flag) {
					
					if (productContainerTask.getType().intValue() == ProductContainerTask.TypeEnum.Product.ordinal()) {
						new_pct.setProduct(productListRepository.findByProductId(id));
						new_pct.setType(ProductContainerTask.TypeEnum.Product.ordinal());
					} else if (productContainerTask.getType().intValue() == ProductContainerTask.TypeEnum.Folder.ordinal()) {
						new_pct.setTfolder(folderRepository.findByFolderId(id));
						new_pct.setType(ProductContainerTask.TypeEnum.Product.ordinal());
					} else if (productContainerTask.getType().intValue() == ProductContainerTask.TypeEnum.Advertise.ordinal()) {
						new_pct.setAdvertise(advertiseRepository.findByAdvertiseId(id));
						new_pct.setType(ProductContainerTask.TypeEnum.Advertise.ordinal());
					}
					
					new_pct.setContainer(container);
					new_pct.setDisplayDate(displayDate);
					new_pct.setStatus(ProductContainerTask.ENABLE);
					new_pct.setModDate(date);
					new_pct.setModBy(AccountContext.getAccountContext().getUsername());
					new_pct.setCreateDate(date);
					new_pct.setCreateBy(AccountContext.getAccountContext().getUsername());
					
					sort = sort + 1;
					new_pct.setSortOrder(sort.longValue());
					
					productContainerTasks.add(new_pct);
				}
			}			
		}
		
		productContainerTaskRepository.save(productContainerTasks);
		return "redirect:/domain/recommandcontainer/showproducttask/" + container.getContainerId();
	}
	
	/**
	 * Serves the delete form asking the user if the entity should be really
	 * deleted.
	 */
	@RequestMapping(value = "deletebydate/{containerid}/{date}", method = GET)
	public String deletebydate(@PathVariable("containerid") Long containerid,
			@PathVariable("date") Integer date,	Model model) {
		model.addAttribute("date", date);
		model.addAttribute("containerid", containerid);
		return "domain/productcontainertask/deletebydate";
	}

	/**
	 * Performs the delete action and redirect to the search view.
	 */
	@RequestMapping(value = "deletebydate/{containerid}/{date}", method = { PUT, POST, DELETE })
	public String deletebydate(@PathVariable("containerid") Long containerid,
			@PathVariable("date") Integer date){
		productContainerTaskRepository.deleteByDateAndContainerId(date, containerid);
		return "redirect:/domain/recommandcontainer/showproducttask/" + containerid;
	}
	
	@RequestMapping(value = "copydate/{containerid}/{date}", method = GET)
	public String copydate(@PathVariable("containerid") Long containerid,
			@PathVariable("date") Integer date,	Model model) {
		model.addAttribute("date", date);
		model.addAttribute("containerid", containerid);
		List<Integer> displayDates = productContainerTaskRepository.findDisplayDateByContainerId(containerid, new Sort(new Order(Direction.ASC, "displayDate")));
		String disStrings = "";
		if (displayDates != null && displayDates.size() > 0) {
			for (Integer dat : displayDates) {
				disStrings = disStrings + dat.toString() + ":";
			}
			model.addAttribute("disStrings", disStrings);
			model.addAttribute("displayDates", displayDates);
		}
    	Calendar cal = Calendar.getInstance();
    	SimpleDateFormat day_df = new SimpleDateFormat("yyyyMMdd");
    	String today = day_df.format(cal.getTime());
		Integer currDate = Integer.parseInt(today);
		model.addAttribute("disToday", currDate);
		return "domain/productcontainertask/copydate";
	}
	
	@RequestMapping(value = "copydate/{containerid}/{date}", method = { PUT, POST, DELETE })
	public String copydate(@PathVariable("containerid") Long containerid,
			@PathVariable("date") Integer date, Model model,
			@RequestParam(value = "copydate",required=true) String copydate
			){
		
		Integer disToDate = Integer.valueOf(copydate.replace("-", ""));
		List<ProductContainerTask> pcts = productContainerTaskRepository.findByContainerIdAndDisplayDate(containerid, date, new Sort(new Order(Direction.ASC, "sortOrder")));
		if (pcts != null && pcts.size() > 0) {
			Date d = new Date();
			List<ProductContainerTask> save_pcts = new ArrayList<ProductContainerTask>();
			for (ProductContainerTask pct : pcts) {
				ProductContainerTask save_pct = pct.copy();
				save_pct.setPrimaryKey(null);
				save_pct.setDisplayDate(disToDate);
				save_pct.setStatus(ProductContainerTask.ENABLE);
				save_pct.setModDate(d);
				save_pct.setModBy(AccountContext.getAccountContext().getUsername());
				save_pct.setCreateDate(d);
				save_pct.setCreateBy(AccountContext.getAccountContext().getUsername());
				save_pcts.add(save_pct);
			}
			productContainerTaskRepository.save(save_pcts);
		}
		return "redirect:/domain/recommandcontainer/showproducttask/" + containerid + "#sort" + disToDate.toString();
	}

	/**
	 * Serves search by example form, search by pattern form and search by named
	 * query form.
	 */
	@RequestMapping(value = "*", method = GET)
	public void catchAll(@ModelAttribute ProductContainerSearchForm productContainerSearchForm) {
	}

}