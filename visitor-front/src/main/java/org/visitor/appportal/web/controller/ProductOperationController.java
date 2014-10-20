/*

 * Template pack-mvc-3:src/main/java/web/controller/controller.e.vm.java
 */
package org.visitor.appportal.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import org.visitor.appportal.domain.ProductOperation;
import org.visitor.appportal.repository.ProductOperationRepository;

@Controller
@RequestMapping("/domain/productoperation/")
public class ProductOperationController {

    @Autowired
    private ProductOperationRepository productOperationRepository;
    
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
    public String list(@ModelAttribute ProductOperationSearchForm productOperationSearchForm, Model model) {
    	model.addAttribute("productOperationsCount", productOperationRepository.findCount(productOperationSearchForm.getProductOperation(), productOperationSearchForm.toSearchTemplate()));
        model.addAttribute("productOperations", productOperationRepository.find(productOperationSearchForm.getProductOperation(), productOperationSearchForm.toSearchTemplate()));
        return "domain/productoperation/list";
    }

    @RequestMapping(value = { "listcreate" })
    public String listCreate(@ModelAttribute ProductOperationSearchForm productOperationSearchForm, Model model) {
    	ProductOperation operation = productOperationSearchForm.getProductOperation();
    	Calendar calendar = Calendar.getInstance();
    	DateTime date = new DateTime(calendar.get(Calendar.YEAR) - 1900, 
    			calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) - 1, 0, 0, 0, 0);
    	productOperationSearchForm.getCreateDateRange().setFromDate(date.toDate());
    	operation.setCreateDate(date.toDate());
    	
    	operation.setType(ProductOperation.OperationTypeEnum.Create.ordinal());
    	Sort sort = new Sort(new Order(Sort.Direction.ASC, "productId"), new Order(Sort.Direction.DESC, "createDate"));
    	Pageable pageable = productOperationSearchForm.getPageable(sort);
    	Page<ProductOperation> pager = productOperationRepository.findAll(productOperationSearchForm.toSpecification(), pageable);
       	model.addAttribute("productOperationsCount", pager.getTotalElements());
        model.addAttribute("productOperations", pager.getContent());
        model.addAttribute("totalPages", pager.getTotalPages());
        return "domain/productoperation/list";
    }
    
    @RequestMapping(value = { "listupdate" })
    public String listUpdate(@ModelAttribute ProductOperationSearchForm productOperationSearchForm, Model model) {
    	ProductOperation operation = productOperationSearchForm.getProductOperation();
    	Calendar calendar = Calendar.getInstance();
    	DateTime date = new DateTime(calendar.get(Calendar.YEAR) - 1900, 
    			calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH) - 1, 0, 0, 0, 0);
    	productOperationSearchForm.getCreateDateRange().setFromDate(date.toDate());
    	operation.setCreateDate(date.toDate());
    	
    	operation.setType(100);
    	Sort sort = new Sort(new Order(Sort.Direction.ASC, "productId"), new Order(Sort.Direction.DESC, "createDate"));
    	Pageable pageable = productOperationSearchForm.getPageable(sort);
    	Page<ProductOperation> pager = productOperationRepository.findAll(productOperationSearchForm.toSpecification(), pageable);
       	model.addAttribute("productOperationsCount", pager.getTotalElements());
        model.addAttribute("productOperations", pager.getContent());
        model.addAttribute("totalPages", pager.getTotalPages());
       return "domain/productoperation/list";
    }

    /**
     * Serves search by example form, search by pattern form and search by named query form.
     */
    @RequestMapping(value = "*", method = GET)
    public void catchAll(@ModelAttribute ProductOperationSearchForm productOperationSearchForm) {
    }

}