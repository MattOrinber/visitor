/*

 * Template pack-mvc-3:src/main/java/web/interceptor/AccountContextInterceptor.p.vm.java
 */
package org.visitor.appportal.web.interceptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import org.visitor.appportal.context.AccountContext;
import org.visitor.appportal.service.SystemPreference;
import org.visitor.appportal.web.context.AccountContextSupport;

/**
 * This {@link HandlerInterceptor} is responsible for setting up the {@link AccountContext} on the current thread of
 * execution and pass it to the view using the {@link ModelMap}
 */
@Service
public class AccountContextInterceptor implements HandlerInterceptor {

    @Autowired
    private AccountContextSupport accountContextSupport;
    @Autowired
    private SystemPreference systemPreference;
    
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws ServletException {
    	request.setAttribute("picDomain", systemPreference.getPictureDomain());
        // Setup AccountContext and Log Context
        accountContextSupport.processAccountContext(request);

        // Give access to the current account context to the view
        // Note: using the modelAndView in the postHandle would not work
        //       in view returned by Spring Web Flow
        request.setAttribute("accountContext", AccountContext.getAccountContext());

        // proceed
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) {
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        accountContextSupport.resetContext();
    }
}