/*

 * Template pack-mvc-3:src/main/java/web/controller/controllerwithPathVariable.e.vm.java
 */
package org.visitor.appportal.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

import org.visitor.appportal.domain.Category;
import org.visitor.appportal.domain.Folder;
import org.visitor.appportal.domain.HtmlPage;
import org.visitor.appportal.domain.HtmlPage.PageTypeEnum;
import org.visitor.appportal.domain.ProductContainer;
import org.visitor.appportal.domain.ProductSiteFolder;
import org.visitor.appportal.redis.SiteRedisRepository;
import org.visitor.appportal.repository.CategoryRepository;
import org.visitor.appportal.repository.FolderRepository;
import org.visitor.appportal.repository.HtmlPageRepository;
import org.visitor.appportal.repository.PictureRepository;
import org.visitor.appportal.repository.ProductContainerRepository;
import org.visitor.appportal.service.SiteFolderService;
import org.visitor.appportal.service.SystemPreference;
import org.visitor.appportal.web.utils.SearchParameters;

@Controller
@RequestMapping("/domain/folder/")
public class FolderControllerWithPathVariable {
	@Autowired
	private SystemPreference systemPreference;
	@Autowired
	private SiteRedisRepository siteRedisRepository;
    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private HtmlPageRepository htmlPageRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private SiteFolderService siteFolderService;
	@Autowired
	private PictureRepository pictureRepository;

	@Autowired
	private ProductContainerRepository productContainerRepository;

    /**
     * This method is invoked by Spring MVC before the handler methods.
     * <p>
     * The path variable is converted by SpringMVC to a Folder via the {@link FolderFormatter}.
     * Before being passed as an argument to the handler, SpringMVC binds the attributes on the resulting model,
     * then each handler method may receive the entity, potentially modified, as an argument.
     */
    @ModelAttribute
    public Folder getFolder(@PathVariable("pk") Long pk) {
        return folderRepository.findOne(pk);
    }

    /**
     * TODO: 不应该使用ProductList的状态，应该自己
     * @return
     */
	@ModelAttribute(value = "safeTypeList")
	public List<Folder.SafeTypeEnum> getSafeType() {
		return Arrays.asList(Folder.SafeTypeEnum.values());
	}
	
    /**
     * Serves the show view for the entity.
     */
	@RequestMapping("show/{pk}")
	public String show(@ModelAttribute Folder folder, Model model) {
		if (null != folder.getPrimaryKey()) {
			
			Map<String,HtmlPage> pages = new LinkedHashMap<String,HtmlPage>();
			pages.put("index",htmlPageRepository
					.findByFolderIdAndPageType(folder.getFolderId(), PageTypeEnum.Index.getValue()));
			pages.put("list", htmlPageRepository
					.findByFolderIdAndPageType(folder.getFolderId(), PageTypeEnum.List.getValue()));
			pages.put("detail", htmlPageRepository
					.findByFolderIdAndPageType(folder.getFolderId(), PageTypeEnum.Detail.getValue()));
			pages.put("navi", htmlPageRepository
					.findByFolderIdAndPageType(folder.getFolderId(), PageTypeEnum.Navi.getValue()));

			model.addAttribute("pagesOfFolder", pages);
			
			Page<ProductSiteFolder> productSiteFolders = siteFolderService.findFolderProducts(folder.getFolderId(), new PageRequest(0,
							20, new Sort(new Order(Direction.DESC, "sortOrder"))));

			model.addAttribute("productSiteFolders", productSiteFolders.getContent());

			model.addAttribute("picDomain", systemPreference.getPictureDomain());
			
			List<ProductContainer> list = productContainerRepository.findByTfolderId(folder.getFolderId());
			model.addAttribute("recommandList", list);

		}
		return "domain/folder/show";
	}

    /**
     * Serves the update form view.
     */
    @RequestMapping(value = "update/{pk}", method = GET)
    public String update() {
        return "domain/folder/update";
    }

    /**
     * Performs the update action and redirect to the show view.
     */
    @RequestMapping(value = "update/{pk}", method = { PUT, POST })
    public String update(@Valid @ModelAttribute Folder folder, BindingResult bindingResult,
    		@RequestParam(value = "picId", required = false) Long picId) {
        if (bindingResult.hasErrors()) {
            return update();
        }
        if (picId != null) {
        	folder.setPic(pictureRepository.findOne(picId));
        }        
        folderRepository.save(folder);
        
        return "redirect:/domain/folder/show/" + folder.getPrimaryKey();
    }
    
    @RequestMapping(value = "tag/{pk}", method = GET)
    public String tag(Model model) {
    	List<Category> categorys = categoryRepository.findByParentCategoryIdAndStatus(Category.TAG, Category.ENABLE, null); 	
    	model.addAttribute("categorys", categorys);
    	return "domain/folder/tag";
    }
    
    @RequestMapping(value = "tag/{pk}", method = { PUT, POST })
    public String tag(@ModelAttribute Folder folder,
    		@RequestParam(value = "tagId", required = false) Long tag,
    		@RequestParam(value = "tagls", required = false) List<String> tagls) {
    	
    	List<Category> olds = folder.getTags();

    	Map<Long, Category> map = new HashMap<Long, Category>();
    	if (tagls != null && tagls.size() > 0) {
    		for (String tagid : tagls) {
    			map.put(Long.valueOf(tagid), null);//标记此tagId为新增加的tag
    		}
    	}
    	for(Iterator<Category> it = olds.iterator(); it.hasNext();) {
    		Category c = it.next();
    		if(!map.containsKey(c.getCategoryId())) {
    			it.remove();
    			map.put(c.getCategoryId(), c);//标记此ID为已经删除的tagId。
    		} 
    	}
    	
    	for(Map.Entry<Long, Category> entry : map.entrySet()) {
    		if(null == entry.getValue()) {//这是未包含已经删除的tagId
    			Category temp = this.categoryRepository.findOne(entry.getKey());
    			if(!olds.contains(temp)) {
    				olds.add(temp);
    				entry.setValue(temp);//保存频道关联发生改变的tag
    			}
    		}
    	}
    	if (tag != null) {
        	folder.setTag(categoryRepository.findOne(tag));
    	} else {
    		folder.setTag(null);
    	}
    	folderRepository.save(folder);
    	
    	//更新各个tag关联的频道
    	for(Map.Entry<Long, Category> entry : map.entrySet()) {
    		if(null != entry.getValue()) {//所有有改动的tag列表
    			List<Folder>  folders = entry.getValue().getFolders();
    			List<Long> ids = new ArrayList<Long>();
    			for(Folder f : folders) {
    				ids.add(f.getFolderId());
    			}
    			siteRedisRepository.setFolderListByTagId(entry.getKey(), ids);
    		}
    	}
    	siteRedisRepository.setFolder(folder);
    	return "redirect:/domain/folder/show/" + folder.getPrimaryKey();
    }
    
    /**
     * sort child folder
     */
    @RequestMapping(value = "child/{pk}", method = GET)
    public String showchild(@ModelAttribute Folder folder, Model model) {
    	Folder f = new Folder();
    	f.setParentFolder(folder);
    	SearchParameters searchParameters = new SearchParameters();
    	searchParameters.setSortColumnKey("sortOrder");
    	searchParameters.setSortOrder(SearchParameters.DESCENDING);
    	searchParameters.setPageSize(100);
    	
    	List<Folder> folderList = siteFolderService.findFolderChild(folder.getFolderId(), new Sort(new Order(Direction.DESC, "sortOrder")));
    	model.addAttribute("folders", folderList);
    	model.addAttribute("picDomain", this.systemPreference.getPictureDomain());
        return "domain/folder/child";
    }
}