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

package com.isotrol.impe3.pms.api.session;


import java.io.Serializable;


/**
 * DTO for environment configuration.
 * @author Andres Rodriguez
 */
public final class EnvironmentConfigDTO implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = 5480123555373351179L;
	/** Internal path segment. */
	private String internalSegment;
	/** Maximun unsuccessful login attempts. */
	private Integer maxLoginAttempts;

	/** Default constructor. */
	public EnvironmentConfigDTO() {
	}

	/**
	 * Returns the internal path segment.
	 * @return The internal path segment.
	 */
	public String getInternalSegment() {
		return internalSegment;
	}

	/**
	 * Sets the internal path segment.
	 * @param internalSegment The internal path segment.
	 */
	public void setInternalSegment(String internalSegment) {
		this.internalSegment = internalSegment;
	}

	/**
	 * Returns the maximun unsuccessful login attempts.
	 * @return The maximun unsuccessful login attempts.
	 */
	public Integer getMaxLoginAttempts() {
		return maxLoginAttempts;
	}

	/**
	 * Touches the maximun unsuccessful login attempts.
	 * @param maxLoginAttempts The maximun unsuccessful login attempts.
	 */
	public void setMaxLoginAttempts(Integer maxLoginAttempts) {
		this.maxLoginAttempts = maxLoginAttempts;
	}
}
