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

package com.isotrol.impe3.freemarker.wrap;


import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;


/**
 * Interface class for Port@l API method FreeMarker models.
 * @author Andres Rodriguez
 * @param <T> Wrapped object type.
 */
interface PortalAPIMethod<T> {
	/** Returns the method name. */
	String getName();

	/**
	 * Computes the method value.
	 * @param wrapper Object wrapper.
	 * @param object Wrapped object.
	 * @return The template model.
	 * @throws TemplateModelException If requested data cannot be retrieved.
	 */
	TemplateModel get(PortalObjectWrapper wrapper, T object) throws TemplateModelException;
}
