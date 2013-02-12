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


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentResponse;


/**
 * Abstract tests for links.
 * @author Andres Rodriguez
 */
abstract class AbstractLinkTest {

	/**
	 * List test.
	 */
	public <T extends AbstractLinkComponent, L> void testLink(T component, Class<L> linkType, L link) throws Exception {
		final ComponentDefinition<?> dfn = ComponentDefinition.of(component.getClass());
		dfn.getDirectInjectors().inject(component, linkType, link);
		dfn.getExtractors().extract(component, linkType);
		component.check();
	}

	static abstract class AbstractLinkComponent implements Component {
		private Object injected;
		private Object extracted;

		public final ComponentResponse execute() {
			return ComponentResponse.OK;
		}

		<T> T injected(T value) {
			assertNotNull(value);
			this.injected = value;
			return value;
		}

		<T> T extracted(T value) {
			assertNotNull(value);
			this.extracted = value;
			return value;
		}

		void check() {
			assertNotNull(injected);
			assertNotNull(extracted);
			assertTrue(injected == extracted);
		}

	}
}
