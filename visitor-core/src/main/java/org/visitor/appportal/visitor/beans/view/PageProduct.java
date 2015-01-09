package org.visitor.appportal.visitor.beans.view;

public class PageProduct {
	private Long totalSize;
	private Long pageNum;
	private Long pageSize;
	private Integer ifPager;
	
	private Long currentPageNum;
	private Long startPilot;
	private Long endPilot;
	
	private Long windowStart;
	private Long windowEnd;
	
	public Long getTotalSize() {
		return totalSize;
	}
	public void setTotalSize(Long totalSize) {
		this.totalSize = totalSize;
	}
	public Long getPageNum() {
		return pageNum;
	}
	public void setPageNum(Long pageNum) {
		this.pageNum = pageNum;
	}
	public Long getPageSize() {
		return pageSize;
	}
	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getIfPager() {
		return ifPager;
	}
	public void setIfPager(Integer ifPager) {
		this.ifPager = ifPager;
	}
	public Long getCurrentPageNum() {
		return currentPageNum;
	}
	public void setCurrentPageNum(Long currentPageNum) {
		this.currentPageNum = currentPageNum;
	}
	public Long getStartPilot() {
		return startPilot;
	}
	public void setStartPilot(Long startPilot) {
		this.startPilot = startPilot;
	}
	public Long getEndPilot() {
		return endPilot;
	}
	public void setEndPilot(Long endPilot) {
		this.endPilot = endPilot;
	}
	public Long getWindowStart() {
		return windowStart;
	}
	public void setWindowStart(Long windowStart) {
		this.windowStart = windowStart;
	}
	public Long getWindowEnd() {
		return windowEnd;
	}
	public void setWindowEnd(Long windowEnd) {
		this.windowEnd = windowEnd;
	}
}
