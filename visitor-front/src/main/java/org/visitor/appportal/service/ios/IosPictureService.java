package org.visitor.appportal.service.ios;

import org.springframework.stereotype.Service;

import org.visitor.appportal.service.site.PictureService;
import org.visitor.appportal.web.utils.SiteUtil;

@Service
public class IosPictureService extends PictureService {

	@Override
	public Integer getSiteId() {
		return SiteUtil.IDEnum.IOS.getValue();
	}

}
