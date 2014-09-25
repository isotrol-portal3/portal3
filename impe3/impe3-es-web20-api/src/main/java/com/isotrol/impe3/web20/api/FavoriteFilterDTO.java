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

import com.isotrol.impe3.dto.PageFilterDTO;

/**
 * DTO for favorite selection filter.
 * @author Emilio Escobar Reyero
 */
public class FavoriteFilterDTO extends PageFilterDTO {

	/** serial uid. */
	private static final long serialVersionUID = -5918813568265656462L;

	/** Member id. */
	private String member;

	/** Community id*/
	private String community; 

	/** Group id */
	private Long group;
	
	/** Constructor. */
	public FavoriteFilterDTO() {
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
	 * Returns the member id.
	 * @return The member id.
	 */
	public String getMember() {
		return member;
	}
	
	/**
	 * Sets the member id.
	 * @param member the member id.
	 */
	public void setMember(String member) {
		this.member = member;
	}
	
	/**
	 * Returns the group id.
	 * @return The group id.
	 */
	public Long getGroup() {
		return group;
	}
	
	/**
	 * Sets the group id.
	 * @param group The group id.
	 */
	public void setGroup(Long group) {
		this.group = group;
	}
	

}
