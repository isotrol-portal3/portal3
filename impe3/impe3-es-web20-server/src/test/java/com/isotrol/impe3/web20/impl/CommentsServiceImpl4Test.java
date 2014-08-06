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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.MemoryContextTest;
import com.isotrol.impe3.web20.api.CommentDTO;
import com.isotrol.impe3.web20.api.CommentFilterDTO;
import com.isotrol.impe3.web20.api.CommentRateDTO;
import com.isotrol.impe3.web20.api.CommentsService;
import com.isotrol.impe3.web20.api.MaxCommentRatesException;
import com.isotrol.impe3.web20.api.NotAllowOwnerCommentRateException;
import com.isotrol.impe3.web20.api.ResourceByCommunityCounterDTO;
import com.isotrol.impe3.web20.api.ResourceCounterDTO;


/**
 * Test for unmoderated comments.
 * @author Andres Rodriguez
 * 
 */
public class CommentsServiceImpl4Test extends MemoryContextTest {

	private CommentsService commentsService;
	private String serviceId = "test_service_id";

	@Before
	public void setUp() {
		commentsService = getBean(CommentsService.class);
	}

	@Test
	public void test() throws ServiceException {
		final CommentDTO comment = getComment();
		final Long commentId = commentsService.comment(serviceId, comment);
		assertNotNull(commentId);

		final CommentDTO c = commentsService.getById(serviceId, commentId);
		assertNotNull(c);
		
		commentsService.moderate(serviceId, commentId, "yo", true);


		CommentFilterDTO filter = new CommentFilterDTO();
		filter.setResourceKey("00-00");
		
		PageDTO<CommentDTO> dtos = commentsService.getResourceComments(serviceId, filter);
		assertNotNull(dtos);
		
		final CommentRateDTO rate = commentsService.rate(serviceId, commentId, null, "guest", 10, true, 1);
		assertNotNull(rate);
		
		try {
			commentsService.rate(serviceId, commentId, null, "guest", 5, false, 2);
			Assert.fail();
		} catch(NotAllowOwnerCommentRateException e) {
			Assert.assertTrue(true);
		}

		try {
			commentsService.rate(serviceId, commentId, null, "guest", 5, true, 1);
			Assert.fail();
		} catch(MaxCommentRatesException e) {
			Assert.assertTrue(true);
		} 

		assertNotNull(commentsService.rate(serviceId, commentId, null, "guest2", 6, true, 1));
		
		
	}

	private CommentDTO getComment() {
		final CommentDTO comment = new CommentDTO();
		comment.setCommunityId(null);
		comment.setResourceKey("00-00");
		comment.setDate(new Date());
		comment.setDescription("comment description.");
		comment.setEmail("e@e.e");
		comment.setRate(0);
		comment.setNumberOfRates(0);
		comment.setTitle("comment title");
		comment.setValid(true);
		comment.setOrigin("guest");

		return comment;
	}
}
