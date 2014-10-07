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


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.isotrol.impe3.api.ETagMode;


/**
 * Portal cache value.
 * @author Andres Rodriguez
 */
@Embeddable
public class PortalCacheValue implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = 7086743993573822282L;
	/** Whether the configuration is inherited. */
	@Column(name = "PRTL_CACHE_INHERITED", nullable = true)
	private Boolean inherited;
	/** Whether the cache is active. */
	@Column(name = "PRTL_CACHE_ACTIVE", nullable = true)
	private Boolean active;
	/** Whether the cache is public. */
	@Column(name = "PRTL_CACHE_PUBLIC", nullable = true)
	private Boolean publicCache;
	/** Seconds to modification date. */
	@Column(name = "PRTL_CACHE_MODIFICATION", nullable = true)
	private Integer modification;
	/** Seconds to expiration date. */
	@Column(name = "PRTL_CACHE_EXPIRATION", nullable = true)
	private Integer expiration;
	/** ETag mode. */
	@Column(name = "PRTL_CACHE_ETAG", nullable = true)
	@Enumerated(EnumType.ORDINAL)
	private ETagMode eTagMode;

	/** Default constructor. */
	public PortalCacheValue(boolean inherited, boolean active, boolean publicCache, Integer modification,
		Integer expiration, ETagMode eTagMode) {
		this.inherited = inherited;
		this.active = active;
		this.publicCache = publicCache;
		this.modification = modification;
		this.expiration = expiration;
		this.eTagMode = eTagMode;
	}

	/** Default constructor. Only for hibernate. */
	@SuppressWarnings("unused")
	private PortalCacheValue() {
	}

	/**
	 * Returns whether the configuration is inherited.
	 * @return True if the configuration is inherited.
	 */
	public boolean isInherited() {
		return inherited != null ? inherited.booleanValue() : false;
	}

	/**
	 * Returns whether the cache is active.
	 * @return True if the cache is active.
	 */
	public boolean isActive() {
		return active != null ? active.booleanValue() : false;
	}

	/**
	 * Returns whether the cache is public.
	 * @return True if the cache is public.
	 */
	public boolean isPublicCache() {
		return publicCache != null ? publicCache.booleanValue() : false;
	}

	/**
	 * Returns the seconds to modification date.
	 * @return The seconds to modification date.
	 */
	public Integer getModification() {
		return modification;
	}

	/**
	 * Returns the seconds to expiration date.
	 * @return The seconds to expiration date.
	 */
	public Integer getExpiration() {
		return expiration;
	}

	/**
	 * Returns the ETag mode.
	 * @return The ETag mode.
	 */
	public ETagMode getETagMode() {
		return eTagMode != null ? eTagMode : ETagMode.OFF;
	}

}
