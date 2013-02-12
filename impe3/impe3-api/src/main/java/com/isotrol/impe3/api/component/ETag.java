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

package com.isotrol.impe3.api.component;


import static com.google.common.base.Preconditions.checkNotNull;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import com.isotrol.impe3.api.Link;


/**
 * Value representing a partial ETag.
 * @author Andres Rodriguez
 */
public abstract class ETag implements Link {
	private static final ETag DISABLED = new Disabled();

	public static ETag disabled() {
		return DISABLED;
	}

	public static ETag of(String value) {
		return value != null ? new StringTag(value) : null;
	}

	public static ETag of(byte[] value) {
		return value != null ? new BytesTag(value) : null;
	}

	/** Constructor. */
	private ETag() {
	}

	/**
	 * Returns whether the value enables ETag support.
	 * @return True if the value enables ETag support.
	 */
	public abstract boolean isEnabled();

	/**
	 * Returns the entity tag content.
	 * @return The entity tag content.
	 * @throws UnsupportedOperationException for disabled support.
	 */
	public abstract ByteBuffer get();

	private static final class Disabled extends ETag {
		/** Constructor. */
		private Disabled() {
		}

		@Override
		public boolean isEnabled() {
			return false;
		}

		@Override
		public ByteBuffer get() {
			throw new UnsupportedOperationException("Disabled ETag");
		}
	}

	private static abstract class Enabled extends ETag {
		/** Constructor. */
		private Enabled() {
		}

		@Override
		public final boolean isEnabled() {
			return true;
		}
	}

	private static final class StringTag extends Enabled {
		private final String value;

		/** Constructor. */
		private StringTag(String value) {
			this.value = checkNotNull(value);
		}

		@Override
		public ByteBuffer get() {
			try {
				return ByteBuffer.wrap(value.getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException("No UTF-8 support", e);
			}
		}
	}

	private static final class BytesTag extends Enabled {
		private final byte[] value;

		/** Constructor. */
		private BytesTag(byte[] value) {
			this.value = checkNotNull(value);
		}

		@Override
		public ByteBuffer get() {
			return ByteBuffer.wrap(value);
		}
	}
}
