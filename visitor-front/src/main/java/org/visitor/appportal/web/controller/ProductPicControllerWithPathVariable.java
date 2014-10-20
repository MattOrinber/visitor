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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.visitor.appportal.domain.ProductPic;
import org.visitor.appportal.redis.ProductRedisRepository;
import org.visitor.appportal.repository.ProductPicRepository;
import org.visitor.appportal.service.ProductService;
import org.visitor.appportal.service.SystemPreference;

@Controller
@RequestMapping("/domain/productpic/")
public class ProductPicControllerWithPathVariable {
    @Autowired
    private ProductPicRepository productPicRepository;
    @Autowired
	private ProductService productPicService;
    @Autowired
	private ProductRedisRepository productRedisRepository;
	@Autowired
	private SystemPreference systemPreference;
    /**
     * This method is invoked by Spring MVC before the handler methods.
     * <p>
     * The path variable is converted by SpringMVC to a ProductPic via the {@link ProductPicFormatter}.
     * Before being passed as an argument to the handler, SpringMVC binds the attributes on the resulting model,
     * then each handler method may receive the entity, potentially modified, as an argument.
     */
    @ModelAttribute
    public ProductPic getProductPic(@PathVariable("pk") Long pk) {
        return productPicRepository.findOne(pk);
    }
    /**
     * 将截图设为封面图
     * @author mengw
     *
     * @param productPic
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "tocover/{pk}", method = { GET })
    public String toCover(@ModelAttribute ProductPic productPic) {
    	productPicService.toCover(productPic);
        productRedisRepository.setProductPics(productPic.getProductId(),
				productPicRepository.findByProductIdAndPicType(productPic.getProductId(), ProductPic.PRINT_SCREEN));
        return "redirect:/domain/productlist/show/" + productPic.getProductId();
    }
    /**
     * Serves the show view for the entity.
     */
    @RequestMapping("show/{pk}")
    public String show(@ModelAttribute ProductPic productPic) {
        return "domain/productpic/show";
    }

    /**
     * Serves the update form view.
     */
    @RequestMapping(value = "update/{pk}", method = GET)
    public String update() {
        return "domain/productpic/update";
    }

    /**
     * Performs the update action and redirect to the show view.
     */
    @RequestMapping(value = "update/{pk}", method = { PUT, POST })
    public String update(@Valid @ModelAttribute ProductPic productPic, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return update();
        }

        productPicRepository.save(productPic);
        return "redirect:/domain/productpic/show/" + productPic.getPrimaryKey();
    }

    /**
     * Serves the delete form asking the user if the entity should be really deleted.
     */
    @RequestMapping(value = "delete/{pk}", method = GET)
    public String delete(Model model) {
    	model.addAttribute("picDomain", systemPreference.getPictureDomain());
        return "domain/productpic/delete";
    }

    /**
     * Performs the delete action and redirect to the search view.
     */
    @RequestMapping(value = "delete/{pk}", method = { PUT, POST, DELETE })
    public String delete(@ModelAttribute ProductPic productPic) {
        productPicRepository.delete(productPic);
        productRedisRepository.setProductPics(productPic.getProductId(), productPicRepository.findByProductIdAndPicType(productPic.getProductId(), ProductPic.PRINT_SCREEN));
        return "redirect:/domain/productlist/show/" + productPic.getProductId();
    }

}