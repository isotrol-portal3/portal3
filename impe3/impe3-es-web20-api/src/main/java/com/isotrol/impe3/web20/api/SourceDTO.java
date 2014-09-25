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
 * DTO that represents a participation source.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
public class SourceDTO implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = 103619904101957566L;

	/** Member Id. */
	private String memberId;
	/** Origin name. */
	private String origin;

	/** Constructor. */
	public SourceDTO() {
	}

	/**
	 * Returns the member id.
	 * @return The member id.
	 */
	public String getMemberId() {
		return memberId;
	}

	/**
	 * Sets the member id.
	 * @param member The member id.
	 */
	public void setMemberId(String member) {
		this.memberId = member;
	}

	/**
	 * Returns the origin.
	 * @return The origin.
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 * Sets the origin.
	 * @param origin The origin.
	 */
	public void setOrigin(String origin) {
		this.origin = origin;
	}

}
