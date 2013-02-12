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

package com.isotrol.impe3.pms.api.user;


import com.isotrol.impe3.pms.api.AbstractWithId;


/**
 * DTO for user selection.
 * @author Andres Rodriguez
 */
public class UserSelDTO extends AbstractWithId {
	/** Serial UID. */
	private static final long serialVersionUID = 7284598741100660857L;
	/** Login name. */
	private String name;
	/** Display name. */
	private String displayName;
	/** Whether the user is root. */
	private boolean root;
	/** Whether the user is active. */
	private boolean active;
	/** Whether the user is locked. */
	private boolean locked;

	/** Default constructor. */
	public UserSelDTO() {
	}

	/**
	 * Copy constructor.
	 * @param dto Source DTO.
	 */
	public UserSelDTO(UserSelDTO dto) {
		super(dto);
		this.name = dto.name;
		this.displayName = dto.displayName;
		this.root = dto.root;
		this.active = dto.active;
		this.locked = dto.locked;
	}

	/**
	 * Returns the login name.
	 * @return The login name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the login name.
	 * @param name The login name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the display name.
	 * @return The display name.
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Sets the display name.
	 * @param displayName The display name.
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Returns whether the user is root.
	 * @return True if the user is root.
	 */
	public boolean isRoot() {
		return root;
	}

	/**
	 * Sets whether the user is root.
	 * @param root True if the user is root.
	 */
	public void setRoot(boolean root) {
		this.root = root;
	}

	/**
	 * Returns whether the user is active.
	 * @return True if the user is active.
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets whether the user is active.
	 * @param active True if the user is active.
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Returns whether the user is locked.
	 * @return True if the user is locked.
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * Sets whether the user is locked.
	 * @param locked True if the user is locked.
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

}
