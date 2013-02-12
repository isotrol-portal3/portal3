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

package com.isotrol.impe3.freemarker;


import org.junit.Before;
import org.junit.Test;

import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.Route;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.test.TestContext;


/**
 * Tests for FreeMarker Port@l object wrapper.
 * @author Andres Rodriguez
 */
public class WrapperTest {
	private TestContext context;
	private RenderContext render;

	@Before
	public void setUp() {
		context = TestContext.empty();
		render = context.getRenderContext();
	}

	/** Route. */
	@Test
	public void route() throws Exception {
		Route r = Route.of(false, PageKey.special("page1"), render.getDevice(), render.getLocale());
		Util.testTemplate("${uri()}\n${absUri(\"a\", 1)}\n", r, render, true);
	}

	/** Special Page. */
	@Test
	public void specialPage() throws Exception {
		Route r = Route.of(false, PageKey.special("page1"), render.getDevice(), render.getLocale());
		Util.testTemplate(
			"\n${toSpecialPage(\"special1\").uri()}\n${toSpecialPage(\"special2\").absUri(\"a\", 1)}\n${toSpecialPage().absUri()}\n",
			r, render, true);
	}

	/** URI builder. */
	@Test
	public void uriBuilder() throws Exception {
		Route r = Route.of(false, PageKey.special("page1"), render.getDevice(), render.getLocale());
		Util.testTemplate("${uriBuilder.uri()}\n${uriBuilder.absUri(\"a\", 1)}\n", r, render, true);
	}

}
