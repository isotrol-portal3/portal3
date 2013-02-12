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

package com.isotrol.impe3.api.component;


import java.net.URI;
import java.util.Set;

import com.google.common.collect.Multimap;
import com.isotrol.impe3.api.Pagination;


/**
 * Interface for an URI generator in the context of a component. All pagination related methods return the same page uri
 * in case of a {@code null} or incomplete pagination argument.
 * @author Andres Rodriguez
 * 
 */
public interface RenderContext extends ComponentRequestContext {
	/**
	 * Returns the assigned layout width, if known.
	 * @return The layout width if known, {@code null} if not known.
	 */
	Integer getWidth();

	/**
	 * Returns an URI to the same page.
	 * @param parameters Query parameters.
	 * @return The requested URI.
	 */
	URI getSamePageURI(Multimap<String, ?> parameters);

	/**
	 * Returns an URI to the same page.
	 * @param remove Extracted query parameters to remove.
	 * @param parameters Query parameters.
	 * @return The requested URI.
	 */
	URI getSamePageURI(Set<String> remove, Multimap<String, ?> parameters);

	/**
	 * Returns an URI to an absolute page number.
	 * @param pagination Pagination.
	 * @param page Page number. If less than 1, 1 will be used.
	 * @return The requested URI.
	 */
	URI getAbsolutePageURI(Pagination pagination, int page);

	/**
	 * Returns an URI to an absolute page number.
	 * @param pagination Pagination.
	 * @param delta Relative movement.
	 * @return The requested URI.
	 */
	URI getRelativePageURI(Pagination pagination, int delta);

	/**
	 * Returns an URI to the first page.
	 * @param pagination Pagination.
	 * @return The requested URI.
	 */
	URI getFirstPageURI(Pagination pagination);

	/**
	 * Returns an URI to the last page.
	 * @param pagination Pagination.
	 * @return The requested URI.
	 */
	URI getLastPageURI(Pagination pagination);
}
