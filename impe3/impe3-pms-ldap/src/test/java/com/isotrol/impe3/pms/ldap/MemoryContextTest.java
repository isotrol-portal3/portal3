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

package com.isotrol.impe3.pms.ldap;


import org.junit.AfterClass;
import org.junit.BeforeClass;


/**
 * Abstract class for tests that share an application context with a private memory database.
 * @author Andres Rodriguez
 */
public abstract class MemoryContextTest {
	/** Aplication context. */
	private static AppContextSupport context; 
	/** String index. */
	private static int stringIndex = 0;

	protected static final String testString() {
		return "test" + (++stringIndex);
	}

	/** Default constructor. */
	public MemoryContextTest() {
	}

	/**
	 * Loads a fresh application context.
	 */
	@BeforeClass
	public static void loadContext() {
		context = AppContextSupport.memory();
	}

	@AfterClass
	public static void shutdown() {
		context.shutdown();
	}

	/**
	 * Returns the unique bean of the context with the specified type.
	 * @param type Requested type.
	 * @return The requested bean.
	 */
	protected static <T> T getBean(Class<T> type) {
		return context.getBean(type);
	}

}
