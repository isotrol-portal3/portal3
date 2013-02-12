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

package com.isotrol.impe3.palette.freemarker;


import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;

import com.isotrol.impe3.test.ComponentTester;
import com.isotrol.impe3.test.ModuleTester;
import com.isotrol.impe3.test.TestContext;
import com.isotrol.impe3.test.TestIABuilder;


/**
 * FreeMarker Utils Module Tests.
 * @author Andres Rodriguez
 */
public class FreeMarkerUtilsModuleTest {
	private static TestContext context;

	@BeforeClass
	public static void setUp() {
		context = new TestIABuilder().get().get();
	}

	private ModuleTester<FreeMarkerUtilsModule> start() {
		ModuleTester<FreeMarkerUtilsModule> module = context.getModule(FreeMarkerUtilsModule.class);
		module.start();
		return module;
	}

	@Test
	public void create() {
		final ComponentTester<CreateModelComponent> component = start().getComponent(CreateModelComponent.class,
			"createModel");
		component.executeOk();
		assertNotNull(component.getComponent().getTemplateModel());
	}

	@Test
	public void newModel() {
		final ComponentTester<NewModelComponent> component = start().getComponent(NewModelComponent.class, "newModel");
		component.executeOk();
		assertNotNull(component.getComponent().getTemplateModel());
	}

}
