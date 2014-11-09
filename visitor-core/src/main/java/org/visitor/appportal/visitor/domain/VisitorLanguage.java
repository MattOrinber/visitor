package org.visitor.appportal.visitor.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "visitor_language")
public class VisitorLanguage {
	private Integer languageId;
	private Long languageSerial;
	private String languageName;
	
	@Column(name = "timezone_id", nullable = false, unique = true, precision = 20)
	@GeneratedValue
	@Id
	public Integer getLanguageId() {
		return languageId;
	}
	public void setLanguageId(Integer languageId) {
		this.languageId = languageId;
	}
	
	@NotNull
	@Column(name = "language_serial", nullable = false, unique = true, precision = 20)
	public Long getLanguageSerial() {
		return languageSerial;
	}
	public void setLanguageSerial(Long languageSerial) {
		this.languageSerial = languageSerial;
	}
	
	@Length(max = 31)
	@Column(name = "language_name", nullable = false)
	public String getLanguageName() {
		return languageName;
	}
	public void setLanguageName(String languageName) {
		this.languageName = languageName;
	}
}
