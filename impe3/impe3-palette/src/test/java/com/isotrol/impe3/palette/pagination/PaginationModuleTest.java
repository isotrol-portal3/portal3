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

package com.isotrol.impe3.palette.pagination;


import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.isotrol.impe3.api.Pagination;
import com.isotrol.impe3.test.ComponentTester;
import com.isotrol.impe3.test.ModuleTester;
import com.isotrol.impe3.test.TestEnvironment;
import com.isotrol.impe3.test.TestEnvironmentBuilder;
import com.isotrol.impe3.test.TestSupport;


/**
 * Pagination Module Test.
 * @author Andres Rodriguez
 */
public class PaginationModuleTest {
	private static TestEnvironment environment;
	private ModuleTester<PaginationModule> module = null;
	private ComponentTester<FetchComponent> fetch = null;
	private ComponentTester<FetchWithSizeComponent> fetchWithSize = null;
	private ComponentTester<ReleaseComponent> release = null;

	@BeforeClass
	public static void environment() {
		TestEnvironmentBuilder b = new TestEnvironmentBuilder();
		environment = b.get();
	}

	/**
	 * Set up
	 */
	@Before
	public void setUp() throws Exception {
		module = environment.getModule(PaginationModule.class);
		module.start();
		fetch = module.getComponent(FetchComponent.class, "fetch");
		fetchWithSize = module.getComponent(FetchWithSizeComponent.class, "fetchWithSize");
		release = module.getComponent(ReleaseComponent.class, "release");
	}

	@Test
	public void edit() {
		fetch.executeOk();
		release.getComponent().setPagination(fetch.getComponent().getPagination());
		release.executeOk();
		// ExtractedParameter<Integer> ep = release.getComponent().getPage();
		// Assert.assertEquals(1, ep.getValue().intValue());
	}

	@Test
	public void size() {
		PageSizeComponentConfig c = TestSupport.config(PageSizeComponentConfig.class, "pageSize", 17);
		fetchWithSize.getComponent().setConfig(c);
		fetchWithSize.executeOk();
		Pagination p = fetchWithSize.getComponent().getPagination();
		assertEquals(17, p.getSize().intValue());
	}

}
