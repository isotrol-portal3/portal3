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

import junit.framework.Assert;

import org.junit.Test;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * Test for Loading Cache.
 * @author Andres Rodriguez
 */
public class CacheTest {
	/** Test. */
	@Test
	public void test() {
		LoadingCache<Object, Object> m = CacheBuilder.newBuilder().build(new F());
		final Object o = new Object();
		boolean ok = false;
		try {
			m.get(o);
		} catch (Exception e) {
			ok = true;
		}
		Assert.assertTrue(ok);
		Assert.assertTrue(o == m.getUnchecked(o));
	}

	private class F extends CacheLoader<Object, Object> {
		private boolean called = false;

		@Override
		public Object load(Object key) throws Exception {
			if (!called) {
				called = true;
				throw new IllegalStateException();
			}
			return key;
		}
	}
}
