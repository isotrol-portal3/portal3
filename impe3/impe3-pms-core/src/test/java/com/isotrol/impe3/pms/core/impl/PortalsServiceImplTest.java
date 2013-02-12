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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.isotrol.impe3.api.DeviceNameUse;
import com.isotrol.impe3.pms.api.NameDTO;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.PropertyDTO;
import com.isotrol.impe3.pms.api.portal.BaseDTO;
import com.isotrol.impe3.pms.api.portal.BasesDTO;
import com.isotrol.impe3.pms.api.portal.DeviceInPortalDTO;
import com.isotrol.impe3.pms.api.portal.IllegalPortalParentException;
import com.isotrol.impe3.pms.api.portal.PortalDevicesTemplateDTO;
import com.isotrol.impe3.pms.api.portal.PortalIATemplateDTO;
import com.isotrol.impe3.pms.api.portal.PortalNameDTO;
import com.isotrol.impe3.pms.api.portal.PortalParentDTO;
import com.isotrol.impe3.pms.api.portal.PortalTreeDTO;
import com.isotrol.impe3.pms.api.portal.PortalURLsDTO;
import com.isotrol.impe3.pms.api.portal.PortalsService;
import com.isotrol.impe3.pms.api.portal.PropertiesDTO;
import com.isotrol.impe3.pms.core.MemoryContextTest;


/**
 * Tests for PortalsServiceImpl.
 * @author Andres Rodriguez
 */
public class PortalsServiceImplTest extends MemoryContextTest {
	private PortalsService service;
	private PortalTreeDTO root;

	// private Map<String, PortalTreeDTO> map;

	@Before
	public void setUp() {
		service = getBean(PortalsService.class);
	}

	/** Tree test. */
	@Test
	public void getPortals() {
		root = service.getPortals();
		Assert.assertNotNull(root);
		Assert.assertNull(root.getNode());
		Assert.assertNotNull(root.getChildren());
	}

	private String create(String parentId) throws PMSException {
		final String id = loadPortal(parentId);
		return id;
	}

	private String create() throws PMSException {
		return create(null);
	}

	/** Save new. */
	@Test
	public void createNew() throws PMSException {
		final NameDTO name = name();
		final String id = loadPortal(name, null);
		final PortalNameDTO saved = service.getName(id);
		assertNotNull(saved);
		assertEquals(id, saved.getId());
		assertNotNull(saved.getName());
		assertEquals(name.getDisplayName(), saved.getName().getDisplayName());
		assertEquals(name.getPath(), saved.getName().getPath());
		checkParent(id, null);
		create();
	}

	/** Update. */
	@Test
	public void update() throws PMSException {
		final String id = create();
		final PortalNameDTO t1 = service.getName(id);
		final String name2 = testString();
		t1.getName().setDisplayName(name2);
		service.setName(t1);
		final PortalNameDTO t2 = service.getName(id);
		assertNotNull(t2);
		assertEquals(t2.getId(), t1.getId());
		assertEquals(t2.getName().getDisplayName(), name2);
	}

	/**
	 * Saves 2 new portals and retrieves them.
	 */
	@Test
	public void testSaveAndRetrieve() throws PMSException {
		root = service.getPortals();
		final int n1 = root.getChildren().size();
		String id1 = create();
		create();
		root = service.getPortals();
		assertTrue(root.getChildren().size() == n1 + 2);
		final String url1 = service.getOfflineURL(id1);
		assertTrue(url1.indexOf(id1) >= 0);
	}

	/** Get bases. */
	@Test
	public void bases() throws PMSException {
		final String id = create();
		final BasesDTO dto = service.getBases(id);
		assertNotNull(dto);
		assertNotNull(dto.getInherited());
		final List<BaseDTO> bases = dto.getBases();
		assertNotNull(bases);
		final String key = testString();
		final String uri = testString();
		bases.add(new BaseDTO(key, uri));
		final BasesDTO dto2 = service.setBases(id, bases);
		assertNotNull(dto);
		assertNotNull(dto.getInherited());
		assertEquals(key, dto2.getBases().get(0).getKey());
		assertEquals(uri, dto2.getBases().get(0).getUri());
	}

	/** Properties. */
	@Test
	public void properties() throws PMSException {
		final String id = create();
		final PropertiesDTO dto = service.getProperties(id);
		assertNotNull(dto);
		assertNotNull(dto.getInherited());
		final List<PropertyDTO> props = dto.getProperties();
		assertNotNull(props);
		final String name = testString();
		final String value = testString();
		props.add(new PropertyDTO(name, value));
		final PropertiesDTO dto2 = service.setProperties(id, props);
		assertNotNull(dto2);
		assertNotNull(dto2.getInherited());
		assertEquals(name, dto2.getProperties().get(0).getName());
		assertEquals(value, dto2.getProperties().get(0).getValue());
	}

	/** Information Architecture. */
	@Test
	public void ia() throws PMSException {
		final String id = create();
		final PortalIATemplateDTO dto = service.getIA(id);
		assertNotNull(dto);
		assertNotNull(dto.getContentTypes());
	}

	private void checkParent(String id, String parentId) throws PMSException {
		final PortalParentDTO dto = service.getParent(id);
		assertNotNull(dto);
		if (parentId == null) {
			assertNull(dto.getParent());
		} else {
			assertNotNull(dto.getParent());
			assertEquals(parentId, dto.getParent().getId());
		}
	}

	private void setParent(String id, String parentId) throws PMSException {
		service.setParent(id, parentId);
		checkParent(id, parentId);
	}

	/** Parent test: OK. */
	@Test
	public void parentOk() throws PMSException {
		final String id1 = create();
		final String id2 = create();
		setParent(id2, id1);
		setParent(id2, null);
		setParent(id1, id2);
		setParent(id1, null);
	}

	/** Parent test: Illegal. */
	@Test(expected = IllegalPortalParentException.class)
	public void parentIllegal() throws PMSException {
		final String id1 = create();
		final String id2 = create();
		setParent(id2, id1);
		setParent(id1, id2);
	}

	/** Save locale with no title. */
	@Test
	public void localeWithoutTitle() throws PMSException {
		String id = create(null);
		PortalNameDTO dto = service.getName(id);
		assertNotNull(dto);
		assertNotNull(dto.getLocales());
		dto.getLocales().put("de", null);
		service.setName(dto);
		dto = service.getName(id);
		assertNotNull(dto);
		assertNotNull(dto.getLocales());
		assertTrue(dto.getLocales().containsKey("de"));
	}

	/** URLs. */
	@Test
	public void urls() throws PMSException {
		String id = create();
		PortalURLsDTO dto = service.getURLs(id);
		assertNotNull(dto);
		assertNotNull(dto.getOffline());
		assertNotNull(dto.getPMS());
	}
	
	/** Devices. */
	@Test
	public void devices() throws PMSException {
		String idp = create();
		PortalDevicesTemplateDTO pd = service.getPortalDevices(idp);
		assertFalse(pd.isInherited());
		DeviceInPortalDTO dip = pd.getDevices().get(0).getNode(); 
		assertFalse(dip.getUse() == DeviceNameUse.EXTENSION);
		dip.setUse(DeviceNameUse.EXTENSION);
		service.setPortalDevices(pd.toPortalDevicesDTO());
		pd = service.getPortalDevices(idp);
		dip = pd.getDevices().get(0).getNode(); 
		assertFalse(pd.isInherited());
		assertTrue(dip.getUse() == DeviceNameUse.EXTENSION);
		// Now: child.
		String idc = create(idp);
		pd = service.getPortalDevices(idc);
		dip = pd.getDevices().get(0).getNode(); 
		assertTrue(pd.isInherited());
		assertTrue(dip.getUse() == DeviceNameUse.EXTENSION);
		pd.setInherited(false);
		dip.setUse(DeviceNameUse.FIRST_SEGMENT);
		service.setPortalDevices(pd.toPortalDevicesDTO());
		pd = service.getPortalDevices(idc);
		dip = pd.getDevices().get(0).getNode(); 
		assertFalse(pd.isInherited());
		assertTrue(dip.getUse() == DeviceNameUse.FIRST_SEGMENT);
		dip.setUse(DeviceNameUse.LAST_SEGMENT);
		service.setPortalDevices(pd.toPortalDevicesDTO());
		pd = service.getPortalDevices(idc);
		dip = pd.getDevices().get(0).getNode(); 
		assertFalse(pd.isInherited());
		assertTrue(dip.getUse() == DeviceNameUse.LAST_SEGMENT);
		pd.setInherited(true);
		service.setPortalDevices(pd.toPortalDevicesDTO());
		pd = service.getPortalDevices(idc);
		dip = pd.getDevices().get(0).getNode(); 
		assertTrue(pd.isInherited());
		assertTrue(dip.getUse() == DeviceNameUse.EXTENSION);
	}
	
}
