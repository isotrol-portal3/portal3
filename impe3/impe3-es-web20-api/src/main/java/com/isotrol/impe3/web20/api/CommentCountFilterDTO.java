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
 * DTO for comment count selection filter.
 * @author Emilio Escobar Reyero
 */
public class CommentCountFilterDTO implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = -1946952194381373060L;
	/** Community. */
	private String communityId;
	private Boolean valid;
	private Boolean moderated;
	/** Amount of time to search (in seconds). */
	private Long time;
	/** Maximum number of values. */
	private int max = 10;

	public CommentCountFilterDTO() {
	}

	public String getCommunityId() {
		return communityId;
	}

	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}

	public Boolean isValid() {
		return valid;
	}

	public void setValid(Boolean valid) {
		this.valid = valid;
	}

	public Boolean isModerated() {
		return moderated;
	}

	public void setModerated(Boolean moderated) {
		this.moderated = moderated;
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

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = Math.max(max, 1);
	}
}
