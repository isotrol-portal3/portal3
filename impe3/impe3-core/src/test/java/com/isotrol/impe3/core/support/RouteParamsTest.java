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

package com.isotrol.impe3.core.support;

import static org.junit.Assert.assertNotNull;

import javax.ws.rs.core.MultivaluedMap;

import org.junit.Test;

import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.EngineMode;
import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.Portal;
import com.isotrol.impe3.api.Route;
import com.isotrol.impe3.core.PortalBuilder;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * Route params resolution test.
 * @author Emilio Escobar Reyero.
 */
public class RouteParamsTest {

	private static final String TYPE = "i3pt";
	private static final String CONTENT_TYPE = "i3ct";
	private static final String CATEGORY = "i3cg";

	private static final String TYPE_L = "L";
	
	@Test
	public void test() {
		final MultivaluedMap<String, String> p = new MultivaluedMapImpl();
		p.add(TYPE, TYPE_L);
		
		
		final PortalBuilder pb = new PortalBuilder();
		final Category cat = pb.category("categoria", null);
		final ContentType cnt = pb.contentType("contenido");

		p.add(CONTENT_TYPE, cnt.getStringId());
		p.add(CATEGORY, cat.getStringId());

		
		final Portal portal = pb.getPortal(EngineMode.OFFLINE);
		
		final Route route = RouteParams.fromParams(portal, null, p);
		
		assertNotNull(route);
		
		final PageKey pk = route.getPage();

		assertNotNull(pk);
		
		
	}
	
	
	
	
}
