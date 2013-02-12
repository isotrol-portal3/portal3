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


import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.collect.ComputationException;
import com.google.common.collect.MapMaker;


/**
 * Test for Computing Map.
 * @author Andres Rodriguez
 */
public class MapTest {
	/** Test. */
	@Test
	public void test() {
		Map<Object, Object> m = new MapMaker().makeComputingMap(new F());
		final Object o = new Object();
		boolean ok = false;
		try {
			m.get(o);
		} catch (ComputationException e) {
			ok = true;
		}
		Assert.assertTrue(ok);
		Assert.assertTrue(o == m.get(o));
	}

	private class F implements Function<Object, Object> {
		private boolean called = false;

		public Object apply(Object from) {
			if (!called) {
				called = true;
				throw new IllegalStateException();
			}
			return from;
		}
	}
}
