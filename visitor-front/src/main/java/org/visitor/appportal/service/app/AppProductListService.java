package org.visitor.appportal.service.app;

import org.springframework.stereotype.Service;

import org.visitor.appportal.service.site.ProductListService;
import org.visitor.appportal.web.utils.SiteUtil;

@Service
public class AppProductListService extends ProductListService {

	@Override
	public Integer getSiteId() {
		// TODO Auto-generated method stub
		return SiteUtil.IDEnum.ANDROID.getValue();
	}

}
