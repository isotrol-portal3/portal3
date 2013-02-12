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

package com.isotrol.impe3.pms.core.impl;


import java.io.Serializable;
import java.util.UUID;

import com.google.common.base.Preconditions;
import com.isotrol.impe3.api.Identifiable;


/**
 * Security context object. Hold the current user's id.
 * @author Andres Rodriguez
 */
public final class SecurityContext implements Identifiable, Serializable {
	private static final long serialVersionUID = 8904438851408156467L;

	/** Context holder. */
	private static final ThreadLocal<SecurityContext> holder = new ThreadLocal<SecurityContext>();

	/**
	 * Returns the current security context.
	 * @return The current security context.
	 */
	public static SecurityContext get() {
		return holder.get();
	}

	/**
	 * Sets the current security context.
	 * @param context The current security context.
	 */
	public static void set(SecurityContext context) {
		holder.set(context);
	}

	/** User Id. */
	private UUID userId;

	/**
	 * Constructor.
	 * @param userId User Id.
	 */
	SecurityContext(UUID userId) {
		this.userId = Preconditions.checkNotNull(userId);
	}

	@SuppressWarnings("unused")
	private SecurityContext() {
	}

	public UUID getId() {
		return userId;
	}

	public String getStringId() {
		return userId.toString();
	}

}
