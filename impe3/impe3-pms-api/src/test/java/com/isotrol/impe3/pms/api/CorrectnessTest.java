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

package com.isotrol.impe3.pms.api;


import static com.isotrol.impe3.pms.api.Correctness.ERROR;
import static com.isotrol.impe3.pms.api.Correctness.OK;
import static com.isotrol.impe3.pms.api.Correctness.WARN;
import static com.isotrol.impe3.pms.api.Correctness.fold;
import static org.junit.Assert.assertEquals;

import org.junit.Test;


/**
 * Tests of the Correctness enumeration.
 * @author Andres Rodriguez
 */
public class CorrectnessTest {
	@Test
	public void merges() {
		assertEquals(OK, OK.merge(null));
		assertEquals(OK, OK.merge(OK));
		assertEquals(WARN, OK.merge(WARN));
		assertEquals(WARN, WARN.merge(null));
		assertEquals(WARN, WARN.merge(WARN));
		assertEquals(ERROR, OK.merge(ERROR));
		assertEquals(ERROR, WARN.merge(ERROR));
		assertEquals(ERROR, ERROR.merge(null));
		assertEquals(ERROR, ERROR.merge(ERROR));
		assertEquals(OK, fold((Correctness[]) null));
		assertEquals(OK, fold((Iterable<Correctness>) null));
		assertEquals(OK, fold(OK));
		assertEquals(OK, fold(OK, OK));
		assertEquals(WARN, fold(OK, WARN));
		assertEquals(WARN, fold(WARN, OK));
		assertEquals(ERROR, fold(WARN, OK, ERROR));
		assertEquals(ERROR, fold(OK, WARN, ERROR));
		assertEquals(ERROR, fold(WARN, ERROR, OK));
		assertEquals(ERROR, fold(ERROR, WARN, OK));
	}

}
