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

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.isotrol.impe3.pms.api.Inherited;
import com.isotrol.impe3.pms.api.PMSException;
import com.isotrol.impe3.pms.api.page.PageLoc;
import com.isotrol.impe3.pms.api.page.PageSelDTO;
import com.isotrol.impe3.pms.core.MemoryContextTest;
import com.isotrol.impe3.pms.core.MemoryContextTest.PageLoader.Page;


/**
 * Tests for portal pages inheritance.
 * @author Andres Rodriguez
 */
public class PagesInheritanceTest extends MemoryContextTest {
	private static final String N1 = "Name1";
	private static final String N2 = "Name2";
	
	private String p1;
	private String p2;
	private PageLoader loader1;
	private PageLoader loader2;

	/** Set up. */
	@Before
	public void setUp() throws PMSException {
		p1 = loadPortalWithPalette().getPortalId();
		p2 = loadPortal(p1);
		loader1 = new PageLoader(p1);
		loader2 = new PageLoader(p2);
	}

	private void check(PageLoader loader, int own, int inherited) throws PMSException {
		List<Inherited<PageSelDTO>> list = loader.getSpecialPages();
		assertEquals(own+inherited, list.size());
		int o = 0;
		int i = 0;
		for (Inherited<PageSelDTO> dto : list) {
			if (dto.isInherited()) i++;
			else o++;
		}
		assertEquals(own, o);
		assertEquals(inherited, i);
	}
	
	private void check(int o1, int i1, int o2, int i2) throws PMSException {
		check(loader1, o1, i1);
		check(loader2, o2, i2);
	}
	

	/** Test. */
	@Test
	public void test() throws PMSException {
		check(0, 0, 0, 0);
		PageLoc pg1 = loader1.loadSpecial(N1);
		check(1, 0, 0, 1);
		loader2.loadSpecial(N2);
		check(1, 0, 1, 1);
		loader1.delete(pg1);
		check(0, 0, 1, 0);
		pg1 = loader1.loadSpecial(N1);
		check(1, 0, 1, 1);
		loader1.get(pg1).save();
		check(1, 0, 1, 1);
		PageLoc pg1_2 = new PageLoc(pg1);
		pg1_2.setPortalId(p2);
		loader2.get(pg1_2).save();
		check(1, 0, 2, 0);
		PageLoc pg2 = loader1.loadSpecial();
		check(2, 0, 2, 1);
		PageLoc pg2_2 = new PageLoc(pg2);
		pg2_2.setPortalId(p2);
		loader2.get(pg2_2).layout();
		check(2, 0, 3, 0);
	}

	/** Test. */
	@Test
	public void test2() throws PMSException {
		PageLoc pl1 = loader1.loadSpecial(N1);
		loader2.loadSpecial(N2);
		Page pg1 = loader1.get(pl1);
		pg1.save();
	}

}
