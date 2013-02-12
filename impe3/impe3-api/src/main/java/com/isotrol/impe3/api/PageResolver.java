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

package com.isotrol.impe3.api;


/**
 * Interface for custom page routers inside a portal. It should be implemented as a connector.
 * @author Andres Rodriguez.
 */
public interface PageResolver {
	/**
	 * Resolves a custom route inside a portal.
	 * @param params Resolution params.
	 * @return The custom route or {@code null} if the standard routing should proceed.
	 */
	ResolvedPage getRoute(PageResolutionParams params);

	/**
	 * Prepares a custom path for a page inside a portal.
	 * @param portal Portal.
	 * @param route Route.
	 * @return A list of decoded path segments if a custom path if desired or {@code null} if not.
	 */
	PageURI getPath(Portal portal, Route route);
}
