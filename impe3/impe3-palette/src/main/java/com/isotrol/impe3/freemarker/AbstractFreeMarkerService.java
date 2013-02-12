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


import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Locale;

import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.html.AbstractWriterHTMLFragment;
import com.isotrol.impe3.api.component.html.HTMLFragment;
import com.isotrol.impe3.palette.Loggers;

import freemarker.template.ObjectWrapper;


/**
 * Skeletal class for implementations of the IMPE3 Interface to the freemarker templating engine.
 * @author Andres Rodriguez
 */
public abstract class AbstractFreeMarkerService implements FreeMarkerService {
	/** Default constructor. */
	AbstractFreeMarkerService() {
	}

	/**
	 * @see com.isotrol.impe3.freemarker.FreeMarkerService#getFragment(java.lang.String,
	 * com.isotrol.impe3.api.component.RenderContext, java.util.Locale, java.lang.Object,
	 * freemarker.template.ObjectWrapper)
	 */
	public HTMLFragment getFragment(final String template, final RenderContext context, final Locale locale,
		final Object dataModel, final ObjectWrapper wrapper) {
		checkNotNull(context, "The render context must be provided");
		return new AbstractWriterHTMLFragment() {
			@Override
			protected void writeTo(OutputStreamWriter writer) throws IOException {
				try {
					process(template, context, locale, dataModel, writer, wrapper, true);
				} catch(Exception e) {
					if (Loggers.palette().isErrorEnabled()) {
						Loggers.palette().error("Exception during FreeMarker processing", e);
					}
				}
			}
		};
	}

	/**
	 * @see com.isotrol.impe3.freemarker.FreeMarkerService#getFragment(java.lang.String,
	 * com.isotrol.impe3.api.component.RenderContext, java.util.Locale, java.lang.Object)
	 */
	public HTMLFragment getFragment(String template, RenderContext context, Locale locale, Object dataModel) {
		return getFragment(template, context, locale, dataModel, null);
	}

	/**
	 * @see com.isotrol.impe3.freemarker.FreeMarkerService#getFragment(java.lang.String,
	 * com.isotrol.impe3.api.component.RenderContext, java.lang.Object)
	 */
	public HTMLFragment getFragment(String template, RenderContext context, Object dataModel) {
		return getFragment(template, context, context.getLocale(), dataModel, null);
	}

	/**
	 * @see com.isotrol.impe3.freemarker.FreeMarkerService#getFragment(java.lang.String,
	 * com.isotrol.impe3.api.component.RenderContext, java.lang.Object, freemarker.template.ObjectWrapper)
	 */
	public HTMLFragment getFragment(String template, RenderContext context, Object dataModel, ObjectWrapper wrapper) {
		return getFragment(template, context, context.getLocale(), dataModel, wrapper);
	}
}
