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

package com.isotrol.impe3.core;


import java.util.UUID;

import com.isotrol.impe3.api.LocalParams;
import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.PathSegments;
import com.isotrol.impe3.api.PortalRequestContext;
import com.isotrol.impe3.api.Route;


/**
 * Interface for the collection of pages in a portal.
 * @author Andres Rodriguez
 */
public interface Pages {
	/**
	 * Returns a page by id.
	 * @param id Page Id.
	 * @return The requested page or {@code null} if it is not found.
	 */
	Page getPage(UUID id);

	/**
	 * Returns CIP by id.
	 * @param cipId CIP Id.
	 * @return The requested CIP or {@code null} if it is not found.
	 */
	CIP getCIP(UUID cipId);

	/**
	 * Returns a page by route.
	 * @param route Page route.
	 * @return The requested page or {@code null} if it no suitable page is found.
	 */
	Page getPage(Route route);

	/**
	 * Returns a page context by path.
	 * @param model Current portal model.
	 * @param context Current portal request context.
	 * @param path Path to resolve.
	 * @param params Initial local parameters.
	 * @param previous Externally resolved page key.
	 * @return The requested page or {@code null} if it no suitable page is found.
	 */
	PageContext getPage(PortalModel model, PortalRequestContext context, PathSegments path, LocalParams params,
		PageKey previous);

}
