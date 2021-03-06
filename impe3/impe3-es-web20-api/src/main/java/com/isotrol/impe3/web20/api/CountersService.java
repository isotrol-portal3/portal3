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

package com.isotrol.impe3.web20.api;


import java.util.List;

import com.isotrol.impe3.dto.ServiceException;


/**
 * Counters Service.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez Chamorro
 */
public interface CountersService extends Web20Service {
	/**
	 * Register an event.
	 * @param serviceId External service id.
	 * @param event Event.
	 * @param async True if the registration should be asynchronous.
	 */
	void register(String serviceId, CounterEventDTO event, boolean async) throws ServiceException;

	/**
	 * Returns the resources with most hits.
	 * @param serviceId External service id.
	 * @param filter Counter filter.
	 * @param max Maximum number of results.
	 * @return The list of resources with most hits.
	 */
	List<ResourceCounterDTO> getGreatestHits(String serviceId, CounterFilterDTO filter, int max)
		throws ServiceException;

	/**
	 * Returns the number of hits of a resource.
	 * @param serviceId External service id.
	 * @param filter Counter filter.
	 * @param resource Resource.
	 * @return The number of hits of the resource (if found).
	 */
	long getResourceHits(String serviceId, CounterFilterDTO filter, String resource) throws ServiceException;

}
