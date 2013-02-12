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


import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.component.ComponentInUseException;
import com.isotrol.impe3.pms.api.component.ComponentsService;
import com.isotrol.impe3.pms.core.MemoryContextTest;


/**
 * Component deletion.
 * @author Andres Rodriguez
 */
public class ComponentDeletionTest extends MemoryContextTest {
	private ComponentsService service;
	private String portalId;
	private String componentId;
	private PageLoader loader;

	/**
	 * Set up.
	 * @throws PMSException
	 */
	@Before
	public void setUp() throws PMSException {
		service = getBean(ComponentsService.class);
		portalId = loadPortal();
		componentId = loadSimpleComponent(portalId);
		loader = new PageLoader(portalId);
	}

	/** No page. */
	@Test
	public void no() throws PMSException {
		service.delete(portalId, componentId);
	}

	/** One page. */
	@Test(expected = ComponentInUseException.class)
	public void used() throws PMSException {
		loader.loadSpecial();
		service.delete(portalId, componentId);
	}

	/** One page and publish. */
	@Test(expected = ComponentInUseException.class)
	public void usedP() throws PMSException {
		loader.loadSpecial();
		publish();
		service.delete(portalId, componentId);
	}

	/** No page, 2 components. */
	@Test
	public void no2() throws PMSException {
		final String c2 = loadSimpleComponent(portalId);
		service.delete(portalId, componentId);
		assertNotNull(service.get(portalId, c2));
	}

	/** One page, 2 components. */
	@Test
	public void used2() throws PMSException {
		loader.loadSpecial();
		final String c2 = loadSimpleComponent(portalId);
		service.delete(portalId, c2);
	}
}
