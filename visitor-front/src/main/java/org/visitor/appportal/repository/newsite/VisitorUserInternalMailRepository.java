package org.visitor.appportal.repository.newsite;

import org.springframework.data.jpa.repository.Query;
import org.visitor.appportal.repository.base.BaseRepository;
import org.visitor.appportal.visitor.domain.UserInternalMail;

public interface VisitorUserInternalMailRepository extends BaseRepository<UserInternalMail, Long> {
	@Query("from UserInternalMail where uimProductId = ?1 and uimFromUserMail = ?2 and uimStatus = 0")
	UserInternalMail getUserInternalMailByPid(Long productId, String userMailStr);
}
