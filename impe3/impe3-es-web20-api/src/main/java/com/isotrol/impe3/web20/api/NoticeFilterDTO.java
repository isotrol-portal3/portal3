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

import com.isotrol.impe3.dto.OrderDTO;
import com.isotrol.impe3.dto.PageFilterDTO;
import com.isotrol.impe3.dto.StringFilterDTO;

/**
 * DTO for community notices filter.
 * @author Emilio Escobar Reyero
 */
public class NoticeFilterDTO extends PageFilterDTO {
	/** serial uid. */
	private static final long serialVersionUID = 5001829818861635988L;

	/** Community id. */
	private String communityId;
	
	private StringFilterDTO title;

	private StringFilterDTO description;
	
	/** null means all, true released and not expired, false not released or expired. */
	private Boolean active;

	/** Generates order by release date. */
	public static OrderDTO release(boolean asc) {
		return new OrderDTO("release", asc);
	}
	
	
	/** Constructor. */
	public NoticeFilterDTO() {
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
	 * @param communityId The community id.
	 */
	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}
	
	/**
	 * Returns the title.
	 * @return The title.
	 */
	public StringFilterDTO getTitle() {
		return title;
	}
	
	/**
	 * Sets the title.
	 * @param title The title.
	 */
	public void setTitle(StringFilterDTO title) {
		this.title = title;
	}
	
	/**
	 * null means all, true released and not expired, false not released or expired.
	 */
	public Boolean isActive() {
		return active;
	}

	/**
	 * Sets the state.
	 * @param active The state.
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}
	
	/**
	 * Returns the description.
	 * @return The description.
	 */
	public StringFilterDTO getDescription() {
		return description;
	}
	
	/**
	 * Sets the description. 
	 * @param description The description.
	 */
	public void setDescription(StringFilterDTO description) {
		this.description = description;
	}
}
