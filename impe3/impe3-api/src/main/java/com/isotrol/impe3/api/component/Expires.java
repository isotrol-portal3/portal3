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

package com.isotrol.impe3.api.component;


import java.util.Calendar;
import java.util.Date;


/**
 * Value representing the expritation timestamp.
 * @author Andres Rodriguez
 */
public final class Expires {
	/** Timestamp. */
	private final long timestamp;

	/**
	 * Creates the object from a timestamp.
	 * @param timestamp Timestamp.
	 * @return The requested object.
	 */
	public static Expires of(long timestamp) {
		return new Expires(timestamp);
	}

	/**
	 * Creates the object from a date.
	 * @param date Date.
	 * @return The requested object or {@code null} if the argument is {@code null}.
	 */
	public static Expires of(Date date) {
		return date == null ? null : new Expires(date.getTime());
	}

	/**
	 * Creates the object from a calendar.
	 * @param calendar Calendar.
	 * @return The requested object or {@code null} if the argument is {@code null}.
	 */
	public static Expires of(Calendar calendar) {
		return calendar == null ? null : new Expires(calendar.getTimeInMillis());
	}

	/**
	 * Constructor.
	 * @param timestamp Timestamp.
	 */
	private Expires(long timestamp) {
		this.timestamp = timestamp;
	}

	public long get() {
		return timestamp;
	}

	public Date toDate() {
		return new Date(timestamp);
	}

	public Calendar toCalendar() {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp);
		return calendar;
	}

	@Override
	public int hashCode() {
		return Long.valueOf(timestamp).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Expires) {
			final Expires e = (Expires) obj;
			return timestamp == e.timestamp;
		}
		return false;
	}
}
