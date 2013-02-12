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


import java.net.URI;
import java.util.UUID;

import javax.ws.rs.core.UriBuilder;

import com.google.common.collect.Multimap;


/**
 * Interface for an URI generator.
 * URIs Generators only exist inside a portal.
 * @author Andres Rodriguez
 */
public interface URIGenerator {
	/**
	 * Returns the portal.
	 * @return The portal.
	 */
	Portal getPortal();
	/**
	 * Returns a new URI builder initalized at the portal's routing domain base.
	 * @return A new URI builder.
	 */
	UriBuilder getBase();

	/**
	 * Returns a new URI builder initalized at the portal's routing domain absolute base.
	 * @return A new URI builder.
	 */
	UriBuilder getAbsoluteBase();
	
	/**
	 * Returns the URI for the specified route.
	 * @param route Route to convert into an URI.
	 * @return The URI for the specified route.
	 */
	URI getURI(Route route);

	/**
	 * Returns the URI for the specified route and parameters.
	 * @param route Route to convert into an URI.
	 * @param parameters Request parameters.
	 * @return The URI for the specified route.
	 */
	URI getURI(Route route, Multimap<String, ?> parameters);

	/**
	 * Returns the absolute URI for the specified route.
	 * @param route Route to convert into an URI.
	 * @return The URI for the specified route.
	 */
	URI getAbsoluteURI(Route route);

	/**
	 * Returns the absolute URI for the specified route and parameters.
	 * @param route Route to convert into an URI.
	 * @param parameters Request parameters.
	 * @return The URI for the specified route.
	 */
	URI getAbsoluteURI(Route route, Multimap<String, ?> parameters);
	
	/**
	 * Returns the URI for the specified file.
	 * @param file File to convert into an URI.
	 * @return The URI for the specified route.
	 */
	URI getURI(FileId file);

	/**
	 * Returns the URI for the specified file bundle item.
	 * @param file File to convert into an URI.
	 * @param name Item name.
	 * @return The URI for the specified route.
	 */
	URI getURI(FileId file, String name);

	/**
	 * Returns the absolute URI for the specified file.
	 * @param file File to convert into an URI.
	 * @return The URI for the specified route.
	 */
	URI getAbsoluteURI(FileId file);

	/**
	 * Returns the absolute URI for the specified file bundle item.
	 * @param file File to convert into an URI.
	 * @param name Item name.
	 * @return The URI for the specified route.
	 */
	URI getAbsoluteURI(FileId file, String name);

	/**
	 * Returns a URI realtive to the provided base URI.
	 * @param base Base name.
	 * @param path Relative path.
	 * @return The requested URI {@code null} if the base is not found.
	 */
	URI getURIByBase(String base, String path);

	/**
	 * Returns a URI realtive to the provided mode dependent base URI.
	 * @param base Base name.
	 * @param path Relative path.
	 * @return The requested URI {@code null} if the base is not found.
	 */
	URI getURIByMDBase(String base, String path);

	/**
	 * Returns an action URI.
	 * @param from Calling route.
	 * @param cipId Component in page Id.
	 * @param name Action name.
	 * @param parameters Action query parameters.
	 * @return The requested URI.
	 */
	URI getActionURI(Route from, UUID cipId, String name, Multimap<String, Object> parameters);

	/**
	 * Returns an absolute action URI.
	 * @param from Calling route.
	 * @param cipId Component in page Id.
	 * @param name Action name.
	 * @param parameters Action query parameters.
	 * @return The requested URI.
	 */
	URI getAbsoluteActionURI(Route from, UUID cipId, String name, Multimap<String, Object> parameters);

}
