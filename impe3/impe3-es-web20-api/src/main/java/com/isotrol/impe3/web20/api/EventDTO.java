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


import java.util.Set;


/**
 * Abstract DTO for events.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
public abstract class EventDTO extends AbstractActionDTO {
	/** Serial UID. */
	private static final long serialVersionUID = 2324408481215832126L;
	

	/** Aggregations. */
	private Set<String> aggregations;

	/**
	 * Constructor.
	 * @param resource Resource.
	 */
	public EventDTO(String resource) {
		super(resource);
	}

	/** Constructor. */
	public EventDTO() {
	}



	/**
	 * Returns the aggregations.
	 * @return The aggregations.
	 */
	public Set<String> getAggregations() {
		return aggregations;
	}

	/**
	 * Sets the aggregations.
	 * @param aggregations The aggregations.
	 */
	public void setAggregations(Set<String> aggregations) {
		this.aggregations = aggregations;
	}
}
