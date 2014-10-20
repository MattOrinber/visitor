package org.visitor.appportal.service.ios;

import org.springframework.stereotype.Service;

import org.visitor.appportal.service.site.RecommandService;
import org.visitor.appportal.web.utils.SiteUtil;

@Service
public class IosRecommandService extends RecommandService {

	@Override
	public Integer getSiteId() {
		return SiteUtil.IDEnum.IOS.getValue();
	}

}
