/*

 * Template pack-mvc-3:src/main/java/web/controller/controller.e.vm.java
 */
package org.visitor.appportal.web.controller;

import org.visitor.appportal.context.AccountContext;
import org.visitor.appportal.domain.*;
import org.visitor.appportal.repository.LabelRepository;
import org.visitor.appportal.repository.RoleRepository;
import org.visitor.appportal.service.game.LabelService;
import org.visitor.appportal.web.utils.SiteUtil;
import org.visitor.appportal.web.vo.FolderOption;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.visitor.appportal.repository.ProductListRepository;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.text.SimpleDateFormat;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@RequestMapping("/domain/label/")
public class LabelController {

    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private Properties systemProperties;
    @Autowired
    private ProductListRepository productListRepository;
    @Autowired
    private LabelService labelService;

    @InitBinder
    public void initDataBinder(WebDataBinder binder) {
        // you can explicitly allow certain fields or exclude them
        // binder.setAllowedFields(new String[]{"resourceAudio.resourceid", "resourceAudio.resourceName", "resourceAudio.userid", "resourceAudio.status", "resourceAudio.auditStatus", "resourceAudio.uploadBy", "resourceAudio.lastoperBy", "resourceAudio.createDt", "resourceAudio.lastoperDt", "resourceAudio.dlCount", "resourceAudio.author", "resourceAudio.audioProperty", "resourceAudio.recordCorp", "resourceAudio.version", "resourceAudio.language", "resourceAudio.region", "resourceAudio.rhythm", "resourceAudio.musicType", "resourceAudio.musicSuit", "resourceAudio.singer", "resourceAudio.lyricsAuthor", "resourceAudio.albumName", "resourceAudio.wayOperate", "resourceAudio.resourceSize", "resourceAudio.resourceFormat", "resourceAudio.audioSource", "resourceAudio.intro", "resourceAudio.tag", "resourceAudio.ifIndexed", "resourceAudio.fileCount", "resourceAudio.resourceStatus"});
        binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }

    @RequestMapping(value = "showtree")
    public String showTree(@ModelAttribute Label label, Model model, HttpServletRequest request) {

        Integer siteId = SiteUtil.getSiteFromSession(request.getSession());
        if (siteId != null) {
            model.addAttribute("labelnode", labelRepository.findAll());
            return "domain/label/showtree";
        } else {
            return "redirect:/login";
        }
    }

    /**
     * Serves search by example form, search by pattern form and search by named query form.
     */
    @RequestMapping(value = "*", method = GET)
    public void catchAll(@ModelAttribute LabelSearchForm labelSearchForm) {

    }

    /**
     * Performs the list action.
     */
    @RequestMapping(value = {"list", ""})
    public String list(@ModelAttribute LabelSearchForm labelSearchForm,
                       Model model, HttpServletRequest request) {
        Label example = labelSearchForm.getLabel();
        Integer siteId = SiteUtil.getSiteFromSession(request.getSession());

        if (siteId != null) {
            if (example.getLabelId() != null) {
                final Label o = labelRepository.findByLabelId(example.getLabelId());
                model.addAttribute("LabelsCount", o != null ? 1 : 0);
                ArrayList<Label> list = new ArrayList<Label>();
                list.add(o);
                model.addAttribute("labels", list);
            } else {

                example.setSiteId(siteId);//for test
                model.addAttribute("labelsCount", labelRepository.findCount(example, labelSearchForm.toSearchTemplate()));
                model.addAttribute("labels", labelRepository.find(example, labelSearchForm.toSearchTemplate()));
            }

            return "domain/label/list";

        } else {

            return "redirect:/login";

        }
    }

    @RequestMapping(value = "create", method = GET)
    public String create(@ModelAttribute Label label, HttpServletRequest request) {

        Integer siteId = SiteUtil.getSiteFromSession(request.getSession());

        if (siteId != null) {
            return "domain/label/create";
        } else {
            return "redirect:/login";
        }
    }

    /**
     * Performs the create action and redirect to the show view.
     */
    @RequestMapping(value = "create", method = {POST, PUT})
    public String create(@Valid @ModelAttribute Label label, BindingResult bindingResult, Model model,
                         HttpServletRequest request) {
        Label tmpLabel = labelRepository.findByName(label.getName());
        if (null != tmpLabel) {
            bindingResult.rejectValue("name", "category_name_exist", new String[]{tmpLabel.getName()},
                    "名称：" + tmpLabel.getName() + " 已经存在。");
        }

        if (bindingResult.hasErrors()) {
            return "domain/label/create";
        }
        Integer siteId = SiteUtil.getSiteFromSession(request.getSession());

        if (siteId != null) {
            Date cdate = new Date();
            String username = AccountContext.getAccountContext().getUsername();
            label.setStatus(Label.ENABLE);
            label.setCreateBy(username);
            label.setCreateDate(cdate);
            label.setModifyBy(username);
            label.setModifyDate(cdate);
            label.setSiteId(siteId);
            labelRepository.save(label);
            return "redirect:/domain/label/show?lid=" + label.getLabelId();
        } else {
            return "redirect:/login";
        }
    }

    @RequestMapping("show")
    public String show(@ModelAttribute Label label, HttpServletRequest request, Model model,
                       HttpSession session) {
        Integer siteId = SiteUtil.getSiteFromSession(session);
        if (siteId != null) {
            label = labelRepository.findByLabelId(Integer.parseInt(request.getParameter("lid")));
            model.addAttribute("label", label);
            return "domain/label/show";
        } else {
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "addProduct", method = GET)
    public String addProduct(HttpServletRequest request, Model model,
                             HttpSession session) {
        Integer siteId = SiteUtil.getSiteFromSession(session);
        if (siteId != null) {
            Label label = labelRepository.findByLabelId(Integer.parseInt(request.getParameter("lid")));
            model.addAttribute("label", label);
            return "domain/label/addproduct";
        } else {
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "addProduct", method = {POST, PUT})
    public String addProduct(@ModelAttribute Label label,
                             @RequestParam(value = "productIds", required = true) String productLists,
                             Model model, BindingResult bindingResult,
                             HttpSession session) {
        Label labelForSave = labelRepository.findByLabelId(label.getLabelId());
        Integer siteId = SiteUtil.getSiteFromSession(session);
        if (siteId != null) {
            if (labelService.addproductForLabel(labelForSave, bindingResult, productLists)) {
                model.addAttribute("label", labelForSave);
                return "domain/label/show";
            }
            return "domain/label/addProduct";
        } else {
            return "redirect:/login";
        }
    }

    /**
     * Serves the delete form asking the user if the entity should be really
     * deleted.
     */
    @RequestMapping(value = "deleteProduct", method = GET)
    public String deleteProduct(@RequestParam(value = "lid", required = true) Integer lid,
                         @RequestParam(value = "pid", required = true) Long pid,
                         Model model) {
        Label label = labelRepository.findByLabelId(lid);
        model.addAttribute("label", label);
        model.addAttribute("productId", pid);
        return "domain/label/deleteProduct";
    }

    /**
     * Performs the delete action and redirect to the search view.
     */
    @RequestMapping(value = "deleteProduct", method = {PUT, POST})
    public String deleteProduct(@RequestParam(value = "productId", required = true) Long productId,
                         @RequestParam(value = "labelId", required = true) Integer labelId) {
        ProductList productListTmp = productListRepository.findByProductId(productId);
        Label labelTmp = labelRepository.findByLabelId(labelId);
        if (labelTmp.getProducts().contains(productListTmp)) {
            labelTmp.getProducts().remove(productListTmp);
            productListTmp.getLabels().remove(labelTmp);
        }
        labelRepository.save(labelTmp);
        return "redirect:/domain/label/show?lid=" + labelId;
    }


    /**
     * Serves the delete form asking the user if the entity should be really
     * deleted.
     */
    @RequestMapping(value = "delete/{lid}", method = GET)
    public String delete(@PathVariable("lid") Integer lid,
                         Model model) {
        Label label = labelRepository.findByLabelId(lid);
        model.addAttribute("label", label);
        return "domain/label/delete";
    }

    /**
     * Performs the delete action and redirect to the search view.
     */
    @RequestMapping(value = "delete", method = {PUT, POST})
    public String delete(@RequestParam(value = "labelId", required = true) Integer labelId) {
        Label labelTmp = labelRepository.findByLabelId(labelId);
        labelRepository.delete(labelTmp);
        return "redirect:/domain/label/search";
    }
}