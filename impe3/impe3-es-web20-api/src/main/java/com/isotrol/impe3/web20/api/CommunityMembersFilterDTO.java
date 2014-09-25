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
public class CommunityMembersFilterDTO implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = 3967155646489978729L;
	
	/** Community id. */
	private String id;
	/** Whether the member is validated. */
	private Boolean validated;
	/** Role name. */
	private String role;
	/** Member filter. */
	private MemberFilterDTO memberFilter;
	
	/** Default constructor. */
	public CommunityMembersFilterDTO() {
	}

	/**
	 * Returns the community id.
	 * @return The community id.
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Sets the community id.
	 * @param id The community id.
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Sets the community id.
	 * @param id The community id.
	 * @return The fluid builder.
	 */
	public CommunityMembersFilterDTO putId(String id) {
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
	public CommunityMembersFilterDTO putValidated(Boolean validated) {
		this.validated = validated;
		return this;
	}
	
	/**
	 * Returns the role name.
	 * @return The role name.
	 */
	public String getRole() {
		return role;
	}
	
	/**
	 * Sets the role name.
	 * @param role The role name.
	 */
	public void setRole(String role) {
		this.role = role;
	}
	
	/**
	 * Sets the role name.
	 * @param role The role name.
	 * @return The fluid builder.
	 */
	public CommunityMembersFilterDTO putRole(String role) {
		this.role = role;
		return this;
	}	
	
	/**
	 * Returns the member filter.
	 * @return The member filter.
	 */
	public MemberFilterDTO getMemberFilter() {
		return memberFilter;
	}
	
	/**
	 * Sets the member filter.
	 * @param memberFilter The member filter.
	 */
	public void setMemberFilter(MemberFilterDTO memberFilter) {
		this.memberFilter = memberFilter;
	}
	
	/**
	 * Sets the member filter.
	 * @param memberFilter The member filter.
	 * @return The fluid builder.
	 */
	public CommunityMembersFilterDTO putMemberFilter(MemberFilterDTO memberFilter) {
		this.memberFilter = memberFilter;
		return this;
	}
}
