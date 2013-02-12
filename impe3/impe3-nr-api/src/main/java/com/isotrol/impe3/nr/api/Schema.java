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


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Schem description.
 * 
 * @author Emilio Escobar Reyero
 */
public final class Schema {
	private Schema() {
		throw new AssertionError();
	}

	/** Reserved schema name. */
	public static final String SCHEMA_NAME = "impe3nr-";
	/** Blob field prefix. */
	public static final String BLOB_PREFIX = "impe3blob-";
	/** Null UUID value. */
	public static final String NULL_UUID = "__NULL__";
	/** Default set. */
	public static final String DEFAULT_SET = "DEFAULT";
	/** All locales. */
	public static final String ALL_LOCALES = "ALL";

	private static final String prefix(String name) {
		return SCHEMA_NAME + name;
	}

	/** Node Set */
	public static final String NODESET = prefix("SET");
	/** Node Key */
	public static final String NODEKEY = prefix("NODEKEY");
	/** Node Type */
	public static final String TYPE = prefix("TYPE");
	/** Node ID */
	public static final String ID = prefix("ID");
	/** Node locale */
	public static final String LOCALE = prefix("LOCALE");
	/** Other locales for the same node. */
	public static final String OTHER_LOCALE = prefix("OTHER_LOCALE");
	/** Node title */
	public static final String TITLE = prefix("TITLE");
	/** Node title (for sorting) */
	public static final String TITLE_SORT = prefix("TITLE_SORT");
	/** Node description */
	public static final String DESCRIPTION = prefix("DESC");
	/** Node date */
	public static final String DATE = prefix("DATE");
	/** Node release date */
	public static final String RELEASEDATE = prefix("RELEASEDATE");
	/** Node expiration date */
	public static final String EXPIRATIONDATE = prefix("EXPIRATIONDATE");
	/** Multi field. NodeKey for categories */
	public static final String MAIN_CATEGORY = prefix("MAIN_CATEGORY");
	/** Multi field. Categories UUIDs. */
	public static final String CATEGORY = prefix("CATEGORY");
	/** Multifield: Tag. */
	public static final String TAG = prefix("TAG");
	/** Multi field. NodeKey for related content */
	public static final String RELATED = prefix("RELATED");
	/** Index field for content. */
	public static final String CONTENT_IDX = prefix("INDEX");
	/** Store field for content. */
	public static final String CONTENT_STORE = prefix("STORE");
	/** Is the content compressed? */
	public static final String COMPRESSED = prefix("COMPRESSED");
	/** Is the content compressed? */
	public static final String COMPRESSED_VALUE = "1";
	/** Mime type. */
	public static final String MIME = prefix("MIME");
	/** Attachments index. */
	public static final String ATTACHMENT = prefix("ATTACH");

	/** Date fields format. */
	public static final String DATEFORMAT = "yyyyMMddHHmmss";

	/** Date formatters. */
	private static final ThreadLocal<SimpleDateFormat> FORMATTERS = new ThreadLocal<SimpleDateFormat>();

	/** Returns the formatter for the current thread. */
	private static SimpleDateFormat formatter() {
		SimpleDateFormat sdf = FORMATTERS.get();
		if (sdf == null) {
			sdf = new SimpleDateFormat(DATEFORMAT);
			FORMATTERS.set(sdf);
		}
		return sdf;
	}

	/**
	 * Parses a date field into a date.
	 * @param text Text to parse.
	 * @return The date or {@code null} if the argument is {@code null}.
	 * @throws IllegalArgumentException if the argument has incorrect format.
	 */
	public static Date toDate(String text) {
		if (text == null) {
			return null;
		}
		try {
			return formatter().parse(text);
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * Parses a date field into a date.
	 * @param text Text to parse.
	 * @return The date or {@code null} if the argument is {@code null} or has incorrect format.
	 */
	public static Date safeToDate(String text) {
		try {
			return toDate(text);
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	private static Calendar toCalendar(final Date date) {
		if (date == null) {
			return null;
		}
		final Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c;
	}

	/**
	 * Parses a date field into a calendar.
	 * @param text Text to parse.
	 * @return The calendar or {@code null} if the argument is {@code null}.
	 * @throws IllegalArgumentException if the argument has incorrect format.
	 */
	public static Calendar toCalendar(String text) {
		return toCalendar(toDate(text));
	}

	/**
	 * Parses a date field into a calendar.
	 * @param text Text to parse.
	 * @return The calendar or {@code null} if the argument is {@code null} or has incorrect format.
	 */
	public static Calendar safeToCalendar(String text) {
		return toCalendar(safeToDate(text));
	}

	/**
	 * Turns a date into a field with the correct format.
	 * @param date Date.
	 * @return The text or {@code null} if the argument is {@code null}.
	 */
	public static String dateToString(final Date date) {
		if (date == null) {
			return null;
		}
		final Date d;
		final long time = date.getTime();
		if (time < MIN_DATE_TS) {
			d = new Date(MIN_DATE_TS);
		} else if (time > MAX_DATE_TS) {
			d = new Date(MAX_DATE_TS);
		} else {
			d = date;
		}
		return formatter().format(d);
	}

	/**
	 * Turns a calendar into a field with the correct format.
	 * @param calendar Calendar.
	 * @return The text or {@code null} if the argument is {@code null}.
	 */
	public static String calendarToString(final Calendar calendar) {
		if (calendar == null) {
			return null;
		}
		return dateToString(calendar.getTime());
	}

	/** Min timestamp. */
	private static final long MIN_DATE_TS = 0L;
	/** Max timestamp. */
	private static final long MAX_DATE_TS;

	static {
		final Date min = new Date(MIN_DATE_TS);
		final Calendar c = Calendar.getInstance();
		c.setTime(min);
		c.set(Calendar.YEAR, 9999);
		final Date max = c.getTime();
		MAX_DATE_TS = max.getTime();
	}

	/**
	 * Returns the minimum date.
	 * @return The minimum date.
	 */
	public static Date getMinDate() {
		return new Date(MIN_DATE_TS);
	}

	/**
	 * Returns the maximum date.
	 * @return The maximum date.
	 */
	public static Date getMaxDate() {
		return new Date(MAX_DATE_TS);
	}

	/**
	 * Returns the minimum date as a calendar.
	 * @return The minimum date as a calendar.
	 */
	public static Calendar getMinCalendar() {
		final Calendar c = Calendar.getInstance();
		c.setTime(getMinDate());
		return c;
	}

	/**
	 * Returns the maximum date as a calendar.
	 * @return The maximum date as a calendar.
	 */
	public static Calendar getMaxCalendar() {
		final Calendar c = Calendar.getInstance();
		c.setTime(getMaxDate());
		return c;
	}

	/**
	 * Returns the minimum date.
	 * @return The minimum date.
	 */
	public static String getMinDateString() {
		return formatter().format(getMinDate());
	}

	/**
	 * Returns the maximum date.
	 * @return The maximum date.
	 */
	public static String getMaxDateString() {
		return formatter().format(getMaxDate());
	}

}
