package org.visitor.appportal.web.vo;

import java.util.ArrayList;
import java.util.List;

import org.visitor.appportal.domain.Folder;
import org.visitor.appportal.domain.Site;

/**
 * 站点频道展示
 * @author mengw
 *
 * 
 */
public class SiteFolderNode {
	private Site site;
	private List<Folder> folders;
	public Site getSite() {
		return site;
	}
	public SiteFolderNode(){
		
	}

	public SiteFolderNode(Site site, List<Folder> folders) {
		this.site = site;
		this.folders = new ArrayList<Folder>();
		for(Folder f : folders) {
			if(null != f && f.getParentFolderId() == null) {
				this.folders.add(f);
			}
		}
	}

	public void setSite(Site site) {
		this.site = site;
	}
	/**
	 * @return the folders
	 */
	public List<Folder> getFolders() {
		return folders;
	}
	/**
	 * @param folders the folders to set
	 */
	public void setFolders(List<Folder> folders) {
		this.folders = folders;
	}
	
	
}
