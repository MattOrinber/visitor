package org.visitor.appportal.service.ios;

import org.springframework.stereotype.Service;

import org.visitor.appportal.service.site.TemplateService;
import org.visitor.appportal.web.utils.SiteUtil;

@Service
public class IosTemplateService extends TemplateService {

	@Override
	public Integer getSiteId() {
		return SiteUtil.IDEnum.IOS.getValue();
	}

}
