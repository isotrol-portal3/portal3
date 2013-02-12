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


import static com.google.common.base.Preconditions.checkArgument;

import java.net.URI;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.springframework.util.StringUtils;


/**
 * Entity that represents a routing domain.
 * @author Andres Rodriguez
 */
@Entity
@Table(name = "ROUTING_DOMAIN")
@NamedQuery(name = RoutingDomainEntity.BY_NAME, query = "from RoutingDomainEntity as e where e.name = ? and e.environment.id = ?")
public class RoutingDomainEntity extends OfEnvironment implements WithUpdated {
	/** Query by name and enviroment. */
	public static final String BY_NAME = "routing.byName";

	private static String checkBase(final String base, boolean absolute) {
		checkArgument(StringUtils.hasText(base));
		final URI uri = URI.create(base);
		if (absolute) {
			checkArgument(uri.isAbsolute());
		}
		return base;
	}

	/** Update record. */
	private Updated updated;
	/** Domain name. */
	@Column(name = "NAME", length = Lengths.NAME, unique = true, nullable = false)
	private String name;
	/** Domain description. */
	@Column(name = "DESCRIPTION", nullable = true, length = Lengths.DESCRIPTION)
	private String description;
	/** Online base URL. */
	@Column(name = "ONLINE_BASE", nullable = false, length = Lengths.DESCRIPTION)
	private String onlineBase;
	/** Online absolute base URI. */
	@Column(name = "ONLINE_BASE_ABS", nullable = true, length = Lengths.DESCRIPTION)
	private String onlineAbsBase;
	/** Offline base URL. */
	@Column(name = "OFFLINE_BASE", nullable = false, length = Lengths.DESCRIPTION)
	private String offlineBase;
	/** Offline absolute base URI.. */
	@Column(name = "OFFLINE_BASE_ABS", nullable = true, length = Lengths.DESCRIPTION)
	private String offlineAbsBase;

	/** Default constructor. */
	public RoutingDomainEntity() {
	}

	/**
	 * @see com.isotrol.impe3.pms.model.WithUpdated#getUpdated()
	 */
	public Updated getUpdated() {
		return updated;
	}

	/**
	 * @see com.isotrol.impe3.pms.model.WithUpdated#setUpdated(com.isotrol.impe3.pms.model.Updated)
	 */
	public void setUpdated(Updated updated) {
		this.updated = updated;
	}

	/**
	 * @see com.isotrol.impe3.pms.model.WithUpdated#setUpdated(com.isotrol.impe3.pms.model.UserEntity)
	 */
	public void setUpdated(UserEntity user) {
		setUpdated(new Updated(user));
	}

	/**
	 * Returns the routing domain name.
	 * @return The routing domain name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the routing domain description.
	 * @param name The routing domain description.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the routing domain description.
	 * @return The routing domain description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the routing domain description.
	 * @param description The routing domain description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the online base URL.
	 * @return The online base URL.
	 */
	public String getOnlineBase() {
		return onlineBase;
	}

	/**
	 * Sets the online base URL.
	 * @param onlineBase The online base URL.
	 */
	public void setOnlineBase(String onlineBase) {
		this.onlineBase = checkBase(onlineBase, false);
	}

	/**
	 * Returns the online absolute base URI.
	 * @return The online absolute base URI.
	 */
	public String getOnlineAbsBase() {
		return onlineAbsBase;
	}

	/**
	 * Sets the online absolute base URI.
	 * @param onlineBase The online absolute base URI.
	 */
	public void setOnlineAbsBase(String onlineAbsBase) {
		this.onlineAbsBase = onlineAbsBase;
	}

	/**
	 * Returns the offline base URL.
	 * @return The offline base URL.
	 */
	public String getOfflineBase() {
		return offlineBase;
	}

	/**
	 * Sets the offline base URL.
	 * @param offlineBase The offline base URL.
	 */
	public void setOfflineBase(String offlineBase) {
		this.offlineBase = checkBase(offlineBase, false);
	}

	/**
	 * Returns the offline absolute base URI.
	 * @return The offline absolute base URI.
	 */
	public String getOfflineAbsBase() {
		return offlineAbsBase;
	}

	/**
	 * Sets the offline absolute base URI.
	 * @param offlineBase The offline absolute base URI.
	 */
	public void setOfflineAbsBase(String offlineAbsBase) {
		this.offlineAbsBase = offlineAbsBase;
	}

}
