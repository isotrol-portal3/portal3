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

import java.net.URI;
import java.util.UUID;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;


/**
 * Layout of a page.
 * @author Andres Rodriguez
 */
public final class LayoutResponse {
	public static Builder builder() {
		return new Builder();
	}

	/** Styles URIs. */
	private final ImmutableList<Style> styles;
	/** IE6 additional styles URIs. */
	private final ImmutableList<Style> ie6styles;
	/** IE7 additional styles URIs. */
	private final ImmutableList<Style> ie7styles;
	/** IE8 additional styles URIs. */
	private final ImmutableList<Style> ie8styles;
	/** Component markup. */
	private final ImmutableMap<UUID, String> markups;

	private LayoutResponse(Builder builder) {
		styles = builder.styles.build();
		ie6styles = builder.ie6styles.build();
		ie7styles = builder.ie7styles.build();
		ie8styles = builder.ie8styles.build();
		markups = builder.markups.build();
	}

	public ImmutableList<Style> getStyles() {
		return styles;
	}

	public ImmutableList<Style> getIE6Styles() {
		return ie6styles;
	}

	public ImmutableList<Style> getIE7Styles() {
		return ie7styles;
	}

	public ImmutableList<Style> getIE8Styles() {
		return ie8styles;
	}

	public ImmutableMap<UUID, String> getMarkups() {
		return markups;
	}

	public static final class Style {
		private final URI uri;
		private final String media;

		private Style(URI uri, String media) {
			this.uri = checkNotNull(uri);
			this.media = media;
		}

		public URI getUri() {
			return uri;
		}

		public String getMedia() {
			return media;
		}
	}

	public static class Builder implements com.isotrol.impe3.api.Builder<LayoutResponse> {
		/** Styles URIs. */
		private final ImmutableList.Builder<Style> styles = ImmutableList.builder();
		/** IE6 additional styles URIs. */
		private final ImmutableList.Builder<Style> ie6styles = ImmutableList.builder();
		/** IE7 additional styles URIs. */
		private final ImmutableList.Builder<Style> ie7styles = ImmutableList.builder();
		/** IE8 additional styles URIs. */
		private final ImmutableList.Builder<Style> ie8styles = ImmutableList.builder();
		/** Component markup. */
		private final ImmutableMap.Builder<UUID, String> markups = ImmutableMap.builder();

		private Builder() {
		}

		public Builder addStyle(URI uri, String media) {
			styles.add(new Style(uri, media));
			return this;
		}

		public Builder addIE6Style(URI uri, String media) {
			ie6styles.add(new Style(uri, media));
			return this;
		}

		public Builder addIE7Style(URI uri, String media) {
			ie7styles.add(new Style(uri, media));
			return this;
		}

		public Builder addIE8Style(URI uri, String media) {
			ie8styles.add(new Style(uri, media));
			return this;
		}

		public Builder putMarkup(UUID cipId, String markup) {
			markups.put(cipId, markup);
			return this;
		}

		public LayoutResponse get() {
			return new LayoutResponse(this);
		}
	}
}
