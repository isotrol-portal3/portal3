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
import java.util.Map;
import java.util.Set;

import com.isotrol.impe3.dto.AbstractVersionedStringId;

/**
 * DTO for member detail.
 * @author Emilio Escobar Reyero
 */
public class MemberDTO extends AbstractVersionedStringId {
	/** Serial UID. */
	private static final long serialVersionUID = 5480576663072892035L;

	/** Member code. */
	private String code;
	
	/** Member name. */
	private String name;
	
	/** Member display name. */
	private String displayName;
	
	/** Member email. */ 
	private String email;
	
	/** Member creation date. */ 
	private Date date;
	
	/** Member properties. */
	private Map<String, String> properties;
	
	/** Member profiles. */
	private Set<String> profiles;
	
	/** Member block status. */
	private boolean blocked = false;
	
	/**
	 * Returns the member code.
	 * @return The member code.
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * Sets the member code.
	 * @param code The member code.
	 */
	public void setCode(String code) {
		this.code = code;
	}
	
	/**
	 * Returns the member name.
	 * @return The member name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the member name.
	 * @param name The member name.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the member display name.
	 * @return The member display name.
	 */
	public String getDisplayName() {
		return displayName;
	}
	
	/**
	 * Sets the member display name.
	 * @param displayName The member display name.
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	/**
	 * Returns the member email.
	 * @return The member email.
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Sets the member email.
	 * @param email The member email.
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Returns the member creation date.
	 * @return The member creation date.
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 * Sets the member creation date.
	 * @param date The member creation date.
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
	/**
	 * Returns the member properties.
	 * @return The member properties.
	 */
	public Map<String, String> getProperties() {
		return properties;
	}
	
	/**
	 * Sets the member properties.
	 * @param properties The member properties.
	 */
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	
	/**
	 * Returns the member profiles.
	 * @return The member profiles.
	 */
	public Set<String> getProfiles() {
		return profiles;
	}
	
	/**
	 * Sets the member profiles.
	 * @param profiles The member profiles.
	 */
	public void setProfiles(Set<String> profiles) {
		this.profiles = profiles;
	}
	
	/**
	 * Returns whether the member is blocked.
	 * @return True if the member is blocked.
	 */	
	public boolean isBlocked() {
		return blocked;
	}
	
	/**
	 * Sets whether the member is blocked.
	 * @param deleted True if the member is blocked.
	 */	
	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}	
}
