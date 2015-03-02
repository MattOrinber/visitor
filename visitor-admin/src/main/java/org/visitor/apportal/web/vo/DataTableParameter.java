package org.visitor.apportal.web.vo;


import java.util.List;

/**
 * @ClassName: DataTableParameter
 * @Description:
 * @author xianbing.liu@renren-inc.com
 * @date 2013-4-27 下午06:03:56
 * 
 */
public class DataTableParameter<T> {

	private long iTotalRecords;// 总记录数

	private long iTotalDisplayRecords;// 总记录数

	private String sEcho;// 服务端分页时返回的数字

	private List<T> aaData;// 返回json数据的key 必须是aaData 与datatable 需要的格式对应

	public List<T> getAaData() {
		return aaData;
	}

	public void setAaData(List<T> aaData) {
		this.aaData = aaData;
	}

	public DataTableParameter() {
		this.iTotalRecords = 0;
		this.iTotalDisplayRecords = 0;
		this.aaData = null;
		this.sEcho = null;
	}

	public DataTableParameter(List<T> d) {
		this.iTotalRecords = d.size();
		this.iTotalDisplayRecords = d.size();
		this.aaData = d;
	}

	public DataTableParameter(long totalRecords, long totalDisplayRecords,
			String echo, List<T> d) {
		this.iTotalRecords = totalRecords;
		this.iTotalDisplayRecords = totalDisplayRecords;
		this.sEcho = echo;
		this.aaData = d;
	}

	public long getiTotalRecords() {
		return iTotalRecords;
	}

	public void setiTotalRecords(long iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}

	public long getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}

	public void setiTotalDisplayRecords(long iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}

	public String getsEcho() {
		return sEcho;
	}

	public void setsEcho(String sEcho) {
		this.sEcho = sEcho;
	}

}
