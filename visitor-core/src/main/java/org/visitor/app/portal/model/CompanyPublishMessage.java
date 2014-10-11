/**
 * 
 */
package org.visitor.app.portal.model;

/**
 * @author mengw
 *
 */
public class CompanyPublishMessage {
	private Long companyId;
	/**
	 * 
	 */
	public CompanyPublishMessage() {
		// TODO Auto-generated constructor stub
	}
	public CompanyPublishMessage(Long companyId) {
		this.companyId = companyId;
	}
	/**
	 * @return the companyId
	 */
	public Long getCompanyId() {
		return companyId;
	}
	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

}
