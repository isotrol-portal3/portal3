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


import static com.isotrol.impe3.nr.api.NodeFilter.builder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import net.derquinse.common.test.HessianSerializabilityTests;
import net.derquinse.common.test.SerializabilityTests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Optional;


public class NodeFilterTest {

	private final static int numContentTypes = 3;
	private final static int numCategories = 3;
	NodeFilter filter;
	Set<Optional<UUID>> categories;
	Set<UUID> contentTypes;

	@Before
	public void before() {
		categories = new HashSet<Optional<UUID>>();
		contentTypes = new HashSet<UUID>();

		for (int i = 0; i < numContentTypes; i++) {
			contentTypes.add(UUID.randomUUID());
		}
		for (int i = 0; i < numCategories; i++) {
			categories.add(Optional.of(UUID.randomUUID()));
		}
		filter = builder().categories().apply(FilterType.OPTIONAL, categories).contentTypes()
			.apply(FilterType.OPTIONAL, contentTypes).build();
		Assert.assertNotNull(filter);
	}

	@Test
	public void notNullTest() {
		Assert.assertFalse(filter.isNull());
	}

	@Test
	public void isEmptyTest() {
		// NodeFilter f = builder().contentTypes().apply(contentTypes, FilterType.OPTIONAL).build();
		NodeFilter f = builder().contentTypes().apply(FilterType.REQUIRED, contentTypes).contentTypes()
			.apply(FilterType.FORBIDDEN, contentTypes).build();
		Assert.assertFalse(filter.isEmpty());
		Assert.assertTrue(f.isEmpty());
	}

	@Test
	public void serializability() {
		SerializabilityTests.check(filter);
		HessianSerializabilityTests.both(filter);
	}

}
