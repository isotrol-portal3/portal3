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


import java.util.HashMap;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.isotrol.impe3.pms.api.NameDTO;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.type.ContentTypeDTO;
import com.isotrol.impe3.pms.api.type.ContentTypeSelDTO;
import com.isotrol.impe3.pms.api.type.ContentTypesService;
import com.isotrol.impe3.pms.core.MemoryContextTest;


/**
 * Tests for ContentTypesServiceImpl.
 * @author Andres Rodriguez
 */
public class ContentTypesServiceImplTest extends MemoryContextTest {
	private static final String NAME = "name";
	private static final String NAME2 = "name2";

	private static String check(ContentTypeDTO dto, String name) {
		Assert.assertNotNull(dto);
		final String id = dto.getId();
		Assert.assertNotNull(id);
		Assert.assertEquals(name, dto.getDefaultName().getDisplayName());
		return id;
	}

	private static void check(ContentTypeDTO dto, String id, String name) {
		Assert.assertNotNull(dto);
		Assert.assertEquals(id, dto.getId());
		Assert.assertEquals(name, dto.getDefaultName().getDisplayName());
	}

	/** Discovery test. */
	@Test
	public void test() throws PMSException {
		final ContentTypesService service = getBean(ContentTypesService.class);
		List<ContentTypeSelDTO> types = service.getContentTypes();
		Assert.assertNotNull(types);
		Assert.assertTrue(types.isEmpty());
		ContentTypeDTO dto = new ContentTypeDTO();
		dto.setDefaultName(name(NAME));
		dto.setLocalizedNames(new HashMap<String, NameDTO>());
		dto.setRoutable(true);
		dto = service.save(dto);
		final String id = check(dto, NAME);
		types = service.getContentTypes();
		Assert.assertNotNull(types);
		Assert.assertFalse(types.isEmpty());
		dto = service.get(id);
		check(dto, id, NAME);
		dto.setDefaultName(name(NAME2));
		dto = service.save(dto);
		check(dto, id, NAME2);
		Assert.assertEquals(true, dto.isRoutable());
	}
}
