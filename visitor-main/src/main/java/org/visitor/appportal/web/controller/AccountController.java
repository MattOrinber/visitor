/*

 * Template pack-mvc-3:src/main/java/web/controller/controller.e.vm.java
 */
package org.visitor.appportal.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.visitor.appportal.context.AccountContext;
import org.visitor.appportal.domain.Account;
import org.visitor.appportal.domain.Role;
import org.visitor.appportal.repository.AccountRepository;
import org.visitor.appportal.repository.RoleRepository;

import com.mysql.jdbc.StringUtils;

@Controller
@RequestMapping("/domain/account/")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
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
    public String list(@ModelAttribute AccountSearchForm accountSearchForm, Model model) {
    	model.addAttribute("accountsCount", accountRepository.findCount(accountSearchForm.getAccount(), accountSearchForm.toSearchTemplate()));
        model.addAttribute("accounts", accountRepository.find(accountSearchForm.getAccount(), accountSearchForm.toSearchTemplate()));
        return "domain/account/list";
    }

    /**
     * Serves the create form.
     */
    @RequestMapping(value = "create", method = GET)
    public String create(@ModelAttribute Account account, Model model) {
    	model.addAttribute("sys_roles", roleRepository.findAll());
        return "domain/account/create";
    }

    /**
     * Serves the changepwd form.
     */
    @RequestMapping(value = "changepwd", method = GET)
    public String changepwd(Model model, HttpServletRequest request) {
    	//当前账户
    	Account account2 = AccountContext.getAccountContext().getAccount();
    	model.addAttribute("accountId", account2.getAccountId());
    	
    	String errors [] = {" ","密码信息填写不完全","原密码输入错误"};
    	
    	int errCode = 0;
    	Integer index = (Integer)request.getAttribute("errorCode");
    	if(index != null){
    		errCode = (index < 0) ? 0 : index;
    		errCode = (index > (errors.length -1)) ? errors.length -1 : index;
    	}
    	
    	model.addAttribute("errInfo", errors[errCode]);
    	request.removeAttribute("errorCode");//只保留一次
    	
        return "domain/account/changepwd";
    }

    /**
     * Performs the change pwd action and redirect to the show view.
     */
    @RequestMapping(value = "changepwd", method = { POST, PUT })
    public String changepwdForPost(Model model, HttpServletRequest request) {
        
    	Long accountId = null;
    	String strId = request.getParameter("accountId");
    	String oldPwd = request.getParameter("oldpwd");
    	String newPwd = request.getParameter("newpwd");
    	String newPwdR = request.getParameter("newpwd_r");
    	
    	String errCode = "errorCode";
		request.setAttribute(errCode, 1);

    	if (StringUtils.isNullOrEmpty(oldPwd) || StringUtils.isNullOrEmpty(newPwd)
    			|| StringUtils.isNullOrEmpty(newPwdR)
    			|| !newPwd.trim().equals(newPwdR.trim())) {
            
    		return changepwd(model,request);
        }
    	
    	try {
    		accountId = Long.valueOf(strId.trim());
    	}catch(NumberFormatException e){
    		
    		return changepwd(model,request);
    	}
    	
    	
    	Account account2 = this.accountRepository.findOne(accountId);
    	if(account2 != null) {
    		
    		//加密
    		Md5PasswordEncoder md5 = new Md5PasswordEncoder();
    		oldPwd = md5.encodePassword(oldPwd.trim(), null);
    		
    		if(account2.getPassword().equals(oldPwd)) {
    			
    			newPwd = md5.encodePassword(newPwd.trim(),null);
    			
    			account2.setPassword(newPwd);
    			this.accountRepository.save(account2);
    			
    			return "redirect:/domain/account/ok";
    		}else{
    			request.setAttribute(errCode, 2);
    			return changepwd(model,request);
    		}
    	}
    	
    	return changepwd(model,request);
    }    
    /**
     * Performs the create action and redirect to the show view.
     */
    @RequestMapping(value = "create", method = { POST, PUT })
    public String create(@Valid @ModelAttribute Account account, BindingResult bindingResult, Model model,
    		@RequestParam(value = "roleIds", required = false) List<String> roleIds) {
        if (bindingResult.hasErrors()) {
            return create(account, model);
        }
        account.setStatus(0);
        if (roleIds != null && roleIds.size() > 0) {
        	List<Role> roles = new ArrayList<Role>();
        	for (String roleId : roleIds) {
        		roles.add(roleRepository.findOne(Long.valueOf(roleId)));
        	}
        	account.setRoles(roles);
        }
        
        Md5PasswordEncoder md5 = new Md5PasswordEncoder();
        String md5pwd = md5.encodePassword(account.getPassword().trim(),null);
        account.setPassword(md5pwd);
        
        accountRepository.save(account);
        return "redirect:/domain/account/show/" + account.getPrimaryKey();
    }

    /**
     * Serves search by example form, search by pattern form and search by named query form.
     */
    @RequestMapping(value = "*", method = GET)
    public void catchAll(@ModelAttribute AccountSearchForm accountSearchForm) {
    }

}