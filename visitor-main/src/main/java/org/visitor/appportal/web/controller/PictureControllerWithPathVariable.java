/*

 * Template pack-mvc-3:src/main/java/web/controller/controllerwithPathVariable.e.vm.java
 */
package org.visitor.appportal.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.visitor.appportal.domain.Advertise;
import org.visitor.appportal.domain.Folder;
import org.visitor.appportal.domain.Picture;
import org.visitor.appportal.redis.SiteRedisRepository;
import org.visitor.appportal.repository.AdvertiseRepository;
import org.visitor.appportal.repository.FolderRepository;
import org.visitor.appportal.repository.PictureRepository;
import org.visitor.appportal.service.SystemPreference;

@Controller
@RequestMapping("/domain/picture/")
public class PictureControllerWithPathVariable {
	@Autowired
	private SiteRedisRepository siteRedisRepository;
    @Autowired
    private PictureRepository pictureRepository;
    @Autowired
    private AdvertiseRepository advertiseRepository;
    @Autowired
    private FolderRepository folderRepository;
	@Autowired
	private SystemPreference systemPreference;

    /**
     * This method is invoked by Spring MVC before the handler methods.
     * <p>
     * The path variable is converted by SpringMVC to a Picture via the {@link PictureFormatter}.
     * Before being passed as an argument to the handler, SpringMVC binds the attributes on the resulting model,
     * then each handler method may receive the entity, potentially modified, as an argument.
     */
    @ModelAttribute
    public Picture getPicture(@PathVariable("pk") Long pk) {
        return this.pictureRepository.findOne(pk);
    }

    /**
     * Serves the show view for the entity.
     */
    @RequestMapping("show/{pk}")
    public String show(@ModelAttribute Picture picture, Model model) {
    	model.addAttribute("picDomain", systemPreference.getPictureDomain());
        return "domain/picture/show";
    }

    /**
     * Serves the update form view.
     */
    @RequestMapping(value = "update/{pk}", method = GET)
    public String update() {
        return "domain/picture/update";
    }

    /**
     * Performs the update action and redirect to the show view.
     */
    @RequestMapping(value = "update/{pk}", method = { PUT, POST })
    public String update(@Valid @ModelAttribute Picture picture, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return update();
        }

        pictureRepository.save(picture);
        return "redirect:/domain/picture/show/" + picture.getPrimaryKey();
    }

    /**
     * Serves the delete form asking the user if the entity should be really deleted.
     */
    @RequestMapping(value = "delete/{pk}", method = GET)
    public String delete(@ModelAttribute Picture picture, Model model) {
    	List<Advertise> advertises= advertiseRepository.findByPictureId(picture.getPictureId());
    	List<Folder> folders = folderRepository.findByPicId(picture.getPictureId());
    	
    	model.addAttribute("advertises", advertises);
    	model.addAttribute("folders", folders);
    	
        return "domain/picture/delete";
    }

    /**
     * Performs the delete action and redirect to the search view.
     */
    @RequestMapping(value = "delete/{pk}", method = { PUT, POST, DELETE })
    public String delete(@ModelAttribute Picture picture) {
        pictureRepository.delete(picture);
        return "redirect:/domain/picture/create";
    }

    /**
     * publish template
     */
    @RequestMapping(value = "publish/{pk}")
	public String publish(@ModelAttribute Picture picture) {
    	siteRedisRepository.setPicture(picture);
		//清除图片的缓存
		List<String> picIds = new ArrayList<String>();
		picIds.add(String.valueOf(picture.getPictureId()));
		siteRedisRepository.sendCacheElementMessage(Picture.class.getName(), picIds);
    	
		return "redirect:/domain/picture/show/" + picture.getPrimaryKey(); //
	}
}