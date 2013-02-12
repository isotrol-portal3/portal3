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
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.isotrol.impe3.api.ClientRequestContext;
import com.isotrol.impe3.api.Device;
import com.isotrol.impe3.api.Portal;
import com.isotrol.impe3.api.content.ContentCriteria;
import com.isotrol.impe3.api.content.ContentLoader;
import com.isotrol.impe3.api.support.DefaultDeviceCapabilities;
import com.isotrol.impe3.core.PortalModel;
import com.isotrol.impe3.core.engine.RequestContexts;
import com.isotrol.impe3.nr.api.FilterType;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.portal.BaseDTO;
import com.isotrol.impe3.pms.api.portal.BasesDTO;
import com.isotrol.impe3.pms.api.portal.PortalsService;
import com.isotrol.impe3.pms.api.portal.SetFilterDTO;
import com.isotrol.impe3.pms.core.MemoryContextTest;


/**
 * Tests for portal bases.
 * @author Andres Rodriguez
 */
public class PortalBasesTest extends MemoryContextTest {
	public static final String K1 = "key1";
	public static final String U1 = "http://uri1.net";
	public static final String K2 = "key2";
	public static final String U2 = "http://uri2.net/kk";

	private PortalsService service;
	private String portalId;

	/** Set up. */
	@Before
	public void setUp() throws PMSException {
		service = getBean(PortalsService.class);
		portalId = loadPortal();
	}

	/** Bases life cycle. */
	@Test
	public void bases() throws PMSException {
		BasesDTO bases = service.getBases(portalId);
		assertNotNull(bases);
		assertTrue(bases.getBases().isEmpty());
		assertTrue(bases.getInherited().isEmpty());
		bases = service.setBases(portalId, Lists.newArrayList(new BaseDTO(K1, U1)));
		assertFalse(bases.getBases().isEmpty());
		assertTrue(bases.getInherited().isEmpty());
		BaseDTO base = bases.getBases().get(0);
		assertEquals(K1, base.getKey());
		assertEquals(U1, base.getUri());
		publish();
		bases = service.getBases(portalId);
		assertFalse(bases.getBases().isEmpty());
		assertTrue(bases.getInherited().isEmpty());
		base = bases.getBases().get(0);
		assertEquals(K1, base.getKey());
		assertEquals(U1, base.getUri());
		// Changed
		bases = service.setBases(portalId, Lists.newArrayList(new BaseDTO(K2, U2)));
		assertFalse(bases.getBases().isEmpty());
		assertTrue(bases.getInherited().isEmpty());
		base = bases.getBases().get(0);
		assertEquals(K2, base.getKey());
		assertEquals(U2, base.getUri());
		// Add set
		SetFilterDTO f = new SetFilterDTO();
		f.setName(K1);
		f.setType(FilterType.OPTIONAL);
		service.putSetFilter(portalId, f);
		// Create model
		PortalModel pm = getOfflinePortalModel(portalId);
		Portal p = pm.getPortal();
		assertEquals(U2, p.getBase(K2).toASCIIString());
		assertEquals(1, p.getSetFilters().size());
		assertEquals(K1, p.getSetFilters().keySet().iterator().next());
		assertEquals(FilterType.OPTIONAL, p.getSetFilters().values().iterator().next());
		final Device device = pm.getDevices().values().iterator().next();
		ClientRequestContext crc = RequestContexts.client(device, DefaultDeviceCapabilities.get(device),
			p.getDefaultLocale());
		ContentLoader c = pm.getContentLoader(crc);
		assertNotNull(c);
		ContentCriteria cc = c.newCriteria();
		assertNotNull(cc);
	}

	/** Available bases. */
	@Test
	public void available() throws PMSException {
		assertNotNull(service.getAvailableBases(portalId));
		assertNotNull(service.getAvailableProperties(portalId));
	}

	/** Publishing. */
	@Test
	public void editions() throws PMSException {
		final String pid = loadPortal();
		service.setBases(pid, Lists.newArrayList(new BaseDTO(K1, U1)));
		publish();
		assertEquals(U1, getOfflinePortalModel(pid).getPortal().getBase(K1).toASCIIString());
		assertEquals(U1, getOnlinePortalModel(pid).getPortal().getBase(K1).toASCIIString());
		service.setBases(pid, Lists.newArrayList(new BaseDTO(K1, U2)));
		assertEquals(U2, getOfflinePortalModel(pid).getPortal().getBase(K1).toASCIIString());
		assertEquals(U1, getOnlinePortalModel(pid).getPortal().getBase(K1).toASCIIString());
		publish();
		assertEquals(U2, getOfflinePortalModel(pid).getPortal().getBase(K1).toASCIIString());
		assertEquals(U2, getOnlinePortalModel(pid).getPortal().getBase(K1).toASCIIString());
	}

}
