package org.visitor.appportal.service.ios;

import org.springframework.stereotype.Service;

import org.visitor.appportal.service.site.RecommendRuleService;
import org.visitor.appportal.web.utils.SiteUtil;

@Service
public class IosRecommendRuleService extends RecommendRuleService {

	@Override
	public Integer getSiteId() {
		return SiteUtil.IDEnum.IOS.getValue();
	}

}
