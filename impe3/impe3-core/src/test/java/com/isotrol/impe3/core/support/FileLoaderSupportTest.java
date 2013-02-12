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

package com.isotrol.impe3.core.support;


import static com.isotrol.impe3.core.support.FileLoaderSupport.getMediaType;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;
import java.util.UUID;

import junit.framework.Assert;
import net.sf.derquinsej.io.Streams;

import org.junit.Test;

import com.isotrol.impe3.api.FileBundleData;
import com.isotrol.impe3.api.FileData;


/**
 * Tests for FileLoaderSupport.
 * @author Andres Rodriguez.
 */
public class FileLoaderSupportTest {
	@Test
	public void extensions() {
		Assert.assertEquals("image/png", getMediaType("test.png").toString());
	}

	private FileBundleData loadBundle(String name) throws Exception {
		byte[] data = Streams.consume(getClass().getResourceAsStream(name), true);
		FileData file = new FileData(UUID.randomUUID(), name, getMediaType("name"), true, true, data, false);
		return FileLoaderSupport.loadBundle(file);
	}

	private void testCatalog(String name) throws Exception {
		final Set<String> catalog = loadBundle(name).getFileNames();
		assertNotNull(catalog);
		assertFalse(catalog.isEmpty());
		System.out.println("--> Start: " + name);
		for (String file : catalog) {
			System.out.println(file);
		}
		System.out.println("--> End: " + name);

	}

	/**
	 * Catalog test.
	 */
	@Test
	public void catalog() throws Exception {
		testCatalog("zip-xp.zip");
		testCatalog("zip-linux.zip");
	}

	private byte[] loadData(String bundleName, String fileName) throws Exception {
		return Streams.consume(loadBundle(bundleName).apply(fileName).getData(), true);
	}

	/**
	 * Data test.
	 */
	@Test
	public void data() throws Exception {
		byte[] data = loadData("zip-xp.zip", "folder2/file1.txt");
		assertNotNull(data);
		assertTrue(data.length > 0);
		final String s = new String(data);
		System.out.println("--> Data: " + s);
		assertTrue(s.indexOf("df") >= 0);
	}

	/**
	 * Relative data test.
	 */
	@Test
	public void relative() throws Exception {
		byte[] data = loadData("zip-xp.zip", "./folder2/file1.txt");
		assertNotNull(data);
		assertTrue(data.length > 0);
		final String s = new String(data);
		System.out.println("--> Data: " + s);
		assertTrue(s.indexOf("df") >= 0);
	}

}
