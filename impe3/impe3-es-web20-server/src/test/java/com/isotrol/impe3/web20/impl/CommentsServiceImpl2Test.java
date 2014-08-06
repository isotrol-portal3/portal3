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

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.MemoryContextTest;
import com.isotrol.impe3.web20.api.CommentDTO;
import com.isotrol.impe3.web20.api.CommentsService;
import com.isotrol.impe3.web20.api.ResourceByCommunityCounterDTO;
import com.isotrol.impe3.web20.api.ResourceCounterDTO;


/**
 * Test for unmoderated comments.
 * @author Andres Rodriguez
 * 
 */
public class CommentsServiceImpl2Test extends MemoryContextTest {

	private CommentsService commentsService;
	private String serviceId = "test_service_id";

	@Before
	public void setUp() {
		commentsService = getBean(CommentsService.class);
	}

	private void check(Collection<?> c, int size) {
		assertNotNull(c);
		assertEquals(size, c.size());
	}

	private void check(List<ResourceCounterDTO> l, int size, long count) {
		check(l, size);
		if (size > 0) {
			assertEquals(count, l.get(0).getCount());
		}
	}
	
	@Test
	public void test() throws ServiceException {
		check(commentsService.getRWUC(serviceId, 10), 0);
		check(commentsService.getRWUCInCommunity(serviceId, null, 10), 0);
		final CommentDTO comment = getComment();
		final Long comment1Id = commentsService.comment(serviceId, comment);
		assertNotNull(comment1Id);
		check(commentsService.getRWUCInCommunity(serviceId, null, 10), 1, 1);
		List<ResourceByCommunityCounterDTO> list = commentsService.getRWUC(serviceId, 10); 
		check(list, 1);
		check(list.get(0).getCommunities(), 1);
		final Long comment2Id = commentsService.comment(serviceId, comment);
		assertNotNull(comment2Id);
		check(commentsService.getRWUCInCommunity(serviceId, null, 10), 1, 2);
		list = commentsService.getRWUC(serviceId, 10); 
		check(list, 1);
		check(list.get(0).getCommunities(), 1);
		commentsService.moderate(serviceId, comment2Id, "guest", true);
		check(commentsService.getRWUCInCommunity(serviceId, null, 10), 1, 1);
	}

	private CommentDTO getComment() {
		final CommentDTO comment = new CommentDTO();
		comment.setCommunityId(null);
		comment.setResourceKey("00-00");
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
