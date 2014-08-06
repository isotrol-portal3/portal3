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

package com.isotrol.impe3.tickets.domain;


import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.TimeUnit;


/**
 * Ticket constraints.
 * @author Andres Rodriguez
 */
public final class TicketConstraints {
	/** Expiration timestamp. */
	private final long timestamp;
	/** Number of uses. */
	private final int uses;

	public static TicketConstraints withTimestamp(long timestamp, int uses) {
		return new TicketConstraints(timestamp, uses);
	}

	public static TicketConstraints withDuration(long duration, TimeUnit unit, int uses) {
		checkNotNull(unit);
		checkArgument(duration > 0);
		final long expiration = System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(duration, unit);
		return new TicketConstraints(expiration, uses);
	}

	private TicketConstraints(long timestamp, int uses) {
		checkArgument(timestamp > 0);
		checkArgument(uses > 0);
		this.timestamp = timestamp;
		this.uses = uses;
	}

	public int getUses() {
		return uses;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public boolean isExpired() {
		return System.currentTimeMillis() > timestamp;
	}
}
