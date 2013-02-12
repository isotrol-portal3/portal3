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


import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import com.google.common.base.Preconditions;
import com.isotrol.impe3.api.Builder;
import com.isotrol.impe3.api.ETagMode;
import com.isotrol.impe3.api.PortalRequestContext;
import com.isotrol.impe3.api.component.ComponentETagMode;
import com.isotrol.impe3.api.component.ETag;


/**
 * ETag builder. THIS CLASS IS NOT THREAD SAFE.
 * @author Andres Rodriguez
 */
public abstract class ETagBuilder implements Builder<PageETag> {
	/** Disabled cache. */
	private static final Disabled DISABLED = new Disabled();

	static ETagBuilder off() {
		return DISABLED;
	}

	static ETagBuilder enabled(ETagMode mode, PortalRequestContext context) {
		if (mode == ETagMode.OFF) {
			return DISABLED;
		}
		return new Enabled(mode, context);
	}

	/** Constructor. */
	private ETagBuilder() {
	}
	
	/**
	 * Adds a component's partial ETag.
	 * @param cipId CIP Id.
	 * @param mode Component ETag mode.
	 * @param tag Partial tag.
	 * @return The builder to use to add the next components.
	 */
	abstract ETagBuilder put(UUID cipId, ComponentETagMode mode, ETag tag);

	private static final class Disabled extends ETagBuilder {
		private Disabled() {
		}

		public PageETag get() {
			return PageETag.disabled();
		}

		@Override
		ETagBuilder put(UUID cipId, ComponentETagMode mode, ETag tag) {
			return this;
		}
	}

	private static final class Enabled extends ETagBuilder {
		/** ETag Mode. */
		private final ETagMode mode;
		/** Message digest. */
		private final MessageDigest digest;

		private static ByteBuffer put(ByteBuffer b, UUID id) {
			return b.putLong(id.getMostSignificantBits()).putLong(id.getLeastSignificantBits());
		}

		private static ByteBuffer put(UUID id) {
			return put(ByteBuffer.allocate(16), id);
		}

		/** Constructor. */
		private Enabled(ETagMode mode, PortalRequestContext context) {
			this.mode = Preconditions.checkNotNull(mode);
			try {
				this.digest = MessageDigest.getInstance("SHA");
			} catch (NoSuchAlgorithmException e) {
				throw new UnsupportedOperationException("No SHA algorithm", e);
			}
			final ByteBuffer bid = ByteBuffer.allocate(16 * 3);
			put(bid, context.getPortalId());
			put(bid, context.getPortalModelInfo().getVersion());
			put(bid, context.getDevice().getId());
			this.digest.update(bid);
			try {
				this.digest.update(context.getLocale().toString().getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				throw new UnsupportedOperationException("No UTF-8 encoding", e);
			}
		}

		public PageETag get() {
			return PageETag.enabled(digest.digest());
		}

		@Override
		ETagBuilder put(UUID cipId, ComponentETagMode cmode, ETag tag) {
			if (mode == ETagMode.OFF || cmode == ComponentETagMode.DISABLED) {
				return DISABLED;
			}
			if (cmode == ComponentETagMode.IGNORE) {
				return this;
			}
			if (tag == null) {
				return mode == ETagMode.LAX ? this : DISABLED;
			}
			if (!tag.isEnabled()) {
				return DISABLED;
			}
			this.digest.update(put(cipId));
			this.digest.update(tag.get());
			return this;
		}
	}

}
