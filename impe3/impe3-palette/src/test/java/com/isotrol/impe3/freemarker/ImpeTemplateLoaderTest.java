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


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.Reader;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.test.TestEnvironment;
import com.isotrol.impe3.test.TestEnvironmentBuilder;

import freemarker.cache.TemplateLoader;


/**
 * Tests for ImpeTemplateLoader
 * @author Andres Rodriguez
 */
public class ImpeTemplateLoaderTest {
	private static TestEnvironment environment;
	private static FileId template;
	private TemplateLoader loader = new ImpeTemplateLoader(environment.getFileLoader(), template);

	@BeforeClass
	public static void environment() {
		TestEnvironmentBuilder b = new TestEnvironmentBuilder();
		template = b.bundle(ImpeTemplateLoaderTest.class, "test.zip");
		environment = b.get();
	}

	/**
	 * Set up
	 */
	@Before
	public void setUp() throws Exception {
		loader = new ImpeTemplateLoader(environment.getFileLoader(), template);
	}

	/**
	 * Catalog
	 */
	@Test
	public void catalog() throws Exception {
		assertNotNull(loader.findTemplateSource("test.ftl"));
		assertNull(loader.findTemplateSource("test_no.ftl"));
		assertNotNull(loader.findTemplateSource("test_es.ftl"));
	}

	/**
	 * Load
	 */
	@Test
	public void load() throws Exception {
		final Reader reader = loader.getReader("test.ftl", "UTF-8");
		final BufferedReader br = new BufferedReader(reader);
		final String line = br.readLine();
		br.close();
		assertTrue(line.indexOf("Hello") > -1);
	}

	/**
	 * Load
	 */
	@Test
	public void loadES() throws Exception {
		final Reader reader = loader.getReader("test_es.ftl", "UTF-8");
		final BufferedReader br = new BufferedReader(reader);
		final String line = br.readLine();
		br.close();
		assertTrue(line.indexOf("Hola") > -1);
	}

}
