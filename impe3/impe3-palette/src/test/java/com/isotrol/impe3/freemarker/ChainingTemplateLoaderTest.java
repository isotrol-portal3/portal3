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
public class ChainingTemplateLoaderTest {
	private static TestEnvironment environment;
	private static FileId template1;
	private static FileId template2;
	private TemplateLoader loader1;
	private TemplateLoader loader2;
	private TemplateLoader loader;

	@BeforeClass
	public static void environment() {
		TestEnvironmentBuilder b = new TestEnvironmentBuilder();
		template1 = b.bundle(ChainingTemplateLoaderTest.class, "test.zip");
		template2 = b.bundle(ChainingTemplateLoaderTest.class, "test2.zip");
		environment = b.get();
	}

	/**
	 * Set up
	 */
	@Before
	public void setUp() throws Exception {
		loader1 = new ImpeTemplateLoader(environment.getFileLoader(), template1);
		loader2 = new ImpeTemplateLoader(environment.getFileLoader(), template2);
		loader = new ChainingTemplateLoader(loader1, loader2);
	}

	private void load(String template, String text) throws Exception {
		final Reader reader = loader.getReader(template, "UTF-8");
		final BufferedReader br = new BufferedReader(reader);
		final String line = br.readLine();
		br.close();
		assertTrue(line.indexOf(text) > -1);
	}

	/**
	 * Load
	 */
	@Test
	public void load() throws Exception {
		assertNotNull(loader.findTemplateSource("test.ftl"));
		assertNull(loader.findTemplateSource("test_no.ftl"));
		assertNotNull(loader.findTemplateSource("test_es.ftl"));
		assertNotNull(loader.findTemplateSource("test2.ftl"));
		assertNull(loader.findTemplateSource("test2_no.ftl"));
		assertNotNull(loader.findTemplateSource("test2_es.ftl"));
		load("test.ftl", "Hello");
		load("test_es.ftl", "Hola");
		load("test2.ftl", "Bye");
		load("test2_es.ftl", "Adios");
	}
}
