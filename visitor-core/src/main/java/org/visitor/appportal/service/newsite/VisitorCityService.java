package org.visitor.appportal.service.newsite;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.visitor.appportal.repository.newsite.VisitorCityRepository;
import org.visitor.appportal.service.newsite.searchforms.CitySearchForm;
import org.visitor.appportal.visitor.domain.City;
import org.visitor.appportal.web.utils.WebInfo;

@Service("visitorCityService")
public class VisitorCityService {
	private static final Logger logger = LoggerFactory.getLogger(VisitorCityService.class);
	
	@Autowired
	private VisitorCityRepository visitorCityRepository;
	
	@Transactional
	public void saveCity(City entity) {
		visitorCityRepository.save(entity);
		if (logger.isInfoEnabled()) {
			logger.info("city instance :"+entity.getCityName()+": saved");
		}
	}
	
	@Transactional
	public City getCityByName(String cityName) {
		City result = visitorCityRepository.findAvailCityByName(cityName);
		if (logger.isInfoEnabled()) {
			if (result == null) {
				logger.info("city not in database for :"+cityName+":");
			} else {
				logger.info("city already in database for :"+cityName+":");
			}
		}
		return result;
	}
	
	@Transactional
	public List<City> getAvailCityList() {
		return visitorCityRepository.findAvailCityList();
	}
	
	@Transactional
	public List<City> getUnavailCityList() {
		return visitorCityRepository.findUnavailCityList();
	}
	
	@Transactional
	public Long getCityCount() {
		return visitorCityRepository.count();
	}
	
	@Transactional
	public List<City> getPagedCityList(Long pageIdx) {
		CitySearchForm csf = new CitySearchForm();
		csf.getSp().setPageNumber(pageIdx.intValue());
		csf.getSp().setPageSize(WebInfo.pageSize.intValue());
		Page<City> pCity = visitorCityRepository.findAll(csf.toSpecification(), csf.getPageable());
		
		return pCity.getContent();
	}
}
