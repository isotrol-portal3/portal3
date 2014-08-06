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
package com.isotrol.impe3.web20.impl;


import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.isotrol.impe3.web20.ComponentMemoryContextTest;


/**
 * Tests for ResourceComponent.
 * @author Andres Rodriguez
 */
public class ResourceComponentTest extends ComponentMemoryContextTest {
	private ResourceComponent component;

	@Before
	public void setUp() {
		component = getBean(ResourceComponent.class);
	}

	@Test
	public void test() {
		assertNotNull(component);
		final String s1 = testString();
		final long id1 = component.get(s1);
		final long id1_1 = component.get(s1);
		assertEquals(id1, id1_1);
		final String s2 = testString();
		final long id2 = component.get(s2);
		assertTrue(id1 != id2);
	}

}
