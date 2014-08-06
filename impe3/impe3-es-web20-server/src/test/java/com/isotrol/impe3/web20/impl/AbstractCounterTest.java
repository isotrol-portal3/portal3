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
package com.isotrol.impe3.web20.impl;


import com.google.common.collect.Sets;
import com.isotrol.impe3.web20.api.CounterEventDTO;
import com.isotrol.impe3.web20.api.CounterFilterDTO;


/**
 * Abstract class for counter tests.
 * @author Andres Rodriguez
 */
public abstract class AbstractCounterTest extends AbstractGroupTest {
	protected static final String TYPE = "VISIT";

	protected static CounterEventDTO event(String resource) {
		final CounterEventDTO dto = new CounterEventDTO();
		dto.setCounterType(TYPE);
		dto.setResource(resource);
		return dto;
	}

	protected static CounterEventDTO event(String resource, String... aggregations) {
		final CounterEventDTO dto = event(resource);
		dto.setAggregations(Sets.newHashSet(aggregations));
		return dto;
	}

	protected static CounterFilterDTO filter() {
		CounterFilterDTO filter = new CounterFilterDTO();
		filter.setCounterType(TYPE);
		return filter;
	}

	protected static CounterFilterDTO filter(String aggregation) {
		CounterFilterDTO filter = filter();
		filter.setAggregation(aggregation);
		return filter;
	}

}
