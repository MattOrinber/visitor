package org.visitor.app.portal.model;

public class SearchItem {

	private Long id;
	private String name;
		
	public SearchItem(){
		
	}
	
	public SearchItem(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
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
		return "SearchItem [id=" + id + ", name=" + name + "]";
	}
	
	
}
