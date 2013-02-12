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
import com.google.common.base.Functions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;


/**
 * Transformer functions for PathSegments.
 * @author Andres Rodriguez.
 */
public final class PathSegmentsTransformers {
	/** Not instantiable. */
	private PathSegmentsTransformers() {
		throw new AssertionError();
	}

	/**
	 * Returns a new transformer that inserts segments at the beginning of the provided path.
	 * @param segments Segments to add.
	 * @return The requested transformer.
	 */
	public static Function<PathSegments, PathSegments> insert(final PathSegments segments) {
		if (segments == null || segments.isEmpty()) {
			return Functions.identity();
		}
		return new Function<PathSegments, PathSegments>() {
			public PathSegments apply(PathSegments input) {
				if (input == null || input.isEmpty()) {
					return segments;
				}
				return segments.add(input);
			}

			public String toString() {
				return String.format("Insert transformer: %s", segments);
			};
		};
	}

	/**
	 * Returns a new transformer that appends segments to the provided path.
	 * @param segments Segments to append.
	 * @return The requested transformer.
	 */
	public static Function<PathSegments, PathSegments> append(final PathSegments segments) {
		if (segments == null || segments.isEmpty()) {
			return Functions.identity();
		}
		return new Function<PathSegments, PathSegments>() {
			public PathSegments apply(PathSegments input) {
				if (input == null || input.isEmpty()) {
					return segments;
				}
				return input.add(segments);
			}

			public String toString() {
				return String.format("Append transformer: %s", segments);
			};
		};
	}

	/**
	 * Returns a new transformer that appends an extension to the last segment of the provided path.
	 * @param extension Extension to append.
	 * @return The requested transformer.
	 */
	public static Function<PathSegments, PathSegments> extension(final String extension) {
		if (extension == null || extension.length() == 0) {
			return Functions.identity();
		}
		return new Function<PathSegments, PathSegments>() {
			public PathSegments apply(PathSegments input) {
				if (input == null || input.isEmpty()) {
					return input;
				}
				String last = input.last();
				if (last == null || last.length() == 0) {
					return input;
				}
				last = new StringBuilder(last).append('.').append(extension).toString();
				return PathSegments.of(false,
					Iterables.concat(input.consumeLast(), ImmutableList.of(last)));
			}

			public String toString() {
				return String.format("Extension transformer: %s", extension);
			};
		};
	}
}
