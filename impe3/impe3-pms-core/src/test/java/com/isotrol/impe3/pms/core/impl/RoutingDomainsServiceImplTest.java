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

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.junit.Before;
import org.junit.Test;

import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.rd.RoutingDomainDTO;
import com.isotrol.impe3.pms.api.rd.RoutingDomainsService;
import com.isotrol.impe3.pms.core.MemoryContextTest;


/**
 * Tests for PortalsServiceImpl.
 * @author Andres Rodriguez
 */
public class RoutingDomainsServiceImplTest extends MemoryContextTest {
	private static String BASE = "http://www.google.com";
	private static String BASE2 = "http://www.google.es";
	private static String ROOT = "/";
	private RoutingDomainsService service;

	@Before
	public void setUp() {
		service = getBean(RoutingDomainsService.class);
	}

	/**
	 * Base.
	 * @throws PMSException
	 */
	@Test
	public void base() throws PMSException {
		RoutingDomainDTO dto = service.getDefault();
		dto.setOnlineBase(BASE);
		dto.setOfflineBase(BASE);
		RoutingDomainDTO dto2 = service.setDefault(dto);
		assertEquals(BASE, dto2.getOfflineBase());
		assertEquals(BASE, dto2.getOnlineBase());
		dto2.setOfflineBase(BASE2);
		RoutingDomainDTO dto3 = service.setDefault(dto2);
		assertEquals(BASE2, dto3.getOfflineBase());
		assertEquals(BASE, dto3.getOnlineBase());
		dto3.setOnlineAbsBase(BASE2);
		dto3.setOnlineBase(ROOT);
		RoutingDomainDTO dto4 = service.setDefault(dto3);
		assertEquals(ROOT, dto4.getOnlineBase());
		assertEquals(BASE2, dto4.getOnlineAbsBase());
	}
	
	@Test
	public void root() throws Exception {
		URI uri = URI.create(ROOT);
		String test = UriBuilder.fromUri(uri).segment("hi").build().toASCIIString();
		assertEquals("/hi", test);
	}
}
