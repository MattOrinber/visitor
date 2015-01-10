package org.visitor.appportal.repository.newsite;

import org.springframework.data.jpa.repository.Query;
import org.visitor.appportal.repository.base.BaseRepository;
import org.visitor.appportal.visitor.domain.Container;

public interface VisitorContainerRepository extends BaseRepository<Container, Long> {
	@Query("select c from Container c where containerName = ?1")
	Container findOneByName(String name);
}
