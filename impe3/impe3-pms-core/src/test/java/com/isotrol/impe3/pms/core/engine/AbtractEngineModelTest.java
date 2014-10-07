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


import java.util.Locale;
import java.util.UUID;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.junit.BeforeClass;

import com.google.common.collect.Multimap;
import com.isotrol.impe3.api.PathSegments;
import com.isotrol.impe3.core.Engine;
import com.isotrol.impe3.core.EngineModel;
import com.isotrol.impe3.core.engine.DefaultOfflineEngine;
import com.isotrol.impe3.core.engine.DefaultOnlineEngine;
import com.isotrol.impe3.core.engine.RequestContexts;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.page.PageLoc;
import com.isotrol.impe3.pms.core.MemoryContextTest;


/**
 * Tests for EngineModelLoaderImpl.
 * @author Andres Rodriguez
 */
public abstract class AbtractEngineModelTest extends MemoryContextTest {
	private static final HttpHeaders EMPTY_HEADERS = new MockHttpHeaders();

	/**
	 * Class set up.
	 * @throws PMSException
	 */
	@BeforeClass
	public static void load() throws PMSException {
		getBean(EngineModelLoader.class);
	}

	protected static void out(Response response) throws Exception {
		System.out.println("------------------------------------------------------------------");
		((StreamingOutput) response.getEntity()).write(System.out);
		System.out.println("------------------------------------------------------------------");
	}

	protected final DefaultOfflineEngine getOffline() throws Exception {
		final EngineModel model = getOfflineModel();
		return new DefaultOfflineEngine(model);
	}

	protected final DefaultOnlineEngine getOnline() throws Exception {
		final EngineModel model = getOnlineModel();
		return new DefaultOnlineEngine(model);
	}

	protected static void preview(DefaultOfflineEngine engine, PageLoc page) throws Exception {
		Response response = engine.getPreview(UUID.fromString(page.getPortalId()), UUID.fromString(page.getId()),
			new Locale("es"));
		out(response);
	}

	private static void process(Engine engine, PathSegments path, Multimap<String, String> params) throws Exception {
		Response response = engine.process(path, EMPTY_HEADERS, RequestContexts.http()).getResponse();
		out(response);
	}

	protected void processOffline(PathSegments path, Multimap<String, String> params) throws Exception {
		System.out.println("OFFLINE REQUEST");
		process(getOffline(), path, params);
	}

	protected void processOnline(PathSegments path, Multimap<String, String> params) throws Exception {
		System.out.println("ONLINE REQUEST");
		process(getOnline(), path, params);
	}

}
