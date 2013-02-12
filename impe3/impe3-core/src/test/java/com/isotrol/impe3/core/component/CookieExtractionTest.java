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

package com.isotrol.impe3.core.component;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;

import org.junit.Test;

import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.ExtractCookie;


/**
 * Tests for component definition exceptions.
 * @author Andres Rodriguez
 */
public class CookieExtractionTest {

	/** No extraction. */
	@Test
	public void no() throws Exception {
		final ComponentDefinition<TestComponent> dfn = ComponentDefinition.of(TestComponent.class);
		assertTrue(dfn.getCookieExtractors().isEmpty());
	}

	/** Good extraction. */
	@Test
	public void good() throws Exception {
		final ComponentDefinition<Good> dfn = ComponentDefinition.of(Good.class);
		assertFalse(dfn.getCookieExtractors().isEmpty());
	}

	/** Bad extraction. */
	@Test(expected = IllegalExtractionMethodException.class)
	public void bad() throws Exception {
		ComponentDefinition.of(Bad.class);
	}

	static class TestComponent implements Component {
		public final ComponentResponse execute() {
			return ComponentResponse.OK;
		}
	}

	static class Good extends TestComponent {
		@ExtractCookie
		public NewCookie getCookie() {
			return null;
		}
	}

	static class Bad extends TestComponent {
		@ExtractCookie
		public Cookie getCookie() {
			return null;
		}
	}
}
