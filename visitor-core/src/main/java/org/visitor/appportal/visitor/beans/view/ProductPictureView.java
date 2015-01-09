package org.visitor.appportal.visitor.beans.view;

public class ProductPictureView {
	private Long pid;
	private Long picId;
	private String picUrl;
	public Long getPid() {
		return pid;
	}
	public void setPid(Long pid) {
		this.pid = pid;
	}
	public Long getPicId() {
		return picId;
	}
	public void setPicId(Long picId) {
		this.picId = picId;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
}
