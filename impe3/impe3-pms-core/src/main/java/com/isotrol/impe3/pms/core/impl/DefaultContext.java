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


import java.util.Locale;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.isotrol.impe3.pms.core.PMSContext;


/**
 * Component that bootstraps the root user and the default environment.
 * @author Andres Rodriguez.
 */
@Component
public final class DefaultContext implements PMSContext {
	private final Locale SPANISH = new Locale("es", "ES");
	private UUID environmentId;

	/** Default constructor. */
	public DefaultContext() {
	}

	/**
	 * @see com.isotrol.impe3.pms.core.PMSContext#getEnvironmentId()
	 */
	public UUID getEnvironmentId() {
		return environmentId;
	}

	/**
	 * @see com.isotrol.impe3.pms.core.PMSContext#setEnvironmentId(java.util.UUID)
	 */
	public void setEnvironmentId(UUID environmentId) {
		this.environmentId = environmentId;
	}

	/**
	 * @see com.isotrol.impe3.pms.core.PMSContext#getUserId()
	 */
	public UUID getUserId() {
		final SecurityContext ctx = SecurityContext.get();
		return ctx != null ? ctx.getId() : null;
	}

	/**
	 * @see com.isotrol.impe3.pms.core.PMSContext#getLocale()
	 */
	public Locale getLocale() {
		return SPANISH;
	}
}
