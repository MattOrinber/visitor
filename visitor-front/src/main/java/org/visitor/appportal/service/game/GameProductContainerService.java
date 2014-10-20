/**
 * 
 */
package org.visitor.appportal.service.game;

import org.springframework.stereotype.Service;

import org.visitor.appportal.service.site.ProductContainerService;
import org.visitor.appportal.web.utils.SiteUtil;

/**
 * @author mengw
 *
 */
@Service
public class GameProductContainerService extends ProductContainerService {

	/* (non-Javadoc)
	 * @see org.visitor.appportal.service.site.SiteService#getSiteId()
	 */
	@Override
	public Integer getSiteId() {
		// TODO Auto-generated method stub
		return SiteUtil.IDEnum.GAME.getValue();
	}

}
