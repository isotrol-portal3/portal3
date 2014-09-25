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
 * DTO for a resource counter.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
public class ResourceRatingDTO extends AbstractResourceEventSummaryDTO {
	/** Serial UID. */
	private static final long serialVersionUID = -5944910687844200685L;

	/** Minimum value. */
	private int min;
	/** Maximum value. */
	private int max;
	/** Mean value. */
	private double mean;

	/**
	 * Constructor.
	 * @param resource Resource.
	 * @param count Counter value.
	 */
	public ResourceRatingDTO(String resource, long count) {
		super(resource, count);
	}

	/** Constructor. */
	public ResourceRatingDTO() {
	}

	/**
	 * Returns the minimum value.
	 * @return The minimum value.
	 */
	public int getMin() {
		return min;
	}

	/**
	 * Sets the minimum value.
	 * @param min The minimum value.
	 */
	public void setMin(int min) {
		this.min = min;
	}

	/**
	 * Returns the maximum value.
	 * @return The maximum value.
	 */
	public int getMax() {
		return max;
	}

	/**
	 * Sets the maximum value.
	 * @param max The maximum value.
	 */
	public void setMax(int max) {
		this.max = max;
	}

	/**
	 * Returns the average value.
	 * @return The average value.
	 */
	public double getMean() {
		return mean;
	}

	/**
	 * Sets the average value.
	 * @param mean The average value.
	 */
	public void setMean(double mean) {
		this.mean = mean;
	}
}
