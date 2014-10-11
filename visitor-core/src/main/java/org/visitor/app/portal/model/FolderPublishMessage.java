/**
 * 
 */
package org.visitor.app.portal.model;

import java.util.List;

import org.visitor.appportal.domain.Folder;

/**
 * @author mengw
 *
 */
public class FolderPublishMessage {
	private Folder folder;
	private List<Long> ancestor;
	private List<Long> children;
	/**
	 * 
	 */
	public FolderPublishMessage() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the folder
	 */
	public Folder getFolder() {
		return folder;
	}
	/**
	 * @param folder the folder to set
	 */
	public void setFolder(Folder folder) {
		this.folder = folder;
	}
	/**
	 * @return the ancestor
	 */
	public List<Long> getAncestor() {
		return ancestor;
	}
	/**
	 * @param ancestor the ancestor to set
	 */
	public void setAncestor(List<Long> ancestor) {
		this.ancestor = ancestor;
	}
	/**
	 * @return the children
	 */
	public List<Long> getChildren() {
		return children;
	}
	/**
	 * @param children the children to set
	 */
	public void setChildren(List<Long> children) {
		this.children = children;
	}
	
}
