package org.visitor.appportal.service.symbian;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import org.visitor.appportal.service.site.CategoryService;
import org.visitor.appportal.web.utils.SiteUtil;

@Service
public class SymbianCategoryService extends CategoryService {

	@Override
	public Integer getSiteId() {
		// TODO Auto-generated method stub
		return SiteUtil.IDEnum.SYMBIAN.getValue();
	}


	@Override
	public List<Long> getPlatformIds() {
		// TODO Auto-generated method stub
		List<Long> ids = new ArrayList<Long>();
		
		ids.add(285L);
		ids.add(286L);
		
		return ids;
	}

}
