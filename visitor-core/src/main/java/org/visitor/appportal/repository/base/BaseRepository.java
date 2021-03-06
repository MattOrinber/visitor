package org.visitor.appportal.repository.base;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends
	PagingAndSortingRepository<T, ID>, JpaSpecificationExecutor<T>, CustomRepository<T> {

}
