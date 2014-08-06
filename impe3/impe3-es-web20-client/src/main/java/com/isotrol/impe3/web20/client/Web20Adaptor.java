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
package com.isotrol.impe3.web20.client;


import java.util.Map;
import java.util.Set;

import com.isotrol.impe3.api.content.Content;


/**
 * Web 2.0 Counters Module
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez Chamorro
 */
public interface Web20Adaptor {
	/**
	 * Maps a resource to a content.
	 * @param resource Resource to map.
	 * @return The mapped content or {@code null} if not found.
	 */
	Content getResource(String resource);

	/**
	 * Maps a resource set to contents.
	 * @param resources Resources to map.
	 * @return A map with the resources that have been found.
	 */
	Map<String, Content> getResources(Set<String> resources);

	/**
	 * Checks whether a resource is attached to a community. If the resource or the community are not found {@code
	 * false} must be returned.
	 * @param resource Resource.
	 * @param communityId Community Id
	 * @return True if the resource and the community exist and the are related.
	 */
	boolean isResourceInCommunity(String resource, String communityId);
}
