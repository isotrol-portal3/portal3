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

package com.isotrol.impe3.lucene;


import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.Filter;


/**
 * Empty Lucene filter.
 * @author Andres Rodriguez Chamorro
 */
public class EmptyFilter extends Filter {
	/** Serial UID. */
	private static final long serialVersionUID = -8619536047010476466L;
	/** Iterator. */
	private static final EmptyIterator ITERATOR = new EmptyIterator();
	/** Set. */
	private static final EmptySet SET = new EmptySet();
	/** Filter. */
	private static final EmptyFilter FILTER = new EmptyFilter();

	public static EmptyFilter create() {
		return FILTER;
	}

	/**
	 * Default constructor.
	 */
	private EmptyFilter() {
	}

	@Override
	public DocIdSet getDocIdSet(IndexReader reader) throws IOException {
		return SET;
	}

	private static class EmptyIterator extends DocIdSetIterator {
		EmptyIterator() {
		}

		@Override
		public int docID() {
			return NO_MORE_DOCS;
		}

		@Override
		public int nextDoc() throws IOException {
			return NO_MORE_DOCS;
		}

		@Override
		public int advance(int target) throws IOException {
			return NO_MORE_DOCS;
		}
	}

	private static class EmptySet extends DocIdSet {
		EmptySet() {
		}

		@Override
		public DocIdSetIterator iterator() {
			return ITERATOR;
		}
	}
}
