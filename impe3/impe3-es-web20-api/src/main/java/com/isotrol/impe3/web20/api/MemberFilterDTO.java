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
import java.util.Map;

import com.isotrol.impe3.dto.StringFilterDTO;


/**
 * DTO for member selection filter.
 * @author Andres Rodriguez
 */
public class MemberFilterDTO implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = 1518982248312294890L;
	
	/** Original member id. */
	private StringFilterDTO memberCode;
	/** Login name. */
	private StringFilterDTO name;
	/** Display name. */
	private StringFilterDTO displayName;
	/** Community id */
	private String communityId;
	/** Role on community. */
	private String role;
	/** Inverse (not on) community. */
	private boolean inverse;
	/** Custom properties. */
	private Map<String, StringFilterDTO> properties;
	/** Profile. */
	private StringFilterDTO profile;
	
	/** Default constructor. */
	public MemberFilterDTO() {
	}

	/**
	 * Returns the member code.
	 * @return The member code.
	 */
	public StringFilterDTO getMemberCode() {
		return memberCode;
	}

	/**
	 * Sets the member code
	 * @param memberCode The member code.
	 */
	public void setMemberCode(StringFilterDTO memberCode) {
		this.memberCode = memberCode;
	}

	/**
	 * Sets the member code
	 * @param memberCode The member code.
	 * @return The fluid builder.
	 */
	public MemberFilterDTO putMemberCode(StringFilterDTO memberCode) {
		this.memberCode = memberCode;
		return this;
	}
	
	/**
	 * Returns the login name.
	 * @return The login name.
	 */
	public StringFilterDTO getName() {
		return name;
	}

	/**
	 * Sets the login name.
	 * @param name The login name.
	 */
	public void setName(StringFilterDTO name) {
		this.name = name;
	}
	
	/**
	 * Sets the login name.
	 * @param name The login name.
	 * @return The fluid builder.
	 */
	public MemberFilterDTO putName(StringFilterDTO name) {
		this.name = name;
		return this;
	}

	/**
	 * Returns the display name.
	 * @return The display name.
	 */
	public StringFilterDTO getDisplayName() {
		return displayName;
	}

	/**
	 * Sets the display name.
	 * @param displayName The display name.
	 */
	public void setDisplayName(StringFilterDTO displayName) {
		this.displayName = displayName;
	}
	
	/**
	 * Sets the display name.
	 * @param displayName The display name.
	 * @return The fluid builder.
	 */
	public MemberFilterDTO putDisplayName(StringFilterDTO displayName) {
		this.displayName = displayName;
		return this;
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
	 * Sets the community id.
	 * @param communityId The community id.
	 * @return The fluid builder.
	 */
	public MemberFilterDTO putCommunityId(String communityId) {
		this.communityId = communityId;
		return this;
	}

	/**
	 * Returns the role on community.
	 * @return The role on community.
	 */
	public String getRole() {
		return role;
	}
	
	/**
	 * Sets the role on community.
	 * @param role The role on community.
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * Sets the role on community.
	 * @param role The role on community.
	 * @return The fluid builder.
	 */
	public MemberFilterDTO putRole(String role) {
		this.role = role;
		return this;
	}

	/**
	 * Returns if wants not on community.
	 * @return True if wants not on community.
	 */
	public boolean isInverse() {
		return inverse;
	}
	
	/**
	 * Sets The inverse value.
	 * @param inverse The inverse value. 
	 */
	public void setInverse(boolean inverse) {
		this.inverse = inverse;
	}

	/**
	 * Sets The inverse value.
	 * @param inverse The inverse value. 
	 * @return The fluid builder.
	 */
	public MemberFilterDTO putInverse(boolean inverse) {
		this.inverse = inverse;
		return this;
	}

	/**
	 * Returns the custom properties map.
	 * @return The custom properties map.
	 */
	public Map<String, StringFilterDTO> getProperties() {
		return properties;
	}
	
	/**
	 * Sets the custom properties map.
	 * @param properties The custom properties map.
	 */
	public void setProperties(Map<String, StringFilterDTO> properties) {
		this.properties = properties;
	}
	
	/**
	 * Sets the custom properties map.
	 * @param properties The custom properties map.
	 * @return The fluid builder.
	 */
	public MemberFilterDTO putProperties(Map<String, StringFilterDTO> properties) {
		this.properties = properties;
		return this;
	}
	
	/**
	 * Returns a member profile.
	 * @return A member profile.
	 */
	public StringFilterDTO getProfile() {
		return profile;
	}
	
	/**
	 * Sets a member profile.
	 * @param profile A member profile.
	 */
	public void setProfile(StringFilterDTO profile) {
		this.profile = profile;
	}
	
	/**
	 * Sets a member profile.
	 * @param profile A member profile.
	 * @return The fluid builder.
	 */
	public MemberFilterDTO putProfile(StringFilterDTO profile) {
		this.profile = profile;
		return this;
	}
}
