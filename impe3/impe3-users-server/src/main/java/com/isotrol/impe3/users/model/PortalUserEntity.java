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

package com.isotrol.impe3.users.model;


import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MapKeyColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;


/**
 * Entity that represents a Portal User.
 * @author Andres Rodriguez
 */
@Entity
@Table(name = "PORTAL_USER")
@NamedQueries( {
	@NamedQuery(name = PortalUserEntity.BY_NAME, query = "from PortalUserEntity as e where e.name = ?"),
	@NamedQuery(name = PortalUserEntity.BY_NAME_OTHER_ID, query = "from PortalUserEntity as e where e.name = ? and e.id != ?"),
	@NamedQuery(name = PortalUserEntity.BY_EMAIL, query = "from PortalUserEntity as e where lower(e.email) = lower(?)")})
public class PortalUserEntity {
	/** Query by name. */
	public static final String BY_NAME = "portalUser.byName";
	/** Query by name excluding ID. */
	public static final String BY_NAME_OTHER_ID = "portalUser.byNameOtherId";
	/** Query by email. */
	public static final String BY_EMAIL = "portalUser.byEMail";
	/** Entity ID. */
	@Id
	@Type(type = "impeId")
	@Column(name = "ID", length = Lengths.UUID, nullable = false)
	private UUID id;
	/** Login name. */
	@Column(name = "NAME", length = Lengths.NAME, unique = true, nullable = false)
	private String name;
	/** Display name. */
	@Column(name = "DISPLAY_NAME", length = Lengths.DESCRIPTION)
	private String displayName;
	/** Email address. */
	@Column(name = "EMAIL", length = Lengths.NAME)
	private String email;
	/** Password. */
	@Column(name = "PASSWORD", length = Lengths.NAME)
	private String password;
	/** Is the user active?. */
	@Column(name = "ACTIVE", nullable = false)
	private boolean active = true;
	/** User properties. */
	@ElementCollection
	@JoinTable(name = "PORTAL_USER_PROPERTY", joinColumns = @JoinColumn(name = "PUSR_ID", nullable = false))
	@MapKeyColumn(name = "PROPERTY_NAME", length = Lengths.NAME)
	@Column(name = "PROPERTY_VALUE", length = Lengths.DESCRIPTION)
	private Map<String, String> properties;
	/** User roles. */
	@ElementCollection
	@JoinTable(name = "PORTAL_USER_ROLE", joinColumns = @JoinColumn(name = "PUSR_ID", nullable = false))
	@Column(name = "ROLE_NAME", length = Lengths.NAME)
	private Set<String> roles;

	/** Default constructor. */
	public PortalUserEntity() {
	}

	/**
	 * Constructor.
	 * @param id Entity ID.
	 */
	public PortalUserEntity(UUID id) {
		this.id = id;
	}

	/**
	 * @return The id
	 */
	public UUID getId() {
		return id;
	}

	/**
	 * Sets the id
	 * @param id the id to set.
	 */
	public void setId(UUID id) {
		this.id = id;
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
	 * Returns the email address.
	 * @return The email address.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email address.
	 * @param email The email address.
	 */
	public void setEmail(String email) {
		this.email = email;
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
	 * Returns user properties.
	 * @return The user properties.
	 */
	public Map<String, String> getProperties() {
		if (properties == null) {
			properties = Maps.newHashMap();
		}
		return properties;
	}

	/**
	 * Returns the user roles.
	 * @return The user roles.
	 */
	public Set<String> getRoles() {
		if (roles == null) {
			roles = Sets.newHashSet();
		}
		return roles;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		return (id == null) ? 0 : id.hashCode();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(Object obj) {
		if (obj instanceof PortalUserEntity) {
			final PortalUserEntity e = (PortalUserEntity) obj;
			return Objects.equal(getId(), e.getId());
		}
		return false;
	}

}
