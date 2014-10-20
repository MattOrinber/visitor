package org.visitor.appportal.service.game;

import org.springframework.stereotype.Service;

import org.visitor.appportal.service.site.PictureService;
import org.visitor.appportal.web.utils.SiteUtil;

@Service
public class GamePictureService extends PictureService {

	@Override
	public Integer getSiteId() {
		// TODO Auto-generated method stub
		return SiteUtil.IDEnum.GAME.getValue();
	}

}
