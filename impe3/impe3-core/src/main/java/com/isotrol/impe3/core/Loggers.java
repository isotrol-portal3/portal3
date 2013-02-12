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

package com.isotrol.impe3.core;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Port@l core logging channels.
 * @author Andres Rodriguez
 */
public final class Loggers {
	private static final String CORE_CHANNEL = "impe3core";
	private static final String PMS_CHANNEL = "impe3pms";
	private static final String PAGE_CHANNEL = "impe3page";
	private static final String URI_CHANNEL = "com.isotrol.impe3.uri";

	public static Logger core() {
		return LoggerFactory.getLogger(CORE_CHANNEL);
	}

	public static Logger pms() {
		return LoggerFactory.getLogger(PMS_CHANNEL);
	}

	public static Logger page() {
		return LoggerFactory.getLogger(PAGE_CHANNEL);
	}

	public static Logger uri() {
		return LoggerFactory.getLogger(URI_CHANNEL);
	}
	
	private Loggers() {
		throw new AssertionError();
	}
}
