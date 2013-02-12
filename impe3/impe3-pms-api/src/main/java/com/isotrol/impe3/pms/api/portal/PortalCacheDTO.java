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

package com.isotrol.impe3.pms.api.portal;


import com.isotrol.impe3.api.ETagMode;
import com.isotrol.impe3.pms.api.AbstractWithId;


/**
 * Portal cache DTO.
 * @author Andres Rodriguez
 */
public class PortalCacheDTO extends AbstractWithId {
	/** Serial UID. */
	private static final long serialVersionUID = 8918161013580991960L;
	/** Whether the configuration is inherited. */
	private boolean inherited;
	/** Whether the cache is active. */
	private boolean active;
	/** Whether the cache is public. */
	private boolean publicCache;
	/** Seconds to modification date. */
	private Integer modification;
	/** Seconds to expiration date. */
	private Integer expiration;
	/** ETag mode. */
	private ETagMode eTagMode;

	/** Default constructor. */
	public PortalCacheDTO() {
	}

	/**
	 * Returns whether the configuration is inherited.
	 * @return True if the configuration is inherited.
	 */
	public boolean isInherited() {
		return inherited;
	}

	/**
	 * Sets whether the configuration is inherited.
	 * @param inherited True if the configuration is inherited.
	 */
	public void setInherited(boolean inherited) {
		this.inherited = inherited;
	}

	/**
	 * Returns whether the cache is active.
	 * @return True if the cache is active.
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets whether the cache is active.
	 * @param active True if the cache is active.
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Returns whether the cache is public.
	 * @return True if the cache is public.
	 */
	public boolean isPublicCache() {
		return publicCache;
	}

	/**
	 * Sets whether the cache is public.
	 * @param publicCache True if the cache is public.
	 */
	public void setPublicCache(boolean publicCache) {
		this.publicCache = publicCache;
	}

	/**
	 * Returns the seconds to modification date.
	 * @return The seconds to modification date.
	 */
	public Integer getModification() {
		return modification;
	}

	/**
	 * Sets the seconds to modification date.
	 * @param modification The seconds to modification date.
	 */
	public void setModification(Integer modification) {
		this.modification = modification;
	}

	/**
	 * Returns the seconds to expiration date.
	 * @return The seconds to expiration date.
	 */
	public Integer getExpiration() {
		return expiration;
	}

	/**
	 * Sets the seconds to expiration date.
	 * @param expiration The seconds to expiration date.
	 */
	public void setExpiration(Integer expiration) {
		this.expiration = expiration;
	}
	
	/**
	 * Returns the ETag mode.
	 * @return The ETag mode.
	 */
	public ETagMode getETagMode() {
		return eTagMode;
	}
	
	/**
	 * Sets the ETag mode.
	 * @param eTagMode The ETag mode.
	 */
	public void setETagMode(ETagMode eTagMode) {
		this.eTagMode = eTagMode;
	}
}
