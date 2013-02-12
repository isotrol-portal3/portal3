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


/**
 * Define how to sort nodes.
 * 
 * @author Emilio Escobar Reyero
 */
public final class NRSortField implements Serializable {
	private static final long serialVersionUID = -6338954217539728266L;

	/** Sort by document score (relevancy).  Sort values are Float and higher
	   * values are at the front. */
	  public static final int SCORE = 0;
	  /** Sort by document number (index order).  Sort values are Integer and lower
	   * values are at the front. */
	  public static final int DOC = 1;
	  /** Guess type of sort based on field contents.  A regular expression is used
	   * to look at the first term indexed for the field and determine if it
	   * represents an integer number, a floating point number, or just arbitrary
	   * string characters. */
	  public static final int AUTO = 2;
	  /** Sort using term values as Strings.  Sort values are String and lower
	   * values are at the front. */
	  public static final int STRING = 3;
	  /** Sort using term values as encoded Integers.  Sort values are Integer and
	   * lower values are at the front. */
	  public static final int INT = 4;
	  /** Sort using term values as encoded Floats.  Sort values are Float and
	   * lower values are at the front. */
	  public static final int FLOAT = 5;
	  /** Sort using term values as encoded Longs.  Sort values are Long and
	   * lower values are at the front. */
	  public static final int LONG = 6;
	  /** Sort using term values as encoded Doubles.  Sort values are Double and
	   * lower values are at the front. */
	  public static final int DOUBLE = 7;
	    /**
	   * Sort using term values as encoded Shorts.  Sort values are shorts and lower values are at the front
	   */
	  public static final int SHORT = 8;
	  /**
	   * Sort using term values as encoded bytes.  Sort values are bytes and lower values are at the front
	   */
	  public static final int BYTE = 10;

	
	

	private final String field;
	private final int type;
	private final boolean reverse;

	private NRSortField(final String field) {
		this(field, false);
	}

	private NRSortField(final String field, final boolean reverse) {
		this(field, STRING, reverse);
	}

	private NRSortField(final String field, final int type, final boolean reverse) {
		this.field = field;
		this.reverse = reverse;
		this.type = safeType(type);
	}

	private int safeType(int type) {
		return (type < 0 || type > 10) ? STRING : type;
	}
	
	/**
	 * Create a sort field with normal order
	 * @param field Name of the field
	 * @return the sort field.
	 */
	public static NRSortField of(final String field) {
		return new NRSortField(field);
	}

	/**
	 * Create a sort field with specific order
	 * @param field Name of the field
	 * @param reverse if true return reverse order
	 * @return the sort field
	 */
	public static NRSortField of(final String field, final boolean reverse) {
		return new NRSortField(field, reverse);
	}

	/**
	 * Create a sort field with specific order
	 * @param field Name of the field
	 * @param type Column type of the field
	 * @param reverse if true return reverse order
	 * @return the sort field
	 */
	public static NRSortField of(final String field, final int type, final boolean reverse) {
		return new NRSortField(field, type, reverse);
	}
	
	
	public String getField() {
		return this.field;
	}

	public boolean isReverse() {
		return this.reverse;
	}
	
	public int getType() {
		return type;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NRSortField) {
			final NRSortField sf = (NRSortField)obj;
			return Objects.equal(field, sf.field) && reverse == sf.reverse && type == sf.type;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(field, type, reverse);
	}

}
