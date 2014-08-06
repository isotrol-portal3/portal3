/**
 * This file is part of Port@l
 * Port@l 3.0 - Portal Engine and Management System
 * Copyright (C) 2010  Isotrol, SA.  http://www.isotrol.com
 *
 * Port@l is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Port@l is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Port@l.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.isotrol.impe3.web20;


import static junit.framework.Assert.assertTrue;

import java.util.Map;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.isotrol.impe3.web20.support.AppContext;


/**
 * Utility class to deal with the Core Application Context during tests.
 * @author Andres Rodriguez
 */
public final class AppContextSupport {
	private static final String MEMORY_PATH = "com/isotrol/impe3/web20/web20-memctx.xml";
	private static final String[] MEMORY = {AppContext.PATH, MEMORY_PATH};

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

	/**
	 * Returns a bean of the context by name.
	 * @param name Bean name.
	 * @return The requested bean.
	 */
	public Object getBean(String name) {
		return context.getBean(name);
	}

	public void shutdown() {
		context.close();
	}
}
