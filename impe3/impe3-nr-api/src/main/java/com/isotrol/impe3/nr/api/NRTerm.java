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


import java.io.Serializable;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;


/**
 * A search term. Represents the text to search and where
 * @author Emilio Escobar
 */
public final class NRTerm implements Serializable {
	

	private static final long serialVersionUID = -8848497396951881837L;

	/** Field name. */
	private final String field;
	/** Term text. */
	private final String text;

	/**
	 * Creates a new term.
	 * @param field Field name.
	 * @param text Term text.
	 * @return The created term.
	 */
	public static NRTerm of(String field, String text) {
		Preconditions.checkNotNull(field, "A field name must be provided");
		return new NRTerm(field, text);
	}

	private NRTerm(String field, String text) {
		this.field = field;
		this.text = text;
	}

	public String getField() {
		return field;
	}

	public String getText() {
		return text;
	}

	/**
	 * Returns a new term with the same field and the provided text.
	 * @param txt Term text.
	 * @return The requested term.
	 */
	public NRTerm text(String txt) {
		return new NRTerm(this.field, txt);
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(field, text);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof NRTerm) {
			final NRTerm t = (NRTerm)obj;
			return Objects.equal(field, t.field) && Objects.equal(text, t.text);
		}

		return false;
	}
}
