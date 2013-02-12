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

package com.isotrol.users.impl;


import java.util.Map;

import org.junit.Assert;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.isotrol.impe3.users.AppContext;


/**
 * Utility class to deal with the Application Context during tests.
 * @author Andres Rodriguez
 */
public final class AppContextSupport {
	private static final String MEMORY_PATH = "com/isotrol/impe3/users/users-memctx.xml";
	private static final String[] MEMORY = {MEMORY_PATH, AppContext.PATH};

	private final ApplicationContext context;

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
		Assert.assertEquals(1, map.size());
		@SuppressWarnings("unchecked")
		final T bean = (T) map.values().iterator().next();
		return bean;
	}
}
