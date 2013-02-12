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

package com.isotrol.impe3.hib.model;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

/**
 * Entity basic test 
 * @author Emilio Escobar Reyero
 */
public class EntityTest {

	@Test
	public void notEqualsTest() {
		final UUID uuid1 = UUID.randomUUID();
		final Entity entity1 = new Entity(uuid1) {
		};

		final UUID uuid2 = UUID.randomUUID();
		final Entity entity2 = new Entity(uuid2) {
		};

		Assert.assertNotSame(entity1, entity2);
	}
	
	@Test
	public void equalsTest() {
		final UUID uuid = UUID.randomUUID();
		final Entity entity1 = new Entity(uuid) {
		};

		final Entity entity2 = new Entity(uuid) {
		};

		Assert.assertEquals(entity1, entity2);
	}
	
	@Test
	public void hashTest() {
		final UUID uuid = UUID.randomUUID();
		final Entity entity1 = new Entity(uuid) {
		};
		
		Assert.assertTrue(uuid.hashCode() == entity1.hashCode());
	}
}
