package org.visitor.appportal.service.game;

import org.springframework.stereotype.Service;

import org.visitor.appportal.service.site.TemplateService;
import org.visitor.appportal.web.utils.SiteUtil;

@Service
public class GameTemplateService extends TemplateService {

	@Override
	public Integer getSiteId() {
		// TODO Auto-generated method stub
		return SiteUtil.IDEnum.GAME.getValue();
	}

}
