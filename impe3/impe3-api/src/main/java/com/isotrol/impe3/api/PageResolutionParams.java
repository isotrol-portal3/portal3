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

package com.isotrol.impe3.api;


import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Parameters for page resolution inside a portal.
 * @author Andres Rodriguez.
 */
public final class PageResolutionParams extends AbstractResolutionParams {
	/** Request context. */
	private final PortalRequestContext context;

	/**
	 * Constructor.
	 * @param params Locale params.
	 * @param resolved Resolved locale.
	 * @param context Request context.
	 */
	public PageResolutionParams(LocaleResolutionParams params, ResolvedLocale resolved, PortalRequestContext context) {
		super(params, resolved);
		this.context = checkNotNull(context);
	}

	/**
	 * Returns the request context.
	 * @return The request context.
	 */
	public PortalRequestContext getContext() {
		return context;
	}
}
