package org.visitor.appportal.repository.newsite;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.visitor.appportal.repository.base.BaseRepository;
import org.visitor.appportal.visitor.domain.FloopyThing;

public interface VisitorFloopyThingsRepository extends BaseRepository<FloopyThing, Long> {
	@Query("select f from FloopyThing f where floopyId = ?1 and floopyStatus = 0")
	FloopyThing getFloopyThingStringByKey(String key);
	
	@Query("from FloopyThing where floopyStatus = 0")
	List<FloopyThing> getFloopyThingListAvailable();
}
