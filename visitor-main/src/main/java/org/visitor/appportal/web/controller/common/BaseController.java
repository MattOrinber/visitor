package org.visitor.appportal.web.controller.common;

import org.springframework.beans.factory.annotation.Autowired;

import org.visitor.appportal.service.site.ServiceFactory;

public class BaseController {
    @Autowired
    private ServiceFactory serviceFactory;
    
    
    public ServiceFactory getServiceFactory(){
    	return serviceFactory;
    }

}
