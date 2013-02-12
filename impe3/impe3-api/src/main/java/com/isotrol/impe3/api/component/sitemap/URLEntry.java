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

package com.isotrol.impe3.api.component.sitemap;


import java.net.URI;
import java.util.Date;

import com.google.common.base.Preconditions;


/**
 * Sitemap URL entry.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez
 */
public final class URLEntry {
	/** Entry URI. */
	private final URI uri;
	/** Last modification. */
	private final Long lastModified;
	/** Change frequency. */
	private final Changefreq changefreq;
	/** Priority. From 1,0 to 0,0 (default 0,5) */
	private final Float priority;

	/**
	 * Creates a new entry with no optional attributes.
	 * @param uri URI entry location.
	 * @return The requested entry.
	 * @throws NullPointerException if the argument is {@code null}.
	 */
	public static URLEntry of(URI uri) {
		return builder(uri).build();
	}

	/**
	 * Creates a new entry with no optional attributes.
	 * @param uri URI entry location.
	 * @return The requested entry.
	 * @throws NullPointerException if the argument is {@code null}.
	 * @throws IllegalArgumentException if the given argument violates RFC 2396.
	 */
	public static URLEntry of(String uri) {
		return builder(uri).build();
	}

	/**
	 * Creates a new builder.
	 * @param uri URI entry location.
	 * @return The requested builder.
	 * @throws NullPointerException if the argument is {@code null}.
	 */
	public static Builder builder(URI uri) {
		return new Builder(uri);
	}

	/**
	 * Creates a new builder.
	 * @param uri URI entry location.
	 * @return The requested builder.
	 * @throws NullPointerException if the argument is {@code null}.
	 * @throws IllegalArgumentException if the given argument violates RFC 2396.
	 */
	public static Builder builder(String uri) {
		return new Builder(URI.create(uri));
	}

	private URLEntry(Builder b) {
		this.uri = b.uri;
		this.lastModified = b.lastModified;
		this.changefreq = b.changefreq;
		this.priority = b.priority;
	}

	/** Returns the entry URI. */
	public URI getUri() {
		return uri;
	}

	/** Returns the last modification date. */
	public Long getLastModified() {
		return lastModified;
	}

	/** Returns the change frequency. */
	public Changefreq getChangefreq() {
		return changefreq;
	}

	/** Returns the priority. */
	public Float getPriority() {
		return priority;
	}

	public static final class Builder {
		/** Entry URI. */
		private final URI uri;
		/** Last modification. */
		private Long lastModified = null;
		/** Change frequency. */
		private Changefreq changefreq = null;
		/** Priority. From 1,0 to 0,0 (default 0,5) */
		private Float priority = null;

		private Builder(URI uri) {
			this.uri = Preconditions.checkNotNull(uri);
		}

		/**
		 * Sets the change frequency.
		 * @param changefreq The change frequency (optional).
		 * @return This builder.
		 */
		public Builder setChangefreq(Changefreq changefreq) {
			this.changefreq = changefreq;
			return this;
		}

		/**
		 * Sets the last modification date.
		 * @param lastModified The last modification date (optional).
		 * @return This builder.
		 */
		public Builder setLastModified(long lastModified) {
			this.lastModified = lastModified;
			return this;
		}

		/**
		 * Sets the last modification date.
		 * @param lastModified The las modification date.
		 * @return This builder.
		 */
		public Builder setLastModified(Date lastModified) {
			if (lastModified == null) {
				this.lastModified = null;
			} else {
				this.lastModified = lastModified.getTime();
			}
			return this;
		}

		/**
		 * Returns the entry priority (0 - 1). The priority is optional with {@code null} considered 0.5.
		 * @param priority The priority. If out of rane a value of {@code null} will be used.
		 * @return This builder.
		 */
		public Builder setPriority(Float priority) {
			if (priority == null) {
				this.priority = null;
			} else {
				float p = priority.floatValue();
				if (p == 0.5f || p < 0 || p > 1) {
					this.priority = p;
				} else {
					this.priority = null;
				}
			}
			return this;
		}

		/**
		 * Builds the entry.
		 */
		public URLEntry build() {
			return new URLEntry(this);
		}
	}
}
