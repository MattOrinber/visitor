package org.visitor.appportal.service.ios;

import org.springframework.stereotype.Service;

import org.visitor.appportal.service.site.SearchKeywordService;
import org.visitor.appportal.web.utils.SiteUtil;

@Service
public class IosSearchKeywordService extends SearchKeywordService {

	@Override
	public Integer getSiteId() {
		return SiteUtil.IDEnum.IOS.getValue();
	}

}
