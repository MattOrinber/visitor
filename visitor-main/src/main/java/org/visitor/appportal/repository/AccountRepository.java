package org.visitor.appportal.repository;

import org.visitor.appportal.domain.Account;
import org.visitor.appportal.repository.base.BaseRepository;

public interface AccountRepository extends BaseRepository<Account, Long> {

	Account findByUsername(String username);

}
