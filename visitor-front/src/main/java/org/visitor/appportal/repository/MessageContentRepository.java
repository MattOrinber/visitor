/**
 * 
 */
package org.visitor.appportal.repository;

import org.visitor.appportal.domain.MessageContent;
import org.visitor.appportal.repository.base.BaseRepository;

/**
 * @author mengw
 *
 */
public interface MessageContentRepository extends BaseRepository<MessageContent, Long> {

	MessageContent findBySourceMessageIdAndSourceProductId(
			String sourceMessageId, String sourceProductId);

}
