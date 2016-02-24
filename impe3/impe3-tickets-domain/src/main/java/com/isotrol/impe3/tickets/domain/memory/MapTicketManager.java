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

package com.isotrol.impe3.tickets.domain.memory;


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;
import java.util.UUID;

import net.sf.derquinsej.uuid.TimeBasedUUIDGenerator;
import net.sf.derquinsej.uuid.UUIDGenerator;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.MapMaker;
import com.isotrol.impe3.tickets.domain.Ticket;
import com.isotrol.impe3.tickets.domain.TicketConstraints;
import com.isotrol.impe3.tickets.domain.TicketManager;


/**
 * Map-backed the ticket manager.
 * @author Andres Rodriguez
 */
public final class MapTicketManager implements TicketManager {
	/** UUID generator. */
	private final UUIDGenerator uuidGen = new TimeBasedUUIDGenerator();
	/** Subject map. */
	private final LoadingCache<String, Subject> subjects = CacheBuilder.newBuilder().build(new CacheLoader<String, Subject>() {
		@Override
		public Subject load(String input) {
			return new Subject();
		};
	});

	public MapTicketManager() {
	}

	public UUID create(String subject, TicketConstraints constraints, Map<String, String> properties) {
		checkNotNull(subject);
		checkNotNull(constraints);
		return subjects.getUnchecked(subject).create(constraints, properties);
	}

	public Ticket consume(String subject, UUID id) {
		checkNotNull(subject);
		checkArgument(subjects.asMap().containsKey(subject));
		return subjects.getUnchecked(subject).consume(id);
	}

	private final class Subject {
		private final Map<UUID, TicketValue> tickets = new MapMaker().makeMap();

		Subject() {
		}

		UUID create(TicketConstraints constraints, Map<String, String> properties) {
			final UUID id = uuidGen.get();
			tickets.put(id, new TicketValue(Ticket.valid(id, properties), constraints));
			return id;
		}

		public Ticket consume(UUID id) {
			if (id == null) {
				return null;
			}
			final TicketValue v = tickets.get(id);
			if (v == null) {
				return null;
			}
			return v.consume();
		}

	}

	public boolean hasSubject(String subject) {
		checkNotNull(subject);
		final Subject subjectId = subjects.getUnchecked(subject);
		return (subjectId != null);
	}
}
