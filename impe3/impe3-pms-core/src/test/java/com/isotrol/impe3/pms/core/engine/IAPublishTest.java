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

package com.isotrol.impe3.pms.core.engine;


import static junit.framework.Assert.assertEquals;

import org.junit.Test;

import com.isotrol.impe3.core.EngineModel;


/**
 * Tests for publishing IA.
 * @author Andres Rodriguez
 */
public class IAPublishTest extends AbtractEngineModelTest {
	private static final int N = 100;

	/**
	 * Portal with default page.
	 */
	@Test
	public void test() throws Exception {
		for (int i = 0; i < N; i++) {
			loadContentType();
			loadCategory(null, i);
		}
		EngineModel offline = getOfflineModel();
		publish();
		EngineModel online = getOnlineModel();
		assertEquals(offline.getContentTypes().size(), online.getContentTypes().size());
		assertEquals(offline.getCategories().size(), online.getCategories().size());
	}
}
