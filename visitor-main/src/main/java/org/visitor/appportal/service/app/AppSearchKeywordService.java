package org.visitor.appportal.service.app;

import org.springframework.stereotype.Service;

import org.visitor.appportal.service.site.SearchKeywordService;
import org.visitor.appportal.web.utils.SiteUtil;

@Service
public class AppSearchKeywordService extends SearchKeywordService {

	@Override
	public Integer getSiteId() {
		// TODO Auto-generated method stub
		return SiteUtil.IDEnum.ANDROID.getValue();
	}

}
