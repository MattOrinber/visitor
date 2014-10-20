/*

 * Template pack-mvc-3:src/main/java/web/controller/controller.e.vm.java
 */
package org.visitor.appportal.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.text.SimpleDateFormat;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import org.visitor.appportal.domain.Role;
import org.visitor.appportal.repository.RoleRepository;

@Controller
@RequestMapping("/domain/role/")
public class RoleController {

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
    public String list(@ModelAttribute RoleSearchForm roleSearchForm, Model model) {
        model.addAttribute("rolesCount", roleRepository.findCount(roleSearchForm.getRole(), roleSearchForm.toSearchTemplate()));
        model.addAttribute("roles", roleRepository.find(roleSearchForm.getRole(), roleSearchForm.toSearchTemplate()));
        return "domain/role/list";
    }

    /**
     * Serves the create form.
     */
    @RequestMapping(value = "create", method = GET)
    public String create(@ModelAttribute Role role) {
        return "domain/role/create";
    }

    /**
     * Performs the create action and redirect to the show view.
     */
    @RequestMapping(value = "create", method = { POST, PUT })
    public String create(@Valid @ModelAttribute Role role, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return create(role);
        }

        roleRepository.save(role);
        return "redirect:/domain/role/show/" + role.getPrimaryKey();
    }

    /**
     * Serves search by example form, search by pattern form and search by named query form.
     */
    @RequestMapping(value = "*", method = GET)
    public void catchAll(@ModelAttribute RoleSearchForm roleSearchForm) {
    }

}