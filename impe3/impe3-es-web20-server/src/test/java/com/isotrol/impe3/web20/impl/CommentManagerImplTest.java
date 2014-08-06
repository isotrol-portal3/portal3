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
package com.isotrol.impe3.web20.impl;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.api.CommentDTO;
import com.isotrol.impe3.web20.api.CommentsService;
import com.isotrol.impe3.web20.api.ResourceCounterDTO;
import com.isotrol.impe3.web20.server.CommentManager;
import com.isotrol.impe3.web20.server.CommentMap;


/**
 * Tests for CommentManagerImplTest.
 * @author Andres Rodriguez
 */
public class CommentManagerImplTest extends AbstractGroupTest {
	private CommentManager manager;
	private CommentsService service;

	@Before
	public void setUp() {
		manager = getBean(CommentManager.class);
		service = getBean(CommentsService.class);
	}

	@Test
	public void test() throws ServiceException {
		assertNotNull(manager);
		assertNotNull(service);
		CommentMap map = manager.loadTimeMap(null);
		assertNotNull(map);
		assertEquals(0L, map.size());
		service.comment(null, getComment());
		map = manager.loadTimeMap(100000L);
		assertNotNull(map);
		assertEquals(4L, map.size());
		List<ResourceCounterDTO> list = map.get(null, null, null, 10);
		assertNotNull(list);
		assertEquals(1, list.size());
		assertEquals(RESOURCE1, list.get(0).getResource());
		assertEquals(1, list.get(0).getCount());
		list = map.get(null, false, null, 10);
		assertNotNull(list);
		assertEquals(0, list.size());
	}
	
	private CommentDTO getComment() {
		final CommentDTO comment = new CommentDTO();
		comment.setCommunityId(null);
		comment.setResourceKey(RESOURCE1);
		comment.setDate(new Date());
		comment.setDescription("comment description.");
		comment.setEmail("e@e.e");
		comment.setRate(0);
		comment.setTitle("comment title");
		comment.setValid(true);
		comment.setOrigin("guest");
		return comment;
	}
	
}
