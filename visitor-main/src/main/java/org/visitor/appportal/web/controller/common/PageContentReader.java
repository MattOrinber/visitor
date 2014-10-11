/**
 * 
 */
package org.visitor.appportal.web.controller.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;

/**
 * @author mengw
 *
 */
@Component
public class PageContentReader {
	/** Logger available to subclasses */
	protected final Log logger = LogFactory.getLog(getClass());
	private PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();
	private ResourceLoader resourceLoader = new DefaultResourceLoader();
	private String fileName = "classpath:htmlpagecontent.xml";
	
	public String getPropertyValue(String property) {
		Properties properties = this.refreshProperties(this.getFileName());
		if(properties != null) {
			return (String)properties.get(property);
		}
		return null;
	}
	/**
	 * Load the properties from the given resource.
	 * @param resource the resource to load from
	 * @param filename the original bundle filename (basename + Locale)
	 * @return the populated Properties instance
	 * @throws IOException if properties loading failed
	 */
	protected Properties loadProperties(Resource resource) throws IOException {
		InputStream is = resource.getInputStream();
		Properties props = new Properties();
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("Loading properties [" + resource.getFilename() + "]");
			}
			this.propertiesPersister.loadFromXml(props, is);
			return props;
		} finally {
			is.close();
		}
	}
	
	
	/**
	 * Refresh the PropertiesHolder for the given bundle filename.
	 * The holder can be <code>null</code> if not cached before, or a timed-out cache entry
	 * (potentially getting re-validated against the current last-modified timestamp).
	 * @param filename the bundle filename (basename + Locale)
	 * @param propHolder the current PropertiesHolder for the bundle
	 */
	protected Properties refreshProperties(String filename) {
		Resource resource = this.resourceLoader.getResource(filename);
		if (resource.exists()) {
			try {
				return loadProperties(resource);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
