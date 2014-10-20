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

import org.visitor.app.portal.model.FolderRank.RankTypeEnum;
import org.visitor.appportal.domain.ProductState;
import org.visitor.appportal.redis.ProductRedisRepository;
import org.visitor.appportal.repository.ProductStateRepository;

@Controller
@RequestMapping("/domain/productstate/")
public class ProductStateControllerWithPathVariable {
    @Autowired
    private ProductStateRepository productStateRepository;
    
    @Autowired
    private ProductRedisRepository productRedisRepository;

    /**
     * This method is invoked by Spring MVC before the handler methods.
     * <p>
     * The path variable is converted by SpringMVC to a ProductState via the {@link ProductStateFormatter}.
     * Before being passed as an argument to the handler, SpringMVC binds the attributes on the resulting model,
     * then each handler method may receive the entity, potentially modified, as an argument.
     */
    @ModelAttribute
    public ProductState getProductState(@PathVariable("pk") Long pk) {
    	ProductState productState = productStateRepository.findOne(pk);
    	Long total_dl = productRedisRepository.getProductState(String.valueOf(pk), RankTypeEnum.TotalDownload);
    	if (total_dl != null && total_dl > 0) {
        	productState.setTotalDl(productRedisRepository.getProductState(String.valueOf(pk), RankTypeEnum.TotalDownload));  
    	}  	
        return productState;
    }

    /**
     * Serves the show view for the entity.
     */
    @RequestMapping("show/{pk}")
    public String show(@ModelAttribute ProductState productState) {
        return "domain/productstate/show";
    }

    /**
     * Serves the update form view.
     */
    @RequestMapping(value = "update/{pk}", method = GET)
    public String update() {
        return "domain/productstate/update";
    }

    /**
     * Performs the update action and redirect to the show view.
     */
    @RequestMapping(value = "update/{pk}", method = { PUT, POST })
    public String update(@Valid @ModelAttribute ProductState productState, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return update();
        }
        productRedisRepository.setProductState(String.valueOf(productState.getProductId()),
        		RankTypeEnum.TotalDownload, productState.getTotalDl());

        productStateRepository.save(productState);
        return "redirect:/domain/productlist/show/" + productState.getProductId();
    }

    /**
     * Serves the delete form asking the user if the entity should be really deleted.
     */
    @RequestMapping(value = "delete/{pk}", method = GET)
    public String delete() {
        return "domain/productstate/delete";
    }

    /**
     * Performs the delete action and redirect to the search view.
     */
    @RequestMapping(value = "delete/{pk}", method = { PUT, POST, DELETE })
    public String delete(@ModelAttribute ProductState productState) {
        productStateRepository.delete(productState);
        return "redirect:/domain/productstate/search";
    }

}