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


import static com.google.common.base.Objects.equal;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.google.common.base.Objects;


/**
 * Composite primary key for counter breakdown tables.
 * @author Andres Rodriguez
 */
@Embeddable
public class CounterPK implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = 7700579653467661104L;

	/** Counter. */
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "COBR_CNTR_ID", nullable = false)
	private CounterEntity counter;
	
	/** Timestamp. */
	@Column(name = "COBR_TIMESTAMP", nullable = false)
	private long timestamp;
	
	/** Default constructor. */
	public CounterPK() {
	}
	
	/**
	 * Constructor.
	 * @param counter Counter.
	 * @param timestamp Timestamp
	 */
	public CounterPK(CounterEntity counter, long timestamp) {
		this.counter = counter;
		this.timestamp = timestamp;
	}

	/**
	 * Returns the counter.
	 * @return The counter.
	 */
	public CounterEntity getCounter() {
		return counter;
	}
	
	/**
	 * Sets the counter.
	 * @param counter The counter.
	 */
	public void setCounter(CounterEntity counter) {
		this.counter = counter;
	}

	/**
	 * Returns the timestamp.
	 * @return The timestamp.
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * Sets the timestamp.
	 * @param timestamp The timestamp.
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(counter, timestamp);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof CounterPK) {
			final CounterPK k = (CounterPK) obj;
			return timestamp == k.timestamp && equal(counter, k.counter);
		}
		return false;
	}
}
