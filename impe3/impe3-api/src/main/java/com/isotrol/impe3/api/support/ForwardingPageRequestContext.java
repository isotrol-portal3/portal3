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

package com.isotrol.impe3.api.support;


import com.isotrol.impe3.api.LocalParams;
import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.PageRequestContext;
import com.isotrol.impe3.api.PathSegments;
import com.isotrol.impe3.api.Route;


/**
 * Forwarding page request context.
 * @author Andres Rodriguez.
 */
public abstract class ForwardingPageRequestContext extends ForwardingPortalRequestContext implements PageRequestContext {
	/** Default constructor. */
	public ForwardingPageRequestContext() {
	}

	protected abstract PageRequestContext delegate();

	public PathSegments getPath() {
		return delegate().getPath();
	}

	public Route getRoute() {
		return delegate().getRoute();
	}

	public PageKey getPageKey() {
		return delegate().getPageKey();
	}
	
	public LocalParams getLocalParams() {
		return delegate().getLocalParams();
	}

	public Exception getException() {
		return delegate().getException();
	}

}
