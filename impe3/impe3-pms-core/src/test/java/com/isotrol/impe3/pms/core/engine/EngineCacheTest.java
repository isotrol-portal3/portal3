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

package com.isotrol.impe3.pms.core.engine;


import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.junit.Test;

import com.isotrol.impe3.core.EngineModel;
import com.isotrol.impe3.core.PortalModel;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.portal.PortalNameDTO;
import com.isotrol.impe3.pms.api.portal.PortalsService;


/**
 * Engine cache check.
 * @author Andres Rodriguez
 */
public class EngineCacheTest extends AbtractEngineModelTest {
	/**
	 * Portal with default page.
	 */
	@Test
	public void test() throws Exception {
		loadContentType();
		final UUID portalId = UUID.fromString(loadPortal());
		EngineModel fem1 = getOfflineModel();
		PortalModel fpm1 = fem1.getPortal(portalId);
		EngineModel fem2 = getOfflineModel();
		PortalModel fpm2 = fem2.getPortal(portalId);
		assertTrue(fem1 == fem2);
		assertTrue(fpm1 == fpm2);
		publish();
		fem2 = getOfflineModel();
		fpm2 = fem2.getPortal(portalId);
		assertTrue(fem1 != fem2);
		assertTrue(fpm1 != fpm2);
		EngineModel oem1 = getOnlineModel();
		PortalModel opm1 = oem1.getPortal(portalId);
		EngineModel oem2 = getOnlineModel();
		PortalModel opm2 = oem2.getPortal(portalId);
		assertTrue(oem1 == oem2);
		assertTrue(opm1 == opm2);
		update(portalId);
		fem1 = getOfflineModel();
		fpm1 = fem1.getPortal(portalId);
		// TODO: fix
		// assertTrue(fem1 == fem2);
		assertTrue(fpm1 != fpm2);
		oem1 = getOnlineModel();
		opm1 = oem1.getPortal(portalId);
		assertTrue(oem1 == oem2);
		assertTrue(opm1 == opm2);
		loadContentType();
		fem2 = getOfflineModel();
		fpm2 = fem2.getPortal(portalId);
		assertTrue(fem1 != fem2);
		assertTrue(fpm1 != fpm2);
		oem2 = getOnlineModel();
		opm2 = oem1.getPortal(portalId);
		assertTrue(oem1 == oem2);
		assertTrue(opm1 == opm2);
	}

	private void update(UUID portalId) throws PMSException {
		String id = portalId.toString();
		PortalNameDTO dto = getBean(PortalsService.class).getName(id);
		dto.setName(name());
		getBean(PortalsService.class).setName(dto);
	}
}
