package org.visitor.appportal.service.newsite;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.visitor.appportal.repository.newsite.VisitorLanguageRepository;
import org.visitor.appportal.visitor.domain.VisitorLanguage;

@Service("visitorLanguageService")
public class VisitorLanguageService {
	@Autowired
	private VisitorLanguageRepository visitorLanguageRepository;
	
	@Transactional
	public List<VisitorLanguage> getAll() {
		List<VisitorLanguage> list = (List<VisitorLanguage>)visitorLanguageRepository.findAll();
		return list;
	}
	
}
