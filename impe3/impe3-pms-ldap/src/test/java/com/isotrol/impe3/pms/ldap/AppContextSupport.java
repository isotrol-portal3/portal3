package com.isotrol.impe3.pms.ldap;


import static junit.framework.Assert.assertTrue;

import java.util.Map;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Utility class to deal with the Core Application Context during tests.
 * @author Andres Rodriguez
 */
public final class AppContextSupport {
	private static final String[] MEMORY = {"com/isotrol/impe3/pms/ldap/ldap-sample.xml"};

	private final ConfigurableApplicationContext context;

	/**
	 * Creates a new memory-based context.
	 * @return The created context.
	 */
	public static AppContextSupport memory() {
		return new AppContextSupport(MEMORY);
	}

	/** Default constructor. */
	private AppContextSupport(String[] locations) {
		this.context = new ClassPathXmlApplicationContext(locations);
	}

	/**
	 * Returns the unique bean of the context with the specified type.
	 * @param type Requested type.
	 * @return The requested bean.
	 */
	public <T> T getBean(Class<T> type) {
		Map<?, ?> map = context.getBeansOfType(type);
		assertTrue(map.size() == 1);
		@SuppressWarnings("unchecked")
		final T bean = (T) map.values().iterator().next();
		return bean;
	}

	public void shutdown() {
		context.close();
	}
}
