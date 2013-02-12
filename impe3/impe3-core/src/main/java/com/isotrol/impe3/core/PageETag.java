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

import java.math.BigInteger;
import java.util.Date;

import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response.ResponseBuilder;


/**
 * Compound Page ETag.
 * @author Andres Rodriguez
 */
public abstract class PageETag {
	/** Disabled ETag support. */
	private static final Disabled DISABLED = new Disabled();

	static PageETag disabled() {
		return DISABLED;
	}

	static PageETag enabled(byte[] digest) {
		return digest != null ? new Enabled(digest) : DISABLED;
	}

	/** Constructor. */
	private PageETag() {
	}

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
	public abstract ResponseBuilder evalutatePreconditions(Request request, Date lastModified);

	private static final class Disabled extends PageETag {
		private Disabled() {
		}

		@Override
		public void apply(ResponseBuilder response) {
			// TODO Send cache-control no cache
		}

		@Override
		public ResponseBuilder evalutatePreconditions(Request request, Date lastModified) {
			if (request != null && lastModified != null) {
				return request.evaluatePreconditions(lastModified);
			}
			return null;
		}

	}

	private static final class Enabled extends PageETag {
		/** Page ETag. */
		private final String tag;

		/** Constructor. */
		private Enabled(byte[] digest) {
			tag = new BigInteger(checkNotNull(digest)).toString(16);
		}

		@Override
		public void apply(ResponseBuilder response) {
			response.tag(tag);
		}

		@Override
		public ResponseBuilder evalutatePreconditions(Request request, Date lastModified) {
			if (request != null) {
				final EntityTag t = new EntityTag(tag);
				if (lastModified == null) {
					return request.evaluatePreconditions(t);
				}
				return request.evaluatePreconditions(lastModified, t);
			}
			return null;
		}
	}

}
