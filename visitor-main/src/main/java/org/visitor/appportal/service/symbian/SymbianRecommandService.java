package org.visitor.appportal.service.symbian;

import org.springframework.stereotype.Service;

import org.visitor.appportal.service.site.RecommandService;
import org.visitor.appportal.web.utils.SiteUtil;

@Service
public class SymbianRecommandService extends RecommandService {

	@Override
	public Integer getSiteId() {
		// TODO Auto-generated method stub
		return SiteUtil.IDEnum.SYMBIAN.getValue();
	}

}
