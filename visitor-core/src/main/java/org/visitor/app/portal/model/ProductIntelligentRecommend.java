package org.visitor.app.portal.model;

public class ProductIntelligentRecommend {
	
	private IntelligentItemRecommend behavior;//用户行为
	private IntelligentItemRecommend similar;//相似度
	private IntelligentItemRecommend storage;//推荐库
	private IntelligentItemRecommend manual;//手工
	
	/**
	 * @return the behavior
	 */
	public IntelligentItemRecommend getBehavior() {
		return behavior;
	}
	/**
	 * @param behaviors the behaviors to set
	 */
	public void setBehavior(IntelligentItemRecommend behavior) {
		this.behavior = behavior;
	}
	/**
	 * @return the similar
	 */
	public IntelligentItemRecommend getSimilar() {
		return similar;
	}
	/**
	 * @param similars the similars to set
	 */
	public void setSimilar(IntelligentItemRecommend similar) {
		this.similar = similar;
	}
	/**
	 * @return the storage
	 */
	public IntelligentItemRecommend getStorage() {
		return storage;
	}
	/**
	 * @param storage the storage to set
	 */
	public void setStorage(IntelligentItemRecommend storage) {
		this.storage = storage;
	}
	/**
	 * @return the manual
	 */
	public IntelligentItemRecommend getManual() {
		return manual;
	}
	/**
	 * @param manual the manual to set
	 */
	public void setManual(IntelligentItemRecommend manual) {
		this.manual = manual;
	}
	@Override
	public String toString() {
		return "ProductIntelligentRecommend [behavior=" + behavior
				+ ", similar=" + similar + ", storage=" + storage + ", manual="
				+ manual + "]";
	}

}
