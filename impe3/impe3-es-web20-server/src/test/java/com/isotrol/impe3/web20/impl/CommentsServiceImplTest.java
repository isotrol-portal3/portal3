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


import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.web20.MemoryContextTest;
import com.isotrol.impe3.web20.api.CommentDTO;
import com.isotrol.impe3.web20.api.CommentFilterDTO;
import com.isotrol.impe3.web20.api.CommentsService;
import com.isotrol.impe3.web20.api.CommunitiesService;
import com.isotrol.impe3.web20.api.CommunityDTO;
import com.isotrol.impe3.web20.api.MemberDTO;
import com.isotrol.impe3.web20.api.MembersService;


/**
 * 
 * @author Emilio Escobar Reyero
 *
 */
public class CommentsServiceImplTest extends MemoryContextTest {

	private CommentsService commentsService;
	private MembersService membersService;
	private CommunitiesService communitiesService;
	private String serviceId = "test_service_id";

	@Before
	public void setUp() {
		commentsService = getBean(CommentsService.class);
		membersService = getBean(MembersService.class);
		communitiesService = getBean(CommunitiesService.class);
	}

	@Test
	public void testCommentOrigin() throws ServiceException {
		final CommentDTO comment = getComment();
		final Long commentid = commentsService.comment(serviceId, comment);
		
		Assert.assertNotNull(commentid);
	}
	
	@Test
	public void testCommentMember() throws ServiceException {
		final MemberDTO member = getMember("testCommentMember");
		
		final String memberId = membersService.create(serviceId, member);
		
		Assert.assertNotNull(memberId);
		
		final CommentDTO comment = getComment();

		comment.setMemberId(memberId);
		
		final Long commentid = commentsService.comment(serviceId, comment);
		
		
		Assert.assertNotNull(commentid);
		
	}

	@Test
	public void testCommentMemberCommunity() throws ServiceException {
		final MemberDTO member = getMember("testCommentMemberCommunity");
		final String memberId = membersService.create(serviceId, member);
		Assert.assertNotNull(memberId);

		final CommunityDTO community = getCommunity("testCommentMemberCommunity");
		final String communityId = communitiesService.create(memberId, community);
		Assert.assertNotNull(communityId);
		
		membersService.addToCommunity(communityId, memberId, communityId, "member", null, true);
		
		final CommentDTO comment = getComment();
		comment.setMemberId(memberId);
		comment.setCommunityId(communityId);
		
		
		final Long commentid = commentsService.comment(serviceId, comment);
		Assert.assertNotNull(commentid);
	}

	@Test
	public void testCommentSearch() throws ServiceException {
		final CommentDTO comment = getComment();
		comment.setResourceKey("testCommentSearch");
		boolean par = true;
		for (int i = 0; i < 100; i++) {
			final Long commentId = commentsService.comment(serviceId, comment);
			Assert.assertNotNull(commentId);
			if (par) {
				commentsService.moderate(serviceId, commentId, "test", true);
			}
			par = !par;
		}

		final CommentFilterDTO filter = new CommentFilterDTO();
		filter.setModerated(true);
		filter.setResourceKey("testCommentSearch");
		filter.setValid(true);
		
		final PageDTO<CommentDTO> page = commentsService.getResourceComments(serviceId, filter);
		Assert.assertNotNull(page);
		Assert.assertTrue(page.getTotal() == 50);
	}

	
	private CommunityDTO getCommunity(String prefix) {
		final CommunityDTO community = new CommunityDTO();
		community.setCode(prefix + "code");
		community.setDate(new Date());
		community.setDescription("Communtiy description. ");
		community.setName(prefix + "name");
		
		return community;
	}

	private MemberDTO getMember(String prefix) {
		final MemberDTO member = new MemberDTO();
		member.setCode(prefix + "memberCode");
		member.setName(prefix + "memberName");
		member.setDate(new Date());
		member.setEmail("email@member.es");
		member.setBlocked(false);
		member.setDisplayName("display name");
		
		return member;
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
