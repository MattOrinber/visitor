package org.visitor.appportal.repository.base;

import java.util.List;

import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CustomRepository<T>{

	public List<T> find(T entity, SearchTemplate searchTemplate);
	
	public int findCount(T entity, SearchTemplate searchTemplate);
}
