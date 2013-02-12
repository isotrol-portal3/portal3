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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.smap.ContentTypeMappingDTO;
import com.isotrol.impe3.pms.api.smap.SourceMappingDTO;
import com.isotrol.impe3.pms.api.smap.SourceMappingTemplateDTO;
import com.isotrol.impe3.pms.api.smap.SourceMappingsService;
import com.isotrol.impe3.pms.api.type.ContentTypeSelDTO;
import com.isotrol.impe3.pms.core.MemoryContextTest;


/**
 * Tests for SourceMappingsImpl.
 * @author Andres Rodriguez
 */
public class SourceMappingsServiceImplTest extends MemoryContextTest {

	/** Created test. */
	@Test
	public void test() throws PMSException {
		loadContentType();
		final SourceMappingsService service = getBean(SourceMappingsService.class);
		final SourceMappingTemplateDTO template = service.newTemplate();
		final SourceMappingDTO dto = template.getMapping();
		dto.setName(testString());
		dto.setDescription(testString());
		ContentTypeMappingDTO m = new ContentTypeMappingDTO();
		final List<ContentTypeSelDTO> cts = template.getContentTypes();
		final ContentTypeSelDTO contentType = cts.get(0);
		m.setContentType(contentType);
		final String mappingTest = testString();
		m.setMapping(mappingTest);
		dto.setContentTypes(Lists.newArrayList(m));
		final SourceMappingTemplateDTO saved = service.save(dto);
		assertNotNull(saved);
		assertNotNull(saved.getMapping());
		assertNotNull(saved.getMapping().getContentTypes());
		assertFalse(saved.getMapping().getContentTypes().isEmpty());
		final ContentTypeMappingDTO savedContentType = saved.getMapping().getContentTypes().get(0);
		assertNotNull(savedContentType);
		assertNotNull(savedContentType.getContentType());
		assertNotNull(savedContentType.getMapping());
		assertEquals(contentType.getId(), savedContentType.getContentType().getId());
		assertEquals(mappingTest, savedContentType.getMapping());
	}
}
