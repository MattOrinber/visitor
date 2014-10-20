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

import org.visitor.appportal.domain.ProductOperation;
import org.visitor.appportal.repository.ProductOperationRepository;

@Controller
@RequestMapping("/domain/productoperation/")
public class ProductOperationControllerWithPathVariable {
    @Autowired
    private ProductOperationRepository productOperationRepository;

    /**
     * This method is invoked by Spring MVC before the handler methods.
     * <p>
     * The path variable is converted by SpringMVC to a ProductOperation via the {@link ProductOperationFormatter}.
     * Before being passed as an argument to the handler, SpringMVC binds the attributes on the resulting model,
     * then each handler method may receive the entity, potentially modified, as an argument.
     */
    @ModelAttribute
    public ProductOperation getProductOperation(@PathVariable("pk") Long pk) {
        return productOperationRepository.findOne(pk);
    }

    /**
     * Serves the show view for the entity.
     */
    @RequestMapping("show/{pk}")
    public String show(@ModelAttribute ProductOperation productOperation) {
        return "domain/productoperation/show";
    }

    /**
     * Serves the update form view.
     */
    @RequestMapping(value = "update/{pk}", method = GET)
    public String update() {
        return "domain/productoperation/update";
    }

    /**
     * Performs the update action and redirect to the show view.
     */
    @RequestMapping(value = "update/{pk}", method = { PUT, POST })
    public String update(@Valid @ModelAttribute ProductOperation productOperation, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return update();
        }

        productOperationRepository.save(productOperation);
        return "redirect:/domain/productoperation/show/" + productOperation.getPrimaryKey();
    }

    /**
     * Serves the delete form asking the user if the entity should be really deleted.
     */
    @RequestMapping(value = "delete/{pk}", method = GET)
    public String delete() {
        return "domain/productoperation/delete";
    }

    /**
     * Performs the delete action and redirect to the search view.
     */
    @RequestMapping(value = "delete/{pk}", method = { PUT, POST, DELETE })
    public String delete(@ModelAttribute ProductOperation productOperation) {
        productOperationRepository.delete(productOperation);
        return "redirect:/domain/productoperation/search";
    }

}