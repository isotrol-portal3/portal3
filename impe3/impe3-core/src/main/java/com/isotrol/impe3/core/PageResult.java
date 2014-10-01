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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.isotrol.impe3.api.PageRequestContext;
import com.isotrol.impe3.api.Portal;
import com.isotrol.impe3.api.component.CacheMode;
import com.isotrol.impe3.api.component.CacheScope;
import com.isotrol.impe3.api.component.ComponentETagMode;
import com.isotrol.impe3.api.component.ComponentRenderer;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.component.ETag;
import com.isotrol.impe3.api.component.Expires;
import com.isotrol.impe3.api.component.LastModified;
import com.isotrol.impe3.api.component.RenderContext;


/**
 * Page execution result. This class is NOT THREAD SAFE.
 * @author Andres Rodriguez
 */
public abstract class PageResult {
	/** Portal that generated the response. */
	private final Portal portal;
	/** Session data. */
	private final Map<String, Object> session;

	/**
	 * Returns a new page result builder.
	 * @param context Page context.
	 * @return The requested builder.
	 */
	public static Builder builder(PageContext context) {
		return new Builder(context);
	}

	private PageResult(Builder b) {
		this.portal = b.pageContext.getPortalModel().getPortal();
		this.session = Collections.unmodifiableMap(b.session);
	}
	
	public final Portal getPortal() {
		return portal;
	}

	public final Map<String, Object> getSession() {
		return session;
	}

	public static final class Early extends PageResult {
		private final Response response;

		private Early(Builder b, Response response) {
			super(b);
			this.response = checkNotNull(response);
		}

		public PageResponse getPageResponse() {
			return new PageResponse(getPortal(), response, getSession());
		}
	}

	public static final class Redirection extends PageResult {
		private final ComponentResponse.Redirection redirection;

		private Redirection(Builder b, ComponentResponse.Redirection redirection) {
			super(b);
			this.redirection = checkNotNull(redirection);
		}

		public ComponentResponse.Redirection getRedirection() {
			return redirection;
		}
	}

	public static final class Ok extends PageResult {
		private final PageRequestContext context;
		private final PortalCacheControl cache;
		private final ImmutableMultimap<String, String> headers;
		private final ImmutableList<NewCookie> cookies;
		private final ImmutableMultimap<String, String> query;
		private final ImmutableMap<UUID, CIPResult> cips;

		private Ok(Builder b) {
			super(b);
			this.context = b.pageContext.getContext();
			this.cache = b.cacheControl;
			this.headers = ImmutableMultimap.copyOf(b.headers);
			this.cookies = ImmutableList.copyOf(b.cookies);
			this.query = ImmutableMultimap.copyOf(b.query);
			this.cips = ImmutableMap.copyOf(b.cips);
		}

		public PageRequestContext getContext() {
			return context;
		}

		public ImmutableMultimap<String, String> getQuery() {
			return query;
		}

		public ImmutableMap<UUID, CIPResult> getCips() {
			return cips;
		}

		public <R extends ComponentRenderer> Map<UUID, R> getRenderers(final Class<R> rendererType,
			final Function<CIP, RenderContext> contextFactory) {
			final Function<CIPResult, R> f = new Function<CIPResult, R>() {
				public R apply(CIPResult from) {
					final RenderContext context = contextFactory.apply(from.getCip());
					final R renderer = from.getCip().getRenderer(rendererType, from.getComponent(), context);
					return renderer;
				}
			};
			return Maps.newLinkedHashMap(Maps.filterValues(Maps.transformValues(cips, f), Predicates.notNull()));
		}

		/**
		 * Applies the result to the response.
		 * @param response Response builder to use.
		 */
		public void apply(ResponseBuilder response) {
			// Set headers
			for (Entry<String, String> header : headers.entries()) {
				response.header(header.getKey(), header.getValue());
			}
			// Set cookies
			for (NewCookie cookie : cookies) {
				if (cookie != null) {
					response.cookie(cookie);
				}
			}
			// Cache
			cache.apply(response);
		}

	}

	public static final class Builder {
		private final PageContext pageContext;
		private final Multimap<String, String> headers = LinkedListMultimap.create();
		private final List<NewCookie> cookies = Lists.newArrayList();
		private final Map<String, Object> session = Maps.newHashMap();
		private final Multimap<String, String> query = LinkedListMultimap.create();
		private final Map<UUID, CIPResult> cips = Maps.newLinkedHashMap();
		private PortalCacheControl cacheControl;
		private boolean built = false;

		private Builder(PageContext pageContext) {
			this.pageContext = pageContext;
			this.cacheControl = pageContext.getPortalModel().getCacheModel().getControl(pageContext.getContext());
		}

		private void notBuilt() {
			Preconditions.checkState(!built, "Page Result already built");
		}

		public Builder headers(Multimap<String, String> h) {
			notBuilt();
			this.headers.putAll(h);
			return this;
		}

		public Builder cookies(List<NewCookie> c) {
			notBuilt();
			this.cookies.addAll(c);
			return this;
		}

		public Builder session(Map<String, Object> s) {
			notBuilt();
			this.session.putAll(s);
			return this;
		}

		public Builder query(Multimap<String, String> q) {
			notBuilt();
			this.query.putAll(q);
			return this;
		}

		public Builder cip(UUID cipId, CIPResult result) {
			notBuilt();
			cips.put(cipId, result);
			return this;
		}

		/**
		 * Returns whether cache control is on.
		 * @return True if cache control is on.
		 */
		public boolean isCacheOn() {
			notBuilt();
			return cacheControl.isOn();
		}

		/**
		 * Sets the cache model.
		 * @param cacheMode The cache mode.
		 * @return This builder.
		 */
		public Builder setCacheMode(CacheMode cacheMode) {
			notBuilt();
			this.cacheControl = cacheControl.setCacheMode(cacheMode);
			return this;
		}

		/**
		 * Sets the cache scope.
		 * @param scope The cache scope.
		 * @return This builder.
		 */
		public Builder setCacheScope(CacheScope cacheScope) {
			notBuilt();
			this.cacheControl = cacheControl.setCacheScope(cacheScope);
			return this;
		}

		/**
		 * Sets the modification date.
		 * @param modification The modification date.
		 * @return This builder.
		 */
		public Builder setModification(LastModified modification) {
			notBuilt();
			this.cacheControl = cacheControl.setModification(modification);
			return this;
		}

		/**
		 * Sets the seconds to expiration date.
		 * @param expiration The seconds to expiration date.
		 * @return This builder.
		 */
		public Builder setExpiration(Expires expiration) {
			notBuilt();
			this.cacheControl = cacheControl.setExpiration(expiration);
			return this;
		}

		/**
		 * Adds a component's partial ETag.
		 * @param cipId CIP Id.
		 * @param mode Component ETag mode.
		 * @param tag Partial tag.
		 * @return This builder.
		 */
		public Builder putETag(UUID cipId, ComponentETagMode mode, ETag tag) {
			notBuilt();
			this.cacheControl = cacheControl.putEtag(cipId, mode, tag);
			return this;
		}

		private Early early(Response response) {
			notBuilt();
			Early r = new Early(this, response);
			built = true;
			return r;
		}

		public Early evalutatePreconditions() {
			final ResponseBuilder rb = this.cacheControl.evalutatePreconditions();
			if (rb != null) {
				return early(rb.build());
			}
			return null;
		}

		public Redirection redirect(ComponentResponse.Redirection redirection) {
			notBuilt();
			Redirection r = new Redirection(this, redirection);
			built = true;
			return r;
		}

		public Ok ok() {
			notBuilt();
			Ok ok = new Ok(this);
			built = true;
			return ok;
		}

	}

}
