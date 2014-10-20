package org.visitor.appportal.web.controller.api;

import java.util.List;


public class ProductApiReturn<T> {
	
	T value;
	List<T> values;
	int status;
	String message;

	public static final int STATUS_SUCCESS = 0;
	public static final int STATUS_FAIL = 1;
	public static final int STATUS_OTHER = 2;
	
	public ProductApiReturn() {
	}

	public ProductApiReturn(int status) {
		this.status = status;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(T value) {
		this.value = value;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the values
	 */
	public List<T> getValues() {
		return values;
	}

	/**
	 * @param values the values to set
	 */
	public void setValues(List<T> values) {
		this.values = values;
	}
	
}
