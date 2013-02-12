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

package com.isotrol.impe3.core;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.ContentKey;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.EngineMode;
import com.isotrol.impe3.api.NavigationKey;
import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.Portal;


/**
 * Tests for PageMapKey.
 * @author Andres Rodriguez
 */
public class PageMapKeyTest {
	private static ContentType ct1;
	private static Category root;
	private static Category level1;
	private static Category level2;
	private static Portal portal;

	@BeforeClass
	public static void ia() {
		final PortalBuilder b = new PortalBuilder();
		ct1 = b.contentType("ct");
		root = b.category("root", null);
		level1 = b.category("cat1", root.getId());
		level1 = b.category("cat2", root.getId());
		portal = b.getPortal(EngineMode.OFFLINE);
	}

	private static int toDefault(PageMapKey k) {
		int n = 0;
		while (k != PageMapKey.defaultPage()) {
			assertNotNull(k);
			assertTrue(n < 10000);
			k = k.getParent(portal);
			n++;
		}
		return n;
	}

	private static int toDefault(PageKey k) {
		return toDefault(PageMapKey.of(k));
	}

	/** Default page. */
	@Test
	public void defaultPage() {
		assertEquals(0, toDefault(PageMapKey.defaultPage()));
	}

	/** Main page. */
	@Test
	public void mainPage() {
		assertEquals(1, toDefault(PageMapKey.main()));
	}

	/** Content. */
	@Test
	public void content() {
		final ContentKey ck = ContentKey.of(ct1, "id");
		toDefault(PageKey.content(ck));
		final NavigationKey nk = NavigationKey.category(level1);
		toDefault(PageKey.content(nk, ck));
	}
}
