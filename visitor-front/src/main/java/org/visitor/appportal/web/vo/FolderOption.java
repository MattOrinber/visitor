package org.visitor.appportal.web.vo;

import org.visitor.appportal.domain.Folder;


public class FolderOption {
	private Folder folder;
	private int level;
	private String label;
	private String value;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public FolderOption(Folder node) {
		this.folder = node;
		if(null != node) {
			Folder parent = node.getParentFolder();
			while(parent != null) {
				level ++;
				parent = parent.getParentFolder();
			}
			
			StringBuffer _ = new StringBuffer();
			for (long i = 1; i <= level; i++) {
				_.append("--");
			}
			label = _.append(node.getName()).toString();
			value = String.valueOf(node.getFolderId());
		}
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
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

}
