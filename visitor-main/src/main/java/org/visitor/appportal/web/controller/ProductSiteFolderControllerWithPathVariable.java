/*

 * Template pack-mvc-3:src/main/java/web/controller/controllerwithPathVariable.e.vm.java
 */
package org.visitor.appportal.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.visitor.appportal.domain.ProductSiteFolder;
import org.visitor.appportal.domain.ProductSiteFolderPk;
import org.visitor.appportal.redis.ProductRedisRepository;
import org.visitor.appportal.repository.ProductSiteFolderRepository;
import org.visitor.appportal.service.ProductService;

@Controller
@RequestMapping("/domain/productsitefolder/")
public class ProductSiteFolderControllerWithPathVariable {
    @Autowired
    private ProductSiteFolderRepository productSiteFolderRepository;
    @Autowired
	private ProductService productService;
    @Autowired
    private ProductRedisRepository productRedisRepository;

    /**
     * This method is invoked by Spring MVC before the handler methods.
     * <p>
     * The path variable is converted by SpringMVC to a ProductSiteFolder via the {@link ProductSiteFolderFormatter}.
     * Before being passed as an argument to the handler, SpringMVC binds the attributes on the resulting model,
     * then each handler method may receive the entity, potentially modified, as an argument.
     */
    @ModelAttribute
    public ProductSiteFolder getProductSiteFolder(@PathVariable("pk") String pk) {
    	String[] pks = pk.split(":");
        return productSiteFolderRepository.findOne(new ProductSiteFolderPk(Long.valueOf(pks[0]),Integer.valueOf(pks[1])));
    }

    /**
     * Serves the delete form asking the user if the entity should be really deleted.
     */
    @RequestMapping(value = "delete/{pk}", method = GET)
    public String delete() {
        return "domain/productsitefolder/delete";
    }

    /**
     * Performs the delete action and redirect to the search view.
     */
    @RequestMapping(value = "delete/{pk}", method = { PUT, POST, DELETE })
    public String delete(@ModelAttribute ProductSiteFolder productSiteFolder) {
        productSiteFolderRepository.delete(productSiteFolder);
        productService.deleteFolderProduct(productSiteFolder);
        productService.setProduct2Redis(productSiteFolder.getProduct());
        return "redirect:/domain/productlist/show/"+productSiteFolder.getProductId();
    }
    
    /**
     * Serves the delete form asking the user if the entity should be really deleted.
     */
    @RequestMapping(value = "delete1/{pk}", method = GET)
    public String delete1() {
        return "domain/productsitefolder/delete1";
    }

    /**
     * Performs the delete action and redirect to the search view.
     */
    @RequestMapping(value = "delete1/{pk}", method = { PUT, POST, DELETE })
    public String delete1(@ModelAttribute ProductSiteFolder productSiteFolder) {
    	productSiteFolderRepository.delete(productSiteFolder);
    	productService.deleteFolderProduct(productSiteFolder);
    	productService.setProduct2Redis(productSiteFolder.getProduct());
        return "redirect:/domain/folder/show/"+productSiteFolder.getFolderId();
    }
    
    /**
     * Serves the update form view.
     */
    @RequestMapping(value = "update/{pk}", method = GET)
    public String update() {
        return "domain/productsitefolder/update";
    }

    /**
     * Performs the update action and redirect to the show view.
     */
    @RequestMapping(value = "update/{pk}", method = { PUT, POST })
    public String update(@Valid @ModelAttribute ProductSiteFolder productSiteFolder, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return update();
        }
        productSiteFolderRepository.save(productSiteFolder);
        productRedisRepository.updateProductSiteFolder(productSiteFolder);
        
        return "redirect:/domain/productlist/show/" + productSiteFolder.getProductId();
    }
}