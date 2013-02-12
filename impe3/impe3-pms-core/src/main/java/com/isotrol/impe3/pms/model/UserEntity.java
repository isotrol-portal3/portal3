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

package com.isotrol.impe3.pms.model;


import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionOfElements;

import com.google.common.collect.Sets;
import com.isotrol.impe3.pms.api.GlobalAuthority;
import com.isotrol.impe3.pms.api.GlobalRole;
import com.isotrol.impe3.pms.core.UserManager;


/**
 * Entity that represents an user.
 * @author Andres Rodriguez
 */
@Entity
@Table(name = "PMS_USER")
@NamedQueries({
	@NamedQuery(name = UserEntity.NOT_DELETED, query = "from UserEntity as e where (e.deleted is null or e.deleted is false)"),
	@NamedQuery(name = UserEntity.BY_NAME, query = "from UserEntity as e where name = :name")})
public class UserEntity extends WithCreatedUpdatedEntity {
	/** Query by not deleted. */
	public static final String NOT_DELETED = "user.notDeleted";
	/** Query by name. */
	public static final String BY_NAME = "user.byName";
	/** Login name. */
	@Column(name = "NAME", length = Lengths.NAME, unique = true, nullable = false)
	private String name;
	/** Display name. */
	@Column(name = "DISPLAY_NAME", length = Lengths.DESCRIPTION)
	private String displayName;
	/** Password. */
	@Column(name = "PASSWORD", length = Lengths.NAME)
	private String password;
	/** Is the user active?. */
	@Column(name = "ACTIVE", nullable = false)
	private boolean active = true;
	/** Whether the user is locked. */
	@Column(name = "USER_LOCKED", nullable = true)
	private Boolean locked = Boolean.FALSE;
	/** User unsuccessful login attempts. */
	@Column(name = "USER_ATTEMPTS", nullable = true)
	private Integer attempts = 0;
	/** Whether the user is root. */
	@Column(name = "ROOT", nullable = true)
	private Boolean root = false;
	/** Whether the user is deleted. */
	@Column(name = "DELETED", nullable = true)
	private Boolean deleted = false;
	/** Global roles. */
	@CollectionOfElements(fetch = FetchType.LAZY)
	@JoinTable(name = "PMS_USER_GROLE", joinColumns = @JoinColumn(name = "USER_ID"))
	@Column(name = "GLOBAL_ROLE", nullable = false)
	@Enumerated(EnumType.STRING)
	private Set<GlobalRole> globalRoles;
	/** Global authorities. */
	@CollectionOfElements(fetch = FetchType.LAZY)
	@JoinTable(name = "PMS_USER_GA", joinColumns = @JoinColumn(name = "USER_ID"))
	@Column(name = "GLOBAL_AUTH", nullable = false, length = Lengths.NAME)
	@Enumerated(EnumType.STRING)
	private Set<GlobalAuthority> globalAuthorities;
	/** Portal authorities. */
	@CollectionOfElements(fetch = FetchType.LAZY)
	@JoinTable(name = "PMS_USER_PA", joinColumns = @JoinColumn(name = "USER_ID"))
	private Set<PortalAuthorityValue> portalAuthorities;

	/** Default constructor. */
	public UserEntity() {
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
	 * Returns the password.
	 * @return The password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 * @param password The password.
	 */
	public void setPassword(String password) {
		this.password = password;
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
	 * Returns whether the user is active.
	 * @return True if the user is active.
	 */
	public boolean isLocked() {
		if (locked == null) {
			locked = Boolean.FALSE;
		}
		return locked.booleanValue();
	}

	/**
	 * Sets whether the user is locked.
	 * @param locked True if the user is active.
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	/**
	 * Returns the number of unsuccessful login attempts.
	 * @return The number of unsuccessful login attempts.
	 */
	public int getAttempts() {
		if (attempts == null || attempts.intValue() < 0) {
			return 0;
		}
		return attempts.intValue();
	}

	/**
	 * Register an unsuccessful login attempt.
	 * @return The number of unsuccessful login attempts.
	 */
	public int registerAttempts() {
		if (attempts == null || attempts.intValue() < 0) {
			attempts = 1;
		} else {
			attempts = attempts + 1;
		}
		return attempts.intValue();
	}

	/**
	 * Clears the unsuccessful login attempts count.
	 */
	public void resetAttempts() {
		if (attempts != null && attempts.intValue() > 0) {
			attempts = 0;
		}
	}

	/**
	 * Returns whether the user is root.
	 * @return True if the user is root.
	 */
	public boolean isRoot() {
		if (root != null) {
			return root.booleanValue();
		}
		return UserManager.ROOT_ID.equals(getId());
	}

	/**
	 * Sets whether the user is root.
	 * @param root True if the user is root.
	 */
	public void setRoot(boolean root) {
		this.root = root;
	}

	/**
	 * Returns whether the user is deleted.
	 * @return True if the user is deleted.
	 */
	public boolean isDeleted() {
		return deleted != null ? deleted.booleanValue() : false;
	}

	/**
	 * Sets whether the user is deleted.
	 * @param root True if the user is deleted.
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	/**
	 * Returns the global roles.
	 * @return The global roles.
	 */
	public Set<GlobalRole> getGlobalRoles() {
		if (globalRoles == null) {
			globalRoles = Sets.newHashSet();
		}
		return globalRoles;
	}

	/**
	 * Returns the global authorities.
	 * @return The global authorities.
	 */
	public Set<GlobalAuthority> getGlobalAuthorities() {
		if (globalAuthorities == null) {
			globalAuthorities = Sets.newHashSet();
		}
		return globalAuthorities;
	}

	/**
	 * Returns the portal authorities.
	 * @return The portal authorities.
	 */
	public Set<PortalAuthorityValue> getPortalAuthorities() {
		if (portalAuthorities == null) {
			portalAuthorities = Sets.newHashSet();
		}
		return portalAuthorities;
	}
}
