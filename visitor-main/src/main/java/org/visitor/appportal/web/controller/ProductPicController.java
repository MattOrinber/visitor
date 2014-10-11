/*

 * Template pack-mvc-3:src/main/java/web/controller/controller.e.vm.java
 */
package org.visitor.appportal.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.validation.Valid;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import org.visitor.appportal.context.AccountContext;
import org.visitor.appportal.domain.ProductPic;
import org.visitor.appportal.redis.ProductRedisRepository;
import org.visitor.appportal.repository.ProductPicRepository;
import org.visitor.appportal.service.ProductService;
import org.visitor.appportal.service.SystemPreference;
import org.visitor.util.AppStringUtils;

@Controller
@RequestMapping("/domain/productpic/")
public class ProductPicController {

	@Autowired
	private ProductPicRepository productPicRepository;
	@Autowired
	private SystemPreference systemPreference;
	@Autowired
	private ProductRedisRepository productRedisRepository;
    @Autowired
	private ProductService productService;
    
    @InitBinder
    public void initDataBinder(WebDataBinder binder){
        // you can explicitly allow certain fields or exclude them
        // binder.setAllowedFields(new String[]{"resourceAudio.resourceid", "resourceAudio.resourceName", "resourceAudio.userid", "resourceAudio.status", "resourceAudio.auditStatus", "resourceAudio.uploadBy", "resourceAudio.lastoperBy", "resourceAudio.createDt", "resourceAudio.lastoperDt", "resourceAudio.dlCount", "resourceAudio.author", "resourceAudio.audioProperty", "resourceAudio.recordCorp", "resourceAudio.version", "resourceAudio.language", "resourceAudio.region", "resourceAudio.rhythm", "resourceAudio.musicType", "resourceAudio.musicSuit", "resourceAudio.singer", "resourceAudio.lyricsAuthor", "resourceAudio.albumName", "resourceAudio.wayOperate", "resourceAudio.resourceSize", "resourceAudio.resourceFormat", "resourceAudio.audioSource", "resourceAudio.intro", "resourceAudio.tag", "resourceAudio.ifIndexed", "resourceAudio.fileCount", "resourceAudio.resourceStatus"});
        binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }
    
	@RequestMapping(value = { "list", "" })
	public String list(@ModelAttribute ProductPicSearchForm productPicSearchForm, Model model) {
		model.addAttribute("productPicsCount", productPicRepository.findCount(productPicSearchForm.getProductPic(),
				productPicSearchForm.toSearchTemplate()));
		model.addAttribute("productPics", productPicRepository.find(productPicSearchForm.getProductPic(),
				productPicSearchForm.toSearchTemplate()));
		return "domain/productpic/list";
	}
	/**
	 * 更换图标或封面图
	 * 
	 * @author mengw
	 * 
	 * @param productPic
	 * @return
	 */
	@RequestMapping(value = "replace", method = GET)
	public String replace(@ModelAttribute ProductPicSearchForm productPicSearchForm,//
			@RequestParam(value = "ppId", required = true) Long ppId) {
		productPicSearchForm.setPpId(ppId);
		ProductPic pp = productPicRepository.findOne(ppId);
		productPicSearchForm.setProId(pp.getProductId());
		return "domain/productpic/replace";
	}
	/**
	 * 保存更换的图片
	 * 
	 * @author mengw
	 * 
	 * @param productPic
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "replace", method = { POST, PUT })
	public String replace(@ModelAttribute ProductPicSearchForm productPicSearchForm,
			@RequestParam(value = "ppId", required = true) Long ppId,//
			@RequestParam("replace") MultipartFile picFile, Model model) {
		ProductPic productPic = productPicRepository.findOne(ppId);
		ProductPic newPic = getProductPic(picFile, ProductPic.COVER);
		productPic.setCreateBy(newPic.getCreateBy());
		productPic.setCreateDate(newPic.getCreateDate());
		productPic.setPicPixels(newPic.getPicPixels());
		productPic.setPicSize(newPic.getPicSize());
		productPic.setPicStyle(newPic.getPicStyle());
		productPic.setJarMd5(newPic.getJarMd5());
		
		productService.saveProductPic(productPic.getProductId(), productPic);
		
		// 保存图片对象
		String picRootPath = systemPreference.getProductPicRoot();
		File outputFile = new File(picRootPath, productPic.getPicPath());
		if (!outputFile.getParentFile().exists()) {
			outputFile.getParentFile().mkdirs();
		}
		try {
			picFile.transferTo(outputFile);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "redirect:/domain/productlist/show/" + productPic.getProductId();
	}

	/**
	 * 添加截图页面
	 * 
	 * @author mengw
	 * 
	 * @param productPic
	 * @return
	 */
	@RequestMapping(value = "createscr", method = GET)
	public String createScr(@ModelAttribute ProductPicSearchForm productPicSearchForm,
			@RequestParam(value = "proId", required = true) Long proId) {
		productPicSearchForm.setProId(proId);
		return "domain/productpic/createscr";
	}

	/**
	 * 保存截图
	 * 
	 * @author mengw
	 * 
	 * @param productPic
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "createscr", method = { POST, PUT })
	public String createScr(@ModelAttribute ProductPicSearchForm productPicSearchForm,
			@RequestParam(value = "proId", required = true) Long proId,//
			@RequestParam("screen") MultipartFile picFile, Model model) {
		
		ProductPic iconPic = getProductPic(picFile, ProductPic.PRINT_SCREEN);
		productService.saveProductPic(proId, iconPic);
		//更新redis
		productRedisRepository.setProductPics(proId,
				productPicRepository.findByProductIdAndPicType(proId, ProductPic.PRINT_SCREEN));
		
		// 保存图片对象
		String picRootPath = systemPreference.getProductPicRoot();
		File outputFile = new File(picRootPath, iconPic.getPicPath());
		if (!outputFile.getParentFile().exists()) {
			outputFile.getParentFile().mkdirs();
		}
		try {
			picFile.transferTo(outputFile);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "redirect:/domain/productlist/show/" + proId;
	}

	public static ProductPic getProductPic(MultipartFile multipartFile, Integer picType) {
		ProductPic pp = new ProductPic();
		pp.setCreateBy(AccountContext.getAccountContext().getUsername());
		pp.setCreateDate(new Date());
		pp.setStatus(ProductPic.ENABLE);
		pp.setPicType(picType);
		try {
			BufferedImage image = ImageIO.read(multipartFile.getInputStream());
			pp.setPicPath("/");
			pp.setPicSize(AppStringUtils.byteCountToDisplaySize(multipartFile.getSize()));
			pp.setPicStyle(FilenameUtils.getExtension(multipartFile.getOriginalFilename()));
			pp.setPicPixels(image.getWidth() + "*" + image.getHeight());
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pp;
	}
	
	/**
	 * Serves the create form.
	 */
	@Deprecated
	@RequestMapping(value = "create", method = GET)
	public String create(@ModelAttribute ProductPic productPic) {
		return "domain/productpic/create";
	}

	/**
	 * Performs the create action and redirect to the show view.
	 */
	@Deprecated
	@RequestMapping(value = "create", method = { POST, PUT })
	public String create(@Valid @ModelAttribute ProductPic productPic, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return create(productPic);
		}

		productPicRepository.save(productPic);
		return "redirect:/domain/productpic/show/" + productPic.getPrimaryKey();
	}

	/**
	 * Serves search by example form, search by pattern form and search by named
	 * query form.
	 */
	@RequestMapping(value = "*", method = GET)
	public void catchAll(@ModelAttribute ProductPicSearchForm productPicSearchForm) {
	}

}