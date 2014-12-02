package org.visitor.appportal.repository.newsite;
import org.springframework.data.jpa.repository.Query;
import org.visitor.appportal.repository.base.BaseRepository;
import org.visitor.appportal.visitor.domain.UserTokenInfo;


public interface VisitorUserTokenInfoRepository extends BaseRepository<UserTokenInfo, Long> {
	@Query("select p from UserTokenInfo p where ufiUserId = ?1")
	UserTokenInfo getUserTokenInfoByUserId(Long userId);
	
	@Query("select p from UserTokenInfo p where ufiUserEmail = ?1")
	UserTokenInfo getUserTokenInfoByUserEmail(String userEmail);
}
