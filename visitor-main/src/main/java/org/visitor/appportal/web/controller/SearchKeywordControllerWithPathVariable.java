/*

 * Template pack-mvc-3:src/main/java/web/controller/controllerwithPathVariable.e.vm.java
 */
package org.visitor.appportal.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.visitor.app.portal.model.SearchItem;
import org.visitor.appportal.context.AccountContext;
import org.visitor.appportal.domain.Advertise;
import org.visitor.appportal.domain.Folder;
import org.visitor.appportal.domain.ProductList;
import org.visitor.appportal.domain.SearchKeyword;
import org.visitor.appportal.domain.Site;
import org.visitor.appportal.repository.AdvertiseRepository;
import org.visitor.appportal.repository.FolderRepository;
import org.visitor.appportal.repository.ProductListRepository;
import org.visitor.appportal.repository.SearchKeywordRepository;
import org.visitor.appportal.repository.SiteRepository;
import org.visitor.appportal.web.utils.SiteUtil;

@Controller
@RequestMapping("/domain/searchkeyword/")
public class SearchKeywordControllerWithPathVariable {
	
    @Autowired
    private SiteRepository siteRepository;
    
    @Autowired
    private SearchKeywordRepository searchKeywordRepository;
    @Autowired
    private ProductListRepository productListRepository;
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private AdvertiseRepository advertiseRepository;

    /**
     * This method is invoked by Spring MVC before the handler methods.
     * <p>
     * The path variable is converted by SpringMVC to a SearchKeyword via the {@link SearchKeywordFormatter}.
     * Before being passed as an argument to the handler, SpringMVC binds the attributes on the resulting model,
     * then each handler method may receive the entity, potentially modified, as an argument.
     */
    @ModelAttribute
    public SearchKeyword getSearchKeyword(@PathVariable("pk") Long pk) {
        return searchKeywordRepository.findOne(pk);
    }

    /**
     * Serves the show view for the entity.
     */
    @RequestMapping("show/{pk}")
    public String show(@ModelAttribute SearchKeyword searchKeyword) {
        return "domain/searchkeyword/show";
    }

    /**
     * 这里，同样需要加入站点和平台列表
     * Serves the update form view.
     */
    @RequestMapping(value = "update/{pk}", method = GET)
    public String update(Model model) {
    	List<Site> siteList = this.siteRepository.findByStatus(Site.StatusEnum.Enable.ordinal());
    	model.addAttribute("siteList", siteList);
    	//List<Category> catList = categoryService.getSpecialPlatList();
    	//model.addAttribute("platList", catList);

        return "domain/searchkeyword/update";
    }

    /**
     * Performs the update action and redirect to the show view.
     */
    @RequestMapping(value = "update/{pk}", method = { PUT, POST })
    public String update(@Valid @ModelAttribute SearchKeyword searchKeyword, BindingResult bindingResult
    		,@RequestParam(value = "oldname", required = false) String oldname, Model model) {
        if (bindingResult.hasErrors()) {
            return update(model);
        }
        if (StringUtils.isNotEmpty(oldname) && !oldname.equals(searchKeyword.getName())) {
            if(!searchKeywordRepository.findBySiteIdAndName(searchKeyword.getSite().getSiteId(), searchKeyword.getName(), null).isEmpty()){
        		bindingResult.addError(new FieldError("searchKeyword", "name", "关键词 '" + searchKeyword.getName() + "' 已经存在"));
        		searchKeyword.setName(oldname);
        		return update(model);
        	}
        }
        
        String userName = AccountContext.getAccountContext().getUsername();
        searchKeyword.setModBy(userName);
        searchKeyword.setModDate(new Date());
        
        Site site = siteRepository.findOne(searchKeyword.getSiteId());
        searchKeyword.setSite(site);
        
        //Category plat = this.categoryService.categoryRepository.findOne(searchKeyword.getPlatformId());
        //searchKeyword.setPlat(plat);
        
        searchKeywordRepository.save(searchKeyword);
        return "redirect:/domain/searchkeyword/show/" + searchKeyword.getPrimaryKey();
    }

    @RequestMapping(value = "updateids/{pk}", method = { PUT, POST })
    public String updateIds(@ModelAttribute SearchKeyword searchKeyword, BindingResult bindingResult
    		,Model model,HttpServletRequest request) {
    	
    	model.addAttribute("search", searchKeyword);
    	
        if(bindingResult.hasErrors()) {
            return "domain/searchkeyword/idlist";
        }

        String idList = searchKeyword.getIdList();
        int wordKind = searchKeyword.getWordKind();
    	String prex = "产品";
    	if(wordKind == SearchKeyword.WordKindEnum.PRD.getValue()){
    		prex = "产品";
    	}else if(wordKind == SearchKeyword.WordKindEnum.FLD.getValue()){
    		prex = "频道";
    	}else {
    		prex = "广告";
    	}
    	
    	boolean isOk = true;
    	Integer siteId = SiteUtil.getSiteFromSession(request.getSession());
        if(StringUtils.isNotBlank(idList) && siteId != null){
        	
        	//对IDList去重
        	Set<String> s2 = new LinkedHashSet<String>(Arrays.asList(idList.split(",")));
        	idList = s2.toString().replaceAll("[\\[\\] ]", "");
        	
	        //检查ID的有效性
	        String ids[] = idList.split(",");
	        Long lngIds[] = new Long[ids.length];
	        int i=0;
	        try{
	        	for(;i<lngIds.length;i++){
	        		lngIds[i] = Long.valueOf(ids[i]);
	        	}
	        }catch(NumberFormatException e){
	        	bindingResult.addError(new FieldError("searchKeyword", "idList", prex + "ID："+ids[i]+"不是有效的数字！"));
	        	isOk = false;
	        }
	        
	        if(isOk){
	        	//有效性测试
	        	i=0;
	        	SearchKeyword.WordKindEnum wk = SearchKeyword.WordKindEnum.getInstance(wordKind);
	        	switch(wk){
	        		case ADV:
			        	for(;i<lngIds.length;i++){
		    				Advertise adv = this.advertiseRepository.findByAdvertiseIdAndSiteId(lngIds[i],siteId);
		        			if(adv != null && (StringUtils.isBlank(adv.getDescription()) || adv.getIconId() == null)){
			    	        	bindingResult.addError(new FieldError("searchKeyword", "idList", prex + "ID："+ids[i]+"无效或不属于当前站点！"));	
			    	        	isOk = false;
			    	        	break;
		        			}
			        	}
			        	break;
	        		case PRD:
		    			for(;i<lngIds.length;i++){
		    				ProductList pl = this.productListRepository.findByPidWithSiteId(siteId, lngIds[i]);
		    				if(pl == null || !pl.isEnable()){
		    	   	        	bindingResult.addError(new FieldError("searchKeyword", "idList", prex + "ID："+ids[i]+"无效或不属于当前站点！"));	
		    	   	        	isOk = false;
		    	   	        	break;
		    				}
		    			}
		    			break;
	        		case FLD:
		    			for(;i<lngIds.length;i++){
		    				Folder folder = this.folderRepository.findByFolderIdAndSiteId(lngIds[i], siteId);//(Long.valueOf(item),
	    	   	        	if(folder == null || !folder.isEnable()){
	    	   	        		bindingResult.addError(new FieldError("searchKeyword", "idList", prex + "ID："+ids[i]+"无效或不属于当前站点！"));	
	    	   	        		isOk = false;
	    	   	        		break;
	    	   	        	}
		    			}
		    			break;
		        }
	        }//is ok
	        
	        if(isOk){
		        searchKeyword.setIdList(idList);
		        searchKeywordRepository.save(searchKeyword);
	        }
        }else{
	        isOk = false;
        }
	    
        if(!isOk){
        	return "domain/searchkeyword/idlist"; 
        }else {
        	return "redirect:/domain/searchkeyword/idlist/" + searchKeyword.getPrimaryKey(); 
        }

    }

    /**
     * Serves the delete form asking the user if the entity should be really deleted.
     */
    @RequestMapping(value = "delete/{pk}", method = GET)
    public String delete() {
        return "domain/searchkeyword/delete";
    }

    /**
     * Performs the delete action and redirect to the search view.
     */
    @RequestMapping(value = "delete/{pk}", method = { PUT, POST, DELETE })
    public String delete(@ModelAttribute SearchKeyword searchKeyword) {
        searchKeywordRepository.delete(searchKeyword);
        return "redirect:/domain/searchkeyword/search";
    }

    /**
     * 查找指定关键字的产品列表，并显示
     */
    @RequestMapping(value = "idlist/{pk}", method = GET)
    public String idlist(@ModelAttribute SearchKeyword searchKeyword,Model model) {
        
    	if(searchKeyword == null){
    		return "redirect:/domain/searchkeyword/search";
    	}
    	
    	model.addAttribute("search", searchKeyword);
    	
    	String idList = searchKeyword.getIdList();
    	Integer wordKind = searchKeyword.getWordKind();
    	
    	if(StringUtils.isBlank(idList)){
    		//如果没有产品，则不做处理
    		return "domain/searchkeyword/idlist";
    	}
    	
    	String idArray [] = idList.split(",");
    	List<SearchItem> itemList = new ArrayList<SearchItem>();
    	
    	for(String id:idArray){
    		SearchItem si = null;
    		if(wordKind == SearchKeyword.WordKindEnum.PRD.getValue()){
    			//做非空判断，因为该元素很可能被删除了
    			ProductList prd = this.productListRepository.findOne(Long.valueOf(id));
    			if(prd!=null){
    				si = new SearchItem(prd.getPrimaryKey(),prd.getName());
    			}
    		}else if(wordKind == SearchKeyword.WordKindEnum.FLD.getValue()){
    			Folder fld = this.folderRepository.findOne(Long.valueOf(id));
    			if(fld!=null){
    				si = new SearchItem(fld.getPrimaryKey(),fld.getName());   
    			}
    		}else{
    			Advertise adv = this.advertiseRepository.findOne(Long.valueOf(id));
    			if(adv!=null){
    				si = new SearchItem(adv.getPrimaryKey(),adv.getName());
    			}
    		}
    		
    		if(si!=null){
    			itemList.add(si);
    		}
    	}
    	
    	model.addAttribute("itemList", itemList);
    	model.addAttribute("itemIds", idList);

        return "domain/searchkeyword/idlist";
    }

    /**
     * 删除指定的关键字中的部分ID
     * @param searchKeyword
     * @param itemIds
     * @return
     */
    @RequestMapping(value = "deleteitem/{pk}/{itemIds}", method = GET)
    public String deleteitem(@ModelAttribute SearchKeyword searchKeyword,
    		@PathVariable("itemIds") String itemIds) {
    	
    	if(searchKeyword != null && StringUtils.isNotBlank(searchKeyword.getIdList())){
    		//以上两个条件必须满足，要不然就没有删除的必要了
    		if(StringUtils.isNotBlank(itemIds)){
    			String arrOfItem[] = itemIds.split("_");
    			
    			String oldIds[] = searchKeyword.getIdList().split(",");
    			//现在我们要做的，是将oldIds中，包含arrOfItem的值给去掉，可转化为集合，进行求差
    			//但集合是无序的，所以还是通过List来做吧
    			//由于会随机移除元素，所以这里我们选择LinkedList，而不是ArrayList
    			List<String> oldList = new LinkedList<String>(Arrays.asList(oldIds));
    			List<String> newList = new LinkedList<String>(Arrays.asList(arrOfItem));
    			
    			//替换
    			oldList.removeAll(newList);
    			//将剩下的结果转化为字符串，有一个比较巧的方法
    			String remainItem = oldList.toString();
    			//去掉左括号，右括号，及空格
    			searchKeyword.setIdList(remainItem.replaceAll("[\\[\\] ]", ""));
    			searchKeywordRepository.save(searchKeyword);
    			
    		}
    		
    		return "redirect:/domain/searchkeyword/idlist/" + searchKeyword.getPrimaryKey(); 
    	}
        return "redirct:/domain/searchkeyword/search";
    }

    /**
     * 对指定的关键字中的结果排序
     * @param searchKeyword
     * @param itemIds
     * @return
     */
    @RequestMapping(value = "sort/{pk}/{itemIds}", method = GET)
	@ResponseBody
    public Boolean sort(@ModelAttribute SearchKeyword searchKeyword,
    		@PathVariable("itemIds") String itemIds) {
    	
    	if(searchKeyword != null && StringUtils.isNotBlank(itemIds)){
    		//以上两个条件必须满足
    		//由于没有关联关系，排序过程实际上就是重设idList而已，相当简单
    		searchKeyword.setIdList(itemIds);
    		searchKeywordRepository.save(searchKeyword);
    		return true;
    	}
        return false;
    }    

    /**
     * 添加结果ID至指定关键字，这个不区分结果类型
     * @param searchKeyword
     * @param itemIds
     * @return
     */
    @RequestMapping(value = "appenditem/{pk}/{itemIds}", method = GET)
	@ResponseBody
    public String appenditem(@ModelAttribute SearchKeyword searchKeyword,
    		@PathVariable("itemIds") String itemIds,HttpServletRequest request) {
    	
    	Integer siteId = SiteUtil.getSiteFromSession(request.getSession());
    	if(searchKeyword != null && StringUtils.isNotBlank(itemIds) && siteId != null){
    		String old = searchKeyword.getIdList();
    		itemIds = itemIds.substring(0,itemIds.length()-1);//remove the last sign
    		itemIds = itemIds.replace("_", ",");
    		
    		if(StringUtils.isNotBlank(old)){
    			List<String> oldList = new LinkedList<String>(Arrays.asList(old.split(",")));
    			List<String> newList = new LinkedList<String>(Arrays.asList(itemIds.split(",")));
    			newList.removeAll(oldList);//移除相同项
    			oldList.addAll(newList);//合并
    			
    			itemIds = oldList.toString().replaceAll("[\\[\\] ]", "");
    		}

    		String items[]= itemIds.split(",");
    		/**产品有效性的判断，只能选择本站点下的产品*/
    		//从列表中选择数据都是有效的，只有当类型为广告时，才判断广告的信息是否有效
    		SearchKeyword.WordKindEnum wordEnum = SearchKeyword.WordKindEnum.getInstance(searchKeyword.getWordKind());
    		
    		switch(wordEnum){
    			case ADV :
	    			//依次判断各项
	    			for(String item:items){
	    				Advertise adv = this.advertiseRepository.findByAdvertiseIdAndSiteId(Long.valueOf(item),
	    					siteId);
	    				if(adv == null || StringUtils.isBlank(adv.getDescription()) || adv.getIconId() == null){
	    					//出错了
	    					return "{\"error\":\"1\",\"errorInfo\":\"invalid id "+item+"\"}";
	    				}
	    			}
	    			break;
    			case PRD :
	    			for(String item : items){
	    				ProductList pl = this.productListRepository.findByPidWithSiteId(siteId, Long.valueOf(item));
	    				if(pl == null || !pl.isEnable()){
	    					return "{\"error\":\"1\",\"errorInfo\":\"invalid id "+item+"\"}";
	    				}
	    			}
	    			break;
    			case FLD:
	    			for(String item:items){
	    				Folder folder = this.folderRepository.findByFolderIdAndSiteId(Long.valueOf(item), siteId);//(Long.valueOf(item),
	    				if(folder == null || !folder.isEnable()){
	    					return "{\"error\":\"1\",\"errorInfo\":\"invalid id " + item +"\"}";
	    				}
	    			}
	    			break;
    		}
    		
    		//save
    		searchKeyword.setIdList(itemIds);
    		searchKeywordRepository.save(searchKeyword);
    		return "{\"error\":\"0\"}";
    	}
    	
        return "{\"error\":\"1\",\"errorInfo\":\"failed\"}";
    }

}