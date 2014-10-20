/*

 * Template pack-mvc-3:src/main/java/web/context/AccountContextSupport.p.vm.java
 */
package org.visitor.appportal.web.context;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.visitor.appportal.context.AccountContext;
import org.visitor.appportal.domain.Account;
import org.visitor.appportal.domain.Site;
import org.visitor.appportal.repository.AccountRepository;
import org.visitor.appportal.repository.SiteRepository;
import org.visitor.appportal.security.SpringSecurityContext;
import org.visitor.appportal.web.utils.SiteUtil;

@Component
public class AccountContextSupport {
    static final private Logger logger = LoggerFactory.getLogger(AccountContextSupport.class);

    private AccountRepository accountRepository;
    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    public AccountContextSupport(AccountRepository accountService) {
        this.accountRepository = accountService;
    }

    /**
     * Set up the LogContext and the AccountContext on the current thread.
     * Should be invoked once, e.g from your web filter or interceptor.
     * Do not forget to call the resetContext method when you are done
     * with the request.
     */
    public void processAccountContext(HttpServletRequest req) {
        String username = SpringSecurityContext.getUsername();

        // set up the account context
        AccountContext accountContext = new AccountContext();
        AccountContext.setAccountContext(accountContext);
        accountContext.setSessionId(req.getSession().getId());
        accountContext.setRoles(SpringSecurityContext.getRoles());

        if (SpringSecurityContext.getUserDetails() != null) {
            // load the account from the database.
            // we assume here that the second level cache is used,
            // otherwise we would hit the database at each request.
            Account account = accountRepository.findByUsername(username);

            if (account != null) {
                // set up account context for this thread
                accountContext.setAccount(account);
                accountContext.setUsername(account.getUsername());
                
                Integer siteId = SiteUtil.getSiteFromSession(req.getSession());
                Site site = siteRepository.findBySiteId(siteId);
                accountContext.setSite(site);
                
            }
        } else if (logger.isDebugEnabled()) {
            logger.debug("No user details");
        }
    }

    /**
     * Reset the account context and the log context from the current thread.
     */
    public void resetContext() {
        AccountContext.resetAccountContext();
    }
}