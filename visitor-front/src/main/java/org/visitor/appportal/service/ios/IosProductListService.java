package org.visitor.appportal.service.ios;

import org.springframework.stereotype.Service;

import org.visitor.appportal.service.site.ProductListService;
import org.visitor.appportal.web.utils.SiteUtil;

@Service
public class IosProductListService extends ProductListService {

	@Override
	public Integer getSiteId() {
		return SiteUtil.IDEnum.IOS.getValue();
	}

}
