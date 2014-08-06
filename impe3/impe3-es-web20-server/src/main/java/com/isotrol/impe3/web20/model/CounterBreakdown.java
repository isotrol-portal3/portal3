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
package com.isotrol.impe3.web20.model;


import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;


/**
 * Mapped superclass for counter breakdown aggregation entities.
 * @author Andres Rodriguez
 */
@MappedSuperclass
public abstract class CounterBreakdown {
	/** Key. */
	@EmbeddedId
	private CounterPK key;

	/** Total count. */
	@Column(name = "COBR_TOTAL", nullable = false)
	private long total = 1L;

	/**
	 * Constructor.
	 * @key Key.
	 */
	public CounterBreakdown(CounterPK key) {
		this.key = key;
	}

	/** Default constructor. */
	public CounterBreakdown() {
	}

	/**
	 * Returns the key.
	 * @return The key.
	 */
	public CounterPK getKey() {
		return key;
	}

	/**
	 * Sets the key.
	 * @param key The key.
	 */
	public void setKey(CounterPK key) {
		this.key = key;
	}

	/**
	 * Returns the total number of hits.
	 * @return The total number of hits.
	 */
	public long getTotal() {
		return total;
	}
}
