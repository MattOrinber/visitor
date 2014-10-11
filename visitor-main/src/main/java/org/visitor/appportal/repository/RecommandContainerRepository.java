/**
 * 
 */
package org.visitor.appportal.repository;

import java.util.List;

import org.visitor.appportal.domain.RecommandContainer;
import org.visitor.appportal.repository.base.BaseRepository;

/**
 * @author mengw
 *
 */
public interface RecommandContainerRepository extends BaseRepository<RecommandContainer, Long> {
	
	List<RecommandContainer> findByContainerId(Long recommandContainerId);

}
