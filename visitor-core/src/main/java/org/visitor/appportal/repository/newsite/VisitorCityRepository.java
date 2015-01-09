package org.visitor.appportal.repository.newsite;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.visitor.appportal.repository.base.BaseRepository;
import org.visitor.appportal.visitor.domain.City;

public interface VisitorCityRepository extends BaseRepository<City, Long> {
	@Query("select c from City c where cityName = ?1")
	City findAvailCityByName(String cityName);
	
	@Query("from City where cityStatus = 0")
	List<City> findAvailCityList();
	
	@Query("from City where cityStatus = 1")
	List<City> findUnavailCityList();
}
