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
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.isotrol.impe3.web20.ComponentMemoryContextTest;
import com.isotrol.impe3.web20.server.SourceKey;


/**
 * Tests for SourceComponent.
 * @author Andres Rodriguez
 */
public class SourceComponentTest extends ComponentMemoryContextTest {
	private SourceComponent component;

	@Before
	public void setUp() {
		component = getBean(SourceComponent.class);
	}

	@Test
	public void test() {
		assertNotNull(component);
		final SourceKey key = SourceKey.source(null);
		long s1 = component.get(key);
		long s2 = component.get(key);
		assertEquals(s1, s2);
	}

}
