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

package com.isotrol.impe3.connectors.httpclient.din;


import org.junit.Before;
import org.junit.Test;

import com.isotrol.impe3.core.modules.ModuleDefinition;
import com.isotrol.impe3.test.ModuleTester;
import com.isotrol.impe3.test.TestEnvironment;
import com.isotrol.impe3.test.TestEnvironmentBuilder;
import com.isotrol.impe3.test.TestSupport;


/**
 * 
 * @author Emilio Escobar Reyero
 */
public class HttpClientModuleTest {
	private TestEnvironment environment;
	private ModuleTester<HttpClientModule> tester;

	@Before
	public void setUp() {
		environment = new TestEnvironmentBuilder().get();
		tester = environment.getModule(HttpClientModule.class);
	}

	/** Module definition. */
	@Test
	public void module() {
		@SuppressWarnings("unused")
		final ModuleDefinition<HttpClientModule> md = ModuleDefinition.of(HttpClientModule.class);
	}

	/** Start. */
	@Test
	public void start() {
		tester.start("config", TestSupport.config(HttpClientConfig.class, "timeout", 30));
	}
/*
	@Test
	public void getTest() {
		HttpClientModule module = tester.start("config", TestSupport.config(HttpClientConfig.class, "timeout", 30));
		HttpClientConnector connector = module.service();

		Response res = connector.get("http://www.google.es");

		Assert.assertNotNull(res);
		Assert.assertEquals(200, res.getCode());
	}

	@Test
	public void multiGetTest() {
		HttpClientModule module = tester.start("config", TestSupport.config(HttpClientConfig.class, "timeout", 30));
		HttpClientConnector connector = module.service();

		for (int i = 0; i < 100; i++) {
			Response res = connector.get("http://www.google.es");

			Assert.assertNotNull(res);
			Assert.assertEquals(200, res.getCode());
		}
		
		Response res = connector.get("http://www.google.es");

		Assert.assertNotNull(res);
		Assert.assertEquals(200, res.getCode());
	}
*/
}
