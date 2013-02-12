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

package com.isotrol.impe3.samples.randomnr;


import net.sf.derquinse.lucis.Page;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.isotrol.impe3.nr.api.Node;
import com.isotrol.impe3.nr.api.NodeQueries;
import com.isotrol.impe3.nr.api.NodeRepository;
import com.isotrol.impe3.test.TestEnvironment;
import com.isotrol.impe3.test.TestEnvironmentBuilder;
import com.isotrol.impe3.test.TestSupport;


/**
 * Tests for RandomModule
 * @author Andres Rodriguez
 */
public class RandomModuleTest {
	private static TestEnvironment environment;

	@BeforeClass
	public static void environment() {
		TestEnvironmentBuilder b = new TestEnvironmentBuilder();
		b.contentType("ct1");
		b.category("cat1", null);
		b.category("cat2", null);
		environment = b.get();
	}

	/**
	 * Repository
	 */
	@Test
	public void testMessageService() {
		final RandomConfig config = TestSupport.config(RandomConfig.class, "number", 50);
		final RandomModule module = environment.getModule(RandomModule.class).start("config", config);
		final NodeRepository service = module.repository();
		final Page<Node> p = service.getPage(NodeQueries.matchAll(), null, null, false, 0, 1000, null);
		Assert.assertTrue(p.getTotalHits() == 50);
	}
}
