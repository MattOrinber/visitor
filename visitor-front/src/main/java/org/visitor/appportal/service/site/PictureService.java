package org.visitor.appportal.service.site;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import org.visitor.appportal.domain.Picture;
import org.visitor.appportal.domain.Picture.PictureType;
import org.visitor.appportal.repository.PictureRepository;
import org.visitor.appportal.service.SystemPreference;
import org.visitor.appportal.web.controller.PictureSearchForm;
import org.visitor.appportal.web.utils.AutoCompleteResult;

/**
 * 图片处理Service
 * @author mengw
 *
 */
public abstract class PictureService extends SiteService{
	
	@Autowired
	private SystemPreference systemPreference;
    @Autowired
    private PictureRepository pictureRepository;

	/**
	 * 获取图片列表
	 * @param pictureSearchForm
	 * @param request
	 * @param model
	 */
	public void list(PictureSearchForm pictureSearchForm, 
    		HttpServletRequest request, Model model){
		
		pictureSearchForm.getPicture().setSite(getDefaultSite());
		
    	model.addAttribute("picturesCount", pictureRepository.findCount(pictureSearchForm.getPicture(), pictureSearchForm.toSearchTemplate()));
        model.addAttribute("pictures", pictureRepository.find(pictureSearchForm.getPicture(), pictureSearchForm.toSearchTemplate()));
        model.addAttribute("picDomain", systemPreference.getPictureDomain());
	}
	
	/**
	 * 创建图片
	 * @param picture
	 * @param bindingResult
	 * @param picFile
	 * @return
	 */
	public ResultEnum create(Picture picture, BindingResult bindingResult, MultipartFile picFile){
    	
	    if (StringUtils.isNotEmpty(picture.getName())) {
	    	if (!picFile.isEmpty()) {
	        		
	        	String name = FilenameUtils.getBaseName(picFile.getOriginalFilename());
	        	String path = "/" + PictureType.getInstance(picture.getType()).getDisplayName() + "/" + name;
	        		
	        	List<Picture> plist = pictureRepository.findByPathLike(path);
	        	if (plist != null && plist.size() > 0) {
	        		path = path + plist.size();
	        	}
	        		
	        	path = path + "." + FilenameUtils.getExtension(picFile.getOriginalFilename());        		
	        	File file = new File(systemPreference.getProductPicRoot(), path);
	        		
	        	try {
					picFile.transferTo(file);
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
	       		picture.setPath(path);
	       	} else {
	        	bindingResult.rejectValue("path", "picture_path_null", new String[]{}, "没有上传任何文件。");
	        }
	    }
	    	
	    if (bindingResult.hasErrors()) {
	        return ResultEnum.ERROR;
	    }
	        
	    picture.setStatus(Picture.StatusEnum.Enable.ordinal());
	    picture.setSite(getSite());
	        
	    pictureRepository.save(picture);
	        
	    return ResultEnum.OK;
    }

    /**
     * 图片自动选择，只选择当前站点下的图片
     * @param type
     * @param searchPattern
     * @return
     */
    public List<AutoCompleteResult> autocomplete(Integer type, String searchPattern) {
        List<AutoCompleteResult> ret = new ArrayList<AutoCompleteResult>();
        searchPattern = "%" + StringUtils.trim(searchPattern) + "%";
        
        List<Picture> list = this.pictureRepository.findByTypeAndNameLikeAndSiteId(type, searchPattern,getSiteId());
        for (Picture picture : list) {
            String objectPk = picture.getPrimaryKey().toString();
            String objectLabel = picture.getName() + " " + picture.getPath();
            ret.add(new AutoCompleteResult(objectPk, objectLabel));
        }
        return ret;
    }
    
}
