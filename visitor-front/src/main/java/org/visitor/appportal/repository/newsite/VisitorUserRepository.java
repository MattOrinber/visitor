package org.visitor.appportal.repository.newsite;

import org.springframework.data.jpa.repository.Query;
import org.visitor.appportal.repository.base.BaseRepository;
import org.visitor.appportal.visitor.domain.User;

public interface VisitorUserRepository extends BaseRepository<User, Long> {
	
	@Query("select u from User u where userEmail = ?1 and userStatus = 0 ")
	User findUserByEmail(String userEmail);
	
	@Query("select count(u.userId) from User u where userEmail = ?1 and userStatus = 0")
	long findUserCountByEmail(String userEmail);
	
	@Query("select u from User u where userFacebookId = ?1 and userStatus = 0 ")
	User findUserByFacebookId(String userFacebookId);
	
	@Query("select count(u.userId) from User u where userFacebookId = ?1 and userStatus = 0")
	long findUserCountByFacebookId(String userFacebookId);
}
