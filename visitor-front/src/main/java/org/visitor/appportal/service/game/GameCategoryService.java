package org.visitor.appportal.service.game;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import org.visitor.appportal.service.site.CategoryService;
import org.visitor.appportal.web.utils.SiteUtil;

@Service
public class GameCategoryService extends CategoryService {

	@Override
	public Integer getSiteId() {
		// TODO Auto-generated method stub
		return SiteUtil.IDEnum.GAME.getValue();
	}


	@Override
	public List<Long> getPlatformIds() {
		// TODO Auto-generated method stub
		List<Long> ids = new ArrayList<Long>();
		ids.add(284L);
		return ids;
	}

}
