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

package com.isotrol.impe3.pms.core.impl;


import java.io.IOException;
import java.security.SecureRandom;

import net.sf.derquinsej.io.Streams;

import org.junit.Assert;
import org.junit.Test;

import com.isotrol.impe3.api.FileData;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.config.UploadedFileDTO;
import com.isotrol.impe3.pms.core.FileManager;
import com.isotrol.impe3.pms.core.MemoryContextTest;


/**
 * Tests for FileManagerImpl.
 * @author Andres Rodriguez
 */
public class FileManagerImplTest extends MemoryContextTest {
	/** Upload test. */
	@Test
	public void test() throws PMSException, IOException {
		final FileManager service = getBean(FileManager.class);
		final byte[] data = new byte[2048];
		new SecureRandom().nextBytes(data);
		final String name = testString() + ".css";
		final UploadedFileDTO dto = service.upload(name, data);
		Assert.assertNotNull(dto);
		Assert.assertNotNull(dto.getId());
		Assert.assertEquals(name, dto.getName());
		final FileData file = service.getFile(dto.getId());
		Assert.assertEquals(dto.getId(), file.getId().toString());
		byte[] loaded = Streams.consume(file.getData(), true);
		Assert.assertArrayEquals(data, loaded);
		Assert.assertNotNull(file.getMediaType());
	}
}
