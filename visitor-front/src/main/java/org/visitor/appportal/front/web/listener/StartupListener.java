package org.visitor.appportal.front.web.listener;

import java.util.Locale;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartupListener implements ServletContextListener{
	private Logger logger = LoggerFactory.getLogger(StartupListener.class);

    /**
     * Called when the servlet container is starting up the application
     */
    public void contextInitialized(ServletContextEvent event) {
        if (logger.isInfoEnabled()) {
            logger.info("Servlet Context initialized...");
        }

        Locale.setDefault(Locale.ENGLISH);
    }

    /**
     * Called when the servlet container is stopping up the application
     */
    public void contextDestroyed(ServletContextEvent event) {
        if (logger.isInfoEnabled()) {
            logger.info("Servlet Context destroyed...");
        }
    }
}
