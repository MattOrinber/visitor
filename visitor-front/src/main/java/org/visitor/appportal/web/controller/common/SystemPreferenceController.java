/**
 * 
 */
package org.visitor.appportal.web.controller.common;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.visitor.appportal.domain.Category;
import org.visitor.appportal.domain.Site;
import org.visitor.appportal.redis.RedisKeys;
import org.visitor.appportal.repository.CategoryRepository;
import org.visitor.appportal.repository.SiteRepository;
import org.visitor.appportal.web.utils.SiteUtil;

/**
 * 设置系统参数。
 * @author mengw
 *
 */
@Controller
@RequestMapping("/domain/config/")
public class SystemPreferenceController {
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private SiteRepository siteRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	/**
	 * 
	 */
	public SystemPreferenceController() {
		// TODO Auto-generated constructor stub
	}
	
    /**
     * Performs the list action.
     */
    @RequestMapping(value = { "list", "" })
    public String list(HttpServletRequest request, Model model) {
    	Map<Object, Object> map = stringRedisTemplate.opsForHash().entries(RedisKeys.getSystemPreferenceKey());
    	model.addAttribute("attributeMap", map);
    	Integer siteId = SiteUtil.getSiteFromSession(request.getSession());
		List<Site> list = new ArrayList<Site>();
    	
		if(siteId != null) {
    		Site site = siteRepository.findOne(siteId);
    		list.add(site);
    	}else {
    		list = this.siteRepository.findAll();
    	}
		
		model.addAttribute("sites", list.iterator());

    	model.addAttribute("oss", categoryRepository.findByParentCategoryId(Category.PLATFORM));
        return "domain/config/list";
    }
    
	@RequestMapping(value = "create", method = { POST, PUT })
    public String create(HttpServletRequest request, Model model) {
		Enumeration<?> names = request.getParameterNames();
    	if(null != names) {
    		while(names.hasMoreElements()) {
    			String key = String.valueOf(names.nextElement());
    			stringRedisTemplate.opsForHash().put(RedisKeys.getSystemPreferenceKey(), 
    				key, request.getParameter(key));
    		}
    	}
    	 return "redirect:/domain/config/list";
    }
}
