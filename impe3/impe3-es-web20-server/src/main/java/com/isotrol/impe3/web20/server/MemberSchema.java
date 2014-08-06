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
package com.isotrol.impe3.web20.server;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Members schema. 
 * @author Emilio Escobar Reyero
 */
public final class MemberSchema {

	private MemberSchema() {
		throw new AssertionError();
	}
	
	/** Reserved schema name. */
	public static final String SCHEMA_NAME = "w20-mmbr-";

	/** Reserved schema name for properties. */
	public static final String SCHEMA_PROPS = "prps-";

	
	private static final String prefix(String name) {
		return SCHEMA_NAME + name;
	}

	public static final String property(String name) {
		return SCHEMA_NAME + SCHEMA_PROPS + name;
	}

	
	/** Member id */
	public static final String ID = prefix("ID");
	/** Member code */
	public static final String CODE = prefix("CODE");
	/** Member name */
	public static final String NAME = prefix("NAME");
	/** Member display name */
	public static final String DISPLAYNAME = prefix("DISPLAYNAME");
	/** Member display name */
	public static final String DISPLAYNAMEORDER = prefix("DISPLAYNAMEORDER");
	/** Member email */
	public static final String EMAIL = prefix("EMAIL");
	/** Member date */
	public static final String DATE = prefix("DATE");
	/** Member blocked */
	public static final String BLOCKED = prefix("BLOCKED");
	/** Member center */
	public static final String CENTER = prefix("CENTER");
	
	/** Member profiles */
	public static final String PROFILE = prefix("PROFILE");
	
	/** Member membership */
	public static final String COMMUNITY = prefix("COMMUNITY");

	/** Member membership role */
	public static final String COMMUNITYROL = prefix("COMMUNITYROLE");

	/** Member membership role */
	public static final String COMMUNITYSTATUS = prefix("COMMUNITYSTATUS");
	
	
	/** Member Property center */
	public static final String PROPERTY_CENTER = "centro";

	
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
