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


import static java.util.UUID.fromString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.junit.Test;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.isotrol.impe3.api.EngineMode;
import com.isotrol.impe3.api.PathSegments;
import com.isotrol.impe3.core.Engine;
import com.isotrol.impe3.core.EngineModel;
import com.isotrol.impe3.core.engine.DefaultOfflineEngine;
import com.isotrol.impe3.core.engine.DefaultOnlineEngine;
import com.isotrol.impe3.core.engine.RequestContexts;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.connector.ConnectorsService;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceTemplateDTO;
import com.isotrol.impe3.pms.api.page.PageClass;
import com.isotrol.impe3.pms.api.page.PageLoc;
import com.isotrol.impe3.pms.api.portal.PortalsService;
import com.isotrol.impe3.pms.core.EnvironmentManager;
import com.isotrol.impe3.pms.core.MemoryContextTest;
import com.isotrol.impe3.pms.core.MemoryContextTest.PageLoader.Page;
import com.isotrol.impe3.samples.calculator.CalculatorComponentModule;
import com.isotrol.impe3.samples.calculator.CalculatorConnectorModule;


/**
 * Tests for EngineModelLoaderImpl.
 * @author Andres Rodriguez
 */
public class DefaultPageTest extends MemoryContextTest {
	private static final HttpHeaders EMPTY_HEADERS = new MockHttpHeaders();

	private String ct1;
	private String cg0;
	private String cg1;
	private String portalId;
	private String portalPath;
	private PageLoader loader;
	private String componentId;
	private String cipId;
	private String pageName;
	private Page page;
	private EngineModelLoader service;

	/**
	 * Portal with default page.
	 */
	@Test
	public void test() throws Exception {
		final PortalsService ps = getBean(PortalsService.class);
		ct1 = loadContentType("ct").getId();
		cg0 = loadCategory(null, 0).getId();
		cg1 = loadCategory(null, 1).getId();
		final ConnectorsService cs = getBean(ConnectorsService.class);
		final ModuleInstanceTemplateDTO template1 = cs.newTemplate(CalculatorConnectorModule.class.getName());
		template1.setName(testString());
		cs.save(template1.toModuleInstanceDTO());
		portalId = loadPortal();
		assertFalse(ps.isOfflineReady(portalId));
		portalPath = ps.getName(portalId).getName().getPath();
		componentId = loadComponent(portalId, CalculatorComponentModule.class);
		loader = new PageLoader(portalId);
		page = loader.create(PageClass.DEFAULT);
		page.putComponents();
		page.save();
		page.layout();
		service = getBean(EngineModelLoader.class);
		assertTrue(ps.isOfflineReady(portalId));
		processOffline(PathSegments.of(false, portalId), null);
		processOffline(PathSegments.of(false, portalId, "ct", "1"), null);
	}

	/** Offline model. */
	public void offlineModel() throws PMSException, IOException {
		final EngineModel model = service.getOffline(EnvironmentManager.NAME);
		assertEquals(EngineMode.OFFLINE, model.getMode());
		assertNotNull(model);
		assertNotNull(model.getCategories());
		assertNotNull(model.getContentTypes());
		assertEquals(1, model.getContentTypes().size());
		assertTrue(model.getContentTypes().containsKey(fromString(ct1)));
		assertTrue(model.getCategories().containsKey(fromString(cg0)));
	}

	private void out(Response response) throws Exception {
		System.out.println("------------------------------------------------------------------");
		((StreamingOutput) response.getEntity()).write(System.out);
		System.out.println("------------------------------------------------------------------");
	}

	private DefaultOfflineEngine getOffline() throws Exception {
		final EngineModel model = service.getOffline(EnvironmentManager.NAME);
		return new DefaultOfflineEngine(model);
	}

	private DefaultOnlineEngine getOnline() throws Exception {
		final EngineModel model = service.getOnline(EnvironmentManager.NAME);
		return new DefaultOnlineEngine(model);
	}

	/** Preview. */
	private void preview(DefaultOfflineEngine engine, PageLoc page) throws Exception {
		Response response = engine.getPreview(UUID.fromString(page.getPortalId()), UUID.fromString(page.getId()),
			new Locale("es"));
		out(response);
	}

	/** Preview. */
	private void preview(PageLoc page) throws Exception {
		preview(getOffline(), page);
	}

	private void process(Engine engine, PathSegments path, Multimap<String, String> params) throws Exception {
		Response response = engine.process(path, EMPTY_HEADERS, RequestContexts.http()).getResponse();
		out(response);
	}

	private void processOffline(PathSegments path, Multimap<String, String> params) throws Exception {
		System.out.println("OFFLINE REQUEST");
		process(getOffline(), path, params);
	}

	private void processOnline(PathSegments path, Multimap<String, String> params) throws Exception {
		System.out.println("ONLINE REQUEST");
		process(getOnline(), path, params);
	}

	/** Offline request. */
	public void offlineRequest() throws Exception {
		final PathSegments path = PathSegments.of(true, portalId, pageName);
		final Multimap<String, String> params = ArrayListMultimap.create();
		params.put("a", "1");
		params.put("b", "2");
		params.put("op", "add");
		processOffline(path, params);
	}

	/** Template. */
	public void template() throws Exception {
		final Page template = loader.create(PageClass.TEMPLATE);
		template.setName(testString());
		template.putComponents();
		template.save();
		final String space = template.getSpaceId();
		assertTrue(template.getLayout().getFrames().isEmpty());
		assertFalse(template.isCIPinLayout(space));
		template.layout();
		assertFalse(template.getLayout().getFrames().isEmpty());
		assertTrue(template.isCIPinLayout(space));
		preview(template.getLoc());
		// Create a new page based on the template
		final Page page = loader.create(PageClass.SPECIAL);
		page.setName(testString());
		page.putComponents();
		page.setTemplate(template.getLoc().getId());
		page.save();
		assertNotNull(page.getDTO().getTemplate());
		assertFalse(page.getLayout().getFrames().isEmpty());
		page.layout();
		assertFalse(page.getLayout().getFrames().isEmpty());
		preview(page.getLoc());
	}

	/** Offline model. */
	public void onlineModel() throws PMSException, IOException {
		publish();
		final EngineModel model = service.getOnline(EnvironmentManager.NAME);
		assertEquals(EngineMode.ONLINE, model.getMode());
		assertNotNull(model);
		assertNotNull(model.getCategories());
		assertNotNull(model.getContentTypes());
		assertEquals(1, model.getContentTypes().size());
		assertTrue(model.getContentTypes().containsKey(fromString(ct1)));
		assertTrue(model.getCategories().containsKey(fromString(cg0)));
	}

	/** Online request. */
	public void onlineRequest() throws Exception {
		publish();
		final PathSegments path = PathSegments.of(true, portalPath, pageName);
		final Multimap<String, String> params = ArrayListMultimap.create();
		params.put("a", "1");
		params.put("b", "2");
		params.put("op", "add");
		processOnline(path, params);
	}

}
