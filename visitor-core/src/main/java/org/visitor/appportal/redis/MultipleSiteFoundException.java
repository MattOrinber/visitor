/**
 * 
 */
package org.visitor.appportal.redis;

import java.util.List;

import org.visitor.appportal.domain.Site;

/**
 * @author mengw
 *
 */
public class MultipleSiteFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9222620472263363491L;

	private List<Site> sites;
	/**
	 * @param list
	 */
	public MultipleSiteFoundException(List<Site> list) {
		this.sites = list;
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MultipleSiteFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @return the sites
	 */
	public List<Site> getSites() {
		return sites;
	}

}
