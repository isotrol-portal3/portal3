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


import java.net.URI;
import java.util.Set;

import com.google.common.collect.Multimap;
import com.isotrol.impe3.api.Pagination;
import com.isotrol.impe3.api.component.RenderContext;


/**
 * Forwarding render context.
 * @author Andres Rodriguez.
 */
public abstract class ForwardingRenderContext extends ForwardingComponentRequestContext implements RenderContext {
	/** Default constructor. */
	public ForwardingRenderContext() {
	}

	protected abstract RenderContext delegate();

	public Integer getWidth() {
		return delegate().getWidth();
	}

	public URI getSamePageURI(Multimap<String, ?> parameters) {
		return delegate().getSamePageURI(parameters);
	}

	public URI getSamePageURI(Set<String> remove, Multimap<String, ?> parameters) {
		return delegate().getSamePageURI(remove, parameters);
	}

	public URI getAbsolutePageURI(Pagination pagination, int page) {
		return delegate().getAbsolutePageURI(pagination, page);
	}

	public URI getRelativePageURI(Pagination pagination, int delta) {
		return delegate().getRelativePageURI(pagination, delta);
	}

	public URI getFirstPageURI(Pagination pagination) {
		return delegate().getFirstPageURI(pagination);
	}

	public URI getLastPageURI(Pagination pagination) {
		return delegate().getLastPageURI(pagination);
	}
}
