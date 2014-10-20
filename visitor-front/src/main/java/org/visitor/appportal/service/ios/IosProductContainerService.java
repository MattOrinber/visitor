/**
 * 
 */
package org.visitor.appportal.service.ios;

import org.springframework.stereotype.Service;

import org.visitor.appportal.service.site.ProductContainerService;
import org.visitor.appportal.web.utils.SiteUtil;

/**
 * @author mengw
 *
 */
@Service
public class IosProductContainerService extends ProductContainerService {

	/* (non-Javadoc)
	 * @see org.visitor.appportal.service.site.SiteService#getSiteId()
	 */
	@Override
	public Integer getSiteId() {
		return SiteUtil.IDEnum.IOS.getValue();
	}

}
