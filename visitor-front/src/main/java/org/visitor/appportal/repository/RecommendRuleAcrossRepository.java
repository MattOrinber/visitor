package org.visitor.appportal.repository;

import java.util.List;

import org.visitor.appportal.domain.RecommendRuleAcross;
import org.visitor.appportal.repository.base.BaseRepository;

public interface RecommendRuleAcrossRepository extends BaseRepository<RecommendRuleAcross, Long> {

	RecommendRuleAcross findByRuleId(Long ruleId);
	RecommendRuleAcross findByServiceIdAndServiceSiteIdAndServiceProductId(Integer serviceId, Integer serviceSiteId, Long serviceProductId);
	RecommendRuleAcross findByServiceIdAndServiceSiteIdAndServiceFolderId(Integer serviceId, Integer serviceSiteId, Long serviceFolderId);
	List<RecommendRuleAcross> findByType(Integer type);
}
