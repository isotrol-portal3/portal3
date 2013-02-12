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


import static org.junit.Assert.assertEquals;

import java.security.SecureRandom;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.isotrol.impe3.api.Configuration;
import com.isotrol.impe3.api.FileId;
import com.isotrol.impe3.core.config.ConfigurationDefinition;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.config.ConfigurationItemDTO;
import com.isotrol.impe3.pms.api.config.UploadedFileDTO;
import com.isotrol.impe3.pms.core.ConfigurationManager;
import com.isotrol.impe3.pms.core.FileManager;
import com.isotrol.impe3.pms.core.MemoryContextTest;


/**
 * Tests for FileManagerImpl.
 * @author Andres Rodriguez
 */
public class FilePurgeTest extends MemoryContextTest {
	/** Upload test. */
	@Test
	public void test() throws Exception {
		final UploadedFileDTO f1 = upload();
		Thread.sleep(2000);
		final UploadedFileDTO f2 = upload();
		Thread.sleep(2000);
		final ConfigurationManager m = getBean(ConfigurationManager.class);
		ConfigurationItemDTO c = new ConfigurationItemDTO();
		c.setKey("file");
		c.setCurrentValue(f2.getId());
		m.create(ConfigurationDefinition.of(Config.class), ImmutableList.of(c));
		final FileManager service = getBean(FileManager.class);
		assertEquals(1, service.purge(1));
		Assert.assertNotNull(service.getFile(f2.getId()));
		try {
			service.getFile(f1.getId());
			Assert.fail();
		} catch(PMSException e) {
		}
	}

	private UploadedFileDTO upload() throws Exception {
		final FileManager service = getBean(FileManager.class);
		final byte[] data = new byte[2048];
		new SecureRandom().nextBytes(data);
		final String name = testString() + ".css";
		return service.upload(name, data);
	}

	interface Config extends Configuration {
		FileId file();
	}
}
