package org.visitor.appportal.service.ios;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import org.visitor.appportal.service.site.CategoryService;
import org.visitor.appportal.web.utils.SiteUtil;

@Service
public class IosCategoryService extends CategoryService {

	@Override
	public Integer getSiteId() {
		return SiteUtil.IDEnum.IOS.getValue();
	}


	@Override
	public List<Long> getPlatformIds() {
		List<Long> ids = new ArrayList<Long>();
		ids.add(317l);
		return ids;
	}

}
