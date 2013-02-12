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


import java.io.Serializable;


/**
 * DTO with the URLs of a portal.
 * @author Andres Rodriguez
 */
public final class PortalURLsDTO implements Serializable {
	/** Serial UID. */
	private static final long serialVersionUID = -956283172162738144L;
	/** Offline URL. */
	private String offline;
	/** Online URL. */
	private String online;
	/** PMS URL. */
	private String pms;

	/** Default constructor. */
	public PortalURLsDTO() {
	}

	/**
	 * Returns the offline URL.
	 * @return The offline URL.
	 */
	public String getOffline() {
		return offline;
	}

	/**
	 * Sets the offline URL.
	 * @param offline The offline URL.
	 */
	public void setOffline(String offline) {
		this.offline = offline;
	}

	/**
	 * Returns the online URL.
	 * @return The online URL.
	 */
	public String getOnline() {
		return online;
	}

	/**
	 * Sets the online URL.
	 * @param online The online URL.
	 */
	public void setOnline(String online) {
		this.online = online;
	}

	/**
	 * Returns the PMS URL.
	 * @return The PMS URL.
	 */
	public String getPMS() {
		return pms;
	}

	/**
	 * Sets the PMS URL.
	 * @param pms The PMS URL.
	 */
	public void setPMS(String pms) {
		this.pms = pms;
	}
}
