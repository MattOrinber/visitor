/**
 * 
 */
package org.visitor.app.portal.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mengw
 *
 */
public class CacheElementPublishMessage {
	private List<String> ids;
	private String cache;
	/**
	 * 
	 */
	public CacheElementPublishMessage() {
		// TODO Auto-generated constructor stub
	}
	
	public CacheElementPublishMessage(String id, String cache) {
		this.ids = new ArrayList<String>();
		ids.add(id);
		this.cache = cache;
	}
	
	public CacheElementPublishMessage(List<String> ids, String cache) {
		this.ids = ids;
		this.cache = cache;
	}
	
	public List<String> getIds() {
		return ids;
	}
	public void setIds(List<String> ids) {
		this.ids = ids;
	}

	public String getCache() {
		return cache;
	}

	public void setCache(String cache) {
		this.cache = cache;
	}

}
