package org.visitor.appportal.repository;

import java.util.List;

import org.visitor.appportal.domain.RecommendRule;
import org.visitor.appportal.repository.base.BaseRepository;

public interface RecommendRuleRepository extends BaseRepository<RecommendRule, Long> {

	RecommendRule findByRuleId(Long ruleId);
	RecommendRule findByFolderId(Long folderId);
	List<RecommendRule> findByType(Integer type);
	RecommendRule findBySiteIdAndType(Integer siteId, Integer type);
	RecommendRule findByProductIdAndSiteId(Long productId, Integer siteId);
	RecommendRule findByRuleIdAndSiteId(Long ruleId, Integer siteId);
}
