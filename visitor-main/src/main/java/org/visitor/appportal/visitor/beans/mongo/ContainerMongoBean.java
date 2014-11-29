package org.visitor.appportal.visitor.beans.mongo;

import java.util.List;

public class ContainerMongoBean {
	private Long container_id;
	private List<Long> product_id_list;
	public Long getContainer_id() {
		return container_id;
	}
	public void setContainer_id(Long container_id) {
		this.container_id = container_id;
	}
	public List<Long> getProduct_id_list() {
		return product_id_list;
	}
	public void setProduct_id_list(List<Long> product_id_list) {
		this.product_id_list = product_id_list;
	}
}
