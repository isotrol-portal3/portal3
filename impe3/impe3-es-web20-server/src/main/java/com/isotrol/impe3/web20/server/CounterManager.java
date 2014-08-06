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
import com.isotrol.impe3.web20.api.CounterEventDTO;
import com.isotrol.impe3.web20.api.CounterFilterDTO;
import com.isotrol.impe3.web20.api.ResourceCounterDTO;
import com.isotrol.impe3.web20.server.CounterMap.Entry;
import com.isotrol.impe3.web20.server.CounterMap.Key;


/**
 * Internal counter manager.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez Chamorro
 */
public interface CounterManager extends ResourceManager, WithTimeMapManager<CounterMap> {
	/**
	 * Sets whether counter event breakdowns are stored.
	 * @param breakdown True if the breakdown must be stored.
	 */
	void setBreakdown(boolean breakdown);

	/**
	 * Register an event.
	 * @param event Event.
	 */
	void register(CounterEventDTO event);

	/**
	 * Computes a counter key from a counter filter.
	 * @param filter Counter filter.
	 * @return The requested key or {@code null} if the filter does not contain enough information.
	 */
	Key getKey(CounterFilterDTO filter) throws ServiceException;

	/**
	 * Returns a list of resources.
	 * @param entries Counter map entries.
	 * @return The requested list.
	 */
	List<ResourceCounterDTO> getResources(Iterable<Entry> entries);
}
