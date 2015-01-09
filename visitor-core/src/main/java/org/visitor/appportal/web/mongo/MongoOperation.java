package org.visitor.appportal.web.mongo;

public class MongoOperation {
	private Integer operationType; //0----save, 1--- fetch
	private Object saveObject;
	private String fetchDB;
	private String fetchCollection;
	private String fetchKey;
	public Integer getOperationType() {
		return operationType;
	}
	public void setOperationType(Integer operationType) {
		this.operationType = operationType;
	}
	public Object getSaveObject() {
		return saveObject;
	}
	public void setSaveObject(Object saveObject) {
		this.saveObject = saveObject;
	}
	public String getFetchDB() {
		return fetchDB;
	}
	public void setFetchDB(String fetchDB) {
		this.fetchDB = fetchDB;
	}
	public String getFetchCollection() {
		return fetchCollection;
	}
	public void setFetchCollection(String fetchCollection) {
		this.fetchCollection = fetchCollection;
	}
	public String getFetchKey() {
		return fetchKey;
	}
	public void setFetchKey(String fetchKey) {
		this.fetchKey = fetchKey;
	}
}
