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

import java.net.URI;
import java.util.Set;

import javax.ws.rs.core.UriBuilder;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.isotrol.impe3.api.Pagination;
import com.isotrol.impe3.api.component.ComponentRequestContext;
import com.isotrol.impe3.api.component.RenderContext;
import com.isotrol.impe3.api.support.ForwardingComponentRequestContext;
import com.isotrol.impe3.api.support.URIs;


/**
 * Immutable render context.
 * @author Andres Rodriguez.
 */
public final class ImmutableRenderContext extends ForwardingComponentRequestContext implements RenderContext {
	private final ComponentRequestContext delegate;
	private final Integer width;
	private final Multimap<String, String> query;

	ImmutableRenderContext(final ComponentRequestContext delegate, final Integer width,
		final Multimap<String, String> query) {
		this.delegate = checkNotNull(delegate);
		this.width = width;
		if (query == null) {
			this.query = ImmutableMultimap.of();
		} else {
			this.query = ImmutableMultimap.copyOf(query);
		}
	}

	@Override
	protected ComponentRequestContext delegate() {
		return delegate;
	}

	public Integer getWidth() {
		return width;
	}

	public URI getSamePageURI(Multimap<String, ?> parameters) {
		final UriBuilder b = UriBuilder.fromUri("");
		URIs.queryParameters(b, query);
		URIs.queryParameters(b, parameters);
		return b.build();
	}

	public URI getSamePageURI(Set<String> remove, Multimap<String, ?> parameters) {
		final UriBuilder b = UriBuilder.fromUri("");
		if (remove == null || remove.isEmpty()) {
			return getSamePageURI(parameters);
		}
		final Multimap<String, ?> qp = ArrayListMultimap.create(query);
		for (String r : remove) {
			qp.removeAll(r);
		}
		URIs.queryParameters(b, qp);
		URIs.queryParameters(b, parameters);
		return b.build();
	}

	/**
	 * @see com.isotrol.impe3.api.component.RenderContext#getAbsolutePageURI(com.isotrol.impe3.api.Pagination, int)
	 */
	public URI getAbsolutePageURI(Pagination pagination, int page) {
		Multimap<String, Object> parameters = ImmutableMultimap.of();
		Set<String> remove = ImmutableSet.of();
		if (pagination != null && page >= 1) {
			final String param = pagination.getParameter();
			parameters = ImmutableMultimap.<String, Object> of(param, page);
			remove = ImmutableSet.of(param);
		}
		return getSamePageURI(remove, parameters);
	}

	public URI getRelativePageURI(Pagination pagination, int delta) {
		int page = -1;
		if (pagination != null) {
			page = pagination.getPage() + delta;
		}
		return getAbsolutePageURI(pagination, page);
	}

	public URI getFirstPageURI(Pagination pagination) {
		return getAbsolutePageURI(pagination, 1);
	}

	public URI getLastPageURI(Pagination pagination) {
		int page = -1;
		if (pagination != null) {
			final Integer last = pagination.getPages();
			if (last != null) {
				page = last.intValue();
			}
		}
		return getAbsolutePageURI(pagination, page);
	}
}
