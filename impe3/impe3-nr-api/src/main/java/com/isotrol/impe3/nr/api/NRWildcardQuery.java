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

package com.isotrol.impe3.nr.api;

import com.google.common.base.Objects;


/**
 * Wildcard query for two supported wildcards, '*' and '?'.
 * @author Emilio Escobar Reyero
 * 
 */
public final class NRWildcardQuery extends NodeQuery {

	private static final long serialVersionUID = 4479938938166277077L;

	private final NRTerm term;

	protected NRWildcardQuery(final NRTerm term) {
		this.term = term;
	}

	public NRTerm getTerm() {
		return term;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getBoost(), term);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NRWildcardQuery) {
			final NRWildcardQuery q = (NRWildcardQuery)obj;
			return getBoost() == q.getBoost() && Objects.equal(term, q.term);
		}
		return false;
	}
	
}
