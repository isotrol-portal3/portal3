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

package com.isotrol.impe3.api;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.Test;


/**
 * Tests for AbstractIdentifiable.
 * @author Andres Rodriguez
 * 
 */
public class AbstractIdentifiableTest {
	private static final UUID ID1 = new UUID(1L, 2L);
	private static final UUID ID2 = new UUID(4L, 3L);;

	/**
	 * Test class.
	 */
	@Test
	public void constructor() {
		final T1 t1 = new T1(ID1);
		assertEquals(ID1, t1.getId());
		assertEquals(t1, t1);
		assertEquals(t1.hashCode(), t1.hashCode());
		final T2 t2 = new T2(ID2);
		assertEquals(ID2, t2.getId());
		assertEquals(t2, t2);
		assertEquals(t2.hashCode(), t2.hashCode());
	}

	/**
	 * Test same class, same id.
	 */
	@Test
	public void scsi() {
		final T1 t1 = new T1(ID1);
		final T1 t2 = new T1(new UUID(ID1.getMostSignificantBits(), ID1.getLeastSignificantBits()));
		assertTrue(t1 != t2);
		assertEquals(t1, t2);
		assertEquals(t1.hashCode(), t2.hashCode());
	}

	/**
	 * Test same class, different id.
	 */
	@Test
	public void scdi() {
		final T1 t1 = new T1(ID1);
		final T1 t2 = new T1(ID2);
		assertTrue(t1 != t2);
		assertNotSame(t1, t2);
	}

	/**
	 * Test different class, different id.
	 */
	@Test
	public void dcdi() {
		final T1 t1 = new T1(ID1);
		final T2 t2 = new T2(ID2);
		assertNotSame(t1, t2);
	}

	/**
	 * Test different class, same id.
	 */
	@Test
	public void dcsi() {
		final T1 t1 = new T1(ID1);
		final T2 t2 = new T2(ID1);
		assertNotSame(t1, t2);
	}

	private static class T1 extends AbstractIdentifiable {
		T1(UUID id) {
			super(id);
		}
	}

	private static class T2 extends AbstractIdentifiable {
		T2(UUID id) {
			super(id);
		}
	}

}
