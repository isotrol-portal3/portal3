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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.Test;


public class SchemaTest {

	@Test
	public void dates() {
		final SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
		Calendar c = Calendar.getInstance();
		final String s1 = Schema.calendarToString(c);
		assertEquals(f.format(c.getTime()), s1);
		c.set(Calendar.YEAR, 1955);
		final String s2 = Schema.calendarToString(c);
		assertEquals(Schema.getMinDateString(), s2);
		assertNotSame(f.format(c.getTime()), s2);
		c.set(Calendar.YEAR, 19550);
		final String s3 = Schema.calendarToString(c);
		assertEquals(Schema.getMaxDateString(), s3);
		assertNotSame(f.format(c.getTime()), s3);
	}
}
