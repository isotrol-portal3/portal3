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

package com.isotrol.impe3.palette.cloud;


import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URI;


public final class CloudItem {
	/** Item name. */
	private final String name;
	/** Item URI. */
	private final String uri;
	/** Weight factor. */
	private final double weight;

	public CloudItem(final String name, final String uri, double weight) {
		this.name = checkNotNull(name);
		this.uri = (uri != null) ? uri : "";
		this.weight = weight;
	}

	public CloudItem(final String name, final URI uri, double weight) {
		this(name, uri != null ? uri.toASCIIString() : "", weight);
	}

	public String getName() {
		return name;
	}

	public String getUri() {
		return uri;
	}

	public double getWeight() {
		return weight;
	}
}