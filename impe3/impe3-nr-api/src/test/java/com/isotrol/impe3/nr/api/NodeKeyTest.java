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

import java.util.UUID;

import net.derquinse.common.test.EqualityTests;
import net.derquinse.common.test.HessianSerializabilityTests;
import net.derquinse.common.test.SerializabilityTests;

import org.junit.Test;


/**
 * Tests for NodeKey.
 * @author Emilio Escobar
 * @author Andres Rodriguez
 */
public class NodeKeyTest {
	private static final UUID TYPE = UUID.randomUUID();
	private static final String ID = "000000001";
	private static final NodeKey KEY = NodeKey.of(TYPE, ID);

	@Test
	public void createFromStringTest() {
		final String key = TYPE.toString() + ":" + ID;
		NodeKey nodeKey = NodeKey.of(key);
		assertEquals(TYPE, nodeKey.getNodeType());
		assertEquals(ID, nodeKey.getNodeId());
		EqualityTests.two(KEY, nodeKey);
	}

	@Test
	public void createFromUUID() {
		assertEquals(ID, KEY.getNodeId());
		assertEquals(TYPE, KEY.getNodeType());
	}

	@Test
	public void equalsTest() {
		NodeKey nodeKey = NodeKey.of(KEY.toString());
		EqualityTests.two(KEY, nodeKey);
	}

	@Test
	public void caseTest() {
		NodeKey nodeKey = NodeKey.of(TYPE.toString().toUpperCase() + ":" + ID);
		EqualityTests.two(KEY, nodeKey);
	}

	@Test
	public void serializability() {
		SerializabilityTests.check(KEY);
		HessianSerializabilityTests.both(KEY);
	}

}
