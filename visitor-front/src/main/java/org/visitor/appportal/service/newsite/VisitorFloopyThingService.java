package org.visitor.appportal.service.newsite;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.visitor.appportal.repository.newsite.VisitorFloopyThingsRepository;
import org.visitor.appportal.visitor.domain.FloopyThing;

@Service("visitorFloopyThingService")
public class VisitorFloopyThingService {
	private static final Logger logger = LoggerFactory.getLogger(VisitorFloopyThingService.class);
	
	@Autowired
	private VisitorFloopyThingsRepository visitorFloopyThingsRepository;
	
	@Transactional
	public void saveFloopyThing(FloopyThing ftTemp) {
		visitorFloopyThingsRepository.save(ftTemp);
		
		if (logger.isInfoEnabled()) {
			logger.info("saved floopy instance of " + ftTemp.getFloopyKey() + " : "+ftTemp.getFloopyValue());
		}
	}
	
	@Transactional
	public FloopyThing getFloopyThingUsingKey(String key) {
		FloopyThing result = visitorFloopyThingsRepository.getFloopyThingStringByKey(key);
		return result;
	}
	
	@Transactional
	public List<FloopyThing> getTotalList() {
		List<FloopyThing> result = (List<FloopyThing>) visitorFloopyThingsRepository.getFloopyThingListAvailable();
		return result;
	}
}
