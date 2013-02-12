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

package com.isotrol.impe3.palette.content.load;


import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.Pagination;
import com.isotrol.impe3.nr.api.NodeRepository;
import com.isotrol.impe3.test.ComponentTester;
import com.isotrol.impe3.test.ModuleTester;
import com.isotrol.impe3.test.TestEnvironment;
import com.isotrol.impe3.test.TestEnvironmentBuilder;
import com.isotrol.impe3.test.TestSupport;


/**
 * Listing component test.
 * @author Andres Rodriguez
 */
public class ListingComponentTest {
	private static TestEnvironment environment;
	private static ContentType contentType;
	private static NodeRepository nodeRepository;
	private static ModuleTester<ContentLoaderModule> module;

	@BeforeClass
	public static void env() {
		final TestEnvironmentBuilder b = new TestEnvironmentBuilder();
		contentType = b.contentType("ct");
		environment = b.get();
		nodeRepository = environment.getRepositoryBuilder().add(217, contentType).getNodeRepository();
		module = environment.getModule(ContentLoaderModule.class);
		module.start("nodeRepository", nodeRepository);
	}

	@Test
	public void listing() {
		final int size = 25;
		ComponentTester<ListingComponent> tester = module.getComponent(ListingComponent.class, "listing");
		ListingComponent component = tester.getComponent();
		component.setConfig(TestSupport.config(PageConfig.class, "pageSize", size));
		component.execute();
		Pagination p = component.getPagination();
		assertEquals(Integer.valueOf(size), p.getSize());
	}

	@Test
	public void listing2() {
		final int size = 23;
		ComponentTester<ListingComponent> tester = module.getComponent(ListingComponent.class, "listing");
		ListingComponent component = tester.getComponent();
		component.setPagination(new Pagination("page", 7, null, null));
		component.setConfig(TestSupport.config(PageConfig.class, "pageSize", size));
		component.execute();
		Pagination p = component.getPagination();
		assertEquals(Integer.valueOf(size), p.getSize());
	}

}
