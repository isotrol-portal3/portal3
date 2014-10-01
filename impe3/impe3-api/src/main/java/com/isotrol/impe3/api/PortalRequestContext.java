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

import com.google.common.collect.Multimap;
import com.isotrol.impe3.api.content.ContentLoader;

/**
 * IMPE3 Portal Request Context.
 * @author Andres Rodriguez
 */
public interface PortalRequestContext extends RequestContext, URIGenerator {
	/**
	 * Returns the portal id.
	 * @return The portal id.
	 */
	UUID getPortalId();

	/**
	 * Returns the portal model information.
	 * @return The portal model information.
	 */
	ModelInfo getPortalModelInfo();

	PrincipalContext getPrincipalContext();

	ContentLoader getContentLoader();

	/**
	 * Returns the URI to a certain page key.
	 * @param page Destination page.
	 * @return The requested URI.
	 */
	URI getURI(PageKey page);

	/**
	 * Returns the URI for the specified page key and parameters.
	 * @param page Destination page.
	 * @param parameters Request parameters.
	 * @return The URI for the specified route.
	 */
	URI getURI(PageKey page, Multimap<String, ?> parameters);

	/**
	 * Returns the absolute URI to a certain page key.
	 * @param page Destination page.
	 * @return The requested URI.
	 */
	URI getAbsoluteURI(PageKey page);

	/**
	 * Returns the absolute URI for the specified page key and parameters.
	 * @param page Destination page.
	 * @param parameters Request parameters.
	 * @return The URI for the specified route.
	 */
	URI getAbsoluteURI(PageKey page, Multimap<String, ?> parameters);

	/**
	 * Returns an URI relative to the portal main page.
	 * @param path Relative path.
	 * @param parameters Query parameters.
	 * @return The requested URI.
	 */
	URI getPortalRelativeURI(String path, Multimap<String, ?> parameters);

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
