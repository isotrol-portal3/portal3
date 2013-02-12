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

package com.isotrol.impe3.core.support;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;


/**
 * Tests for SingleValueSupport.
 * @author Andres Rodriguez.
 */
public class SingleValueSupportTest {
	private static final Integer K1 = 1;
	private static final Integer K2 = 2;
	private static final String V1 = "1";
	private static final String V2 = "2";
	
	/**
	 * General test.
	 */
	@Test
	public void test() {
		final SingleValueSupport<Integer, String> store = SingleValueSupport.create();
		assertNull(store.get(K1));
		assertNull(store.get(K2));
		assertEquals(store.put(K1, V1), V1);
		assertNotNull(store.get(K1));
		assertEquals(store.put(K1, V2), V1);
		assertEquals(store.put(K2, V2), V2);
		assertNotNull(store.get(K2));
	}
}
