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

package com.isotrol.impe3.core.modules;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.GET;

import net.sf.derquinsej.Proxies;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.ImmutableSet;
import com.isotrol.impe3.api.component.Component;
import com.isotrol.impe3.api.component.ComponentResponse;
import com.isotrol.impe3.api.modules.Module;
import com.isotrol.impe3.api.modules.SpringSimple;


/**
 * Tests for Spring Simple Modules.
 * @author Andres Rodriguez
 */
public class SpringSimpleModuleTest extends AbstractModuleTest {
	private static final String M = "m";

	@SpringSimple
	static interface Minimum extends Module {
		ComponentBean component();
	}

	/**
	 * Test minimum module.
	 */
	@Test
	public void minimum() throws ModuleException {
		ModuleDefinition<Minimum> md = ModuleDefinition.of(Minimum.class);
		Minimum m = md.starter().start(null).getModule();
		ComponentBean c1 = m.component();
		assertNotNull(c1);
		ComponentBean c2 = m.component();
		assertTrue(c1 != c2);
	}

	public static class CompleteComponent implements Component {
		@Autowired
		private MessageConfig config;
		@Autowired
		private Runnable dependency;

		public CompleteComponent() {
		}

		public ComponentResponse execute() {
			return ComponentResponse.OK;
		}

		String getText() {
			assertNotNull(dependency);
			return config.text();
		}
	}

	@SpringSimple
	static interface Complete extends Module {
		CompleteComponent component();

		void config(MessageConfig config);

		void dep(Runnable r);
	}

	private static <T extends Complete> T start(Class<T> type, String text) throws ModuleException {
		ModuleDefinition<T> md = ModuleDefinition.of(type);
		return md.starter().put("config", AbstractModuleTest.config(text))
			.put("dep", Proxies.alwaysNull(Runnable.class)).start(null).getModule();
	}

	/**
	 * Test minimum module.
	 */
	@Test
	public void complete() throws ModuleException {
		Complete m = start(Complete.class, M);
		Assert.assertEquals(M, m.component().getText());
	}

	public static class ActionBean {
		public ActionBean() {
		}

		@GET
		public String get() {
			return "get";
		}
	}

	@SpringSimple
	static interface ActionModule extends Complete {
		ActionBean action();
	}

	/**
	 * Test action module.
	 */
	@Test
	public void action() throws ModuleException {
		Complete m = start(Complete.class, M);
		assertEquals(M, m.component().getText());
		assertTrue(ModuleDefinition.of(ActionModule.class).getActions().equals(ImmutableSet.of("action")));
	}

}
