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

package com.isotrol.impe3.pms.core.impl;


import org.junit.Test;

import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.core.MemoryContextTest;


/**
 * IA loading test.
 * @author Andres Rodriguez
 */
public class IALoadingTest extends MemoryContextTest {
	private static final int NUMBER = 0;

	/** Content types. */
	@Test
	public void contentTypes() throws PMSException {
		long d0 = 1L;
		long dn = 1L;
		for (int i = 0; i < NUMBER; i++) {
			long t0 = System.nanoTime();
			loadContentType();
			long t1 = System.nanoTime();
			if (i == 0) {
				d0 = t1 - t0;
			} else if (i == NUMBER - 1) {
				dn = t1 - t0;
			}
		}
		System.out.println(String.format("%d ; %d ; %f\n", d0, dn, ((double) dn) / d0));
	}
}
