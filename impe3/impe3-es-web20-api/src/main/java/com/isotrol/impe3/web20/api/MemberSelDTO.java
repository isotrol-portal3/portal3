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

import java.util.HashMap;
import java.util.Map;

import com.isotrol.impe3.dto.AbstractStringId;


/**
 * DTO for member selection.
 * @author Andres Rodriguez
 */
public class MemberSelDTO extends AbstractStringId {
	/** Serial UID. */
	private static final long serialVersionUID = -4508525911203597095L;

	/** Member code. */
	private String code;
	
	/** Member name. */
	private String name;
	
	/** Member display name. */
	private String displayName;
	
	/** Member block status. */
	private boolean blocked;
	
	/** Member properties. */
	private Map<String, String> properties = new HashMap<String, String>();
	
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

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
}
