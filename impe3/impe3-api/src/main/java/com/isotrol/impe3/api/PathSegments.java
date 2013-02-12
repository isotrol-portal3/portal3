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


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.ForwardingList;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;


/**
 * List of URI path segments.
 * @author Andres Rodriguez.
 */
public final class PathSegments extends ForwardingList<String> {
	private static final PathSegments EMPTY = new PathSegments(ImmutableList.<String> of());

	/**
	 * Returns an empty list of segments.
	 * @return An empty list of segments.
	 */
	public static PathSegments of() {
		return EMPTY;
	}

	/**
	 * Turns a single segment into a list of decoded segments.
	 * @param segment Segment.
	 * @param encoded If the segment is encoded.
	 * @return The never {@code null} list of segments.
	 */
	public static PathSegments segment(String segment, boolean encoded) {
		if (segment == null || segment.length() == 0) {
			return EMPTY;
		}
		String s = segment;
		if (encoded) {
			try {
				s = URLDecoder.decode(s, "UTF-8");
			} catch (UnsupportedEncodingException e) {}
		}
		return new PathSegments(ImmutableList.of(s));
	}

	/**
	 * Turns a path string into a list of decoded segments.
	 * @param path Path to split.
	 * @param encoded If the path is encoded.
	 * @return The never {@code null} list of segments.
	 */
	public static PathSegments of(String path, boolean encoded) {
		if (path == null || path.length() == 0) {
			return EMPTY;
		}
		ImmutableList.Builder<String> builder = ImmutableList.builder();
		for (String s : path.split("/")) {
			if (s != null && s.length() > 0) {
				if (encoded) {
					try {
						s = URLDecoder.decode(s, "UTF-8");
					} catch (UnsupportedEncodingException e) {}
				}
				builder.add(s);
			}
		}
		return new PathSegments(builder.build());
	}

	/**
	 * Turns a collection of string into a list of decoded segments.
	 * @param encoded If the segments are encoded.
	 * @param segments String segments.
	 * @return The never {@code null} list of segments.
	 */
	public static PathSegments of(boolean encoded, Iterable<String> segments) {
		if (segments == null) {
			return EMPTY;
		}
		if (!encoded) {
			return new PathSegments(ImmutableList.copyOf(segments));
		}
		ImmutableList.Builder<String> builder = ImmutableList.builder();
		for (String s : segments) {
			if (s != null && s.length() > 0) {
				if (encoded) {
					try {
						s = URLDecoder.decode(s, "UTF-8");
					} catch (UnsupportedEncodingException e) {}
				}
				builder.add(s);
			}
		}
		return new PathSegments(builder.build());
	}

	/**
	 * Turns an array of strings into a list of decoded segments.
	 * @param encoded If the segments are encoded.
	 * @param segments String segments.
	 * @return The never {@code null} list of segments.
	 */
	public static PathSegments of(boolean encoded, String... segments) {
		if (segments == null) {
			return EMPTY;
		}
		return of(encoded, Arrays.asList(segments));
	}

	/**
	 * Extracts the extension from a segment.
	 * @param segment Segment.
	 * @return The extension of {@code null} if no extension is found.
	 */
	public static String getExtension(String segment) {
		if (segment == null) {
			return null;
		}
		int li = segment.lastIndexOf('.');
		if (li >= 0 && li < (segment.length() - 1)) {
			return segment.substring(li + 1);
		}
		return null;
	}

	/**
	 * Removes the extension from a segment.
	 * @param segment Segment.
	 * @return The segment without the extension (and without the dot) or the same segment if no extension is found.
	 */
	public static String removeExtension(String segment) {
		if (segment == null) {
			return null;
		}
		int li = segment.lastIndexOf('.');
		if (li >= 0 && li < (segment.length() - 1)) {
			return segment.substring(0, li);
		}
		return segment;
	}

	private final ImmutableList<String> segments;

	/** Constructor. */
	private PathSegments(ImmutableList<String> segments) {
		this.segments = segments;
	}

	@Override
	protected List<String> delegate() {
		return segments;
	}

	public PathSegments consume(int n) {
		if (n == 0) {
			return this;
		}
		return new PathSegments(segments.subList(n, segments.size()));
	}

	public PathSegments consumeLast(int n) {
		if (n == 0) {
			return this;
		}
		return new PathSegments(segments.subList(0, segments.size() - n));
	}

	public String head() {
		return get(0);
	}

	public String last() {
		return get(size() - 1);
	}

	public PathSegments consume() {
		return consume(1);
	}

	public PathSegments consumeLast() {
		return consumeLast(1);
	}

	/**
	 * Removes the extension from the last segment.
	 * @return The modified segments.
	 */
	public PathSegments removeExtension() {
		if (isEmpty()) {
			return this;
		}
		final String last = last();
		final String removed = removeExtension(last);
		if (last.equals(removed)) {
			return this;
		}
		return new PathSegments(ImmutableList.copyOf(Iterables.concat(segments.subList(0, segments.size() - 1),
			ImmutableList.of(removed))));
	}

	public String join() {
		return Joiner.on("/").skipNulls().join(this);
	}

	public PathSegments add(PathSegments other) {
		if (other == null || other.isEmpty()) {
			return this;
		}
		if (isEmpty()) {
			return other;
		}
		return new PathSegments(ImmutableList.copyOf(Iterables.concat(segments, other.segments)));
	}

	@Override
	public int hashCode() {
		return segments.hashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof PathSegments) {
			return Objects.equal(segments, ((PathSegments) object).segments);
		}
		return false;
	}

	@Override
	public String toString() {
		return String.format("[%d, %s]", size(), join());
	}
}
