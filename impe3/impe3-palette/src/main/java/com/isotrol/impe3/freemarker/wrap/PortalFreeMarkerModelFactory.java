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


/**
 * Interface for FreeMarker model factories used by the Port@l Object Wrapper.
 * @author Andres Rodriguez
 */
public interface PortalFreeMarkerModelFactory {
	/**
	 * Checks whether the provided object is wrapped by this factory.
	 * @param obj Object to test.
	 * @return True is this factory must be used to wrap this object.
	 */
	boolean isInstance(Object obj);

	/**
	 * Wraps the object in a template model.
	 * @param wrapper Object wrapper in use.
	 * @param obj Object to wrap.
	 * @return The wrapped object.
	 */
	TemplateModel build(PortalObjectWrapper wrapper, Object obj);
}
