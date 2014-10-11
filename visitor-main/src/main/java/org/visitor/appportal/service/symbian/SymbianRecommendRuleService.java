package org.visitor.appportal.service.symbian;

import org.springframework.stereotype.Service;

import org.visitor.appportal.service.site.RecommendRuleService;
import org.visitor.appportal.web.utils.SiteUtil;

@Service
public class SymbianRecommendRuleService extends RecommendRuleService {

	@Override
	public Integer getSiteId() {
		// TODO Auto-generated method stub
		return SiteUtil.IDEnum.SYMBIAN.getValue();
	}

}
