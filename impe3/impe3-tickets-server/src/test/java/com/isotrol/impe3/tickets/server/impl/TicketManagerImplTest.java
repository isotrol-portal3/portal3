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
package com.isotrol.impe3.tickets.server.impl;


import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.isotrol.impe3.tickets.domain.Ticket;
import com.isotrol.impe3.tickets.domain.TicketConstraints;
import com.isotrol.impe3.tickets.domain.TicketManager;
import com.isotrol.impe3.tickets.domain.TicketState;
import com.isotrol.impe3.tickets.server.MemoryContextTest;


/**
 * Tests for TicketManagerImplTest.
 * @author Andres Rodriguez
 */
public class TicketManagerImplTest extends MemoryContextTest {
	private static final Map<String, String> PAYLOAD = ImmutableMap.of("k1", "v1", "k2", "v2");
	private static final String SUBJECT = "subject";
	private static final String SUBJECT2 = "subject2";

	private TicketManager service;

	@Before
	public void setUp() {
		service = getBean(TicketManager.class);
	}

	@Test
	public void test() {
		assertNotNull(service);
	}

	private void check(Ticket t, TicketState state, UUID id) {
		assertNotNull(id);
		assertNotNull(t);
		assertEquals(id, t.getId());
		assertEquals(state, t.getState());
	}

	private void valid(Ticket t, UUID id) {
		check(t, TicketState.VALID, id);
		assertEquals(PAYLOAD, t.getProperties());
	}

	private void expired(Ticket t, UUID id) {
		check(t, TicketState.EXPIRED, id);
	}

	private void consumed(Ticket t, UUID id) {
		check(t, TicketState.CONSUMED, id);
	}

	private UUID create() {
		final UUID id = service.create(SUBJECT, TicketConstraints.withDuration(1, TimeUnit.SECONDS, 2), PAYLOAD);
		assertNotNull(id);
		return id;
	}

	@Test
	public void used() {
		final UUID id = create();
		valid(service.consume(SUBJECT, id), id);
		valid(service.consume(SUBJECT, id), id);
		consumed(service.consume(SUBJECT, id), id);
	}

	@Test
	public void expired() throws Exception {
		final UUID id = create();
		valid(service.consume(SUBJECT, id), id);
		Thread.sleep(1500);
		expired(service.consume(SUBJECT, id), id);
	}

	@Test(expected = IllegalArgumentException.class)
	public void badSubject() throws Exception {
		final UUID id = create();
		valid(service.consume(SUBJECT2, id), id);
	}

	@Test(expected = NullPointerException.class)
	public void nullSubject() throws Exception {
		final UUID id = create();
		valid(service.consume(null, id), id);
	}

	@Test
	public void nullTicket() throws Exception {
		create();
		assertNull(service.consume(SUBJECT, null));
	}

	@Test
	public void badTicket() throws Exception {
		create();
		assertNull(service.consume(SUBJECT, UUID.randomUUID()));
	}

}
