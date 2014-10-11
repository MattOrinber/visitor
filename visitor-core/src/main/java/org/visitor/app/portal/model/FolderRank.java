package org.visitor.app.portal.model;

import java.io.Serializable;
import java.util.List;

public class FolderRank implements Serializable {
	static final private long serialVersionUID = 1L;
	
	List<Long> totalPv;
	List<Long> totalUv;
	List<Long> totalDl;
	List<Long> weekPv;
	List<Long> weekUv;
	List<Long> weekDl;
	List<Long> monthPv;
	List<Long> monthUv;
	List<Long> monthDl;
	List<Long> dailyPv;
	List<Long> dailyUv;
	List<Long> dailyDl;
	
	public List<Long> getTotalPv() {
		return totalPv;
	}
	public void setTotalPv(List<Long> totalPv) {
		this.totalPv = totalPv;
	}
	public List<Long> getTotalUv() {
		return totalUv;
	}
	public void setTotalUv(List<Long> totalUv) {
		this.totalUv = totalUv;
	}
	public List<Long> getTotalDl() {
		return totalDl;
	}
	public void setTotalDl(List<Long> totalDl) {
		this.totalDl = totalDl;
	}
	public List<Long> getWeekPv() {
		return weekPv;
	}
	public void setWeekPv(List<Long> weekPv) {
		this.weekPv = weekPv;
	}
	public List<Long> getWeekUv() {
		return weekUv;
	}
	public void setWeekUv(List<Long> weekUv) {
		this.weekUv = weekUv;
	}
	public List<Long> getWeekDl() {
		return weekDl;
	}
	public void setWeekDl(List<Long> weekDl) {
		this.weekDl = weekDl;
	}
	public List<Long> getMonthPv() {
		return monthPv;
	}
	public void setMonthPv(List<Long> monthPv) {
		this.monthPv = monthPv;
	}
	public List<Long> getMonthUv() {
		return monthUv;
	}
	public void setMonthUv(List<Long> monthUv) {
		this.monthUv = monthUv;
	}
	public List<Long> getMonthDl() {
		return monthDl;
	}
	public void setMonthDl(List<Long> monthDl) {
		this.monthDl = monthDl;
	}
	
	/**
	 * @return the dailyPv
	 */
	public List<Long> getDailyPv() {
		return dailyPv;
	}
	/**
	 * @param dailyPv the dailyPv to set
	 */
	public void setDailyPv(List<Long> dailyPv) {
		this.dailyPv = dailyPv;
	}
	/**
	 * @return the dailyUv
	 */
	public List<Long> getDailyUv() {
		return dailyUv;
	}
	/**
	 * @param dailyUv the dailyUv to set
	 */
	public void setDailyUv(List<Long> dailyUv) {
		this.dailyUv = dailyUv;
	}
	/**
	 * @return the dailyDl
	 */
	public List<Long> getDailyDl() {
		return dailyDl;
	}
	/**
	 * @param dailyDl the dailyDl to set
	 */
	public void setDailyDl(List<Long> dailyDl) {
		this.dailyDl = dailyDl;
	}


	public enum RankTypeEnum {
		WeekPV, WeekDownload, MonthPV, MonthDownload, TotalDownload, DailyDownload, NewUpdate;
		
		public static RankTypeEnum getInstance(Integer value) {
			if(null != value) {
				RankTypeEnum[] values = RankTypeEnum.values();
				for(RankTypeEnum t : values) {
					if(t.ordinal() == value.intValue()) {
						return t;
					}
				}
			}
			return null;
		}		
	}
}
