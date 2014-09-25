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


import com.isotrol.impe3.dto.AbstractStringId;


/**
 * DTO for a community counter.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
public class CommunityCounterDTO extends AbstractStringId {
	/** Serial UID. */
	private static final long serialVersionUID = -3781395928607662881L;

	/** Community name. */
	private String name;

	/** Count value. */
	private long count;

	/**
	 * Constructor.
	 * @param name Community name.
	 * @param count Counter value.
	 */
	public CommunityCounterDTO(String id, String name, long count) {
		super(id);
		this.name = name;
		this.count = count;
	}

	/** Constructor. */
	public CommunityCounterDTO() {
	}

	/**
	 * Returns the community name.
	 * @return The community name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the community name.
	 * @param name The community name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the count value.
	 * @return The count value.
	 */
	public long getCount() {
		return count;
	}

	/**
	 * Sets the count value.
	 * @param count The count value.
	 */
	public void setCount(long count) {
		this.count = count;
	}

}
