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


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.base.Function;


/**
 * Test for Computing Map.
 * @author Andres Rodriguez
 */
public class PathSegmentsTransformersTest {
	private static final String RSS = "rss";
	private static final PathSegments RSS_S = PathSegments.of(false, RSS);
	private static final PathSegments P1 = PathSegments.of("/a/b/c", false);
	private static final PathSegments P2 = PathSegments.of("rss/a/b/c", false);
	private static final PathSegments P3 = PathSegments.of("/a/b/c/rss", false);
	private static final PathSegments P4 = PathSegments.of("/a/b/c.rss", false);

	private static PathSegments apply(Function<PathSegments, PathSegments> f) {
		return f.apply(P1);
	}

	/** Insert. */
	@Test
	public void insert() {
		assertEquals(P2, apply(PathSegmentsTransformers.insert(RSS_S)));
	}

	/** Append. */
	@Test
	public void append() {
		assertEquals(P3, apply(PathSegmentsTransformers.append(RSS_S)));
	}

	/** Extension. */
	@Test
	public void extension() {
		assertEquals(P4, apply(PathSegmentsTransformers.extension(RSS)));
	}

}
