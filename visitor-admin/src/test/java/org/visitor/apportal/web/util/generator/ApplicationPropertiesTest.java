package org.visitor.apportal.web.util.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.junit.Test;

import org.visitor.apportal.web.util.generator.config.ApplicationProperties;

public class ApplicationPropertiesTest {

	@Test
	public void testInitializeProperties() {
		Properties props = ApplicationProperties.getProperties();

		assertTrue(props.containsKey("jdbc.driver"));
		assertTrue(props.containsValue("com.mysql.jdbc.Driver"));

		assertEquals("web_test", props.getProperty("generator.tablename"));
	}

	@Test
	public void testPropertiesUtil() {
		Properties velocityList = ApplicationProperties.extractProperties(
				ApplicationProperties.getProperties(), "template");

		assertEquals(7, velocityList.size());
	}
}
