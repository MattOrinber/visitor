package org.visitor.app.portal.model;

import java.util.List;

public class IntelligentItemRecommend {
	
	/** 产品列表 */
	private List<Long> productIds;
	/** 排序 */
	private int sequence;
	/** redis Key */
	private String redisKey;
	/** 显示个数 */
	private int disNum;
	/** 是否随机显示 */
	private boolean random;
	
	private String name;
	
	/**
	 * @return the productId
	 */
	public List<Long> getProductIds() {
		return productIds;
	}
	/**
	 * @param productId the productId to set
	 */
	public void setProductIds(List<Long> productIds) {
		this.productIds = productIds;
	}
	/**
	 * @return the sequence
	 */
	public int getSequence() {
		return sequence;
	}
	/**
	 * @param sequence the sequence to set
	 */
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	/**
	 * @return the redisKey
	 */
	public String getRedisKey() {
		return redisKey;
	}
	/**
	 * @param redisKey the redisKey to set
	 */
	public void setRedisKey(String redisKey) {
		this.redisKey = redisKey;
	}
	/**
	 * @return the disNum
	 */
	public int getDisNum() {
		return disNum;
	}
	/**
	 * @param disNum the disNum to set
	 */
	public void setDisNum(int disNum) {
		this.disNum = disNum;
	}
	/**
	 * @return the random
	 */
	public boolean isRandom() {
		return random;
	}
	/**
	 * @param random the random to set
	 */
	public void setRandom(boolean random) {
		this.random = random;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "IntelligentItemRecommend [productIds=" + productIds
				+ ", sequence=" + sequence + ", redisKey=" + redisKey
				+ ", disNum=" + disNum + ", random=" + random + ", name="
				+ name + "]";
	}
	
}
