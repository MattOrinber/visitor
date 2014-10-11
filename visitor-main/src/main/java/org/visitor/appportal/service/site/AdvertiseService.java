package org.visitor.appportal.service.site;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import org.visitor.appportal.context.AccountContext;
import org.visitor.appportal.domain.Advertise;
import org.visitor.appportal.domain.Folder;
import org.visitor.appportal.domain.Picture;
import org.visitor.appportal.domain.ProductList;
import org.visitor.appportal.domain.ProductPic;
import org.visitor.appportal.domain.Picture.PictureType;
import org.visitor.appportal.repository.AdvertiseRepository;
import org.visitor.appportal.repository.FolderRepository;
import org.visitor.appportal.repository.PictureRepository;
import org.visitor.appportal.repository.ProductListRepository;
import org.visitor.appportal.repository.ProductPicRepository;
import org.visitor.appportal.service.SystemPreference;
import org.visitor.appportal.web.controller.AdvertiseSearchForm;
import org.visitor.appportal.web.utils.ImageUtils;

public abstract class AdvertiseService extends SiteService {

	@Autowired
    private AdvertiseRepository advertiseRepository;
	@Autowired
	private PictureRepository pictureRepository;
	@Autowired
	private FolderRepository folderRepository;
	@Autowired
	private ProductListRepository productListRepository;
	@Autowired
	SystemPreference systemPreference;
	@Autowired
	private ProductPicRepository productPicRepository;

	public void list(AdvertiseSearchForm advertiseSearchForm, Model model) {
		// TODO Auto-generated method stub
		Advertise example = advertiseSearchForm.getAdvertise();//.getFolder();
		
		if (example.getAdvertiseId() != null) {
			final Advertise o = advertiseRepository.findByAdvertiseIdAndSiteId(example.getAdvertiseId(),getSiteId());
			model.addAttribute("advertiseCount", o != null ? 1 : 0);
			ArrayList<Advertise> list = new ArrayList<Advertise>();
			list.add(o);
			model.addAttribute("advertises", list);
		} else {
			
			example.setSiteId(getSiteId());
			
	    	model.addAttribute("advertiseCount", advertiseRepository.findCount(example, advertiseSearchForm.toSearchTemplate()));
	        model.addAttribute("advertises", advertiseRepository.find(example, advertiseSearchForm.toSearchTemplate()));
		}
	}

	/**
	 * 创建广告，需要增加对频道和产品是否为当前站点下的判断
	 * @param advertise
	 * @param bindingResult
	 * @param pictureSize
	 * @param waterMarkIds
	 * @param model
	 * @return
	 */
	public ResultEnum create(Advertise advertise, BindingResult bindingResult,
			Integer pictureSize, List<String> waterMarkIds, Model model) {
		// TODO Auto-generated method stub
		
		if (bindingResult.hasErrors()) {
			return ResultEnum.ERROR;
		}
		
		//广告图(验证)
		if(pictureSize != null && pictureSize == 0){
			if(advertise.getPictureId()==null){
				bindingResult.addError(new FieldError("advertise", "pictureId", "请选择广告图"));
				return ResultEnum.ERROR;
			} else {
				
				Picture p =  pictureRepository.findOne(advertise.getPictureId());
				
				if(p==null){
					bindingResult.addError(new FieldError("advertise", "pictureId", "广告图不存在"));
					return ResultEnum.ERROR;
				} else {
					advertise.setPicture(p);
				}
			}
		}
		
		//合作URL(验证)
		if(advertise.getType() == Advertise.AdvertiseTypeEnum.ExtUrl.ordinal()){
			if(advertise.getUrl()==null){
				bindingResult.addError(new FieldError("advertise", "url", "必须输入"));
				return ResultEnum.ERROR;
			}
		}
		
		//频道广告(验证)
		if(advertise.getType() == Advertise.AdvertiseTypeEnum.Folder.ordinal()){
			//验证频道
			if(advertise.getFolderId()==null){
				bindingResult.addError(new FieldError("advertise", "folderId", "必须输入"));
				return ResultEnum.ERROR;
			}else {
				/*根据当前站点和频道ID取出频道信息，放入广告类中*/
				Folder folder=folderRepository.findByFolderIdAndSiteId(advertise.getFolderId(),getSiteId());
				if(folder==null){
					bindingResult.addError(new FieldError("advertise", "folderId", "请输入当前站点下有效的频道ID"));
					return ResultEnum.ERROR;
				}else {
					advertise.setFolder(folder);
				}
			}
		}
		
		//产品广告(验证)
		if(advertise.getType() == Advertise.AdvertiseTypeEnum.Product.ordinal()){
			if(advertise.getProductId()==null){
				bindingResult.addError(new FieldError("advertise", "productId", "必须输入"));
				return ResultEnum.ERROR;
			}else {
				/*根据产品ID取出产品信息，放入广告类中*/
				ProductList productList = productListRepository.findByPidWithSiteId(getSiteId(),advertise.getProductId());
				if(productList==null){
					bindingResult.addError(new FieldError("advertise", "productId", "请输入当前站点下的有效的产品ID"));
					return ResultEnum.ERROR;
				}else {
					advertise.setProduct(productList);
				}
			}
			
			if(advertise.getIconId()!=null){
				Picture icon =  pictureRepository.findOne(advertise.getIconId());
				if(icon==null){
					bindingResult.addError(new FieldError("advertise", "iconId", "图标不存在"));
					return ResultEnum.ERROR;
				} else {
					advertise.setIcon(icon);
				}
			}
		}

		//根据表单的控件，及数据表的字段，可以知道，除了创建人及日期信息，就只有状态信息没有设置了
		//状态信息可以默认为0
		advertise.setStatus(Advertise.AdvertiseStatusEnum.Enable.ordinal());
		
		Date date = new Date();
		advertise.setCreateDate(date);
		advertise.setModDate(date);
		advertise.setModBy(AccountContext.getAccountContext().getUsername());
		advertise.setCreateBy(AccountContext.getAccountContext().getUsername());
		advertise.setSite(getSite());
		
		//创建广告
		if(pictureSize != null && pictureSize.intValue() == 0){ 
			advertiseRepository.save(advertise);
		} else {
			List<Picture> pictures = new ArrayList<Picture>();
			if (waterMarkIds != null && waterMarkIds.size() > 0) {
				for (String waterMarkId : waterMarkIds) {
					pictures.add(pictureRepository.findOne(Long.valueOf(waterMarkId)));
				}
			}
			
			saveProductAdvertiseWithWaterMark(advertise, pictures, pictureSize);
		}
		
		return ResultEnum.OK;
	}

    /**
     * 创建产品广告 及 水印图，从ProductService中移过来
     * 
     * @param advertise
     * @param pictures
     * @param size
     */
    @Transactional
    public void saveProductAdvertiseWithWaterMark(Advertise advertise, List<Picture> pictures, Integer size) {
    	String water_tag = "water";
    	
    	List<ProductPic> procuctPics = productPicRepository.findByProductIdAndPicType(advertise.getProductId(), ProductPic.ICON);
    	ProductPic original_image = procuctPics.get(0);
    	String original_file = systemPreference.getProductOrgPicRoot() + original_image.getPicPath();
    	String new_file = null;
    	String old_file = null;
    	
		Picture picture = new Picture();
    	if (advertise.getAdvertiseId() != null) {
    		String o_path = water_tag + "_" + String.valueOf(advertise.getAdvertiseId());
    		List<Picture> plist = pictureRepository.findByPathLike("%" + o_path + "%");
    		if (plist != null && plist.size() > 0) {
    			picture = plist.get(0);
    		}
    		old_file = systemPreference.getProductOrgPicRoot() + picture.getPath();
    	}

    	String path = "/" + PictureType.cms.getDisplayName() + "/" + water_tag;
    	
    	if (picture == null || picture.getPictureId() == null) {
        	path = path + "." + FilenameUtils.getExtension(original_file);
    		new_file = systemPreference.getProductPicRoot() + path;
    		
           	picture.setPath(path);
            picture.setName(advertise.getName());
            picture.setType(Picture.PictureType.cms.getValue());
            picture.setStatus(Picture.StatusEnum.Enable.ordinal());
            	
            pictureRepository.save(picture);
    	}
    	
        advertise.setPicture(picture);
        advertiseRepository.save(advertise);
        
        //生成 水印图
    	path = "/" + PictureType.cms.getDisplayName() + "/" + water_tag + "_" + String.valueOf(advertise.getAdvertiseId() + "_" + String.valueOf(System.currentTimeMillis()));
    	path = path + "." + FilenameUtils.getExtension(original_file);
    	picture.setPath(path);
    	pictureRepository.save(picture);
    	
		new_file = systemPreference.getProductPicRoot() + path;
		
    	ImageUtils.resize(original_file,new_file, size, size, false);
    	
    	if (pictures != null && pictures.size() > 0) {
    		for (Picture p : pictures) {
    			String water_file = systemPreference.getProductOrgPicRoot() + p.getPath();
    			ImageUtils.pressImage(new_file, water_file, -1, -1, 1.0f);
    		}
    	}
    	
    	if (StringUtils.isNotEmpty(old_file)){
    		File of = new File(old_file);
    		if (of.exists()) {
    			of.delete();
    		}
    	}
    }

	public ResultEnum update(Advertise advertise, BindingResult bindingResult,
			Integer pictureSize, List<String> waterMarkIds, Model model) {
		// TODO Auto-generated method stub
		// 广告图(验证)
		
		if(advertise.getSiteId() != getSiteId().intValue()){
			bindingResult.addError(new FieldError("advertise", "pictureId", "该广告不属于当前站点，请选择本站点广告"));
			return ResultEnum.ERROR;
		}
		
		
		if (pictureSize != null && pictureSize == 0) {
			if (advertise.getPictureId() == null) {
				bindingResult.addError(new FieldError("advertise", "pictureId",
						"请选择广告图"));
				return ResultEnum.ERROR;
			} else {
				Picture p = pictureRepository.findOne(advertise.getPictureId());
				if (p == null) {
					bindingResult.addError(new FieldError("advertise",
							"pictureId", "广告图不存在"));
					return ResultEnum.ERROR;
				} else {
					advertise.setPicture(p);
				}
			}
		}

		// 合作URL(验证)
		if (advertise.getType() == Advertise.AdvertiseTypeEnum.ExtUrl.ordinal()) {
			if (advertise.getUrl() == null) {
				bindingResult.addError(new FieldError("advertise", "url",
						"必须输入"));
				return ResultEnum.ERROR;
			}
		}

		// 频道广告(验证)
		if (advertise.getType() == Advertise.AdvertiseTypeEnum.Folder.ordinal()) {
			// 验证频道
			if (advertise.getFolderId() == null) {
				bindingResult.addError(new FieldError("advertise", "folderId",
						"必须输入"));
				return ResultEnum.ERROR;
			} else {
				/* 根据频道ID取出频道信息，放入广告类中 */
				Folder folder = folderRepository.findByFolderIdAndSiteId(advertise
						.getFolderId(),getSiteId());
				if (folder == null) {
					bindingResult.addError(new FieldError("advertise",
							"folderId", "请输入有效的频道ID（必须在当前站点下）"));
					return ResultEnum.ERROR;
				} else {
					advertise.setFolder(folder);
				}
			}
		}

		// 产品广告(验证)
		if (advertise.getType() == Advertise.AdvertiseTypeEnum.Product
				.ordinal()) {
			if (advertise.getProductId() == null) {
				bindingResult.addError(new FieldError("advertise", "productId",
						"必须输入"));
				return ResultEnum.ERROR;
			} else {
				/* 根据产品ID取出产品信息，放入广告类中 */
				ProductList productList = productListRepository
						.findByPidWithSiteId(getSiteId(),advertise.getProductId());
				if (productList == null) {
					bindingResult.addError(new FieldError("advertise",
							"productId", "请输入有效的产品ID(必须在当前站点下)"));
					return ResultEnum.ERROR;
				} else {
					advertise.setProduct(productList);
				}
			}

			if (advertise.getIconId() != null) {
				Picture icon = pictureRepository.findOne(advertise.getIconId());
				if (icon == null) {
					bindingResult.addError(new FieldError("advertise",
							"iconId", "图标不存在"));
					return ResultEnum.ERROR;
				} else {
					advertise.setIcon(icon);
				}
			}
		}

		// 最后，更新修改人信息及时间
		Date date = new Date();
		advertise.setModDate(date);
		advertise.setModBy(AccountContext.getAccountContext().getUsername());

		// 创建广告
		if (pictureSize != null && pictureSize.intValue() == 0) {
			advertiseRepository.save(advertise);
		} else {
			List<Picture> pictures = new ArrayList<Picture>();
			if (waterMarkIds != null && waterMarkIds.size() > 0) {
				for (String waterMarkId : waterMarkIds) {
					pictures.add(pictureRepository.findOne(Long
							.valueOf(waterMarkId)));
				}
			}
			
			saveProductAdvertiseWithWaterMark(advertise,
					pictures, pictureSize);
		}
		
		return ResultEnum.OK;
	}	
}
