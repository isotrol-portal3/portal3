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


import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.Writer;

import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.freemarker.wrap.PortalObjectWrapper;

import freemarker.template.Configuration;
import freemarker.template.Template;


/**
 * Util for FreeMarker tests.
 * @author Andres Rodriguez
 */
public final class Util {
	private Util() {
		throw new AssertionError();
	}

	static Writer out() {
		return new OutputStreamWriter(System.out);
	}

	static void testTemplate(String templateStr, Object model, RenderContext context, boolean useWrapper)
		throws Exception {
		final Template t = new Template("name", new StringReader(templateStr), new Configuration());
		if (context != null && useWrapper) {
			t.process(model, out(), PortalObjectWrapper.create(context));
		} else {
			t.process(model, out());
		}
	}

	static void testTemplate(String templateStr, Object model) throws Exception {
		testTemplate(templateStr, model, null, false);
	}
}
