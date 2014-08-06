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
package com.isotrol.impe3.web20.server;


import java.util.List;

import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.api.EventDTO;
import com.isotrol.impe3.web20.api.EventFilterDTO;
import com.isotrol.impe3.web20.api.RatingEventDTO;
import com.isotrol.impe3.web20.api.ResourceRatingDTO;
import com.isotrol.impe3.web20.server.RatingMap.Entry;


/**
 * Internal rating manager.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez Chamorro
 */
public interface RatingManager extends ResourceManager, WithTimeMapManager<RatingMap> {
	boolean isRated(EventDTO event) throws ServiceException;
	
	/**
	 * Register an event.
	 * @param event Event.
	 * @param allowMore
	 */
	void register(RatingEventDTO event, boolean allowMore) throws ServiceException;

	/**
	 * Computes a group id from an event filter.
	 * @param filter Event filter.
	 * @return The requested id or {@code null} if the filter does not contain enough information.
	 */
	Long getGroupId(EventFilterDTO filter) throws ServiceException;

	/**
	 * Returns a list of resources.
	 * @param entries Counter map entries.
	 * @return The requested list.
	 */
	List<ResourceRatingDTO> getResources(Iterable<Entry> entries);

	/**
	 * Returns the ratings of a resource.
	 * @param map Rating map.
	 * @param groupId Group Id.
	 * @param resource Resource.
	 * @return The resource ratings.
	 */
	ResourceRatingDTO getResource(RatingMap map, Long groupId, String resource);

	/**
	 * Returns the ratings of a resource.
	 * @param map Rating map.
	 * @param filter Event filter.
	 * @param resource Resource.
	 * @return The resource ratings.
	 */
	ResourceRatingDTO getResource(RatingMap map, EventFilterDTO filter, String resource) throws ServiceException;

}
