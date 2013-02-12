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


import java.io.Writer;
import java.util.Locale;

import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.component.TemplateModel;
import com.isotrol.impe3.api.component.html.HTMLFragment;

import freemarker.template.ObjectWrapper;


/**
 * IMPE3 Interface to the freemarker templating engine.
 * @author Andres Rodriguez
 */
public interface FreeMarkerService {
	/**
	 * Creates a new freemarker model with the default functions.
	 * @param context Render context.
	 * @return A new model with the default functions.
	 */
	Model createModel(RenderContext context);

	/**
	 * Resolves objects deferred to the rendering phase.
	 * @param context Render context.
	 * @param model Model to resolve.
	 */
	void decorateRenderModel(RenderContext context, TemplateModel model);

	/**
	 * Encapsulate the processing of a FreeMarker template in a HTML fragment.
	 * @param template Template to process.
	 * @param context Component rendering context.
	 * @param locale Locale to use or {@code null} to use the locale provided by the render context.
	 * @param dataModel Data model to use as input.
	 * @return A HTML fragment encapsulating the processing of the template.
	 * @throws NullPointerException If the provided locale is {@code null}.
	 */
	HTMLFragment getFragment(String template, RenderContext context, Locale locale, Object dataModel);

	/**
	 * Encapsulate the processing of a FreeMarker template in a HTML fragment.
	 * @param template Template to process.
	 * @param context Component rendering context.
	 * @param locale Locale to use or {@code null} to use the locale provided by the render context.
	 * @param dataModel Data model to use as input.
	 * @param wrapper FreeMarker object wrapper to use.
	 * @return A HTML fragment encapsulating the processing of the template.
	 * @throws NullPointerException If the provided locale or object wrapper is {@code null}.
	 */
	HTMLFragment getFragment(String template, RenderContext context, Locale locale, Object dataModel,
		ObjectWrapper wrapper);

	/**
	 * Encapsulate the processing of a FreeMarker template in a HTML fragment.
	 * @param template Template to process.
	 * @param context Component rendering context.
	 * @param dataModel Data model to use as input.
	 * @return A HTML fragment encapsulating the processing of the template.
	 * @throws NullPointerException If the provided locale is {@code null}.
	 */
	HTMLFragment getFragment(String template, RenderContext context, Object dataModel);

	/**
	 * Encapsulate the processing of a FreeMarker template in a HTML fragment.
	 * @param template Template to process.
	 * @param context Component rendering context.
	 * @param dataModel Data model to use as input.
	 * @param wrapper FreeMarker object wrapper to use.
	 * @return A HTML fragment encapsulating the processing of the template.
	 * @throws NullPointerException If the provided locale or object wrapper is {@code null}.
	 */
	HTMLFragment getFragment(String template, RenderContext context, Object dataModel, ObjectWrapper wrapper);

	/**
	 * Process a FreeMarker template (low-level method). In case of an exception in safe mode, the exception (offline) or an empty
	 * string (online) will be written to the output before rethrowing the exception.
	 * @param template Template to process.
	 * @param context Component rendering context.
	 * @param locale Locale to use or {@code null} to use the locale provided by the render context.
	 * @param dataModel Data model to use as input.
	 * @param output Output writer.
	 * @param wrapper FreeMarker object wrapper to use (optional).
	 * @param safeModel Wthether to use safe mode.
	 * @throws Exception If an exception occurs during template processing.
	 */
	void process(String template, RenderContext context, Locale locale, Object dataModel, Writer output,
		ObjectWrapper wrapper, boolean safeMode) throws Exception;
}
