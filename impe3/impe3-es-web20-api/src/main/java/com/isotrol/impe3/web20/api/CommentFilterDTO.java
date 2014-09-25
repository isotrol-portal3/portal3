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


import java.util.Date;

import com.isotrol.impe3.dto.PageFilterDTO;


/**
 * DTO for comment selection filter.
 * @author Emilio Escobar Reyero
 */
public class CommentFilterDTO extends PageFilterDTO {

	/** serial uid. */
	private static final long serialVersionUID = -2627696355644799608L;

	private String resourcePrefix;
	private String resourceKey;
	private String communityId;

	private Boolean valid; // default true

	private Boolean moderated; // default true

	private Date lowDate;
	private Date highDate;

	public String getResourcePrefix() {
		return resourcePrefix;
	}

	public void setResourcePrefix(String resourcePrefix) {
		this.resourcePrefix = resourcePrefix;
	}

	public String getResourceKey() {
		return resourceKey;
	}

	public void setResourceKey(String resourceKey) {
		this.resourceKey = resourceKey;
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
	 * @return the lowDate
	 */
	public Date getLowDate() {
		return lowDate;
	}

	/**
	 * @param lowDate the lowDate to set
	 */
	public void setLowDate(Date lowDate) {
		this.lowDate = lowDate;
	}

	/**
	 * @return the highDate
	 */
	public Date getHighDate() {
		return highDate;
	}

	/**
	 * @param highDate the highDate to set
	 */
	public void setHighDate(Date highDate) {
		this.highDate = highDate;
	}
}
