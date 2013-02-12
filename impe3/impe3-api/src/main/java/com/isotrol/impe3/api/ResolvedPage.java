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


import javax.ws.rs.core.Response;


/**
 * Resolved page inside a portal.
 * @author Andres Rodriguez.
 */
public final class ResolvedPage extends Resolved {
	/** Resolved page key. */
	private final PageKey page;

	/**
	 * Constructor for an interrupting resolution.
	 * @param response Response.
	 */
	public ResolvedPage(Response response) {
		super(response);
		this.page = null;
	}

	/**
	 * Constructor for a normal resolution.
	 * @param path Remaining path segments (not {@code null}).
	 * @param page Resolved page key (maybe {@code null})
	 * @param parameters Local parameters (not {@code null}).
	 */
	public ResolvedPage(final PathSegments path, final PageKey page, final LocalParams parameters) {
		super(path, parameters);
		this.page = page;
	}

	/**
	 * Returns the resolved page key.
	 * @return The resolved page key.
	 */
	public PageKey getPage() {
		return page;
	}
}
