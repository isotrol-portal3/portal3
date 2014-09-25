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


import java.util.Map;

import com.isotrol.impe3.dto.AbstractVersionedLongId;

/**
 * DTO for membership (community view) selection.
 * @author Emilio Escobar Reyero
 */
public class CommunityMemberSelDTO extends AbstractVersionedLongId {
	/** Serial UID*/
	private static final long serialVersionUID = -862591319808871804L;

	/** Member. */
	private MemberSelDTO member;
	
	/** Member role on community. */
	private String role;

	/** Whether the user is validated. */
	private boolean validated;
	
	/** Membership properties. */
	private Map<String, String> properties;

	/** Default constructor. */
	public CommunityMemberSelDTO() {
	}
	
	/**
	 * Returns the member info.
	 * @return The member info.
	 */
	public MemberSelDTO getMember() {
		return member;
	}
	
	/**
	 * Sets the member info.
	 * @param member The member info.
	 */
	public void setMember(MemberSelDTO member) {
		this.member = member;
	}
	
	/**
	 * Returns the member role on community.
	 * @return The member role on community.
	 */
	public String getRole() {
		return role;
	}
	
	/**
	 * Sets the member role on community.
	 * @param role The member role on community.
	 */
	public void setRole(String role) {
		this.role = role;
	}
	
	/**
	 * Returns whether the member is validated.
	 * @return True if the member is validated.
	 */
	public boolean isValidated() {
		return validated;
	}
	
	/**
	 * Sets whether the member is validated.
	 * @param deleted True if the member is validated.
	 */
	public void setValidated(boolean validated) {
		this.validated = validated;
	}
	
	/**
	 * Returns the membership properties.
	 * @return The membership properties.
	 */
	public Map<String, String> getProperties() {
		return properties;
	}
	
	/**
	 * Sets the membership properties.
	 * @param properties The membership properties.
	 */
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
}
