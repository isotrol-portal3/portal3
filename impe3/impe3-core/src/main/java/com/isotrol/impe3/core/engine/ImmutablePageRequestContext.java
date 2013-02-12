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

package com.isotrol.impe3.core.engine;


import static com.google.common.base.Preconditions.checkNotNull;

import com.isotrol.impe3.api.LocalParams;
import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.PageRequestContext;
import com.isotrol.impe3.api.PathSegments;
import com.isotrol.impe3.api.PortalRequestContext;
import com.isotrol.impe3.api.Route;


/**
 * Immutable implementation of PageRequestContext. This is a non-final class. Subclasses must guarantee immutability.
 * @author Andres Rodriguez
 */
public class ImmutablePageRequestContext extends ImmutablePortalRequestContext implements PageRequestContext {
	private final PathSegments path;
	private final Route route;
	private final PageKey pageKey;
	private final LocalParams localParams;
	private final Exception exception;

	ImmutablePageRequestContext(PortalRequestContext context, PathSegments path, PageKey pageKey,
		LocalParams localParams, Exception exception) {
		super(context);
		this.path = checkNotNull(path);
		this.pageKey = checkNotNull(pageKey);
		this.route = Route.of(context, pageKey);
		this.localParams = checkNotNull(localParams);
		this.exception = exception;
	}

	ImmutablePageRequestContext(PageRequestContext context, PageKey pageKey, LocalParams localParams,
		Exception exception) {
		super(context);
		this.path = checkNotNull(context.getPath());
		this.route = checkNotNull(context.getRoute());
		this.pageKey = (pageKey != null) ? pageKey : checkNotNull(context.getPageKey());
		this.localParams = (localParams != null) ? localParams : checkNotNull(context.getLocalParams());
		this.exception = (exception != null) ? exception : context.getException();
	}

	/**
	 * Constructor for action exception pages.
	 * @param context Portal request context.
	 * @param path Path segments.
	 * @param route Calling route.
	 * @param pageKey Error page key.
	 * @param localParams Local params.
	 * @param exception Exception.
	 */
	ImmutablePageRequestContext(PortalRequestContext context, PathSegments path, Route route, PageKey pageKey,
		LocalParams localParams, Exception exception) {
		super(context);
		this.path = checkNotNull(path);
		this.route = checkNotNull(route);
		this.pageKey = checkNotNull(pageKey);
		this.localParams = checkNotNull(localParams);
		this.exception = exception;
	}

	public final PathSegments getPath() {
		return path;
	}

	public final Route getRoute() {
		return route;
	}

	public final PageKey getPageKey() {
		return pageKey;
	}

	public final LocalParams getLocalParams() {
		return localParams;
	}

	public final Exception getException() {
		return exception;
	}

}
