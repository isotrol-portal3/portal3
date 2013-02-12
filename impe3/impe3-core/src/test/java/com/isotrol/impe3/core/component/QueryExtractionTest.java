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


import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.Multimap;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.ExtractQuery;


/**
 * Tests for component definition exceptions.
 * @author Andres Rodriguez
 */
public class QueryExtractionTest {
	private static final String K = "k";
	private static final String V = "v";

	/**
	 * Content extraction test.
	 */
	@Test
	public void test() throws Exception {
		final ComponentDefinition<TestComponent> dfn = ComponentDefinition.of(TestComponent.class);
		final Component component = new TestComponent();
		final Multimap<String, String> map = dfn.getQueryExtractors().extract(component);
		assertTrue(map.containsEntry(K, V));
	}

	static class TestComponent implements Component {
		public ComponentResponse execute() {
			// TODO Auto-generated method stub
			return null;
		}

		@ExtractQuery(K)
		public String getContent() {
			return V;
		}

	}
}
