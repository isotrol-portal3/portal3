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

package com.isotrol.impe3.pms.api.rd;


/**
 * DTO for routing domain modification.
 * @author Andres Rodriguez
 */
public class RoutingDomainDTO extends RoutingDomainSelDTO {
	/** Serial UID. */
	private static final long serialVersionUID = -3243090976884552352L;
	/** Online base URL. */
	private String onlineBase;
	/** Online absolute base URI. */
	private String onlineAbsBase;
	/** Offline base URL. */
	private String offlineBase;
	/** Offline absolute base URI.. */
	private String offlineAbsBase;
	
	/**
	 * Default constructor.
	 */
	public RoutingDomainDTO() {
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
		this.onlineBase = onlineBase;
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
		this.offlineBase = offlineBase;
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
