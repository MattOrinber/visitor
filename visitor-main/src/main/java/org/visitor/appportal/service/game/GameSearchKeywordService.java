package org.visitor.appportal.service.game;

import org.springframework.stereotype.Service;

import org.visitor.appportal.service.site.SearchKeywordService;
import org.visitor.appportal.web.utils.SiteUtil;

@Service
public class GameSearchKeywordService extends SearchKeywordService {

	@Override
	public Integer getSiteId() {
		// TODO Auto-generated method stub
		return SiteUtil.IDEnum.GAME.getValue();
	}

}
