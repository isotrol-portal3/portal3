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

package com.isotrol.impe3.pms.api.page;


import com.isotrol.impe3.pms.api.WithId;


/**
 * DTO for a page locator.
 * @author Andres Rodriguez
 */
public class PageLoc extends PortalPagesLoc implements WithId {
	/** Serial UID. */
	private static final long serialVersionUID = 3221239793112744169L;
	/** Page id. */
	private String id;

	/** Default constructor. */
	public PageLoc() {
	}

	/**
	 * Copy constructor.
	 * @param loc Source DTO.
	 */
	public PageLoc(PageLoc loc) {
		super(loc);
		this.id = loc.id;
	}

	/**
	 * Constructor.
	 * @param portalId Portal Id.
	 * @param deviceId Device Id.
	 * @param id Page Id.
	 */
	public PageLoc(String portalId, String deviceId, String id) {
		super(portalId, deviceId);
		this.id = id;
	}

	/**
	 * Constructor.
	 * @param loc Portal Pages locator.
	 * @param id Page Id.
	 */
	public PageLoc(PortalPagesLoc loc, String id) {
		super(loc);
		this.id = id;
	}

	/**
	 * Returns the page id.
	 * @return The page id.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the page id.
	 * @param id The page id.
	 */
	public void setId(String id) {
		this.id = id;
	}
}
