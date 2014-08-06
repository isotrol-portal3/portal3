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

package com.isotrol.impe3.tickets.api;


import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;


/**
 * Tests for NewTicketDTO.
 * @author Andres Rodriguez
 */
public class NewTicketDTOTest {

	@Test
	public void test() {
		final NewTicketDTO t1 = new NewTicketDTO(2, 30, ImmutableMap.of("k", "v"));
		String json = new Gson().toJson(t1);
		final NewTicketDTO t2 = new Gson().fromJson(json, NewTicketDTO.class);
		Assert.assertEquals(t1, t2);
	}
}
