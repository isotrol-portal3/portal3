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


import java.io.Serializable;


/**
 * Abstract DTO for a counter filters.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
public class AbstractCounterFilterDTO implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = 173077941219901305L;
	/** Counter type. */
	private String counterType;
	/** Community Id. */
	private String communityId;
	/** Aggregation. */
	private String aggregation;
	/** Amount of time to search (in seconds). */
	private Long time;

	/** Constructor. */
	public AbstractCounterFilterDTO() {
	}

	/**
	 * Returns the counter type.
	 * @return The counter type.
	 */
	public String getCounterType() {
		return counterType;
	}

	/**
	 * Sets the counter type.
	 * @param counterType The counter type.
	 */
	public void setCounterType(String counterType) {
		this.counterType = counterType;
	}

	/**
	 * Returns the community id.
	 * @return The community id.
	 */
	public String getCommunityId() {
		return communityId;
	}

	/**
	 * Sets the community id.
	 * @param name The community id.
	 */
	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}

	/**
	 * Returns the aggregation.
	 * @return The aggregation.
	 */
	public String getAggregation() {
		return aggregation;
	}

	/**
	 * Sets the aggregation.
	 * @param aggregation The aggregation.
	 */
	public void setAggregation(String aggregation) {
		this.aggregation = aggregation;
	}

	/**
	 * Returns the amount of time to search (in seconds).
	 * @return The amount of time to search (in seconds).
	 */
	public Long getTime() {
		return time;
	}

	/**
	 * Sets the amount of time to search (in seconds).
	 * @param time The amount of time to search (in seconds).
	 */
	public void setTime(Long time) {
		this.time = time;
	}
}
