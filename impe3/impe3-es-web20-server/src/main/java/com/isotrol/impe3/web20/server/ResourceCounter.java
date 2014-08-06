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


import com.google.common.base.Function;
import com.isotrol.impe3.web20.api.ResourceCounterDTO;


/**
 * Value for a resource counter.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
public final class ResourceCounter {
	public static final Function<ResourceCounter, ResourceCounterDTO> TO_DTO = new Function<ResourceCounter, ResourceCounterDTO>() {
		public ResourceCounterDTO apply(ResourceCounter from) {
			return new ResourceCounterDTO(from.resource, from.count);
		}
	};

	/** Resource. */
	private final String resource;
	/** Counter value. */
	private final int count;

	/**
	 * Constructor.
	 * @param resource Resource.
	 * @param count Counter value.
	 */
	public ResourceCounter(String resource, int count) {
		this.resource = resource;
		this.count = count;
	}

	/**
	 * Returns the resource.
	 * @return The resource.
	 */
	public String getResource() {
		return resource;
	}

	/**
	 * Returns the counter value.
	 * @return The counter value.
	 */
	public int getCount() {
		return count;
	}
}
