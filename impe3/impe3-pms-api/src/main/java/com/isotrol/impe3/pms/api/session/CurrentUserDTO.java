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

package com.isotrol.impe3.pms.api.session;


import java.io.Serializable;


/**
 * DTO for current user information.
 * @author Andres Rodriguez
 */
public class CurrentUserDTO implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = -5511134959109420667L;
	/** User name. */
	private UserNameDTO name;
	/** Whether the user is root. */
	private boolean root;
	/** Whether the user can change its display name. */
	private boolean changeDisplayName;
	/** Whether the user can change its login name. */
	private boolean changeLogin;
	/** Whether the user can change its password. */
	private boolean changePassword;

	/** Default constructor. */
	public CurrentUserDTO() {
	}

	/**
	 * Returns the user name.
	 * @return The user name.
	 */
	public UserNameDTO getName() {
		return name;
	}

	/**
	 * Sets the user name.
	 * @param name The user name.
	 */
	public void setName(UserNameDTO name) {
		this.name = name;
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
	 * Returns whether the user can change its display name.
	 * @return True if the user can change its display name.
	 */
	public boolean isChangeDisplayName() {
		return changeDisplayName;
	}

	/**
	 * Sets whether the user is active.
	 * @param changeDisplayName True if the user is active.
	 */
	public void setChangeDisplayName(boolean changeDisplayName) {
		this.changeDisplayName = changeDisplayName;
	}

	/**
	 * Returns whether the user can change its display name.
	 * @return True if the user can change its display name.
	 */
	public boolean isChangeLogin() {
		return changeLogin;
	}

	/**
	 * Sets whether the user is active.
	 * @param changeDisplayName True if the user is active.
	 */
	public void setChangeLogin(boolean changeLogin) {
		this.changeLogin = changeLogin;
	}

	/**
	 * Returns whether the user can change its display name.
	 * @return True if the user can change its display name.
	 */
	public boolean isChangePassword() {
		return changePassword;
	}

	/**
	 * Sets whether the user is active.
	 * @param changeDisplayName True if the user is active.
	 */
	public void setChangePassword(boolean changePassword) {
		this.changePassword = changePassword;
	}
}
