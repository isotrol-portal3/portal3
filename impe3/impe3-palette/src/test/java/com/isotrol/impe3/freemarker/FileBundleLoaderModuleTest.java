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

package com.isotrol.impe3.freemarker;


import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.test.TestEnvironment;
import com.isotrol.impe3.test.TestEnvironmentBuilder;
import com.isotrol.impe3.test.TestSupport;


/**
 * Tests for FileBundleLoaderModule
 * @author Andres Rodriguez
 */
public class FileBundleLoaderModuleTest {
	private static TestEnvironment environment;
	private static FileId template;
	private FileBundleLoaderModule module = null;

	@BeforeClass
	public static void environment() {
		TestEnvironmentBuilder b = new TestEnvironmentBuilder();
		template = b.bundle(FileBundleLoaderModuleTest.class, "test_module.zip");
		environment = b.get();
	}

	/**
	 * Set up
	 */
	@Before
	public void setUp() throws Exception {
		final FileBundleLoaderConfiguration config = TestSupport.config(FileBundleLoaderConfiguration.class,
			"templateBundle", template);
		module = environment.getModule(FileBundleLoaderModule.class).start("config", config);
	}

	/**
	 * Load
	 */
	@Test
	public void load() throws Exception {
		Assert.assertNotNull(module.templateLoader().findTemplateSource("test.ftl"));
	}
}
