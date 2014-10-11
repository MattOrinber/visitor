/*

 * Template pack-backend:src/main/java/project/security/AccountDetailsServiceImpl-spring3.p.vm.java
 */
package org.visitor.appportal.security;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.visitor.appportal.domain.Account;
import org.visitor.appportal.repository.AccountRepository;

/**
 * An implementation of Spring Security's UserDetailsService.
 */
@Service("accountDetailsService")
public class AccountDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(AccountDetailsServiceImpl.class);

    private AccountRepository accountRepository;

    public AccountDetailsServiceImpl() {
    }

    @Autowired
    public AccountDetailsServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Retrieve an account depending on its login this method is not case sensitive.<br>
     * use <code>obtainAccount</code> to match the login to either email, login or whatever is your login logic
     *
     * @param login the account login
     * @return a Spring Security userdetails object that matches the login
     * @see #obtainAccount(String)
     * @throws UsernameNotFoundException when the user could not be found
     * @throws DataAccessException when an error occured while retrieving the account
     */
    @Transactional
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException, DataAccessException {
        if (login == null || login.trim().isEmpty()) {
            throw new UsernameNotFoundException("Empty login");
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Security verification for user '" + login + "'");
        }

        Account account = obtainAccount(login);

        if (account == null) {
            if (logger.isInfoEnabled()) {
                logger.info("Account " + login + " could not be found");
            }
            throw new UsernameNotFoundException("account " + login + " could not be found");
        }

        Collection<GrantedAuthority> grantedAuthorities = obtainGrantedAuthorities(login);

        if (grantedAuthorities == null) {
            grantedAuthorities = SpringSecurityContext.toGrantedAuthorities(account.getRoleNames());
        }

        String password = obtainPassword(login);

        if (password == null) {
            password = account.getPassword();
        }

        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        return new org.springframework.security.core.userdetails.User(login, password, enabled, accountNonExpired,
                credentialsNonExpired, accountNonLocked, grantedAuthorities);
    }

    /**
     * Return the account depending on the login provided by spring security.
     * @return the account if found
     */
    protected Account obtainAccount(String login) {
        return accountRepository.findByUsername(login);
    }

    /**
     * Returns null. Subclass may override it to provide their own granted authorities.
     */
    protected Collection<GrantedAuthority> obtainGrantedAuthorities(String username) {
        return null;
    }

    /**
     * Returns null. Subclass may override it to provide their own password.
     */
    protected String obtainPassword(String username) {
        return null;
    }
}