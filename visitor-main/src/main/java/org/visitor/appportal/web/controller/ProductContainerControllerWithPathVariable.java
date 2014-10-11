/*

 * Template pack-mvc-3:src/main/java/web/controller/controllerwithPathVariable.e.vm.java
 */
package org.visitor.appportal.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.visitor.appportal.context.AccountContext;
import org.visitor.appportal.domain.ProductContainer;
import org.visitor.appportal.redis.ProductRedisRepository;
import org.visitor.appportal.repository.ProductContainerRepository;

@Controller
@RequestMapping("/domain/productcontainer/")
public class ProductContainerControllerWithPathVariable {
	@Autowired
	private ProductContainerRepository productContainerRepository;
	@Autowired
	private ProductRedisRepository productRedisRepository;
	

    @InitBinder
    public void initDataBinder(WebDataBinder binder){
        // you can explicitly allow certain fields or exclude them
        // binder.setAllowedFields(new String[]{"resourceAudio.resourceid", "resourceAudio.resourceName", "resourceAudio.userid", "resourceAudio.status", "resourceAudio.auditStatus", "resourceAudio.uploadBy", "resourceAudio.lastoperBy", "resourceAudio.createDt", "resourceAudio.lastoperDt", "resourceAudio.dlCount", "resourceAudio.author", "resourceAudio.audioProperty", "resourceAudio.recordCorp", "resourceAudio.version", "resourceAudio.language", "resourceAudio.region", "resourceAudio.rhythm", "resourceAudio.musicType", "resourceAudio.musicSuit", "resourceAudio.singer", "resourceAudio.lyricsAuthor", "resourceAudio.albumName", "resourceAudio.wayOperate", "resourceAudio.resourceSize", "resourceAudio.resourceFormat", "resourceAudio.audioSource", "resourceAudio.intro", "resourceAudio.tag", "resourceAudio.ifIndexed", "resourceAudio.fileCount", "resourceAudio.resourceStatus"});
        binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }

	/**
	 * This method is invoked by Spring MVC before the handler methods.
	 * <p>
	 * The path variable is converted by SpringMVC to a ProductContainer via the
	 * {@link ProductContainerFormatter}. Before being passed as an argument to
	 * the handler, SpringMVC binds the attributes on the resulting model, then
	 * each handler method may receive the entity, potentially modified, as an
	 * argument.
	 */
	@ModelAttribute
	public ProductContainer getProductContainer(@PathVariable("pk") Long pk) {
		return this.productContainerRepository.findOne(pk);
	}


	/**
	 * Serves the update form view.
	 */
	@RequestMapping(value = "update/{pk}", method = GET)
	public String update() {
		return "domain/productcontainer/update";
	}

	/**
	 * Performs the update action and redirect to the show view.
	 */
	@RequestMapping(value = "update/{pk}", method = { PUT, POST })
	public String update(@Valid @ModelAttribute ProductContainer productContainer, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return update();
		}

		//错误处理
    	//0 日期
    	Date startDate = productContainer.getStartDate();
		Date endDate = productContainer.getEndDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
		if(startDate != null && endDate != null && endDate.before(startDate)){
			bindingResult.rejectValue("startDate", "productcontainer.date.invalid", new Object[]{startDate,endDate}, 
					"结束日期：" + sdf.format(endDate) + "不能早于开始日期" + sdf.format(startDate));//.addError(new ObjectError("errors", "该推荐已存在"));
			return update();
		}
		
		Date date = new Date();
		productContainer.setModDate(date);
		productContainer.setModBy(AccountContext.getAccountContext().getUsername());
		productContainerRepository.save(productContainer);
		
		int type = productContainer.getType();
		
		//根据不同的类型，跳转到不同的页面
		if(type == ProductContainer.TypeEnum.Product.getValue()){
			return "redirect:/domain/productlist/show/" + productContainer.getProductId();
		}else if(type == ProductContainer.TypeEnum.Folder.getValue()){
			return "redirect:/domain/folder/show/" + productContainer.getTfolderId();
		}else {
			return "redirect:/domain/advertise/show/" + productContainer.getAdvertiseId();
		}
		
	}

	/**
	 * Serves the delete form asking the user if the entity should be really
	 * deleted.
	 */
	@RequestMapping(value = "delete/{pk}", method = GET)
	public String delete() {
		return "domain/productcontainer/delete";
	}

	/**
	 * Performs the delete action and redirect to the search view.
	 */
	@RequestMapping(value = "delete/{pk}", method = { PUT, POST, DELETE })
	public String delete(@ModelAttribute ProductContainer productContainer) {
		Long container_id = productContainer.getContainerId();
		//删除执行两种操作，即删除产品推荐，及关系数据库里的数据
		productRedisRepository.deleteProductRecommand(productContainer);
		productRedisRepository.deleteProductIdInContainer(null, productContainer.getProductContainerId());
		productContainerRepository.delete(productContainer);
		return "redirect:/domain/recommandcontainer/show/" + container_id;
	}

}