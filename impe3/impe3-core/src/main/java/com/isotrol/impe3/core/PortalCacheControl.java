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


import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.isotrol.impe3.api.ETagMode;
import com.isotrol.impe3.api.PortalRequestContext;
import com.isotrol.impe3.api.component.CacheMode;
import com.isotrol.impe3.api.component.CacheScope;
import com.isotrol.impe3.api.component.ComponentETagMode;
import com.isotrol.impe3.api.component.ETag;
import com.isotrol.impe3.api.component.Expires;
import com.isotrol.impe3.api.component.LastModified;


/**
 * Portal cache control. THIS CLASS IS NOT THREAD SAFE.
 * @author Andres Rodriguez
 */
public abstract class PortalCacheControl {
	/** Disabled cache. */
	private static final Off OFF = new Off();

	static PortalCacheControl off() {
		return OFF;
	}

	static PortalCacheControl on(PortalRequestContext context, boolean publicCache, Integer modification,
		Integer expiration, ETagMode eTagMode) {
		return new On(context, publicCache, modification, expiration, eTagMode);
	}

	/** Constructor. */
	private PortalCacheControl() {
	}

	/**
	 * Returns whether cache control is on.
	 * @return True if cache control is on.
	 */
	public abstract boolean isOn();

	/**
	 * Applies the cache control to the response.
	 * @param response Response builder to use.
	 */
	public abstract void apply(ResponseBuilder response);

	/**
	 * Evaluate request preconditions based on the passed in value.
	 * @return null if the preconditions are met or a ResponseBuilder set with the appropriate status if the
	 * preconditions are not met.
	 */
	public abstract ResponseBuilder evalutatePreconditions();

	/**
	 * Sets the cache model.
	 * @param cacheMode The cache mode.
	 * @return The object to use for subsequent calls.
	 */
	public abstract PortalCacheControl setCacheMode(CacheMode cacheMode);

	/**
	 * Sets the cache scope.
	 * @param scope The cache scope.
	 * @return The object to use for subsequent calls.
	 */
	public abstract PortalCacheControl setCacheScope(CacheScope cacheScope);

	/**
	 * Sets the modification date.
	 * @param modification The modification date.
	 * @return The object to use for subsequent calls.
	 */
	public abstract PortalCacheControl setModification(LastModified modification);

	/**
	 * Sets the seconds to expiration date.
	 * @param expiration The seconds to expiration date.
	 * @return The object to use for subsequent calls.
	 */
	public abstract PortalCacheControl setExpiration(Expires expiration);

	/**
	 * Adds a component's partial ETag.
	 * @param cipId CIP Id.
	 * @param mode Component ETag mode.
	 * @param tag Partial tag.
	 * @return The object to use for subsequent calls.
	 */
	public abstract PortalCacheControl putEtag(UUID cipId, ComponentETagMode mode, ETag tag);

	private static final class Off extends PortalCacheControl {
		private Off() {
		}

		@Override
		public boolean isOn() {
			return false;
		}

		@Override
		public void apply(ResponseBuilder response) {
			// TODO Send cache-control no cache
		}

		@Override
		public ResponseBuilder evalutatePreconditions() {
			return null;
		}

		@Override
		public PortalCacheControl setCacheMode(CacheMode cacheMode) {
			return this;
		}

		@Override
		public PortalCacheControl setCacheScope(CacheScope cacheScope) {
			return this;
		}

		@Override
		public PortalCacheControl setModification(LastModified modification) {
			return this;
		}

		@Override
		public PortalCacheControl setExpiration(Expires expiration) {
			return this;
		}

		@Override
		public PortalCacheControl putEtag(UUID cipId, ComponentETagMode mode, ETag tag) {
			return this;
		}
	}

	private static final class On extends PortalCacheControl {
		/** JAX-RS Request. */
		private Request request;
		/** Whether the cache is public. */
		private boolean publicCache;
		/** Modification date. */
		private long modification;
		/** Expiration date. */
		private long expiration;
		/** ETag mode. */
		private ETagBuilder tagBuilder;
		/** ETag. */
		private PageETag tag = null;

		/** Constructor. */
		private On(PortalRequestContext context, boolean publicCache, Integer modification, Integer expiration,
			ETagMode eTagMode) {
			this.request = context.getJAXRSRequest();
			final long now = Calendar.getInstance().getTimeInMillis();
			this.publicCache = publicCache;
			this.modification = modification != null ? (now - modification * 1000) : context.getPortalModelInfo()
				.getTimestamp();
			this.expiration = expiration != null ? (now + expiration * 1000) : now;
			this.tagBuilder = ETagBuilder.enabled(eTagMode, context);
		}

		private PageETag tag() {
			if (tag == null) {
				tag = tagBuilder.get();
			}
			return tag;
		}

		@Override
		public boolean isOn() {
			return true;
		}

		@Override
		public void apply(ResponseBuilder response) {
			CacheControl control = new CacheControl();
			control.setPrivate(!publicCache);
			response.cacheControl(control);
			response.lastModified(new Date(modification));
			response.expires(new Date(expiration));
			tag().apply(response);
		}

		@Override
		public ResponseBuilder evalutatePreconditions() {
			return tag().evalutatePreconditions(request, new Date(modification));
		}

		@Override
		public PortalCacheControl setCacheMode(CacheMode cacheMode) {
			if (cacheMode == null || cacheMode == CacheMode.ON) {
				return this;
			}
			return OFF;
		}

		@Override
		public PortalCacheControl setCacheScope(CacheScope cacheScope) {
			if (cacheScope != null && publicCache) {
				publicCache = cacheScope == CacheScope.PUBLIC;
			}
			return this;
		}

		@Override
		public PortalCacheControl setModification(LastModified modification) {
			if (modification != null) {
				long t = modification.get();
				if (t > this.modification) {
					this.modification = t;
				}
			}
			return this;
		}

		@Override
		public PortalCacheControl setExpiration(Expires expiration) {
			if (expiration != null) {
				long t = expiration.get();
				if (t < this.expiration) {
					this.expiration = t;
				}
			}
			return this;
		}

		@Override
		public PortalCacheControl putEtag(UUID cipId, ComponentETagMode mode, ETag tag) {
			tagBuilder = tagBuilder.put(cipId, mode, tag);
			return this;
		}
	}

}
