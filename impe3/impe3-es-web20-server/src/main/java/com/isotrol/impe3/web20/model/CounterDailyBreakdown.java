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
package com.isotrol.impe3.web20.model;


import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * Counter daily breakdown aggregation entity.
 * @author Andres Rodriguez
 */
@Entity
@Table(name = "WEB20_COUNTER_DAY")
@NamedQuery(name = CounterDailyBreakdown.INC, query = "update CounterDailyBreakdown set total = total+1 where key.counter = ? and key.timestamp = ?")
public class CounterDailyBreakdown extends CounterBreakdown {
	/** Increment query. */
	public static final String INC = "counter.daily.inc";

	/**
	 * Constructor.
	 * @key Key.
	 */
	public CounterDailyBreakdown(CounterPK key) {
		super(key);
	}

	/** Default constructor. */
	public CounterDailyBreakdown() {
	}
}
