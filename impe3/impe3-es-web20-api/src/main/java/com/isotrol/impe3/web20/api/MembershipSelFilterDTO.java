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
 * DTO for community members selection filter.
 * @author Emilio Escobar Reyero
 */
public class MembershipSelFilterDTO implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = -5677547823246512567L;
	
	/** Member id. */
	private String id;
	/** Whether the member is validated. */
	private Boolean validated;
	/** Whether the global community is included. */
	private boolean global = false;
	/** Role on community. */
	private String role;
	
	/** Default constructor. */
	public MembershipSelFilterDTO() {
	}

	/**
	 * Returns the member id.
	 * @return The member id.
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Sets the member id.
	 * @param id The member id.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Sets the member id.
	 * @param id The member id.
	 * @return The fluid builder.
	 */
	public MembershipSelFilterDTO putId(String id) {
		this.id = id;
		return this;
	}
	
	/**
	 * Returns whether the membership is validated.
	 * @return True if the membership is validated.
	 */
	public Boolean isValidated() {
		return validated;
	}
	
	/**
	 * Sets whether the membership is validated.
	 * @param validated True if the membership is validated.
	 */
	public void setValidated(Boolean validated) {
		this.validated = validated;
	}
	
	/**
	 * Sets whether the membership is validated.
	 * @param validated True if the membership is validated.
	 * @return The fluid builder.
	 */
	public MembershipSelFilterDTO putValidated(Boolean validated) {
		this.validated = validated;
		return this;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isGlobal() {
		return global;
	}
	
	/**
	 * 
	 * @param global
	 */
	public void setGlobal(boolean global) {
		this.global = global;
	}
	
	/**
	 * 
	 * @param global
	 * @return
	 */
	public MembershipSelFilterDTO putGlobal(boolean global) {
		this.global = global;
		return this;
	}
	
	/**
	 * Returns the member role.
	 * @return The member role.
	 */
	public String getRole() {
		return role;
	}
	
	/**
	 * Sets the member role.
	 * @param role The member role.
	 */
	public void setRole(String role) {
		this.role = role;
	}
	
	/**
	 * Sets the member role.
	 * @param role The member role.
	 * @return The fluid builder.
	 */
	public MembershipSelFilterDTO putRole(String role) {
		this.role = role;
		return this;
	}
}
