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
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.isotrol.impe3.api.Category;
import com.isotrol.impe3.api.ContentKey;
import com.isotrol.impe3.api.ContentType;
import com.isotrol.impe3.api.Device;
import com.isotrol.impe3.api.EngineMode;
import com.isotrol.impe3.api.NavigationKey;
import com.isotrol.impe3.api.PageKey;
import com.isotrol.impe3.api.PathSegments;
import com.isotrol.impe3.api.Route;
import com.isotrol.impe3.api.URIGenerator;
import com.isotrol.impe3.core.Engine;
import com.isotrol.impe3.core.EngineModel;
import com.isotrol.impe3.core.PortalModel;
import com.isotrol.impe3.core.engine.DefaultOfflineEngine;
import com.isotrol.impe3.core.engine.DefaultOnlineEngine;
import com.isotrol.impe3.core.engine.RequestContexts;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.connector.ConnectorsService;
import com.isotrol.impe3.pms.api.minst.ModuleInstanceTemplateDTO;
import com.isotrol.impe3.pms.api.page.ComponentFrameDTO;
import com.isotrol.impe3.pms.api.page.FrameDTO;
import com.isotrol.impe3.pms.api.page.LayoutDTO;
import com.isotrol.impe3.pms.api.page.LayoutItemDTO;
import com.isotrol.impe3.pms.api.page.PageClass;
import com.isotrol.impe3.pms.api.page.PageLoc;
import com.isotrol.impe3.pms.api.page.PagesService;
import com.isotrol.impe3.pms.api.portal.PortalsService;
import com.isotrol.impe3.pms.core.EnvironmentManager;
import com.isotrol.impe3.pms.core.MemoryContextTest;
import com.isotrol.impe3.pms.core.MemoryContextTest.PageLoader;
import com.isotrol.impe3.pms.core.MemoryContextTest.PageLoader.Page;
import com.isotrol.impe3.samples.calculator.CalculatorComponentModule;
import com.isotrol.impe3.samples.calculator.CalculatorConnectorModule;


/**
 * Tests for EngineModelLoaderImpl.
 * @author Andres Rodriguez
 */
public class EngineModelLoaderImplTest extends MemoryContextTest {
	private static final HttpHeaders EMPTY_HEADERS = new MockHttpHeaders();

	private static String ct1;
	private static String cg0;
	private static String cg1;
	private static String portalId;
	private static String portalPath;
	private static PageLoader loader;
	private static String componentId;
	private static String cipId;
	private static String pageName;
	private static PageLoc page;
	private static EngineModelLoader service;

	/**
	 * Class set up.
	 * @throws PMSException
	 */
	@BeforeClass
	public static void load() throws PMSException {
		ct1 = loadContentType().getId();
		cg0 = loadCategory(null, 0).getId();
		cg1 = loadCategory(null, 1).getId();
		final ConnectorsService cs = getBean(ConnectorsService.class);
		final ModuleInstanceTemplateDTO template1 = cs.newTemplate(CalculatorConnectorModule.class.getName());
		template1.setName(testString());
		cs.save(template1.toModuleInstanceDTO());
		portalId = loadPortal();
		portalPath = getBean(PortalsService.class).getName(portalId).getName().getPath();
		componentId = loadComponent(portalId, CalculatorComponentModule.class);
		loader = new PageLoader(portalId);
		pageName = testString();
		page = loader.loadSpecial(pageName);
		service = getBean(EngineModelLoader.class);
		LayoutDTO dto = getBean(PagesService.class).getLayout(page);
		assertNotNull(dto);
		for (LayoutItemDTO item : dto.getItems()) {
			if (cipId == null) {
				cipId = item.getId();
			}
		}
		List<FrameDTO> frames = dto.getFrames();
		assertNotNull(frames);
		assertTrue(frames.isEmpty());
		frames.add(new ComponentFrameDTO(cipId));
		dto = getBean(PagesService.class).setLayout(page, frames);
		assertTrue(getBean(PagesService.class).isCIPinLayout(page, cipId));
		// Create a template

	}

	/** Offline model. */
	@Test
	public void offlineModel() throws PMSException, IOException {
		final EngineModel model = service.getOffline(EnvironmentManager.NAME);
		assertEquals(EngineMode.OFFLINE, model.getMode());
		assertNotNull(model);
		assertNotNull(model.getCategories());
		assertNotNull(model.getContentTypes());
		assertEquals(1, model.getContentTypes().size());
		assertTrue(model.getContentTypes().containsKey(fromString(ct1)));
		assertTrue(model.getCategories().containsKey(fromString(cg0)));
		final UUID uuid = UUID.fromString(portalId);
		final PortalModel pm = model.getPortal(uuid);
		assertNotNull(pm);
		assertEquals(uuid, pm.getId());
		assertEquals(uuid, pm.getPortal().getId());
		final Device device = pm.getDevices().values().iterator().next();
		final Locale locale = Locale.ENGLISH;
		final URI uri = pm.getURIGenerator().getAbsoluteURI(Route.of(false, PageKey.main(), device, locale));
		assertTrue(uri.toASCIIString().toLowerCase().contains(portalId.toLowerCase()));
	}

	/** Layout. */
	@Test
	public void layout() throws Exception {
		LayoutDTO dto = getBean(PagesService.class).getLayout(page);
		for (LayoutItemDTO item : dto.getItems()) {
			System.out.println(item.getMarkup(500));
		}
		assertEquals(cipId, dto.getFrames().get(0).getComponent());
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

	/** Preview. */
	@Test
	public void preview() throws Exception {
		preview(page);
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
	@Test
	public void offlineRequest() throws Exception {
		final PathSegments path = PathSegments.of(true, portalId, pageName);
		final Multimap<String, String> params = ArrayListMultimap.create();
		params.put("a", "1");
		params.put("b", "2");
		params.put("op", "add");
		processOffline(path, params);
	}

	/** Template. */
	@Test
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

	/** Nested Template. */
	@Test
	public void nestedTemplate() throws Exception {
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
		// Create a new template based on the template
		final Page template2 = loader.create(PageClass.TEMPLATE);
		template2.setName(testString());
		template2.putComponents();
		template2.setTemplate(template.getLoc().getId());
		template2.save();
		final String space2 = template2.getSpaceId();
		assertNotNull(template2.getDTO().getTemplate());
		assertFalse(template2.getLayout().getFrames().isEmpty());
		template2.layout();
		assertFalse(template2.getLayout().getFrames().isEmpty());
		assertFalse(template2.isCIPinLayout(space));
		assertTrue(template2.isCIPinLayout(space2));
		preview(template2.getLoc());
		// Create a new page based on the template
		final Page page = loader.create(PageClass.SPECIAL);
		page.setName(testString());
		page.putComponents();
		page.setTemplate(template2.getLoc().getId());
		page.save();
		assertNotNull(page.getDTO().getTemplate());
		assertFalse(page.getLayout().getFrames().isEmpty());
		page.layout();
		assertFalse(page.getLayout().getFrames().isEmpty());
		preview(page.getLoc());
	}

	/** Offline model. */
	@Test
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
	@Test
	public void onlineRequest() throws Exception {
		publish();
		final PathSegments path = PathSegments.of(true, portalPath, pageName);
		final Multimap<String, String> params = ArrayListMultimap.create();
		params.put("a", "1");
		params.put("b", "2");
		params.put("op", "add");
		processOnline(path, params);
	}

	@Test
	public void uri() {
		final EngineModel model = service.getOffline(EnvironmentManager.NAME);
		Category c1 = model.getCategories().get(UUID.fromString(cg1));
		NavigationKey nk = NavigationKey.category(c1);
		PageKey pk = PageKey.navigation(nk);
		Route r = Route.of(false, pk, null, null);
		URI uri = model.getPortal(UUID.fromString(portalId)).getURIGenerator().getURI(r);
		assertNotNull(uri);
		System.out.println(uri.toASCIIString());
		ContentType ct = model.getContentTypes().get(UUID.fromString(ct1));
		ContentKey ck = ContentKey.of(ct, "2");
		r = Route.of(false, PageKey.content(ck), null, null);
		final URIGenerator g = model.getPortal(UUID.fromString(portalId)).getURIGenerator();
		uri = g.getURI(r);
		assertNotNull(uri);
		System.out.println(uri.toASCIIString());
	}

}
