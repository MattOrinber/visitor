/**
 * 
 */
package org.visitor.appportal.service.symbian;

import org.springframework.stereotype.Service;

import org.visitor.appportal.service.site.ProductListService;
import org.visitor.appportal.web.utils.SiteUtil;

/**
 * @author mengw
 *
 */
@Service
public class SymbianProductListService extends ProductListService {

	/* (non-Javadoc)
	 * @see org.visitor.appportal.service.site.SiteService#getSiteId()
	 */
	@Override
	public Integer getSiteId() {
		// TODO Auto-generated method stub
		return SiteUtil.IDEnum.SYMBIAN.getValue();
	}

}
