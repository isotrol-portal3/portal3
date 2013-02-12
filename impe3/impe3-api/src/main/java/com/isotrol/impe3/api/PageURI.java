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

package com.isotrol.impe3.api;


import com.google.common.base.Function;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;


/**
 * Partial URI of a page.
 * @author Andres Rodriguez.
 */
public final class PageURI {
	private final PathSegments path;
	private final ImmutableMultimap<String, String> parameters;

	public PageURI(final PathSegments path, Multimap<String, String> parameters) {
		this.path = (path != null) ? path : PathSegments.of();
		if (parameters != null) {
			this.parameters = ImmutableMultimap.copyOf(parameters);
		} else {
			this.parameters = ImmutableMultimap.of();
		}
	}

	public PathSegments getPath() {
		return path;
	}

	public ImmutableMultimap<String, String> getParameters() {
		return parameters;
	}

	public PageURI add(PageURI uri) {
		if (uri == null) {
			return this;
		}
		final PathSegments _path = path.add(uri.path);
		final ImmutableMultimap.Builder<String, String> b = ImmutableMultimap.builder();
		b.putAll(parameters).putAll(uri.parameters);
		return new PageURI(_path, b.build());
	}

	public PageURI add(PathSegments path) {
		if (path == null || path.isEmpty()) {
			return this;
		}
		final PathSegments _path = this.path.add(path);
		return new PageURI(_path, parameters);
	}

	/**
	 * Apply a path segments transformer.
	 * @param pst Transformer to apply.
	 * @return The resulting URI.
	 */
	public PageURI apply(Function<PathSegments, PathSegments> pst) {
		if (pst == null) {
			return this;
		}
		final PathSegments _path = pst.apply(path);
		if (_path == null || _path == path) {
			return this;
		}
		return new PageURI(_path, parameters);
	}
}
