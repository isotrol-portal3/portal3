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

package com.isotrol.impe3.core;


import static com.google.common.base.Preconditions.checkNotNull;

import com.isotrol.impe3.api.PageRequestContext;


/**
 * Page context after path processing.
 * @author Andres Rodriguez.
 */
public final class PageContext {
	/** Portal Model. */
	private final PortalModel portalModel;
	/** Current request context. */
	private final PageRequestContext context;
	/** Resolved page. */
	private final Page page;

	public PageContext(PortalModel portalModel, PageRequestContext context, Page page) {
		this.portalModel = checkNotNull(portalModel);
		this.context = checkNotNull(context);
		this.page = checkNotNull(page);
	}

	/**
	 * Returns the current portal model.
	 * @return The current portal model.
	 */
	public PortalModel getPortalModel() {
		return portalModel;
	}

	/**
	 * Returns the current request context.
	 * @return The current request context.
	 */
	public PageRequestContext getContext() {
		return context;
	}

	/**
	 * Returns the resolved page.
	 * @return The resolved page.
	 */
	public Page getPage() {
		return page;
	}
}
