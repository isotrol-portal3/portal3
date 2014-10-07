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

package com.isotrol.impe3.connectors.nrproxy;


import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.isotrol.impe3.api.EngineMode;
import com.isotrol.impe3.core.modules.ModuleDefinition;
import com.isotrol.impe3.nr.api.NodeRepository;
import com.isotrol.impe3.support.nr.ForwardingNodeRepository;
import com.isotrol.impe3.test.ModuleTester;
import com.isotrol.impe3.test.TestEnvironment;
import com.isotrol.impe3.test.TestEnvironmentBuilder;


/**
 * Tests for NodeRepositoryProxy.
 * @author Emilio Escobar Reyero
 * @author Andres Rodriguez Chamorro
 */
public class NodeRepositoryProxyTest {
	private static TestEnvironment environment;

	@BeforeClass
	public static void setUp() {
		environment = new TestEnvironmentBuilder().get();
	}

	/** Module definition. */
	@Test
	public void module() {
		ModuleDefinition.of(NodeRepositoryProxyModule.class);
	}

	/** Module loading. */
	@Test
	public void load() {
		final ModuleTester<NodeRepositoryProxyModule> mt = environment.getModule(NodeRepositoryProxyModule.class);
		final NodeRepositoryProxyModule module = mt.start("online", new TestNR(EngineMode.ONLINE), "offline",
			new TestNR(EngineMode.OFFLINE));
		final NodeRepository proxy = module.service();
		Assert.assertNotNull(proxy);
	}

	private static class TestNR extends ForwardingNodeRepository {
		@SuppressWarnings("unused")
		private final EngineMode mode;

		TestNR(EngineMode mode) {
			this.mode = mode;
		}

		@Override
		protected NodeRepository delegate() {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
