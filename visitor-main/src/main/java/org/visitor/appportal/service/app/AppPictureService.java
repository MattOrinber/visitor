package org.visitor.appportal.service.app;

import org.springframework.stereotype.Service;

import org.visitor.appportal.service.site.PictureService;
import org.visitor.appportal.web.utils.SiteUtil;

@Service
public class AppPictureService extends PictureService {
	
	public AppPictureService(){
		//site = this.siteRepository.findOne(13);
	}
	
	public Integer getSiteId(){
		return SiteUtil.IDEnum.ANDROID.getValue();
	}

}
