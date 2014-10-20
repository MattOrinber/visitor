package org.visitor.appportal.service.ios;

import org.springframework.stereotype.Service;

import org.visitor.appportal.service.site.AdvertiseService;
import org.visitor.appportal.web.utils.SiteUtil;

@Service
public class IosAdvertiseService extends AdvertiseService {

	@Override
	public Integer getSiteId() {
		return SiteUtil.IDEnum.IOS.getValue();
	}

}
