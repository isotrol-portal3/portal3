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


/**
 * Abstract DTO for a resource event summary.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
public class AbstractResourceEventSummaryDTO extends AbstractResourceDTO {
	/** Serial UID. */
	private static final long serialVersionUID = 5375999049259355088L;
	/** Event count value. */
	private long count;

	/**
	 * Constructor.
	 * @param resource Resource.
	 * @param count Event count value.
	 */
	public AbstractResourceEventSummaryDTO(String resource, long count) {
		super(resource);
		this.count = count;
	}

	/** Constructor. */
	public AbstractResourceEventSummaryDTO() {
	}

	/**
	 * Returns the event count value.
	 * @return The event count value.
	 */
	public long getCount() {
		return count;
	}

	/**
	 * Sets the event count value.
	 * @param count The event count value.
	 */
	public void setCount(long count) {
		this.count = count;
	}
}
