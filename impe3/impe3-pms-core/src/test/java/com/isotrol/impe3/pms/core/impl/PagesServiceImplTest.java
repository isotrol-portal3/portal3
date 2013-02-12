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

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.isotrol.impe3.pms.api.Inherited;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.page.ComponentInPageTemplateDTO;
import com.isotrol.impe3.pms.api.page.ComponentKey;
import com.isotrol.impe3.pms.api.page.DefaultPagesDTO;
import com.isotrol.impe3.pms.api.page.PageClass;
import com.isotrol.impe3.pms.api.page.PageLoc;
import com.isotrol.impe3.pms.api.page.PageSelDTO;
import com.isotrol.impe3.pms.api.page.PageTemplateDTO;
import com.isotrol.impe3.pms.api.page.PagesService;
import com.isotrol.impe3.pms.api.page.PaletteDTO;
import com.isotrol.impe3.pms.api.page.PortalPagesLoc;
import com.isotrol.impe3.pms.core.MemoryContextTest;


/**
 * Tests for PagesServiceImpl.
 * @author Andres Rodriguez
 */
public class PagesServiceImplTest extends MemoryContextTest {
	private static PagesService service;
	private static PortalPagesLoc portal;

	/**
	 * Set up.
	 * @throws PMSException
	 */
	@BeforeClass
	public static void setUp() throws PMSException {
		service = getBean(PagesService.class);
		portal = loadPortalWithPalette();
	}

	/** Device id. */
	@Test
	public void deviceId() throws PMSException {
		assertNotNull(portal.getDeviceId());

	}

	/** Palette. */
	@Test
	public void palette() throws PMSException {
		List<PaletteDTO> palette = service.getPalette(portal);
		assertNotNull(palette);
		assertFalse(palette.isEmpty());
	}

	/** Default pages. */
	@Test
	public void defaultPages() throws PMSException {
		final DefaultPagesDTO pages = service.getDefaultPages(portal);
		assertNotNull(pages);
	}

	/** Error pages. */
	@Test
	public void errorPages() throws PMSException {
		final List<Inherited<PageSelDTO>> pages = service.getErrorPages(portal);
		assertNotNull(pages);
	}

	private static <T extends PortalPagesLoc> T checkLoc(T loc) {
		assertNotNull(loc);
		assertEquals(portal.getPortalId(), loc.getPortalId());
		assertEquals(portal.getDeviceId(), loc.getDeviceId());
		return loc;
	}

	private static ComponentInPageTemplateDTO check(ComponentInPageTemplateDTO cip, boolean isSpace) {
		assertNotNull(cip);
		assertNotNull(cip.getId());
		assertNotNull(cip.getName());
		assertTrue(isSpace == cip.isSpace());
		return cip;
	}

	private static ComponentInPageTemplateDTO newTemplate(ComponentKey key) throws PMSException {
		final ComponentInPageTemplateDTO cip = service.newComponentTemplate(portal, key);
		check(cip, false);
		return cip;
	}

	private static ComponentInPageTemplateDTO newCIP(PageTemplateDTO dto) throws PMSException {
		return check(newTemplate(service.getPalette(portal).get(0).getKey()), false);
	}

	/** Empty templates. */
	@Test
	public void empty() throws PMSException {
		for (PageClass pc : PageClass.values()) {
			new Page(pc).create();
		}
	}

	/** Special page with one component. */
	@Test
	public void special1() throws PMSException {
		final Page p = new Page(PageClass.SPECIAL);
		p.create();
		final CIP cip = p.addCIP();
		p.save();
		p.check(cip, 0);
		p.get();
		p.check(cip, 0);
		assertFalse(p.isCIPinLayout(cip));
	}

	/** Template page with two components. */
	@Test
	public void template1() throws PMSException {
		int n = service.getTemplatePages(portal).size();
		final Page p = new Page(PageClass.TEMPLATE);
		final CIP space = p.create();
		final CIP cip = p.addCIP();
		final CIP cip2 = p.addCIP(1);
		p.save();
		p.check(space, 0);
		p.check(cip, 1);
		p.check(cip2, 1, 0);
		p.get();
		p.check(space, 0);
		p.check(cip, 1);
		p.check(cip2, 1, 0);
		assertEquals(n + 1, service.getTemplatePages(portal).size());
	}

	private static final class Page {
		private final PageClass pageClass;
		private PageTemplateDTO template = null;
		private String id = null;
		private String name = null;

		Page(final PageClass pageClass) throws PMSException {
			this.pageClass = pageClass;
		}

		private PageTemplateDTO check(PageTemplateDTO dto) throws PMSException {
			checkLoc(dto);
			assertEquals(pageClass, dto.getPageClass());
			assertNotNull(dto.getComponents());
			return dto;
		}

		CIP create() throws PMSException {
			assertNull(template);
			template = check(service.newTemplate(portal, pageClass));
			name = testString();
			template.setName(name);
			if (pageClass == PageClass.TEMPLATE) {
				assertFalse(template.getComponents().isEmpty());
				return new CIP(template.getComponents().get(0), true);
			}
			assertTrue(template.getComponents().isEmpty());
			return null;
		}

		CIP addCIP(int... pos) throws PMSException {
			assertNotNull(template);
			return new CIP(template, pos);
		}

		void save() throws PMSException {
			assertNotNull(template);
			template = check(service.save(template.toPageDTO()));
			if (id == null) {
				id = template.getId();
			} else {
				assertEquals(id, template.getId());
			}
			assertEquals(name, template.getName());
		}

		void get() throws PMSException {
			assertNotNull(id);
			final PageLoc loc = new PageLoc();
			loc.setPortalId(portal.getPortalId());
			loc.setDeviceId(portal.getDeviceId());
			loc.setId(id);
			template = check(service.get(loc));
			assertEquals(id, template.getId());
			assertEquals(name, template.getName());
		}

		void check(CIP cip, int... pos) throws PMSException {
			assertNotNull(template);
			cip.check(template, pos);
		}

		boolean isCIPinLayout(CIP cip) throws PMSException {
			assertNotNull(id);
			final PageLoc loc = new PageLoc();
			loc.setPortalId(portal.getPortalId());
			loc.setDeviceId(portal.getDeviceId());
			loc.setId(id);
			return service.isCIPinLayout(loc, cip.id);
		}

	}

	private static final class CIP {
		private final String id;
		private final String name;
		private final boolean isSpace;

		CIP(ComponentInPageTemplateDTO cipt) throws PMSException {
			this.id = cipt.getId();
			this.name = cipt.getName();
			this.isSpace = cipt.isSpace();
		}

		CIP(ComponentInPageTemplateDTO cipt, boolean space) throws PMSException {
			this(PagesServiceImplTest.check(cipt, space));
		}

		CIP(PageTemplateDTO dto, int... pos) throws PMSException {
			final ComponentInPageTemplateDTO cipt = newCIP(dto);
			this.id = cipt.getId();
			this.name = testString();
			this.isSpace = cipt.isSpace();
			cipt.setName(name);
			List<ComponentInPageTemplateDTO> cips = dto.getComponents();
			if (cips == null) {
				cips = Lists.newArrayList();
				dto.setComponents(cips);
			}
			if (pos != null && pos.length > 0) {
				for (int p : pos) {
					cips = cips.get(p).getChildren();
				}
			}
			cips.add(cipt);
		}

		void check(PageTemplateDTO dto, int... pos) throws PMSException {
			List<ComponentInPageTemplateDTO> cips = dto.getComponents();
			ComponentInPageTemplateDTO cip = null;
			for (int p : pos) {
				cip = cips.get(p);
				cips = cip.getChildren();
			}
			PagesServiceImplTest.check(cip, isSpace);
			assertEquals(id, cip.getId());
			assertEquals(name, cip.getName());
		}
	}
}
