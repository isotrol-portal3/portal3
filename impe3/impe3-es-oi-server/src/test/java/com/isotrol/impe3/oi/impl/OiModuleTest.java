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
package com.isotrol.impe3.oi.impl;


import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.isotrol.impe3.dto.OrderDTO;
import com.isotrol.impe3.dto.PageDTO;
import com.isotrol.impe3.dto.PageFilter;
import com.isotrol.impe3.dto.PaginationDTO;
import com.isotrol.impe3.dto.ServiceException;
import com.isotrol.impe3.oi.MemoryContextTest;
import com.isotrol.impe3.oi.api.ClassesService;
import com.isotrol.impe3.oi.api.InterviewDTO;
import com.isotrol.impe3.oi.api.InterviewsService;
import com.isotrol.impe3.oi.api.MembersService;
import com.isotrol.impe3.oi.api.QaADTO;
import com.isotrol.impe3.oi.api.QaAFilterDTO;
import com.isotrol.impe3.oi.api.QaAsService;


/**
 * Complete module test
 * @author Emilio Escobar Reyero
 */
public class OiModuleTest extends MemoryContextTest {

	private InterviewsService interviewsService;
	private MembersService membersService;
	private QaAsService questionsService;
	private ClassesService classesService;

	private final String serviceId = "service";

	@Before
	public void setUp() {
		interviewsService = getBean(InterviewsService.class);
		membersService = getBean(MembersService.class);
		questionsService = getBean(QaAsService.class);
		classesService = getBean(ClassesService.class);
	}

	@Test
	public void test() {
		assertNotNull(interviewsService);
		assertNotNull(membersService);
		assertNotNull(questionsService);
		assertNotNull(classesService);
	}

	@Test
	public void testModule() throws ServiceException {
		// add classes
		classesService.addClassification(serviceId, "CLASSIFICATION", "sport");
		classesService.addClassification(serviceId, "CLASSIFICATION", "world");

		// create interview
		final String interviewId = interviewsService.create(serviceId, interview());
		assertNotNull(interviewId);

		// read interview
		InterviewDTO interviewDTO = interviewsService.getById(serviceId, interviewId);
		assertNotNull(interviewDTO);
		assertEquals(interviewId, interviewDTO.getId());

		// classificate interview
		interviewsService.classify(serviceId, interviewId, "CLASSIFICATION", Sets.newHashSet("sport"));

		// read interview again
		interviewDTO = interviewsService.getById(serviceId, interviewId);

		// check classifications
		Map<String, Set<String>> classes = interviewDTO.getClasses();
		assertNotNull(classes);
		assertTrue(!classes.isEmpty());
		assertTrue(classes.containsKey("CLASSIFICATION"));
		assertTrue(!classes.get("CLASSIFICATION").isEmpty());
		assertTrue(classes.get("CLASSIFICATION").contains("sport"));

		// add question
		Long q1 = questionsService.ask(serviceId, interviewId, "we wish you a merry christmas... ", "member41");

		// recover questions
		PageDTO<QaADTO> page = interviewsService.getInterviewQuestions(serviceId, PageFilter.of(new QaAFilterDTO()
			.putId(interviewId)));
		assertNotNull(page);
		assertTrue(page.getTotal() == 1);

		// add question
		Long q2 = questionsService.ask(serviceId, interviewId, "... and a happy new year", "member29");

		// filter questions
		page = interviewsService.getInterviewQuestions(serviceId, PageFilter.of(new QaAFilterDTO().putId(interviewId)
			.putMember("member41")));
		assertNotNull(page);
		assertTrue(page.getTotal() == 1);

		// answer
		questionsService.answer(serviceId, q1, "venimos a desearle muy felices navidades...");
		// filter questions
		page = interviewsService.getInterviewQuestions(serviceId, PageFilter.of(new QaAFilterDTO().putId(interviewId)
			.putAnswered(false)));
		assertNotNull(page);
		assertTrue(page.getTotal() == 1);

		// rate
		try {
			questionsService.rate(serviceId, q1, "member41", 49D, false);
			fail();
		} catch (ServiceException e) {
			questionsService.rate(serviceId, q1, "member41", 49D, true);
		}
		questionsService.rate(serviceId, q2, "member41", 12D, false);

		// most rated!
		page = interviewsService.getInterviewQuestions(serviceId, PageFilter.of(new QaAFilterDTO().putId(interviewId),
			new PaginationDTO(), Lists.newArrayList(new OrderDTO("rate", false))));
		assertNotNull(page);
		assertEquals(q1, page.getElements().get(0).getId());

		// change rate
		questionsService.rate(serviceId, q2, "member41", 120D, false);
		page = interviewsService.getInterviewQuestions(serviceId, PageFilter.of(new QaAFilterDTO().putId(interviewId),
			new PaginationDTO(), Lists.newArrayList(new OrderDTO("rate", false))));
		assertNotNull(page);
		assertEquals(q2, page.getElements().get(0).getId());
		
		
		// validate
		questionsService.validate(serviceId, q1, false);
		questionsService.validate(serviceId, q2, true);
		
		page = interviewsService.getInterviewQuestions(serviceId, PageFilter.of(new QaAFilterDTO().putId(interviewId)
			.putValid(true)));
		assertNotNull(page);
		assertTrue(page.getTotal() == 1);
		assertEquals(q2, page.getElements().get(0).getId());
		
	}

	@Test
	public void testCreateClass() throws ServiceException {
		final Map<String, Set<String>> classes = Maps.newHashMap();
		classes.put("TEST", Sets.newHashSet("test1", "test2"));
		
		final InterviewDTO interview = interview();
		interview.setClasses(classes);
		
		final String uuid = interviewsService.create(serviceId, interview);
		
		assertNotNull(uuid);
		
		final InterviewDTO dto = interviewsService.getById(serviceId, uuid);
		
		assertNotNull(dto);
		
		final Map<String, Set<String>> classesRecovered = dto.getClasses();
		
		assertNotNull(classesRecovered);
	}
	
	private InterviewDTO interview() {
		final InterviewDTO dto = new InterviewDTO();

		dto.setAuthor("author");
		dto.setDate(new Date());
		dto.setDescription("description");
		dto.setInterviewee("interviewee");
		dto.setNewQuestionsAllowed(true);
		dto.setTitle("title");
		
		Map<String, String> properties = Maps.newHashMap();
		properties.put("PP_KEY", "value");
		dto.setProperties(properties);
		

		return dto;
	}
}
