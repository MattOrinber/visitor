package org.visitor.appportal.service.newsite;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.visitor.appportal.repository.newsite.VisitorTimezoneRepository;
import org.visitor.appportal.visitor.domain.TimeZone;

@Service("visitorTimezoneService")
public class VisitorTimezoneService {
	
	@Autowired
	private VisitorTimezoneRepository visitorTimezoneRepository;
	
	@Transactional
	public List<TimeZone> getAll() {
		List<TimeZone> list = (List<TimeZone>)visitorTimezoneRepository.findAll();
		return list;
	}
}
