/*

 * Template pack-mvc-3:src/main/java/web/controller/controllerwithPathVariable.e.vm.java
 */
package org.visitor.appportal.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.visitor.appportal.domain.Account;
import org.visitor.appportal.domain.Role;
import org.visitor.appportal.repository.AccountRepository;
import org.visitor.appportal.repository.RoleRepository;

@Controller
@RequestMapping("/domain/account/")
public class AccountControllerWithPathVariable {
	
    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private RoleRepository roleRepository;

    /**
     * This method is invoked by Spring MVC before the handler methods.
     * <p>
     * The path variable is converted by SpringMVC to a Account via the {@link AccountFormatter}.
     * Before being passed as an argument to the handler, SpringMVC binds the attributes on the resulting model,
     * then each handler method may receive the entity, potentially modified, as an argument.
     */
    @ModelAttribute
    public Account getAccount(@PathVariable("pk") Long pk) {
        return accountRepository.findOne(pk);
    }

    /**
     * Serves the show view for the entity.
     */
    @RequestMapping("show/{pk}")
    public String show(@ModelAttribute Account account) {
        return "domain/account/show";
    }

    /**
     * Serves the update form view.
     */
    @RequestMapping(value = "update/{pk}", method = GET)
    public String update(Model model) {
    	model.addAttribute("sys_roles", roleRepository.findAll());    	
        return "domain/account/update";
    }

    /**
     * Performs the update action and redirect to the show view.
     */
    @RequestMapping(value = "update/{pk}", method = { PUT, POST })
    public String update(@Valid @ModelAttribute Account account, BindingResult bindingResult, Model model,
    		@RequestParam(value = "roleIds", required = false) List<String> roleIds) {
        if (bindingResult.hasErrors()) {
            return update(model);
        }
        if (roleIds != null && roleIds.size() > 0) {
        	List<Role> roles = new ArrayList<Role>();
        	for (String roleId : roleIds) {
        		Role e = roleRepository.findOne(Long.valueOf(roleId));
        		roles.add(e);
        	}
        	account.setRoles(roles);
        }
        accountRepository.save(account);
        return "redirect:/domain/account/show/" + account.getPrimaryKey();
    }

    /**
     * Serves the delete form asking the user if the entity should be really deleted.
     */
    @RequestMapping(value = "delete/{pk}", method = GET)
    public String delete() {
        return "domain/account/delete";
    }

    /**
     * Performs the delete action and redirect to the search view.
     */
    @RequestMapping(value = "delete/{pk}", method = { PUT, POST, DELETE })
    public String delete(@ModelAttribute Account account) {
    	accountRepository.delete(account);
        return "redirect:/domain/account/search";
    }

}