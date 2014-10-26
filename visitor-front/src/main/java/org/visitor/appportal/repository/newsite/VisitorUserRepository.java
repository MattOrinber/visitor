package org.visitor.appportal.repository.newsite;

import org.springframework.data.jpa.repository.Query;
import org.visitor.appportal.repository.base.BaseRepository;
import org.visitor.appportal.visitor.domain.User;

public interface VisitorUserRepository extends BaseRepository<User, Long> {
	
	@Query("select u from user u where user_email = ?1 and user_status = 0 ")
	User findUserByEmail(String userEmail);
	
	@Query("select count(u.user_id) from user u where user_email = ?1 and user_status = 0")
	long findUserCountByEmail(String userEmail);
	
	@Query("select u from user u where user_facebookid = ?1 and user_status = 0 ")
	User findUserByFacebookId(String userFacebookId);
	
	@Query("select count(u.user_id) from user u where user_facebookid = ?1 and user_status = 0")
	long findUserCountByFacebookId(String userFacebookId);
}
