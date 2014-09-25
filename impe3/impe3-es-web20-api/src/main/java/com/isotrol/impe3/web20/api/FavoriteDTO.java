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

import com.isotrol.impe3.dto.AbstractLongId;

/**
 * Favorite resources of members DTO. 
 * @author Emilio Escobar Reyero
 */
public class FavoriteDTO extends AbstractLongId {
	/** serial uid. */
	private static final long serialVersionUID = -925298766926126093L;

	/** Member uuid. */
	private String member;
	
	/** Resource id. */
	private String resource;
	
	/** Community id. */
	private String community;
	
	/** Description */
	private String description;
	
	/** Favorite insert date. */
	private Date date;

	/** Constructor. */
	public FavoriteDTO() {
	}
	
	/**
	 * Returns the member id.
	 * @return The member id.
	 */
	public String getMember() {
		return member;
	}
	
	/**
	 * Sets the member id.
	 * @param member The member id.
	 */
	public void setMember(String member) {
		this.member = member;
	}
	
	/**
	 * Returns the community id.
	 * @return The community id.
	 */
	public String getCommunity() {
		return community;
	}
	
	/**
	 * Sets the community id.
	 * @param community The community id.
	 */
	public void setCommunity(String community) {
		this.community = community;
	}
	
	/**
	 * Returns the resource key.
	 * @return The resource key.
	 */
	public String getResource() {
		return resource;
	}
	
	/**
	 * Sets the resource key.
	 * @param resource The resource key.
	 */
	public void setResource(String resource) {
		this.resource = resource;
	}
	
	/**
	 * Returns the description. 
	 * @return The description.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets the description.
	 * @param description The description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Returns the date.
	 * @return The date.
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 * Sets the date.
	 * @param date The date.
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
}
